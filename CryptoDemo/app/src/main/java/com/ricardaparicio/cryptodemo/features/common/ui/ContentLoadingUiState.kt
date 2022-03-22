package com.ricardaparicio.cryptodemo.features.common.ui

import com.ricardaparicio.cryptodemo.features.common.ui.model.AlertErrorUiModel

data class ContentLoadingUiState(
    val loading: Boolean = false,
    val error: AlertErrorUiModel? = null,
) {
    companion object {
        val empty get() = ContentLoadingUiState()
    }
}