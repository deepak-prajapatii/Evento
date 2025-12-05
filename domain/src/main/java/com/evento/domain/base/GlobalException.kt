package com.evento.domain.base

/**
 * A unified error model for handling all failures coming from API/network/repository layers.
 */
data class GlobalException(
    val code: Int? = null,
    val errorMessage: String,
    override val cause: Throwable? = null,
    val details: Map<String, Any>? = null,
    val type: ErrorType = ErrorType.UNKNOWN
) : Exception(errorMessage, cause)
