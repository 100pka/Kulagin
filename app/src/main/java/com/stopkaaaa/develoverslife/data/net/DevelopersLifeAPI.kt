package com.stopkaaaa.develoverslife.data.net

import com.stopkaaaa.develoverslife.data.Mem
import com.stopkaaaa.develoverslife.data.Memes
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DevelopersLifeAPI {
    @GET("random")
    suspend fun getRandomMem(): Mem

    @GET("latest/{page}")
    suspend fun getLatestMemes(@Path("page") page: Int): Memes

    @GET("top/{page}")
    suspend fun getTopMemes(@Path("page") page: Int): Memes

    @GET("hot/{page}")
    suspend fun getHotMemes(@Path("page") page: Int): Memes
}