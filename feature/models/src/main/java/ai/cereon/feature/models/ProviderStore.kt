package ai.cereon.feature.models

import android.content.Context
import ai.cereon.core.security.SecretStore
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class SavedProvider(val id: String, val name: String, val baseUrl: String, val model: String, val enabled: Boolean)

class ProviderStore(context: Context, private val secrets: SecretStore) {
    private val prefs = context.getSharedPreferences("cereon_providers", Context.MODE_PRIVATE)
    fun all(): List<SavedProvider> = try {
        val array = JSONArray(prefs.getString("items", "[]"))
        (0 until array.length()).map { i -> array.getJSONObject(i).let { SavedProvider(it.getString("id"), it.getString("name"), it.getString("baseUrl"), it.optString("model"), it.optBoolean("enabled", true)) } }
    } catch (_: Exception) { emptyList() }
    fun save(name: String, baseUrl: String, model: String, apiKey: String, existingId: String? = null): SavedProvider {
        require(name.isNotBlank() && baseUrl.startsWith("https://") && model.isNotBlank() && apiKey.isNotBlank())
        val id = existingId ?: UUID.randomUUID().toString()
        secrets.put("provider:$id", apiKey)
        val provider = SavedProvider(id, name.trim(), baseUrl.trimEnd('/'), model.trim(), true)
        val updated = all().filterNot { it.id == id } + provider
        val array = JSONArray().apply { updated.forEach { put(JSONObject().put("id", it.id).put("name", it.name).put("baseUrl", it.baseUrl).put("model", it.model).put("enabled", it.enabled)) } }
        check(prefs.edit().putString("items", array.toString()).commit())
        return provider
    }
    fun delete(id: String) {
        secrets.remove("provider:$id")
        val array = JSONArray().apply { all().filterNot { it.id == id }.forEach { put(JSONObject().put("id", it.id).put("name", it.name).put("baseUrl", it.baseUrl).put("model", it.model).put("enabled", it.enabled)) } }
        prefs.edit().putString("items", array.toString()).apply()
    }
}
