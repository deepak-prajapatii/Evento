package com.evento.ui.customerdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.evento.R
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
        val slotId: String = savedStateHandle[AppConstants.SLOT_ID] ?: AppConstants.EMPTY
        val slotName: String = savedStateHandle[AppConstants.SLOT_NAME] ?: AppConstants.EMPTY
        val startTime: String = savedStateHandle[AppConstants.START_TIME] ?: AppConstants.EMPTY
        val endTime: String = savedStateHandle[AppConstants.END_TIME] ?: AppConstants.EMPTY

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
        var nameError: CustomerFormInputError? = null
        var phoneError: CustomerFormInputError? = null

        // Validate name
        if (currentState.customerName.isBlank()) {
            nameError = CustomerFormInputError.EmptyName
        }

        // Validate phone
        if (currentState.phoneNumber.isBlank()) {
            phoneError = CustomerFormInputError.EmptyPhone
        } else if (currentState.phoneNumber.length < AppConstants.MAX_PHONE_NUMBER_LENGTH) {
            phoneError = CustomerFormInputError.InvalidPhone
        }

        // If any error -> update state with BOTH errors at once and stop
        if (nameError != null || phoneError != null) {
            updateState {
                it.copy(
                    nameError = nameError,
                    phoneError = phoneError
                )
            }
            return
        }


        updateState {
            it.copy(
                nameError = null,
                phoneError = null
            )
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
                        updateState { state -> state.copy(isLoading = false, uiErrorTitle = it.type.name, uiErrorMessage = it.errorMessage) }
                        sendEvent(CustomerDetailsUIEvent.CreateEventFailure)
                    }
                }
        }
    }
}