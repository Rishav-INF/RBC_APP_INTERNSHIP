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
import com.airbnb.lottie.compose.*
import com.example.rbc_app.R
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.example.rbc_app.MentorshipsActivity
import com.example.rbc_app.MoreOppActivity
import com.example.rbc_app.ProMembershipActivity
import com.example.rbc_app.RbcContactInfoActivity

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFFFFD700), // Gold/yellow
                    onPrimary = Color.Black,
                    secondary = Color(0xFFFFFACD), // Lemon chiffon
                    background = Color(0xFFFFF8E1), // Light yellowish white
                    surface = Color.White,
                    onSurface = Color.Black
                )
            ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerOpportunitiesScreen(navController: NavController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header with floating animation
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            LottieAnimation(
                resId = R.raw.animationjson,
                modifier = Modifier.size(48.dp),
                iterations = LottieConstants.IterateForever
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Unlock Your Career",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }

        // Search bar with subtle animation
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LottieAnimation(
                        resId = R.raw.search, // Replace with your search animation
                        modifier = Modifier.size(20.dp),
                        iterations = LottieConstants.IterateForever
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Search Opportunities")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color(0xFFFFECB3) // Light yellow
            )
        )

        // First row of cards with enhanced animations
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OpportunityCard(
                title = "Internships",
                items = listOf("Gain", "Practical", "Experience"),
                modifier = Modifier
                    .weight(1f)
                    .clickable { context.startActivity(Intent(context, Internships::class.java)) },
                animationRes = R.raw.internshipjson,
                animationSize = 100.dp
            )

            Spacer(modifier = Modifier.width(16.dp))

            OpportunityCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable { context.startActivity(Intent(context, JobsActivity::class.java)) },
                title = "Jobs",
                items = listOf("Explore", "Diverse Careers"),
                animationRes = R.raw.animationjson,
                animationSize = 100.dp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mentorships card with floating animation
        OpportunityCard(
            title = "Mentorships",
            items = listOf("Get mentored for", "Excellence"),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { context.startActivity(Intent(context, MentorshipsActivity::class.java)) },
            animationRes = R.raw.mentorship,
            animationSize = 120.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // More Opportunities card with animated arrow
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF3E0) // Very light yellow
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "More Opportunities",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { context.startActivity(Intent(context, MoreOppActivity::class.java)) }
                    )
                    Text(
                        "Discover additional programs",
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                }
                LottieAnimation(
                    resId = R.raw.moreopp, // Replace with your arrow animation
                    modifier = Modifier.size(60.dp),
                    iterations = LottieConstants.IterateForever
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pro Membership banner with premium animation
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFD700) // Gold/yellow
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "RBC Pro Membership",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { context.startActivity(Intent(context, ProMembershipActivity::class.java)) }
                    )
                    Text(
                        text = "Unlock premium features",
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }
                LottieAnimation(
                    resId = R.raw.premium, // Replace with premium animation
                    modifier = Modifier.size(80.dp),
                    iterations = LottieConstants.IterateForever
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Business banner with professional animation
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFECB3) // Light yellow
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "RBC For Business",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { context.startActivity(Intent(context, RbcContactInfoActivity::class.java)) }
                    )
                    Text(
                        "Solutions for your organization",
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                }
                LottieAnimation(
                    resId = R.raw.business, // Replace with business animation
                    modifier = Modifier.size(80.dp),
                    iterations = LottieConstants.IterateForever
                )
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
    animationSize: Dp = 100.dp
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text Content
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                when (title) {
                    "Jobs" -> Text(
                        text = "Explore Diverse Careers",
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    "Internships" -> Text(
                        text = "Gain Practical Experience",
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    else -> {
                        Column {
                            items.forEach { item ->
                                Text(
                                    text = item,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Animation (placed below text)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.size(animationSize),
                contentAlignment = Alignment.Center
            ) {
                if (animationRes != null) {
                    LottieAnimation(
                        resId = animationRes,
                        modifier = Modifier.fillMaxSize(),
                        iterations = LottieConstants.IterateForever
                    )
                }
            }
        }
    }
}

@Composable
fun LottieAnimation(
    resId: Int,
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = iterations
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
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFFFD700),
            onPrimary = Color.Black,
            secondary = Color(0xFFFFFACD),
            background = Color(0xFFFFF8E1),
            surface = Color.White,
            onSurface = Color.Black
        )
    ) {
        CareerOpportunitiesScreen(navController = rememberNavController())
    }
}