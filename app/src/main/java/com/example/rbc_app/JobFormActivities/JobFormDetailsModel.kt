package com.example.rbc_app.JobFormActivities

import com.example.rbc_app.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UserAddFormDetails(
    var job_id:Int,
    var emp_id:Int,
    var user_id:Int,
    var field_details :String,
    @Serializable(with = LocalDateTimeSerializer::class)
    var submitted_at: LocalDateTime = LocalDateTime.now()
)