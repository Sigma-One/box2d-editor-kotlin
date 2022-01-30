package filechooser

import java.io.File
import javax.swing.filechooser.FileFilter

/**
 * @author Sigma-One
 *
 * An object used as a file filter to select image files in a Swing file chooser
 */
object ImageFileFilter: FileFilter() {
    override fun accept(file: File?): Boolean {
        if (file != null) {
            return (file.extension in arrayOf(
                "png", "jpg", "jpeg", "gif"
            )) || file.isDirectory
        }
        return false
    }

    override fun getDescription(): String = "Image Files (PNG, JP(E)G, GIF)"
}

/**
 * @author Sigma-One
 *
 * An object used as a file filter to select JSON files in a Swing file chooser
 */
object JsonFileFilter: FileFilter() {
    override fun accept(file: File?): Boolean {
        if (file != null) {
            return (file.extension == "json") || file.isDirectory
        }
        return false
    }

    override fun getDescription(): String = "JSON Files"
}