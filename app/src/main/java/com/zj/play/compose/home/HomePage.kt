/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zj.play.compose.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.model.room.entity.Article
import com.zj.play.R
import com.zj.play.compose.common.*
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.home.*

@Composable
fun HomePage(
    enterArticle: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = viewModel()
) {
    val onRefreshPostsState by rememberUpdatedState(0)

    val result by viewModel.state.observeAsState(PlayLoading)

    val refresh by viewModel.refreshState.observeAsState(REFRESH_DEFAULT)

    if (onRefreshPostsState == 0 && refresh == REFRESH_DEFAULT) {
        viewModel.getArticleList(1, true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PlayAppBar(stringResource(id = R.string.home_page), false)
        SwipeToRefreshLayout(
            refreshingState = refresh == REFRESH_START,
            onRefresh = {
                viewModel.onRefreshChanged(REFRESH_START)
                viewModel.getArticleList(1, true)
                Log.e("ZHUJIANG123", "HomePage: ")
            },
            refreshIndicator = {
                Surface(elevation = 10.dp, shape = CircleShape) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                    )
                }
            },
            content = {
                when (result) {
                    is PlayLoading -> {
                        LoadingContent()
                    }
                    is PlaySuccess<*> -> {
                        onRefreshPostsState.and(1)
                        viewModel.onRefreshChanged(REFRESH_STOP)
                        val data = result as PlaySuccess<List<Article>>
                        LazyColumn(modifier) {
                            itemsIndexed(data.data) { index, article ->
                                ArticleItem(
                                    article,
                                    index,
                                    enterArticle = { urlArgs -> enterArticle(urlArgs) })
                            }
                        }
                    }
                    is PlayError -> {
                        viewModel.onRefreshChanged(REFRESH_STOP)
                        ErrorContent(enterArticle = { viewModel.getArticleList(1, true) })
                    }
                }
            },
        )


    }

}