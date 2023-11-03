package app.example.testproject.network

import app.example.testproject.data.Locations
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface TestApi {

    @GET("https://4ulq3vb3dogn4fatjw3uq7kqby0dweob.lambda-url.eu-central-1.on.aws/")
    suspend fun getLocations(@Query("input") input: String): Locations
}