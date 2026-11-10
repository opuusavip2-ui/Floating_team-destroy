
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                }
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
                    oauthNote = "নিরাপদ Google OAuth সংযোগ পরের ধাপে যুক্ত হবে।"
                    message = "Demo: account form saved"
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
        Text("TEAM DESTROY", fontSize = 27.sp,
            fontWeight = FontWeight.Bold, color = Purple)
        Spacer(Modifier.height(12.dp))
        Text("Enter your license key to continue.", color = Color.Gray)
        Spacer(Modifier.height(22.dp))
        OutlinedTextField(
            value = license,
            onValueChange = onLicenseChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter License Key") },
            singleLine = true
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onActivate,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("ACTIVATE APP", fontWeight = FontWeight.Bold)
        }
        if (message.isNotBlank()) {
            Text(message, color = Color.Red, modifier = Modifier.padding(12.dp))
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
    Column(
        Modifier.fillMaxSize().background(Pale)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            Modifier.fillMaxWidth().background(Purple)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(shape = RoundedCornerShape(50), color = Color(0xFF3949AB)) {
                    Text("OP", Modifier.padding(12.dp),
                        color = Color.White, fontWeight = FontWeight.Bold)
                }
                Text("TEAM DESTROY", color = Color.White,
                    fontSize = 21.sp, fontWeight = FontWeight.Black)
                TextButton(onClick = {}) {
                    Text("Logout", color = Color.White, fontSize = 12.sp)
                }
            }

            Text(
                "Make Your OTP & Email Life More Easy!",
                color = Color.White.copy(alpha = .9f),
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(bottom = 9.dp)
            )

            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    "License KEY-******-1234  •  Expires: Connect license server",
                    Modifier.padding(10.dp),
                    color = Color.DarkGray,
                    fontSize = 11.sp
                )
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryCard(
                "$accountCount account(s)",
                "Gmail accounts",
                Modifier.weight(1f)
            )
            SummaryCard(
                if (running) "Running" else "Stopped",
                "Floating email",
                Modifier.weight(1f),
                if (running) Green else Color(0xFFE05A20)
            )
        }

        ActionCard(
            "✉", "Gmail Accounts",
            "Connect and manage Gmail inboxes",
            "ADD YOUR GMAIL ACCOUNT",
            Purple, onConnect
        )

        ActionCard(
            "▣", "Floating Email Permission",
            "Keep email previews and quick controls on top",
            "START FLOATING EMAIL",
            Purple, onStart
        )

        ActionCard(
            "⇧", "Check for updates",
            "Tap to check latest version",
            "CHECK FOR UPDATES",
            Orange, {}
        )

        Column(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 5.dp)
                .background(Color.White, RoundedCornerShape(6.dp))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🎧 Support Team",
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(Modifier.height(6.dp))
            Text("📢 JOIN OUR CHANNEL",
                color = Orange, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        if (message.isNotBlank()) {
            Text(message, Modifier.padding(10.dp),
                color = Color.Gray, fontSize = 11.sp)
        }

        Text(
            "Developer by Team Destroy",
            Modifier.align(Alignment.CenterHorizontally).padding(8.dp),
            color = Color.Gray, fontSize = 11.sp
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    subtitle: String,
    modifier: Modifier,
    titleColor: Color = Color.Black
) {
    Surface(
        modifier,
        color = Color.White,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            Modifier.padding(vertical = 10.dp, horizontal = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, color = titleColor,
                fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(subtitle, color = Color.Gray, fontSize = 11.sp)
        }
    }
}

@Composable
private fun ActionCard(
    icon: String,
    title: String,
    subtitle: String,
    button: String,
    buttonColor: Color,
    onClick: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp)
            .background(Color.White, RoundedCornerShape(6.dp))
            .padding(horizontal = 13.dp, vertical = 9.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, color = Purple, fontSize = 21.sp)
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, color = Color.Gray,
                    fontSize = 11.sp, maxLines = 1)
            }
        }

        Spacer(Modifier.height(7.dp))

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(37.dp),
            contentPadding = PaddingValues(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(button, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
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
        Row(
            Modifier.fillMaxWidth().background(Purple).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
            Text("Connect Gmail", color = Color.White,
                fontSize = 21.sp, fontWeight = FontWeight.Bold)
        }

        Column(Modifier.padding(20.dp)) {
            Text("Connect your Gmail account",
                fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(14.dp))
            Text(
                "Use Google's secure sign-in. Do not enter or share your Gmail password here.",
                color = Color.DarkGray
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Gmail address") },
                placeholder = { Text("you@gmail.com") },
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("CONTINUE WITH GOOGLE", fontWeight = FontWeight.Bold)
            }

            if (note.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(note, color = Color.Gray)
            }
        }
    }
}
