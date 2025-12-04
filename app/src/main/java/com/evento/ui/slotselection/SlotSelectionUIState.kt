package com.evento.ui.slotselection

import com.evento.base.UIState
import com.evento.domain.entities.TimeSlot

data class SlotSelectionUIState(
    val isLoading: Boolean = false,
    val timeSlots: List<TimeSlot> = emptyList(),
    val selectedTimeSlot: TimeSlot? = null
) : UIState