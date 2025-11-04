package com.example.youtubeapp.screen

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.youtubeapp.VideoViewModel
import com.example.youtubeapp.data.Screen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val videoViewModel: VideoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
//        onVideoClick: (VideoModel) -> Unit = {}
    ) {
        composable(Screen.Home.route) {
            DisplayVideoScreen(
                modifier = Modifier,
                videoModel = videoViewModel,
                onVideoClick = { video ->
                    navController.navigate(
                        Screen.VideoPlayer.createRoute(
                            video.videoUrl,
                            video.videoName,
                            video.storagePath
                        )
                    )
                }
            )
        }

        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(
                navArgument("videoUrl") { type = NavType.StringType },
                navArgument("videoName") { type = NavType.StringType },
                navArgument("storagePath") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val videoUrl =
                backStackEntry.arguments?.getString("videoUrl")?.let { Uri.decode(it) } ?: ""
            val videoName = backStackEntry.arguments?.getString("videoName") ?: ""
            val storagePath =
                backStackEntry.arguments?.getString("storagePath")?.let { Uri.decode(it) } ?: ""

            VideoPlayerScreen(
                videoUrl = videoUrl,
                videoName = videoName,
                onBackPressed = { navController.navigateUp() }
            )
        }
    }
}