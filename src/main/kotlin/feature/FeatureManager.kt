package moe.nekocafe.sakurabot.feature

import moe.nekocafe.sakurabot.exception.FeatureNotFoundException
import moe.nekocafe.sakurabot.exception.InvalidSyntaxException
import moe.nekocafe.sakurabot.util.Reflection
import net.mamoe.mirai.message.data.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class FeatureManager {
    private var featureClasses: List<KClass<out Feature>> = Reflection().findSubTypes("moe.nekocafe.sakurabot.feature.features", Feature::class) as List<KClass<out Feature>>

    @Throws(Exception::class)
    fun handler(
        command: String,
        groupID: Long,
        senderID: Long,
        args: Array<Message>,
        quoteReply: QuoteReply?
    ): MessageChain {
        return when (command) {
            "help", "菜单" -> {
                val helpMsg = MessageChainBuilder().apply {
                    append(At(senderID)).append(" 当前可用的指令有\n(其中\"<>\"内的为必填参数，\"[]\"内的为可选参数)\n——————\n")
                    for (singleClass in featureClasses) {
                        val pluginInfo = singleClass.createInstance().register() as? Map<String, Any>
                        if (pluginInfo != null) {
                            val pluginUsage = pluginInfo["usages"] as? Map<String, String>
                            pluginUsage?.forEach { (_, value) ->
                                append(value).append("\n")
                            }
                        }
                    }
                }
                helpMsg.build()
            }
            "about", "关于" -> {
                val aboutMsg = MessageChainBuilder().append("""
                    SakuraBot
                    
                    By SakuraHaruko
                    
                    Mirai + Overflow + OneBot + Kotlin
                """.trimIndent()).build()
                aboutMsg
            }
            else -> {
                for (singleClass in featureClasses) {
                    val pluginInfo = singleClass.createInstance().register() as? Map<String, Any>
                    if (pluginInfo != null) {
                        val pluginCommands = pluginInfo["commands"] as? Map<String, Method>
                        if (pluginCommands?.containsKey(command) == true) {
                            try {
                                return pluginCommands[command]?.invoke(
                                    singleClass.createInstance(),
                                    groupID, senderID, args, quoteReply
                                ) as MessageChain
                            } catch (e: InvocationTargetException) {
                                val errorMsg = MessageChainBuilder().apply {
                                    append(At(senderID)).append(" ")
                                    when (e.targetException) {
                                        is InvalidSyntaxException -> {
                                            val pluginUsage = pluginInfo["usages"] as? Map<String, String>
                                            append(" 语法错误\n用法: ").append(pluginUsage?.get(command))
                                        }
                                        else -> {
                                            append(e.targetException.message)
                                        }
                                    }
                                }
                                return errorMsg.build()
                            }
                        }
                    }
                }
                throw FeatureNotFoundException()
            }
        }
    }
}