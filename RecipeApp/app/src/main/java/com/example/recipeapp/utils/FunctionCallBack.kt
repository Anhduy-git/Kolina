package com.example.recipeapp.utils

interface FunctionCallBackIsLiked {
    fun onCallbackIsLiked(isLiked: Boolean)
}

interface FunctionCallBackNumLiked {
    fun onCallbackNumLiked(numLiked: Int)
}

interface FunctionCallBackIsDisliked {
    fun onCallbackIsDisliked(isDisliked: Boolean)
}

interface FunctionCallBackNumDisliked {
    fun onCallbackNumDisliked(numDisliked: Int)
}
interface FunctionCallBackNumComment {
    fun onCallbackNumComment(numComment: Int)
}