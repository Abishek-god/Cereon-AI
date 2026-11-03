package ai.cereon.core.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/** AES-GCM encryption with a non-exportable Android Keystore key. */
class SecretStore(context: Context) {
    private val preferences = context.getSharedPreferences("cereon_secrets", Context.MODE_PRIVATE)
    private val alias = "cereon_provider_key_v1"
    fun put(name: String, value: String) {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        val encoded = Base64.encodeToString(cipher.iv + encrypted, Base64.NO_WRAP)
        check(preferences.edit().putString(name, encoded).commit()) { "Unable to persist encrypted credential" }
    }
    fun get(name: String): String? {
        val encoded = preferences.getString(name, null) ?: return null
        return try { val bytes = Base64.decode(encoded, Base64.NO_WRAP); val iv = bytes.copyOfRange(0, 12); val ciphertext = bytes.copyOfRange(12, bytes.size); val cipher = Cipher.getInstance("AES/GCM/NoPadding"); cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), GCMParameterSpec(128, iv)); String(cipher.doFinal(ciphertext), Charsets.UTF_8) } catch (_: Exception) { null }
    }
    fun remove(name: String) { preferences.edit().remove(name).apply() }
    private fun getOrCreateKey(): SecretKey {
        val store = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        (store.getKey(alias, null) as? SecretKey)?.let { return it }
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        generator.init(KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).setRandomizedEncryptionRequired(true).build())
        return generator.generateKey()
    }
}
