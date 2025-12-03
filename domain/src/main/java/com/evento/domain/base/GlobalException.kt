package com.evento.domain.base

sealed class GlobalException(message: String): Exception(message) {
    data class Network(val msg: String) : GlobalException(msg)
    data class Http(val code: Int, val msg: String) : GlobalException(msg)
    data class Unknown(val msg: String) : GlobalException(msg)
}