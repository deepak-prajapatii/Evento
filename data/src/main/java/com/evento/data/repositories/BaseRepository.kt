package com.evento.data.repositories

import com.evento.data.remote.responsebody.BaseResponseDto
import com.evento.domain.base.Either
import com.evento.domain.base.ErrorType
import com.evento.domain.base.GlobalException
import retrofit2.Response

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseRepository {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<BaseResponseDto<T>>
    ): Either<T> {
        return try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                if (body == null) {
                    return Either.failure(
                        GlobalException(
                            code = response.code(),
                            errorMessage = "Empty response from server",
                            type = ErrorType.API_ERROR
                        )
                    )
                }

                if (!body.status.equals("success", ignoreCase = true)) {
                    return Either.failure(
                        GlobalException(
                            code = response.code(),
                            errorMessage = body.message.ifBlank { "Something went wrong" },
                            type = ErrorType.API_ERROR
                        )
                    )
                }

                val data = body.data
                return if (data != null) {
                    Either.success(data)
                } else {
                    Either.failure(
                        GlobalException(
                            code = response.code(),
                            errorMessage = body.message.ifBlank { "No data available" },
                            type = ErrorType.EMPTY_DATA
                        )
                    )
                }
            } else {
                val errorMsg = response.errorBody()?.string()
                    ?.takeIf { it.isNotBlank() }
                    ?: response.message().ifBlank { "HTTP ${response.code()}" }

                return Either.failure(
                    GlobalException(
                        code = response.code(),
                        errorMessage = errorMsg,
                        type = ErrorType.HTTP_ERROR
                    )
                )
            }
        } catch (e: UnknownHostException) {
            Either.failure(
                GlobalException(
                    errorMessage = "Please check your internet connection",
                    cause = e,
                    type = ErrorType.NETWORK_ERROR
                )
            )
        } catch (e: SocketTimeoutException) {
            Either.failure(
                GlobalException(
                    errorMessage = "Request timed out, please try again",
                    cause = e,
                    type = ErrorType.TIMEOUT_ERROR
                )
            )
        } catch (e: IOException) {
            Either.failure(
                GlobalException(
                    errorMessage = "Network error, please try again",
                    cause = e,
                    type = ErrorType.NETWORK_ERROR
                )
            )
        } catch (e: HttpException) {
            Either.failure(
                GlobalException(
                    code = e.code(),
                    errorMessage = e.message().ifBlank { "HTTP ${e.code()} error" },
                    cause = e,
                    type = ErrorType.HTTP_ERROR
                )
            )
        } catch (e: Exception) {
            Either.failure(
                GlobalException(
                    errorMessage = e.message ?: "Something went wrong",
                    cause = e,
                    type = ErrorType.UNKNOWN
                )
            )
        }
    }
}
