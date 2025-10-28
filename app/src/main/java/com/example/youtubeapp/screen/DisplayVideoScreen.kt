package com.example.youtubeapp.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.youtubeapp.FirebaseStorageHelper
import com.example.youtubeapp.FirebaseStorageHelper.replaceVideo
import com.example.youtubeapp.VideoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayVideoScreen(modifier: Modifier, videoModel: VideoViewModel = viewModel()) {
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
                        viewModelScope.launch {
                            replaceVideo((video.storagePath), uri, context)
                        }
                    }
                )
            }
        }

    }
}