package io.mustelidae.smoothcoatedotter.api.sender

import io.mustelidae.smoothcoatedotter.api.config.AppEnvironment
import io.mustelidae.smoothcoatedotter.utils.ClientSupport
import io.mustelidae.smoothcoatedotter.utils.toJson
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.slf4j.LoggerFactory

class StableSlackClient(
    private val env: AppEnvironment.Slack,
    private val httpClient: CloseableHttpClient
) : SlackClient, ClientSupport(
    SlackResources.getMapper(),
    env.logging,
    LoggerFactory.getLogger(StableSlackClient::class.java)
) {

    override fun incomingWebhook(path: String, payload: SlackResources.Payload) {
        val header = listOf("Content-Type" to "application/json", "charset" to "utf-8")

        val url = "${env.host}$path"
        log.info("send to slack via incoming webhook [path=$path/payload=${payload.toJson()}]")
        httpClient.post(url, header, payload).orElseThrow()
    }

    override fun chatBot(token: String, channel: String, payload: SlackResources.Payload) {
        val url = "${env.host}/api/chat.postMessage"

        val header = listOf(
            "Content-Type" to "application/json",
            "Authorization" to "Bearer $token"
        )

        log.info("send to slack via token [url=$url/payload=${payload.toJson()}]")

        httpClient.post(
            url,
            header,
            PostMessage(
                payload.blocks,
                channel
            )
        ).orElseThrow()
    }

    private data class PostMessage(
        val blocks: List<BlockKit.Block>,
        val channel: String,
    )
}
