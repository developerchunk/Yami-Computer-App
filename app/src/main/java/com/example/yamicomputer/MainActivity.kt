package com.example.yamicomputer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.yamicomputer.navigation.NavGraph
import com.example.yamicomputer.ui.theme.YamiComputerTheme
import com.example.yamicomputer.logic.SharedViewModel

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YamiComputerTheme {

            navController = rememberNavController()
            NavGraph(navHostController = navController, sharedViewModel = viewModel)

            }
        }
    }
}

