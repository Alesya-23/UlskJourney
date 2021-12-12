package com.ulskjourney.ulskjourney.utils

import com.ulskjourney.ulskjourney.model.models.Mark
import com.ulskjourney.ulskjourney.model.models.User

interface IFirebasePostService {
    suspend fun getListMarks(): List<Mark>

    suspend fun getMark(id: Int): Mark

    suspend fun getListUsers(): List<User>

    suspend fun getUser(id: String): User
}