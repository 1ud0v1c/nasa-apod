package com.ludovic.vimont.nasaapod.api.glide

class GlideDispatchProgressListener: ResponseProgressListener {
    companion object {
        private val listeners = HashMap<String, UIDownloadProgressListener>()

        fun add(url: String, listener: UIDownloadProgressListener) {
            listeners[url] = listener
        }

        fun remove(url: String) {
            listeners.remove(url)
        }
    }

    override fun update(url: String, totalBytesRead: Long, contentLength: Long, done: Boolean) {
        val progress: Int = (100 * totalBytesRead / contentLength).toInt()
        listeners[url]?.let { uiDownloadProgressListener: UIDownloadProgressListener ->
            uiDownloadProgressListener.update(progress)
            if (done) {
                uiDownloadProgressListener.done()
            }
        }
    }
}