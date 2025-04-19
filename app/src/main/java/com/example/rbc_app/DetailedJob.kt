package com.example.rbc_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.rbc_app.JobFormActivities.jobFormActivity
import com.example.rbc_app.JobList.LoadAndDisplayImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailedJob : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val jobId = intent.getIntExtra("job_id",-1)
            val jobName = intent.getStringExtra("jobName") ?: ""
            val jobDesc = intent.getStringExtra("jobDesc") ?: ""
            val companyName = intent.getStringExtra("companyName") ?: ""
            val companyLogoName = intent.getStringExtra("companyLogoName") ?: ""
            val jobType = intent.getStringExtra("jobType") ?: ""
            val jobLocation = intent.getStringExtra("jobLocation") ?: ""
            val preferredCandType = intent.getStringExtra("preferredCandType") ?: ""
            val jobStatus = intent.getStringExtra("jobStatus") ?: ""

            JobPostingApp(
                jobId = jobId.toInt(),
                jobName = jobName,
                jobDesc = jobDesc,
                companyName = companyName,
                companyLogoName = companyLogoName,
                jobType = jobType,
                jobLocation = jobLocation,
                preferredCandType = preferredCandType,
                jobStatus = jobStatus
            )
        }
    }
}

@Composable
fun JobPostingApp(
    jobId: Int,
    jobName: String,
    jobDesc: String,
    companyName: String,
    companyLogoName: String,
    jobType: String,
    jobLocation: String,
    preferredCandType: String,
    jobStatus: String
) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                JobDetailHeader(
                    jobName = jobName,
                    companyName = companyName,
                    preferredCandType= preferredCandType,
                    companyLogoName = companyLogoName,
                    jobLocation = jobLocation,
                    jobStatus = jobStatus
                )

                Divider(color = Color.LightGray, thickness = 8.dp)

                JobDescriptionSection(
                    jobDesc = jobDesc,
                    jobType = jobType,
                    preferredCandType = preferredCandType,
                    jobId = jobId
                )
            }
        }
    }
}

@Composable
fun JobDetailHeader(
    jobName: String,
    companyName: String,
    preferredCandType:String,
    companyLogoName: String,
    jobLocation: String,
    jobStatus: String
) {
    val context = LocalContext.current
    val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())

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
                text = jobName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Company info
        Text(
            text = companyName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = jobLocation,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = "Status: $jobStatus",
            fontSize = 14.sp,
            color = if (jobStatus == "Active") Color.Green else Color.Red
        )
        Text(
            text = "Updated On: $currentDate",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Divider(color = Color.LightGray)

        // Quick Apply section
        Text(
            text = "Quick Apply",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Company Logo",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                LoadAndDisplayImage(companyLogoName)
            }

            Column {
                Text(
                    text = "Candidate Type",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "For $preferredCandType",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Blue
                )
            }
        }
    }
}

@Composable
fun JobDescriptionSection(
    jobDesc: String,
    jobType: String,
    preferredCandType: String,
    jobId: Int
) {
    var context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TabItem("Job Description", true)
            TabItem("Dates & Deadlines", false)
            TabItem("Reviews", false)
        }

        Text(
            text = "Details",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // About the Role
        Text(
            text = "About the Role:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = jobDesc,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Requirements
        Text(
            text = "Requirements:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        BulletPoint("Job Type: $jobType")
        BulletPoint("Preferred Candidate: $preferredCandType")
        BulletPoint("Job ID: $jobId")
        Log.d("JOB_ID FOR NEW","$jobId")
        // Quick Apply button
        Button(
            onClick = {

                    val intent = Intent(context, jobFormActivity::class.java)
                    intent.putExtra("job_id", jobId)
                    context.startActivity(intent)

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
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

@Composable
fun TabItem(text: String, isSelected: Boolean) {
    val backgroundColor = if (isSelected) Color.LightGray else Color.Transparent
    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(4.dp)
                .background(Color.Black)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJobPostingApp() {
    JobPostingApp(
        jobId = 111,
        jobName = "MEP BIM Modeler",
        jobDesc = "Matrix BIM and Design Solutions LLP is hiring an experienced MEP BIM Modeler...",
        companyName = "Matrix Designs",
        companyLogoName = "matrix_logo.png",
        jobType = "Full-time",
        jobLocation = "Pune",
        preferredCandType = "Experienced Professionals",
        jobStatus = "Active"
    )
}