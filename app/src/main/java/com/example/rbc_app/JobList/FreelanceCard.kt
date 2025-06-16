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
import com.example.rbc_app.DetailedFreelance
import com.example.rbc_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FreelanceCard(freeLanceInfo: KtorClient.FreeLanceCard) {
    val context = LocalContext.current

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
            // Freelancer required badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Freelance Opportunity",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Job title (using freelance type as title)
            Text(
                text = freeLanceInfo.freelance_type,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Company name with image
            Row(verticalAlignment = Alignment.CenterVertically) {
                LoadAndDisplayImageFr(freeLanceInfo.freelance_comp_logo)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = freeLanceInfo.companyName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact information
            JobDetailRow(
                imageRes = R.drawable.internships, // Replace with your contact icon
                text = "Contact: ${freeLanceInfo.Contact}"
            )

            // Payment information
            JobDetailRow(
                imageRes = R.drawable.rupees_logo,
                text = "Pay: ${freeLanceInfo.Pay}"
            )

            // Location row
            JobDetailRow(
                imageRes = R.drawable.location,
                text = freeLanceInfo.location
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Estimated duration
            JobDetailRow(
                imageRes = R.drawable.cale, // Replace with your clock icon
                text = "Duration: ${freeLanceInfo.EstimatedDuration}"
            )

            // Preferred candidate type
            JobDetailRow(
                imageRes = R.drawable.internships,
                text = freeLanceInfo.preferredCandType
            )

            // Status
            JobDetailRow(
                imageRes = R.drawable.location, // Replace with your status icon
                text = "Status: ${freeLanceInfo.Status}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Apply section
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
                        painter = painterResource(R.drawable.home),
                        contentDescription = "Freelance",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            context.startActivity(Intent(context, DetailedFreelance::class.java).apply {
                                putExtra("freelance_info", freeLanceInfo.toString()) // Or pass individual fields
                                CoroutineScope(Dispatchers.IO).launch {
                                    val intent = Intent(context, DetailedFreelance::class.java).apply {
                                        putExtra("freelance_id",KtorClient.getFreelanceIdfromcreator(freeLanceInfo.creator))
                                        Log.d("putIntent",KtorClient.getFreelanceIdfromcreator(freeLanceInfo.creator).toString())
                                        putExtra("created_at", freeLanceInfo.created_at)
                                        putExtra("Empid",KtorClient.getEmpIdfromcreator(freeLanceInfo.creator).toInt())
                                        Log.d("EmpId_freelance",KtorClient.getEmpIdfromcreator(freeLanceInfo.creator).toString())

                                        putExtra("Contact", freeLanceInfo.Contact)
                                        putExtra("Pay", freeLanceInfo.Pay)
                                        putExtra("EstimatedDuration", freeLanceInfo.EstimatedDuration)
                                        putExtra("Status", freeLanceInfo.Status)
                                        putExtra("location", freeLanceInfo.location)
                                        putExtra("companyName", freeLanceInfo.companyName)
                                        putExtra("freelance_comp_logo", freeLanceInfo.freelance_comp_logo)
                                        putExtra("freelance_type", freeLanceInfo.freelance_type)
                                        putExtra("preferredCandType", freeLanceInfo.preferredCandType)
                                        putExtra("creator", freeLanceInfo.creator)
                                    }
                                    context.startActivity(intent)
                                }
                            })
                        }
                    ) {
                        Text("APPLY NOW")
                    }
                }
            }
        }
    }
}

@Composable
fun LoadAndDisplayImageFr(imageName: String) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null) {
        val imageData = KtorClient.loadImageFreeLance(imageName)
        Log.d("LoadImageDebug", "Image Data: ${imageData?.size ?: "null"} bytes")
        value = imageData?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Company logo",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { /* Handle logo click */ },
            contentScale = ContentScale.Crop
        )
    } ?: Text("Loading Image...")
}

@Preview(showBackground = true)
@Composable
fun FreelancePostingCardPreview() {
    MaterialTheme {
        val freelance = KtorClient.FreeLanceCard(
            created_at = "2023-05-20",
            Contact = "hr@company.com",
            Pay = "₹15,000 - ₹25,000",
            EstimatedDuration = "3 months",
            Status = "Open",
            location = "Remote",
            companyName = "DesignHub Inc",
            freelance_comp_logo = "designhub_logo.png",
            freelance_type = "UI/UX Design",
            preferredCandType = "2+ years experience",
            creator = "user123"
        )
        FreelanceCard(freelance)
    }
}