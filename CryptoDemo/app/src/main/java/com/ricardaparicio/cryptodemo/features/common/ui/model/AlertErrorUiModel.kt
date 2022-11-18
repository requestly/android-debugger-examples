package com.ricardaparicio.cryptodemo.features.common.ui.model

import androidx.annotation.StringRes
import com.ricardaparicio.cryptodemo.R
import com.ricardaparicio.cryptodemo.core.Failure
import com.ricardaparicio.cryptodemo.core.LocalError
import com.ricardaparicio.cryptodemo.core.NetworkingError
import com.ricardaparicio.cryptodemo.core.ServerError

enum class AlertErrorUiModel(
    @StringRes val errorTitle: Int,
    @StringRes val errorSubtitle: Int
) {
    Networking(
        R.string.alert_error_title,
        R.string.alert_network_error_subtitle
    ),
    Server(
        R.string.alert_error_title,
        R.string.alert_server_error_subtitle
    ),
    Internal(
        R.string.alert_error_title,
        R.string.alert_internal_error_subtitle
    );

    companion object {
        fun from(failure: Failure): AlertErrorUiModel =
            when (failure) {
                LocalError -> Internal
                NetworkingError -> Networking
                ServerError -> Server
            }
    }
}