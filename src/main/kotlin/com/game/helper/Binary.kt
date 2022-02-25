package com.game.helper

import java.nio.ByteBuffer

class Binary {
  companion object {
    fun IntToBytes(number: Int, bytes: Int): ByteArray {
      val result = ByteArray(bytes)

      for (i in 0 until bytes) {
        result[i] = (number shr (i * 8)).toByte()
      }

      return result
    }

    fun FloatToBytes(number: Float, bytes: Int): ByteArray {
      return ByteBuffer.allocate(bytes).putFloat(number).array()
    }

    fun BytesToFloat(bytes: ByteArray): Float {
      return ByteBuffer.wrap(bytes).float
    }

    fun BytesToInt(number: ByteArray, bytes: Int): Int {
      var result = 0

      for (i in 0 until bytes) {
        result += (number[i].toInt() shl (i * 8))
      }

      return result
    }
  }
}