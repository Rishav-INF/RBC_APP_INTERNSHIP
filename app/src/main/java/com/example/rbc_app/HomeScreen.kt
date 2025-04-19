package com.example.rbc_app


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.tooling.preview.Preview

class HomeScreen:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(){
            CareerOpportunitiesScreen()
        }
    }

    @Composable
    fun CareerOpportunitiesScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Unlock Your Career",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Search bar placeholder
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Q Search Opportunities") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // First row of cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Internships card
                OpportunityCard(
                    title = "Internships",
                    items = listOf("Gain", "Practical", "Experience"),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Mentorships card
                OpportunityCard(
                    title = "Mentorships",
                    items = listOf("Guidance", "From Top Mentors"),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Second row of cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Jobs card
                OpportunityCard(
                    title = "Jobs",
                    items = listOf("Explore", "Diverse Careers"),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Practice card
                OpportunityCard(
                    title = "Practice",
                    items = listOf("Refine", "Skills Daily"),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Competitions card (full width)
            OpportunityCard(
                title = "Competitions",
                items = listOf("Battle", "For Excellence"),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // More card (full width)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "More",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pro banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Just Went RBC Pro! Explore Pro",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Business banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF03DAC6))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "RBC For Business →",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer links
            Column {
                Text(
                    text = "RBC",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("Internships", modifier = Modifier.padding(bottom = 4.dp))
                Text("Jobs", modifier = Modifier.padding(bottom = 4.dp))
                Text("Competitions", modifier = Modifier.padding(bottom = 4.dp))
                Text("Mentorships", modifier = Modifier.padding(bottom = 4.dp))
                Text("More")
            }
        }
    }

    @Composable
    fun OpportunityCard(
        title: String,
        items: List<String>,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                items.forEach { item ->
                    Text(
                        text = item,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewCareerOpportunitiesScreen() {
        CareerOpportunitiesScreen()
    }

}