package com.game.entity

import com.game.lib.Entity
import com.game.lib.Game
import com.game.lib.PluginRepository
import com.game.plugin.ControllerPlugin
import com.game.plugin.SizePlugin
import java.util.*

class Character(uuid: UUID, game: Game) : Entity(uuid, game) {
  init {
    isNPC = false
    speed = 5

    usePlugin(ControllerPlugin(this))

    (plugins[PluginRepository.SizePlugin] as SizePlugin).setSize(15, 15)
  }
}