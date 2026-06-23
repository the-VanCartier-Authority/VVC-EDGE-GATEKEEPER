package com.vvc.edge.gatekeeper.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

class VerifyFaceVectorUseCase {

    private val THRESHOLD = 0.4f

    suspend fun execute(capturedVector: FloatArray, patternVector: FloatArray): Boolean = withContext(Dispatchers.Default) {
        if (capturedVector.size != patternVector.size) return@withContext false

        var sum = 0f
        for (i in capturedVector.indices) {
            val diff = capturedVector[i] - patternVector[i]
            sum += diff * diff
        }
        
        val distance = sqrt(sum)
        distance <= THRESHOLD
    }
}
