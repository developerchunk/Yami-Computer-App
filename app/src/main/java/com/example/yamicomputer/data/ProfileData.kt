package com.example.yamicomputer.data

import androidx.annotation.Keep

@Keep
data class ProfileData(
    var name: String = "",
    var address: String = "",
    var city: String = ""
)
