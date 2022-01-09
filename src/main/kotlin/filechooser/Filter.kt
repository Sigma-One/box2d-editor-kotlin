package filechooser

import java.io.File
import javax.swing.filechooser.FileFilter

object ImageFileFilter: FileFilter() {
    override fun accept(file: File?): Boolean {
        if (file != null) {
            return (file.extension in arrayOf(
                "png", "jpg", "jpeg", "gif"
            )) || file.isDirectory
        }
        return false
    }

    override fun getDescription(): String {
        return "Image Files (PNG, JP(E)G, GIF)"
    }
}

object JsonFileFilter: FileFilter() {
    override fun accept(file: File?): Boolean {
        if (file != null) {
            return (file.extension == "json") || file.isDirectory
        }
        return false
    }

    override fun getDescription(): String {
        return "JSON Files"
    }
}