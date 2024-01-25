package com.example.weightracker.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface WeightRecordDao {

    @Insert
    suspend fun insertWeightRecord(record: WeightRecord)

    @Query("Select * FROM weightrecord")
    fun getAllWeightsAndDates(): LiveData<List<WeightRecord>>


}