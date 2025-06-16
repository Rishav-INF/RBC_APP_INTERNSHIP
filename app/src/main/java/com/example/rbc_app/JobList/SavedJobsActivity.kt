package com.example.rbc_app.JobList

import KtorClient
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rbc_app.RoomDatabase.AppDatabase

class SavedJobsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SavedJobsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedJobsScreen() {
    val jobList = remember { mutableStateListOf<KtorClient.jobforCard>() }
    var savedJobsIds by remember { mutableStateOf(listOf<String>()) }
    var filteredJobs by remember { mutableStateOf(listOf<KtorClient.jobforCard>()) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val jobs = KtorClient.getJobList()
        val savedJobs = AppDatabase.getInstance(context).UserDao().getSavedJobs()
        savedJobsIds = savedJobs.split(",").filter { it.all { char -> char.isDigit() } }
        jobList.addAll(jobs)

        filteredJobs = jobs.filter { job ->
            savedJobsIds.contains(job.job_id.toString())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Jobs") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (filteredJobs.isEmpty()) {
                Text(
                    text = "No saved jobs available.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredJobs) { job ->
                        JobPostingCard(job)
                    }
                }
            }
        }
    }
}
