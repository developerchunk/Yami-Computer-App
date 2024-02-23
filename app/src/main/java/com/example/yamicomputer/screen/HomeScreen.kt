package com.example.yamicomputer.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.yamicomputer.ui.theme.DarkBlue
import com.example.yamicomputer.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "YAMI Computer",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBlue))
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {



        }

    }

}