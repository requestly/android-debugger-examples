package com.ricardaparicio.cryptodemo.features.common.data.api.model

data class CoinApiModel(
    val id: String,
    val symbol: String,
    val name: String,
    val links: CoinLinksApiModel,
    val image: CoinImagesApiModel,
    val market_data: CoinMarketDataApiModel,
    val description: CoinDescriptionApiModel,
    val market_cap_rank: Int,
)

data class CoinDescriptionApiModel(
    val es: String,
)

data class CoinLinksApiModel(
    val homepage: List<String>,
)

data class CoinImagesApiModel(
    val thumb: String,
    val small: String,
    val large: String
)

data class CoinMarketDataApiModel(
    val current_price: CoinPriceApiModel,
    val ath: CoinPriceApiModel,
    val market_cap: CoinPriceApiModel,
    val price_change_24h: Float,
    val price_change_percentage_24h: Float,
)

data class CoinPriceApiModel(
    val eur: Float,
    val usd: Float,
)