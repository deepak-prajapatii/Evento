package com.evento.domain.base

/**
 * A unified error model for handling all failures coming from API/network/repository layers.
 */
data class GlobalException(
    val code: Int? = null,
    val messageText: String,
    override val cause: Throwable? = null,
    val details: Map<String, Any>? = null,
    val type: ErrorType = ErrorType.UNKNOWN
) : Exception(messageText, cause) {

    enum class ErrorType {
        API_ERROR,
        NETWORK_ERROR,
        TIMEOUT_ERROR,
        HTTP_ERROR,
        EMPTY_DATA,
        UNKNOWN
    }
}
