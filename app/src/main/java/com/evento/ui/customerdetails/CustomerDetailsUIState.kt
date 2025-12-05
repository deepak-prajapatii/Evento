package com.evento.ui.customerdetails

import androidx.annotation.StringRes
import com.evento.base.UIState
import com.evento.domain.base.ErrorType
import com.evento.domain.entities.TimeSlot

data class CustomerDetailsUIState(
    val isLoading: Boolean = false,
    val timeSlot: TimeSlot? = null,
    val customerName: String = "",
    val phoneNumber: String = "",
    val nameError: CustomerFormInputError? = null,
    val phoneError: CustomerFormInputError? = null,
    val uiErrorTitle: String? = null,
    val uiErrorMessage: String? = null
) : UIState