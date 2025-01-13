package com.sol.smsredirectorai.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SmsDao {
    @Insert
    suspend fun insert(sms: SmsData)

    @Delete
    suspend fun delete(sms: SmsData)

    @Query("SELECT * FROM pending_sms")
    suspend fun getAllPendingSms(): List<SmsData>

    @Query("SELECT * FROM pending_sms ORDER BY id DESC LIMIT 1")
    suspend fun getLastSms(): SmsData?
} 