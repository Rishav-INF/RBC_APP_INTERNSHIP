package com.example.rbc_app.InternJobFreelanceAdd

import KtorClient
import android.os.Bundle
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

class AddJobActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddJobScreen()
        }
    }
}

@Composable
fun AddJobScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var jobName by remember { mutableStateOf("") }
    var jobType by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var companyLogo by remember { mutableStateOf("") }
    var jobLocation by remember { mutableStateOf("") }
    var preferredCandType by remember { mutableStateOf("") }
    var jobStatus by remember { mutableStateOf("Open") }
    var salaryRange by remember { mutableStateOf("") }
    var experienceRequired by remember { mutableStateOf("") }
    var skillsRequired by remember { mutableStateOf("") }
    var perksOffered by remember { mutableStateOf("") }
    var jobPhoto by remember { mutableStateOf("") }
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
            text = "Add Job Opportunity",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Job Name Field
        OutlinedTextField(
            value = jobName,
            onValueChange = { jobName = it },
            label = { Text("Job Title*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., UI/UX Designer") }
        )

        // Job Type Field
        var jobTypeExpanded by remember { mutableStateOf(false) }
        val jobTypes = listOf("Onsite", "Remote", "Hybrid")
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = jobType,
                onValueChange = {},
                label = { Text("Job Type*") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            DropdownMenu(
                expanded = jobTypeExpanded,
                onDismissRequest = { jobTypeExpanded = false }
            ) {
                jobTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            jobType = type
                            jobTypeExpanded = false
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable { jobTypeExpanded = true }
            )
        }

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
            placeholder = { Text("e.g., DesignHive") }
        )

        // Company Logo
        OutlinedTextField(
            value = companyLogo,
            onValueChange = { companyLogo = it },
            label = { Text("Company Logo URL*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., https://example.com/images/designhive_logo.png") }
        )

        // Job Location
        OutlinedTextField(
            value = jobLocation,
            onValueChange = { jobLocation = it },
            label = { Text("Job Location*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Pune") }
        )

        // Preferred Candidate Type
        OutlinedTextField(
            value = preferredCandType,
            onValueChange = { preferredCandType = it },
            label = { Text("Preferred Candidate Type*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Fresher") }
        )

        // Job Status
        var jobStatusExpanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = jobStatus,
                onValueChange = {},
                label = { Text("Job Status*") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            DropdownMenu(
                expanded = jobStatusExpanded,
                onDismissRequest = { jobStatusExpanded = false }
            ) {
                listOf("Open", "Closed", "Coming Soon").forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status) },
                        onClick = {
                            jobStatus = status
                            jobStatusExpanded = false
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable { jobStatusExpanded = true }
            )
        }

        // Salary Range
        OutlinedTextField(
            value = salaryRange,
            onValueChange = { salaryRange = it },
            label = { Text("Salary Range*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., ₹4–6 LPA") }
        )

        // Experience Required
        OutlinedTextField(
            value = experienceRequired,
            onValueChange = { experienceRequired = it },
            label = { Text("Experience Required*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., 0–1 year") }
        )

        // Skills Required
        OutlinedTextField(
            value = skillsRequired,
            onValueChange = { skillsRequired = it },
            label = { Text("Skills Required*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Figma, Adobe XD, HTML/CSS") }
        )

        // Perks Offered
        OutlinedTextField(
            value = perksOffered,
            onValueChange = { perksOffered = it },
            label = { Text("Perks Offered") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Gym Membership, Free Snacks") }
        )

        // Job Photo URL
        OutlinedTextField(
            value = jobPhoto,
            onValueChange = { jobPhoto = it },
            label = { Text("Job Photo URL*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., https://example.com/images/job_image.png") }
        )

        // Contact Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Contact Email*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., design@designhive.co") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                // Build job description string exactly matching the SQL format
                val jobDesc = buildString {
                    append("companyName: $companyName, ")
                    append("companyLogo: $companyLogo, ")
                    append("jobLocation: $jobLocation, ")
                    append("preferredCandType: $preferredCandType, ")
                    append("jobStatus: $jobStatus, ")
                    append("salary: $salaryRange, ")
                    append("experience: $experienceRequired, ")
                    append("skills: $skillsRequired")
                    if (perksOffered.isNotBlank()) {
                        append(", perks: $perksOffered")
                    }
                }

                // Create job data object
                val jobData = KtorClient.addJob(
                    job_name = jobName,
                    job_type = jobType,
                    job_desc = jobDesc,
                    job_photo = jobPhoto,
                    email = email
                )

                // Send to server
                CoroutineScope(Dispatchers.IO).launch{
                    withContext(Dispatchers.Main) {
                        val response = KtorClient.sendJobRowToAdd(jobData)
                        if (response) {
                            Toast.makeText(context, "Job Posted Successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to Post Job", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            enabled = jobName.isNotBlank() &&
                    jobType.isNotBlank() &&
                    companyName.isNotBlank() &&
                    companyLogo.isNotBlank() &&
                    jobLocation.isNotBlank() &&
                    preferredCandType.isNotBlank() &&
                    jobStatus.isNotBlank() &&
                    salaryRange.isNotBlank() &&
                    experienceRequired.isNotBlank() &&
                    skillsRequired.isNotBlank() &&
                    jobPhoto.isNotBlank() &&
                    email.isNotBlank(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Post Job Opportunity", style = MaterialTheme.typography.labelLarge)
        }
    }
}

// Data class that exactly matches the database row structure
data class JobData(
    val job_name: String,
    val job_type: String,
    val job_desc: String,
    val job_photo: String,
    val email: String
)