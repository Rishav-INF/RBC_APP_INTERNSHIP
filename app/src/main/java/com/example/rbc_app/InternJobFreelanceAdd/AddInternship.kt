package com.example.rbc_app.InternJobFreelanceAdd

import KtorClient
import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rbc_app.JobFormActivities.InternshipFieldDefinition
import com.example.rbc_app.JobFormActivities.JobFieldDefinition
import com.example.rbc_app.JobList.Internship
import com.example.rbc_app.RoomDatabase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddInternship: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddInternshipScreen()
        }
    }
}

@Composable
fun AddInternshipScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    var internshipName by remember { mutableStateOf("") }
    var internshipType by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var companyLogo by remember { mutableStateOf("") }
    var internshipLocation by remember { mutableStateOf("") }
    var preferredCandType by remember { mutableStateOf("") }
    var internshipStatus by remember { mutableStateOf("Open") }
    var duration by remember { mutableStateOf("") }
    var stipend by remember { mutableStateOf("") }
    var skillsRequired by remember { mutableStateOf("") }
    var perksOffered by remember { mutableStateOf("") }
    var internshipPhoto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var customFields by remember { mutableStateOf<List<InternshipFieldDefinition>>(emptyList()) }
    var showAddFieldDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var internshipId by remember { mutableStateOf("") }
    var empId by remember { mutableStateOf("") }
    var isInternshipAdded by remember { mutableStateOf(false) }

    // States for the new field dialog
    var newFieldLabel by remember { mutableStateOf("") }
    var newFieldLabelText by remember { mutableStateOf("") }
    var newFieldType by remember { mutableStateOf("Text") }
    var newFieldIsRequired by remember { mutableStateOf(false) }
    var newFieldTypeExpanded by remember { mutableStateOf(false) }

    // Professional color scheme
    val primaryYellow = Color(0xFFFFC107)
    val lightYellow = Color(0xFFFFF8E1)
    val accentYellow = Color(0xFFFFD54F)
    val darkYellow = Color(0xFFFF8F00)
    val pureWhite = Color.White
    val lightGray = Color(0xFFF8F9FA)
    val textDark = Color(0xFF2C3E50)
    val textSecondary = Color(0xFF5D6D7E)

    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(lightYellow, pureWhite)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = pureWhite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Create Internship Opportunity",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Complete 2 simple steps to post your internship",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = textSecondary
                        )
                    )
                }
            }

            // Step 1 Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = pureWhite),
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
                                    primaryYellow,
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
                                color = textDark
                            )
                        )
                    }

                    // Internship Name Field
                    OutlinedTextField(
                        value = internshipName,
                        onValueChange = { internshipName = it },
                        label = { Text("Internship Title*", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., UI/UX Design Intern", color = textSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Internship Type Field
                    var internshipTypeExpanded by remember { mutableStateOf(false) }
                    val internshipTypes = listOf("Onsite", "Remote", "Hybrid")
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = internshipType,
                            onValueChange = {},
                            label = { Text("Internship Type*", color = textSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = primaryYellow)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        DropdownMenu(
                            expanded = internshipTypeExpanded,
                            onDismissRequest = { internshipTypeExpanded = false },
                            modifier = Modifier.background(pureWhite, RoundedCornerShape(8.dp))
                        ) {
                            internshipTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type, color = textDark) },
                                    onClick = {
                                        internshipType = type
                                        internshipTypeExpanded = false
                                    }
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .alpha(0f)
                                .clickable { internshipTypeExpanded = true }
                        )
                    }
                }
            }

            // Company Details Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = pureWhite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Company Details",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = textDark
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Company Name
                    OutlinedTextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = { Text("Company Name*", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., DesignHive", color = textSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Company Logo
                    OutlinedTextField(
                        value = companyLogo,
                        onValueChange = { companyLogo = it },
                        label = { Text("Company Logo URL*", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., https://example.com/images/designhive_logo.png", color = textSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Internship Location
                    OutlinedTextField(
                        value = internshipLocation,
                        onValueChange = { internshipLocation = it },
                        label = { Text("Internship Location*", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Pune", color = textSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Preferred Candidate Type
                    OutlinedTextField(
                        value = preferredCandType,
                        onValueChange = { preferredCandType = it },
                        label = { Text("Preferred Candidate Type*", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Student or Fresher", color = textSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Internship Status
                    var internshipStatusExpanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = internshipStatus,
                            onValueChange = {},
                            label = { Text("Internship Status*", color = textSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = primaryYellow)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        DropdownMenu(
                            expanded = internshipStatusExpanded,
                            onDismissRequest = { internshipStatusExpanded = false },
                            modifier = Modifier.background(pureWhite, RoundedCornerShape(8.dp))
                        ) {
                            listOf("Open", "Closed", "Coming Soon").forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status, color = textDark) },
                                    onClick = {
                                        internshipStatus = status
                                        internshipStatusExpanded = false
                                    }
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .alpha(0f)
                                .clickable { internshipStatusExpanded = true }
                        )
                    }
                }
            }

            // Position Details Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = pureWhite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Position Details",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = textDark
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Duration
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Duration*", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., 3 months", color = textSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Stipend
                    OutlinedTextField(
                        value = stipend,
                        onValueChange = { stipend = it },
                        label = { Text("Stipend*", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., ₹5,000/month", color = textSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Skills Required
                    OutlinedTextField(
                        value = skillsRequired,
                        onValueChange = { skillsRequired = it },
                        label = { Text("Skills Required*", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Figma, Adobe XD, HTML/CSS", color = textSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Perks Offered
                    OutlinedTextField(
                        value = perksOffered,
                        onValueChange = { perksOffered = it },
                        label = { Text("Perks Offered", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Certificate, Letter of Recommendation", color = textSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Internship Photo URL
                    OutlinedTextField(
                        value = internshipPhoto,
                        onValueChange = { internshipPhoto = it },
                        label = { Text("Internship Photo URL*", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., https://example.com/images/internship_image.png", color = textSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Contact Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Contact Email*", color = textSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., internships@designhive.co", color = textSecondary.copy(alpha = 0.6f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryYellow,
                            focusedLabelColor = primaryYellow,
                            cursorColor = primaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Submit Button for Step 1
            Button(
                onClick = {
                    // Build internship description string
                    val internshipDesc = buildString {
                        append("companyName: $companyName, ")
                        append("companyLogo: $companyLogo, ")
                        append("internshipLocation: $internshipLocation, ")
                        append("preferredCandType: $preferredCandType, ")
                        append("internshipStatus: $internshipStatus, ")
                        append("duration: $duration, ")
                        append("stipend: $stipend, ")
                        append("skills: $skillsRequired")
                        if (perksOffered.isNotBlank()) {
                            append(", perks: $perksOffered")
                        }
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
                            if (response != -1) {
                                Toast.makeText(context, "Internship Posted Successfully", Toast.LENGTH_SHORT).show()
                                internshipId = response.toString()
                                isInternshipAdded = true
                            } else {
                                Toast.makeText(context, "Failed to Post Internship", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(4.dp, RoundedCornerShape(14.dp)),
                enabled = internshipName.isNotBlank() &&
                        internshipType.isNotBlank() &&
                        companyName.isNotBlank() &&
                        companyLogo.isNotBlank() &&
                        internshipLocation.isNotBlank() &&
                        preferredCandType.isNotBlank() &&
                        internshipStatus.isNotBlank() &&
                        duration.isNotBlank() &&
                        stipend.isNotBlank() &&
                        skillsRequired.isNotBlank() &&
                        internshipPhoto.isNotBlank() &&
                        email.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryYellow,
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

            // Information Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = lightYellow),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "💡 After posting, you'll have the option to add a custom application form for your internship",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = textDark,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(20.dp)
                )
            }

            // Custom Application Form Section (Step 2)
            if (isInternshipAdded) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = pureWhite),
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
                                        accentYellow,
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
                                    color = textDark
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
                                containerColor = accentYellow,
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
                                    colors = CardDefaults.cardColors(containerColor = lightGray),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${field.label} (${field.type}) - ${if (field.isRequired) "Required" else "Optional"}",
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = textDark
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Dialog for adding new field
            if (showAddFieldDialog) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = pureWhite),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "Add New Field",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = textDark
                            )
                        )

                        OutlinedTextField(
                            value = newFieldLabel,
                            onValueChange = { newFieldLabel = it },
                            label = { Text("Field Label*", color = textSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g., Portfolio Link", color = textSecondary.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryYellow,
                                focusedLabelColor = primaryYellow,
                                cursorColor = primaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = newFieldLabelText,
                            onValueChange = { newFieldLabelText = it },
                            label = { Text("Label Text*", color = textSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g., Please provide your portfolio URL", color = textSecondary.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryYellow,
                                focusedLabelColor = primaryYellow,
                                cursorColor = primaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Field Type Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = newFieldType,
                                onValueChange = {},
                                label = { Text("Field Type*", color = textSecondary) },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = primaryYellow)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryYellow,
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            DropdownMenu(
                                expanded = newFieldTypeExpanded,
                                onDismissRequest = { newFieldTypeExpanded = false },
                                modifier = Modifier.background(pureWhite, RoundedCornerShape(8.dp))
                            ) {
                                listOf("Text", "Number", "Email", "Date", "File", "Checkbox").forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type, color = textDark) },
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
                                    checkedColor = primaryYellow,
                                    uncheckedColor = Color(0xFFE0E0E0)
                                )
                            )
                            Text(
                                text = "Required Field",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = textDark,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        // Dialog Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { showAddFieldDialog = false },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE0E0E0),
                                    contentColor = textDark
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
                            Button(
                                onClick = {
                                    if (newFieldLabel.isNotBlank() && newFieldLabelText.isNotBlank()) {
                                        customFields = customFields + InternshipFieldDefinition(
                                            label = newFieldLabel,
                                            label_text = newFieldLabelText,
                                            type = newFieldType,
                                            isRequired = newFieldIsRequired
                                        )
                                        // Reset dialog fields
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
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryYellow,
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
                        }
                    }
                }
            }

            // Final Submit Button
            if (customFields.isNotEmpty() && isInternshipAdded) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val res = KtorClient.InternshipformTemplate(
                                emp_id = AppDatabase.getInstance(context).UserDao().getUid(),
                                internship_id = internshipId.toInt(),
                                fields = customFields
                            )
                            val resp = KtorClient.addInternshipForm(res)
                            if(resp == true){
                                Toast.makeText(context, "Internship with form successfully created", Toast.LENGTH_SHORT).show()

                                val intent = Intent(context, Internship::class.java)
                                intent.putExtra("key", "value")
                                context.startActivity(intent)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(4.dp, RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = darkYellow,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "🚀 POST THE INTERNSHIP",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
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