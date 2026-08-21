package com.vvc.edge.gatekeeper.domain.usecase

import com.vvc.edge.gatekeeper.domain.model.FaceVector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

class VerifyFaceVectorUseCase {

    suspend fun execute(capturedVector: FloatArray, patternVector: FloatArray): Boolean = withContext(Dispatchers.Default) {
        val captured = FaceVector.from(capturedVector) ?: return@withContext false
        val pattern = FaceVector.from(patternVector) ?: return@withContext false

        var sum = 0.0
        for (i in captured.data.indices) {
            val diff = captured.data[i].toDouble() - pattern.data[i].toDouble()
            sum += diff * diff
        }

        val distance = sqrt(sum)
        distance <= ACCEPTANCE_THRESHOLD.toDouble()
    }

    companion object {
        const val ACCEPTANCE_THRESHOLD = 0.4f
    }
}
