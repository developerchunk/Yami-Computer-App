package com.example.yamicomputer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yamicomputer.R
import com.example.yamicomputer.navigation.Routes
import com.example.yamicomputer.ui.theme.UIBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController
) {

    var mobileno by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier
                .height(150.dp)
                .width(200.dp),
            painter = painterResource(id = R.drawable.yamicard),
            contentDescription = "welcome to yami computer",
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "Mobile No",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            TextField(
                modifier = Modifier
                    .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                    .fillMaxWidth(),
                value = mobileno,
                onValueChange = {
                    mobileno = it
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        Button(
            modifier = Modifier
                .padding(top = 70.dp, start = 30.dp, end = 30.dp, bottom = 100.dp)
                .width(200.dp),
            onClick = {
                navController.popBackStack()
                navController.navigate(Routes.HomeScreen.id)
            },
            shape = RoundedCornerShape(5.dp),

            ) {
            Text(text = "Login", fontSize = 16.sp, fontWeight = FontWeight.Medium)

        }

    }

}