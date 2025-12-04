package com.evento.data.remote.mappers

import com.evento.data.remote.responsebody.TimeSlotDto
import com.evento.domain.entities.TimeSlot

fun TimeSlotDto.toDomain(): TimeSlot =
    TimeSlot(
        slotId = slotId,
        name = name,
        startTime = startTime,
        endTime = endTime
    )


fun TimeSlot.toDto(): TimeSlotDto =
    TimeSlotDto(
        slotId = slotId,
        name = name,
        startTime = startTime,
        endTime = endTime
    )

fun List<TimeSlotDto>.toDomainList(): List<TimeSlot> =
    map { it.toDomain() }


