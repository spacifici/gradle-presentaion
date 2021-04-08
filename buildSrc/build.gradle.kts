plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("eyeoCMakePlugin") {
            id = "eyeo-cmake-plugin"
            implementationClass = "com.eyeo.cmake.Plugin"
        }
    }
}

repositories {
    jcenter()
}