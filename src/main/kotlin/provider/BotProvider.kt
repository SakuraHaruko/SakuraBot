package moe.nekocafe.sakurabot.provider

import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.MessageChain
import top.mrxiaom.overflow.BotBuilder

object BotProvider {
    var bot: Bot? = null

    suspend fun connect(address: String, accessToken: String) {
        val bot = BotBuilder.positive(address)
            .token(accessToken)
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
}