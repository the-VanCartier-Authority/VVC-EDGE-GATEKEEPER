package com.vvc.edge.gatekeeper.domain.model

data class FaceVector(
    val data: FloatArray
) {
    init {
        require(data.size == DIMENSION) { "Face vectors must contain exactly $DIMENSION values" }
        require(data.all { it.isFinite() }) { "Face vectors must contain only finite values" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as FaceVector
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int = data.contentHashCode()

    companion object {
        const val DIMENSION = 128

        fun from(values: FloatArray): FaceVector? = runCatching {
            FaceVector(values.copyOf())
        }.getOrNull()
    }
}
