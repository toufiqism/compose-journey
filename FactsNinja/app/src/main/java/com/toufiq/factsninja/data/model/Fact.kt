package com.toufiq.factsninja.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facts")
data class Fact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fact: String,
    val timestamp: Long = System.currentTimeMillis()
) 