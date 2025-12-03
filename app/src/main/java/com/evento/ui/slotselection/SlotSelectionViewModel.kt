package com.evento.ui.slotselection

import com.evento.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SlotSelectionViewModel @Inject constructor(): BaseViewModel<SlotSelectionUIState, SlotSelectionUIEvent>(initialState = SlotSelectionUIState()){
}