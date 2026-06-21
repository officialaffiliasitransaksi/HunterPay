package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    suspend fun planTrip(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Halo! 🌴 Saya HunterPAI, Asisten AI resmi HunterPay.\n\nTampaknya API Key Gemini belum terkonfigurasi di Secrets. Namun saya bisa merekomendasikan destinasi terpopuler untuk Anda:\n\n1. **Bali (DPS)**: Pantai Kuta, Uluwatu, dan Ubud. Tiket mulai Rp 750.000.\n2. **Yogyakarta (YIA)**: Candi Borobudur & Malioboro. Tiket mulai Rp 450.000.\n3. **Labuan Bajo (LBJ)**: Pulau Padar & Taman Nasional Komodo. Tiket mulai Rp 1.200.000.\n\nAnda bisa langsung mengetik rencana perjalanan baru, atau gunakan menu di beranda untuk memesan Tiket & Hotel dengan saldo HunterPay Anda! 😊"
        }

        try {
            val payload = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                }
                put("contents", contentsArray)

                val systemInstruction = JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "Anda adalah HunterPAI, Asisten AI Pintar HunterPay. Jawab pertanyaan pengguna dengan ramah, berikan info travel terbaik di Indonesia (seperti rekomendasi hotel/pesawat/tempat wisata/kuliner), pas dengan budget mereka, serta jelaskan bahwa pembayaran rincian liburan tersebut dapat dibayar instan melalui HunterPay Wallet. Gunakan Bahasa Indonesia yang sopan dan ceria dengan emoji yang sesuai.")
                        })
                    })
                }
                put("systemInstruction", systemInstruction)
            }

            val requestBody = payload.toString().toRequestBody("application/json".toMediaType())
            val url = "$BASE_URL?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e("GeminiClient", "API failed: $errBody")
                    return@withContext "Halo! HunterPAI sedang mengalami sedikit gangguan koneksi. Tenang, Anda tetap bisa memesan tiket pesawat & hotel impian Anda langsung di aplikasi HunterPay!"
                }

                val bodyStr = response.body?.string() ?: return@withContext "Respon kosong dari AI."
                val responseJson = JSONObject(bodyStr)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "Halo! Mau cari info tiket atau hotel apa hari ini? HunterPAI siap membantu!")
                    }
                }
                return@withContext "Maaf, HunterPAI kurang mengerti pertanyaan itu. Mau rekomendasi destinasi wisata di Bali atau Yogyakarta? 😊"
            }
        } catch (e: Exception) {
            Log.e("GeminiClient", "Exception: ${e.message}", e)
            return@withContext "Halo! HunterPAI sedang beristirahat sejenak 🌴 Namun, rekomendasi terbaik saya hari ini adalah liburan ke Bali (DPS) dengan menginap di Kuta Resort & Spa. Silakan pesan langsung di HunterPay!"
        }
    }
}
