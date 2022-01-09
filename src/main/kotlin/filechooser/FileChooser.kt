package filechooser

import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

class FileChooser(
    defaultDir: String,
    filter: FileFilter
) {
    private val dialog: JFileChooser = JFileChooser()

    init {
        dialog.currentDirectory = File(defaultDir)
        dialog.fileFilter = filter
    }

    fun show(): File? {
        val returnVal: Int = dialog.showOpenDialog(null)

        return if (returnVal == JFileChooser.APPROVE_OPTION) {
            val file: File = dialog.selectedFile
            file
        } else { null }
    }
}