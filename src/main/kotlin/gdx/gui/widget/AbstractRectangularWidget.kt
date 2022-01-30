package gdx.gui.widget

import com.badlogic.gdx.graphics.Color
import data.DataHolder

@Suppress("LongParameterList") // This is a widget base class, lots of parameters can't really be avoided well
abstract class AbstractRectangularWidget(
    val width: Float,
    val height: Float,
    var x: Float,
    var y: Float,
    val label: String = "",
    val id: String = "",
    val defaultBg: Color = DataHolder.uiConfig["defaultBg"]!!,
    val hoveredBg: Color = DataHolder.uiConfig["hoveredBg"]!!,
    val defaultFg: Color = DataHolder.uiConfig["defaultFg"]!!,
    val hoveredFg: Color = DataHolder.uiConfig["hoveredFg"]!!,
    var isHovered: Boolean = false,
    var isVisible: Boolean = true
)