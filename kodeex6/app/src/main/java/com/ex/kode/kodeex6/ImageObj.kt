package com.ex.kode.kodeex6

import java.io.File

class ImageObj(var path: String, var name: String, var select: Boolean, var dir: File) {
    companion object {
        fun getExt(path: String): String {
            var splits = path.split(".")
            return splits[splits.size-1]
        }

        fun checkFile(file: String): Boolean {
            return file.toLowerCase().endsWith("jpg") or
                    file.toLowerCase().endsWith("png") or
                    file.toLowerCase().endsWith("gif") or
                    file.toLowerCase().endsWith("jpeg") or
                    file.toLowerCase().endsWith("bmp")
        }

        fun deleteFile(file: String): Boolean {
            try {
                var fl: File = File(file)
                fl.delete()
                return true
            } catch (e: Exception) {
                return false
            }
        }

        fun renameFile(path: String, old: String, name: String): Boolean {
            var file = File(path)
            var new_file = File(path.replace(old, name))
            file.renameTo(new_file)

            return new_file.exists()
        }
    }
}