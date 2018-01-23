package com.seven.app.tools.common.file

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * 文件类型工具
 * Created by Seven on 2018/1/23.
 */
object FileTypeTool {


    @Throws(IOException::class)
    private fun getFileContent(inputStream: InputStream?): String {

        val b = ByteArray(28)

        try {
            inputStream!!.read(b, 0, 28)
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw e
                }

            }
        }
        return String(b)
    }

    @Throws(IOException::class)
    fun getType(inputStream: InputStream): FileType? {

        var fileHead: String? = getFileContent(inputStream)

        if (fileHead == null || fileHead.isEmpty()) {
            return null
        }

        fileHead = fileHead.toUpperCase()

        val fileTypes = FileType.values()

        return fileTypes.firstOrNull { fileHead.startsWith(it.value!!) }
    }

    @Throws(IOException::class)
    fun getType(filePath: String): FileType? {
        return getType(File(filePath))
    }

    /**
     * 判断文件类型
     *
     * @param file
     * 文件
     *
     * @return 文件类型
     */
    @Throws(IOException::class)
    fun getType(file: File): FileType? {
        return getType(FileInputStream(file))
    }
}
/**
 * Constructor
 */