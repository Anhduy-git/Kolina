package com.example.recipeapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MSPTextViewBold(context: Context, attributeSet: AttributeSet)
    : AppCompatTextView(context, attributeSet) {

    init{
        applyFont()
    }
    private fun applyFont() {
        val boldTypeFace: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(boldTypeFace)
    }
}