package com.evento.domain.usecase

import com.evento.domain.base.Either
import com.evento.domain.entities.Event
import com.evento.domain.repositories.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookedEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) : UseCase<List<Event>, Unit> {
    override fun execute(params: Unit): Flow<Either<List<Event>>> {
        return eventRepository.getBookedEvents()
    }
}