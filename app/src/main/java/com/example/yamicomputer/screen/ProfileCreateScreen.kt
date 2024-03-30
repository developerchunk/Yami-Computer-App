package com.example.yamicomputer.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yamicomputer.data.ProfileData
import com.example.yamicomputer.navigation.Routes
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreateScreen(
    navController: NavController
) {

    val context = LocalContext.current

    var name by remember {
        mutableStateOf("")
    }

    var address by remember {
        mutableStateOf("")
    }

    var city by remember {
        mutableStateOf("")
    }

    // for firebase auth and callback
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val userId: FirebaseUser = mAuth.currentUser!!

    // on below line creating variable for freebase database
    // and database reference.
//    val firebaseDatabase = FirebaseDatabase.database
    val database = Firebase.database
    val databaseReference = database.getReference("customer-profile")


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Name
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "Full Name",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            TextField(
                modifier = Modifier
                    .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                    .fillMaxWidth(),
                value = name,
                onValueChange = {
                    name = it
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
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        // Address
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "Address",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            TextField(
                modifier = Modifier
                    .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                    .fillMaxWidth(),
                value = address,
                onValueChange = {
                    address = it
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
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        // City
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "City",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            TextField(
                modifier = Modifier
                    .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                    .fillMaxWidth(),
                value = city,
                onValueChange = {
                    city = it
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
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        Button(
            modifier = Modifier
                .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 100.dp)
                .width(200.dp),
            onClick = {

                // on below line we are adding data.
                val profileData = ProfileData(name, address, city)

                val newRef = databaseReference.child(userId.uid)
                newRef.setValue(profileData)
                Log.d("error-firebase-database", "onCancelled:")
                databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // Data has changed
                        Toast.makeText(
                            context,
                            "Profile Created!",
                            Toast.LENGTH_SHORT
                        ).show()

                        navController.popBackStack()
                        navController.navigate(Routes.HomeScreen.id)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Error occurred
                        Toast.makeText(
                            context,
                            "Fail to add data $error",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d("error-firebase-database", "onCancelled: $error")
                    }
                })

            },
            shape = RoundedCornerShape(10.dp),

            ) {

            Text(text = "Continue", fontSize = 16.sp, fontWeight = FontWeight.Medium)

        }

    }

}