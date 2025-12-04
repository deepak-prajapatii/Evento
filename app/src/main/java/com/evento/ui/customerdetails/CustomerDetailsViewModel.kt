package com.evento.ui.customerdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.evento.base.BaseViewModel
import com.evento.domain.entities.TimeSlot
import com.evento.domain.requestbody.CreateEvent
import com.evento.domain.usecase.BookEventUseCase
import com.evento.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bookEventUseCase: BookEventUseCase
) :
    BaseViewModel<CustomerDetailsUIState, CustomerDetailsUIEvent>(initialState = CustomerDetailsUIState()) {

    init {
        val slotId: String = savedStateHandle["slotId"] ?: ""
        val slotName: String = savedStateHandle["slotName"] ?: ""
        val startTime: String = savedStateHandle["startTime"] ?: ""
        val endTime: String = savedStateHandle["endTime"] ?: ""

        val timeSlot = TimeSlot(
            slotId = slotId,
            name = slotName,
            startTime = startTime,
            endTime = endTime
        )
        updateState { state ->
            state.copy(timeSlot = timeSlot)
        }
    }


    fun updateCustomerName(name: String) {
        updateState { state -> state.copy(customerName = name) }
    }

    fun updatePhoneNumber(number: String) {
        updateState { state -> state.copy(phoneNumber = number) }
    }

    fun bookEvent(){
        var nameError: String? = null
        var phoneError: String? = null

        if (currentState.customerName.isBlank()) {
            nameError = "Customer name cannot be empty"
        }

        if (currentState.phoneNumber.isBlank()) {
            phoneError = "Phone number cannot be empty"
        } else if (currentState.phoneNumber.length < AppConstants.MAX_PHONE_NUMBER_LENGTH) {
            phoneError = "Phone number must be 10 digits"
        }

        if (nameError != null || phoneError != null) {
            updateState {
                it.copy(
                    nameError = nameError,
                    phoneError = phoneError
                )
            }
            return
        }

        val createEvent = CreateEvent(
            customerName = currentState.customerName,
            phoneNumber = currentState.phoneNumber,
            slotId = currentState.timeSlot?.slotId?:""
        )
        viewModelScope.launch {
            updateState { state -> state.copy(isLoading = true) }
            bookEventUseCase.execute(createEvent)
                .collect { either ->
                    either.onSuccess {
                        updateState { state -> state.copy(isLoading = false) }
                        sendEvent(CustomerDetailsUIEvent.NavigateToEventBooking)
                    }.onFailure {
                        updateState { state -> state.copy(isLoading = false) }
                    }
                }
        }
    }
}