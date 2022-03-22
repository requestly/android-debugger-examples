package com.ricardaparicio.cryptodemo.core

sealed interface Failure

object NetworkingError : Failure
object ServerError : Failure
object LocalError : Failure
