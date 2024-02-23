package com.example.yamicomputer.navigation

sealed class Routes(val id: String) {

    object LoginScreen: Routes("login_screen")
    object HomeScreen: Routes("home_screen")

}