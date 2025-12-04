package com.evento.data.remote.responsebody

data class BaseResponseDto<out T>(
    val status: String,
    val message: String,
    val data: T
)