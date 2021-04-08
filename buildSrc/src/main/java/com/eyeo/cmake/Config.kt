package com.eyeo.cmake

internal class Config {
    @set:JvmName("name")
    var name: String? = null

    @set:JvmName("src")
    var src: String? = null

    val params = mutableMapOf<String, String>()

    fun param(key: String, value: String) = params.put(key, value)
}
