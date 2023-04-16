package com.ludovic.vimont.nasaapod.helper

import android.content.SharedPreferences

class FakeSharedPreferences: SharedPreferences {
    private val mutableMap = HashMap<String, Any>()

    override fun getAll(): MutableMap<String, *> {
        return mutableMap
    }

    override fun getString(key: String?, default: String?): String? {
        return mutableMap[key] as? String
    }

    override fun getStringSet(key: String?, default: MutableSet<String>?): MutableSet<String>? {
        return mutableMap[key] as? MutableSet<String>
    }

    override fun getInt(key: String?, default: Int): Int {
        return mutableMap[key] as? Int ?: default
    }

    override fun getLong(key: String?, default: Long): Long {
        return mutableMap[key] as? Long ?: default
    }

    override fun getFloat(key: String?, default: Float): Float {
        return mutableMap[key] as? Float ?: default
    }

    override fun getBoolean(key: String?, default: Boolean): Boolean {
        return mutableMap[key] as? Boolean ?: default
    }

    override fun contains(key: String?): Boolean {
        return mutableMap.containsKey(key)
    }

    override fun edit(): SharedPreferences.Editor {
        return object : SharedPreferences.Editor {
            override fun putString(key: String?, value: String?): SharedPreferences.Editor {
                if (key == null) return this
                mutableMap[key] = value.orEmpty()
                return this
            }

            override fun putStringSet(
                key: String?, value: MutableSet<String>?
            ): SharedPreferences.Editor = this

            override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
                if (key == null) return this
                mutableMap[key] = value
                return this
            }

            override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
                if (key == null) return this
                mutableMap[key] = value
                return this
            }

            override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
                if (key == null) return this
                mutableMap[key] = value
                return this
            }

            override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
                if (key == null) return this
                mutableMap[key] = value
                return this
            }

            override fun remove(key: String?): SharedPreferences.Editor {
                mutableMap.remove(key)
                return this
            }

            override fun clear(): SharedPreferences.Editor {
                mutableMap.clear()
                return this
            }

            override fun commit(): Boolean = true

            override fun apply() = Unit
        }
    }

    override fun registerOnSharedPreferenceChangeListener(
        onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener?
    ) = Unit

    override fun unregisterOnSharedPreferenceChangeListener(
        onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener?
    ) = Unit

}