
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
        setContent {
            MaterialTheme {
                TeamDestroyApp()
            }
        }
    }
}

private enum class Page {
    ACTIVATION, DASHBOARD, CONNECT_GMAIL
}

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
        Modifier.fillMaxSize().background(Pale).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "TEAM DESTROY",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = Purple
        )
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
    BoxWithConstraints(
        Modifier.fillMaxSize().background(Pale)
    ) {
        val scale = (maxWidth / 390.dp).coerceIn(0.92f, 1.08f)

        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Column(
                Modifier.fillMaxWidth()
                    .background(Purple)
                    .padding(horizontal = 16.dp * scale, vertical = 14.dp * scale)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFF3949AB)
                    ) {
                        Text(
                            "OP",
                            Modifier.padding(13.dp * scale),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp * scale
                        )
                    }

                    Text(
                        "TEAM DESTROY",
                        color = Color.White,
                        fontSize = 22.sp * scale,
                        fontWeight = FontWeight.Black
                    )

                    TextButton(onClick = {}) {
                        Text("Logout", color = Color.White)
                    }
                }

                Spacer(Modifier.height(10.dp * scale))

                Text(
                    "Make Your OTP & Email Life More Easy!",
                    Modifier.align(Alignment.CenterHorizontally),
                    color = Color.White,
                    fontSize = 12.sp * scale
                )

                Spacer(Modifier.height(12.dp * scale))

                Surface(
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Text(
                        "License KEY-******-1234  •  Expires: Connect license server",
                        Modifier.padding(13.dp * scale),
                        color = Color.DarkGray,
                        fontSize = 12.sp * scale
                    )
                }
            }

            Row(
                Modifier.fillMaxWidth().padding(12.dp * scale),
                horizontalArrangement = Arrangement.spacedBy(10.dp * scale)
            ) {
                SummaryCard(
                    "$accountCount account(s)",
                    "Gmail accounts",
                    Modifier.weight(1f),
                    scale = scale
                )
                SummaryCard(
                    if (running) "Running" else "Stopped",
                    "Floating email",
                    Modifier.weight(1f),
                    if (running) Green else Color(0xFFE05A20),
                    scale
                )
            }

            ActionCard(
                "✉", "Gmail Accounts",
                "Connect and manage Gmail inboxes",
                "ADD YOUR GMAIL ACCOUNT",
                Purple, onConnect, scale
            )

            ActionCard(
                "▣", "Floating Email Permission",
                "Keep email previews and quick controls on top",
                "START FLOATING EMAIL",
                Purple, onStart, scale
            )

            ActionCard(
                "⇧", "Check for updates",
                "Tap to check latest version",
                "CHECK FOR UPDATES",
                Orange, {}, scale
            )

            Column(
                Modifier.fillMaxWidth()
                    .padding(horizontal = 12.dp * scale, vertical = 7.dp * scale)
                    .background(Color.White, RoundedCornerShape(9.dp))
                    .padding(15.dp * scale),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "🎧 Support Team",
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp * scale
                )
                Spacer(Modifier.height(12.dp * scale))
                Text(
                    "📢 JOIN OUR CHANNEL",
                    color = Orange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp * scale
                )
            }

            if (message.isNotBlank()) {
                Text(
                    message,
                    Modifier.padding(12.dp),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Text(
                "Developer by Team Destroy",
                Modifier.align(Alignment.CenterHorizontally).padding(12.dp),
                color = Color.Gray,
                fontSize = 12.sp * scale
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    subtitle: String,
    modifier: Modifier,
    titleColor: Color = Color.Black,
    scale: Float = 1f
) {
    Surface(
        modifier.height(82.dp * scale),
        color = Color.White,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(9.dp)
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                title,
                color = titleColor,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp * scale
            )
            Spacer(Modifier.height(5.dp))
            Text(
                subtitle,
                color = Color.Gray,
                fontSize = 12.sp * scale
            )
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
    onClick: () -> Unit,
    scale: Float = 1f
) {
    Column(
        Modifier.fillMaxWidth()
            .padding(horizontal = 12.dp * scale, vertical = 7.dp * scale)
            .background(Color.White, RoundedCornerShape(9.dp))
            .padding(horizontal = 15.dp * scale, vertical = 13.dp * scale)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, color = Purple, fontSize = 23.sp * scale)
            Spacer(Modifier.width(12.dp * scale))
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp * scale
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    subtitle,
                    color = Color.Gray,
                    fontSize = 12.sp * scale,
                    maxLines = 2
                )
            }
        }

        Spacer(Modifier.height(12.dp * scale))

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(46.dp * scale),
            contentPadding = PaddingValues(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                button,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp * scale
            )
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
            Text(
                "Connect Gmail",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(Modifier.padding(20.dp)) {
            Text(
                "Connect your Gmail account",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
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
