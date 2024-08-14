package moe.nekocafe.sakurabot.util

import moe.nekocafe.sakurabot.SakuraBot
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import java.io.File
import java.net.URLDecoder
import java.util.jar.JarFile

class Reflection {
    private val logger = SakuraBot.logger

    fun findSubTypes(packageName: String, superType: KClass<*>): List<KClass<*>> {
        val classLoader = Thread.currentThread().contextClassLoader
        val path = packageName.replace('.', '/')
        val resources = classLoader.getResources(path).toList()
        val classes = mutableListOf<KClass<*>>()

        logger.info("Scanning package: $packageName")

        for (resource in resources) {
            val uri = resource.toURI()
            if (uri.scheme == "file") {
                val file = File(URLDecoder.decode(uri.path, "UTF-8"))
                logger.info("Resource: ${file.path}")
                if (file.isDirectory) {
                    // 处理目录
                    file.walkTopDown().forEach { f ->
                        if (f.isFile && f.name.endsWith(".class")) {
                            val className = f.relativeTo(File(URLDecoder.decode(resource.file, "UTF-8")))
                                .path.replace('/', '.')
                                .removeSuffix(".class")
                            try {
                                val clazz = Class.forName("$packageName.$className").kotlin
                                if (clazz.isSubclassOf(superType)) {
                                    classes.add(clazz)
                                    logger.info("Found subclass: $className")
                                }
                            } catch (e: ClassNotFoundException) {
                                logger.warning("Class not found: $className")
                            }
                        }
                    }
                } else {
                    logger.error("Resource is not a directory: ${file.path}")
                }
            } else if (uri.scheme == "jar") {
                val jarPath = uri.schemeSpecificPart.split("!")[0].removePrefix("file:")
                val jarFile = JarFile(URLDecoder.decode(jarPath, "UTF-8"))
                val entries = jarFile.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    if (entry.name.endsWith(".class") && entry.name.startsWith(path)) {
                        val className = entry.name.replace('/', '.').removeSuffix(".class")
                        try {
                            val clazz = Class.forName(className).kotlin
                            if (clazz.isSubclassOf(superType)) {
                                classes.add(clazz)
                                logger.info("Found subclass: $className")
                            }
                        } catch (e: ClassNotFoundException) {
                            logger.warning("Class not found: $className")
                        }
                    }
                }
            }
        }

        logger.info("Total classes found: ${classes.size}")
        return classes
    }
}

