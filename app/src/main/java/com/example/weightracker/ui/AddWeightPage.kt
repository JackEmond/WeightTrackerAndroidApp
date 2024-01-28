package com.example.weightracker.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.weightracker.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.ui.graphics.Path
import kotlin.math.sin


@Composable
fun AddWeightPage(
    navController: NavHostController,
    viewModel: WeightTrackerViewModel = hiltViewModel()
)
{
    val uiState by viewModel.uiState.collectAsState()

    NavigationHandler(viewModel = viewModel, navController = navController)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_colour))
            .padding(20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.white)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        SquigglyLine(Modifier.weight(1f))
        BackIconButton(Modifier.weight(1f), navController)
        InnerContent(uiState, viewModel, Modifier.weight(5f))
        SquigglyLine(Modifier.weight(1f))
    }
}

@Composable
fun NavigationHandler(viewModel: WeightTrackerViewModel, navController: NavHostController) {
    val navigateHome by viewModel.navigateHome
    if (navigateHome) {
        LaunchedEffect(Unit) {
            navController.navigate("home")
            viewModel.onNavigationDone()
        }
    }
}



@Composable
fun BackIconButton(modifier: Modifier, navController: NavHostController) {
    Box( modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp),
            onClick = { navController.navigate("home") }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,


            )
        }
    }
}

@Composable
fun InnerContent(uiState: AddWeightUiState, viewModel: WeightTrackerViewModel, modifier: Modifier) {
    Column (modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){


        Text(
            text = "Add Weight",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom=20.dp)

        )

        //textDecoration = TextDecoration.Underline, fontSize = 20.dp)
        AddWeightInput(weight = uiState.weight, onWeightChanged = viewModel::updateWeight)
        DateInput(date = uiState.date, onDateChanged = viewModel::updateDate)
        SubmitButton(saveContent = viewModel::saveContent)
        Text(text = uiState.errorMessage)
    }

}

@Composable
fun SquigglyLine(modifier: Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        val path = Path()
        val amplitude = 20f // Height of the waves
        val frequency = 0.05f // Frequency of the waves

        // Start at the top left
        path.moveTo(0f, size.height / 2)

        // Draw the flowing squiggly line
        for (x in 0 until size.width.toInt()) {
            val y = size.height / 2 + amplitude * sin(x * frequency)
            path.lineTo(x.toFloat(), y)
        }

        // Draw the path
        drawPath(path = path, color = Color(R.color.background_colour)
        )
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
    //SquigglyLine(Modifier.Companion.weight(1f))
}