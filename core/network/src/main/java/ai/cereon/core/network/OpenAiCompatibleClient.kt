package ai.cereon.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class ChatTurn(val role: String, val content: String)
data class ProviderModel(val id: String, val ownedBy: String? = null)

class OpenAiCompatibleClient {
    private val http = OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(0, TimeUnit.MILLISECONDS).callTimeout(0, TimeUnit.MILLISECONDS).build()
    fun stream(baseUrl: String, apiKey: String, model: String, turns: List<ChatTurn>): Flow<String> = callbackFlow {
        val payload = JSONObject().put("model", model).put("stream", true).put("messages", JSONArray().apply { turns.forEach { put(JSONObject().put("role", it.role).put("content", it.content)) } })
        val request = Request.Builder().url(endpoint(baseUrl, "chat/completions")).header("Authorization", "Bearer $apiKey").header("Accept", "text/event-stream").post(payload.toString().toRequestBody("application/json".toMediaType())).build()
        val source = EventSources.createFactory(http).newEventSource(request, object : EventSourceListener() {
            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                if (data == "[DONE]") { close(); return }
                try { val delta = JSONObject(data).optJSONArray("choices")?.optJSONObject(0)?.optJSONObject("delta")?.optString("content").orEmpty(); if (delta.isNotEmpty()) trySend(delta) } catch (e: Exception) { close(IllegalStateException("Invalid streaming response payload", e)) }
            }
            override fun onFailure(eventSource: EventSource, t: Throwable?, response: okhttp3.Response?) { close(t ?: IllegalStateException("Provider stream failed (HTTP ${response?.code ?: "unknown"})")) }
            override fun onClosed(eventSource: EventSource) { close() }
        })
        awaitClose { source.cancel() }
    }
    suspend fun discoverModels(baseUrl: String, apiKey: String): List<ProviderModel> {
        val request = Request.Builder().url(endpoint(baseUrl, "models")).header("Authorization", "Bearer $apiKey").get().build()
        return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) { http.newCall(request).execute().use { response -> if (!response.isSuccessful) error("Model discovery failed: HTTP ${response.code}"); val json = JSONObject(response.body?.string().orEmpty()); val data = json.optJSONArray("data") ?: JSONArray(); (0 until data.length()).mapNotNull { i -> data.optJSONObject(i)?.let { ProviderModel(it.optString("id"), it.optString("owned_by").ifBlank { null }) } } } }
    }
    private fun endpoint(base: String, path: String): String { val normalized = base.trimEnd('/'); require(normalized.startsWith("https://") || normalized.startsWith("http://10.0.2.2")) { "Use HTTPS for provider endpoints" }; return normalized + "/" + path.trimStart('/') }
}
