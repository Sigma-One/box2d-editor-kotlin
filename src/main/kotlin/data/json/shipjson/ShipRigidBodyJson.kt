package data.json.shipjson

import data.json.VertexJson
import kotlinx.serialization.Serializable

@Serializable
data class ShipRigidBodyJson(
    val origin: VertexJson,
    val polygons: ArrayList<ArrayList<VertexJson>>, // Triangle points go here
    val circles: ArrayList<Unit>, // TODO: Figure out what this is and if it's needed
    val shapes: ArrayList<ShipShapeJson> // This seems to usually contain one shape of type POLYGON with all vertices in sequence
)
