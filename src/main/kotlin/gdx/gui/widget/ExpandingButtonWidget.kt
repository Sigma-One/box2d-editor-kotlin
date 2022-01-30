package gdx.gui.widget

class ExpandingButtonWidget(
    width: Float,
    height: Float,
    x: Float,
    y: Float,
    label: String = "Button",
    vararg val childButtons: ButtonWidget,
    id: String = ""
) :
    AbstractRectangularWidget(width, height, x, y, label, id),
    IClickableWidget
{
    private var isOpen = false

    init {
        // Hide all child buttons by default and set their positions
        for ((idx, button) in childButtons.withIndex()) {
            button.isVisible = false
            button.x = idx*width + x+width
            button.y = y
        }
    }

    override var onClick = {
        for (button in childButtons) { button.isVisible = !isOpen }
        isOpen = !isOpen
    }
}