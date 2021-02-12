package com.ludovic.vimont.nasaapod.screens.settings

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.cache.DiskCache
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.preferences.DataHolder
import com.ludovic.vimont.nasaapod.preferences.UserPreferences
import java.io.File

class SettingsRepository(private val glide: Glide,
                         private val dataHolder: DataHolder) {
    companion object {
        private const val ONE_KILO_OCTET: Int = 1024
    }

    // @see: https://bumptech.github.io/glide/doc/configuration.html#disk-cache
    fun getCacheSize(): Long {
        return getDirSize(File(glide.context.cacheDir, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR))
    }

    private fun getDirSize(file: File): Long {
        if (file.exists()) {
            var result: Long = 0
            file.listFiles()?.forEach { currentFile: File ->
                result += if (currentFile.isDirectory) {
                    getDirSize(currentFile)
                } else {
                    currentFile.length()
                }
            }
            return result / ONE_KILO_OCTET / ONE_KILO_OCTET
        }
        return 0
    }

    fun clearCache() {
        glide.clearDiskCache()
    }

    fun getQuota(): String {
        val remainingQuota: Int = dataHolder[UserPreferences.KEY_REMAINING_RATE_LIMIT, NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR]
        val rateLimit: Int = dataHolder[UserPreferences.KEY_RATE_LIMIT, NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR]
        return "$remainingQuota/$rateLimit"
    }
}