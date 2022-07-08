package com.example.recipeapp.models

data class DislikePost(
    val dish_id: String = "",
    val list_user_dislike: ArrayList<String>? = null,
    var post_id: String = ""
)