package com.evento.ui.customerdetails

import com.evento.base.UIState
import com.evento.domain.entities.TimeSlot

data class CustomerDetailsUIState(
    val isLoading: Boolean = false,
    val timeSlot: TimeSlot? = null,
    val customerName: String = "",
    val phoneNumber: String = "",
    val nameError: String? = null,
    val phoneError: String? = null
) : UIState