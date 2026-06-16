package com.vvc.edge.gatekeeper.data.datasource

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.vvc.edge.gatekeeper.domain.model.FaceVector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementación de almacenamiento seguro utilizando AES-256 a través de Jetpack Security.
 * Forzado a correr fuera del hilo principal para optimizar la I/O de archivos.
 */
class EncryptedStorageImpl(private val context: Context) : EncryptedStorage {

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

    override suspend fun saveFacePattern(faceVector: FaceVector): Boolean = 
        withContext(Dispatchers.IO) {
            try {
                // Convertimos el FloatArray a una cadena de texto separada por comas para su almacenamiento simple
                val vectorString = faceVector.embeddings.joinToString(separator = ",") { it.toString() }
                
                sharedPreferences.edit().apply {
                    putString("vector_id_${faceVector.id}", faceVector.id)
                    putString("vector_token_${faceVector.id}", faceVector.token)
                    putString("vector_data_${faceVector.id}", vectorString)
                    putLong("vector_time_${faceVector.id}", faceVector.timestamp)
                    apply()
                }
                true
            } catch (e: Exception) {
                false
            }
        }

    override suspend fun getFacePattern(id: String): FaceVector? = 
        withContext(Dispatchers.IO) {
            try {
                val savedId = sharedPreferences.getString("vector_id_$id", null) ?: return@withContext null
                val token = sharedPreferences.getString("vector_token_$id", "") ?: ""
                val vectorString = sharedPreferences.getString("vector_data_$id", null) ?: return@withContext null
                val timestamp = sharedPreferences.getLong("vector_time_$id", 0L)

                // Reconstruimos el FloatArray de 128 posiciones
                val embeddings = vectorString.split(",").map { it.toFloat() }.toFloatArray()

                if (embeddings.size != 128) return@withContext null

                FaceVector(id = savedId, token = token, embeddings = embeddings, timestamp = timestamp)
            } catch (e: Exception) {
                null
            }
        }

    override suspend fun deleteFacePattern(id: String): Boolean = 
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit().apply {
                    remove("vector_id_$id")
                    remove("vector_token_$id")
                    remove("vector_data_$id")
                    remove("vector_time_$id")
                    apply()
                }
                true
            } catch (e: Exception) {
                false
            }
        }
}

