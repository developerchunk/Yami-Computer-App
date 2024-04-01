package com.example.yamicomputer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.yamicomputer.screen.AddComplaintScreen
import com.example.yamicomputer.screen.HomeScreen
import com.example.yamicomputer.screen.LoginScreen
import com.example.yamicomputer.screen.ProfileCreateScreen
import com.example.yamicomputer.screen.SplashScreen
import com.example.yamicomputer.screen.TotalComplaintScreen
import com.example.yamicomputer.viewmodel.SharedViewModel

@Composable
fun NavGraph(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {

    NavHost(
        navController = navHostController,
        startDestination = Routes.SplashScreen.id
    ) {

        composable(route = Routes.SplashScreen.id) {
            SplashScreen(navController = navHostController, sharedViewModel = sharedViewModel)
        }

        composable(route = Routes.LoginScreen.id) {
            LoginScreen(navController = navHostController, sharedViewModel = sharedViewModel)
        }

        composable(route = Routes.HomeScreen.id) {
            HomeScreen(navHostController, sharedViewModel)
        }

        composable(route = Routes.ProfileCreateScreen.id) {
            ProfileCreateScreen(navController = navHostController, sharedViewModel = sharedViewModel)
        }

        composable(route = Routes.AddComplaintScreen.id) {
            AddComplaintScreen(
                navController = navHostController,
                sharedViewModel = sharedViewModel
            )
        }

        composable(route = Routes.TotalComplaintScreen.id) {
            TotalComplaintScreen(
                navController = navHostController,
                sharedViewModel = sharedViewModel
            )
        }

    }

}

















