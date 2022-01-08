package gdx.gui.widget

import com.badlogic.gdx.graphics.Color

class ButtonWidget(
    width: Float,
    height: Float,
    x: Float,
    y: Float,
    label: String = "Button",
    val callback : () -> Unit
) :
    AbstractRectangularWidget(width, height, x, y, label),
    IClickableWidget
{
    override fun onClick() {
        callback()
    }
}