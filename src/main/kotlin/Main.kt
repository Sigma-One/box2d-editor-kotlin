import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import data.DataHolder
import gdx.GdxEditor

fun main(args: Array<String>) {
    // Simply sets some configuration and launches the program
    DataHolder.config.title  = "Box2D Editor"
    DataHolder.config.width  = 800
    DataHolder.config.height = 600

    LwjglApplication(GdxEditor(), DataHolder.config)
}