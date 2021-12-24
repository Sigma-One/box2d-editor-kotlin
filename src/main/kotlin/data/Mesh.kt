package data

import org.poly2tri.Poly2Tri
import org.poly2tri.geometry.polygon.Polygon
import org.poly2tri.geometry.polygon.PolygonPoint

/** Mesh
 * @author  Sigma-One
 * @created 15/12/2021 13:33
 *
 * Represents a mesh, consists of points, simple enough
 **/

data class Point(
    var x: Float,
    var y: Float,
    var parent: Mesh? = null
)

class Mesh {
    // Holds all of the mesh points in order
    val points = arrayListOf<Point>()
    // Holds points of a triangulated mesh
    val triangles = arrayListOf<Triple<Point, Point, Point>>()
    // true if mesh is invalid
    var invalid = false
    private set

    fun addPoint(point: Point) {
        point.parent = this
        points.add(point)
        this.triangulate()
    }

    fun removePoint(point: Point) {
        points.remove(point)
        this.triangulate()
    }

    fun triangulate() {
        if (points.size > 2) {
            // Create array to be used for earcut4j
            val delaunayPolygon = Polygon(points.map { point ->
                PolygonPoint(point.x.toDouble(), point.y.toDouble(), 0.0)
            })

            // Try to triangulate
            try {
                Poly2Tri.triangulate(delaunayPolygon)
            }
            // If triangulation fails, edges are intersecting and mesh is invalid
            catch (e: RuntimeException) {
                invalid = true
                return
            }
            invalid = false

            // Add points to triangulated mesh holder thing
            val delaunayTriangles = delaunayPolygon.triangles
            triangles.clear()
            for (t in delaunayTriangles) {
                triangles.add(Triple(
                    Point(t.points[0].x.toFloat(), t.points[0].y.toFloat()),
                    Point(t.points[1].x.toFloat(), t.points[1].y.toFloat()),
                    Point(t.points[2].x.toFloat(), t.points[2].y.toFloat())
                ))
            }
        }
    }
}