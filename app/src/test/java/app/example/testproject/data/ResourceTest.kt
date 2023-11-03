package app.example.testproject.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class ResourceTest {

    @Test
    fun successResource_containsData() {
        // Arrange
        val expectedData = "Test data"

        // Act
        val resource = Resource.Success(expectedData)

        // Assert
        assertEquals(expectedData, resource.data)
        assertEquals(null, resource.message)
    }

    @Test
    fun errorResource_containsMessageAndData() {
        // Arrange
        val expectedMessage = "Test error message"
        val expectedData = 123

        // Act
        val resource = Resource.Error(expectedMessage, expectedData)

        // Assert
        assertEquals(expectedMessage, resource.message)
        assertEquals(expectedData, resource.data)
    }

    @Test
    fun loadingResource_containsData() {
        // Arrange
        val expectedData = listOf(1, 2, 3)

        // Act
        val resource = Resource.Loading(expectedData)

        // Assert
        assertEquals(expectedData, resource.data)
        assertEquals(null, resource.message)
    }

    @Test
    fun loadingResource_mockData() {
        // Arrange
        val data: List<Int>? = mock(List::class.java) as List<Int>?

        // Act
        val resource = Resource.Loading(data)

        // Assert
        assertEquals(data, resource.data)
        assertEquals(null, resource.message)
    }
}