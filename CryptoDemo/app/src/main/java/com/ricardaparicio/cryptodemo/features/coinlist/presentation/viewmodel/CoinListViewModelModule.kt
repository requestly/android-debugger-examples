package com.ricardaparicio.cryptodemo.features.coinlist.presentation.viewmodel

import com.ricardaparicio.cryptodemo.core.Reducer
import com.ricardaparicio.cryptodemo.features.coinlist.presentation.ui.CoinListUiState
import com.ricardaparicio.cryptodemo.features.coinlist.presentation.reducer.CoinListReducer
import com.ricardaparicio.cryptodemo.features.coinlist.presentation.reducer.CoinListUiAction
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface CoinListViewModelModule {
    @Binds
    fun provideCoinListReducer(reducer: CoinListReducer): Reducer<CoinListUiState, CoinListUiAction>
}