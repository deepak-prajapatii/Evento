package com.evento.ui.customerdetails

import com.evento.base.UIEvent

sealed class CustomerDetailsUIEvent : UIEvent {
    object NavigateToEventBooking: CustomerDetailsUIEvent()
}