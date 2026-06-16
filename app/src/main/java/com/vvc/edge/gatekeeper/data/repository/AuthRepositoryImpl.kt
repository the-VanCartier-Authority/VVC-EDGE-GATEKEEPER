package com.vvc.edge.gatekeeper.data.repository

import com.vvc.edge.gatekeeper.data.datasource.EncryptedStorage
import com.vvc.edge.gatekeeper.domain.model.FaceVector
import com.vvc.edge.gatekeeper.domain.usecase.VerifyFaceVectorUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio central de autenticación biométrica de la suite.
 * Coordina la extracción de patrones locales y la ejecución del caso de uso matemático.
 */
class AuthRepositoryImpl(
    private val encryptedStorage: EncryptedStorage,
    private val verifyFaceVectorUseCase: VerifyFaceVectorUseCase
) {
    /**
     * Valida el rostro capturado contra el patrón encriptado de un operador específico.
     * Ejecutado estrictamente en hilos de fondo.
     */
    suspend fun authenticateOperator(operatorId: String, capturedEmbeddings: FloatArray): Boolean =
        withContext(Dispatchers.Default) {
            val storedPattern = encryptedStorage.getFacePattern(operatorId) 
                ?: return@withContext false // Si no hay patrón registrado, deniega acceso inmediato.

            // Invoca la validación matemática por distancia euclidiana (Fase 1)
            return@withContext verifyFaceVectorUseCase.execute(capturedEmbeddings, storedPattern)
        }

    /**
     * Registra un nuevo patrón biométrico encriptado localmente.
     */
    suspend fun registerOperatorPattern(faceVector: FaceVector): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext encryptedStorage.saveFacePattern(faceVector)
        }
}

