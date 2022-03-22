package com.ricardaparicio.cryptodemo.features.common.ui.viewmodel

import com.ricardaparicio.cryptodemo.core.Reducer
import com.ricardaparicio.cryptodemo.features.common.ui.ContentLoadingUiState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface CommonViewModelModule {
    @Binds
    fun provideContentLoadingReducer(reducer: ContentLoadingReducer): Reducer<ContentLoadingUiState, ContentLoadingUiAction>
}