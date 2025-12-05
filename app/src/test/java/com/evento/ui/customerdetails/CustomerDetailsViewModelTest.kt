package com.evento.ui.customerdetails

import app.cash.turbine.test
import com.evento.domain.usecase.BookEventUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import androidx.lifecycle.SavedStateHandle
import com.evento.domain.base.Either
import com.evento.domain.base.ErrorType
import com.evento.domain.base.GlobalException
import com.evento.domain.entities.Event

@OptIn(ExperimentalCoroutinesApi::class)
class CustomerDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var bookEventUseCase: BookEventUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    private val fakeEvent =
        Event(
            slotId = "SLOT001",
            name = "Morning Slot",
            startTime = "09:00",
            endTime = "10:00",
            customerName = "Deepak",
            phoneNumber = "9821334206",
            date = 1765159876000
        )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        bookEventUseCase = mockk()
        savedStateHandle = SavedStateHandle(
            mapOf(
                "slotId" to "SLOT001",
                "slotName" to "Morning Slot",
                "startTime" to "09:00",
                "endTime" to "10:00"
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should set timeSlot from saved state handle`() = runTest(testDispatcher) {
        coEvery { bookEventUseCase.execute(any()) } returns flowOf(
            Either.success(fakeEvent)
        )

        val viewModel = CustomerDetailsViewModel(savedStateHandle, bookEventUseCase)

        val state: CustomerDetailsUIState = viewModel.state.value

        assertNotNull(state.timeSlot)
        val slot = state.timeSlot!!
        assertEquals("SLOT001", slot.slotId)
        assertEquals("Morning Slot", slot.name)
        assertEquals("09:00", slot.startTime)
        assertEquals("10:00", slot.endTime)
    }

    @Test
    fun `updateCustomerName should update customerName in state`() = runTest(testDispatcher) {
        coEvery { bookEventUseCase.execute(any()) } returns flowOf(Either.success(fakeEvent))

        val viewModel = CustomerDetailsViewModel(savedStateHandle, bookEventUseCase)

        viewModel.updateCustomerName("Deepak")

        val state = viewModel.state.value
        assertEquals("Deepak", state.customerName)
    }

    @Test
    fun `updatePhoneNumber should update phoneNumber in state`() = runTest(testDispatcher) {
        coEvery { bookEventUseCase.execute(any()) } returns flowOf(Either.success(fakeEvent))

        val viewModel = CustomerDetailsViewModel(savedStateHandle, bookEventUseCase)

        viewModel.updatePhoneNumber("9821334206")

        val state = viewModel.state.value
        assertEquals("9821334206", state.phoneNumber)
    }

    @Test
    fun `bookEvent with empty fields should set name and phone errors`() = runTest(testDispatcher) {
        coEvery { bookEventUseCase.execute(any()) } returns flowOf(Either.success(fakeEvent))

        val viewModel = CustomerDetailsViewModel(savedStateHandle, bookEventUseCase)

        viewModel.bookEvent()

        val state = viewModel.state.value

        assertEquals(CustomerFormInputError.EmptyName, state.nameError)
        assertEquals(CustomerFormInputError.EmptyPhone, state.phoneError)
        assertFalse(state.isLoading)

        coVerify(exactly = 0) { bookEventUseCase.execute(any()) }
    }


    @Test
    fun `bookEvent with short phone should set phone length error`() = runTest(testDispatcher) {
        coEvery { bookEventUseCase.execute(any()) } returns flowOf(Either.success(fakeEvent))

        val viewModel = CustomerDetailsViewModel(savedStateHandle, bookEventUseCase)

        viewModel.updateCustomerName("Deepak")
        viewModel.updatePhoneNumber("12345")

        viewModel.bookEvent()

        val state = viewModel.state.value

        assertNull(state.nameError)
        assertEquals(CustomerFormInputError.InvalidPhone, state.phoneError)
        assertFalse(state.isLoading)

        coVerify(exactly = 0) { bookEventUseCase.execute(any()) }
    }

    @Test
    fun `bookEvent success should stop loading and send NavigateToEventBooking event`() =
        runTest(testDispatcher) {


            coEvery { bookEventUseCase.execute(any()) } returns flowOf(
                Either.success(fakeEvent)
            )

            val viewModel = CustomerDetailsViewModel(savedStateHandle, bookEventUseCase)

            viewModel.updateCustomerName("Deepak")
            viewModel.updatePhoneNumber("9821334206")

            viewModel.event.test {
                viewModel.bookEvent()

                advanceUntilIdle()

                val event = awaitItem()
                assertEquals(CustomerDetailsUIEvent.NavigateToEventBooking, event)

                val state = viewModel.state.value
                assertFalse(state.isLoading)

                coVerify(exactly = 1) {
                    bookEventUseCase.execute(
                        match { createEvent ->
                            createEvent.customerName == "Deepak" &&
                                    createEvent.phoneNumber == "9821334206" &&
                                    createEvent.slotId == "SLOT001"
                        }
                    )
                }

                cancelAndIgnoreRemainingEvents()
            }
        }


    @Test
    fun `bookEvent failure should stop loading set error and send CreateEventFailure event`() =
        runTest(testDispatcher) {
            val exception = GlobalException(
                errorMessage = "Booking failed",
                type = ErrorType.API_ERROR
            )

            coEvery { bookEventUseCase.execute(any()) } returns flowOf(
                Either.failure(exception)
            )

            val viewModel = CustomerDetailsViewModel(savedStateHandle, bookEventUseCase)

            viewModel.updateCustomerName("Deepak")
            viewModel.updatePhoneNumber("9821334206")

            viewModel.event.test {
                viewModel.bookEvent()

                advanceUntilIdle()

                val event = awaitItem()
                assertEquals(CustomerDetailsUIEvent.CreateEventFailure, event)

                val state = viewModel.state.value
                assertFalse(state.isLoading)
                assertEquals(ErrorType.API_ERROR.name, state.uiErrorTitle)
                assertEquals("Booking failed", state.uiErrorMessage)

                coVerify(exactly = 1) { bookEventUseCase.execute(any()) }

                cancelAndIgnoreRemainingEvents()
            }
        }
}