package com.vvc.edge.gatekeeper.data.datasource

import android.content.Context
import com.vvc.edge.gatekeeper.domain.model.FaceVector
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
        val validatedVector = requireNotNull(FaceVector.from(vector)) {
            "Only finite face vectors with ${FaceVector.DIMENSION} values can be stored"
        }
        val vectorString = validatedVector.data.joinToString(",")
        sharedPreferences.edit().putString(PATTERN_VECTOR_KEY, vectorString).apply()
    }

    fun getFaceVector(): FloatArray? {
        val vectorString = sharedPreferences.getString(PATTERN_VECTOR_KEY, null) ?: return null
        val values = vectorString.split(",").map { token ->
            token.toFloatOrNull() ?: return null
        }.toFloatArray()
        return FaceVector.from(values)?.data
    }

    private companion object {
        const val PATTERN_VECTOR_KEY = "pattern_vector"
    }
}
