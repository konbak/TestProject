package app.example.testproject.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.example.testproject.data.Resource
import app.example.testproject.model.LocationItem
import app.example.testproject.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel  @Inject constructor(
    private val repository: LocationRepository
): ViewModel() {
    var locationList: List<LocationItem> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(false)


    fun searchLocation(query: String){
        isLoading = true
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                return@launch
            }
            try{
                when(val response = repository.getLocations(query)){
                    is Resource.Success ->{
                        locationList = response.data!!
                        if(locationList.isNotEmpty()) isLoading = false
                    }
                    is Resource.Error ->{
                        isLoading = false
                    }
                    else -> {isLoading = false}
                }

            }catch (e: Exception){
                isLoading = false
            }
        }
    }

    fun clearList() = viewModelScope.launch {
        locationList = emptyList()
    }

}