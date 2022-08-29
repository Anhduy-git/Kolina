package com.example.recipeapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText

class MSPButton(context: Context, attributeSet: AttributeSet)
    : AppCompatButton(context, attributeSet) {
    init {
        applyFont()
    }
    private fun applyFont() {
        val boldTypeFace: Typeface =
            Typeface.createFromAsset(context.assets, "Roboto-Bold.ttf")
        setTypeface(boldTypeFace)
    }
}