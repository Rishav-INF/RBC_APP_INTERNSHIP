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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.freelanceapp.ui.screens.AddFreelanceWork
import com.example.rbc_app.InternJobFreelanceAdd.AddInternship
import com.example.rbc_app.InternJobFreelanceAdd.AddInternshipScreen
import com.example.rbc_app.InternshipList.InternshipPostingCard
import com.example.rbc_app.R

class Internships : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternshipsScreen(navController = rememberNavController())
        }
    }
}

@Composable
fun InternshipsScreen(navController: NavController,) {

    val context = LocalContext.current

    var internshipList = remember{ mutableListOf<KtorClient.internshipforCard>() }
    var searchQueryState = remember{ mutableStateOf("") }
    var searchValue = searchQueryState.value

    val searchList=remember(internshipList,searchValue) {
        if (searchValue.isEmpty()) {
            internshipList
        } else {
            internshipList.filter { job ->
                job.Name.contains(searchValue, ignoreCase = true) ||
                        job.internshipDesc.contains(searchValue, ignoreCase = true) ||
                        job.internshipType.contains(searchValue, ignoreCase = true) ||
                        job.internshipLocation.contains(searchValue, ignoreCase = true) ||
                        job.internshipStatus.contains(searchValue, ignoreCase = true) ||
                        job.preferredCandType.contains(searchValue, ignoreCase = true) ||
                        job.companyName.contains(searchValue, ignoreCase = true)
            }
        }
    }
    LaunchedEffect(Unit){ val intern = KtorClient.getInternshipList()
        internshipList.addAll(intern)}

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {



            TextField(
                value = searchQueryState.value,
                onValueChange = { searchQueryState.value = it },
                modifier = Modifier.fillMaxWidth().padding(vertical=8.dp),
                placeholder = { Text("Search Internships by title, company or loaction") },
                leadingIcon = {
                    Icon(
                            imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
            Text(
                text = "Internships Available",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Spacer(Modifier.height(10.dp))
            Button(
                onClick = {
                    context.startActivity(Intent(context, AddInternship()::class.java))
                }
            ) {
                Text("ADD INTERNSHIP OPPORTUNITIES")
            }

            Spacer(Modifier.height(10.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)){
                items(searchList){
                    internship->
                    InternshipPostingCard(internship)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInternships() {
    InternshipsScreen(navController = rememberNavController())
}
