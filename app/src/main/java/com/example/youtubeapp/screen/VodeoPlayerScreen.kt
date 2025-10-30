package com.example.youtubeapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
    videoUrl: String,
    videoName: String,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(0) }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Video Player Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            player = exoPlayer
                            useController = true
                            controllerShowTimeoutMs = 3000
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
            }

            // Video Details Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // Video Title
                Text(
                    text = videoName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Views and Date
                Text(
                    text = "1.2M views • 2 days ago",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons Row (Like, Share, etc.)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Like Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = {
                                isLiked = !isLiked
                                likeCount = if (isLiked) likeCount + 1 else likeCount - 1
                            }
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                contentDescription = "Like",
                                tint = if (isLiked) Color.Red else Color.Black
                            )
                        }
                        Text(
                            text = if (likeCount > 0) "$likeCount" else "Like",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Dislike Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Outlined.ThumbUp,
                                contentDescription = "Dislike",
                                tint = Color.Black,
                                modifier = Modifier.graphicsLayer {
                                    rotationZ = 180f
                                }
                            )
                        }
                        Text(
                            text = "Dislike",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Share Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.Black
                            )
                        }
                        Text(
                            text = "Share",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                Spacer(modifier = Modifier.height(16.dp))

                // Channel Info Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Channel Avatar
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = Color.Red
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "YT",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Channel Name and Subscribers
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "YouTube Channel",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "1.5M subscribers",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    // Subscribe Button
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Subscribe")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Video Description
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Video playing from Firebase Storage",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This is a sample video description. You can add more details about the video content here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                Spacer(modifier = Modifier.height(16.dp))

                // Comments Section Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Comments • 1.2K",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sample Comment
                Row(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = CircleShape,
                        color = Color.Blue
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "U",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "@username",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "1 day ago",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Great video! Thanks for sharing.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}