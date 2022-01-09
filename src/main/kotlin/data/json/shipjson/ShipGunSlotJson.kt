package data.json.shipjson

import kotlinx.serialization.Serializable

@Serializable
data class ShipGunSlotJson(
    val position: String,
    val isUnderneathHull: Boolean,
    val allowsRotation: Boolean
)
