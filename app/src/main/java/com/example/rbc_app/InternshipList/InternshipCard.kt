package com.example.rbc_app.InternshipList

import KtorClient
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
import com.example.rbc_app.R

@Composable
fun InternshipPostingCard(internInfo : KtorClient.internshipforCard) {
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
                    text = "Urgently hiring",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Job title
            Text(
                text = internInfo.Name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Company name with image
            Row(verticalAlignment = Alignment.CenterVertically) {
                LoadAndDisplayImage(internInfo.companyLogoName)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = internInfo.companyName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location row with image
            JobDetailRow(
                imageRes = R.drawable.location, // Replace with your image resource
                text = internInfo.internshipLocation
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
                text = internInfo.internshipType
            )


            // Experience level row with image
            JobDetailRow(
                imageRes = R.drawable.internships, // Replace with your image resource
                text = internInfo.preferredCandType
            )

            JobDetailRow(
                imageRes = R.drawable.internships, // Replace with your image resource
                text = internInfo.internshipStatus
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Apply Now",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
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
fun LoadAndDisplayImage(imageName: String) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null) {
        val imageData = KtorClient.loadImageInternships(imageName)
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
        val internship = KtorClient.internshipforCard(
            Name = "Android Developer",
            internshipDesc = "Looking for a skilled Android developer with experience in Kotlin and Jetpack Compose.",
            companyName = "CodeKriti Tech",
            companyLogoName = "codekriti_logo.png",
            internshipType = "Full-time",
            internshipLocation = "Bangalore",
            preferredCandType = "Experienced",
            internshipStatus = "Open"
        )
        InternshipPostingCard(internship)
    }
}
