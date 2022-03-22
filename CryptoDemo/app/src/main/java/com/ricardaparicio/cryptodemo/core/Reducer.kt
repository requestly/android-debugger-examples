package com.ricardaparicio.cryptodemo.core

interface Reducer<T, A : UiAction> {
    val reduce: (T, A) -> T
}

interface UiAction
