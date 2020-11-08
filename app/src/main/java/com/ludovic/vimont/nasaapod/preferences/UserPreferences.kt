package com.ludovic.vimont.nasaapod.preferences

object UserPreferences {
    // Nasa API related
    const val KEY_RANGE_OF_DAYS_TO_FETCH = "range_of_days_to_fetch"
    const val KEY_RATE_LIMIT = "rate_limit"
    const val KEY_REMAINING_RATE_LIMIT = "remaining_rate_limit"

    // Current layout adapter used
    const val LAYOUT_LIST = "list"
    const val LAYOUT_GRID = "grid"
    const val GRID_SPAN_COUNT = 2
    const val KEY_CURRENT_LAYOUT = "current_layout"
    const val DEFAULT_LAYOUT: String = LAYOUT_LIST

    // Current theme used
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    const val THEME_BATTERY = "battery"
    const val THEME_DEFAULT = "default"
}