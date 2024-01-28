package com.example.weightracker.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightracker.data.WeightRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.map

data class AddWeightUiState(
    val weight: String = "",
    val date: String = "",
    val errorMessage: String = ""
)
data class FormattedWeightRecord(
    val id: Int,
    val weight: Float,
    val date: String
)

@HiltViewModel
class WeightTrackerViewModel @Inject constructor(
    private val repository: WeightRecordRepository
): ViewModel() {
    // Expose screen UI state
    private val _uiState = MutableStateFlow(AddWeightUiState())
    val uiState: StateFlow<AddWeightUiState> = _uiState.asStateFlow()

    //Variables to navigate to home page after inserting content into db
    private val _navigateHome = mutableStateOf(false)
    val navigateHome: State<Boolean> = _navigateHome


    fun getAllWeightsAndDates(): LiveData<List<FormattedWeightRecord>> {
        return repository.getAllWeightsAndDates().map { list ->
            list.map { weightRecord ->
                FormattedWeightRecord(
                    id = weightRecord.id,
                    weight = weightRecord.weight,
                    date = convertLongToDate(weightRecord.date)
                )
            }
        }
    }

    fun deleteItem(id: Int) = viewModelScope.launch{
            repository.deleteRecord(id)
    }

    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return format.format(date)
    }

    init {
        //update ui state to the current date
        val currentDate = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(Date())
        _uiState.update { it.copy(date = currentDate) }
    }

    fun updateWeight(newWeight: String) {
        _uiState.update {
            it.copy(weight = newWeight)
        }
    }

    fun updateDate(newDate: String) {
        _uiState.update {
            it.copy(date = newDate)
        }
    }

    fun saveContent() {
        val weight = uiState.value.weight.toFloatOrNull()
        val date = uiState.value.date.toDate()?.time


        var errorMessage = ""
        if (weight == null)    errorMessage += "Enter a valid weight!"
        else if(weight <= 0)   errorMessage += "Weight must be above 0 lbs!"
        else if(weight > 2000) errorMessage += "Weight must be below 2000 lbs!"



        //if(date > currentDate) show error

        if(errorMessage != ""){
            _uiState.update {
                it.copy(errorMessage = errorMessage)
            }
            return
        }

        if (weight == null || date == null) return

        _navigateHome.value = true
        addWeightInfoToDatabase(weight, date)
    }



    private fun addWeightInfoToDatabase(weight: Float, date: Long)
            = viewModelScope.launch{
        repository.insertWeightRecord(weight, date)
    }

    private fun String.toDate(format: String = "MM-dd-yyyy"): Date? {
        return try {
            SimpleDateFormat(format, Locale.getDefault()).parse(this)
        } catch (e: Exception) {
            null
        }
    }

    fun onNavigationDone() {
        _navigateHome.value = false
    }



}