package com.example.rbc_app.SignUp

import KtorClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rbc_app.Employer
import com.example.rbc_app.MainActivity
import com.example.rbc_app.R
import com.example.rbc_app.login.LoginEmployer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class signUpEmployer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpScreenEmployer()
        }
    }
}

@Composable
fun SignUpScreenEmployer() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var orgEmail by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Male") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var otp by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var companyName by remember { mutableStateOf("") }

    // Check if all required fields are filled
    val allFieldsFilled = remember(firstName, orgEmail, phone, password, confirmPassword, termsAccepted) {
        firstName.isNotBlank() &&
                orgEmail.isNotBlank() &&
                phone.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword &&
                termsAccepted
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Logo and Title in Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sign up as recruiter",
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Company Name
        Text(text = "Company Name", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = companyName,
            onValueChange = { companyName = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // First Name and Last Name
        Text(text = "First Name*", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Last Name", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Divider
        Divider(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        // Organization Email and Phone
        Text(text = "Organisation Email*", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = orgEmail,
            onValueChange = { orgEmail = it },
            label = { Text("Official Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Phone* [+91]", style = MaterialTheme.typography.bodySmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Divider
        Divider(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        // Gender Selection
        Text(text = "Gender*", style = MaterialTheme.typography.bodySmall)
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedGender == "Male",
                    onClick = { selectedGender = "Male" }
                )
                Text(text = "Male*")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedGender == "Female",
                    onClick = { selectedGender = "Female" }
                )
                Text(text = "Female")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedGender == "More Options",
                    onClick = { selectedGender = "More Options" }
                )
                Text(text = "More Options")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Divider
        Divider(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        Text(text = "Password*", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Confirm Password*", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Terms and Conditions Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "All your information is collected, stored and processed as per our data processing guidelines. By signing up on RSB, you agree to our Privacy Policy and Terms of Use",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Already have an account and Send OTP button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Login")
                    }
                },
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, MainActivity::class.java))
                }
            )

            Button(
                onClick = {

                    CoroutineScope(Dispatchers.IO).launch {
                        val employer = Employer(
                            firstName = firstName,
                            lastName = lastName,
                            email = orgEmail,
                            password = password,
                            phone = phone,
                            gender = selectedGender,
                            companyName = companyName,
                            createdAt = LocalDateTime.now()
                        )

                        val response = KtorClient.signUpemployer(employer)
                        if(response.contains("OTP sent successfully")) otpSent=true

                        withContext(Dispatchers.Main) {
                            loginMessage = response
                            Toast.makeText(context, response, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                enabled = allFieldsFilled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (allFieldsFilled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            ) {
                Text("Send OTP", style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (allFieldsFilled) FontWeight.Bold else FontWeight.Normal
                ))
            }
        }

        // OTP Field (Only Appears After Sending OTP)
        if (otpSent) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val employer = Employer(
                            firstName = firstName,
                            lastName = lastName,
                            email = orgEmail,
                            password = password,
                            phone = phone,
                            gender = selectedGender,
                            companyName = companyName,
                            createdAt = LocalDateTime.now()
                        )

                        val response = KtorClient.otpverificationEmployerSignUp(
                            employer = employer,
                            otp = otp
                        )
                        withContext(Dispatchers.Main) {
                            loginMessage = response
                            Toast.makeText(context, "Sign Up successful Login Now !", Toast.LENGTH_SHORT).show()
                            if (response.contains("Sign up successful", ignoreCase = true)) {
                                context.startActivity(Intent(context, LoginEmployer::class.java))
                            }
                        }
                        Log.d("otp veri response", response)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = otp.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (otp.isNotBlank()) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            ) {
                Text("Verify OTP", style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (otp.isNotBlank()) FontWeight.Bold else FontWeight.Normal
                ))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreviewEmployer() {
    SignUpScreenEmployer()
}