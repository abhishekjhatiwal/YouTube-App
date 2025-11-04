package com.example.youtubeapp

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeapp.data.VideoData
import kotlinx.coroutines.launch

data class VideoUiState(
    val isLoading: Boolean = false,
    val videos: List<VideoData> = emptyList(),
    val errorMessage: String? = null
)

class VideoViewModel : ViewModel() {

    var uiState by mutableStateOf(VideoUiState())
        private set

    init {
        fetchVideo()
    }

    fun fetchVideo() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                val videos = FirebaseStorageHelper.fetchAllVideo()
                uiState = uiState.copy(isLoading = false, videos = videos, errorMessage = null)
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun addVideo(video: VideoData) {
        uiState = uiState.copy(videos = uiState.videos + video)
    }

    fun deleteVideo(video: VideoData) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                FirebaseStorageHelper.deleteVideo(video.storagePath)
                uiState = uiState.copy(
                    isLoading = false,
                    videos = uiState.videos.filterNot { it.storagePath == video.storagePath }
                )
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun replaceView(video: VideoData, newUri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                val newVideo = FirebaseStorageHelper.replaceVideo(video.storagePath, newUri, context)
                uiState = uiState.copy(
                    isLoading = false,
                    videos = uiState.videos.map {
                        if (it.storagePath == video.storagePath) newVideo else it
                    }
                )
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }
}
































/*
class VideoViewModel : ViewModel() {
    var videoList by mutableStateOf<List<VideoData>>(emptyList())
    private set

    init{
        fetchVideo()
    }

    fun fetchVideo(){
        viewModelScope.launch {
            videoList = FirebaseStorageHelper.fetchAllVideo()
        }
    }

    fun addVideo(video: VideoData) {
        videoList = videoList + video
    }

    fun deleteVideo(video: VideoData) {
        viewModelScope.launch {
            FirebaseStorageHelper.deleteVideo(video.storagePath)
            videoList = videoList.filterNot {
                it.storagePath == video.storagePath
            }
        }
    }

    fun replaceView(video: VideoData, newUri: Uri, context: Context) {
        viewModelScope.launch {
            val newVideo = FirebaseStorageHelper.replaceVideo(video.storagePath, newUri, context)
            videoList = videoList.map{
                if(it.storagePath == video.storagePath) newVideo else it
            }
        }
    }

}

 */