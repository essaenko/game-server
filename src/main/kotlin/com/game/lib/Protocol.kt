package com.game.lib

import com.game.helper.Message
import com.game.helper.MessagePayload
import com.game.helper.SocketEvents
import com.game.helper.Update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Protocol {
    fun decode(pkg: ByteArray): Message? {
        when(pkg[0].toInt()) {
            SocketEvents.SceneConfigs.ordinal -> {
                return Message(
                    event = SocketEvents.SceneConfigs
                )
            }
            SocketEvents.InitCharacter.ordinal -> {
                return Message(
                    event = SocketEvents.InitCharacter,
                    payload = MessagePayload(
                        scene = pkg.decodeToString(1)
                    )
                )
            }
            SocketEvents.Update.ordinal -> {
                return Message(
                    event = SocketEvents.Update,
                    payload = MessagePayload(
                        update = Update(
                            plugin = pkg[1].toInt(),
                            data = pkg.sliceArray(2 until pkg.size)
                        )
                    )
                )
            }
        }

        return null
    }

    fun encode(message: Message): ByteArray? {
        when(message.event) {
            SocketEvents.ClientId -> {
                if (message.payload?.id != null) {
                    val idPackage: ByteArray = message.payload.id.toByteArray()
                    var pkg = ByteArray(1)
                    pkg[0] = SocketEvents.ClientId.ordinal.toByte()
                    pkg = pkg.plus(idPackage)

                    return pkg
                }
            }
            SocketEvents.InitCharacter -> {
                val pkg = ByteArray(1)
                pkg[0] = SocketEvents.InitCharacter.ordinal.toByte()

                return pkg
            }
            SocketEvents.Update -> {
                if (message.payload?.update?.entity != null) {
                    var pkg = ByteArray(1)
                    val pluginPkg = message.payload.update.plugin.toByte()
                    val entityPkg = message.payload.update.entity?.toByteArray()!!
                    pkg[0] = SocketEvents.Update.ordinal.toByte()

                    pkg = pkg.plus(pluginPkg).plus(entityPkg).plus(message.payload.update.data)

                    return pkg
                }
            }
            SocketEvents.SceneConfigs -> {
                if (message.payload?.scenes != null) {
                    var pkg = ByteArray(1)
                    pkg[0] = SocketEvents.SceneConfigs.ordinal.toByte()
                    val scenesPackage: ByteArray = message.payload.scenes.map { scene -> scene.name }.toString().toByteArray()
                    pkg = pkg.plus(scenesPackage)

                    return pkg
                }
            }
            SocketEvents.NewPlayer -> {
                if (message.payload?.player != null) {
                    val pkg = ByteArray(1)
                    val playerPkg = message.payload.player.toByteArray()
                    pkg[0] = SocketEvents.NewPlayer.ordinal.toByte()


                    return pkg.plus(playerPkg)
                }
            }
            SocketEvents.NewNPCInit -> {
                if (message.payload?.npc != null) {
                    val pkg = ByteArray(1)
                    val npcPkg = message.payload.npc.toByteArray()
                    pkg[0] = SocketEvents.NewNPCInit.ordinal.toByte()


                    return pkg.plus(npcPkg)
                }
            }
            SocketEvents.PlayerLeave -> {
                if (message.payload?.player != null) {
                    val pkg = ByteArray(1)
                    val npcPkg = message.payload.player.toByteArray()
                    pkg[0] = SocketEvents.PlayerLeave.ordinal.toByte()


                    return pkg.plus(npcPkg)
                }
            }
        }

        return null
    }

}