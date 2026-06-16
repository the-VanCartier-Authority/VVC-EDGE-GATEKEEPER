package com.vvc.edge.gatekeeper.data.datasource

import com.vvc.edge.gatekeeper.domain.model.FaceVector

/**
 * Contrato estricto para el almacenamiento local cifrado.
 * Garantiza que los vectores biométricos no sean legibles en texto plano en el dispositivo.
 */
interface EncryptedStorage {
    suspend fun saveFacePattern(faceVector: FaceVector): Boolean
    suspend fun getFacePattern(id: String): FaceVector?
    suspend fun deleteFacePattern(id: String): Boolean
}

