package com.evento.ui.slotselection

import com.evento.domain.base.Either
import com.evento.domain.base.ErrorType
import com.evento.domain.base.GlobalException
import com.evento.domain.entities.TimeSlot
import com.evento.domain.usecase.GetAvailableSlotsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SlotSelectionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getAvailableSlotsUseCase: GetAvailableSlotsUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getAvailableSlotsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads available slots and updates state on success`() = runTest(testDispatcher) {
        val fakeSlots = listOf(
            TimeSlot(
                slotId = "SLOT001",
                name = "Morning Slot",
                startTime = "09:00",
                endTime = "10:00"
            )
        )

        coEvery { getAvailableSlotsUseCase.execute(Unit) } returns flowOf(
            Either.success(fakeSlots)
        )

        val viewModel = SlotSelectionViewModel(getAvailableSlotsUseCase)
        advanceUntilIdle()

        val state: SlotSelectionUIState = viewModel.state.value

        assertFalse(state.isLoading)
        assertEquals(fakeSlots, state.timeSlots)
        assertNull(state.uiErrorType)
        assertNull(state.uiErrorMessage)

        coVerify(exactly = 1) { getAvailableSlotsUseCase.execute(Unit) }
    }

    @Test
    fun `init stops loading and sets error state on failure`() = runTest(testDispatcher) {
        val exception = GlobalException(
            errorMessage = "Failed to fetch slots",
            type = ErrorType.API_ERROR
        )

        coEvery { getAvailableSlotsUseCase.execute(Unit) } returns flowOf(
            Either.failure(exception)
        )

        val viewModel = SlotSelectionViewModel(getAvailableSlotsUseCase)
        advanceUntilIdle()

        val state: SlotSelectionUIState = viewModel.state.value

        assertFalse(state.isLoading)
        assertTrue(state.timeSlots.isEmpty())
        assertEquals(ErrorType.API_ERROR, state.uiErrorType)
        assertEquals("Failed to fetch slots", state.uiErrorMessage)

        coVerify(exactly = 1) { getAvailableSlotsUseCase.execute(Unit) }
    }

    @Test
    fun `state should become loading before slots result is emitted`() = runTest(testDispatcher) {
        coEvery { getAvailableSlotsUseCase.execute(Unit) } returns flow {
            delay(10)
            emit(Either.success(emptyList()))
        }

        val viewModel = SlotSelectionViewModel(getAvailableSlotsUseCase)

        runCurrent()

        val loadingState: SlotSelectionUIState = viewModel.state.value
        assertTrue(loadingState.isLoading)

        advanceUntilIdle()

        val finalState: SlotSelectionUIState = viewModel.state.value
        assertFalse(finalState.isLoading)
    }

    @Test
    fun `onSlotSelected should update selectedTimeSlot in state`() = runTest(testDispatcher) {
        coEvery { getAvailableSlotsUseCase.execute(Unit) } returns flowOf(
            Either.success(emptyList())
        )

        val viewModel = SlotSelectionViewModel(getAvailableSlotsUseCase)
        advanceUntilIdle()

        val selectedSlot = TimeSlot(
            slotId = "SLOT002",
            name = "Evening Slot",
            startTime = "18:00",
            endTime = "19:00"
        )

        viewModel.onSlotSelected(selectedSlot)

        val state: SlotSelectionUIState = viewModel.state.value

        assertEquals(selectedSlot, state.selectedTimeSlot)
    }
}