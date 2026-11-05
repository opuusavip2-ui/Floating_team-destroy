package com.allinworld.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF6500E8)
private val Pale = Color(0xFFF6F7FA)
private val Green = Color(0xFF4CAF50)
private val Orange = Color(0xFFFF9800)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { TeamDestroyApp() } }
    }
}

private enum class Page { ACTIVATION, DASHBOARD, CONNECT_GMAIL }

@Composable
private fun TeamDestroyApp() {
    var page by remember { mutableStateOf(Page.ACTIVATION) }
    var license by remember { mutableStateOf("") }
    var gmail by remember { mutableStateOf("") }
    var oauthNote by remember { mutableStateOf("") }
    var accountCount by remember { mutableIntStateOf(0) }
    var floatingRunning by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    when (page) {
        Page.ACTIVATION -> ActivationScreen(
            license = license,
            onLicenseChange = { license = it },
            onActivate = {
    if (license.trim() == "TEAM-DESTROY-OPU-MASTER-2026") {
        page = Page.DASHBOARD
        message = ""
    } else {
        message = "Invalid License Key!"
},
            message = message
        )
        Page.DASHBOARD -> DashboardScreen(
            accountCount = accountCount,
            running = floatingRunning,
            onConnect = { page = Page.CONNECT_GMAIL },
            onStart = {
                message = "Floating bar ও permission service পরের ধাপে যুক্ত হবে"
            },
            message = message
        )
        Page.CONNECT_GMAIL -> ConnectGmailScreen(
            email = gmail,
            onEmailChange = { gmail = it },
            note = oauthNote,
            onBack = { page = Page.DASHBOARD },
            onSave = {
                if (gmail.contains("@")) {
                    accountCount = maxOf(1, accountCount)
                    oauthNote = "ইমেইল ঠিকানা দেওয়া হয়েছে। নিরাপদ Google OAuth সংযোগ পরের ধাপে যুক্ত হবে।"
                    message = "ডেমো: account form saved"
                    page = Page.DASHBOARD
                } else {
                    oauthNote = "একটি বৈধ Gmail address লিখুন"
                }
            }
        )
    }
}

@Composable
private fun ActivationScreen(
    license: String,
    onLicenseChange: (String) -> Unit,
    onActivate: () -> Unit,
    message: String
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Pale).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("App Activation", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222222))
        Spacer(Modifier.height(12.dp))
        Text("Enter your 3, 7, or 15 days license key to continue.", color = Color.Gray)
        Spacer(Modifier.height(28.dp))
        OutlinedTextField(
            value = license, onValueChange = onLicenseChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter License Key") },
            singleLine = true
        )
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = onActivate,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green),
            shape = RoundedCornerShape(30.dp)
        ) { Text("ACTIVATE APP", fontWeight = FontWeight.Bold) }
        if (message.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(message, color = Color.Gray)
        }
    }
}

@Composable
private fun DashboardScreen(
    accountCount: Int,
    running: Boolean,
    onConnect: () -> Unit,
    onStart: () -> Unit,
    message: String
) {
    Column(Modifier.fillMaxSize().background(Pale).verticalScroll(rememberScrollState())) {
        Column(Modifier.fillMaxWidth().background(Purple).padding(22.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Surface(shape = RoundedCornerShape(50), color = Color(0xFF3949AB)) {
                    Text("OP", Modifier.padding(17.dp), color = Color.White, fontWeight = FontWeight.Bold)
                }
                Text("TEAM DESTROY", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black)
                TextButton(onClick = {}) { Text("Logout", color = Color.White) }
            }
            Spacer(Modifier.height(8.dp))
            Text("Make Your OTP & Email Life More Easy!", color = Color.White.copy(alpha = .85f))
            Spacer(Modifier.height(22.dp))
            Surface(color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Text("License KEY-******-1234  •  Expires: Connect license server", Modifier.padding(14.dp), color = Color.DarkGray)
            }
        }
        Row(Modifier.fillMaxWidth().padding(18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("$accountCount account(s)", "Gmail accounts", Modifier.weight(1f))
            SummaryCard(if (running) "Running" else "Stopped", "Floating email", Modifier.weight(1f),
                if (running) Green else Color(0xFFE05A20))
        }
        ActionCard("✉", "Gmail Accounts", "Connect and manage Gmail inboxes", "Add Your Gmail Account", Purple, onConnect)
        ActionCard("▣", "Floating Email Permission", "Keep email previews and quick controls on top", "Start Floating Email", Purple, onStart)
        ActionCard("⇧", "Check for updates", "Tap to check latest version", "CHECK FOR UPDATES", Orange) {}
        Column(Modifier.fillMaxWidth().padding(20.dp).background(Color.White).padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎧 Support Team", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(18.dp))
            Text("📢 JOIN OUR CHANNEL", color = Orange, fontWeight = FontWeight.Bold)
        }
        if (message.isNotBlank()) Text(message, Modifier.padding(16.dp), color = Color.Gray)
        Text("Developer by Team Destroy", Modifier.align(Alignment.CenterHorizontally).padding(22.dp), color = Color.Gray)
    }
}

@Composable
private fun SummaryCard(title: String, subtitle: String, modifier: Modifier, titleColor: Color = Color.Black) {
    Surface(modifier, color = Color.White, shadowElevation = 2.dp) {
        Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, color = titleColor, fontWeight = FontWeight.Bold, fontSize = 19.sp)
            Text(subtitle, color = Color.Gray)
        }
    }
}

@Composable
private fun ActionCard(
    icon: String, title: String, subtitle: String, button: String,
    buttonColor: Color, onClick: () -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)
        .background(Color.White).padding(22.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, color = Purple, fontSize = 25.sp)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                Text(subtitle, color = Color.Gray, fontSize = 14.sp)
            }
        }
        Spacer(Modifier.height(20.dp))
        Button(onClick = onClick, modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(28.dp)) { Text(button, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun ConnectGmailScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    note: String,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(Pale)) {
        Row(Modifier.fillMaxWidth().background(Purple).padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }
            Text("Connect Gmail", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Column(Modifier.padding(22.dp)) {
            Text("Connect your Gmail account", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(18.dp))
            Text("Use Google's secure sign-in. Do not enter or share your Gmail password or App Password here.",
                color = Color.DarkGray)
            Spacer(Modifier.height(18.dp))
            OutlinedTextField(
                value = email, onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Gmail address") },
                placeholder = { Text("you@gmail.com") },
                singleLine = true
            )
            Spacer(Modifier.height(18.dp))
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green),
                shape = RoundedCornerShape(28.dp)) { Text("CONTINUE WITH GOOGLE", fontWeight = FontWeight.Bold) }
            if (note.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(note, color = Color.Gray)
            }
        }
    }
}
