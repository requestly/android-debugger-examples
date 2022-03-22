package com.ricardaparicio.cryptodemo.features.common.domain.model

sealed class CoinListState {
    object Loading : CoinListState()
    data class Coins(val coins: List<CoinSummary>): CoinListState()
}