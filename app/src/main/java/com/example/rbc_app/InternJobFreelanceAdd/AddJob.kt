package com.example.rbc_app.InternJobFreelanceAdd

import KtorClient
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rbc_app.JobFormActivities.JobFieldDefinition
import com.example.rbc_app.RoomDatabase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Professional color scheme
object JobFormColors {
    val PrimaryYellow = Color(0xFFFFC107)
    val LightYellow = Color(0xFFFFF8E1)
    val AccentYellow = Color(0xFFFFD54F)
    val DarkYellow = Color(0xFFFF8F00)
    val PureWhite = Color.White
    val LightGray = Color(0xFFF8F9FA)
    val TextDark = Color(0xFF2C3E50)
    val TextSecondary = Color(0xFF5D6D7E)
}

class AddJobActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(JobFormColors.LightYellow, JobFormColors.PureWhite)
                        )
                    )
            ) {
                AddJobScreen()
            }
        }
    }
}

@Composable
fun AddJobScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Form state variables
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
    var customFields by remember { mutableStateOf<List<JobFieldDefinition>>(emptyList()) }
    var showAddFieldDialog by remember { mutableStateOf(false) }
    var jobId by remember { mutableStateOf("") }
    var isJobAdded by remember { mutableStateOf(false) }

    // Field dialog state
    var newFieldLabel by remember { mutableStateOf("") }
    var newFieldLabelText by remember { mutableStateOf("") }
    var newFieldType by remember { mutableStateOf("Text") }
    var newFieldIsRequired by remember { mutableStateOf(false) }
    var newFieldTypeExpanded by remember { mutableStateOf(false) }

    // Dropdown states
    var jobTypeExpanded by remember { mutableStateOf(false) }
    var jobStatusExpanded by remember { mutableStateOf(false) }
    val jobTypes = listOf("Onsite", "Remote", "Hybrid")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = JobFormColors.PureWhite),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Create Job Opportunity",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = JobFormColors.TextDark
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Complete 2 simple steps to post your job",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = JobFormColors.TextSecondary
                    )
                )
            }
        }

        if (!isJobAdded) {
            // STEP 1: Basic Information
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = JobFormColors.PureWhite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Step Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    JobFormColors.PrimaryYellow,
                                    RoundedCornerShape(50)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "STEP 1",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Basic Information",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = JobFormColors.TextDark
                            )
                        )
                    }

                    // Job Title
                    OutlinedTextField(
                        value = jobName,
                        onValueChange = { jobName = it },
                        label = { Text("Job Title*", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., UI/UX Designer", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Job Type Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = jobType,
                            onValueChange = {},
                            label = { Text("Job Type*", color = JobFormColors.TextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = JobFormColors.PrimaryYellow)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = JobFormColors.PrimaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        DropdownMenu(
                            expanded = jobTypeExpanded,
                            onDismissRequest = { jobTypeExpanded = false },
                            modifier = Modifier.background(JobFormColors.PureWhite, RoundedCornerShape(8.dp))
                        ) {
                            jobTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type, color = JobFormColors.TextDark) },
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

                    // Company Details Section
                    Text(
                        text = "Company Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = JobFormColors.TextDark
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Company Name
                    OutlinedTextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = { Text("Company Name*", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., DesignHive", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Company Logo
                    OutlinedTextField(
                        value = companyLogo,
                        onValueChange = { companyLogo = it },
                        label = { Text("Company Logo URL*", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., https://example.com/images/designhive_logo.png", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Job Location
                    OutlinedTextField(
                        value = jobLocation,
                        onValueChange = { jobLocation = it },
                        label = { Text("Job Location*", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Pune", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Preferred Candidate Type
                    OutlinedTextField(
                        value = preferredCandType,
                        onValueChange = { preferredCandType = it },
                        label = { Text("Preferred Candidate Type*", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Fresher", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Job Status Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = jobStatus,
                            onValueChange = {},
                            label = { Text("Job Status*", color = JobFormColors.TextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = JobFormColors.PrimaryYellow)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = JobFormColors.PrimaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        DropdownMenu(
                            expanded = jobStatusExpanded,
                            onDismissRequest = { jobStatusExpanded = false },
                            modifier = Modifier.background(JobFormColors.PureWhite, RoundedCornerShape(8.dp))
                        ) {
                            listOf("Open", "Closed", "Coming Soon").forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status, color = JobFormColors.TextDark) },
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
                        label = { Text("Salary Range*", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., ₹4–6 LPA", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Experience Required
                    OutlinedTextField(
                        value = experienceRequired,
                        onValueChange = { experienceRequired = it },
                        label = { Text("Experience Required*", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., 0–1 year", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Skills Required
                    OutlinedTextField(
                        value = skillsRequired,
                        onValueChange = { skillsRequired = it },
                        label = { Text("Skills Required*", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Figma, Adobe XD, HTML/CSS", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Perks Offered
                    OutlinedTextField(
                        value = perksOffered,
                        onValueChange = { perksOffered = it },
                        label = { Text("Perks Offered", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Gym Membership, Free Snacks", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Job Photo URL
                    OutlinedTextField(
                        value = jobPhoto,
                        onValueChange = { jobPhoto = it },
                        label = { Text("Job Photo URL*", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., https://example.com/images/job_image.png", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Contact Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Contact Email*", color = JobFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., design@designhive.co", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JobFormColors.PrimaryYellow,
                            focusedLabelColor = JobFormColors.PrimaryYellow,
                            cursorColor = JobFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Submit Button
                    Button(
                        onClick = {
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

                            val jobData = KtorClient.addJob(
                                job_name = jobName,
                                job_type = jobType,
                                job_desc = jobDesc,
                                job_photo = jobPhoto,
                                email = email
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                withContext(Dispatchers.Main) {
                                    val response = KtorClient.sendJobRowToAdd(jobData)
                                    if (response != -1) {
                                        Toast.makeText(context, "Job Posted Successfully", Toast.LENGTH_SHORT).show()
                                        jobId = response.toString()
                                        isJobAdded = true
                                    } else {
                                        Toast.makeText(context, "Failed to Post Job", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(4.dp, RoundedCornerShape(14.dp)),
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
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = JobFormColors.PrimaryYellow,
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFE0E0E0)
                        )
                    ) {
                        Text(
                            text = "Complete Step 1",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

            // Information Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = JobFormColors.LightYellow),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "💡 After posting you will have the option to add your custom application form for the job",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = JobFormColors.TextDark,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(20.dp)
                )
            }
        } else {
            // STEP 2: Custom Application Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = JobFormColors.PureWhite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Step 2 Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    JobFormColors.AccentYellow,
                                    RoundedCornerShape(50)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "STEP 2",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Custom Application Form",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = JobFormColors.TextDark
                            )
                        )
                    }

                    // Button to add new field
                    Button(
                        onClick = { showAddFieldDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = JobFormColors.AccentYellow,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Add New Field",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    // Display added fields
                    if (customFields.isNotEmpty()) {
                        customFields.forEach { field ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = JobFormColors.LightGray),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = field.label,
                                        fontWeight = FontWeight.SemiBold,
                                        color = JobFormColors.TextDark
                                    )
                                    Text(
                                        text = "Type: ${field.type} • ${if (field.isRequired) "Required" else "Optional"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = JobFormColors.TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Final Submit Button
            if (customFields.isNotEmpty()) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val res = KtorClient.formTemplate(
                                job_id = jobId.toInt(),
                                emp_id = AppDatabase.getInstance(context).UserDao().getUid(),
                                fields = customFields
                            )
                            KtorClient.addJobForm(res)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(4.dp, RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = JobFormColors.DarkYellow,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "🚀 POST THE JOB",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }

        // Dialog for adding new field
        if (showAddFieldDialog) {
            AlertDialog(
                onDismissRequest = { showAddFieldDialog = false },
                title = {
                    Text(
                        "Add New Field",
                        fontWeight = FontWeight.Bold,
                        color = JobFormColors.TextDark
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = newFieldLabel,
                            onValueChange = { newFieldLabel = it },
                            label = { Text("Field Label*", color = JobFormColors.TextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g., Portfolio Link", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = JobFormColors.PrimaryYellow,
                                focusedLabelColor = JobFormColors.PrimaryYellow,
                                cursorColor = JobFormColors.PrimaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = newFieldLabelText,
                            onValueChange = { newFieldLabelText = it },
                            label = { Text("Label Text*", color = JobFormColors.TextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g., Please provide your portfolio URL", color = JobFormColors.TextSecondary.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = JobFormColors.PrimaryYellow,
                                focusedLabelColor = JobFormColors.PrimaryYellow,
                                cursorColor = JobFormColors.PrimaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Field Type Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = newFieldType,
                                onValueChange = {},
                                label = { Text("Field Type*", color = JobFormColors.TextSecondary) },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = JobFormColors.PrimaryYellow)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = JobFormColors.PrimaryYellow,
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            DropdownMenu(
                                expanded = newFieldTypeExpanded,
                                onDismissRequest = { newFieldTypeExpanded = false },
                                modifier = Modifier.background(JobFormColors.PureWhite, RoundedCornerShape(8.dp))
                            ) {
                                listOf("Text", "Number", "Email", "Date", "File", "Checkbox").forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type, color = JobFormColors.TextDark) },
                                        onClick = {
                                            newFieldType = type
                                            newFieldTypeExpanded = false
                                        }
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .alpha(0f)
                                    .clickable { newFieldTypeExpanded = true }
                            )
                        }

                        // Is Required Checkbox
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { newFieldIsRequired = !newFieldIsRequired }
                                .padding(vertical = 8.dp)
                        ) {
                            Checkbox(
                                checked = newFieldIsRequired,
                                onCheckedChange = { newFieldIsRequired = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = JobFormColors.PrimaryYellow,
                                    uncheckedColor = Color(0xFFE0E0E0)
                                )
                            )
                            Text(
                                text = "Required Field",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = JobFormColors.TextDark,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newFieldLabel.isNotBlank() && newFieldLabelText.isNotBlank()) {
                                customFields = customFields + JobFieldDefinition(
                                    label = newFieldLabel,
                                    label_text = newFieldLabelText,
                                    type = newFieldType,
                                    isRequired = newFieldIsRequired
                                )
                                newFieldLabel = ""
                                newFieldLabelText = ""
                                newFieldType = "Text"
                                newFieldIsRequired = false
                                showAddFieldDialog = false
                            } else {
                                Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = JobFormColors.PrimaryYellow,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Add Field",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showAddFieldDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE0E0E0),
                            contentColor = JobFormColors.TextDark
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Cancel",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                },
                containerColor = JobFormColors.PureWhite,
                shape = RoundedCornerShape(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddJobScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(JobFormColors.LightYellow, JobFormColors.PureWhite)
                )
            )
    ) {
        AddJobScreen()
    }
}