package moe.nekocafe.sakurabot.config

import kotlinx.serialization.Serializable

@Serializable
data class SakuraBotConfig(
    var address: String = "ws://localhost:3001",
    var accessToken: String = "awa",
    var commandPrefix: String = "!",
    var enableWhitelist: Boolean = false,
    var enableBlacklist: Boolean = false,
    var groupWhitelist: List<Long> = listOf(1111111,2222222),
    var groupBlacklist: List<Long> = listOf(3333333,4444444)
)
