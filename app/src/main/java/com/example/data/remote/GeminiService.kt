package com.example.data.remote

import com.example.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// Re-using simplified classes compatible with Moshi to prevent serialization version issues
data class GeminiPart(val text: String? = null)

data class GeminiContent(val parts: List<GeminiPart>)

data class GeminiGenerationConfig(val temperature: Float? = 0.7f)

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null,
    val generationConfig: GeminiGenerationConfig? = null
)

data class GeminiResponseCandidate(val content: GeminiContent)

data class GeminiResponse(val candidates: List<GeminiResponseCandidate>?)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    suspend fun askMentor(prompt: String, systemPrompt: String = "You are BCS Assistant, a professional AI mentor for Bangladeshi BCS and government job exams. Answer in Bangla or English clearly, giving subject matter advice, study strategy, or explanation for MCQs."): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "GEMINI_API_KEY") {
            return "দুঃখিত, এআই মেন্টর ব্যবহার করার জন্য আপনার একটি বৈধ API Key প্রয়োজন। দয়া করে Google AI Studio-র Secrets প্যানেলে GEMINI_API_KEY সেট করুন।"
        }

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)))
        )

        return try {
            val response = service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "কোনো উত্তর পাওয়া যায়নি। পুনরায় চেষ্টা করুন।"
        } catch (e: Exception) {
            "Error: ${e.localizedMessage ?: "নেটওয়ার্ক ত্রুটি ঘটেছে। পুনরায় চেষ্টা করুন।"}"
        }
    }
}
