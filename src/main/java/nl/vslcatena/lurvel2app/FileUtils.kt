package nl.vslcatena.lurvel2app

import java.io.File
import java.io.InputStream

object FileUtils {
    fun inputFromResources(fileName: String): InputStream {
        return javaClass.classLoader.getResourceAsStream(fileName)
    }

    fun fromExternal(fileName: String): File {
        return File(System.getProperty("user.dir") + File.separator + fileName)
    }
}