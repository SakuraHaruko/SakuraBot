package moe.nekocafe.sakurabot.feature.features

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import moe.nekocafe.sakurabot.feature.Feature
import moe.nekocafe.sakurabot.util.HttpUtils
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.QuoteReply
import java.lang.reflect.Method

class Hitokoto : Feature {
    private fun hitokotoMessage(): String {
        var jsonString: String = HttpUtils.doGet("https://v1.hitokoto.cn/")
        if (jsonString.isEmpty()) jsonString = HttpUtils.doGet("https://v1.hitokoto.cn/")
        val res: JSONObject = JSON.parseObject(jsonString)

        return java.lang.String.format(
            "%s\n\n—— %s\n(来自hitokoto.cn, ID: %s)",
            res.get("hitokoto"), res.get("from").toString().replace("\"", ""), res.get("id")
        )
    }

    override fun onEnable(groupID: Long, senderID: Long, args: Array<Message>, quoteReply: QuoteReply?): MessageChain {
        return MessageChainBuilder().append(hitokotoMessage()).build()
    }

    override fun register(): Map<String, Any> {
        val info: MutableMap<String, Any> = HashMap()
        val commands: MutableMap<String, Method> = HashMap()
        val usages: MutableMap<String, String> = HashMap()

        commands["hitokoto"] = Hitokoto::class.java.getMethod(
            "onEnable",
            Long::class.java,
            Long::class.java,
            Array<Message>::class.java,
            QuoteReply::class.java
        )

        usages["hitokoto"] = "!hitokoto - 获取一言"

        info["name"] = "hitokoto"
        info["commands"] = commands
        info["usages"] = usages
        info["author"] = "Maplef"
        info["description"] = "获取一言"
        info["version"] = "1.3"

        return info
    }
}