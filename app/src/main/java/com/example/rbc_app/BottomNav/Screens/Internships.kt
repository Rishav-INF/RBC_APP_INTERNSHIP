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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.rbc_app.InternJobFreelanceAdd.AddInternship
import com.example.rbc_app.InternshipList.InternshipPostingCard
import com.example.rbc_app.JobList.SavedJobsActivity
import com.example.rbc_app.R

class Internships : ComponentActivity() {
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InternshipsScreen(navController = rememberNavController(),shrd)
                }
            }
        }
    }
}

@Composable
fun InternshipsScreen(navController: NavController,shrd:String?) {
    val context = LocalContext.current
    var internshipList = remember { mutableStateListOf<KtorClient.internshipforCard>() }
    var searchQueryState = remember { mutableStateOf("") }

    val searchList = remember(searchQueryState.value, internshipList) {
        if (searchQueryState.value.isEmpty()) {
            internshipList
        } else {
            internshipList.filter {
                it.Name.contains(searchQueryState.value, true) ||
                        it.internshipDesc.contains(searchQueryState.value, true) ||
                        it.internshipType.contains(searchQueryState.value, true) ||
                        it.internshipLocation.contains(searchQueryState.value, true) ||
                        it.internshipStatus.contains(searchQueryState.value, true) ||
                        it.preferredCandType.contains(searchQueryState.value, true) ||
                        it.companyName.contains(searchQueryState.value, true)
            }
        }
    }

    LaunchedEffect(Unit) {
        val fetched = KtorClient.getInternshipList()
        internshipList.addAll(fetched)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Enhanced Search Bar
        TextField(
            value = searchQueryState.value,
            onValueChange = { searchQueryState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
            placeholder = {
                Text(
                    "Search internships...",
                    color = Color(0xFF795548).copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(24.dp)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Enhanced Header with decorative elements
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(
                    color = Color(0xFFFFF3E0), // Light yellow
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFD700), // Gold
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Available Internships",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF795548) // Brown
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enhanced Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if(shrd=="Employer"){
                Button(
                    onClick = {
                        context.startActivity(Intent(context, AddInternship::class.java))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
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
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add Internship",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Button(
                onClick = {
                    context.startActivity(Intent(context, SavedJobsActivity::class.java))
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
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
                        painter = painterResource(R.drawable.cale),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Saved",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Internship List (unchanged except for spacing)
        if (searchList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (searchQueryState.value.isEmpty()) {
                        "No internships available"
                    } else {
                        "No matching internships found"
                    },
                    color = Color(0xFF795548), // Brown
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(searchList) { internship ->
                    // Original InternshipPostingCard remains unchanged
                    InternshipPostingCard(internship)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInternships() {
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
        InternshipsScreen(navController = rememberNavController(),"def")
    }
}