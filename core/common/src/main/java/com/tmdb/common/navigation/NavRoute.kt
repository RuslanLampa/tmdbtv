package com.tmdb.common.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object HomeNavRoute : NavRoute

    @Serializable
    data class DetailNavRoute(val movieId: Int) : NavRoute

    @Serializable
    data class PlayerNavRoute(val movieId: Int) : NavRoute
}