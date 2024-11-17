package com.tmdb.common.utils

sealed class State<out T> {

    data class Loading<out T>(val data: T?) : State<T>()
    data class Successes<out T>(val data: T) : State<T>()
    data class Error<out T>(val errorModel: ErrorModel, val data: T?) : State<T>()

    companion object {
        fun <T> loading(data: T? = null) = Loading(data)
        fun <T> successes(data: T) = Successes(data)
        fun <T> error(errorModel: ErrorModel? = null, data: T? = null) = Error(errorModel = errorModel ?: ErrorModel.unknown(), data = data)
    }

    val isLoading get() = this is Loading<*>
    val isError get() = this is Error<*>
    val errorMessage get() = when {
        this is Error -> this.errorModel.message
        else -> null
    }

    fun asSuccess(): Successes<T>? {
        return this as? Successes
    }

    fun getStateData(): T? {
        return when (this) {
            is Loading -> data
            is Successes -> data
            is Error -> data
        }
    }
}