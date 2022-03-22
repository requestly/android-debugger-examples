package com.ricardaparicio.cryptodemo.features.common.domain.model

data class Coin(
    val coinSummary: CoinSummary,
    val description: String,
    val ath: Float,
    val marketCap: Float,
    val priceChange24h: Float,
    val priceChangePercentage24h: Float,
)