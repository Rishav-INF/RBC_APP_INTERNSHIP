package com.example.rbc_app

import kotlinx.serialization.Serializable

@Serializable
data class OtpModel(val email:String,val password:String,val otp:String)
