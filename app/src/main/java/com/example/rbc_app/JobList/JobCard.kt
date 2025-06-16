package com.example.rbc_app.JobList

import KtorClient
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.rbc_app.DetailedJob
import com.example.rbc_app.JobFormActivities.jobFormActivity
import com.example.rbc_app.R
import com.example.rbc_app.RoomDatabase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun JobPostingCard(jobinfo : KtorClient.jobforCard) {
    var context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Urgently hiring badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Urgently Hiring",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Job title
            Text(
                text = jobinfo.jobName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Company name with image
            Row(verticalAlignment = Alignment.CenterVertically) {
                LoadAndDisplayImage(jobinfo.companyLogoName)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = jobinfo.companyName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location row with image
            JobDetailRow(
                imageRes = R.drawable.location, // Replace with your image resource
                text = jobinfo.jobLocation
            )

            // Salary row with image
            JobDetailRow(
                imageRes = R.drawable.rupees_logo, // Replace with your image resource
                text = "₹8,000 - ₹15,000"
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Work type row with image
            JobDetailRow(
                imageRes = R.drawable.internships, // Replace with your image resource
                text = jobinfo.jobType
            )


            // Experience level row with image
            JobDetailRow(
                imageRes = R.drawable.internships, // Replace with your image resource
                text = jobinfo.preferredCandType
            )

            JobDetailRow(
                imageRes = R.drawable.internships, // Replace with your image resource
                text = jobinfo.jobStatus
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Walk-in interview section with image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.home), // Replace with your image resource
                        contentDescription = "Walk-in",
                        modifier = Modifier.size(20.dp)
                    )
                    Log.d("JOB_ID_CARD",jobinfo.job_id.toString())
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                            onClick = {
                                context.startActivity(Intent(context,jobFormActivity::class.java).apply {
                                    putExtra("job_id",jobinfo.job_id)
                                    CoroutineScope(Dispatchers.IO).launch{
//                                        val uid =AppDatabase.getInstance(context).UserDao().getUid()
                                        val intent = Intent(context, DetailedJob::class.java)
                                        Log.d("JOB FOR CARD",jobinfo.job_id.toString())
                                        intent.putExtra("job_id",jobinfo.job_id)
                                        intent.putExtra("jobName",jobinfo.jobName)
                                        intent.putExtra("jobDesc",jobinfo.jobDesc)
                                        intent.putExtra("companyName",jobinfo.jobDesc)
                                        intent.putExtra("companyLogoName",jobinfo.companyLogoName)
                                        intent.putExtra("jobType",jobinfo.jobType)
                                        intent.putExtra("jobLocation",jobinfo.jobLocation)
                                        intent.putExtra("preferredCandType",jobinfo.preferredCandType)
                                        intent.putExtra("jobStatus",jobinfo.jobStatus)

//                                        val job_id:Int,
//                                        val jobName : String,
//                                        val jobDesc : String,
//                                        val companyName : String,
//                                        val companyLogoName : String,
//                                        val jobType : String,
//                                        val jobLocation : String,
//                                        val preferredCandType : String,
//                                        val jobStatus : String

                                        context.startActivity(intent)
                                    }
                                })
                            }
                    ){
                        Text("APPLY NOW ")
                    }
                }
            }
        }
    }
}

@Composable
fun JobDetailRow(imageRes: Int, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun LoadAndDisplayImage(imageName: String, modifier: Modifier = Modifier ) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null) {
        val imageData = KtorClient.loadImageJobs(imageName)
        Log.d("LoadImageDebug", "Image Data: ${imageData?.size ?: "null"} bytes") // Log size
        value = imageData?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { /* Handle profile picture change */ },
            contentScale = ContentScale.Crop
        )
    } ?: Text("Loading Image...")

}

@Preview(showBackground = true)
@Composable
fun JobPostingCardPreview() {
    MaterialTheme {
        val job = KtorClient.jobforCard(
            job_id=1,
            jobName = "Android Developer",
            jobDesc = "Looking for a skilled Android developer with experience in Kotlin and Jetpack Compose.",
            companyName = "CodeKriti Tech",
            companyLogoName = "codekriti_logo.png",
            jobType = "Full-time",
            jobLocation = "Bangalore",
            preferredCandType = "Experienced",
            jobStatus = "Open"
        )
        JobPostingCard(job)
    }
}
