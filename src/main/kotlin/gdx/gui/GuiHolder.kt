package gdx.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import gdx.gui.widget.AbstractRectangularWidget
import gdx.gui.widget.IClickableWidget

class GuiHolder(private val gridSize: Int = 20) {
    val widgets = arrayListOf<AbstractRectangularWidget>()
    var visible = true

    fun addWidgets(vararg newWidgets: AbstractRectangularWidget) {
        for (w in newWidgets) { widgets.add(w) }
    }

    fun render(shapeRenderer: ShapeRenderer, textRenderer: BitmapFont, sprites: SpriteBatch) {

        val oldColour = shapeRenderer.color
        shapeRenderer.end()


        for (w in widgets) {
            sprites.end()
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = if (w.isHovered && w is IClickableWidget) { w.hoveredBg }
            else { w.defaultBg }
            shapeRenderer.rect(
                w.x * gridSize,
                w.y * gridSize,
                w.width * gridSize,
                w.height * gridSize
            )


            shapeRenderer.end()
            shapeRenderer.flush()
            sprites.begin()

            textRenderer.color = if (w.isHovered && w is IClickableWidget) { w.hoveredFg }
            else { w.defaultFg }

            textRenderer.draw(sprites, w.label, w.x * gridSize + 5, w.y * gridSize + 17)
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = oldColour
    }

    fun input(cursorX: Float, cursorT: Float): Boolean {
        var clicked = false

        for (w in widgets) {
            if (w is IClickableWidget) {
                val xRange = w.x * gridSize..w.x * gridSize + w.width * gridSize
                val yRange = w.y * gridSize..w.y * gridSize + w.height * gridSize

                w.isHovered = if (cursorX in xRange && cursorT in yRange) {
                    if (Gdx.input.isButtonJustPressed(0)) {
                        w.onClick()
                        clicked = true
                    }
                    true
                }
                else { false }
            }
        }

        return clicked
    }
}