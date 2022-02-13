package com.game.plugin

import com.game.helper.Binary
import com.game.helper.Update
import com.game.lib.Entity
import com.game.lib.Plugin
import com.game.lib.PluginRepository

data class Vector2 (
    val x: Float,
    val y: Float,
);

class ControllerPlugin(override val entity: Entity): Plugin {
    override val id: PluginRepository = PluginRepository.ControllerPlugin
    override val name: String = "ControllerPlugin"
    override var update: Update? = null
    private var speed: Int = 1

    override fun update(update: Update) {
        val vector = decode<Vector2>(update.data)

        val position: PositionPlugin? = entity.plugins[PluginRepository.PositionPlugin] as? PositionPlugin
        position?.update(
            Update(
                plugin = PluginRepository.PositionPlugin.ordinal,
                entity = entity.uid.toString(),
                data = position.encode(
                    mapOf(
                        Pair("x", (speed * vector.x).toInt()),
                        Pair("y", (speed * vector.y).toInt()),
                    )
                )
            )
        )
    }

    override fun <T> decode(data: ByteArray): T {
        return Vector2(
            (data[0].toFloat() / 100),
            (data[1].toFloat() / 100),
        ) as T
    }
    override fun <T> encode(data: Map<String, T>): ByteArray {
        return ByteArray(0)
    }
    override fun syncWith(entity: Entity) {}

    fun setSpeed(speed: Int) {
        this.speed = speed
    }

}