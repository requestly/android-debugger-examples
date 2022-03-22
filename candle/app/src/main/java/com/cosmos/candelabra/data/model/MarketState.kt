package com.cosmos.candelabra.data.model

enum class MarketState(val value: String) {
    PRE("PRE"), REGULAR("REGULAR"), POST("POST");

    companion object {
        fun fromString(state: String): MarketState? {
            return values().find { it.value == state }
        }
    }
}
