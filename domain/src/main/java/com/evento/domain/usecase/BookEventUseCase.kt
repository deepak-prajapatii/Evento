package com.evento.domain.usecase

import com.evento.domain.common.Either
import com.evento.domain.entities.Event
import com.evento.domain.repositories.EventRepository
import com.evento.domain.requestbody.CreateEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookEventUseCase @Inject constructor(
    private val repository: EventRepository
) : UseCase<Event, CreateEvent> {
    override fun execute(params: CreateEvent): Flow<Either<Event>> {
        return repository.createEvent(params)
    }
}