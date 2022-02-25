package com.game.lib

import com.game.config.SceneConfig
import com.game.config.SceneMap
import com.game.entity.Character
import com.game.helper.*
import io.ktor.http.cio.websocket.*
import java.util.*
import kotlinx.coroutines.*

class Client(uuid: UUID, socket: DefaultWebSocketSession, game: Game) {
  val uid: UUID
  val protocol: Protocol = Protocol()
  private val socket: DefaultWebSocketSession
  private val game: Game
  private var entity: Character? = null

  init {
    this.socket = socket
    this.uid = uuid
    this.game = game

    this.sendIDToClient()
  }

  private fun sendMessage(pkg: ByteArray?) = runBlocking {
    launch {
      if (pkg != null) {
        socket.send(pkg)
      }
    }
  }

  private fun sendIDToClient() {
    this.sendMessage(
      this.protocol.encode(
        Message(
          event = SocketEvents.ClientId,
          payload = MessagePayload(
            id = this.uid.toString()
          )
        )
      )
    )
  }

  private fun sendSceneConfigsToClient(configs: List<SceneConfig>) {
    this.sendMessage(
      this.protocol.encode(
        Message(
          event = SocketEvents.SceneConfigs,
          payload = MessagePayload(
            scenes = configs,
          )
        )
      )
    )
  }

  fun destroy() {
    if (this.entity != null && this.entity?.scene != null) {
      this.entity?.scene?.releaseEntity(this.entity!!)
    }
  }

  fun handleIncomingMessage(message: Message) {
    when (message.event) {
      SocketEvents.SceneConfigs -> {
        this.sendSceneConfigsToClient(SceneMap)
      }
      SocketEvents.InitCharacter -> {
        if (message.payload?.scene != null) {
          val scene = this.game.sceneManager.getScene(message.payload.scene.toString())
          entity = Character(uid, game)
          entity?.useClient(this)
          scene?.useEntity(entity!!)

          this.sendMessage(
            this.protocol.encode(
              Message(
                event = SocketEvents.InitCharacter
              )
            )
          )

          entity?.syncWith(entity!!)
        }
      }
      SocketEvents.Update -> {
        if (message.payload?.update != null) {
          entity?.updates?.add(
            Update(
              plugin = message.payload.update.plugin,
              data = message.payload.update.data,
            )
          )
        }
      }
      else -> {

      }
    }
  }

  fun sendUpdate(update: Update) {
    this.sendMessage(
      this.protocol.encode(
        Message(
          event = SocketEvents.Update,
          payload = MessagePayload(
            update = update,
          )
        )
      )
    )
  }

  fun sendNPCInit(entity: Entity) {
    this.sendMessage(
      this.protocol.encode(
        Message(
          event = SocketEvents.NewNPCInit,
          payload = MessagePayload(
            npc = entity.uid.toString(),
          )
        )
      )
    )
  }

  fun sendNewPlayerInit(entity: Entity) {
    this.sendMessage(
      this.protocol.encode(
        Message(
          event = SocketEvents.NewPlayer,
          payload = MessagePayload(
            player = entity.uid.toString(),
          )
        )
      )
    )
  }

  fun sendPlayerLeave(entity: Entity) {
    this.sendMessage(
      this.protocol.encode(
        Message(
          event = SocketEvents.PlayerLeave,
          payload = MessagePayload(
            player = entity.uid.toString(),
          )
        )
      )
    )
  }
}