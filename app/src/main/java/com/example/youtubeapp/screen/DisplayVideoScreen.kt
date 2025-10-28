package com.example.youtubeapp.screen

import android.R.attr.contentDescription
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.youtubeapp.FirebaseStorageHelper
import com.example.youtubeapp.VideoViewModel
import kotlinx.coroutines.launch

@Composable
fun DispalyVideoScreen(videoModel: VideoViewModel = viewModel()) {
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
        LazyColumn(
            contentPadding = padding
        ) {}
    }

}