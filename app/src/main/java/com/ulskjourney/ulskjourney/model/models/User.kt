package com.ulskjourney.ulskjourney.model.models

data class User(
        val id: Int = 0,
        val login: String,
        val password: String,
        val name: String,
)