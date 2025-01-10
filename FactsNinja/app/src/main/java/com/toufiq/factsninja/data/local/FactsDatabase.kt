package com.toufiq.factsninja.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.toufiq.factsninja.data.model.Fact

@Database(entities = [Fact::class], version = 1)
abstract class FactsDatabase : RoomDatabase() {
    abstract fun factsDao(): FactsDao
} 