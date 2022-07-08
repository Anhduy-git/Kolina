package com.example.recipeapp.utils

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.animation.*
import android.widget.ImageView

class AnimationUtil {
    companion object {
        private val ANIMATION_DURATION = 500
        private var isAnimationStart: Boolean = false
        fun translateAnimation(
            viewAnimation: ImageView, startView: View,
            endView: View, animationListener: Animation.AnimationListener) {

            startView.isDrawingCacheEnabled = true
            val cache: Bitmap = startView.drawingCache
            if (cache == null) {
                return
            }
            val bitmap: Bitmap = Bitmap.createBitmap(cache)
            startView.isDrawingCacheEnabled = false
            viewAnimation.setImageBitmap(bitmap)

            val startViewWidthCenter: Float =  startView.width.toFloat() / 2f
            val startViewHeightCenter: Float =  startView.height.toFloat() / 2f

            val endViewWidthCenter: Float =  endView.width.toFloat() * 0.75f
            val endViewHeightCenter: Float =  startView.height.toFloat() / 2f

            val startPos = IntArray(2)
            startView.getLocationOnScreen(startPos)



            val endPos = IntArray(2)
            endView.getLocationOnScreen(endPos)


            val fromX: Float = startPos[0].toFloat()
            val toX: Float = endPos[0].toFloat() + 60
//            + endViewWidthCenter - startViewWidthCenter

//            Log.d("animation", startPos[0].toString())
//            Log.d("animation", fromX.toString())
//            Log.d("animation", toX.toString())

            val fromY: Float = startPos[1].toFloat() - startViewHeightCenter
            val toY: Float = endPos[1].toFloat() - 40
//            - endViewHeightCenter + startViewHeightCenter




            val animationSet: AnimationSet = AnimationSet(true)
            animationSet.interpolator = AccelerateInterpolator()

            val animationDuration = 100

            val startScaleAnimation = ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, startViewWidthCenter, startViewHeightCenter)
            startScaleAnimation.startOffset = 0
            startScaleAnimation.duration = animationDuration.toLong()

            val translateAnimation: TranslateAnimation = TranslateAnimation(fromX, toX, fromY, toY)
            translateAnimation.startOffset = animationDuration.toLong()
            translateAnimation.duration = ANIMATION_DURATION.toLong()

            val translateScaleAnimation: ScaleAnimation = ScaleAnimation(1.0f, 0f, 1.0f, 0f, toX, toY)
            translateScaleAnimation.startOffset = animationDuration.toLong()
            translateScaleAnimation.duration = ANIMATION_DURATION.toLong()

            animationSet.addAnimation(startScaleAnimation)
            animationSet.addAnimation(translateAnimation)
            animationSet.addAnimation(translateScaleAnimation)

            if (isAnimationStart) {

                viewAnimation.clearAnimation()
                if (animationListener != null) {
                    animationListener.onAnimationEnd(null)
                }
            }
            viewAnimation.startAnimation(animationSet)

            animationSet.setAnimationListener (object :Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation) {
                    isAnimationStart = true
                    viewAnimation.visibility = View.VISIBLE
                    if (animationListener != null) {

                        animationListener.onAnimationStart(animation)
                    }

                }
                override fun onAnimationEnd(animation: Animation) {
                    viewAnimation.visibility = View.GONE
                    startView.visibility = View.VISIBLE
                    if (animationListener != null) {

                        animationListener.onAnimationEnd(animation)
                    }
                    isAnimationStart = false
                }

                override fun onAnimationRepeat(animation: Animation) {

                    if (animationListener != null) {
                        animationListener.onAnimationRepeat(animation)
                    }

                }


            })







        }
    }
}
