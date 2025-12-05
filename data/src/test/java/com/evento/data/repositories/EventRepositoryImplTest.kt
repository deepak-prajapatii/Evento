package com.evento.data.repositories

import com.evento.data.remote.responsebody.BaseResponseDto
import com.evento.data.remote.responsebody.EventDto
import com.evento.data.remote.responsebody.TimeSlotDto
import com.evento.data.remote.service.EventService
import com.evento.domain.base.Either
import com.evento.domain.base.ErrorType
import com.evento.domain.base.GlobalException
import com.evento.domain.entities.Event
import com.evento.domain.entities.TimeSlot
import com.evento.domain.requestbody.CreateEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class EventRepositoryImplTest {

    private val eventService: EventService = mockk()
    private val repository = EventRepositoryImpl(eventService)

    @Test
    fun `getBookedEvents emits Success when api returns success response`() = runTest {
        val dtoList = listOf(
            EventDto(
                slotId = "SLOT001",
                name = "Morning Slot",
                startTime = "09:00",
                endTime = "10:00",
                customerName = "Deepak",
                phoneNumber = "9821334206",
                date = 1765159876000L
            )
        )

        val body = BaseResponseDto(
            status = "success",
            message = "Fetched successfully",
            data = dtoList
        )

        coEvery { eventService.getBookedEvents() } returns Response.success(body)

        val result = repository.getBookedEvents().first()

        assertTrue(result is Either.Success)
        val success = result as Either.Success<List<Event>>
        assertEquals(1, success.data.size)
        assertEquals("SLOT001", success.data[0].slotId) // assuming your toDomainList maps id
    }


    @Test
    fun `getBookedEvents emits Failure with network error on UnknownHostException`() = runTest {
        coEvery { eventService.getBookedEvents() } throws UnknownHostException("No internet")

        val result = repository.getBookedEvents().first()

        assertTrue(result is Either.Failure)
        val failure = result as Either.Failure
        val ex = failure.exception
        assertEquals(ErrorType.NETWORK_ERROR, ex.type)
        assertEquals("Please check your internet connection", ex.errorMessage)
    }

    @Test
    fun `getAvailableSlots emits Success when api returns success response`() = runTest {
        val dtoList = listOf(
            TimeSlotDto(
                slotId = "SLOT001",
                name = "Morning Slot",
                startTime = "09:00",
                endTime = "10:00"
            )
        )

        val body = BaseResponseDto(
            status = "SUCCESS",
            message = "ok",
            data = dtoList
        )

        coEvery { eventService.getAvailableSlots() } returns Response.success(body)

        val result = repository.getAvailableSlots().first()

        assertTrue(result is Either.Success)
        val success = result as Either.Success<List<TimeSlot>>
        assertEquals(1, success.data.size)
        assertEquals("SLOT001", success.data[0].slotId)
    }

    @Test
    fun `createEvent emits Success when api returns success`() = runTest {
        val dto = EventDto(
            slotId = "SLOT001",
            name = "Morning Slot",
            startTime = "09:00",
            endTime = "10:00",
            customerName = "Deepak",
            phoneNumber = "9821334206",
            date = 1765159876000L
        )

        val body = BaseResponseDto(
            status = "success",
            message = "Created",
            data = dto
        )

        coEvery { eventService.createEvent(any()) } returns Response.success(body)

        val createEvent = CreateEvent(
            customerName = "Deepak",
            phoneNumber = "9821334206",
            slotId = "SLOT001"
        )

        val result = repository.createEvent(createEvent).first()

        assertTrue(result is Either.Success)
        val success = result as Either.Success<Event>
        assertEquals("SLOT001", success.data.slotId)
    }

    @Test
    fun `createEvent emits Failure with HTTP_ERROR on non-2xx response`() = runTest {
        val errorJson = """{"status":"error","message":"Bad request"}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaType())

        coEvery { eventService.createEvent(any()) } returns Response.error(
            400,
            errorBody
        )

        val createEvent = CreateEvent(
            customerName = "Deepak",
            phoneNumber = "9821334206",
            slotId = "SLOT001"
        )

        val result = repository.createEvent(createEvent).first()

        assertTrue(result is Either.Failure)
        val failure = result as Either.Failure
        val ex = failure.exception
        assertEquals(ErrorType.HTTP_ERROR, ex.type)
        // because safeApiCall uses the raw errorBody string as errorMessage
        assertTrue(ex.errorMessage.contains("Bad request"))
    }
}
