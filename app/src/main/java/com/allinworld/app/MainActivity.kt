
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
        val screenHeight = maxHeight

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

            Spacer(Modifier.height(8.dp))

            Text(
                "Developer by Team Destroy",
                Modifier.align(Alignment.CenterHorizontally)
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
            modifier = Modifier.fillMaxWidth()
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
