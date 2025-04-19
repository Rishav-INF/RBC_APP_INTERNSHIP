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
import com.example.rbc_app.InternshipList.InternshipPostingCard
import com.example.rbc_app.JobList.FreelanceCard
import com.example.rbc_app.R

class FreeLance : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternshipsScreen(navController = rememberNavController())
        }
    }
}

@Composable
fun FreeLanceScreen(navController: NavController,) {
    val context = LocalContext.current
    var FreeLanceCardList = remember{ mutableListOf<KtorClient.FreeLanceCard>() }
    var searchQueryState = remember{ mutableStateOf("") }
    var searchValue = searchQueryState.value

    val searchList=remember(FreeLanceCardList,searchValue) {
        if (searchValue.isEmpty()) {
            FreeLanceCardList
        } else {
            FreeLanceCardList.filter { FreeLance ->
                FreeLance.freelance_type.contains(searchValue, ignoreCase = true) ||
                        FreeLance.companyName.contains(searchValue, ignoreCase = true) ||
                        FreeLance.preferredCandType.contains(searchValue, ignoreCase = true) ||
                        FreeLance.Pay.contains(searchValue, ignoreCase = true) ||
                        FreeLance.EstimatedDuration.contains(searchValue, ignoreCase = true) ||
                        FreeLance.Contact.contains(searchValue, ignoreCase = true) ||
                        FreeLance.location.contains(searchValue, ignoreCase = true) ||
                        FreeLance.Status.contains(searchValue,ignoreCase = true)
            }
        }
    }
    LaunchedEffect(Unit){ val FrLance = KtorClient.getFreeLanceList()
        FreeLanceCardList.addAll(FrLance)}

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
                placeholder = { Text("Search FreeLance Opportunities by title, company or loaction") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = {
                    context.startActivity(Intent(context, AddFreelanceWork::class.java))
                }
            ) {
                Text("ADD FREELANCE OPPORTUNITIES")
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "FreeLance Available",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)){
                items(searchList){
                        freelance->
                    FreelanceCard(freelance)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFreeLance() {
    InternshipsScreen(navController = rememberNavController())
}
