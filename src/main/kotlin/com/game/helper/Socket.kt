package com.game.helper

import com.game.config.SceneConfig
import kotlinx.serialization.Serializable

@Serializable
enum class SocketEvents {
    ClientId,
    SceneConfigs,
    InitCharacter,
    Update,
    NewPlayer,
    NewNPCInit,
    PlayerLeave,
}

@Serializable
data class MessagePayload(
    val id: String? = null,
    val scenes: List<SceneConfig>? = null,
    val scene: String? = null,
    val plugin: Int? = null,
    val updates: Map<String, String>? = null,
    val update: Update? = null,
    val player: String? = null,
    val npc: String? = null,
    val entity: String? = null,
)

@Serializable
data class Message(
    val event: SocketEvents,
    val payload: MessagePayload? = null,
)