package com.example.pokemon.core.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.example.pokemon.BuildConfig
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val imageModule = module {
    single { provideImageLoader(get(), get()) }
}

private fun provideImageLoader(
    context: Context,
    okHttpClient: OkHttpClient
): ImageLoader {
    return ImageLoader.Builder(context)
        // OkHttp client for network requests
        .okHttpClient {
            okHttpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()
        }
        // Memory Cache configuration
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25) // Use 25% of app's available memory
                .build()
        }
        // Disk Cache configuration
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizeBytes(50 * 1024 * 1024) // 50 MB
                .build()
        }
        // Cache policies
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        // Enable crossfade animation
        .crossfade(true)
        .crossfade(300)
        // Debug logging in debug builds
        .apply {
            if (BuildConfig.DEBUG) {
                logger(DebugLogger())
            }
        }
        .build()
}
