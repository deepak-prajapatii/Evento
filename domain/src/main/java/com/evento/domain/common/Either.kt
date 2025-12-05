package com.evento.domain.common

sealed class Either<out T> {

    data class Success<out T>(val data: T) : Either<T>()
    data class Failure(val exception: GlobalException) : Either<Nothing>()

    companion object {

        fun <T> success(data: T): Either<T> = Success(data)

        fun failure(exception: GlobalException): Either<Nothing> = Failure(exception)
    }


    inline fun onSuccess(block: (T) -> Unit): Either<T> = apply { if (this is Success) block(data) }

    inline fun onFailure(block: (GlobalException) -> Unit): Either<T> = apply {
        if (this is Failure) block(exception)
    }
}