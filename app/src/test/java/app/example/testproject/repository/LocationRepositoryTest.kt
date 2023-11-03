package app.example.testproject.repository

import app.example.testproject.data.Locations
import app.example.testproject.data.Resource
import app.example.testproject.model.LocationItem
import app.example.testproject.network.TestApi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class LocationRepositoryTest {

    @Mock
    private lateinit var api: TestApi

    private lateinit var repository: LocationRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = LocationRepository(api)
    }

    @Test
    fun testGetLocationsSuccess() = runBlocking {
        val searchQuery = "example"
        val locationList = Locations().apply {
            add(LocationItem("1", "Location 1", "Description 1"))
            add(LocationItem("2", "Location 2", "Description 2"))
            // Add more LocationItem objects as needed
        }

        // Mock the behavior of the API call
        Mockito.`when`(api.getLocations(searchQuery)).thenReturn(locationList)

        val result = repository.getLocations(searchQuery)

        // Verify that the result is a Resource.Success containing the locationList
        assert(result is Resource.Success)
        val actualSuccessResult = result as Resource.Success
        assertEquals(locationList, actualSuccessResult.data)
    }

    @Test
    fun testGetLocationsError() = runBlocking {
        val searchQuery = "example"
        val errorMessage = "An error occurred"

        // Mock the behavior of the API call to throw an exception
        Mockito.`when`(api.getLocations(searchQuery)).thenThrow(RuntimeException(errorMessage))

        val result = repository.getLocations(searchQuery)

        // Verify that the result is a Resource.Error containing the error message
        assert(result is Resource.Error)
        val errorResult = result as Resource.Error
        assertEquals(errorMessage, errorResult.message)
    }
}