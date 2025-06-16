package com.example.rbc_app.BottomNav.Screens

import KtorClient
import KtorClient.loadImage
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.GlideImage
import com.example.rbc_app.MainActivity
import com.example.rbc_app.R
import com.example.rbc_app.RoomDatabase.AppDatabase
import com.example.rbc_app.RoomDatabase.UserCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Professional Color Palette
object ProfileColors {
    val PrimaryYellow = Color(0xFFF5C842)
    val LightYellow = Color(0xFFFFF8E1)
    val DarkYellow = Color(0xFFE6B800)
    val CreamWhite = Color(0xFFFFFDF7)
    val WarmWhite = Color(0xFFFAF9F7)
    val SoftGray = Color(0xFF8A8A8A)
    val DarkGray = Color(0xFF2C2C2C)
    val AccentGray = Color(0xFFF5F5F5)
    val BorderGray = Color(0xFFE0E0E0)
}

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shrd = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("Type","def")
//        if(shrd=="Employer") {
//            CoroutineScope(Dispatchers.IO).launch{
//                val appdb = AppDatabase.getInstance(this@ProfileActivity).UserDao().getUid()
//            }
//            val userProf = KtorClient.getEmployerForProf()
//        }
//        if(shrd=="Seeker") {
//            CoroutineScope(Dispatchers.IO).launch{
//                val appdb = AppDatabase.getInstance(this@ProfileActivity).UserDao().getUid()
//            }
//            val userProf = KtorClient.getSeekerForProf()
//        }

        setContent {
            ProfileScreen(navController = rememberNavController(),shrd)
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController,shrd :String?) {
    var showDialog by remember{ mutableStateOf(true) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = remember { AppDatabase.getInstance(context) }
    var showEditDialog by remember { mutableStateOf(false) }
    var userProfile by remember {
        mutableStateOf(
            UserProfile(
                name = "Rishav Rana",
                email = "rishav@example.com",
                phone = "+91 1234567890",
                joinDate = "January 2024",
                bio = "Software Developer | Android Enthusiast"
            )
        )
    }

    if (showEditDialog) {
        EditProfileDialog(
            profile = userProfile,
            onDismiss = { showEditDialog = false },
            onSave = { updatedProfile ->
                userProfile = updatedProfile
                showEditDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ProfileColors.LightYellow,
                        ProfileColors.CreamWhite,
                        ProfileColors.WarmWhite
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))

                // Professional Header Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ProfileColors.WarmWhite
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        // Profile Image with Professional Border
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(
                                    4.dp,
                                    Brush.linearGradient(
                                        colors = listOf(
                                            ProfileColors.PrimaryYellow,
                                            ProfileColors.DarkYellow
                                        )
                                    ),
                                    CircleShape
                                )
                                .background(ProfileColors.AccentGray)
                        ) {
                            LoadAndDisplayImage("sarthak.jpg")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Name and Edit Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = userProfile.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = ProfileColors.DarkGray
                            )
                            IconButton(
                                onClick = { showEditDialog = true },
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .background(
                                        ProfileColors.LightYellow,
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = ProfileColors.DarkYellow
                                )
                            }
                        }

                        Text(
                            text = userProfile.bio,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ProfileColors.SoftGray,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        Text(
                            text = shrd!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ProfileColors.SoftGray,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Profile Details Section
                ProfessionalProfileSection(title = "Personal Information") {
                    ProfileDetailItem(icon = R.drawable.email, title = "Email", value = userProfile.email)
                    ProfileDetailItem(icon = R.drawable.call, title = "Phone", value = userProfile.phone)
                    ProfileDetailItem(icon = R.drawable.cale, title = "Member Since", value = userProfile.joinDate)
                }
            }

            item {
                // Settings Section
                ProfessionalProfileSection(title = "Settings") {
                    ProfileActionItem(
                        icon = Icons.Default.Settings,
                        title = "Account Settings",
                        onClick = { /* Navigate to settings */ }
                    )
                    ProfileActionItem(
                        icon = R.drawable.internships,
                        title = "Privacy",
                        onClick = { /* Navigate to privacy */ }
                    )
                    ProfileActionItem(
                        icon = R.drawable.ic_launcher_foreground,
                        title = "Notifications",
                        onClick = { /* Navigate to notifications */ }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                val showDialog=remember{ mutableStateOf(false) }

                // Professional Logout Button
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ProfileColors.WarmWhite
                    )
                ) {
                    Button(
                        onClick = {
                            showDialog.value=true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFEBEE),
                            contentColor = Color(0xFFD32F2F)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Log Out",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                LogOutDialog(
                    showDialog=showDialog,
                    onConfirm = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val curr =  db.UserDao().getAll()
                            for(user in curr){
                                db.UserDao().delete(user)
                            }
                            withContext(Dispatchers.Main) {
                                context.startActivity(
                                    Intent(
                                        context,
                                        MainActivity::class.java
                                    )
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogOutDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                        showDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F),
                        contentColor = Color.White
                    )
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProfileColors.LightYellow,
                        contentColor = ProfileColors.DarkGray
                    )
                ) {
                    Text("Cancel")
                }
            },
            title = {
                Text(
                    "ARE YOU SURE?",
                    color = ProfileColors.DarkGray,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "YOU WANT TO LOG OUT",
                    color = ProfileColors.SoftGray
                )
            },
            containerColor = ProfileColors.WarmWhite
        )
    }
}

