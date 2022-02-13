package com.game.lib

import com.game.config.SceneConfig
import java.util.*

class Scene(config: SceneConfig) {
    val name: String = config.name

    private val entities: MutableMap<UUID, Entity> = mutableMapOf()
    val players: Collection<Entity>
    get() {
        return this.entities.filterValues { !it.isNPC }.values
    }
    val npcs: Collection<Entity>
    get() {
        return this.entities.filterValues { it.isNPC }.values
    }

    fun useEntity(entity: Entity) {
        this.entities[entity.uid] = entity
        entity.scene = this

        for (player in players) {
            if (entity.uid != player.uid) {
                entity.client?.sendNewPlayerInit(player)
                player.syncWith(entity)
                player.client?.sendNewPlayerInit(entity)
                entity.syncWith(player)
            }
        }

        for (npc in npcs) {
            entity.client?.sendNPCInit(npc)
        }
    }

    fun releaseEntity(entity: Entity) {
        this.entities.remove(entity.uid)

        players.forEach { player ->
            player.client?.sendPlayerLeave(entity)
        }
    }

    fun update() {
        for((_, entity) in entities) {
            entity.update();
        }
    }
}