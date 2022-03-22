package com.ricardaparicio.cryptodemo.features.common.ui.viewmodel

import com.ricardaparicio.cryptodemo.core.Failure
import com.ricardaparicio.cryptodemo.core.Reducer
import com.ricardaparicio.cryptodemo.core.UiAction
import com.ricardaparicio.cryptodemo.features.common.ui.ContentLoadingUiState
import com.ricardaparicio.cryptodemo.features.common.ui.model.AlertErrorUiModel
import javax.inject.Inject

sealed class ContentLoadingUiAction : UiAction {
    object Loading : ContentLoadingUiAction()
    object Success : ContentLoadingUiAction()
    object CloseError : ContentLoadingUiAction()
    data class Error(val failure: Failure) : ContentLoadingUiAction()
}

class ContentLoadingReducer @Inject constructor() : Reducer<ContentLoadingUiState, ContentLoadingUiAction> {

    override val reduce: (ContentLoadingUiState, ContentLoadingUiAction) -> ContentLoadingUiState =
        { state, action ->
            when (action) {
                ContentLoadingUiAction.Loading -> {
                    state.copy(
                        loading = true,
                    )
                }
                ContentLoadingUiAction.CloseError -> {
                    state.copy(
                        error = null
                    )
                }
                is ContentLoadingUiAction.Error -> {
                    state.copy(
                        error = AlertErrorUiModel.from(action.failure),
                        loading = false,
                    )
                }
                ContentLoadingUiAction.Success -> {
                    state.copy(
                        error = null,
                        loading = false,
                    )
                }
            }
        }
}
