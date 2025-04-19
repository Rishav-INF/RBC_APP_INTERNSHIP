package com.example.rbc_app.BottomNav.Screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*
import com.example.rbc_app.R
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.clickable

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CareerOpportunitiesScreen(navController = rememberNavController())
                }
            }
        }
    }
}

@Composable
fun CareerOpportunitiesScreen(navController: NavController) {
    val context= LocalContext.current
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
            label = { Text("🔍 Search Opportunities") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        // First row of cards
        Row(
            modifier = Modifier.fillMaxWidth().clickable{

            },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OpportunityCard(
                title = "Internships",
                items = listOf("Gain", "Practical", "Experience"),
                modifier = Modifier.weight(1f).clickable { context.startActivity(Intent(context,Internships::class.java)) },
                animationRes = R.raw.internshipjson
            )

            Spacer(modifier = Modifier.width(16.dp))


        }

        Spacer(modifier = Modifier.height(16.dp))

        // Second row of cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OpportunityCard(
                modifier =Modifier.weight(1f).clickable { context.startActivity(Intent(context,JobsActivity::class.java)) },
                title = "Jobs",
                items = listOf("Explore", "Diverse Careers"),
                animationRes = R.raw.animationjson
            )

            Spacer(modifier = Modifier.width(16.dp))

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Competitions card (full width)
        OpportunityCard(
            title = "Mentorships",
            items = listOf("Get mentored for", "Excellence"),
            modifier = Modifier.fillMaxWidth(),
            animationRes = R.raw.mentorship
        )

        Spacer(modifier = Modifier.height(16.dp))

        // More card (full width)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "More Opportunities",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Discover additional programs")
                }
                LottieAnimation(R.raw.animationjson, modifier = Modifier.size(80.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pro banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "RBC Pro Membership",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Unlock premium features",
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                LottieAnimation(
                    resId = R.raw.animationjson,
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Business banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF03DAC6))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "RBC For Business",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Solutions for your organization")
                }
                LottieAnimation(R.raw.animationjson, modifier = Modifier.size(80.dp))
            }
        }
    }
}

@Composable
fun OpportunityCard(
    title: String,
    items: List<String>,
    modifier: Modifier = Modifier,
    animationRes: Int? = null,
    gifRes: Int? = null
) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
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

            // Animation/GIF on the right side
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    animationRes != null -> {
                        LottieAnimation(resId = animationRes)
                    }
                    gifRes != null -> {
                        LottieAnimation(resId = gifRes)
                    }
                    else -> {
                        Image(
                            painter = painterResource(R.drawable.home),
                            contentDescription = title,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LottieAnimation(
    resId: Int,
    modifier: Modifier = Modifier.size(80.dp)
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewCareerOpportunitiesScreen() {
    MaterialTheme {
        CareerOpportunitiesScreen(navController = rememberNavController())
    }
}