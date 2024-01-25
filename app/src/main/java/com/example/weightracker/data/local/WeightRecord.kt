package com.example.weightracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeightRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weight: Float,
    val date: Long
)