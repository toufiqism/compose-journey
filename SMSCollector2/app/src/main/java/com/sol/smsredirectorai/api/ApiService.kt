package com.sol.smsredirectorai.api

import com.sol.smsredirectorai.data.SmsData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    @POST
    suspend fun sendSms(@Url url: String, @Body smsData: SmsData): Response<Unit>
} 