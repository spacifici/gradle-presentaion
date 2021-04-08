package com.eyeo.cmake

import groovy.lang.Closure
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import java.nio.file.Paths

// This is capturing the cmake method we had in the build script before
internal open class Convention(private val project: Project) {

    @Suppress("DefaultLocale")
    fun cmake(closure: Closure<Any>) {
        val config = Config()
        closure.delegate = config
        closure.call()

        if (config.name.isNullOrEmpty() || config.src.isNullOrEmpty()) {
            throw GradleException("Bad Config!")
        }

        val relativeSourceDir = Paths.get(config.src!!)
        val absoluteSourceDir = project.projectDir.toPath().resolve(relativeSourceDir)

        val buildTasks = mutableListOf<String>() // Let's collect all the build task names!

        listOf("Debug", "Release").forEach { mode ->
            val configureTaskName = "configure${config.name!!.capitalize()}${mode}"
            val buildTaskName = "build${config.name!!.capitalize()}${mode}"
            buildTasks += buildTaskName
            val relativeBuildDir = Paths.get("build", "cmake", config.name, mode.toLowerCase())
            val absoluteBuildDir = project.projectDir.toPath().resolve(relativeBuildDir)

            val cmakeCmdLine = mutableListOf(
                "cmake",
                "-G", "Ninja",
                "-DCMAKE_BUILD_TYPE=${mode}"
            )

            config.params.forEach {
                cmakeCmdLine += "-D${it.key}=${it.value}"
            }
            cmakeCmdLine += absoluteSourceDir.toString()



            project.tasks.register(configureTaskName, Exec::class.java) {
                commandLine = cmakeCmdLine
                workingDir = absoluteBuildDir.toFile()
                group = "configure"

                doFirst {
                    absoluteBuildDir.toFile().mkdirs()
                }
            }

            project.tasks.register(buildTaskName, Exec::class.java) {
                commandLine = listOf("ninja")
                workingDir = absoluteBuildDir.toFile()
                group = "build"


                dependsOn(configureTaskName)
            }
        }

        project.tasks.register("assemble${config.name!!.capitalize()}") {
            group = "build"
            dependsOn(buildTasks)
        }
    }
}