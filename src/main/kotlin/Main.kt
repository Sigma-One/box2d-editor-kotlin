import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import data.DataHolder
import gdx.GdxEditor


fun main(args: Array<String>) {
    // Simply sets some configuration and launches the program
    DataHolder.lwjglConfig.title  = "Box2D Editor"
    DataHolder.lwjglConfig.width  = 800
    DataHolder.lwjglConfig.height = 600

    LwjglApplication(GdxEditor(), DataHolder.lwjglConfig)
}