package app.example.testproject.repository

import android.util.Log
import app.example.testproject.data.Resource
import app.example.testproject.model.LocationItem
import app.example.testproject.network.TestApi
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val api: TestApi
) {

    suspend fun getLocations(searchQuery: String): Resource<List<LocationItem>> {
        return try {
            val itemList = api.getLocations(searchQuery)
            Resource.Success(data = itemList)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}