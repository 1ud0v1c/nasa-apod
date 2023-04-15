package com.ludovic.vimont.nasaapod.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class TestObserver<T> private constructor() : Observer<T> {
    private val history = ArrayList<T>()

    override fun onChanged(t: T) {
        history.add(t)
    }

    private fun value(): T {
        return history[history.size - 1]
    }

    fun assertValue(expected: T): TestObserver<T> {
        val value = value()
        if (value != expected) {
            throw AssertionError("Expected: $expected, but actual: $value")
        }
        return this
    }

    fun assertNoValue(): TestObserver<T> {
        if (history.isNotEmpty()) {
            throw AssertionError("Expected no value, but received: " + value())
        }
        return this
    }

    companion object {
        fun <T> test(liveData: LiveData<T>): TestObserver<T> {
            val observer = TestObserver<T>()
            liveData.observeForever(observer)
            return observer
        }
    }
}

fun <T> LiveData<T>.test(): TestObserver<T> {
    return TestObserver.test(this)
}