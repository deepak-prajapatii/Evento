package com.evento.data.repositories

import com.evento.data.remote.responsebody.BaseResponseDto
import com.evento.domain.base.Either
import retrofit2.Response

class TestRepository : BaseRepository() {
    suspend fun <T> call(api: suspend () -> Response<BaseResponseDto<T>>): Either<T> =
        safeApiCall(api)
}