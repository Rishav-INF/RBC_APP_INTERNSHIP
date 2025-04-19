package com.example.rbc_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.room.Room
import com.example.rbc_app.BottomNav.BottomNavigationActivity
import com.example.rbc_app.BottomNav.Screens.Home
import com.example.rbc_app.RoomDatabase.AppDatabase
import com.example.rbc_app.RoomDatabase.UserCache
import com.example.rbc_app.SignUp.signUpEmployer
import com.example.rbc_app.SignUp.signUpSeeker
import com.example.rbc_app.login.LoginEmployer
import com.example.rbc_app.login.LoginSeeker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "rbc"
        ).build()
        GlobalScope.launch {
            val userDao = db.UserDao()
            val cnt = userDao.chk()
            if(cnt==1){
                startActivity(Intent(applicationContext,BottomNavigationActivity::class.java))
            }
           }

        setContent {
            SimpleAppScreen()
        }
    }
}

@Composable
fun SimpleAppScreen() {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("") }
    val options = listOf("Career Aspirant", "Employer")

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image only (no overlay)
        Spacer(Modifier.height(30.dp))
        Image(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(30.dp))
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(70.dp))
            // Title


            // Selection options - now with solid white background
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOption = option }
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedOption == option),
                            onClick = { selectedOption = option },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Blue,
                                unselectedColor = Color.Gray
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (option == "Career Aspirant") "A Career Aspirant" else "An Employer",
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Login Button - solid blue
            Button(
                onClick = {
                    if (selectedOption.isNotEmpty()) {
                        when (selectedOption) {
                            "Career Aspirant" -> context.startActivity(Intent(context, LoginSeeker::class.java))
                            "Employer" -> context.startActivity(Intent(context, LoginEmployer::class.java))
                        }
                    } else {
                        Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text("LOGIN", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))

            // Signup Button - white with blue border
            OutlinedButton(
                onClick = {
                    if (selectedOption.isNotEmpty()) {
                        when (selectedOption) {
                            "Career Aspirant" -> context.startActivity(Intent(context, signUpSeeker::class.java))
                            "Employer" -> context.startActivity(Intent(context, signUpEmployer::class.java))
                        }
                    } else {
                        Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp
                )
            ) {
                Text("SIGN UP NOW", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSimpleAppScreen() {
    SimpleAppScreen()
}