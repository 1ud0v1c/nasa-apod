package com.ludovic.vimont.nasaapod.api.glide

/**
 * Provide a way to register an UIDownloadProgressListener interface which returns the progression of
 * the download with a percentage.
 */
class GlideDispatchProgressListener: ResponseProgressListener {
    companion object {
        private const val HUNDRED_PERCENT: Int = 100
        private val listeners = HashMap<String, UIDownloadProgressListener>()
        private val progresses = HashMap<String, Int>()

        fun add(url: String, listener: UIDownloadProgressListener) {
            listeners[url] = listener
        }

        fun remove(url: String) {
            listeners.remove(url)
            progresses.remove(url)
        }
    }

    override fun update(url: String, totalBytesRead: Long, contentLength: Long, isDone: Boolean) {
        val currentProgress: Int = (HUNDRED_PERCENT * totalBytesRead / contentLength).toInt()
        if (isDispatchNeeded(url, currentProgress)) {
            listeners[url]?.invoke(currentProgress)
        }
    }

    private fun isDispatchNeeded(key: String, currentProgress: Int): Boolean {
        if (progresses.containsKey(key)) {
            progresses[key]?.let { lastProgress: Int ->
                return if (currentProgress != lastProgress) {
                    progresses[key] = currentProgress
                    true
                } else {
                    false
                }
            }
        }
        progresses[key] = currentProgress
        return true
    }
}