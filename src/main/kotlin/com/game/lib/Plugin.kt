package com.game.lib

import com.game.helper.Update

enum class PluginRepository {
    PositionPlugin,
    ControllerPlugin,
    SizePlugin
}

interface Plugin {
    val id: PluginRepository
    val name: String
    val entity: Entity
    var update: Update?

    fun update(update: Update)

    fun <T> decode(data: ByteArray): T
    fun <T> encode(data: Map<String, T>): ByteArray

    fun apply() {
        if (update != null) {
            update?.entity = entity.uid.toString()

            entity.scene?.players?.forEach { player ->
                player.client?.sendUpdate(update!!)
            }
            update = null;
        }
    }

    fun syncWith(entity: Entity)
}