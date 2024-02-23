package com.example.yamicomputer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.yamicomputer.screen.HomeScreen
import com.example.yamicomputer.screen.LoginScreen

@Composable
fun NavGraph(
    navHostController: NavHostController
) {

    NavHost(
        navController = navHostController,
        startDestination = Routes.HomeScreen.id
    ) {

        composable(route = Routes.LoginScreen.id) {
            LoginScreen(navController = navHostController)
        }

        composable(route = Routes.HomeScreen.id) {
            HomeScreen()
        }

    }

}

















