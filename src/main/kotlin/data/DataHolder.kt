package data

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color

/**
 * @author  Sigma-One
 *
 * Used to pass editing data between different parts of the program easily
 * Essentially the only purpose is to hold a bunch of variables
 * Should maybe be refactored into something better some day, but this is a simple solution that works
 **/

object DataHolder {
    val lwjglConfig: LwjglApplicationConfiguration = LwjglApplicationConfiguration()
    val uiConfig = hashMapOf(
        "defaultBg" to Color(0f, 0f, 0f, 1f),
        "defaultFg" to Color(1f, 1f, 1f, 1f),
        "hoveredBg" to Color(1f, 1f, 1f, 1f),
        "hoveredFg" to Color(0f, 0f, 0f, 1f)
    )
    val meshes = arrayListOf<Mesh>()
}