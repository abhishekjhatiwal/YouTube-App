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

    suspend fun fetchAllVideo() : List<VideoData>{
        val result = firebaseStorage.listAll().await()
        return result.items.mapNotNull{ ref->
            val url = ref.downloadUrl.await().toString()
            val name = ref.name
            val path = ref.path
            VideoData(videoUrl = url, videoName = name, storagePath = path)
        }
    }

    suspend fun deleteVideo(path: String){
        Firebase.storage.reference.child(path).delete().await()
    }

    suspend fun replaceVideo(oldPath: String, newUri: Uri, context: Context): VideoData{
        deleteVideo(oldPath)
        return uploadVideo(newUri,context)
    }
}