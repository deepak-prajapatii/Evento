package com.evento.domain.usecase

import com.evento.domain.common.Either
import kotlinx.coroutines.flow.Flow

interface UseCase<out ReturnType, in Params> {
    fun execute(params: Params): Flow<Either<ReturnType>>
}