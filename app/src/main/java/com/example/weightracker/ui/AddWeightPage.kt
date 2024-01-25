package com.example.weightracker.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun AddWeightPage(
    navController: NavHostController,
    viewModel: WeightTrackerViewModel = hiltViewModel()
)
{
    val uiState by viewModel.uiState.collectAsState()

    val navigateToNextScreen by viewModel.navigateToNextScreen
    if (navigateToNextScreen) {
        LaunchedEffect(Unit) {
            navController.navigate("home")
            viewModel.onNavigationDone()
        }
    }


    /*
    LaunchedEffect(uiState.weight, uiState.date) {
        Log.d("AddWeightPage", "Weight: ${uiState.weight}, Date: ${uiState.date}")
    }
     */

    Column ( modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){

        AddWeightInput(weight = uiState.weight, onWeightChanged = viewModel::updateWeight)
        DateInput(date = uiState.date, onDateChanged = viewModel::updateDate)
        SubmitButton(saveContent = viewModel::saveContent)
    }
}


@Composable
private fun AddWeightInput(onWeightChanged: (String) -> Unit, weight: String) {
    TextField(
        value = weight,
        onValueChange = onWeightChanged,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        label = { Text("Current Weight") }
    )
}


@Composable
fun DateInput(date: String, onDateChanged: (String) -> Unit) {
    val context = LocalContext.current
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Format the date and update state
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
            onDateChanged(dateFormat.format(calendar.time))
        },
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    )

    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
    ){
        Text(
            text = date,
            fontSize = 16.sp,
        )
        Button(onClick = {
            datePickerDialog.show()
        }) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit"
            )
        }
    }
}


@Composable
fun SubmitButton(saveContent:() -> Unit) {
    Button(onClick = saveContent) {
        Text("Submit")
    }
}




@Preview(showBackground = true)
@Composable
fun AddWeightPagePreview() {
    //AddWeightPage()
}