package com.ludovic.vimont.nasaapod.api.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.*
import java.io.InputStream

/**
 * We use a custom GlideModule to be able to catch the download of the image through an OkHttp interceptor.
 * @see: https://medium.com/@mr.johnnyne/how-to-use-glide-v4-load-image-with-progress-update-eb02671dac18
 */
@GlideModule
class NasaGlideModule: AppGlideModule() {
    private val dispatchProgressListener = GlideDispatchProgressListener()

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor { chain: Interceptor.Chain ->
                getNetworkInterceptor(chain)
            }.build()

        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }

    private fun getNetworkInterceptor(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        return response.newBuilder()
            .body(response.body()?.let { responseBody: ResponseBody ->
                OkHttpResponseBody(request.url(), responseBody, dispatchProgressListener)
            })
            .build()
    }
}