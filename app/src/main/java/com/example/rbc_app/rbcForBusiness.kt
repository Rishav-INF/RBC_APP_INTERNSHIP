package com.example.rbc_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

class RbcContactInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactInfoScreen()
        }
    }
}

@Composable
fun ContactInfoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Logo (faded)
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(vertical = 24.dp),
            contentScale = ContentScale.Fit,
            alpha = 0.1f
        )

        // ABOUT US
        Text(
            text = "ABOUT US",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFC107)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We believe that we are your best placement Consultants agencies services in Delhi, Gurgaon because our team has diverse skills. When you deal with us.",
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // WORKING HOURS
        Text(
            text = "WORKING HOURS",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFC107)
        )
        Spacer(modifier = Modifier.height(8.dp))
        WorkingHours("Monday - Friday", "10:00 - 19:00")
        WorkingHours("Saturday", "10:00 - 14:00")
        WorkingHours("Sunday and holidays", "10:00 - 12:00")

        Spacer(modifier = Modifier.height(24.dp))

        // SERVICES
        Text(
            text = "SERVICES",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFC107)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ServicesList()
    }
}


@Composable
fun WorkingHours(day: String, time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = day, color = Color.White, fontSize = 14.sp)
        Text(text = time, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun ServicesList() {
    val services = listOf(
        "Placement services",
        "HR Services",
        "Manpower Recruitment Services",
        "Career Consultant Services",
        "Head Hunting Services"
    )
    services.forEach {
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(text = "➤", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = it, color = Color.White, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun pre(){
    ContactInfoScreen()
}

