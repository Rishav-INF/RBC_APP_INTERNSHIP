package com.example.rbc_app.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rbc_app.BottomNav.BottomNavigationActivity
import com.example.rbc_app.R
import kotlinx.coroutines.*

class LoginEmployer : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedpref = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        setContent {
            LoginScreen(sharedpref)
        }
    }

    @Composable
    fun LoginScreen(sharedpref:SharedPreferences) {
        val context= LocalContext.current
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var loginMessage by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier.height(100.dp).width(100.dp)
            )
            Text(
                text="Company/Employer",
                fontSize =30.sp
            )
            Spacer(
                modifier = Modifier.height(30.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val type = sharedpref.getString("type","Guest").toString()
                        val response = KtorClient.loginemployer(email,password)
                        withContext(Dispatchers.Main) {
                            loginMessage = response
                            Toast.makeText(this@LoginEmployer, response, Toast.LENGTH_SHORT).show()
                            if(loginMessage.contains("Login successful"))
                                context.startActivity(Intent(context, BottomNavigationActivity::class.java))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (loginMessage.isNotEmpty()) {
                Text(text = loginMessage, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun previewEmployerLogin(){
        val sharedpref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        LoginScreen(sharedpref)
    }

}



