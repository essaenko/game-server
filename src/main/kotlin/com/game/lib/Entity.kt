package com.game.lib

import com.game.helper.Update
import com.game.plugin.PositionPlugin
import com.game.plugin.SizePlugin
import java.util.*

open class Entity(uuid: UUID, val game: Game) {
    val updates: Queue<Update> = LinkedList()
    var client: Client? = null
    val uid: UUID = uuid
    var isNPC: Boolean = true
    var scene: Scene? = null


    val plugins: MutableMap<PluginRepository, Plugin> = mutableMapOf(
        Pair(PluginRepository.PositionPlugin, PositionPlugin(this)),
        Pair(PluginRepository.SizePlugin, SizePlugin(this)),
    )

    fun usePlugin(plugin: Plugin) {
        this.plugins[plugin.id] = plugin
    }

    fun useClient(cl: Client) {
        client = cl;
    }

    fun update() {
        while(!updates.isEmpty()) {
            val update = updates.poll()
            val plugin = PluginRepository.values()[update.plugin]

            if (plugins.containsKey(plugin)) {
                plugins[plugin]?.update(update)
            }
        }

        for ((_, plugin) in plugins) {
            plugin.apply()
        }
    }

    fun syncWith(entity: Entity) {
        for ((_, plugin) in plugins) {
            plugin.syncWith(entity)
        }
    }
}