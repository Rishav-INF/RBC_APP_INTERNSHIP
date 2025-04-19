package com.example.freelanceapp.ui.screens

import KtorClient
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class AddFreelanceWork:ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddFreelanceWorkScreen()
        }
    }
}

@Composable
fun AddFreelanceWorkScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var freelanceDesc by remember { mutableStateOf("") }
    var freelanceType by remember { mutableStateOf("") }
    var freelanceCreator by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Add Freelance Work",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Description Field
        OutlinedTextField(
            value = freelanceDesc,
            onValueChange = { freelanceDesc = it },
            label = { Text("Description*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 4,
            placeholder = { Text("Describe the work to be done") }
        )

        // Type Field
        OutlinedTextField(
            value = freelanceType,
            onValueChange = { freelanceType = it },
            label = { Text("Type*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Design, Development, Writing") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Creator Field
        OutlinedTextField(
            value = freelanceCreator,
            onValueChange = { freelanceCreator = it },
            label = { Text("Creator*") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Who created the opportunity") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                // Create a Freelance row with the current values
                val FreeLanceRow = KtorClient.Freelance(
                    createdAt = LocalDateTime.now(),
                    freelanceDesc = freelanceDesc,
                    freelanceId = null,
                    freelanceType = freelanceType,
                    freelanceCreator = freelanceCreator,
                    requestsBy = "",
                    transferredTo = ""
                )

                // Send the Freelance row data to the server
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        val response = KtorClient.sendFreelanceRow(FreeLanceRow)
                        if (response) {
                            Log.d("RowAddedOrNot","${response}")
                            Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            enabled = freelanceDesc.isNotBlank() && freelanceType.isNotBlank() && freelanceCreator.isNotBlank(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Confirm Add", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun pr(){
    AddFreelanceWorkScreen()
}