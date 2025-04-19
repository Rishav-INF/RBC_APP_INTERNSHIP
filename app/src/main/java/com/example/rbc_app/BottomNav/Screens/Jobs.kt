package com.example.rbc_app.BottomNav.Screens

import KtorClient
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rbc_app.InternJobFreelanceAdd.AddInternship
import com.example.rbc_app.InternJobFreelanceAdd.AddJobActivity
import com.example.rbc_app.JobList.JobPostingCard
import com.example.rbc_app.R
import com.example.rbc_app.RoomDatabase.UserDao_Impl
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class JobsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobsScreen(navController = rememberNavController())
        }
    }
}

@Composable
fun JobsScreen(navController: NavController) {
    var jobList = remember { mutableStateListOf<KtorClient.jobforCard>() }
    val context= LocalContext.current
    val searchQueryState = remember { mutableStateOf("") }
    val SearchQuery = searchQueryState.value
    val searchList = remember ( jobList,SearchQuery){
        if(SearchQuery.isEmpty()){
            jobList
        }else{
            jobList.filter{
                job->
                job.jobName.contains(SearchQuery,ignoreCase = true) ||
                        job.jobDesc.contains(SearchQuery,ignoreCase = true) ||
                        job.jobStatus.contains(SearchQuery,ignoreCase = true) ||
                        job.jobType.contains(SearchQuery,ignoreCase = true) ||
                        job.jobLocation.contains(SearchQuery,ignoreCase = true) ||
                        job.companyName.contains(SearchQuery,ignoreCase = true)
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



            // Header
            TextField(
                value = searchQueryState.value,
                onValueChange = {searchQueryState.value = it},
                modifier = Modifier.fillMaxWidth().padding(vertical=8.dp),
                placeholder = { Text("Search jobs by title, company or location")},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(8.dp),
                singleLine=true
            )
//            Button(
//                onClick = {
//                    context.startActivity(Intent(context,))
//                }
//            ) { }

            Text(
                text = "Available Positions",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = {
                    context.startActivity(Intent(context, AddJobActivity()::class.java))
                }
            ) {
                Text("ADD JOB OPPORTUNITIES")
            }
            Spacer(Modifier.height(10.dp))
            // Job List
                LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(searchList) { job ->
                    JobPostingCard(job)
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewJobsScreen() {
    JobsScreen(navController = rememberNavController())
}