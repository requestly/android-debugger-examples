package com.ricardaparicio.cryptodemo.features.common.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ricardaparicio.cryptodemo.core.Failure
import com.ricardaparicio.cryptodemo.core.LocalError
import com.ricardaparicio.cryptodemo.features.common.domain.model.FiatCurrency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class CoinPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : CoinLocalDataSource {

    override fun fiatCurrencyFlow(): Flow<Either<Failure, FiatCurrency>> =
        dataStore.data.map { preferences ->
            wrapEither {
                FiatCurrency.valueOf(preferences[Keys.FIAT_CURRENCY] ?: FiatCurrency.Eur.name)
            }
        }

    override suspend fun updateFiatCurrency(currency: FiatCurrency): Either<Failure, Unit> =
        wrapEither {
            dataStore.edit { preferences ->
                preferences[Keys.FIAT_CURRENCY] = currency.name
            }
        }

    private suspend fun <T : Any> wrapEither(block: suspend () -> T): Either<Failure, T> =
        runCatching {
            block().right()
        }.onFailure { error ->
            Timber.e(error)
        }.getOrElse {
            LocalError.left()
        }

    private object Keys {
        val FIAT_CURRENCY = stringPreferencesKey("fiat_currency")
    }
}