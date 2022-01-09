package data.json.shipjson

import kotlinx.serialization.Serializable

@Serializable
data class ShipJsonTemplate(
    val size: Float,
    val maxLife: Int,
    val type: String,
    val price: Int,
    val ability: ShipAbilityJson,
    val displayName: String,
    val engine: String,
    val gunSlots: ArrayList<ShipGunSlotJson>,
    val rigidBody: ShipRigidBodyJson,
    val particleEmitters: ArrayList<ShipParticleEmitterJson>
)
