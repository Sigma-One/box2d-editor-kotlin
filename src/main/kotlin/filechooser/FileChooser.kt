package filechooser

import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

class FileChooser(
    filter: FileFilter,
    val type: Int = 0
) {
    private val dialog: JFileChooser = JFileChooser()

    init {
        dialog.fileFilter = filter
    }

    fun show(): File? {
        val choice: Int = if (type == 0) { dialog.showOpenDialog(null) }
        else { dialog.showSaveDialog(null) }

        return if (choice == JFileChooser.APPROVE_OPTION) { dialog.selectedFile }
        else { null }
    }
}