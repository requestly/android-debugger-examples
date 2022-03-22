package com.ricardaparicio.cryptodemo.features.coindetail.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ricardaparicio.cryptodemo.R
import com.ricardaparicio.cryptodemo.core.util.Block
import com.ricardaparicio.cryptodemo.features.common.ui.AlertError
import com.ricardaparicio.cryptodemo.features.coindetail.presentation.viewmodel.CoinDetailViewModel
import com.ricardaparicio.cryptodemo.theme.Boulder

@ExperimentalMaterialApi
@Composable
fun CoinDetailScreen(onBackClicked: Block) {
    val viewModel = hiltViewModel<CoinDetailViewModel>()
    CoinDetail(
        uiState = viewModel.uiState,
        onBackClicked = onBackClicked,
        onClickDismissError = viewModel::onDismissDialogRequested
    )
}

@ExperimentalMaterialApi
@Composable
private fun CoinDetail(
    uiState: CoinDetailUiState,
    onBackClicked: Block,
    onClickDismissError: Block,
) {

    val lazyListState = rememberLazyListState()

    val appBarVisibility = when (lazyListState.firstVisibleItemIndex) {
        0 -> lazyListState.firstVisibleItemScrollOffset > 400f
        else -> true
    }
    val appBarTitleVisibility = when (lazyListState.firstVisibleItemIndex) {
        0 -> lazyListState.firstVisibleItemScrollOffset > 500f
        else -> true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Content(
            uiState = uiState,
            lazyListState = lazyListState,
        )
        CollapsibleAppBar(
            title = uiState.coinSummary.name,
            appBarVisibility = appBarVisibility,
            titleVisibility = appBarTitleVisibility
        ) {
            onBackClicked()
        }
        if (!appBarVisibility) {
            FloatingBackIcon {
                onBackClicked()
            }
        }
        if (uiState.contentLoadingUiState.loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

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

@ExperimentalMaterialApi
@Composable
private fun FloatingBackIcon(
    onBackClicked: Block
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp)
            .height(dimensionResource(R.dimen.app_bar_height)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.clip(CircleShape),
            onClick = onBackClicked,
            color = Color.Transparent
        ) {
            Image(
                modifier = Modifier.padding(10.dp),
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun CoinInfo(
    uiState: CoinDetailUiState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        CoinInfoItem(
            title = stringResource(R.string.coin_market_cap_position),
            text = uiState.coinSummary.marketCapPosition
        )
        Spacer(modifier = Modifier.height(15.dp))
        CoinInfoItem(
            title = stringResource(R.string.coin_current_price),
            text = uiState.coinSummary.price
        )
        Spacer(modifier = Modifier.height(15.dp))
        CoinInfoItem(
            title = stringResource(R.string.coin_ath),
            text = uiState.ath
        )
        Spacer(modifier = Modifier.height(15.dp))
        CoinInfoItem(
            title = stringResource(R.string.coin_price_change_24h),
            text = uiState.priceChange24h
        )
        Spacer(modifier = Modifier.height(15.dp))
        CoinInfoItem(
            title = stringResource(R.string.coin_price_percentage_change_24h),
            text = uiState.priceChangePercentage24h
        )
        Spacer(modifier = Modifier.height(15.dp))
        CoinInfoItem(
            title = stringResource(R.string.coin_description),
            text = uiState.description
        )
    }
}

@Composable
private fun CoinInfoItem(title: String, text: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
private fun Content(
    uiState: CoinDetailUiState,
    lazyListState: LazyListState,
) {
    Surface {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
        ) {
            item {
                CoinImage(lazyListState, uiState.coinSummary.image)
            }
            item {
                CoinInfo(uiState)
            }
        }
    }
}

@Composable
fun CollapsibleAppBar(
    title: String,
    appBarVisibility: Boolean,
    titleVisibility: Boolean,
    onClickBack: Block
) {
    AnimatedVisibility(
        visible = appBarVisibility,
        enter = slideInVertically(),
        exit = ExitTransition.None
    ) {
        TopAppBar(
            title = {
                AnimatedVisibility(
                    visible = titleVisibility,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onClickBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary,
                    )
                }
            },
            elevation = 8.dp,
        )
    }
}

@Composable
private fun CoinImage(
    lazyListState: LazyListState,
    imageUrl: String
) {
    val effectDivider = 8f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .background(Boulder)
            .alpha(
                when (lazyListState.firstVisibleItemIndex) {
                    0 -> 1 - (lazyListState.firstVisibleItemScrollOffset / (effectDivider * 100))
                    else -> 0f
                }
            )
            .offset(
                y = when (lazyListState.firstVisibleItemIndex) {
                    0 -> (lazyListState.firstVisibleItemScrollOffset / effectDivider)
                    else -> 0f
                }.dp
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.size(140.dp),
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}
