package com.ricardaparicio.cryptodemo.core.usecase

import arrow.core.Either
import com.ricardaparicio.cryptodemo.core.Failure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<P : UseCaseParams, R : UseCaseResult>(
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(params: P): Either<Failure, R> = withContext(dispatcher) { doWork(params) }

    abstract suspend fun doWork(params: P): Either<Failure, R>
}

