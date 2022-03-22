package com.ricardaparicio.cryptodemo.features.coinlist.domain

import arrow.core.Either
import com.ricardaparicio.cryptodemo.core.Failure
import com.ricardaparicio.cryptodemo.core.usecase.*
import com.ricardaparicio.cryptodemo.features.common.data.repository.CoinRepository
import com.ricardaparicio.cryptodemo.features.common.domain.model.FiatCurrency
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetFiatCurrencyUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) : UseCase<NoParam, GetFiatCurrencyUseCase.Result>(Dispatchers.IO) {

    data class Result(val currency: FiatCurrency) : UseCaseResult

    override suspend fun doWork(params: NoParam): Either<Failure, Result> =
        coinRepository.getFiatCurrency().map { currency -> Result(currency) }
}