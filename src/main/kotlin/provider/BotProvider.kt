package moe.nekocafe.sakurabot.provider

import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.MessageChain
import top.mrxiaom.overflow.BotBuilder

object BotProvider {
    private var bot: Bot? = null

    suspend fun connect() {
        val bot = BotBuilder.positive("ws://127.0.0.1:3001")
            .token("awa")
            .connect()

        if (bot != null) {
            this.bot = bot
        }
    }

    suspend fun sendGroupMessage(groupID: Long, message: MessageChain) {
        bot?.getGroup(groupID)?.sendMessage(message)
    }

    suspend fun sendGroupMessage(groupID: Long, message: String) {
        bot?.getGroup(groupID)?.sendMessage(message)
    }

    fun getBot(): Bot? {
        return bot
    }

}