package com.ricardaparicio.cryptodemo.features.coindetail.presentation.ui

import com.ricardaparicio.cryptodemo.features.common.ui.model.CoinSummaryUiModel
import com.ricardaparicio.cryptodemo.features.common.ui.ContentLoadingUiState


data class CoinDetailUiState(
    val coinSummary: CoinSummaryUiModel = CoinSummaryUiModel(),
    val description: String = "",
    val ath: String = "",
    val marketCap: String = "",
    val priceChange24h: String = "",
    val priceChangePercentage24h: String = "",
    val contentLoadingUiState: ContentLoadingUiState = ContentLoadingUiState.empty,
)
