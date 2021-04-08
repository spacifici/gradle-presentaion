package com.eyeo.cmake

import org.gradle.api.Plugin
import org.gradle.api.Project

class Plugin : Plugin<Project> {
    override fun apply(target: Project) {
        // Let's make the cmake block available inside the build script
        target.convention.plugins["cmake"] = Convention(target)
    }
}