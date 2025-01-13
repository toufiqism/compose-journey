package com.sol.smsredirectorai

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import java.util.Locale

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        var simSlot: String? = null
        try {
            val extras = intent?.extras
            if (extras != null) {
                val sms = extras.get("pdus") as Array<Any>

                simSlot = detectSim(extras) ?: "undetected"

                Log.d("TEST", "onReceive: $simSlot")
            }
        } catch (ex: Exception) {
            Log.e("TEST", ex.stackTraceToString())
        }
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            messages.forEach { smsMessage ->
                val messageBody = smsMessage.messageBody
                val sender = smsMessage.originatingAddress

                Log.d("SMSReceiver", "SMS received from: $sender")
                Log.d("SMSReceiver", "Message: $messageBody")
                Log.d("SimSlot", "SimSlot: $simSlot")

                // Start the SMS service
                val serviceIntent = Intent(context, SMSService::class.java).apply {
                    putExtra("sender", sender)
                    putExtra("message", messageBody)
                    putExtra("simSlot", simSlot)
                }
                context.startService(serviceIntent)
            }
        }
    }

    private fun detectSim(bundle: Bundle): String? {
        var slot = -1
        val keySet = bundle.keySet()
        for (key in keySet) {
            when (key) {
                "phone" -> slot = bundle.getInt("phone", -1)
                "slot" -> slot = bundle.getInt("slot", -1)
                "simId" -> slot = bundle.getInt("simId", -1)
                "simSlot" -> slot = bundle.getInt("simSlot", -1)
                "slot_id" -> slot = bundle.getInt("slot_id", -1)
                "simnum" -> slot = bundle.getInt("simnum", -1)
                "slotId" -> slot = bundle.getInt("slotId", -1)
                "slotIdx" -> slot = bundle.getInt("slotIdx", -1)
                else -> if (key.lowercase(Locale.getDefault()).contains("slot") or key.lowercase(
                        Locale.getDefault()
                    ).contains("sim")
                ) {
                    val value = bundle.getString(key, "-1")
                    if ((value == "0") or (value == "1") or (value == "2")) {
                        slot = bundle.getInt(key, -1)
                    }
                }
            }
        }
//        Log.d("KKK", slot.toString())
        return when (slot) {
            0 -> {
                "sim1"
            }

            1 -> {
                "sim2"
            }

            else -> {
                "undetected"
            }
        }
    }

} 