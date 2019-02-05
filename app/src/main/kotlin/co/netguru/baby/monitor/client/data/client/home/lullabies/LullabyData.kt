package co.netguru.baby.monitor.client.data.client.home.lullabies

import co.netguru.baby.monitor.client.data.communication.websocket.Action

sealed class LullabyData {

    abstract val name: String

    data class LullabyInfo(
            override val name: String,
            val duration: String,
            var action: Action = Action.STOP,
            val source: String = ""
    ) : LullabyData()

    data class LullabyHeader(
            override val name: String
    ) : LullabyData()
}
