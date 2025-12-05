package com.evento.data.repositories

import com.evento.data.remote.responsebody.BaseResponseDto
import com.evento.domain.base.Either
import com.evento.domain.base.ErrorType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class BaseRepositoryTest {

    private val repo = TestRepository()

    @Test
    fun `safeApiCall returns success when body status is success and data non-null`() = runTest {
        val body = BaseResponseDto(
            status = "success",
            message = "ok",
            data = 123
        )

        val result = repo.call { Response.success(body) }

        assertTrue(result is Either.Success)
        assertEquals(123, (result as Either.Success).data)
    }

    @Test
    fun `safeApiCall returns failure with API_ERROR when status is not success`() = runTest {
        val body = BaseResponseDto(
            status = "error",
            message = "Something failed",
            data = null
        )

        val result = repo.call { Response.success(body) }

        assertTrue(result is Either.Failure)
        val ex = (result as Either.Failure).exception
        assertEquals(ErrorType.API_ERROR, ex.type)
        assertEquals("Something failed", ex.errorMessage)
    }

    @Test
    fun `safeApiCall returns NETWORK_ERROR on UnknownHostException`() = runTest {
        val result = repo.call<Int> { throw UnknownHostException("No internet") }

        assertTrue(result is Either.Failure)
        val ex = (result as Either.Failure).exception
        assertEquals(ErrorType.NETWORK_ERROR, ex.type)
        assertEquals("Please check your internet connection", ex.errorMessage)
    }
}
