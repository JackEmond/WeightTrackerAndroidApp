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

data class AddWeightUiState(
    val weight: String = "",
    val date: String = "",
)

@HiltViewModel
class WeightTrackerViewModel @Inject constructor(
    private val repository: WeightRecordRepository
): ViewModel() {
    // Expose screen UI state
    private val _uiState = MutableStateFlow(AddWeightUiState())
    val uiState: StateFlow<AddWeightUiState> = _uiState.asStateFlow()

    //Variables to navigate to home page after inserting content into db
    private val _navigateToNextScreen = mutableStateOf(false)
    val navigateToNextScreen: State<Boolean> = _navigateToNextScreen


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

        if (weight == null || date == null) {
            _uiState.update {
                TODO("Not yet implemented")
                //Pop up error on screen
                //it.copy(userMessage = R.string.empty_task_message)
            }
            return
        }
        _navigateToNextScreen.value = true
        addWeightInfoToDatabase(weight, date);
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
        _navigateToNextScreen.value = false
    }

}