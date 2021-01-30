package com.stopkaaaa.develoverslife.data.net

import com.stopkaaaa.develoverslife.data.Mem
import retrofit2.Response
import retrofit2.http.GET

interface DevelopersLifeAPI {
    @GET("random?json=true")
    suspend fun getRandomMem(): Response<Mem>
}