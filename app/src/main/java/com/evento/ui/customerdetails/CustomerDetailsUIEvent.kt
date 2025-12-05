package com.evento.ui.customerdetails

import com.evento.common.UIEvent

sealed class CustomerDetailsUIEvent : UIEvent {
    object NavigateToEventBooking: CustomerDetailsUIEvent()
    object CreateEventFailure: CustomerDetailsUIEvent()
}