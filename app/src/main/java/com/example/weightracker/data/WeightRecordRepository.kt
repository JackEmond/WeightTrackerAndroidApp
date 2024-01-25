package com.example.weightracker.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weightracker.data.local.WeightRecord
import com.example.weightracker.data.local.WeightRecordDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
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

    fun getAllWeightsAndDates(): LiveData<List<WeightRecord>> = localDataSource.getAllWeightsAndDates()

    suspend fun deleteRecord(id: Int) {
        localDataSource.deleteById(id)
    }
}
