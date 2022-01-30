package gdx.gui.widget

class ButtonWidget(
    width: Float,
    height: Float,
    x: Float,
    y: Float,
    label: String = "Button",
    id: String = "",
    val callback : () -> Unit
) :
    AbstractRectangularWidget(width, height, x, y, label, id),
    IClickableWidget
{
    override var onClick = {
        callback()
    }
}