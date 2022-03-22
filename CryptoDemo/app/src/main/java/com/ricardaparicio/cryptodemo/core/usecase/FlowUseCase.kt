package com.ricardaparicio.cryptodemo.core.usecase

import arrow.core.Either
import com.ricardaparicio.cryptodemo.core.Failure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<P : UseCaseParams, R : UseCaseResult>(
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(params: P): Flow<Either<Failure, R>> = doWork(params).flowOn(dispatcher)

    abstract fun doWork(params: P): Flow<Either<Failure, R>>
}