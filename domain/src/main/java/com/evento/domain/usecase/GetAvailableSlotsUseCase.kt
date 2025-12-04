package com.evento.domain.usecase

import com.evento.domain.base.Either
import com.evento.domain.entities.TimeSlot
import com.evento.domain.repositories.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvailableSlotsUseCase @Inject constructor(
    private val repository: EventRepository
) : UseCase<List<TimeSlot>, Unit>{
    override fun execute(params: Unit): Flow<Either<List<TimeSlot>>> {
        return repository.getAvailableSlots()
    }
}