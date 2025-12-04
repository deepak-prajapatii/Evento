package com.evento.data.remote.responsebody

data class EventDto(
    val slotId: String,
    val name: String,
    val startTime: String,
    val endTime: String,
    val customerName: String,
    val contactNumber: String,
    val date: Long
)