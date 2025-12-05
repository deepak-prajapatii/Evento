package com.evento.ui.customerdetails


sealed class CustomerFormInputError {
    data object EmptyName : CustomerFormInputError()
    data object EmptyPhone : CustomerFormInputError()
    data object InvalidPhone : CustomerFormInputError()
}