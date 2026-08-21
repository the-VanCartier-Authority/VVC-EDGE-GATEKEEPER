package com.vvc.edge.gatekeeper.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FaceVectorTest {
    @Test
    fun validVectorIsCopiedAndAccepted() {
        val source = FloatArray(FaceVector.DIMENSION) { it.toFloat() }

        val result = FaceVector.from(source)
        source[0] = -1f

        assertNotNull(result)
        assertEquals(0f, result!!.data[0])
    }

    @Test
    fun wrongDimensionIsRejected() {
        assertNull(FaceVector.from(FloatArray(FaceVector.DIMENSION - 1)))
        assertNull(FaceVector.from(FloatArray(FaceVector.DIMENSION + 1)))
    }

    @Test
    fun nonFiniteValuesAreRejected() {
        val nanVector = FloatArray(FaceVector.DIMENSION).apply { this[10] = Float.NaN }
        val infiniteVector = FloatArray(FaceVector.DIMENSION).apply { this[10] = Float.POSITIVE_INFINITY }

        assertNull(FaceVector.from(nanVector))
        assertNull(FaceVector.from(infiniteVector))
    }
}
