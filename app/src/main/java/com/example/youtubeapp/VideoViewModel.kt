package com.example.youtubeapp

import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.youtubeapp.data.VideoData

class VideoViewModel : ViewModel() {
    var videoList by mutableStateOf<List<VideoData>>(emptyList())
    private set

    fun addVideo(video: VideoData) {
        videoList = videoList + video
    }

//    fun deleteVideo(video: VideoData) {
//        videoList = videoList - video
//    }

}