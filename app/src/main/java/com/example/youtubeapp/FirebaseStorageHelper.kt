package com.example.youtubeapp

import android.content.Context
import android.net.Uri
import com.example.youtubeapp.data.VideoData
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

object FirebaseStorageHelper {
    private val  firebaseStorage = Firebase.storage.reference.child("videos")

    suspend fun uploadVideo(uri: Uri, context: Context) : VideoData {
        val videoFileName = uri.lastPathSegment?.split("/")?.last() ?: "video_${System.currentTimeMillis()}"
        val videoRef = firebaseStorage.child(videoFileName)
        videoRef.putFile(uri).await()
        val url = videoRef.downloadUrl.await()
        return VideoData(videoUrl = url.toString(), videoName = videoFileName, storagePath = videoRef.path)
    }
}