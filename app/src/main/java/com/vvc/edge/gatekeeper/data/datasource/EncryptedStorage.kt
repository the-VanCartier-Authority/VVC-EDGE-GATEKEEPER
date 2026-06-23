package com.vvc.edge.gatekeeper.data.datasource

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptedStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "vvc_gatekeeper_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveFaceVector(vector: FloatArray) {
        val vectorString = vector.joinToString(",")
        sharedPreferences.edit().putString("pattern_vector", vectorString).apply()
    }

    fun getFaceVector(): FloatArray? {
        val vectorString = sharedPreferences.getString("pattern_vector", null) ?: return null
        return vectorString.split(",").map { it.toFloat() }.toFloatArray()
    }
}
