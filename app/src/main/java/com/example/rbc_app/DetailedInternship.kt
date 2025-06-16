package com.example.rbc_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.rbc_app.InternshipFormActivities.InternshipFormActivity
import com.example.rbc_app.RoomDatabase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class detailedInternshipActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternshipTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F5F5)
                ) {
                    val internshipId = intent.getIntExtra("internship_id", -1)
                    val name = intent.getStringExtra("Name") ?: ""
                    val internshipDesc = intent.getStringExtra("internshipDesc") ?: ""
                    val companyName = intent.getStringExtra("companyName") ?: ""
                    val companyLogoName = intent.getStringExtra("companyLogoName") ?: ""
                    val internshipType = intent.getStringExtra("internshipType") ?: ""
                    val internshipLocation = intent.getStringExtra("internshipLocation") ?: ""
                    val preferredCandType = intent.getStringExtra("preferredCandType") ?: ""
                    val internshipStatus = intent.getStringExtra("internshipStatus") ?: ""

                    InternshipPostingApp(
                        internshipId = internshipId,
                        name = name,
                        internshipDesc = internshipDesc,
                        companyName = companyName,
                        companyLogoName = companyLogoName,
                        internshipType = internshipType,
                        internshipLocation = internshipLocation,
                        preferredCandType = preferredCandType,
                        internshipStatus = internshipStatus
                    )
                }
            }
        }
    }
}

@Composable
fun InternshipTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF1976D2),
            secondary = Color(0xFFFFC107),
            background = Color(0xFFF5F5F5)
        ),
        content = content
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternshipPostingApp(
    internshipId: Int,
    name: String,
    internshipDesc: String,
    companyName: String,
    companyLogoName: String,
    internshipType: String,
    internshipLocation: String,
    preferredCandType: String,
    internshipStatus: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        InternshipDetailHeader(
            name = name,
            companyName = companyName,
            preferredCandType = preferredCandType,
            companyLogoName = companyLogoName,
            internshipLocation = internshipLocation,
            internshipStatus = internshipStatus,
            internshipId = internshipId
        )

        Spacer(modifier = Modifier.height(8.dp))

        InternshipDescriptionSection(
            internshipDesc = internshipDesc,
            internshipType = internshipType,
            preferredCandType = preferredCandType,
            internshipId = internshipId
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternshipDetailHeader(
    name: String,
    companyName: String,
    preferredCandType: String,
    companyLogoName: String,
    internshipLocation: String,
    internshipStatus: String,
    internshipId: Int
) {
    val context = LocalContext.current
    val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val savedInternships = AppDatabase.getInstance(context).UserDao().getSavedInternships()
            val savedList = savedInternships.split(",").filter { it.isNotEmpty() }
            isSaved = savedList.contains(internshipId.toString())
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with back button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            ContextCompat.startActivity(
                                context,
                                context.packageManager.getLaunchIntentForPackage(context.packageName)!!,
                                null
                            )
                            (context as? ComponentActivity)?.finish()
                        }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }

            // Company info section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = companyName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF555555)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
                            contentDescription = "Location",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF777777)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = internshipLocation,
                            fontSize = 14.sp,
                            color = Color(0xFF777777)
                        )
                    }
                }

                // Company logo
                LoadAndDisplayImage(
                    imageName = companyLogoName,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (internshipStatus == "Open") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = internshipStatus,
                    fontSize = 14.sp,
                    color = if (internshipStatus == "Open") Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save and date section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Updated: $currentDate",
                    fontSize = 12.sp,
                    color = Color(0xFF999999)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        CoroutineScope(Dispatchers.IO).launch {
                            val savedInternships = AppDatabase.getInstance(context).UserDao().getSavedInternships()
                            if (!isSaved) {
                                val updated = if (savedInternships.isEmpty()) {
                                    internshipId.toString()
                                } else {
                                    "$savedInternships,$internshipId"
                                }
                                AppDatabase.getInstance(context).UserDao()
                                    .UpdateSavedInternships(updated)
                                isSaved = true
                            } else {
                                val updated = savedInternships.split(",")
                                    .filter { it != internshipId.toString() }
                                    .joinToString(",")
                                AppDatabase.getInstance(context).UserDao()
                                    .UpdateSavedInternships(updated)
                                isSaved = false
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.CheckCircle else Icons.Filled.AddCircle,
                        contentDescription = "Save",
                        tint = if (isSaved) Color(0xFFFFC107) else Color(0xFF999999)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSaved) "Saved" else "Save",
                        color = if (isSaved) Color(0xFFFFC107) else Color(0xFF999999),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Candidate type chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE3F2FD))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "For $preferredCandType",
                    fontSize = 14.sp,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternshipDescriptionSection(
    internshipDesc: String,
    internshipType: String,
    preferredCandType: String,
    internshipId: Int
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Section title
            Text(
                text = "Internship Description",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // About the Internship
            Text(
                text = "About the Role:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF444444),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = internshipDesc,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Requirements section
            Text(
                text = "Requirements:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF444444),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFFDE7))
                    .padding(12.dp)
            ) {
                RequirementItem("Internship Type", internshipType)
                RequirementItem("Preferred Candidate", preferredCandType)
                RequirementItem("Internship ID", internshipId.toString())
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Apply button
            Button(
                onClick = {
                    val intent = Intent(context, InternshipFormActivity::class.java)
                    intent.putExtra("internship_id", internshipId)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107),
                    contentColor = Color(0xFF333333)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Quick Apply",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

