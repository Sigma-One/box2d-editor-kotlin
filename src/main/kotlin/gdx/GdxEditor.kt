package gdx

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import data.DataHolder
import data.Mesh
import data.Point
import java.lang.Thread.sleep
import kotlin.math.abs

/** gdx.GdxEditor
 * @author  Sigma-One
 * @created 15/12/2021 13:21
 *
 * The main GDX application for displaying the edited mesh
 * Will be embedded in a Swing UI for the rest of the program
 * This mainly handles rendering along with mouse-based editing of points
 **/

class GdxEditor: ApplicationAdapter() {
    // Sprite batch, used to render reference image
    // TODO: This
    // val sprites: SpriteBatch
    // Shape renderer, used for points and lines representing meshes
    private lateinit var shapeRenderer: ShapeRenderer
    // Camera, should be obvious
    private lateinit var camera: OrthographicCamera
    // Active point, used for moving points
    private var activePoint: Point? = null

    // Configuration variables

    // Colours
    var inactiveMeshPointColour      = Color(.2f, .5f, .2f, 1f) // Colour for inactive meshes' points
    var inactiveMeshLastPointColour  = Color(.3f, .3f, 1f,  1f) // Colour for inactive meshes' last point
    var inactiveMeshFirstPointColour = Color(1f,  0f,  0f,  1f) // Colour for inactive meshes' last point
    var inactiveMeshLineColour       = Color(1f,  .5f, .5f, 1f) // Colour for inactive meshes' lines
    var invalidMeshLineColour        = Color(1f,  0f,  0f,  1f) // Colour for invalid meshes' lines
    var gridColour                   = Color(.5f, .5f, .5f, 1f) // Colour for the grid

    var pointSize     = 4f // Size of mesh points (radius)
    var lineThickness = 3f   // Mesh line thickness

    var drawGrid      = true // Whether grid should be rendered
    var gridDensity   = 25   // Grid density
    var gridThickness = 1f   // Grid line thickness


    override fun create() {
        // Initialise things
        shapeRenderer = ShapeRenderer()
        camera = OrthographicCamera()

        DataHolder.meshes.add(Mesh())
        DataHolder.meshes[0].addPoint(Point(0.5f, 0.5f))

        // Set camera to viewport size
        // TODO: Scale properly with window
        camera.setToOrtho(false, 800f, 600f)
    }


    override fun render() {
        // Clear screen with a grey colour
        // TODO: Make this configurable
        ScreenUtils.clear(.9f, .9f, .9f, 1f)

        // Render grid
        if (drawGrid) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = gridColour
            // Horizontal lines
            // Start at center, ensuring grid is centered
            var linePos = camera.viewportHeight / 2
            while (linePos < camera.viewportHeight) {
                // Lines below center
                shapeRenderer.rectLine(
                    0f, linePos,
                    camera.viewportWidth, linePos,
                    gridThickness
                )
                // Lines above center
                shapeRenderer.rectLine(
                    0f, camera.viewportHeight - linePos,
                    camera.viewportWidth, camera.viewportHeight - linePos,
                    gridThickness
                )
                linePos += gridDensity
            }
            // Vertical lines
            // Start at center, ensuring grid is centered
            linePos = camera.viewportWidth / 2
            while (linePos < camera.viewportWidth) {
                // Lines left of center
                shapeRenderer.rectLine(
                    linePos, 0f,
                    linePos, camera.viewportHeight,
                    gridThickness
                )
                // Lines right of center
                shapeRenderer.rectLine(
                    camera.viewportWidth - linePos, 0f,
                    camera.viewportWidth - linePos, camera.viewportHeight,
                    gridThickness
                )
                linePos += gridDensity
            }
            shapeRenderer.end()
        }

