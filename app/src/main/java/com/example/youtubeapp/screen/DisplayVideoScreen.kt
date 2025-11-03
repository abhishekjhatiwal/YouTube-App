package com.example.youtubeapp.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.youtubeapp.FirebaseStorageHelper
import com.example.youtubeapp.VideoViewModel
import com.example.youtubeapp.data.VideoData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayVideoScreen(
    modifier: Modifier = Modifier,
    videoModel: VideoViewModel = viewModel(),
    onVideoClick: (VideoData) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            coroutineScope.launch {
                val video = FirebaseStorageHelper.uploadVideo(it, context)
                videoModel.addVideo(video)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "YouTube",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    launcher.launch("video/*")
                },
                containerColor = Color.Red,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Video"
                )
            }
        },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            // Add a spacer at the top
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(videoModel.videoList, key = { it.storagePath }) { video ->
                VideoItem(
                    video = video,
                    onDelete = {
                        coroutineScope.launch {
                            videoModel.deleteVideo(it)
                        }
                    },
                    onReplace = { uri ->
                        coroutineScope.launch {
                            videoModel.replaceView(video, uri, context)
                        }
                    },
                    onVideoClick = {
                        onVideoClick(video)
                    }
                )
            }

            // Add a spacer at the bottom for better scrolling
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}




/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayVideoScreen(
    modifier: Modifier,
    videoModel: VideoViewModel = viewModel(),
    onVideoClick: (VideoData) -> Unit = {}
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            videoModel.viewModelScope.launch {
                val video = FirebaseStorageHelper.uploadVideo(it, context)
                videoModel.addVideo(video)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "YouTube")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    launcher.launch("video/*")
                },
                containerColor = Color.Red
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Video"
                )
            }
        }
    ) { padding ->
        val viewModelScope = videoModel.viewModelScope
        LazyColumn(
            contentPadding = padding
        ) {
            items(videoModel.videoList, key = { it.storagePath }) { video ->
                VideoItem(
                    video = video,
                    onDelete = { viewModelScope.launch { videoModel.deleteVideo(it) } },
                    onReplace = { uri ->
                        coroutineScope.launch {
                            val newVideo = FirebaseStorageHelper.replaceVideo(
                                oldPath = video.storagePath,
                                newUri = uri,
                                context = context
                            )
                            // videoModel.updateVideo(video, newVideo)
                        }
                    }
                )
            }
        }

    }
}

 */