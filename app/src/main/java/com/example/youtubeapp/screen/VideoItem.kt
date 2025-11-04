package com.example.youtubeapp.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.youtubeapp.data.VideoData

@Composable
fun VideoItem(
    video: VideoData,
    onDelete: (VideoData) -> Unit,
    onReplace: (Uri) -> Unit,
    onVideoClick: @Composable () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(video.videoUrl) {
        exoPlayer.setMediaItem(MediaItem.fromUri(video.videoUrl))
        exoPlayer.prepare()
    }

//    DisposableEffect(video.videoUrl) {
//        onDispose {
//            exoPlayer.release()
//        }
//    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.stop()
            exoPlayer.release()
        }
    }


    val replaceLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                onReplace(it)
            }
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onVideoClick },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Video Thumbnail
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            player = exoPlayer
                            useController = false
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Menu Button (Top Right)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Replace Video") },
                            onClick = {
                                showMenu = false
                                replaceLauncher.launch("video/*")
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Edit, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Video") },
                            onClick = {
                                showMenu = false
                                onDelete(video)
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Delete, contentDescription = null)
                            }
                        )
                    }
                }
            }

            // Video Info Section (YouTube-like)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Channel Avatar
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = Color.Red
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "YT",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Video Title and Channel Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = video.videoName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "YouTube Channel • 1M views • 1 day ago",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}





































/*
@Composable
fun VideoItem(video: VideoData, onDelete: (VideoData) -> Unit, onReplace: (Uri) -> Unit) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    var isPlaying by remember { mutableStateOf(false) }
    var hasEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(video.videoUrl) {
        exoPlayer.setMediaItem(MediaItem.fromUri(video.videoUrl))
        exoPlayer.prepare()
        isPlaying = false
        hasEnabled = false
    }

    DisposableEffect(video.videoUrl) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    hasEnabled = true
                    isPlaying = false
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }
    val replaceLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                onReplace(it)
            }
        }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Box {
            AndroidView(
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { onDelete(video) }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = colorResource(id = R.color.white)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val whiteColor = colorResource(id = R.color.white)
                IconButton(onClick = {
                    if (isPlaying) {
                        exoPlayer.pause()
                        isPlaying = false
                    } else {
                        if (hasEnabled) {
                            exoPlayer.seekTo(0)
                            hasEnabled = false
                        }
                        exoPlayer.play()
                        isPlaying = true
                    }
                }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Refresh else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = whiteColor
                    )
                }
                IconButton(onClick = {
                    replaceLauncher.launch("video/*")
                }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = whiteColor)
                }
            }
        }
    }

}

 */