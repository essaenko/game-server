package com.game.helper

import kotlinx.serialization.Serializable

@Serializable
data class Update(
    val plugin: Int,
    val data: ByteArray,
    var entity: String? = null,
)