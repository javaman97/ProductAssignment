package com.aman.swipeassignment.utils

sealed class ResponseState<out T> {
    data object Loading : ResponseState<Nothing>()
    data class Success<out T>(val data: T) : ResponseState<T>()
    data class Failure(val error: String?) : ResponseState<Nothing>()
}