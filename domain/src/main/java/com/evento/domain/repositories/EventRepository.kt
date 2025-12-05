package com.evento.domain.repositories

import com.evento.domain.common.Either
import com.evento.domain.entities.Event
import com.evento.domain.entities.TimeSlot
import com.evento.domain.requestbody.CreateEvent
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getBookedEvents(): Flow<Either<List<Event>>>
    fun getAvailableSlots(): Flow<Either<List<TimeSlot>>>
    fun createEvent(createEvent: CreateEvent): Flow<Either<Event>>
}