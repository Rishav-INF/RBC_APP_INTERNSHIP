package com.example.rbc_app.JobFormActivities

import kotlinx.serialization.Serializable

@Serializable
class JobFieldDefinition (
        var label:String,
        var label_text:String,
        var type:String,
        var isRequired:Boolean
    )
