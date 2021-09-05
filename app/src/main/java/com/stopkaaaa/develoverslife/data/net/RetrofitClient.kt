package com.stopkaaaa.develoverslife.data.net

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.stopkaaaa.develoverslife.BuildConfig
import com.stopkaaaa.develoverslife.data.Mem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {

  private val okHttpClient =
      OkHttpClient()
          .newBuilder()
          .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
          .addInterceptor {
            val request = it.request()
            val url = request.url.newBuilder().addQueryParameter("json", "true").build()
            it.proceed(request.newBuilder().url(url).build())
          }
          .build()

  private val contentType = "application/json".toMediaType()

  private val json = Json { ignoreUnknownKeys = true }

  @Suppress("EXPERIMENTAL_API_USAGE")
  private val retrofit: Retrofit =
      Retrofit.Builder()
          .client(okHttpClient)
          .baseUrl(BuildConfig.DEVELOPERS_LIFE_BASE_URL)
          .addConverterFactory(json.asConverterFactory(contentType))
          .build()

  private val developersLifeAPI: DevelopersLifeAPI = retrofit.create(DevelopersLifeAPI::class.java)

  suspend fun getRandomMem(): Result<List<Mem>> =
      withContext(Dispatchers.IO) {
        try {
          val mem = developersLifeAPI.getRandomMem()
          Result.success(listOf(mem))
        } catch (exception: Exception) {
          Result.failure(exception)
        }
      }

  suspend fun getLatestMemes(page: Int): Result<List<Mem>> =
      withContext(Dispatchers.IO) {
        try {
          val memes = developersLifeAPI.getLatestMemes(page)
          Result.success(memes.list)
        } catch (exception: Exception) {
          Result.failure(exception)
        }
      }

  suspend fun getTopMemes(page: Int): Result<List<Mem>> =
      withContext(Dispatchers.IO) {
        try {
          val memes = developersLifeAPI.getTopMemes(page)
          Result.success(memes.list)
        } catch (exception: Exception) {
          Result.failure(exception)
        }
      }

  suspend fun getHotMemes(page: Int): Result<List<Mem>> =
      withContext(Dispatchers.IO) {
        try {
          val memes = developersLifeAPI.getHotMemes(page)
          Result.success(memes.list)
        } catch (exception: Exception) {
          Result.failure(exception)
        }
      }
}
