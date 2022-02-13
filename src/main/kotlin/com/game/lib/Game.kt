package com.game.lib

import com.game.config.SceneMap
import io.ktor.http.cio.websocket.*
import java.util.*

class Game() {
    val sceneManager: SceneManager = SceneManager()
    private val clients: MutableMap<UUID, Client> = mutableMapOf()

    init {
        this.readSceneConfigs()
        this.initGameLoop()
    }

    private fun readSceneConfigs() {
        SceneMap.forEach { config ->
            sceneManager.initScene(config)
        }
    }

    private fun initGameLoop() {
        val timer = Timer()
        timer.schedule(object: TimerTask() {
            override fun run() {
                tick()
            }
        }, 0L, 20L)
    }

    private fun tick() {
        val scenes: Map<String, Scene> = this.sceneManager.getScenes()
        for ((_, scene) in scenes) {
            scene.update()
        }
    }

    fun addClient(socket: DefaultWebSocketSession): Client {
        val uuid: UUID = UUID.randomUUID()
        val client = Client(uuid, socket, this)
        this.clients[uuid] = client

        return client
    }

    fun removeClient(client: Client) {
        client.destroy()
        this.clients.remove(client.uid)
    }
}