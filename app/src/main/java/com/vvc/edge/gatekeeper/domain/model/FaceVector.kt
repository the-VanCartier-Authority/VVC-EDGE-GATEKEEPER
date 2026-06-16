package com.vvc.edge.gatekeeper.domain.model

/**
 * Representa el embedding matemático del rostro extraído por el sensor.
 * Consta de un array estricto de 128 posiciones numéricas.
 */
data class FaceVector(
    val id: String,
    val token: String, // Identificador único del operador de la suite
    val embeddings: FloatArray, // Vector de 128 flotantes
    val timestamp: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as FaceVector
        if (id != other.id) return false
        if (!embeddings.contentEquals(other.embeddings)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + embeddings.contentHashCode()
        return result
    }
}

