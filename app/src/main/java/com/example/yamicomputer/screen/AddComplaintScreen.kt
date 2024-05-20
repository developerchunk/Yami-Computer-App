package com.example.yamicomputer.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.yamicomputer.data.ComplaintData
import com.example.yamicomputer.data.ComplaintStatus
import com.example.yamicomputer.data.DealerNames
import com.example.yamicomputer.data.ProfileActions
import com.example.yamicomputer.data.stringToComplaintStatus
import com.example.yamicomputer.data.stringToDealerName
import com.example.yamicomputer.logic.SharedViewModel
import com.example.yamicomputer.ui.theme.BrightRed
import com.example.yamicomputer.ui.theme.UIBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddComplaintScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {

    val context = LocalContext.current

    val smsPermissionState = rememberPermissionState(
        permission = Manifest.permission.SEND_SMS
    )

    var smsPermissionClicked by remember {
        mutableStateOf(false)
    }

    //define permission in composable fun
    val getPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
//        if (isGranted) {
//            //permission accepted do something
//            Toast.makeText(context, "Permission Granted!", Toast.LENGTH_SHORT).show()
//        } else {
//            //permission not accepted show message
//            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
//        }
    }

    SideEffect {
        getPermission.launch(Manifest.permission.SEND_SMS)
    }

//    val permissionResult = (
//        permissionState = smsPermissionState
//    )
    LaunchedEffect(smsPermissionState) {
        getPermission.launch(Manifest.permission.SEND_SMS)
    }

    val id by sharedViewModel.id
    val profileAction by sharedViewModel.profileAction

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

    var mobileNo by rememberSaveable {
        mutableStateOf(complaintData.mobileNo)
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

    var collectorName by rememberSaveable {
        mutableStateOf(complaintData.collectorName)
    }

    var collectionDate by rememberSaveable {
        mutableStateOf(complaintData.collectionDate)
    }

    var dealerName by rememberSaveable {
        mutableStateOf(complaintData.dealerName.stringToDealerName())
    }

    var dealerNameClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var addComplaintClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var deleteClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }


    var showDatePicker by remember {
        mutableStateOf(false)
    }


    // on below line creating variable for freebase database
    // and database reference.
