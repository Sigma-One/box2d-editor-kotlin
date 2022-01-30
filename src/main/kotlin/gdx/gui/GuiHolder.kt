package gdx.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import gdx.gui.widget.AbstractRectangularWidget
import gdx.gui.widget.ExpandingButtonWidget
import gdx.gui.widget.IClickableWidget

class GuiHolder(private val gridSize: Int = 20) {
    private val widgets = arrayListOf<AbstractRectangularWidget>()
    // var visible = true

    fun addWidgets(vararg newWidgets: AbstractRectangularWidget) {
        for (w in newWidgets) {
            widgets.add(w)
            if (w is ExpandingButtonWidget) { addWidgets(*w.childButtons) }
        }
    }

    fun removeWidgetById(widgetId: String) {
        val removeList = arrayListOf<AbstractRectangularWidget>()
        for (w in widgets) {
            if (w.id == widgetId) {
                removeList.add(w)
                if (w is ExpandingButtonWidget) {
                    w.childButtons.forEach { c -> removeList.add(c) }
                }
            }
        }
        for (w in removeList) {
            widgets.remove(w)
        }
    }

    fun render(shapeRenderer: ShapeRenderer, textRenderer: BitmapFont, sprites: SpriteBatch) {

        val oldColour = shapeRenderer.color
        shapeRenderer.end()


        for (w in widgets) {
            if (!w.isVisible) { continue } // Skip if widget is invisible
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

        try {
            for (w in widgets) {
                if (!w.isVisible) { continue }

                if (w is IClickableWidget) {
                    val xRange = w.x * gridSize..w.x * gridSize + w.width * gridSize
                    val yRange = w.y * gridSize..w.y * gridSize + w.height * gridSize

                    w.isHovered = if (cursorX in xRange && cursorT in yRange) {
                        if (Gdx.input.isButtonJustPressed(0)) {
                            w.onClick()
                            clicked = true
                        }
                        true
                    } else {
                        false
                    }
                }
            }
        // A bit hacky, but this allows modifying UI at runtime
        } catch (exception: ConcurrentModificationException) { clicked = true }

        return clicked
    }
}