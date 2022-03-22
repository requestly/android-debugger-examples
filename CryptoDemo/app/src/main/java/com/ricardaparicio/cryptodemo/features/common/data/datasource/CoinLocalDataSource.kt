package com.ricardaparicio.cryptodemo.features.common.data.datasource

import arrow.core.Either
import com.ricardaparicio.cryptodemo.core.Failure
import com.ricardaparicio.cryptodemo.features.common.domain.model.FiatCurrency
import kotlinx.coroutines.flow.Flow

interface CoinLocalDataSource {
    fun fiatCurrencyFlow(): Flow<Either<Failure, FiatCurrency>>
    suspend fun updateFiatCurrency(currency: FiatCurrency): Either<Failure, Unit>
}