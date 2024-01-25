package com.example.weightracker.data

import android.util.Log
import com.example.weightracker.data.local.WeightRecord
import com.example.weightracker.data.local.WeightRecordDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightRecordRepository @Inject constructor(
    private val localDataSource: WeightRecordDao,
) {
    suspend fun insertWeightRecord(weight: Float, date: Long) {
        val weightRecord = WeightRecord(
            weight = weight,
            date = date
        )
        Log.w("Jack test", "weight is ${weightRecord.weight}")
        localDataSource.insertWeightRecord(weightRecord)
    }
}
