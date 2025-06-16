package com.example.rbc_app.InternshipFormActivities

import KtorClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.rbc_app.BottomNav.Screens.Internships
import com.example.rbc_app.JobFormActivities.InternshipFieldDefinition
import com.example.rbc_app.JobFormActivities.UserAddFormDetailsInternships
import com.example.rbc_app.RoomDatabase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Professional Color Palette (same as FreeLanceForm)
object InternshipFormColors {
    val PrimaryYellow = Color(0xFFF5C842)
    val LightYellow = Color(0xFFFFF8E1)
    val DarkYellow = Color(0xFFE6B800)
    val CreamWhite = Color(0xFFFFFDF7)
    val WarmWhite = Color(0xFFFAF9F7)
    val SoftGray = Color(0xFF8A8A8A)
    val DarkGray = Color(0xFF2C2C2C)
    val AccentGray = Color(0xFFF5F5F5)
    val BorderGray = Color(0xFFE0E0E0)
    val SuccessGreen = Color(0xFF4CAF50)
}

class InternshipFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val internship_id = intent.getIntExtra("internship_id", -1)
        Log.d("InternshipFormActivity", "Internship ID: $internship_id")
        setContent {
            InternshipForm(internship_id, this)
        }
    }
}

@Composable
fun InternshipForm(internshipId: Int, activity: ComponentActivity) {
    val context = LocalContext.current
    var fields by remember { mutableStateOf<List<InternshipFieldDefinition>>(emptyList()) }
    var supervisor_id by remember { mutableStateOf("") }
    val inputValues = remember { mutableStateMapOf<String, String>() }
    var uid by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    LaunchedEffect(internshipId) {
        uid = AppDatabase.getInstance(context).UserDao().getUid().toString()
        supervisor_id = KtorClient.getEmpIdFromInternshipId(internshipId).toString()
        fields = KtorClient.getInternshipFormTemplate(internshipId)
        Log.d("InternshipForm", "Fields fetched: ${fields.size} fields")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        InternshipFormColors.LightYellow,
                        InternshipFormColors.CreamWhite,
                        InternshipFormColors.WarmWhite
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Professional Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = InternshipFormColors.WarmWhite
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        InternshipFormColors.PrimaryYellow,
                                        InternshipFormColors.DarkYellow
                                    )
                                ),
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Form Icon",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "INTERNSHIP APPLICATION FORM",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = InternshipFormColors.DarkGray
                    )

                    Text(
                        "Fill out the details below to submit your application",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InternshipFormColors.SoftGray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            fields.forEach { field ->
                ProfessionalInternshipInputField(field, inputValues)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Professional Submit Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = InternshipFormColors.WarmWhite
                )
            ) {
                Button(
                    onClick = {
                        Log.d("InternshipForm", "Submit button clicked!")
                        CoroutineScope(Dispatchers.IO).launch {
                            val formJson = Json.encodeToString(inputValues.toMap())
                            val details = UserAddFormDetailsInternships(
                                emp_id = supervisor_id.toInt(),
                                internship_id = internshipId,
                                user_id = uid.toInt(),
                                field_details = formJson
                            )

                            Log.d("InternshipForm", "Submitting details: $details")

                            val res: String = KtorClient.detailAddInternshipForm(details)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, res, Toast.LENGTH_SHORT).show()

                                if (res.contains("internship application submitted successfully")) {
                                    context.startActivity(Intent(context, Internships::class.java))
                                    activity.finish() // Finish current activity to prevent back navigation
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = InternshipFormColors.PrimaryYellow,
                        contentColor = InternshipFormColors.DarkGray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Submit",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Submit Application",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfessionalInternshipInputField(
    field: InternshipFieldDefinition,
    inputValues: MutableMap<String, String>
) {
    var input by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = InternshipFormColors.WarmWhite
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Field Header with Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            InternshipFormColors.LightYellow,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getFieldIcon(field.type),
                        contentDescription = field.label,
                        tint = InternshipFormColors.DarkYellow,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = field.label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = InternshipFormColors.DarkGray
                    )
                    if (field.label_text.isNotEmpty()) {
                        Text(
                            text = field.label_text,
                            style = MaterialTheme.typography.bodySmall,
                            color = InternshipFormColors.SoftGray
                        )
                    }
                }
            }

            when (field.type.lowercase()) {
                "text", "email", "number" -> {
                    OutlinedTextField(
                        value = input,
                        onValueChange = {
                            input = it
                            inputValues[field.label] = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = when (field.type.lowercase()) {
                                "email" -> KeyboardType.Email
                                "number" -> KeyboardType.Number
                                else -> KeyboardType.Text
                            }
                        ),
                        placeholder = {
                            Text(
                                "Enter ${field.label.lowercase()}",
                                color = InternshipFormColors.SoftGray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InternshipFormColors.PrimaryYellow,
                            focusedLabelColor = InternshipFormColors.DarkYellow,
                            cursorColor = InternshipFormColors.DarkYellow
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                "portfolio" -> {
                    OutlinedTextField(
                        value = input,
                        onValueChange = {
                            input = it
                            inputValues[field.label] = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Paste your portfolio link",
                                color = InternshipFormColors.SoftGray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InternshipFormColors.PrimaryYellow,
                            focusedLabelColor = InternshipFormColors.DarkYellow,
                            cursorColor = InternshipFormColors.DarkYellow
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                "file" -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = InternshipFormColors.AccentGray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info",
                                    tint = InternshipFormColors.SoftGray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "(File upload will be implemented later)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = InternshipFormColors.SoftGray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                else -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Unsupported input type: ${field.type}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFD32F2F),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getFieldIcon(fieldType: String): ImageVector {
    return when (fieldType.lowercase()) {
        "email" -> Icons.Default.Email
        "text" -> Icons.Default.Person
        "number" -> Icons.Default.Info
        "portfolio" -> Icons.Default.Send
        else -> Icons.Default.Info
    }
}