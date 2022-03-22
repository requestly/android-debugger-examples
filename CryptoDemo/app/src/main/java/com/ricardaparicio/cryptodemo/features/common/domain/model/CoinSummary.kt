package com.ricardaparicio.cryptodemo.features.common.domain.model

data class CoinSummary(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val price: Float,
    val marketCapRank: Int,
    val fiatCurrency: FiatCurrency,
)