package com.evento.ui.eventbooking

import com.evento.domain.common.Either
import com.evento.domain.common.ErrorType
import com.evento.domain.common.GlobalException
import com.evento.domain.entities.Event
import com.evento.domain.usecase.GetBookedEventsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class EventBookingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getBookedEventsUseCase: GetBookedEventsUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getBookedEventsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads booked events and updates state on success`() = runTest(testDispatcher) {
        val fakeEvents = listOf(
            Event(
                slotId = "SLOT001",
                name = "Morning Slot",
                startTime = "09:00",
                endTime = "10:00",
                customerName = "Deepak",
                phoneNumber = "9821334206",
                date = 1765159876000
            )
        )

        coEvery { getBookedEventsUseCase.execute(Unit) } returns flowOf(
            Either.success(fakeEvents)
        )

        val viewModel = EventBookingViewModel(getBookedEventsUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertEquals(fakeEvents, state.bookedEvents)
        assertNull(state.uiErrorType)
        assertNull(state.uiErrorMessage)

        coVerify(exactly = 1) { getBookedEventsUseCase.execute(Unit) }
    }

    @Test
    fun `init stops loading and sets error state on failure`() = runTest(testDispatcher) {
        val exception = GlobalException(
            errorMessage = "API error occurred",
            type = ErrorType.API_ERROR
        )

        coEvery { getBookedEventsUseCase.execute(Unit) } returns flowOf(
            Either.failure(exception)
        )

        val viewModel = EventBookingViewModel(getBookedEventsUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertTrue(state.bookedEvents.isEmpty())
        assertEquals(ErrorType.API_ERROR, state.uiErrorType)
        assertEquals("API error occurred", state.uiErrorMessage)

        coVerify(exactly = 1) { getBookedEventsUseCase.execute(Unit) }
    }

    @Test
    fun `state should become loading before result is emitted`() = runTest(testDispatcher) {
        coEvery { getBookedEventsUseCase.execute(Unit) } returns flow {
            delay(10) // scheduled later
            emit(Either.success(emptyList()))
        }

        val viewModel = EventBookingViewModel(getBookedEventsUseCase)

        runCurrent()

        val loadingState: EventBookingUIState = viewModel.state.value
        assertTrue(loadingState.isLoading)

        advanceUntilIdle()

        val finalState: EventBookingUIState = viewModel.state.value
        assertFalse(finalState.isLoading)
    }

}