package com.example.yamicomputer.data

import androidx.annotation.Keep

@Keep
data class ComplaintData(
    var name: String = "",
    var date: String = "",
    var item: String = "",
    var problem: String = "",
    var charge: String = "",
    var photo: String = "",
    var status: String = "",
    var complaintId: String = "",
)

enum class ComplaintStatus {
    ONGOING,
    COMPLETE,
    PENDING,
    NOTHING
}
