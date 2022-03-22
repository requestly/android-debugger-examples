package com.ricardaparicio.cryptodemo.features.coinlist.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.ricardaparicio.cryptodemo.R
import com.ricardaparicio.cryptodemo.core.util.Block
import com.ricardaparicio.cryptodemo.core.util.TypedBlock
import com.ricardaparicio.cryptodemo.features.common.domain.model.FiatCurrency
import com.ricardaparicio.cryptodemo.features.common.ui.AlertError
import com.ricardaparicio.cryptodemo.features.common.ui.model.CoinSummaryUiModel
import com.ricardaparicio.cryptodemo.features.coinlist.presentation.viewmodel.CoinListViewModel

@Composable
fun CoinListScreen(onClickCoin: TypedBlock<CoinSummaryUiModel>) {
    val viewModel = hiltViewModel<CoinListViewModel>()
    CoinList(
        uiState = viewModel.uiState,
        onClickCoin = onClickCoin,
        onClickCurrency = { currency -> viewModel.onFiatCurrencyClicked(currency) },
        onClickDismissError = { viewModel.onDismissDialogRequested() }
    )
}

@Composable
private fun CoinList(
    uiState: CoinListUiState,
    onClickCoin: TypedBlock<CoinSummaryUiModel>,
    onClickCurrency: TypedBlock<FiatCurrency>,
    onClickDismissError: Block,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CoinLazyColumn(
            uiState = uiState,
            onClickCoin = onClickCoin
        )

        if (uiState.contentLoadingUiState.loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        FloatingFiatCurrency(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp),
            onClickCurrency = onClickCurrency,
            uiState = uiState
        )

        uiState.contentLoadingUiState.error?.let {
            AlertError(
                modifier = Modifier.align(Alignment.Center),
                model = uiState.contentLoadingUiState.error
            ) {
                onClickDismissError()
            }
        }
    }
}

@Composable
private fun FloatingFiatCurrency(
    modifier: Modifier,
    onClickCurrency: TypedBlock<FiatCurrency>,
    uiState: CoinListUiState
) {
    if (!uiState.contentLoadingUiState.loading) {
        FloatingActionButton(
            modifier = modifier,
            onClick = { onClickCurrency(uiState.fiatCurrency) }
        ) {
            Icon(
                painter = painterResource(
                    when (uiState.fiatCurrency) {
                        FiatCurrency.Eur -> R.drawable.ic_euro
                        FiatCurrency.Usd -> R.drawable.ic_dollar
                    }
                ),
                contentDescription = null
            )
        }
    }

}

@Composable
private fun CoinLazyColumn(
    uiState: CoinListUiState,
    onClickCoin: TypedBlock<CoinSummaryUiModel>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 30.dp)
    ) {
        val size = uiState.coins.size
        items(size) { index ->
            val coinItem = uiState.coins[index]
            CoinItem(coinItem, onClickCoin)
            if (index < size - 1) {
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun CoinItem(coinItem: CoinSummaryUiModel, onClickCoin: TypedBlock<CoinSummaryUiModel>) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClickCoin(coinItem) }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Center,
                text = coinItem.marketCapPosition,
                style = MaterialTheme.typography.caption,
            )
            Spacer(Modifier.width(15.dp))
            Image(
                modifier = Modifier.size(35.dp),
                painter = rememberImagePainter(
                    data = coinItem.image,
                    builder = {
                        transformations(CircleCropTransformation())
                        crossfade(true)
                    }
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.width(15.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = coinItem.symbol,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = coinItem.price,
                    style = MaterialTheme.typography.h5
                )
            }

        }
    }
}