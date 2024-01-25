package com.example.weightracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.weightracker.R
import com.example.weightracker.data.local.WeightRecord

@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: WeightTrackerViewModel = hiltViewModel()
) {

    Column ( modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        WeightLossLogo()
        GoToAddWeightPageButton{navController.navigate("addWeight")}

        val weightRecords by viewModel.getAllWeightsAndDates().observeAsState(initial = emptyList())
        DisplayWeights(weightRecords)
    }
}



@Composable
fun DisplayWeights(weightRecords: List<FormattedWeightRecord>) {
    LazyColumn {
        items(weightRecords) { weightRecord ->
            DisplayWeight(weightRecord)
        }
    }

}

@Composable
fun DisplayWeight(weightRecord: FormattedWeightRecord) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
        ){
        Column {
            Text(text = weightRecord.weight.toString(),
                fontSize = 20.sp)
            Text(text = weightRecord.date, fontSize = 12.sp)
        }
        Image(
            painter = painterResource(id = R.drawable.trash_can),
            contentDescription = "trash_can",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(50.dp, 30.dp)
        )
    }
}



@Composable
fun GoToAddWeightPageButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Add Weight")
    }
}

@Composable
fun WeightLossLogo() {
    Column(
        modifier = Modifier.padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.weight_loss_logo),
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(150.dp, 60.dp)
        )
        Text(
            text = "Weight Loss",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WeightLossLogoPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WeightLossLogo()
        GoToAddWeightPageButton(onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayWeightsPreview() {
    DisplayWeight(FormattedWeightRecord( 200.00F, "12/24/2023"))
}
