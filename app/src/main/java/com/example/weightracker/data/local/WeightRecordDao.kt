package com.example.weightracker.data.local

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface WeightRecordDao {

    @Insert
    suspend fun insertWeightRecord(record: WeightRecord)
}