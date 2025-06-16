package com.example.freelanceapp.ui.screens

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
import com.example.rbc_app.RoomDatabase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

// Professional color scheme
object FreelanceFormColors {
    val PrimaryYellow = Color(0xFFFFC107)
    val LightYellow = Color(0xFFFFF8E1)
    val AccentYellow = Color(0xFFFFD54F)
    val DarkYellow = Color(0xFFFF8F00)
    val PureWhite = Color.White
    val LightGray = Color(0xFFF8F9FA)
    val TextDark = Color(0xFF2C3E50)
    val TextSecondary = Color(0xFF5D6D7E)
}

class AddFreelanceWork : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(FreelanceFormColors.LightYellow, FreelanceFormColors.PureWhite)
                        )
                    )
            ) {
                AddFreelanceWorkScreen()
            }
        }
    }
}

@Composable
fun AddFreelanceWorkScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Form state variables (unchanged)
    var freelanceTitle by remember { mutableStateOf("") }
    var freelanceDesc by remember { mutableStateOf("") }
    var freelanceType by remember { mutableStateOf("") }
    var budgetRange by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var requiredSkills by remember { mutableStateOf("") }
    var contactEmail by remember { mutableStateOf("") }
    var isRemote by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf("") }
    var companyLogo by remember { mutableStateOf("") }
    var customFields by remember { mutableStateOf<List<KtorClient.FreelanceFieldDefinition>>(emptyList()) }
    var showAddFieldDialog by remember { mutableStateOf(false) }
    var isBasicInfoAdded by remember { mutableStateOf(false) }
    var freelanceId by remember { mutableStateOf("") }
    var newFieldLabel by remember { mutableStateOf("") }
    var newFieldLabelText by remember { mutableStateOf("") }
    var newFieldType by remember { mutableStateOf("Text") }
    var newFieldIsRequired by remember { mutableStateOf(false) }
    var newFieldTypeExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }
    val freelanceTypes = listOf("Fixed Price", "Hourly", "Milestone-based")

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
            colors = CardDefaults.cardColors(containerColor = FreelanceFormColors.PureWhite),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Create Freelance Opportunity",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = FreelanceFormColors.TextDark
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Complete 2 simple steps to post your freelance work",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = FreelanceFormColors.TextSecondary
                    )
                )
            }
        }

        if (!isBasicInfoAdded) {
            // STEP 1: Basic Information
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = FreelanceFormColors.PureWhite),
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
                                    FreelanceFormColors.PrimaryYellow,
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
                                color = FreelanceFormColors.TextDark
                            )
                        )
                    }

                    // Project Title
                    OutlinedTextField(
                        value = freelanceTitle,
                        onValueChange = { freelanceTitle = it },
                        label = { Text("Project Title*", color = FreelanceFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Mobile App UI Design", color = FreelanceFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                            focusedLabelColor = FreelanceFormColors.PrimaryYellow,
                            cursorColor = FreelanceFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Project Description
                    OutlinedTextField(
                        value = freelanceDesc,
                        onValueChange = { freelanceDesc = it },
                        label = { Text("Project Description*", color = FreelanceFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Describe the project requirements in detail", color = FreelanceFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        singleLine = false,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                            focusedLabelColor = FreelanceFormColors.PrimaryYellow,
                            cursorColor = FreelanceFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Freelance Type Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = freelanceType,
                            onValueChange = {},
                            label = { Text("Freelance Type*", color = FreelanceFormColors.TextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = FreelanceFormColors.PrimaryYellow)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        DropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false },
                            modifier = Modifier.background(FreelanceFormColors.PureWhite, RoundedCornerShape(8.dp))
                        ) {
                            freelanceTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type, color = FreelanceFormColors.TextDark) },
                                    onClick = {
                                        freelanceType = type
                                        typeExpanded = false
                                    }
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .alpha(0f)
                                .clickable { typeExpanded = true }
                        )
                    }

                    // Budget Range
                    OutlinedTextField(
                        value = budgetRange,
                        onValueChange = { budgetRange = it },
                        label = { Text("Budget Range*", color = FreelanceFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., \$500-\$1000", color = FreelanceFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                            focusedLabelColor = FreelanceFormColors.PrimaryYellow,
                            cursorColor = FreelanceFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Deadline
                    OutlinedTextField(
                        value = deadline,
                        onValueChange = { deadline = it },
                        label = { Text("Project Deadline*", color = FreelanceFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., 30 days or 2023-12-31", color = FreelanceFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                            focusedLabelColor = FreelanceFormColors.PrimaryYellow,
                            cursorColor = FreelanceFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Required Skills
                    OutlinedTextField(
                        value = requiredSkills,
                        onValueChange = { requiredSkills = it },
                        label = { Text("Required Skills*", color = FreelanceFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Figma, React, Content Writing", color = FreelanceFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                            focusedLabelColor = FreelanceFormColors.PrimaryYellow,
                            cursorColor = FreelanceFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Remote Work Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isRemote = !isRemote }
                            .padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = isRemote,
                            onCheckedChange = { isRemote = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = FreelanceFormColors.PrimaryYellow,
                                uncheckedColor = Color(0xFFE0E0E0)
                            )
                        )
                        Text(
                            text = "Remote Work Available",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = FreelanceFormColors.TextDark,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Contact Email
                    OutlinedTextField(
                        value = contactEmail,
                        onValueChange = { contactEmail = it },
                        label = { Text("Contact Email*", color = FreelanceFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., contact@example.com", color = FreelanceFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                            focusedLabelColor = FreelanceFormColors.PrimaryYellow,
                            cursorColor = FreelanceFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Location
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location", color = FreelanceFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Mumbai", color = FreelanceFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                            focusedLabelColor = FreelanceFormColors.PrimaryYellow,
                            cursorColor = FreelanceFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Company Logo
                    OutlinedTextField(
                        value = companyLogo,
                        onValueChange = { companyLogo = it },
                        label = { Text("Company Logo Filename", color = FreelanceFormColors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., photo.png", color = FreelanceFormColors.TextSecondary.copy(alpha = 0.6f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                            focusedLabelColor = FreelanceFormColors.PrimaryYellow,
                            cursorColor = FreelanceFormColors.PrimaryYellow,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Submit Button
                    Button(
                        onClick = {
                            val freelanceWork = KtorClient.Freelance(
                                createdAt = LocalDateTime.now(),
                                freelanceDesc = buildString {
                                    append("EstimatedDuration:$deadline,")
                                    append("Pay:$budgetRange,")
                                    append("Contact Email:$contactEmail,")
                                    append("Status:Open,")
                                    append("CompanyName:$freelanceTitle,")
                                    append("CompanyLogo:$companyLogo,")
                                    append("Location:$location,")
                                    append("PreferredCandidateType:$freelanceType")
                                },
                                freelanceType = freelanceType,
                                freelanceCreator = contactEmail,
                                freelanceId = 0,
                                requestsBy = "",
                                transferredTo = ""
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                withContext(Dispatchers.Main) {
                                    val response = KtorClient.sendFreelanceRow(freelanceWork)
                                    if (response != -1) {
                                        freelanceId = response.toString()
                                        isBasicInfoAdded = true
                                        Toast.makeText(context, "Basic information saved", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Failed to save", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(4.dp, RoundedCornerShape(14.dp)),
                        enabled = freelanceTitle.isNotBlank() &&
                                freelanceDesc.isNotBlank() &&
                                freelanceType.isNotBlank() &&
                                budgetRange.isNotBlank() &&
                                deadline.isNotBlank() &&
                                requiredSkills.isNotBlank() &&
                                contactEmail.isNotBlank(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FreelanceFormColors.PrimaryYellow,
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
                colors = CardDefaults.cardColors(containerColor = FreelanceFormColors.LightYellow),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "💡 After posting basic info, you can add custom application questions",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = FreelanceFormColors.TextDark,
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
                colors = CardDefaults.cardColors(containerColor = FreelanceFormColors.PureWhite),
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
                                    FreelanceFormColors.AccentYellow,
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
                                color = FreelanceFormColors.TextDark
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
                            containerColor = FreelanceFormColors.AccentYellow,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Add New Question",
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
                                colors = CardDefaults.cardColors(containerColor = FreelanceFormColors.LightGray),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = field.label,
                                        fontWeight = FontWeight.SemiBold,
                                        color = FreelanceFormColors.TextDark
                                    )
                                    Text(
                                        text = "Type: ${field.type} • ${if (field.isRequired) "Required" else "Optional"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = FreelanceFormColors.TextSecondary
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
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = KtorClient.addFreelanceForm(
                                KtorClient.FreelanceFormTemplate(
                                    freelance_id = freelanceId.toInt(),
                                    user_id = AppDatabase.getInstance(context).UserDao().getUid()!!,
                                    fields = customFields
                                )
                            )

                            withContext(Dispatchers.Main) {
                                if (response) {
                                    Toast.makeText(context, "Freelance work posted successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to save form", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(4.dp, RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FreelanceFormColors.DarkYellow,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "🚀 PUBLISH FREELANCE WORK",
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
                        "Add New Question",
                        fontWeight = FontWeight.Bold,
                        color = FreelanceFormColors.TextDark
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = newFieldLabel,
                            onValueChange = { newFieldLabel = it },
                            label = { Text("Question Label*", color = FreelanceFormColors.TextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g., Portfolio Link", color = FreelanceFormColors.TextSecondary.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                                focusedLabelColor = FreelanceFormColors.PrimaryYellow,
                                cursorColor = FreelanceFormColors.PrimaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = newFieldLabelText,
                            onValueChange = { newFieldLabelText = it },
                            label = { Text("Question Text*", color = FreelanceFormColors.TextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g., Please provide your portfolio URL", color = FreelanceFormColors.TextSecondary.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                                focusedLabelColor = FreelanceFormColors.PrimaryYellow,
                                cursorColor = FreelanceFormColors.PrimaryYellow,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Field Type Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = newFieldType,
                                onValueChange = {},
                                label = { Text("Answer Type*", color = FreelanceFormColors.TextSecondary) },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = FreelanceFormColors.PrimaryYellow)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = FreelanceFormColors.PrimaryYellow,
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            DropdownMenu(
                                expanded = newFieldTypeExpanded,
                                onDismissRequest = { newFieldTypeExpanded = false },
                                modifier = Modifier.background(FreelanceFormColors.PureWhite, RoundedCornerShape(8.dp))
                            ) {
                                listOf("Text", "Number", "Email", "Date", "File", "Checkbox").forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type, color = FreelanceFormColors.TextDark) },
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
                                    checkedColor = FreelanceFormColors.PrimaryYellow,
                                    uncheckedColor = Color(0xFFE0E0E0)
                                )
                            )
                            Text(
                                text = "Required Question",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = FreelanceFormColors.TextDark,
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
                                customFields = customFields + KtorClient.FreelanceFieldDefinition(
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
                            containerColor = FreelanceFormColors.PrimaryYellow,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Add Question",
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
                            contentColor = FreelanceFormColors.TextDark
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
                containerColor = FreelanceFormColors.PureWhite,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddFreelanceWorkScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(FreelanceFormColors.LightYellow, FreelanceFormColors.PureWhite)
                )
            )
    ) {
        AddFreelanceWorkScreen()
    }
}