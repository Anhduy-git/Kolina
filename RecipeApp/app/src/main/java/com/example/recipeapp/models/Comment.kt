package com.example.recipeapp.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Comment(
    val userId: String = "",
    val userImage: String = "",
    val userFullName: String = "",
    val content: String = "",
    val date: Long = 0,
    var comment_id: String = ""
)
