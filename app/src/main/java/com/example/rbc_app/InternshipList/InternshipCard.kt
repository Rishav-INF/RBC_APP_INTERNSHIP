package com.example.rbc_app.InternshipList

import KtorClient
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.example.rbc_app.detailedInternshipActivity

@Composable
fun InternshipPostingCard(internInfo: KtorClient.internshipforCard) {
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
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Internship Opportunity",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = internInfo.Name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

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

            JobDetailRow(
                imageRes = R.drawable.location,
                text = "Location: ${internInfo.internshipLocation}"
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            JobDetailRow(
                imageRes = R.drawable.cale,
                text = "Type: ${internInfo.internshipType}"
            )

            JobDetailRow(
                imageRes = R.drawable.internships,
                text = "Preferred: ${internInfo.preferredCandType}"
            )

            JobDetailRow(
                imageRes = R.drawable.location,
                text = "Status: ${internInfo.internshipStatus}"
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                        contentDescription = "Internship",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val intent = Intent(context, detailedInternshipActivity::class.java).apply {
                                putExtra("internship_id", internInfo.internship_id)
                                putExtra("Name", internInfo.Name)
                                putExtra("internshipDesc", internInfo.internshipDesc)
                                putExtra("companyName", internInfo.companyName)
                                putExtra("companyLogoName", internInfo.companyLogoName)
                                putExtra("internshipType", internInfo.internshipType)
                                putExtra("internshipLocation", internInfo.internshipLocation)
                                putExtra("preferredCandType", internInfo.preferredCandType)
                                putExtra("internshipStatus", internInfo.internshipStatus)
                            }
                            context.startActivity(intent)
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
    val bitmap by produceState<Bitmap?>(initialValue = null) {
        val imageData = KtorClient.loadImageInternships(imageName)
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
                .clickable { },
            contentScale = ContentScale.Crop
        )
    } ?: Text("Loading Image...")
}

@Preview(showBackground = true)
@Composable
fun InternshipPostingCardPreview() {
    MaterialTheme {
        val internship = KtorClient.internshipforCard(
            internship_id = 101,
            Name = "Android Developer Intern",
            internshipDesc = "Work on building cool Android apps.",
            companyName = "TechNova",
            companyLogoName = "technova_logo.png",
            internshipType = "Remote",
            internshipLocation = "Remote",
            preferredCandType = "CS Students",
            internshipStatus = "Open"
        )
        InternshipPostingCard(internship)
    }
}
