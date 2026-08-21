package com.vvc.edge.gatekeeper.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VerifyFaceVectorUseCaseTest {
    private val useCase = VerifyFaceVectorUseCase()

    @Test
    fun identicalVectorsAreAccepted() = runTest {
        val pattern = vectorWith(first = 0.25f)

        assertTrue(useCase.execute(pattern, pattern.copyOf()))
    }

    @Test
    fun vectorsAtAcceptanceThresholdAreAccepted() = runTest {
        val captured = vectorWith(first = VerifyFaceVectorUseCase.ACCEPTANCE_THRESHOLD)
        val pattern = FloatArray(128)

        assertTrue(useCase.execute(captured, pattern))
    }

    @Test
    fun vectorsAboveAcceptanceThresholdAreRejected() = runTest {
        val captured = vectorWith(first = 0.4001f)
        val pattern = FloatArray(128)

        assertFalse(useCase.execute(captured, pattern))
    }

    @Test
    fun vectorsWithWrongDimensionAreRejected() = runTest {
        assertFalse(useCase.execute(FloatArray(127), FloatArray(128)))
    }

    @Test
    fun vectorsWithNonFiniteValuesAreRejected() = runTest {
        val captured = vectorWith(first = Float.NaN)
        val pattern = FloatArray(128)

        assertFalse(useCase.execute(captured, pattern))
    }

    private fun vectorWith(first: Float): FloatArray = FloatArray(128).apply {
        this[0] = first
    }
}
