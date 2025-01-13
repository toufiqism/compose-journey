package com.sol.smsredirectorai.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "pending_sms")
data class SmsData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sender: String,
    val receiver: String,
    val body: String,
    val timestamp: String,
    val simSlot: String = "undetected"
) {
    companion object {
        fun create(sender: String, receiver: String, body: String,simSlot: String="undetected"): SmsData {
            val timestamp = SimpleDateFormat("yyyy/dd/MM HH:mm:ss", Locale.getDefault())
                .format(Date())
            return SmsData(
                sender = sender,
                receiver = receiver,
                body = body,
                timestamp = timestamp,
                simSlot=simSlot
            )
        }
    }
} 