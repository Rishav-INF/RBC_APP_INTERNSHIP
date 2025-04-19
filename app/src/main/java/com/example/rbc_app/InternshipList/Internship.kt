package com.example.rbc_app.JobList

import android.media.Image

data class Internship(
    val jobName : String,
    val jobDesc : String,
    val companyName : String,
    val companyLogo : ByteArray,
    val jobType : String,
    val jobLocation : String,
    val preferredCandType : String,
    val jobStatus : String
)