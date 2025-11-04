package com.example.youtubeapp.data

import android.net.Uri

data class VideoData(
    val videoUrl: String,
    val videoName: String,
    val storagePath: String
)

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object VideoPlayer : Screen("video_player/{videoUrl}/{videoName}/{storagePath}") {
        fun createRoute(videoUrl: String, videoName: String, storagePath: String): String {
            val encodedUrl = Uri.encode(videoUrl)
            val encodedPath = Uri.encode(storagePath)
            return "video_player/$encodedUrl/$videoName/$encodedPath"
        }
    }
}

data class VideoUiState(
    val isLoading: Boolean = false,
    val videos: List<VideoData> = emptyList(),
    val errorMessage: String? = null
)