package com.ricardaparicio.cryptodemo.features.coinlist.domain

import arrow.core.Either
import com.ricardaparicio.cryptodemo.core.Failure
import com.ricardaparicio.cryptodemo.core.usecase.UseCase
import com.ricardaparicio.cryptodemo.core.usecase.UseCaseParams
import com.ricardaparicio.cryptodemo.core.usecase.UseCaseResult
import com.ricardaparicio.cryptodemo.features.common.data.repository.CoinRepository
import com.ricardaparicio.cryptodemo.features.common.domain.model.FiatCurrency
import com.ricardaparicio.cryptodemo.features.coinlist.domain.UpdateFiatCurrencyUseCase.Params
import com.ricardaparicio.cryptodemo.features.coinlist.domain.UpdateFiatCurrencyUseCase.Result
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class UpdateFiatCurrencyUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) : UseCase<Params, Result>(Dispatchers.IO) {

    data class Params(val currency: FiatCurrency) : UseCaseParams
    data class Result(val currency: FiatCurrency) : UseCaseResult

    override suspend fun doWork(params: Params): Either<Failure, Result> =
        coinRepository.updateFiatCurrency(params.currency).map { currency -> Result(currency) }
}