package com.ludovic.vimont.nasaapod.helper.viewmodel

/**
 * LiveData wrapper to easily handle network issues and display the corresponding state through the
 * Activity.
 */
data class StateData<T>(val status: DataStatus,
                        val data: T?,
                        val errorMessage: String) {
    companion object {
        fun <T> loading(): StateData<T> {
            return StateData(DataStatus.LOADING, null, "")
        }

        fun <T> success(data: T?): StateData<T> {
            return StateData(DataStatus.SUCCESS, data, "")
        }

        fun <T> error(dataStatus: DataStatus): StateData<T> {
            return error(dataStatus, "")
        }

        fun <T> error(errorMessage: String): StateData<T> {
            return error(DataStatus.ERROR_NETWORK, errorMessage)
        }

        fun <T> error(dataStatus: DataStatus, errorMessage: String): StateData<T> {
            return StateData(dataStatus, null, errorMessage)
        }
    }
}