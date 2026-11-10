
package com.allinworld.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import android.os.Bundle
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope

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
    ACTIVATION,
    DASHBOARD,
    CONNECT_GMAIL
}

@Composable
private fun TeamDestroyApp() {
    var page by remember { mutableStateOf(Page.ACTIVATION) }
    var license by remember { mutableStateOf("") }
    var oauthNote by remember { mutableStateOf("") }
    var gmailAccessToken by remember { mutableStateOf<String?>(null) }
    var accountCount by remember { mutableIntStateOf(0) }
    var floatingRunning by remember { mutableStateOf(false) }
    var connectedGmail by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
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
            onConnect = {
                oauthNote = ""
                page = Page.CONNECT_GMAIL
            },
            onStart = {
                message = "Floating bar ও permission service পরের ধাপে যুক্ত হবে"
            },
            message = message
        )

        Page.CONNECT_GMAIL -> ConnectGmailScreen(
            note = oauthNote,
            onBack = {
                page = Page.DASHBOARD
            },
            onAuthorized = { token ->
                gmailAccessToken = token
                accountCount = 1
                oauthNote = "Google authorization সফল হয়েছে।"
                message = "Gmail authorization complete"
                page = Page.DASHBOARD
            },
            onError = { error ->
                oauthNote = error
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
        Modifier
            .fillMaxSize()
            .background(Pale)
            .padding(24.dp),
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

        Text(
            "Enter your license key to continue.",
            color = Color.Gray
        )

        Spacer(Modifier.height(22.dp))

        OutlinedTextField(
            value = license,
            onValueChange = onLicenseChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Enter License Key")
            },
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onActivate,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                "ACTIVATE APP",
                fontWeight = FontWeight.Bold
            )
        }

        if (message.isNotBlank()) {
            Text(
                message,
                color = Color.Red,
                modifier = Modifier.padding(12.dp)
            )
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
        Modifier
            .fillMaxSize()
            .background(Pale)
    ) {
        val scale = (maxWidth / 390.dp).coerceIn(0.92f, 1.08f)

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Purple)
                    .padding(
                        horizontal = 16.dp * scale,
                        vertical = 14.dp * scale
                    )
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
                        Text(
                            "Logout",
                            color = Color.White
                        )
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
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp * scale),
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
                "✉",
                "Gmail Accounts",
                "Connect and manage Gmail inboxes",
                "ADD YOUR GMAIL ACCOUNT",
                Purple,
                onConnect,
                scale
            )

            ActionCard(
                "▣",
                "Floating Email Permission",
                "Keep email previews and quick controls on top",
                "START FLOATING EMAIL",
                Purple,
                onStart,
                scale
            )

            ActionCard(
                "⇧",
                "Check for updates",
                "Tap to check latest version",
                "CHECK FOR UPDATES",
                Orange,
                {},
                scale
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp * scale,
                        vertical = 7.dp * scale
                    )
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
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp),
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
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp * scale,
                vertical = 7.dp * scale
            )
            .background(Color.White, RoundedCornerShape(9.dp))
            .padding(
                horizontal = 15.dp * scale,
                vertical = 13.dp * scale
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                icon,
                color = Purple,
                fontSize = 23.sp * scale
            )

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
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp * scale),
            contentPadding = PaddingValues(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            ),
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
    note: String,
    onBack: () -> Unit,
    onAuthorized: (String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    var status by remember { mutableStateOf(note) }
    var loading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        loading = false

        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val authResult =
                    Identity.getAuthorizationClient(context)
                        .getAuthorizationResultFromIntent(result.data)

                val token = authResult.accessToken

                if (!token.isNullOrBlank()) {
                    onAuthorized(token)
                } else {
                    status = "Google access token পাওয়া যায়নি।"
                    onError(status)
                }
            } catch (e: ApiException) {
                status = "Google Error: ${e.statusCode}"
                onError(status)
            } catch (e: Exception) {
                status = "Authorization error: ${e.message}"
                onError(status)
            }
        } else {
            status = "Google authorization বাতিল হয়েছে।"
            onError(status)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Pale)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Purple)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
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
                "Sign in securely with Google and authorize Gmail read access. Your Gmail password is never requested by this app.",
                color = Color.DarkGray
            )

            Spacer(Modifier.height(20.dp))

            Button(
                enabled = !loading,
                onClick = {
                    loading = true
                    status = "Opening Google authorization..."

                    val request = AuthorizationRequest.builder()
                        .setRequestedScopes(
                            listOf(
                                Scope(
                                    "https://www.googleapis.com/auth/gmail.readonly"
                                )
                            )
                        )
                        .build()

                    Identity.getAuthorizationClient(context)
                        .authorize(request)
                        .addOnSuccessListener { authResult ->
                            if (authResult.hasResolution()) {
                                val pendingIntent = authResult.pendingIntent

                                if (pendingIntent != null) {
                                    try {
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                pendingIntent.intentSender
                                            ).build()
                                        )
                                    } catch (e: Exception) {
                                        loading = false
                                        status = "Google window error: ${e.message}"
                                        onError(status)
                                    }
                                } else {
                                    loading = false
                                    status = "Google consent window পাওয়া যায়নি।"
                                    onError(status)
                                }
                            } else {
                                loading = false
                                val token = authResult.accessToken

                                if (!token.isNullOrBlank()) {
                                    onAuthorized(token)
                                } else {
                                    status = "Access token পাওয়া যায়নি।"
                                    onError(status)
                                }
                            }
                        }
                        .addOnFailureListener { error ->
                            loading = false
                            status = "Google Error: ${error.message}"
                            onError(status)
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    if (loading) "PLEASE WAIT..." else "CONTINUE WITH GOOGLE",
                    fontWeight = FontWeight.Bold
                )
            }

            if (status.isNotBlank()) {
                Spacer(Modifier.height(14.dp))
                Text(
                    status,
                    color = Color.Gray
                )
            }
        }
    }
}
private suspend fun fetchGoogleEmail(token: String): String? {
    return withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null

        try {
            connection = URL(
                "https://www.googleapis.com/oauth2/v2/userinfo"
            ).openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.setRequestProperty(
                "Authorization",
                "Bearer $token"
            )
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            if (connection.responseCode == 200) {
                val response = connection.inputStream
                    .bufferedReader()
                    .use { it.readText() }

                JSONObject(response).optString("email")
                    .takeIf { it.isNotBlank() }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        } finally {
            connection?.disconnect()
        }
    }
}
