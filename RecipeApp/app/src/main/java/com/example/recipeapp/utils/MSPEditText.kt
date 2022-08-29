package com.example.recipeapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MSPEditText(context: Context, attributeSet: AttributeSet)
    : AppCompatEditText(context, attributeSet) {
    init {
        applyFont()
    }
    private fun applyFont() {
        val regularTypeFace: Typeface =
            Typeface.createFromAsset(context.assets, "Roboto-Regular.ttf")
        setTypeface(regularTypeFace)
    }
}