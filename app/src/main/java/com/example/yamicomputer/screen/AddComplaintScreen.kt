package com.example.yamicomputer.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yamicomputer.data.ComplaintData
import com.example.yamicomputer.data.ComplaintStatus
import com.example.yamicomputer.data.stringToComplaintStatus
import com.example.yamicomputer.ui.theme.BrightRed
import com.example.yamicomputer.viewmodel.SharedViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddComplaintScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {

    val context = LocalContext.current

    val id by sharedViewModel.id

    /** we are checking if id is empty or not, here if the id is empty
    then this screen will be used to create new Complaint and
    if id is not empty then this screen will be used to update an existing complaint.
    This variable gets the ID of the existing complaint. **/
    var idNotEmpty by remember {
        mutableStateOf(id.isNotEmpty())
    }

    idNotEmpty = id.isNotEmpty()

    val complaintData by sharedViewModel.complaintData

    var name by rememberSaveable {
        mutableStateOf(complaintData.name)
    }

    var date by rememberSaveable {
        mutableStateOf(complaintData.date)
    }

    var item by rememberSaveable {
        mutableStateOf(complaintData.item)
    }

    var problem by rememberSaveable {
        mutableStateOf(complaintData.problem)
    }

    var complaintStatus by rememberSaveable {
        mutableStateOf(complaintData.status.stringToComplaintStatus())
    }

    var complaintStatusClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var charge by rememberSaveable {
        mutableStateOf(complaintData.charge)
    }

    var photo by rememberSaveable {
        mutableStateOf(complaintData.photo)
    }

    var addComplaintClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var deleteClicked by rememberSaveable {
        mutableStateOf(false)
    }

    // on below line creating variable for freebase database
    // and database reference.
//    val firebaseDatabase = FirebaseDatabase.database
    val database = Firebase.database
    val databaseReference = database.getReference("customer-complaints")

    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {

                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "back",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )

                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    text = "Name",
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
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            // date
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "Date",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                TextField(
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                        .fillMaxWidth(),
                    value = date,
                    onValueChange = {
                        date = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            // item
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "Item",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                TextField(
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                        .fillMaxWidth(),
                    value = item,
                    onValueChange = {
                        item = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            // problem
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "Problem",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                TextField(
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                        .fillMaxWidth(),
                    value = problem,
                    onValueChange = {
                        problem = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            // complaint Status
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "Complaint Status",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                TextField(
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                        .fillMaxWidth()
                        .clickable {
                            complaintStatusClicked = !complaintStatusClicked
                        },
                    value = complaintStatus.name,
                    onValueChange = {

                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        disabledTextColor = Color.Black
                    ),
                    enabled = false
                )

                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    expanded = complaintStatusClicked,
                    onDismissRequest = {
                        complaintStatusClicked = false
                    }) {
                    ComplaintStatus.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(text = it.name, color = Color.Black)
                            },
                            onClick = {
                                complaintStatus = it
                                complaintStatusClicked = false
                            },
                        )
                    }
                }
            }

            // charge
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "Charge",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                TextField(
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                        .fillMaxWidth(),
                    value = charge,
                    onValueChange = {
                        charge = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Button(
                modifier = Modifier
                    .padding(
                        top = 30.dp,
                        start = 30.dp,
                        end = 30.dp,
                        bottom = if (idNotEmpty) 0.dp else 100.dp
                    )
                    .width(270.dp),
                onClick = {
                    addComplaintClicked = true
                },
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = "${if (idNotEmpty) "Update" else "Add"} Complaint",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

            }

            if (idNotEmpty) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 100.dp, top = 20.dp)
                        .clickable {
                            deleteClicked = true
                        },
                    text = "Delete Complaint",
                    color = BrightRed,
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.Underline
                )
            }

        }
    }

    if (deleteClicked) {
        AlertDialog(
            onDismissRequest = {
                deleteClicked = false
            },
            confirmButton = {
                Button(onClick = {
                    sharedViewModel.id.value = id
                    sharedViewModel.deleteCompliant.value = true
                    navController.popBackStack()
                    deleteClicked = false
                }) {
                    Text(text = "Confirm")

                }
            },
            dismissButton = {
                Button(onClick = { deleteClicked = false }) {
                    Text(text = "Cancel")
                }
            },
            title = {
                Text(text = "Delete the Complaint!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }, text = {
                Text(text = "Are you sure to delete the complaint? Once deleted the action can not be reversed, resulting permanent deletion of the complaint. \nTo delete click confirm")
            }
        )
    }

    if (addComplaintClicked) {
        LaunchedEffect(key1 = Unit) {
            val newRef = if (idNotEmpty) databaseReference.child(id) else databaseReference.push()

            // on below line we are adding data.
            val complaintDataEdited = ComplaintData(
                name = name,
                date = date,
                item = item,
                problem = problem,
                charge = charge,
                photo = photo,
                status = complaintStatus.name,
                complaintId = newRef.key ?: "error while adding complaint!"
            )

            newRef.setValue(complaintDataEdited)
            Log.d("error-firebase-database", "onCancelled:")
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Data has changed
                    Toast.makeText(
                        context,
                        "Complaint ${if (idNotEmpty) "Updated" else "Created"}!",
                        Toast.LENGTH_SHORT
                    ).show()

                    name = ""
                    date = ""
                    item = ""
                    problem = ""
                    charge = ""
                    photo = ""
                    complaintStatus = ComplaintStatus.NOTHING
                    sharedViewModel.id.value = ""

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

            addComplaintClicked = false
        }
    }

}