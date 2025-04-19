package com.example.rbc_app.InternJobFreelanceAdd

import KtorClient
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class AddInternship:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddInternshipScreen()
        }
    }
}


@Composable
fun AddInternshipScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var internshipName by remember { mutableStateOf("") }
    var internshipType by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var companyLogo by remember { mutableStateOf("") }
    var internshipLocation by remember { mutableStateOf("") }
    var preferredCandType by remember { mutableStateOf("") }
    var internshipStatus by remember { mutableStateOf("Open") } // Default to "Open"
    var internshipPhoto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Add Internship Opportunity",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Internship Name Field
        OutlinedTextField(
            value = internshipName,
            onValueChange = { internshipName = it },
            label = { Text("Internship Name*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Designer bhai") }
        )

        // Internship Type Field
        OutlinedTextField(
            value = internshipType,
            onValueChange = { internshipType = it },
            label = { Text("Internship Type*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Designing posters") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Company Details Section
        Text(
            text = "Company Details",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Company Name
        OutlinedTextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Company Name*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., WebVerse") }
        )

        // Company Logo
        OutlinedTextField(
            value = companyLogo,
            onValueChange = { companyLogo = it },
            label = { Text("Company Logo filename") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., webverse_logo.png") }
        )

        // Internship Location
        OutlinedTextField(
            value = internshipLocation,
            onValueChange = { internshipLocation = it },
            label = { Text("Location*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Bengaluru") }
        )

        // Preferred Candidate Type
        OutlinedTextField(
            value = preferredCandType,
            onValueChange = { preferredCandType = it },
            label = { Text("Preferred Candidate Type*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Fresher") }
        )

        // Internship Status (Dropdown)
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = internshipStatus,
                onValueChange = {},
                label = { Text("Internship Status") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Open", "Closed", "Coming Soon").forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status) },
                        onClick = {
                            internshipStatus = status
                            expanded = false
                        }
                    )
                }
            }
            // Make the whole field clickable
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable { expanded = true }
            )
        }

        // Internship Photo
        OutlinedTextField(
            value = internshipPhoto,
            onValueChange = { internshipPhoto = it },
            label = { Text("Internship Photo filename") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., hello.png") }
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Contact Email*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., example@gmail.com") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                // Combine details into the description format
                val internshipDesc = buildString {
                    append("companyName: $companyName, ")
                    append("companyLogo: $companyLogo, ")
                    append("internshipLocation: $internshipLocation, ")
                    append("preferredCandType: $preferredCandType, ")
                    append("internshipStatus: $internshipStatus")
                }

                // Create internship data object
                val internshipData = KtorClient.addInternship(
                    internship_name = internshipName,
                    internship_type = internshipType,
                    internship_desc = internshipDesc,
                    internship_photo = internshipPhoto,
                    email = email
                )

                // Send to server
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        val response = KtorClient.sendInternRowToAdd(internshipData)
                        if (response) {
                            Toast.makeText(context, "Internship Added", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            enabled = internshipName.isNotBlank() &&
                    internshipType.isNotBlank() &&
                    companyName.isNotBlank() &&
                    internshipLocation.isNotBlank() &&
                    preferredCandType.isNotBlank() &&
                    email.isNotBlank(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Add Internship", style = MaterialTheme.typography.labelLarge)
        }
    }
}

// Data class for the internship
data class InternshipData(
    val internship_name: String,
    val internship_type: String,
    val internship_desc: String,
    val internship_photo: String,
    val email: String
)