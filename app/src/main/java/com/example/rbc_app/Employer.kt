package com.example.rbc_app

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Employer(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String,
    val gender: String,
    val companyName: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now()
)