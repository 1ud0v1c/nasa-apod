package com.ludovic.vimont.nasaapod

import java.net.URL
import java.net.URLConnection

object NetworkHelper {
    fun isUrlValid(url: String, timeout: Int = 10_000): Boolean {
        return try {
            val myUrl = URL(url)
            val connection: URLConnection = myUrl.openConnection()
            connection.connectTimeout = timeout
            connection.readTimeout = timeout
            connection.connect()
            true
        } catch (e: Exception) {
            false
        }
    }
}