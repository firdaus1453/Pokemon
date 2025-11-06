package com.example.pokemon.profile.data

import com.example.pokemon.core.database.dao.UserDao
import com.example.pokemon.core.database.entity.UserEntity
import com.example.pokemon.core.domain.SessionStorage
import com.example.pokemon.profile.domain.ProfileRepository

class ProfileRepositoryImpl(
    private val userDao: UserDao,
    private val sessionStorage: SessionStorage
) : ProfileRepository {

    override suspend fun getCurrentUser(): UserEntity? {
        val authInfo = sessionStorage.get() ?: return null
        return userDao.getUserById(authInfo.userId)
    }

    override suspend fun logout() {
        sessionStorage.set(null)
    }
}
