package com.toufiq.factsninja.di

import android.content.Context
import androidx.room.Room
import com.toufiq.factsninja.data.local.FactsDatabase

object DatabaseModule {
    private var INSTANCE: FactsDatabase? = null

    fun getDatabase(context: Context): FactsDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                FactsDatabase::class.java,
                "facts_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
} 