package app.example.testproject.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.example.testproject.screens.home.Home
import app.example.testproject.screens.home.HomeScreenViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.HomeScreen.name){

        composable(ReaderScreens.HomeScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            Home(viewModel = homeViewModel)
        }

    }
}