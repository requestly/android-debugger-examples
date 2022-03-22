package com.ricardaparicio.cryptodemo.features.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ricardaparicio.cryptodemo.core.util.Block
import com.ricardaparicio.cryptodemo.features.common.ui.model.AlertErrorUiModel

@Composable
fun AlertError(modifier: Modifier, model: AlertErrorUiModel, onDismiss: Block) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(stringResource(model.errorTitle))
        },
        text = {
            Text(stringResource(model.errorSubtitle))
        },
        buttons = {
            Row(
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 15.dp,
                ),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onDismiss() }
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            }

        }
    )
}