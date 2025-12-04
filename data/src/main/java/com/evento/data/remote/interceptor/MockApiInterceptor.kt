package com.evento.data.remote.interceptor


import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class MockApiInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
): Interceptor{
    private val mockDelayMs = 800L

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath


        Log.d("MockApi", "URL = ${chain.request().url}")
        Log.d("MockApi", "encodedPath = ${chain.request().url.encodedPath}")
        Log.d("MockApi", "method = ${chain.request().method}")

        val fileName = when {
            path.endsWith("/api/events") && request.method == "GET" -> "mock/booked_events.json"

            path.endsWith("/slots/available") && request.method == "GET" ->
                "mock/available_slots.json"

            path.endsWith("/events") && request.method == "POST" ->
                "mock/create_event.json"

            else -> null
        }

        return if (fileName != null) {
            Thread.sleep(mockDelayMs)
            val json = loadJsonFromAssets(fileName)
            buildMockResponse(chain, json)
        } else {
            // fallback to real network if you want
            chain.proceed(request)
        }
    }

    private fun loadJsonFromAssets(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    private fun buildMockResponse(chain: Interceptor.Chain, bodyString: String): Response {
        return Response.Builder()
            .code(200)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .body(bodyString.toByteArray().toResponseBody("application/json".toMediaType()))
            .build()
    }

}