package data

/** Mesh
 * @author  Sigma-One
 * @created 15/12/2021 13:33
 *
 * Represents a mesh, consists of points, simple enough
 * Initialised with coordinates of first point
 **/

data class Point(
    var x: Float,
    var y: Float
)

class Mesh (firstCoords: Point) {
    // Holds all of the mesh points in order
    val points = arrayListOf(firstCoords)
    // Holds the cuts that are to be made when triangulating the mesh
    val triangleCuts = arrayListOf<Pair<Int, Int>>()
}