lpackage com.vvc.edge.gatekeeper.domain.usecase

import com.vvc.edge.gatekeeper.domain.model.FaceVector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

/**
 * Caso de Uso encargado de realizar la validación biométrica perimetral.
 * Fuerza el cómputo en un hilo de procesamiento secundario (Dispatchers.Default).
 */
class VerifyFaceVectorUseCase {

    // Umbral de tolerancia estricto para evitar falsos positivos
    private val MATCH_THRESHOLD = 0.4f 

    /**
     * Compara el vector de entrada actual contra el vector patrón guardado.
     * Retorna [True] si la distancia euclidiana es menor o igual al umbral (0.4f).
     */
    suspend fun execute(capturedVector: FloatArray, storedVector: FaceVector): Boolean = 
        withContext(Dispatchers.Default) {
            if (capturedVector.size != 128 || storedVector.embeddings.size != 128) {
                return@withContext false
            }

            var sum = 0.0f
            for (i in capturedVector.indices) {
                val diff = capturedVector[i] - storedVector.embeddings[i]
                sum += diff * diff
            }
            
            val distance = sqrt(sum)
            
            // Si la distancia matemática es menor al umbral, hay match de identidad
            return@withContext distance <= MATCH_THRESHOLD
        }
}
