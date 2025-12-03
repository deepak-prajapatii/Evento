package com.evento.ui.customerdetails

import com.evento.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerDetailsViewModel @Inject constructor():
    BaseViewModel<CustomerDetailsUIState, CustomerDetailsUIEvent>(initialState = CustomerDetailsUIState())