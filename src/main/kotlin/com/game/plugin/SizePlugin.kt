package com.game.plugin

import com.game.helper.Binary
import com.game.helper.Update
import com.game.lib.Entity
import com.game.lib.Plugin
import com.game.lib.PluginRepository

class SizePlugin(override val entity: Entity, var width: Int = 0, var height: Int = 0): Plugin {
    override val id: PluginRepository = PluginRepository.SizePlugin
    override val name: String = "SizePlugin"
    override var update: Update? = null

    override fun update(update: Update) {

    }

    override fun <T> decode(data: ByteArray): T {
        return null as T
    }

    override fun <T> encode(data: Map<String, T>): ByteArray {
        val widthPkg = Binary.IntToBytes(data["width"] as Int, 2)
        val heightPkg = Binary.IntToBytes(data["height"] as Int, 2)

        return widthPkg.plus(heightPkg)
    }

    override fun syncWith(entity: Entity) {
        entity.client?.sendUpdate(
            Update(
                entity = this.entity.uid.toString(),
                plugin = PluginRepository.SizePlugin.ordinal,
                data = this.encode(
                    mapOf(
                        Pair("width", width),
                        Pair("height", height)
                    )
                )
            )
        )
    }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
}