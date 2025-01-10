package com.toufiq.factsninja.data.remote

import com.toufiq.factsninja.data.model.Fact
import retrofit2.http.GET
import retrofit2.http.Headers

interface FactsApi {
    @Headers(
        "X-Api-Key: nfbK0fQv/Ikx91nSQ0OLIw==3e33TZv6bpGu97R2"
    )
    @GET("v1/facts")
    suspend fun getRandomFact(): List<Fact>
} 