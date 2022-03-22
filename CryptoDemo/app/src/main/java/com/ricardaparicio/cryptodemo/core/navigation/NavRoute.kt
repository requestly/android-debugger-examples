package com.ricardaparicio.cryptodemo.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavRoute(
    private val baseRoute: String,
    private val navArgs: List<NavArg> = emptyList(),
) {
    object CoinList : NavRoute("coinList")
    object CoinDetail : NavRoute("coinDetail", listOf(NavArg.CoinId))

    val args: List<NamedNavArgument> = navArgs.map { navArg ->
        navArgument(navArg.key) {
            type = navArg.type
        }
    }

    fun route(vararg args: String = emptyArray()): String =
        buildList {
            add(baseRoute)
            addAll(args)
        }.joinToString("/")

    val destination: String = run {
        val argKeys = navArgs.map { navArg -> "{${navArg.key}}" }
        buildList {
            add(baseRoute)
            addAll(argKeys)
        }.joinToString("/")
    }
}

enum class NavArg(val key: String, val type: NavType<*>) {
    CoinId("coinId", NavType.StringType),
}