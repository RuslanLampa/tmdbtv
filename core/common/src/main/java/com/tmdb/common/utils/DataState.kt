package com.tmdb.common.utils

sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val errorModel: ErrorModel) : DataState<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(errorModel: ErrorModel) = Error(errorModel)
    }
}