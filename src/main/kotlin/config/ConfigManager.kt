package moe.nekocafe.sakurabot.config

import com.alibaba.fastjson2.JSONWriter
import com.alibaba.fastjson2.toJSONString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class ConfigManager {
    var sakuraBotConfig: SakuraBotConfig? = null

    fun load() {
        val configFile = File("config.json")
        val config = Json.decodeFromString<SakuraBotConfig>(configFile.readText())
        sakuraBotConfig = config
    }

    fun createDefaultConfig() {
        val json = SakuraBotConfig().toJSONString(JSONWriter.Feature.PrettyFormat)
        File("config.json").writeText(json)
    }
}