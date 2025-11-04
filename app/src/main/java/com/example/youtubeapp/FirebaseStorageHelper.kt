package com.example.youtubeapp

import android.content.Context
import android.net.Uri
import com.example.youtubeapp.data.VideoData
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object FirebaseStorageHelper {
    private val firebaseStorage = Firebase.storage.reference.child("videos")

    suspend fun uploadVideo(uri: Uri, context: Context): VideoData {
        return try {
            val videoFileName = uri.lastPathSegment?.substringAfterLast("/")
                ?: "video_${System.currentTimeMillis()}"
            val videoRef = firebaseStorage.child(videoFileName)

            videoRef.putFile(uri).await()
            val url = videoRef.downloadUrl.await()

            VideoData(
                videoUrl = url.toString(),
                videoName = videoFileName,
                storagePath = videoRef.path
            )
        } catch (e: Exception) {
            e.printStackTrace()
            VideoData(videoUrl = "", videoName = "Upload Failed", storagePath = "")
        }
    }

    suspend fun fetchAllVideo(): List<VideoData> = coroutineScope {
        val result = firebaseStorage.listAll().await()
        result.items.map { ref ->
            async {
                val url = ref.downloadUrl.await().toString()
                VideoData(
                    videoUrl = url,
                    videoName = ref.name,
                    storagePath = ref.path
                )
            }
        }.awaitAll()
    }

    suspend fun deleteVideo(path: String) {
        if (path.isBlank()) return
        try {
            Firebase.storage.reference.child(path).delete().await()
        } catch (e: Exception) {
            Log.e("FirebaseStorageHelper", "Error deleting video: ${e.message}")
        }
    }

    suspend fun replaceVideo(oldPath: String, newUri: Uri, context: Context): VideoData {
        return try {
            val newVideo = uploadVideo(newUri, context)
            deleteVideo(oldPath)
            newVideo
        } catch (e: Exception) {
            e.printStackTrace()
            VideoData(videoUrl = "", videoName = "Replace Failed", storagePath = "")
        }
    }
}


/*
object FirebaseStorageHelper {
    private val firebaseStorage = Firebase.storage.reference.child("videos")

//    suspend fun uploadVideo(uri: Uri, context: Context) : VideoData {
//        val videoFileName = uri.lastPathSegment?.split("/")?.last() ?: "video_${System.currentTimeMillis()}"
//        val videoRef = firebaseStorage.child(videoFileName)
//        videoRef.putFile(uri).await()
//        val url = videoRef.downloadUrl.await()
//        return VideoData(videoUrl = url.toString(), videoName = videoFileName, storagePath = videoRef.path)
//    }

    suspend fun uploadVideo(uri: Uri, context: Context): VideoData {
        return try {
            val videoFileName = uri.lastPathSegment?.substringAfterLast("/")
                ?: "video_${System.currentTimeMillis()}"
            val videoRef = firebaseStorage.child(videoFileName)

            videoRef.putFile(uri).await()
            val url = videoRef.downloadUrl.await()

            VideoData(
                videoUrl = url.toString(),
                videoName = videoFileName,
                storagePath = videoRef.path
            )
        } catch (e: Exception) {
            e.printStackTrace()
            VideoData(videoUrl = "", videoName = "Upload Failed", storagePath = "")
        }
    }


//    suspend fun fetchAllVideo() : List<VideoData>{
//        val result = firebaseStorage.listAll().await()
//        return result.items.mapNotNull{ ref->
//            val url = ref.downloadUrl.await().toString()
//            val name = ref.name
//            val path = ref.path
//            VideoData(videoUrl = url, videoName = name, storagePath = path)
//        }
//    }

    suspend fun fetchAllVideo(): List<VideoData> {
        val result = firebaseStorage.listAll().await()
        return result.items.map { ref ->
            async {
                val url = ref.downloadUrl.await().toString()
                VideoData(videoUrl = url, videoName = ref.name, storagePath = ref.path)
            }
        }.awaitAll()
    }


    suspend fun deleteVideo(path: String) {
        if (path.isBlank()) return
        Firebase.storage.reference.child(path).delete().await()
    }


    suspend fun replaceVideo(oldPath: String, newUri: Uri, context: Context): VideoData {
        val newVideo = uploadVideo(newUri, context)
        deleteVideo(oldPath)
        return newVideo
    }

}


 */