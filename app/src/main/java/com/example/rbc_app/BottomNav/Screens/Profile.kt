package com.example.rbc_app.BottomNav.Screens

import KtorClient
import KtorClient.loadImage
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfileScreen(navController = rememberNavController())
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    var showDialog by remember{ mutableStateOf(true) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = remember { AppDatabase.getInstance(context) }
    var showEditDialog by remember { mutableStateOf(false) }
    var userProfile by remember {
        mutableStateOf(
            UserProfile(
                name = "John Doe",
                email = "john.doe@example.com",
                phone = "+1 234 567 8901",
                joinDate = "January 2023",
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                // Profile Header
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    LoadAndDisplayImage("sarthak.jpg")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name and Edit Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile"
                        )
                    }
                }

                Text(
                    text = userProfile.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Profile Details Section
                ProfileSection(title = "Personal Information") {
                    ProfileDetailItem(icon = R.drawable.email, title = "Email", value = userProfile.email)
                    ProfileDetailItem(icon = R.drawable.call, title = "Phone", value = userProfile.phone)
                    ProfileDetailItem(icon = R.drawable.cale, title = "Member Since", value = userProfile.joinDate)
                }
            }

            item {
                // Settings Section
                ProfileSection(title = "Settings") {
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
                // Logout Button
                Button(
                    onClick = {
                        showDialog.value=true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Log Out")
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
                Button(onClick = {
                    onConfirm() // Perform log-out action
                    showDialog.value = false
                }) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("ARE YOU SURE?") },
            text = { Text("YOU WANT TO LOG OUT") }
        )
    }
}

@Composable
fun ProfileSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
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
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (icon) {
            is Int -> Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            else -> Icon(
                imageVector = icon as ImageVector,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Navigate",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}






@Composable
fun LoadAndDisplayImage(imageName: String) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null) {
        val imageData = loadImage(imageName)
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




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(profile: UserProfile, onDismiss: () -> Unit, onSave: (UserProfile) -> Unit) {
    var editedProfile by remember { mutableStateOf(profile) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = editedProfile.name,
                    onValueChange = { editedProfile = editedProfile.copy(name = it) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = editedProfile.email,
                    onValueChange = { editedProfile = editedProfile.copy(email = it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = editedProfile.phone,
                    onValueChange = { editedProfile = editedProfile.copy(phone = it) },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = editedProfile.bio,
                    onValueChange = { editedProfile = editedProfile.copy(bio = it) },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(editedProfile) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
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
    ProfileScreen(navController = rememberNavController())
}