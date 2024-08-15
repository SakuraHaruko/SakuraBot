package moe.nekocafe.sakurabot.listener

import moe.nekocafe.sakurabot.SakuraBot
import moe.nekocafe.sakurabot.exception.FeatureNotFoundException
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext


object GroupMessageListener : SimpleListenerHost() {
    private val featureManager = SakuraBot.featureManager
    private val configManager = SakuraBot.configManager

    override fun handleException(context: CoroutineContext, exception: Throwable) {

    }

    @EventHandler
    suspend fun GroupMessageEvent.onMessage() {
        val commandPattern =
            ("^" + configManager.sakuraBotConfig!!.commandPrefix + "[\\u4E00-\\u9FA5A-Za-z0-9_]+(\\s([\\u4E00-\\u9FA5A-Za-z0-9_\\[\\]\\s]|[^\\x00-\\xff])+)?$")

        val messageContent: MessageContent =
            message[MessageContent] ?: return

        val textString = messageContent.contentToString().trim { it <= ' ' }
        if (!Pattern.matches(commandPattern, textString)) return

        val command = textString.split(" ".toRegex(), limit = 2).toTypedArray()[0].substring(1)

        val argsList: MutableList<Message> = ArrayList()
        for (_message in message) {
            if (_message is PlainText) {
                for (arg in _message.contentToString().split(" ")) argsList.add(PlainText(arg))
            } else {
                argsList.add(_message)
            }
        }
        argsList.removeAt(0)
        argsList.removeAt(0)

        val quoteReply: QuoteReply? = message[QuoteReply]

        val args = argsList.toTypedArray()

        val message = MessageChainBuilder()
        try {
            message.append(
                featureManager.handler(
                    command,
                    group.id,
                    sender.id,
                    args,
                    quoteReply
                )
            )
        } catch (ex: FeatureNotFoundException) {
            message.append(ex.message)
        } catch (ignored: NullPointerException) {
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        if (message.build().isEmpty()) {
            return
        }
        group.sendMessage(message.build())
    }
}