//    val firebaseDatabase = FirebaseDatabase.database
    val database = Firebase.database
    val databaseReference = database.getReference("customer-complaints")

    var imageUri by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(key1 = Unit) {
        imageUri = getImageUrlFromFirebaseStorage("images/$photo")
    }

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

            if (!smsPermissionState.status.isGranted) {
                Button(onClick = {
                    smsPermissionClicked = true
                }, colors = ButtonDefaults.buttonColors(UIBlue)) {
                    Text(text = "Request SMS Permission")
                }
            }

            if (!idNotEmpty) {
                ImagePickerScreen(selectedImageUriListener = {
                    selectedImageUri = it
                })
            } else {

                imageUri?.let { uri ->
                    val painter = rememberAsyncImagePainter(uri)
                    Image(
                        painter = painter,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                    )
                }
            }

            // Name
            RegularTextField(
                textFieldName = "Name",
                value = name,
                onTextChange = {
                    name = it
                }
            )

            // Mobile Number
            RegularTextField(
                textFieldName = "Mobile Number",
                value = mobileNo,
                onTextChange = {
                    mobileNo = it
                },
                keyboardType = KeyboardType.Number
            )

            // date
            RegularTextField(
                textFieldName = "Date",
                value = date,
                onTextChange = {
                    date = it
                },
                enable = false,
                onClick = {
                    showDatePicker = true
                }
            )

            if (showDatePicker) {
                MyDatePickerDialog(
                    onDateSelected = { date = it },
                    onDismiss = { showDatePicker = false }
                )
            }


            // item
            RegularTextField(
                textFieldName = "Item",
                value = item,
                onTextChange = {
                    item = it
                }
            )

            // problem
            RegularTextField(
                textFieldName = "Problem",
                value = problem,
                onTextChange = {
                    problem = it
                }
            )

            if (idNotEmpty) {
                // complaint Status
                RegularTextField(
                    textFieldName = "Complaint Status",
                    value = complaintStatus.name,
                    onTextChange = {

                    },
                    enable = false,
                    onClick = {
                        complaintStatusClicked = !complaintStatusClicked
                    }
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {

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
                RegularTextField(
                    textFieldName = "Charge",
                    value = charge,
                    onTextChange = {
                        charge = it
                    }
                )

                // if this screen is used as outwards screen
                if (profileAction == ProfileActions.OUTWARD_COMPLAINT) {
                    // collector name
                    RegularTextField(
                        textFieldName = "Collector Name",
                        value = collectorName,
                        onTextChange = {
                            collectorName = it
                        }
                    )

                    // collection date
                    RegularTextField(
                        textFieldName = "Collection Date",
                        value = collectionDate,
                        onTextChange = {
                            collectionDate = it
                        }
                    )
                }
            }

            // Dealer Name
            RegularTextField(
                textFieldName = "Dealer Name",
                value = dealerName.name,
                onTextChange = {

                },
                enable = false,
                onClick = {
                    dealerNameClicked = !dealerNameClicked
                }
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    expanded = dealerNameClicked,
                    onDismissRequest = {
                        dealerNameClicked = false
                    }) {
                    DealerNames.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(text = it.name, color = Color.Black)
                            },
                            onClick = {
                                dealerName = it
                                dealerNameClicked = false
                            },
                        )
                    }
                }
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
                colors = ButtonDefaults.buttonColors(UIBlue),
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

    if (addComplaintClicked && mobileNo.length == 10) {
        LaunchedEffect(key1 = Unit) {
            if (smsPermissionState.status.isGranted) {

                val newRef = if (idNotEmpty) databaseReference.child(id) else databaseReference.push()

                selectedImageUri?.let {
                    uploadPhotoToFirebase(
                        context = context,
                        imageUri = it,
                        onImageUploadSuccessListener = { photoPath ->

                            // on below line we are adding data.
                            val complaintDataEdited = ComplaintData(
                                name = name,
                                date = date,
                                item = item,
                                problem = problem,
                                charge = charge,
                                photo = photoPath,
                                status = complaintStatus.name,
                                complaintId = newRef.key ?: "error while adding complaint!",
                                mobileNo = mobileNo,
                                collectorName = collectorName,
                                collectionDate = collectionDate,
                                dealerName = dealerName.name
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

                                    // on below line initializing sms manager.
                                    val smsManager: SmsManager =
                                        context.getSystemService(SmsManager::class.java)

                                    // sending the message
                                    if (mobileNo.isNotEmpty()) {
                                        smsManager.sendTextMessage(
                                            mobileNo,
                                            null,
                                            "Hello ${complaintDataEdited.name}, Welcome to YAMI Computers\nYour item: ${complaintDataEdited.item} which had the problem: ${complaintDataEdited.problem} is now in the ${complaintDataEdited.status} stage, we will update you if any changes happens in future",
                                            null,
                                            null
                                        )
                                    }

                                    if (!idNotEmpty) {
                                        name = ""
                                        date = ""
                                        item = ""
                                        problem = ""
                                        charge = ""
                                        photo = ""
                                    }

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

                        })
                }

                if (selectedImageUri == null) {
                    // on below line we are adding data.
                    val complaintDataEdited = ComplaintData(
                        name = name,
                        date = date,
                        item = item,
                        problem = problem,
                        charge = charge,
                        photo = photo,
                        status = complaintStatus.name,
                        complaintId = newRef.key ?: "error while adding complaint!",
                        mobileNo = mobileNo,
                        collectorName = collectorName,
                        collectionDate = collectionDate,
                        dealerName = dealerName.name
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

                            // on below line initializing sms manager.
                            val smsManager: SmsManager =
                                context.getSystemService(SmsManager::class.java)

                            // sending the message
                            if (mobileNo.isNotEmpty()) {
                                smsManager.sendTextMessage(
                                    mobileNo,
                                    null,
                                    "Hello ${complaintDataEdited.name}, Welcome to YAMI Computers\nYour item: ${complaintDataEdited.item} which had the problem: ${complaintDataEdited.problem} is now in the ${complaintDataEdited.status} stage, we will update you if any changes happens in future",
                                    null,
                                    null
                                )
                            }

                            if (!idNotEmpty) {
                                name = ""
                                date = ""
                                item = ""
                                problem = ""
                                charge = ""
                                photo = ""
                                selectedImageUri = null
                                mobileNo = ""
                            }

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
            } else {
                Toast.makeText(context, "SMS Permission not granted!", Toast.LENGTH_LONG).show()
            }
        }
    } else if (addComplaintClicked) {
        Toast.makeText(context, "Mobile Number Not Valid!", Toast.LENGTH_LONG).show()
    }

}

@Composable
fun RegularTextField(
    textFieldName: String,
    value: String,
    onTextChange: (String) -> Unit,
    enable: Boolean = true,
    onClick: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clickEnableOnCondition(condition = !enable, clickable = {
                onClick()
            }),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = textFieldName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        TextField(
            modifier = Modifier
                .background(shape = RoundedCornerShape(20.dp), color = Color.Gray)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp)),
            value = value,
            onValueChange = {
                onTextChange(it)
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
            enabled = enable,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),

            )
    }
}

fun Modifier.clickEnableOnCondition(
    condition: Boolean,
    clickable: () -> Unit
): Modifier {

    if (condition) {
        return this.clickable {
            clickable()
        }
    }

    return this

}

@Composable
fun ImagePickerScreen(
    selectedImageUriListener: (Uri) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Activity result launcher for picking images
    val pickImage =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = uri
            }
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            // Launch image picker
            pickImage.launch("image/*")
        }, colors = ButtonDefaults.buttonColors(UIBlue)) {
            Text("Select Image")
        }

        selectedImageUri?.let { uri ->
            val painter = rememberAsyncImagePainter(uri)
            Image(
                painter = painter,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
            selectedImageUriListener(uri)
        }
    }
}

fun uploadPhotoToFirebase(
    context: Context,
    imageUri: Uri,
    onImageUploadSuccessListener: (String) -> Unit
) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val imageName = "${System.currentTimeMillis()}_${imageUri.lastPathSegment}"
    val imageRef = storageRef.child("images/$imageName")

    imageRef.putFile(imageUri)
        .addOnSuccessListener { _ ->
            // Image uploaded successfully
            onImageUploadSuccessListener(imageName)
            Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { exception ->
            // Handle unsuccessful uploads
            Toast.makeText(
                context,
                "Failed to upload image: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
}

suspend fun getImageUrlFromFirebaseStorage(imagePath: String): String? {
    return try {
        val storageRef = FirebaseStorage.getInstance().reference.child(imagePath)
        storageRef.downloadUrl.await().toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}