package com.game.config

import kotlinx.serialization.Serializable

@Serializable
data class SceneConfig(
    val name: String,
)

@Serializable
val SceneMap = listOf(
    SceneConfig("Game"),
    SceneConfig("World")
)