package com.example.recipeapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Material(
    val name: String,
    val image: Int,
    var isSelected: Boolean = false

): Parcelable

