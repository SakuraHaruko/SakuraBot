package moe.nekocafe.sakurabot.feature

import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.QuoteReply

interface Feature {
    @Throws(Exception::class)
    fun onEnable(
        groupID: Long,
        senderID: Long,
        args: Array<Message>,
        quoteReply: QuoteReply?
    ): MessageChain

    @Throws(NoSuchMethodException::class)
    fun register(): Map<String, Any>
}