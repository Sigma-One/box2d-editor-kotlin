package filechooser

import java.io.File
import javax.swing.filechooser.FileFilter

object ImageFileFilter: FileFilter() {
    override fun accept(file: File?): Boolean {
        if (file != null) {
            return file.extension in arrayOf(
                "png", "jpg", "jpeg", "gif"
            )
        }
        return false
    }

    override fun getDescription(): String {
        return "Image files (PNG, JP(E)G, GIF)"
    }
}