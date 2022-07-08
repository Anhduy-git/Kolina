package com.example.recipeapp.models

data class LikePost(
    val dish_id: String = "",
    val list_user_like: ArrayList<String>? = null,
    var post_id: String = ""
)