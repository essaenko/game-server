package com.game.plugin

import com.game.helper.Binary
import com.game.helper.Update
import com.game.lib.Entity
import com.game.lib.Plugin
import com.game.lib.PluginRepository

class PositionPlugin(override val entity: Entity) : Plugin {
  override val id: PluginRepository = PluginRepository.PositionPlugin
  override val name: String = "PositionPlugin"
  override var update: Update? = null

  var x: Float = 0f
  var y: Float = 0f

  override fun update(update: Update) {
    val data = decode<Map<String, Float>>(update.data)
    println("Update call: ${data["x"]}, ${data["y"]} new position: ${x + (data["x"] ?: 0f)}, ${y + (data["y"] ?: 0f)}")
    this.setPosition(x + (data["x"] ?: 0f), y + (data["y"] ?: 0f))

    this.update = Update(
      plugin = id.ordinal,
      data = encode(
        mapOf(
          Pair("x", x),
          Pair("y", y)
        ),
      )
    )
  }

  override fun <T> encode(data: Map<String, T>): ByteArray {
    val xPkg = Binary.FloatToBytes(data["x"] as Float, 4)
    val yPkg = Binary.FloatToBytes(data["y"] as Float, 4)

    return xPkg.plus(yPkg)
  }

  override fun <T> decode(data: ByteArray): T {
    return mapOf(
      Pair("x", Binary.BytesToFloat(data.sliceArray(0..3))),
      Pair("y", Binary.BytesToFloat(data.sliceArray(4..7)))
    ) as T
  }

  override fun syncWith(entity: Entity) {
    entity.client?.sendUpdate(
      Update(
        plugin = id.ordinal,
        entity = this.entity.uid.toString(),
        data = encode(
          mapOf(
            Pair("x", x),
            Pair("y", y)
          ),
        )
      )
    )
  }

  private fun setPosition(x: Float, y: Float) {
    this.x = x
    this.y = y
  }
}