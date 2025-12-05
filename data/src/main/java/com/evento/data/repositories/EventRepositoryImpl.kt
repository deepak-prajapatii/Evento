package com.evento.data.repositories

import com.evento.data.remote.mappers.toDomain
import com.evento.data.remote.mappers.toDomainList
import com.evento.data.remote.service.EventService
import com.evento.domain.common.Either
import com.evento.domain.entities.Event
import com.evento.domain.entities.TimeSlot
import com.evento.domain.repositories.EventRepository
import com.evento.domain.requestbody.CreateEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventService: EventService
) : EventRepository, BaseRepository() {
    override fun getBookedEvents(): Flow<Either<List<Event>>> {
        return flow {
            val response = safeApiCall { eventService.getBookedEvents() }
            if (response is Either.Success){
                emit(Either.success(response.data.toDomainList()))
            } else if(response is Either.Failure) {
                emit(Either.failure(response.exception))
            }
        }
    }

    override fun getAvailableSlots(): Flow<Either<List<TimeSlot>>> {
        return flow {
            val response = safeApiCall { eventService.getAvailableSlots() }
            if (response is Either.Success){
                emit(Either.success(response.data.toDomainList()))
            } else if(response is Either.Failure) {
                emit(Either.failure(response.exception))
            }
        }
    }

    override fun createEvent(createEvent: CreateEvent): Flow<Either<Event>> {
        return flow {
            val response = safeApiCall { eventService.createEvent(createEvent) }
            if (response is Either.Success){
                emit(Either.success(response.data.toDomain()))
            } else if(response is Either.Failure){
                emit(Either.failure(response.exception))
            }
        }
    }
}