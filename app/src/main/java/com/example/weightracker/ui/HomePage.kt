package com.example.weightracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.weightracker.R

@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: WeightTrackerViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_colour)),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        WeightLossLogo()

        Column(
            Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(Color.White)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            GoToAddWeightPageButton{navController.navigate("addWeight")}
            DisplayWeights(viewModel)
        }
    }
}

@Composable
fun DisplayWeights(viewModel: WeightTrackerViewModel) {
    val weightRecords by viewModel.getAllWeightsAndDates().observeAsState(initial = emptyList())
    LazyColumn{
        items(weightRecords) { weightRecord ->
            DisplayWeight(weightRecord, viewModel)
        }
    }

}

@Composable
fun DisplayWeight(weightRecord: FormattedWeightRecord, viewModel: WeightTrackerViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically,
        ){
        Column {
            Text(text = weightRecord.weight.toString(),
                fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(text = weightRecord.date, fontSize = 12.sp)
        }
        Image(
            painter = painterResource(id = R.drawable.trash_can),
            contentDescription = "trash_can",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(20.dp)
                .size(60.dp, 40.dp)
                .clickable { viewModel.deleteItem(weightRecord.id) }
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
    Column (
        Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
        fontWeight = FontWeight.ExtraBold
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
    //DisplayWeight(FormattedWeightRecord( 1, 200.00F, "12/24/2023"),)
}

