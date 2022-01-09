package data.json.shipjson

import data.json.ParticleJson
import kotlinx.serialization.Serializable

@Serializable
data class ShipParticleEmitterJson(
    val position: String,
    val trigger: String,
    val hasLight: Boolean,
    val particle: ParticleJson,
    val workSounds: ArrayList<String>
)
