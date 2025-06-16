package com.example.rbc_app.BottomNav.Screens

import KtorClient
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rbc_app.InternJobFreelanceAdd.AddJobActivity
import com.example.rbc_app.JobList.JobPostingCard
import com.example.rbc_app.JobList.SavedJobsActivity
import com.example.rbc_app.R

class JobsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shrd = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("Type","def")

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFFFFD700), // Gold/yellow
                    onPrimary = Color.Black,
                    secondary = Color(0xFFFFFACD), // Lemon chiffon
                    background = Color(0xFFFFF8E1), // Light yellowish white
                    surface = Color.White,
                    onSurface = Color.Black
                )
            ) {
                JobsScreen(navController = rememberNavController(),shrd)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsScreen(navController: NavController,shrd:String?) {
    var jobList = remember { mutableStateListOf<KtorClient.jobforCard>() }
    val context = LocalContext.current
    val searchQueryState = remember { mutableStateOf("") }
    val SearchQuery = searchQueryState.value

    val searchList = remember(jobList, SearchQuery) {
        if(SearchQuery.isEmpty()) {
            jobList
        } else {
            jobList.filter { job ->
                job.jobName.contains(SearchQuery, ignoreCase = true) ||
                        job.jobDesc.contains(SearchQuery, ignoreCase = true) ||
                        job.jobStatus.contains(SearchQuery, ignoreCase = true) ||
                        job.jobType.contains(SearchQuery, ignoreCase = true) ||
                        job.jobLocation.contains(SearchQuery, ignoreCase = true) ||
                        job.companyName.contains(SearchQuery, ignoreCase = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        val jobs = KtorClient.getJobList()
        jobList.addAll(jobs)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Enhanced Header with decorative elements
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp)
                    .background(
                        color = Color(0xFFFFF3E0), // Light yellow
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Job Opportunities",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF795548) // Brown
                    )
                )
            }

            // Enhanced Search Bar
            TextField(
                value = searchQueryState.value,
                onValueChange = { searchQueryState.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
                placeholder = {
                    Text(
                        "Search jobs by title, company or location",
                        color = Color(0xFF795548).copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFFFFD700), // Gold
                        modifier = Modifier.size(24.dp)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true
            )

            // Enhanced Action buttons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if(shrd=="Employer"){
                    Button(
                        onClick = {
                            context.startActivity(Intent(context, AddJobActivity::class.java))
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700), // Gold
                            contentColor = Color.Black
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add Job",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(context, SavedJobsActivity::class.java))
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFF3E0), // Light yellow
                        contentColor = Color(0xFF795548) // Brown
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Saved",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Saved Jobs",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Job list (unchanged except for spacing)
            if (searchList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "No jobs found",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No job opportunities found",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFF795548) // Brown
                    )
                    Text(
                        text = "Try adjusting your search or add a new job posting",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF795548).copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                Column {
                    Text(
                        text = "Available Positions (${searchList.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF795548) // Brown
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(searchList) { job ->
                            // Original JobPostingCard remains unchanged
                            JobPostingCard(job)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJobsScreen() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFFFD700),
            onPrimary = Color.Black,
            secondary = Color(0xFFFFFACD),
            background = Color(0xFFFFF8E1),
            surface = Color.White,
            onSurface = Color.Black
        )
    ) {
        JobsScreen(navController = rememberNavController(),"def")
    }
}