package com.ricardaparicio.cryptodemo.features.coindetail.presentation.reducer

import com.ricardaparicio.cryptodemo.core.Reducer
import com.ricardaparicio.cryptodemo.core.UiAction
import com.ricardaparicio.cryptodemo.core.util.formatPercentage
import com.ricardaparicio.cryptodemo.core.util.formatPrice
import com.ricardaparicio.cryptodemo.features.common.domain.model.Coin
import com.ricardaparicio.cryptodemo.features.common.ui.model.CoinSummaryUiModel
import com.ricardaparicio.cryptodemo.features.common.ui.viewmodel.ContentLoadingReducer
import com.ricardaparicio.cryptodemo.features.common.ui.viewmodel.ContentLoadingUiAction
import com.ricardaparicio.cryptodemo.features.coindetail.presentation.ui.CoinDetailUiState
import javax.inject.Inject

sealed class CoinDetailUiAction : UiAction {
    data class NewCoin(val coin: Coin) : CoinDetailUiAction()
    data class UpdateContentLoading(val action: ContentLoadingUiAction) : CoinDetailUiAction()
}

class CoinDetailReducer @Inject constructor(
    contentLoadingReducer: ContentLoadingReducer
) : Reducer<CoinDetailUiState, CoinDetailUiAction> {

    override val reduce: (CoinDetailUiState, CoinDetailUiAction) -> CoinDetailUiState =
        { state, action ->
            when (action) {
                is CoinDetailUiAction.NewCoin -> {
                    val coin = action.coin
                    val fiatCurrency = coin.coinSummary.fiatCurrency
                    state.copy(
                        coinSummary = CoinSummaryUiModel.from(coin.coinSummary),
                        description = coin.description,
                        ath = coin.ath.formatPrice(fiatCurrency),
                        marketCap = coin.marketCap.formatPrice(fiatCurrency),
                        priceChange24h = coin.priceChange24h.formatPrice(fiatCurrency),
                        priceChangePercentage24h = coin.priceChangePercentage24h.formatPercentage(),
                    )
                }
                is CoinDetailUiAction.UpdateContentLoading -> {
                    state.copy(
                        contentLoadingUiState = contentLoadingReducer.reduce(
                            state.contentLoadingUiState,
                            action.action
                        )
                    )
                }
            }
        }
}