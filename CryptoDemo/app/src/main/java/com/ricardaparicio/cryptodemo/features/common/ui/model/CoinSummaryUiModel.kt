package com.ricardaparicio.cryptodemo.features.common.ui.model

import com.ricardaparicio.cryptodemo.core.util.formatPrice
import com.ricardaparicio.cryptodemo.features.common.domain.model.CoinSummary

data class CoinSummaryUiModel(
    val id: String = "",
    val marketCapPosition: String = "",
    val symbol: String = "",
    val name: String = "",
    val image: String = "",
    val price: String = "",
    val error: AlertErrorUiModel? = null
) {
    companion object {
        fun from(coinSummary: CoinSummary): CoinSummaryUiModel =
            CoinSummaryUiModel(
                id = coinSummary.id,
                symbol = coinSummary.symbol,
                name = coinSummary.name,
                image = coinSummary.image,
                price = coinSummary.price.formatPrice(coinSummary.fiatCurrency),
                marketCapPosition = coinSummary.marketCapRank.toString()
            )
    }
}