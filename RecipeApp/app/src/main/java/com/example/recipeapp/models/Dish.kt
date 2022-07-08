package com.example.recipeapp.models

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Dish(
    val user_id: String = "",
    val user_name: String = "",
    val title: String = "",
    val description: String = "",
    val serving_size: String = "",
    val image: String? = "",
    val user_image: String? = "",
    val dish_type: String = "",
    val main_material: ArrayList<String>? = null,
    val material: ArrayList<String>? = null,
    val recipe: ArrayList<String>? = null,
    @ServerTimestamp
    val date: Date? = null,
    var dish_id: String = ""

) : Parcelable