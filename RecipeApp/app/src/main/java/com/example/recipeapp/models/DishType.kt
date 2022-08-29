package com.example.recipeapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DishType(
        val name: String = "",
        val image: Int,
        val color: Int
) : Parcelable
