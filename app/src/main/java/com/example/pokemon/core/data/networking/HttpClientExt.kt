package com.example.pokemon.core.data.networking

import com.example.pokemon.BuildConfig
import com.example.pokemon.core.domain.util.DataError
import com.example.pokemon.core.domain.util.Result
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <reified T : Any, S> S.safeCall(
    crossinline block: suspend S.() -> Response<T>
): Result<T, DataError.Network> {
    val response = try {
        block()
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: ConnectException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: SocketTimeoutException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.REQUEST_TIMEOUT)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.SERIALIZATION)
    } catch (e: HttpException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.UNKNOWN)
    } catch (e: IOException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.UNKNOWN)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        return Result.Error(DataError.Network.UNKNOWN)
    }

    return response.toNetworkResult()
}

inline fun <reified T : Any> Response<T>.toNetworkResult(): Result<T, DataError.Network> {
    val code = code()

    return if (isSuccessful) {
        val body = body()
        if (body != null) {
            Result.Success(body)
        } else {
            Result.Error(DataError.Network.SERIALIZATION)
        }
    } else {
        when (code) {
            401 -> Result.Error(DataError.Network.UNAUTHORIZED)
            408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
            409 -> Result.Error(DataError.Network.CONFLICT)
            413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
            429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
            in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
            else -> Result.Error(DataError.Network.UNKNOWN)
        }
    }
}

fun constructRoute(route: String): String {
    return when {
        route.contains(BuildConfig.BASE_URL) -> route
        route.startsWith("/") -> BuildConfig.BASE_URL + route
        else -> BuildConfig.BASE_URL + "/$route"
    }
}