@Composable
fun ProfessionalProfileSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = ProfileColors.DarkGray,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(6.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = ProfileColors.WarmWhite
            )
        ) {
            Column {
                content()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProfileDetailItem(icon: Int, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    ProfileColors.LightYellow,
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(20.dp),
                tint = ProfileColors.DarkYellow
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = ProfileColors.SoftGray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = ProfileColors.DarkGray,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ProfileActionItem(icon: Any, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    ProfileColors.LightYellow,
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            when (icon) {
                is Int -> Icon(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(20.dp),
                    tint = ProfileColors.DarkYellow
                )
                else -> Icon(
                    imageVector = icon as ImageVector,
                    contentDescription = title,
                    modifier = Modifier.size(20.dp),
                    tint = ProfileColors.DarkYellow
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = ProfileColors.DarkGray,
            fontWeight = FontWeight.Medium
        )
        Icon(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Navigate",
            tint = ProfileColors.SoftGray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun LoadAndDisplayImage(imageName: String) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null) {
        val imageData = loadImage(imageName)
        Log.d("LoadImageDebug", "Image Data: ${imageData?.size ?: "null"} bytes")
        value = imageData?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
                .clickable { /* Handle profile picture change */ },
            contentScale = ContentScale.Crop
        )
    } ?: Box(
        modifier = Modifier
            .size(112.dp)
            .clip(CircleShape)
            .background(ProfileColors.AccentGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Rishav...",
            color = ProfileColors.SoftGray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(profile: UserProfile, onDismiss: () -> Unit, onSave: (UserProfile) -> Unit) {
    var editedProfile by remember { mutableStateOf(profile) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Edit Profile",
                fontWeight = FontWeight.Bold,
                color = ProfileColors.DarkGray
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = editedProfile.name,
                    onValueChange = { editedProfile = editedProfile.copy(name = it) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ProfileColors.PrimaryYellow,
                        focusedLabelColor = ProfileColors.DarkYellow
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = editedProfile.email,
                    onValueChange = { editedProfile = editedProfile.copy(email = it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ProfileColors.PrimaryYellow,
                        focusedLabelColor = ProfileColors.DarkYellow
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = editedProfile.phone,
                    onValueChange = { editedProfile = editedProfile.copy(phone = it) },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ProfileColors.PrimaryYellow,
                        focusedLabelColor = ProfileColors.DarkYellow
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = editedProfile.bio,
                    onValueChange = { editedProfile = editedProfile.copy(bio = it) },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ProfileColors.PrimaryYellow,
                        focusedLabelColor = ProfileColors.DarkYellow
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(editedProfile) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProfileColors.PrimaryYellow,
                    contentColor = ProfileColors.DarkGray
                )
            ) {
                Text("Save", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ProfileColors.SoftGray
                )
            ) {
                Text("Cancel")
            }
        },
        containerColor = ProfileColors.WarmWhite
    )
}

data class UserProfile(
    val name: String,
    val email: String,
    val phone: String,
    val joinDate: String,
    val bio: String
)

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen(navController = rememberNavController(),"def")
}