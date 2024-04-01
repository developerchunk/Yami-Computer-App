package com.example.yamicomputer.viewmodel

import androidx.annotation.Keep
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.yamicomputer.data.ComplaintData
import com.example.yamicomputer.data.ComplaintStatus
import com.example.yamicomputer.data.ProfileData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.delay

@Keep
class SharedViewModel : ViewModel() {

    var id: MutableState<String> = mutableStateOf("")
    var complaintData: MutableState<ComplaintData> = mutableStateOf(ComplaintData())
    var compliantStatus: MutableState<ComplaintStatus> = mutableStateOf(ComplaintStatus.NOTHING)

    var deleteCompliant: MutableState<Boolean> = mutableStateOf(false)

    // for firebase auth and callback
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database
    private val profileReference = database.getReference("customer-profile")

    fun isUserLoggedIn(): Boolean {
        return mAuth.currentUser != null
    }

    private suspend fun isProfileCreated(): ProfileData {

        val dataSet: MutableState<ProfileData> = mutableStateOf(ProfileData())

        try {
            val userId: FirebaseUser = mAuth.currentUser!!
            profileReference.child(userId.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get the data as a User object
                    dataSet.value = dataSnapshot.getValue<ProfileData>()!!
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })


        } catch (_: Exception) {

        }

        delay(1000)
        return dataSet.value
    }

    suspend fun isProfileComplete(): Boolean {

        val validation: MutableState<Boolean> = mutableStateOf(isUserLoggedIn() && isProfileCreated().name.isNotEmpty())
        
        return validation.value
    }



}