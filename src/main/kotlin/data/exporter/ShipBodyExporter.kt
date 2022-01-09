package data.exporter

import data.DataHolder
import data.json.VertexJson
import data.json.shipjson.ShipRigidBodyJson
import data.json.shipjson.ShipShapeJson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

fun exportShipRigidBody(jsonFile: File?) {
    if (jsonFile == null) { return }

    // TODO: Multiple meshes
    val polygons = arrayListOf<ArrayList<VertexJson>>()
    for (triangle in DataHolder.meshes[0].triangles) {
        polygons.add(arrayListOf(
            triangle.first.toVertexJson(),
            triangle.second.toVertexJson(),
            triangle.third.toVertexJson()
        ))
    }

    val shapeVertices = ArrayList(DataHolder.meshes[0].points.map { point ->
        point.toVertexJson()
    })
    val shapes = ShipShapeJson(
        type = "POLYGON",
        vertices = shapeVertices
    )

    val rootJson = ShipRigidBodyJson(
        circles  = arrayListOf(),
        origin   = VertexJson(0.5, 0.5), // TODO Don't hardcode, make adjustable
        polygons = polygons,
        shapes   = arrayListOf(shapes)
    )

    jsonFile.writeText(Json.encodeToString(rootJson))
}