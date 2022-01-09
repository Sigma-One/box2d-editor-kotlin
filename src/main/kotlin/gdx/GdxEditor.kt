package gdx

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import data.DataHolder
import data.Mesh
import data.Point
import gdx.gui.GuiHolder
import gdx.gui.widget.ButtonWidget
import filechooser.FileChooser
import filechooser.ImageFileFilter
import java.io.File
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
    private lateinit var sprites: SpriteBatch
    // Shape renderer, used for points and lines representing meshes
    private lateinit var shapeRenderer: ShapeRenderer
    // Text renderer (Font) for drawing UI and stuff
    private lateinit var textRenderer: BitmapFont
    // Camera, should be obvious
    private lateinit var camera: OrthographicCamera
    // Active point, used for moving points
    private var activePoint: Point? = null
    // GUI root object thing
    private val gui = GuiHolder(gridSize = 10)
    // Reference image stuff
    private var referenceImage: Sprite? = null
    private val imageChooser = FileChooser("/home/sigma1/box2d-editor-kotlin", ImageFileFilter)

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

    var drawGui = true // Whether GUI should be visible

    var drawGrid      = true // Whether grid should be rendered
    var gridDensity   = 25   // Grid density
    var gridThickness = 1f   // Grid line thickness


    override fun create() {
        // Initialise things
        generateFont()

        sprites = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        camera = OrthographicCamera()

        DataHolder.meshes.add(Mesh())
        DataHolder.meshes[0].addPoint(Point(0.5f, 0.5f))

        gui.addWidgets(
            ButtonWidget(15f, 2f, 2f, 2f, label = "Export.. (TODO)") { println("Exporting...") }, // TODO Implement this
            ButtonWidget(15f, 2f, 2f, 5f, label = "Import.. (TODO)") { println("Importing...") },  // TODO Implement this too
            ButtonWidget(15f, 2f, 2f, 8f, label = "Load Reference..") { setReferenceImage(imageChooser.show()) },
            ButtonWidget(15f, 2f, 2f, 11f, label = "Clear Reference") { setReferenceImage(null) }
        )

        camera.setToOrtho(false, DataHolder.config.width.toFloat(), DataHolder.config.height.toFloat())
    }

    private fun setReferenceImage(imageFile: File?) {
        referenceImage = if (imageFile != null) {
            Sprite(Texture(FileHandle(imageFile)))
        }
        else {
            null
        }
    }

    private fun generateFont() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("src/assets/fonts/font.ttf"))
        val parameter = FreeTypeFontParameter()
        //parameter.color = Color.BLACK
        parameter.size = 16
        parameter.hinting = FreeTypeFontGenerator.Hinting.Full
        textRenderer = generator.generateFont(parameter) // font size 12 pixels

        generator.dispose() // don't forget to dispose to avoid memory leaks!
    }

    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, width.toFloat(), height.toFloat())
    }


    override fun render() {
        // Clear screen with a grey colour
        // TODO: Make this configurable
        ScreenUtils.clear(.9f, .9f, .9f, 1f)
        shapeRenderer.projectionMatrix = camera.projection
        shapeRenderer.transformMatrix = camera.view
        sprites.projectionMatrix = camera.projection
        sprites.transformMatrix = camera.view

        // Reference on background
        // TODO: Make this thing's scale adjustable without breaking the exporting stuff
        if (referenceImage != null) {
            sprites.begin()
            referenceImage!!.setAlpha(0.5f)
            referenceImage!!.setCenter(
                pointToCameraCoordinates(Point(.5f, .5f)).x,
                pointToCameraCoordinates(Point(.5f, .5f)).y
            )
            // Calculate scaling such that the coordinates align when exporting
            referenceImage!!.setScale(
                camera.viewportWidth / referenceImage!!.width,
                camera.viewportHeight / referenceImage!!.height
            )
            referenceImage!!.draw(sprites)
            sprites.end()
        }

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

        sprites.begin()
        gui.render(shapeRenderer, textRenderer, sprites)
        sprites.end()
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

        val didGuiClick = gui.input(screenPos.x, screenPos.y)

        // Run this as button 0 (Left) is first pressed
        if (Gdx.input.isButtonJustPressed(0)) {
            // Add a point if not clicked on existing one
            if (getPointAtPos(screenPos.x, screenPos.y) == null && !didGuiClick) {
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