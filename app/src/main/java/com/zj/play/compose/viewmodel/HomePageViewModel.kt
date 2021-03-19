package com.zj.play.compose.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.play.compose.model.QueryHomeArticle
import com.zj.model.room.entity.Article
import com.zj.play.compose.model.PlayState
import com.zj.play.compose.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */

class HomePageViewModel(application: Application) : BaseViewModel(application) {

    private val homeRepository: HomeRepository = HomeRepository(application)

    private val _bannerState = MutableLiveData<PlayState>()

    val bannerState: LiveData<PlayState>
        get() = _bannerState

    private val _articleDataList = MutableLiveData<ArrayList<Article>>()

    private val _page = MutableLiveData<Int>()

    val page: LiveData<Int>
        get() = _page

    fun onPageChanged(refresh: Int) {
        _page.postValue(refresh)
    }

    fun getData(isLoad: Boolean = false, isRefresh: Boolean = false) {
        Log.e(TAG, "getData: 进来了  isLoad:$isLoad   isRefresh:$isRefresh")
        viewModelScope.launch(Dispatchers.IO) {
            if (!isLoad) {
                homeRepository.getBanner(_bannerState)
            }
            homeRepository.getArticleList(
                mutableLiveData,
                _articleDataList,
                QueryHomeArticle(page.value ?: 0, isRefresh),
                isLoad
            )
        }
    }

    companion object {
        private const val TAG = "HomePageViewModel"
    }

}

const val REFRESH_START = 1
const val REFRESH_STOP = 2
