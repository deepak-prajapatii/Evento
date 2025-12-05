package com.evento.ui.slotselection

import androidx.lifecycle.viewModelScope
import com.evento.base.BaseViewModel
import com.evento.domain.entities.TimeSlot
import com.evento.domain.usecase.GetAvailableSlotsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlotSelectionViewModel @Inject constructor(
    private val getAvailableSlotsUseCase: GetAvailableSlotsUseCase
) : BaseViewModel<SlotSelectionUIState, SlotSelectionUIEvent>(initialState = SlotSelectionUIState()) {

    init {
        getAvailableSlots()
    }

    private fun getAvailableSlots() {
        viewModelScope.launch {
            updateState { state -> state.copy(isLoading = true) }
            getAvailableSlotsUseCase.execute(Unit)
                .collect { either ->
                    either.onSuccess { timeSlots ->
                        updateState { state -> state.copy(isLoading = false, timeSlots = timeSlots) }
                    }.onFailure {
                        updateState { state -> state.copy(isLoading = false, uiErrorType = it.type, uiErrorMessage = it.errorMessage) }
                    }
                }
        }
    }

    fun onSlotSelected(slot: TimeSlot) {
        updateState { state -> state.copy(selectedTimeSlot = slot) }
    }
}