        // Render points of each mesh and lines between them
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (mesh in DataHolder.meshes) {
            // Draw triangulation lines first
            shapeRenderer.color = gridColour
            for (triPoints in mesh.triangles) {
                shapeRenderer.rectLine(
                    pointToCameraCoordinates(triPoints.first).x,
                    pointToCameraCoordinates(triPoints.first).y,
                    pointToCameraCoordinates(triPoints.second).x,
                    pointToCameraCoordinates(triPoints.second).y,
                    lineThickness / 2
                )
                shapeRenderer.rectLine(
                    pointToCameraCoordinates(triPoints.second).x,
                    pointToCameraCoordinates(triPoints.second).y,
                    pointToCameraCoordinates(triPoints.third).x,
                    pointToCameraCoordinates(triPoints.third).y,
                    lineThickness / 2
                )
                shapeRenderer.rectLine(
                    pointToCameraCoordinates(triPoints.third).x,
                    pointToCameraCoordinates(triPoints.third).y,
                    pointToCameraCoordinates(triPoints.first).x,
                    pointToCameraCoordinates(triPoints.first).y,
                    lineThickness / 2
                )
            }

            for ((idx, point) in mesh.points.withIndex()) {
                // TODO: Active mesh in different colour
                // TODO: Index numbers on points

                if (mesh.invalid) {
                    shapeRenderer.color = invalidMeshLineColour
                }
                else {
                    shapeRenderer.color = inactiveMeshLineColour
                }
                // Draw line to next point
                val nextIndex = if (idx == mesh.points.lastIndex) { 0 } else { idx+1 }
                shapeRenderer.rectLine(
                    pointToCameraCoordinates(point).x,  // X1
                    pointToCameraCoordinates(point).y, // Y1
                    pointToCameraCoordinates(mesh.points[nextIndex]).x, // X2
                    pointToCameraCoordinates(mesh.points[nextIndex]).y, // Y2
                    lineThickness
                )

                // Draw point itself
                // Done after line to render over the line ends
                // Will cause weirdness on first point, but that can be fixed when rendering the special colour for the first and last point
                shapeRenderer.color = inactiveMeshPointColour
                shapeRenderer.circle(
                    pointToCameraCoordinates(point).x,
                    pointToCameraCoordinates(point).y,
                    pointSize
                )
            }
            shapeRenderer.color = inactiveMeshLastPointColour
            // Last point special colour
            shapeRenderer.circle(
                pointToCameraCoordinates(mesh.points.last()).x,
                pointToCameraCoordinates(mesh.points.last()).y,
                pointSize
            )
            // First point special colour
            shapeRenderer.color = inactiveMeshFirstPointColour
            shapeRenderer.circle(
                pointToCameraCoordinates(mesh.points.first()).x,
                pointToCameraCoordinates(mesh.points.first()).y,
                pointSize
            )
        }
        shapeRenderer.end()

        // Process inputs
        // In separate method because doing it in render() feels wrong
        input()

        // Sleep to limit rendering to 60FPS
        // Precaution against wasting resources, as I've heard of GDX not limiting automatically and wasting CPU time
        // Unsure if necessary
        if (Gdx.graphics.deltaTime < 1000/60) {
            sleep(1000/60-Gdx.graphics.deltaTime.toLong())
        }

        camera.update()
    }


    private fun input() {
        // Utility function to check for point at some position
        // Position given in screen/camera coordinates
        fun getPointAtPos(x: Float, y: Float): Point? {
            for (mesh in DataHolder.meshes) {
                for (point in mesh.points) {
                    val screenPos = pointToCameraCoordinates(point)
                    if (abs(screenPos.x - x) <= pointSize && abs(screenPos.y - y) <= pointSize) {
                        return point
                    }
                }
            }
            return null
        }

        val screenPos = Vector3()
        screenPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        camera.unproject(screenPos)
        val pointPos = cameraToPointCoordinates(Point(screenPos.x, screenPos.y))

        // Run this as button 0 (Left) is first pressed
        if (Gdx.input.isButtonJustPressed(0)) {
            // Add a point if not clicked on existing one
            if (getPointAtPos(screenPos.x, screenPos.y) == null) {
                DataHolder.meshes[0].addPoint(pointPos)
            }
            // Set clicked point as active for dragging
            activePoint = getPointAtPos(screenPos.x, screenPos.y)
        }

        // Clear active point if button 0 (Left) is released
        if (!Gdx.input.isButtonPressed(0) && activePoint != null) { activePoint = null }

        // Set point positions for dragging
        if (activePoint != null) {
            activePoint!!.x = pointPos.x
            activePoint!!.y = pointPos.y
            activePoint!!.parent!!.triangulate()
        }

        // Run this as button 1 (Right) is first pressed
        // Last in function as things may rely on this point existing
        // Probably better solutions exist but this works well enough
        if (Gdx.input.isButtonJustPressed(1)) {
            // Remove clicked point
            if (getPointAtPos(screenPos.x, screenPos.y) != null) {
                DataHolder.meshes[0].removePoint(getPointAtPos(screenPos.x, screenPos.y)!!)
            }
        }
    }


    // Utility methods to convert point coordinates to camera ones and back
    private fun pointToCameraCoordinates(point: Point): Point {
        return Point(
            point.x * camera.viewportWidth,
            camera.viewportHeight - point.y * camera.viewportHeight
        )
    }


    private fun cameraToPointCoordinates(point: Point): Point {
        return Point(
            point.x / camera.viewportWidth,
            (camera.viewportHeight - point.y) / camera.viewportHeight
        )
    }
}