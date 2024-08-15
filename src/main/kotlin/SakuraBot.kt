package moe.nekocafe.sakurabot

import moe.nekocafe.sakurabot.config.ConfigManager
import moe.nekocafe.sakurabot.feature.FeatureManager
import moe.nekocafe.sakurabot.listener.GroupMessageListener
import moe.nekocafe.sakurabot.provider.BotProvider
import net.mamoe.mirai.utils.MiraiLogger
import java.io.File


suspend fun main() {
    println("""
  _____       _                    ____        _   
 / ____|     | |                  |  _ \      | |  
| (___   __ _| | ___   _ _ __ __ _| |_) | ___ | |_ 
 \___ \ / _` | |/ / | | | '__/ _` |  _ < / _ \| __|
 ____) | (_| |   <| |_| | | | (_| | |_) | (_) | |_ 
|_____/ \__,_|_|\_\\__,_|_|  \__,_|____/ \___/ \__|

    """.trimIndent())
    val startTime = System.currentTimeMillis()
    SakuraBot.logger.info("Starting ${SakuraBot.app_name} version ${SakuraBot.app_version}")

    if (!File("config.json").exists()) {
        SakuraBot.logger.warning("config.json Not Found, Creating...")
        SakuraBot.configManager.createDefaultConfig()
    }

    SakuraBot.configManager.load()
    SakuraBot.featureManager.load()

    SakuraBot.logger.info("Connecting to ${SakuraBot.configManager.sakuraBotConfig?.address}")
    SakuraBot.configManager.sakuraBotConfig?.let {
        BotProvider.connect(it.address, it.accessToken)
    }

    val bot = BotProvider.bot

    if (bot == null) {
        SakuraBot.logger.info("Connection failure.")
        return
    }

    bot.eventChannel.registerListenerHost(GroupMessageListener)

    SakuraBot.logger.info("Done! (${System.currentTimeMillis() - startTime}ms)")
}

object SakuraBot {
    var app_name = "SakuraBot"
    var app_version = "1.0.0"
    var logger = MiraiLogger.Factory.create(SakuraBot::class, "SakuraBot")
    var configManager = ConfigManager()
    var featureManager = FeatureManager()
}