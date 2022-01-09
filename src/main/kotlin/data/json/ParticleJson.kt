package data.json

import kotlinx.serialization.Serializable

@Serializable
data class ParticleJson(
    val effectFile: String,
    val size: Float,
    val tex: String,
    val tint: String
)
