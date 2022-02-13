package com.game.plugin

import com.game.helper.Binary
import com.game.helper.Update
import com.game.lib.Entity
import com.game.lib.Plugin
import com.game.lib.PluginRepository

class PositionPlugin(override val entity: Entity): Plugin {
    override val id: PluginRepository = PluginRepository.PositionPlugin
    override val name: String = "PositionPlugin"
    override var update: Update? = null

    var x: Int = 0
    var y: Int = 0

    override fun update(update: Update) {
        val data = decode<Map<String, Int>>(update.data)
        this.setPosition(x + (data["x"] ?: 0), y + (data["y"] ?: 0))

        this.update = Update(
            plugin = id.ordinal,
            data = encode(mapOf(
                Pair("x", x),
                Pair("y", y)),
            )
        )
    }

    override fun <T> encode(data: Map<String, T>): ByteArray {
        val xPkg = Binary.IntToBytes(data["x"] as Int, 2)
        val yPkg = Binary.IntToBytes(data["y"] as Int, 2)

        return xPkg.plus(yPkg)
    }

    override fun <T> decode(data: ByteArray): T {
        return mapOf(
            Pair("x", data[0].toInt()),
            Pair("y", data[2].toInt())
        ) as T
    }

    override fun syncWith(entity: Entity) {
        entity.client?.sendUpdate(
            Update(
                plugin = id.ordinal,
                entity = this.entity.uid.toString(),
                data = encode(mapOf(
                    Pair("x", x),
                    Pair("y", y)),
                )
            )
        )
    }

    fun setPosition(x: Int, y: Int) {
        this.x = x;
        this.y = y
    }
}