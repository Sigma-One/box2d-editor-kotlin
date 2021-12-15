import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import gdx.GdxEditor

fun main(args: Array<String>) {
    // Simply sets some configuration and launches the program
    val config = LwjglApplicationConfiguration()

    config.title  = "Box2D Editor"
    config.width  = 800
    config.height = 600

    // TODO: Wrap in Swing
    LwjglApplication(GdxEditor(), config)
}

class Main {


}