package com.tmdb.common.utils

sealed class ErrorModel(open val message: String) {

    data class Error(override val message: String) : ErrorModel(message)

    data object EmptyBody : ErrorModel("Empty body")

    companion object {
        fun create(message: String) = Error(message)
        fun unknown() = Error("Unknown error")
    }

}

fun Throwable.toErrorModel(): ErrorModel =
    this.message?.let { ErrorModel.Error(it) } ?: ErrorModel.unknown()