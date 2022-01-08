package gdx.gui.widget

import com.badlogic.gdx.graphics.Color

abstract class AbstractRectangularWidget(
    val width: Float,
    val height: Float,
    val x: Float,
    val y: Float,
    val label: String = "",
    val defaultBg: Color = Color(0f, 0f, 0f, 1f),
    val hoveredBg: Color = Color(1f, 1f, 1f, 1f),
    val defaultFg: Color = hoveredBg,
    val hoveredFg: Color = defaultBg,
    var isHovered: Boolean = false
)