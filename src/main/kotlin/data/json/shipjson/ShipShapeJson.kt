package data.json.shipjson

import data.json.VertexJson
import kotlinx.serialization.Serializable

@Serializable
data class ShipShapeJson(
    val type: String,
    val vertices: ArrayList<VertexJson>
)
