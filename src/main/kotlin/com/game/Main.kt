package com.game

import com.game.helper.Message
import com.game.helper.SocketEvents
import com.game.lib.Game
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.util.logging.Logger

fun main() {
    val game = Game();

    embeddedServer(Netty, 9000) {
        install(WebSockets)
        install(ContentNegotiation) {
            json()
        }

        routing {
            webSocket("/socket") {
                val client = game.addClient(this)
                val logger = Logger.getAnonymousLogger()

                try {
                    for(frame in incoming) {
                        frame as? Frame.Binary ?: continue
                        val bytes = frame.readBytes()
                        val message = client.protocol.decode(bytes)

                        if (message != null) {
                            client.handleIncomingMessage(message)
                        }
                    }
                } catch(e: Exception) {
                    println("Catch connection error $e")
                    print(e.stackTraceToString())
                } finally {
                    game.removeClient(client)
                }
            }
        }
    }.start(true);
}