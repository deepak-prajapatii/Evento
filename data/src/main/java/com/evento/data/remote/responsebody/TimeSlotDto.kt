package com.evento.data.remote.responsebody

data class TimeSlotDto(
    val slotId: String,
    val name: String,
    val startTime: String,
    val endTime: String
)