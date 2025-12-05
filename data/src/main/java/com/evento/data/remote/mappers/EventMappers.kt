package com.evento.data.remote.mappers

import com.evento.data.remote.responsebody.EventDto
import com.evento.domain.entities.Event

fun EventDto.toDomain(): Event =
    Event(
        slotId = slotId,
        name = name,
        startTime = startTime,
        endTime = endTime,
        customerName = customerName,
        phoneNumber = phoneNumber,
        date = date
    )


fun List<EventDto>.toDomainList(): List<Event> =
    map { it.toDomain() }
