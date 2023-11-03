package app.example.testproject.screens.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.example.testproject.components.LocationInput
import app.example.testproject.components.TextInput
import app.example.testproject.components.TheAppBar
import app.example.testproject.model.LocationItem
import org.json.JSONObject

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(viewModel: HomeScreenViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TheAppBar(title = "New property")
        }
    ) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(top = it.calculateTopPadding())) {
            HomeContent(viewModel)
        }
    }
}

@Composable
private fun HomeContent(viewModel: HomeScreenViewModel){

    val openDialog = remember{
        mutableStateOf(false)
    }
    val jsonString: MutableState<String> = remember{mutableStateOf("")}

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            UserForm(viewModel) { title, price, description, locationId ->
                val jsonObject = JSONObject()

                jsonObject.put("title", title)
                jsonObject.put("price", price)
                jsonObject.put("description", description)
                jsonObject.put("locationId", locationId)

                jsonString.value = jsonObject.toString()

                openDialog.value = true
            }
        }

        if(openDialog.value){
            ShowAlertDialog(message = jsonString.value, openDialog){
                openDialog.value = false
            }

        }
    }


}

@Composable
fun ShowAlertDialog(
    message: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {  },
            title = { Text(text = "Send data?") },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(onClick = { onYesPressed.invoke() }) {
                    Text(text = "Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "cancel")
                }
            },
            modifier = Modifier.padding(all = 8.dp)
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UserForm(
    viewModel: HomeScreenViewModel,
    onDone: (String, String, String, String) -> Unit = { title, price, description, locationId -> }
){
    val title = rememberSaveable{ mutableStateOf("") }
    val price = rememberSaveable{mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf ("") }
    val location = rememberSaveable {mutableStateOf ("") }
    val savedLocation = rememberSaveable {mutableStateOf ("")}
    val savedLocationId = rememberSaveable { mutableStateOf("") }
    val loading = rememberSaveable { mutableStateOf(false) }
    val priceFocusRequest = FocusRequester()
    val descriptionFocusRequest = FocusRequester()
    val locationFocusRequest = FocusRequester()
    val isLocationFocused = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(!isLocationFocused.value) {

            TextInput(
                titleState = title,
                labelId = "title",
                enabled = !loading.value,
                onAction = KeyboardActions {
                    priceFocusRequest.requestFocus()
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextInput(
                modifier = Modifier.focusRequester(priceFocusRequest),
                titleState = price,
                labelId = "price",
                enabled = !loading.value,
                onAction = KeyboardActions {
                    descriptionFocusRequest.requestFocus()
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextInput(
                modifier = Modifier.focusRequester(descriptionFocusRequest),
                titleState = description,
                labelId = "description",
                enabled = !loading.value,
                onAction = KeyboardActions {
                    locationFocusRequest.requestFocus()
                }
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        Text(text = savedLocation.value)

        LocationInput(
            modifier = Modifier
                .focusRequester(locationFocusRequest),
            titleState = location,
            labelId = "search location",
            enabled = !loading.value,
        ){ text ->
            isLocationFocused.value = text.isNotEmpty()
            viewModel.searchLocation(text)
        }
        if(isLocationFocused.value) {
            LocationList(viewModel) {
                location.value = ""
                isLocationFocused.value = false
                keyboardController?.hide()
                viewModel.clearList()
                savedLocation.value = "${it.mainText}, ${it.secondaryText}"
                savedLocationId.value = it.placeId
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if(savedLocationId.value.isEmpty() || title.value.isEmpty()){
                        Toast.makeText(context, "Please enter title and location", Toast.LENGTH_LONG).show()
                    }else{
                        onDone(title.value, price.value, description.value, savedLocationId.value)
                        title.value = ""
                        price.value = ""
                        description.value = ""
                        location.value = ""
                        savedLocation.value = ""
                        savedLocationId.value = ""
                    }
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
            ) {
                Text(text = "Confirm")
            }

            Button(
                onClick = {
                    title.value = ""
                    price.value = ""
                    description.value = ""
                    location.value = ""
                    savedLocation.value = ""
                    savedLocationId.value = ""
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
            ) {
                Text(text = "Clear")
            }
        }
    }

}

@Composable
private fun LocationList(
    viewModel: HomeScreenViewModel,
    onLocation:(LocationItem) -> Unit = {}
){
    val listOfLocations = viewModel.locationList

    if(viewModel.isLoading){
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            LinearProgressIndicator()
            Text(text = "Loading...")
        }
    }else{
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)){
            items(items= listOfLocations){ location ->
                LocationRow(location){
                    onLocation(it)
                }
            }
        }
    }

}

@Composable
private fun LocationRow(
    location: LocationItem,
    onLocationSelected: (LocationItem) -> Unit = {}
){
    Card(
        modifier = Modifier
            .clickable {
                onLocationSelected(location)
            }
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp)
    ) {
        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {
            Column() {
                Text(text = location.mainText, overflow = TextOverflow.Ellipsis)
                Text(
                    text = location.secondaryText,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic
                )

            }
        }
    }
}