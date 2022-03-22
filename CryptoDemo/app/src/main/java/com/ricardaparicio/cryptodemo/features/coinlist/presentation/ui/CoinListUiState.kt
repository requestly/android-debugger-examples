package com.ricardaparicio.cryptodemo.features.coinlist.presentation.ui

import com.ricardaparicio.cryptodemo.features.common.domain.model.FiatCurrency
import com.ricardaparicio.cryptodemo.features.common.ui.ContentLoadingUiState
import com.ricardaparicio.cryptodemo.features.common.ui.model.CoinSummaryUiModel

data class CoinListUiState(
    val coins: List<CoinSummaryUiModel> = emptyList(),
    val fiatCurrency: FiatCurrency = FiatCurrency.Eur,
    val contentLoadingUiState: ContentLoadingUiState = ContentLoadingUiState.empty,
)
