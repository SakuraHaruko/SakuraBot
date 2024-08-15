package moe.nekocafe.sakurabot.config

import kotlinx.serialization.Serializable

@Serializable
data class SakuraBotConfig(
    var address: String = "ws://localhost:3001",
    var accessToken: String = "awa",
    var commandPrefix: String = "!",
    var enableWhitelist: Boolean = false,
    var enableBlacklist: Boolean = false,
    var groupWhitelist: List<String> = listOf("100111","111032"),
    var groupBlacklist: List<String> = listOf("119111","111111")
)
