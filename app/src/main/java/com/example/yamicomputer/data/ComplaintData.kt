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

fun String.stringToComplaintStatus(): ComplaintStatus {
    return when (this) {
        ComplaintStatus.ONGOING.name -> ComplaintStatus.ONGOING
        ComplaintStatus.COMPLETE.name -> ComplaintStatus.COMPLETE
        ComplaintStatus.PENDING.name -> ComplaintStatus.PENDING
        ComplaintStatus.NOTHING.name -> ComplaintStatus.NOTHING
        else -> {ComplaintStatus.NOTHING}
    }
}
