package org.openlake.sampoorna.data.repository

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import java.security.KeyStore
import java.util.ServiceLoader.load
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object CryptoManager {

    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "contact_key"

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

        return keyStore.getKey(KEY_ALIAS, null) as? SecretKey ?: run {
            val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            val keySpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()

            keyGen.init(keySpec)
            keyGen.generateKey()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        val encryptedBase64 = Base64.encodeToString(encrypted, Base64.DEFAULT).trim()
        val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT).trim()

        return "$encryptedBase64:$ivBase64"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun decrypt(data: String): String {
        val parts = data.split(":")
        if (parts.size != 2) return "" // or throw an exception

        val encryptedBytes = Base64.decode(parts[0], Base64.DEFAULT)
        val ivBytes = Base64.decode(parts[1], Base64.DEFAULT)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)

        return String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
    }
}
