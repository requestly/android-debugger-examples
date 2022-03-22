package com.ricardaparicio.cryptodemo.features.coindetail.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricardaparicio.cryptodemo.core.Reducer
import com.ricardaparicio.cryptodemo.core.navigation.NavArg
import com.ricardaparicio.cryptodemo.features.common.ui.viewmodel.ContentLoadingUiAction
import com.ricardaparicio.cryptodemo.features.coindetail.domain.GetCoinUseCase
import com.ricardaparicio.cryptodemo.features.coindetail.presentation.ui.CoinDetailUiState
import com.ricardaparicio.cryptodemo.features.coindetail.presentation.reducer.CoinDetailUiAction
import com.ricardaparicio.cryptodemo.features.coindetail.presentation.reducer.CoinDetailUiAction.NewCoin
import com.ricardaparicio.cryptodemo.features.coindetail.presentation.reducer.CoinDetailUiAction.UpdateContentLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    private val reducer: Reducer<CoinDetailUiState, CoinDetailUiAction>,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var uiState by mutableStateOf(CoinDetailUiState())
        private set

    private val coinId: String
        get() = requireNotNull(
            savedStateHandle.get<String>(NavArg.CoinId.key)
        )

    init {
        fetchCoin()
    }

    private fun fetchCoin() =
        viewModelScope.launch {
            reduce(UpdateContentLoading(ContentLoadingUiAction.Loading))
            getCoinUseCase(GetCoinUseCase.Params(coinId))
                .fold(
                    { failure ->
                        reduce(UpdateContentLoading(ContentLoadingUiAction.Error(failure)))
                    },
                    { result ->
                        reduce(NewCoin(result.coin))
                        reduce(UpdateContentLoading(ContentLoadingUiAction.Success))
                    }
                )
        }

    private fun reduce(action: CoinDetailUiAction) {
        uiState = reducer.reduce(uiState, action)
    }

    fun onDismissDialogRequested() {
        reduce(UpdateContentLoading(ContentLoadingUiAction.CloseError))
    }

}