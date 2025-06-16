package com.example.rbc_app

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


class MentorshipsActivity:ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorshipScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorshipScreen() {
    val context = LocalContext.current

    val mentorshipVideos = listOf(
        MentorshipVideo(
            id = "dQw4w9WgXcQ",
            title = "Getting Started in Your Tech Career",
            description = "Essential tips for beginners in the tech industry",
            duration = "12:34"
        ),
        MentorshipVideo(
            id = "9bZkp7q19f0",
            title = "Advanced Android Development",
            description = "Learn modern Android development techniques",
            duration = "23:45"
        ),
        MentorshipVideo(
            id = "JGwWNGJdvx8",
            title = "UI/UX Design Principles",
            description = "Fundamental design principles for developers",
            duration = "18:22"
        ),
        MentorshipVideo(
            id = "kJQP7kiw5Fk",
            title = "Effective Team Collaboration",
            description = "How to work effectively in tech teams",
            duration = "15:10"
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mentorship Videos",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(mentorshipVideos) { video ->
                MentorshipVideoCard(
                    video = video,
                    onClick = {
                        // Open video in YouTube app or browser
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=${video.id}")
                        )
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun MentorshipVideoCard(
    video: MentorshipVideo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column {
            // YouTube thumbnail (using maxresdefault.jpg for highest quality)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = "https://img.youtube.com/vi/${video.id}/maxresdefault.jpg",
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.logomain),
                    error = painterResource(id = R.drawable.logomain)
                )

                // Play button overlay - CORRECTED MODIFIER CHAIN
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(8.dp)
                )
            }

            // Video details
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = video.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = video.duration,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

data class MentorshipVideo(
    val id: String,
    val title: String,
    val description: String,
    val duration: String
)

@Preview(showBackground = true)
@Composable
fun MentorshipScreenPreview() {
    MentorshipScreen()
}
