package com.example.rbc_app.JobFormActivities

import KtorClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.rbc_app.BottomNav.Screens.JobsActivity
import com.example.rbc_app.RoomDatabase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class jobFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val job_id = intent.getIntExtra("job_id", -1)
        Log.d("JobFormActivity", "Job ID: $job_id")
        setContent {
            JobForm(job_id, this)
        }
    }
}

@Composable
fun JobForm(jobId: Int, activity: ComponentActivity) {
    val context = LocalContext.current
    var fields by remember { mutableStateOf<List<JobFieldDefinition>>(emptyList()) }
    var emp_id by remember { mutableStateOf("") }
    val inputValues = remember { mutableStateMapOf<String, String>() }
    var uid by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    LaunchedEffect(jobId) {
        uid = AppDatabase.getInstance(context).UserDao().getUid().toString()
        emp_id = KtorClient.getEmpIdFromJobId(jobId).toString()
        fields = KtorClient.getJobFormTemplate(jobId)
        Log.d("JobForm", "Fields fetched: ${fields.size} fields")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("FILL OUT THE FORM FOR THE JOB", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(20.dp))

        fields.forEach { field ->
            DynamicInputField(field, inputValues)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                Log.d("JobForm", "Submit button clicked!")

                CoroutineScope(Dispatchers.IO).launch {
                    val formJson = Json.encodeToString(inputValues.toMap())

                    val details = UserAddFormDetails(
                        emp_id = emp_id.toInt(),
                        job_id = jobId,
                        user_id = uid.toInt(),
                        field_details = formJson
                    )

                    Log.d("JobForm", "Submitting details: $details")

                    val res: String = KtorClient.detailAddJobForm(details)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, res, Toast.LENGTH_SHORT).show()

                        if (res.contains("form submitted successfully")) {
                            context.startActivity(Intent(context, JobsActivity::class.java))
                            activity.finish() // ✅ Finish current activity to prevent back navigation
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun DynamicInputField(field: JobFieldDefinition, inputValues: MutableMap<String, String>) {
    var input by remember { mutableStateOf("") }

    Column {
        Text(text = field.label, style = MaterialTheme.typography.bodyLarge)
        Text(text = field.label_text, style = MaterialTheme.typography.bodySmall)

        when (field.type.lowercase()) {
            "text", "email", "number" -> {
                TextField(
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
                    placeholder = { Text("Enter ${field.label.lowercase()}") }
                )
            }

            "file" -> {
                Text("(File input not supported yet)", style = MaterialTheme.typography.bodySmall)
            }

            else -> {
                Text("Unsupported input type: ${field.type}")
            }
        }
    }
}
