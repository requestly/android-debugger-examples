package com.ricardaparicio.cryptodemo.features.coindetail.presentation.viewmodel

import com.ricardaparicio.cryptodemo.core.Reducer
import com.ricardaparicio.cryptodemo.features.coindetail.presentation.ui.CoinDetailUiState
import com.ricardaparicio.cryptodemo.features.coindetail.presentation.reducer.CoinDetailReducer
import com.ricardaparicio.cryptodemo.features.coindetail.presentation.reducer.CoinDetailUiAction
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface CoinDetailViewModelModule {
    @Binds
    fun provideCoinDetailReducer(reducer: CoinDetailReducer): Reducer<CoinDetailUiState, CoinDetailUiAction>
}