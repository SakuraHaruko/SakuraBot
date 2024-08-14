package moe.nekocafe.sakurabot.util

import org.apache.http.HttpEntity
import org.apache.http.ParseException
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.io.IOException

object HttpUtils {
    fun doGet(url: String?): String {
        try {
            HttpClients.createDefault().use { httpClient ->
                val httpGet = HttpGet(url)
                try {
                    httpClient.execute(httpGet).use { response ->
                        val entity: HttpEntity = response.entity
                        val content: String = EntityUtils.toString(entity)
                        EntityUtils.consume(entity)
                        return content
                    }
                } catch (e: ParseException) {
                    throw RuntimeException(e)
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}