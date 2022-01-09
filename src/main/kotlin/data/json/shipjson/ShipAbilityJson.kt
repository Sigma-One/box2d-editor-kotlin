package data.json.shipjson

import kotlinx.serialization.Serializable

@Serializable
data class ShipAbilityJson(
    val type: String,
    val factor: Float,
    val rechargeTime: Int
)
