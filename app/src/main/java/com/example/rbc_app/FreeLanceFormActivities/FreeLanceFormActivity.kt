package com.example.rbc_app.FreeLanceFormActivities

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
import com.example.rbc_app.BottomNav.Screens.FreeLance
import com.example.rbc_app.JobFormActivities.JobFieldDefinition
import com.example.rbc_app.RoomDatabase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Professional Color Palette (same as ProfileScreen)
object FreeLanceFormColors {
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

class FreeLanceFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val freelance_id = intent.getIntExtra("freelance_id", -1)
        val freelance_emp_id = intent.getIntExtra("Empid", -1)

        Log.d("FreeLanceFormActivity", "Freelance ID: $freelance_id")
        setContent {
            FreeLanceForm(freelance_id,freelance_emp_id, this)
        }
    }
}

@Composable
fun FreeLanceForm(freelanceId: Int,freelance_emp_id:Int, activity: ComponentActivity) {
    val context = LocalContext.current
    var fields by remember { mutableStateOf<List<JobFieldDefinition>>(emptyList()) }
    var creator_id by remember { mutableStateOf("") }
    val inputValues = remember { mutableStateMapOf<String, String>() }
    var uid by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    LaunchedEffect(freelanceId) {
        uid = AppDatabase.getInstance(context).UserDao().getUid().toString()
        creator_id = KtorClient.getFreelanceIdfromcreator(freelanceId.toString()).toString()
        fields = KtorClient.getFreeLanceFormTemplate(freelanceId)
        Log.d("FreeLanceForm", "Fields fetched: ${fields.size} fields")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        FreeLanceFormColors.LightYellow,
                        FreeLanceFormColors.CreamWhite,
                        FreeLanceFormColors.WarmWhite
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
                    containerColor = FreeLanceFormColors.WarmWhite
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
                                        FreeLanceFormColors.PrimaryYellow,
                                        FreeLanceFormColors.DarkYellow
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
                        "FREELANCE APPLICATION FORM",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = FreeLanceFormColors.DarkGray
                    )

                    Text(
                        "Fill out the details below to submit your application",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FreeLanceFormColors.SoftGray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            fields.forEach { field ->
                ProfessionalFreeLanceInputField(field, inputValues)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Professional Submit Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FreeLanceFormColors.WarmWhite
                )
            ) {
                Button(
                    onClick = {
                        Log.d("FreeLanceForm", "Submit button clicked!")
                        CoroutineScope(Dispatchers.IO).launch {
                            val formJson = Json.encodeToString(inputValues.toMap())
                            val details = KtorClient.FreelanceFormTemplateResponse(
                                freelance_id = freelanceId,
                                emp_id =freelance_emp_id ,
                                user_id = uid.toInt(),
                                field_details = formJson
                            )

                            Log.d("FreeLanceForm", "Submitting details: $details")

                            val res: String = KtorClient.detailAddFreeLanceForm(details)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, res, Toast.LENGTH_SHORT).show()

                                if (res.contains("form submitted successfully")) {
                                    context.startActivity(Intent(context,FreeLance::class.java))
                                    activity.finish() // Finish current activity to prevent back navigation
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FreeLanceFormColors.PrimaryYellow,
                        contentColor = FreeLanceFormColors.DarkGray
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
fun ProfessionalFreeLanceInputField(
    field: JobFieldDefinition,
    inputValues: MutableMap<String, String>
) {
    var input by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = FreeLanceFormColors.WarmWhite
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
                            FreeLanceFormColors.LightYellow,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getFieldIcon(field.type),
                        contentDescription = field.label,
                        tint = FreeLanceFormColors.DarkYellow,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = field.label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = FreeLanceFormColors.DarkGray
                    )
                    if (field.label_text.isNotEmpty()) {
                        Text(
                            text = field.label_text,
                            style = MaterialTheme.typography.bodySmall,
                            color = FreeLanceFormColors.SoftGray
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
                                color = FreeLanceFormColors.SoftGray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreeLanceFormColors.PrimaryYellow,
                            focusedLabelColor = FreeLanceFormColors.DarkYellow,
                            cursorColor = FreeLanceFormColors.DarkYellow
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
                                color = FreeLanceFormColors.SoftGray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FreeLanceFormColors.PrimaryYellow,
                            focusedLabelColor = FreeLanceFormColors.DarkYellow,
                            cursorColor = FreeLanceFormColors.DarkYellow
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                "file" -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = FreeLanceFormColors.AccentGray
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
                                    tint = FreeLanceFormColors.SoftGray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "(File upload will be implemented later)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FreeLanceFormColors.SoftGray,
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

// Data classes needed for the freelance form
data class FreeLanceFieldDefinition(
    val label: String,
    val label_text: String,
    val type: String,
    val required: Boolean
)