package com.ricardaparicio.cryptodemo.features.coindetail.domain

import arrow.core.Either
import com.ricardaparicio.cryptodemo.core.Failure
import com.ricardaparicio.cryptodemo.core.usecase.UseCase
import com.ricardaparicio.cryptodemo.core.usecase.UseCaseParams
import com.ricardaparicio.cryptodemo.core.usecase.UseCaseResult
import com.ricardaparicio.cryptodemo.features.common.data.repository.CoinRepository
import com.ricardaparicio.cryptodemo.features.common.domain.model.Coin
import com.ricardaparicio.cryptodemo.features.coindetail.domain.GetCoinUseCase.Params
import com.ricardaparicio.cryptodemo.features.coindetail.domain.GetCoinUseCase.Result
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) : UseCase<Params, Result>(Dispatchers.IO) {

    data class Result(val coin: Coin) : UseCaseResult
    data class Params(val coinId: String) : UseCaseParams

    override suspend fun doWork(params: Params): Either<Failure, Result> =
        coinRepository.getCoin(params.coinId).map { coin ->
            Result(coin)
        }
}