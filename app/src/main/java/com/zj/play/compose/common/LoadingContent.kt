package com.zj.play.compose.common

import android.widget.ProgressBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zj.play.R

@Composable
fun LoadingContent() {
    val context = LocalContext.current
    val progressBar = remember {
        ProgressBar(context).apply {
            id = R.id.progress_bar
        }
    }
    progressBar.indeterminateDrawable =
        AppCompatResources.getDrawable(LocalContext.current, R.drawable.loading_animation)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Adds view to Compose
        AndroidView(
            { progressBar }, modifier = Modifier
                .width(200.dp)
                .height(110.dp)
        ) {}
    }

}