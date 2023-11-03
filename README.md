# TestProject

In the 'components' package, you'll find the 'Components' file, which contains global composables like the AppBar and input fields.
In the 'Location Text' field, there is a coroutine scope with a 1-second delay on 'textChanged' and a mechanism to cancel the 'onTextChanged' if the text changes within less than 1 second.

In the 'data' package, you'll discover the 'Locations' ArrayList. This class serves as a container for a list of location items retrieved from a JSON array. The '[Resource]' class is utilized to encapsulate the response from a network request, providing different states, including success, error, and loading.

Navigation -> This function configures the navigation logic for the application by leveraging Jetpack Compose's navigation components. It defines the navigation graph and designates the initial destination for the app.

The 'LocationRepositoryTest' class tests the repository, and 'ResourceTest' is a straightforward test designed to assess the 'Resource.

![Documents](https://github.com/konbak/TestProject/assets/13588870/57f643b3-2062-430f-9dd9-5d73247662a8)
