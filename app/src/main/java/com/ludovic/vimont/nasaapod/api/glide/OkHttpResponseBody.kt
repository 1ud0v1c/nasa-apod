package com.ludovic.vimont.nasaapod.api.glide

import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Okio
import okio.Source

/**
 * OkHttp interceptor which give us the possibility to track the download and thus be able to
 * display a progression to a user, using the interface ResponseProgressListener.
 * @see: GlideDispatchProgressListener
 */
class OkHttpResponseBody(private val httpUrl: HttpUrl,
                         private val responseBody: ResponseBody,
                         private val progressListener: ResponseProgressListener) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            /**
             * read() returns the number of bytes read, or -1 if this source is exhausted.
             */
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead: Long = super.read(sink, byteCount)
                if (bytesRead != -1L) {
                    totalBytesRead += bytesRead
                }
                val isDone: Boolean = (bytesRead == -1L)
                val url: String = httpUrl.uri().toString()
                progressListener.update(url, totalBytesRead, responseBody.contentLength(), isDone)
                return bytesRead
            }
        }
    }
}