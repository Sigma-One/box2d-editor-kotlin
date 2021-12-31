package data

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import data.Mesh

/** DataHolder
 * @author  Sigma-One
 * @created 15/12/2021 13:30
 *
 * Used to pass editing data between different parts of the program easily
 * Essentially the only purpose is to hold a bunch of variables
 * Should maybe be refactored into something better some day, but this is a simple solution that works
 **/

object DataHolder {
    val config: LwjglApplicationConfiguration = LwjglApplicationConfiguration()

    val meshes = arrayListOf<Mesh>()
}