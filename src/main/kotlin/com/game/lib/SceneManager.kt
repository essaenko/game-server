package com.game.lib

import com.game.config.SceneConfig

class SceneManager {
    private val scenes: MutableMap<String, Scene> = mutableMapOf()

    fun initScene(config: SceneConfig) {
        scenes[config.name] = Scene(config)
    }

    fun getScene(name: String): Scene? {
        return this.scenes[name]
    }

    fun getScenes(): Map<String, Scene> {
        return scenes
    }
}