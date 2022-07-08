package com.example.recipeapp.models

data class CommentPost(
    val dish_id: String = "",
    val list_comment: ArrayList<Comment>? = null,
    var post_id: String = ""
)