package com.example.rbc_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.rbc_app.FreeLanceFormActivities.FreeLanceFormActivity
import com.example.rbc_app.RoomDatabase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailedFreelance : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF9F9F9)
                ) {
                    val freelanceId = intent.getIntExtra("freelance_id", 0)
                    val freelanceEmpId = intent.getIntExtra("Empid", -1)
                    val freelanceTitle = intent.getStringExtra("freelance_title") ?: ""
                    val freelanceDesc = intent.getStringExtra("freelance_desc") ?: ""
                    val companyName = intent.getStringExtra("freelance_company") ?: ""
                    val companyLogoName = intent.getStringExtra("freelance_comp_logo") ?: ""
                    val freelanceType = intent.getStringExtra("freelance_type") ?: ""
                    val location = intent.getStringExtra("location") ?: ""
                    val preferredCandType = intent.getStringExtra("preferredCandType") ?: ""
                    val status = intent.getStringExtra("Status") ?: ""
                    val minSalary = intent.getStringExtra("min_salary") ?: ""
                    val maxSalary = intent.getStringExtra("max_salary") ?: ""

                    FreelancePostingApp(
                        freelanceId = freelanceId,
                        freelanceEmpId = freelanceEmpId,
                        freelanceTitle = freelanceTitle,
                        freelanceDesc = freelanceDesc,
                        companyName = companyName,
                        companyLogoName = companyLogoName,
                        freelanceType = freelanceType,
                        location = location,
                        preferredCandType = preferredCandType,
                        status = status,
                        minSalary = minSalary,
                        maxSalary = maxSalary
                    )
                }
            }
        }
    }
}

@Composable
fun LoadAndDisplayImage(
    imageName: String,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageName)
            .build()
    )

    Image(
        painter = painter,
        contentDescription = "Company Logo",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun FreelancePostingApp(
    freelanceId: Int,
    freelanceEmpId: Int,
    freelanceTitle: String,
    freelanceDesc: String,
    companyName: String,
    companyLogoName: String,
    freelanceType: String,
    location: String,
    preferredCandType: String,
    status: String,
    minSalary: String,
    maxSalary: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        FreelanceDetailHeader(
            freelanceTitle = freelanceTitle,
            companyName = companyName,
            preferredCandType = preferredCandType,
            companyLogoName = companyLogoName,
            location = location,
            status = status,
            freelanceId = freelanceId,
            minSalary = minSalary,
            maxSalary = maxSalary
        )

        FreelanceDescriptionSection(
            freelanceDesc = freelanceDesc,
            freelanceEmpId = freelanceEmpId,
            freelanceType = freelanceType,
            preferredCandType = preferredCandType,
            freelanceId = freelanceId
        )
    }
}

@Composable
fun FreelanceDetailHeader(
    freelanceTitle: String,
    companyName: String,
    preferredCandType: String,
    companyLogoName: String,
    location: String,
    status: String,
    freelanceId: Int,
    minSalary: String,
    maxSalary: String
) {
    val context = LocalContext.current
    val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val savedFreelance = AppDatabase.getInstance(context).UserDao().getSavedFreelance()
            val savedFreelanceList = savedFreelance.split(",").filter { it.isNotEmpty() }
            isSaved = savedFreelanceList.contains(freelanceId.toString())
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
                    text = freelanceTitle,
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
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Location",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF777777)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = location,
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

            // Status and budget row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (status == "Open") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = status,
                        fontSize = 14.sp,
                        color = if (status == "Open") Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFF8E1))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "₹$minSalary - ₹$maxSalary",
                        fontSize = 14.sp,
                        color = Color(0xFFF57F17),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save and candidate type section
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
                            val savedFreelance = AppDatabase.getInstance(context).UserDao().getSavedFreelance()
                            if (!isSaved) {
                                val updatedFreelance = if (savedFreelance.isEmpty()) {
                                    freelanceId.toString()
                                } else {
                                    "$savedFreelance,$freelanceId"
                                }
                                AppDatabase.getInstance(context).UserDao()
                                    .updateSavedFreelance(updatedFreelance)
                                isSaved = true
                            } else {
                                val updatedFreelance = savedFreelance.split(",")
                                    .filter { it != freelanceId.toString() }
                                    .joinToString(",")
                                AppDatabase.getInstance(context).UserDao()
                                    .updateSavedFreelance(updatedFreelance)
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

@Composable
fun FreelanceDescriptionSection(
    freelanceDesc: String,
    freelanceEmpId: Int,
    freelanceType: String,
    preferredCandType: String,
    freelanceId: Int
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
            Text(
                text = "Project Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "About the Project",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF444444),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = freelanceDesc,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Requirements",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF444444),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFFDE7))
                    .padding(12.dp)
            ) {
                RequirementItem("Freelance Type", freelanceType)
                RequirementItem("Preferred Candidate", preferredCandType)
                RequirementItem("Project ID", freelanceId.toString())
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val intent = Intent(context, FreeLanceFormActivity::class.java)
                    intent.putExtra("freelance_id", freelanceId)
                    intent.putExtra("Empid", freelanceEmpId)
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
                    text = "Apply Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RequirementItem(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Color(0xFFFFC107), RoundedCornerShape(50))
                .align(Alignment.CenterVertically)
                .padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color(0xFF777777),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color(0xFF333333),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFreelancePostingApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF9F9F9)
        ) {
            FreelancePostingApp(
                freelanceId = 222,
                freelanceEmpId = 88,
                freelanceTitle = "UI/UX Design Project",
                freelanceDesc = "We're looking for a skilled UI/UX designer to create a mobile app interface...",
                companyName = "DesignHub Inc",
                companyLogoName = "designhub_logo.png",
                freelanceType = "Project-based",
                location = "Remote",
                preferredCandType = "2+ years experience",
                status = "Open",
                minSalary = "15000",
                maxSalary = "25000"
            )
        }
    }
}