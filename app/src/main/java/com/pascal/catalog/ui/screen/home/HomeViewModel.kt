@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.pascal.catalog.ui.screen.home

import android.content.Context
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.pascal.catalog.domain.mapper.getFavoriteTitlesFlow
import com.pascal.catalog.domain.mapper.withFavorites
import com.pascal.catalog.domain.model.CommonArticle
import com.pascal.catalog.domain.usecase.local.LocalUseCase
import com.pascal.catalog.domain.usecase.news.NewsUseCase
import com.pascal.catalog.ui.screen.home.state.HomeUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val newsUseCase: NewsUseCase,
    private val localUseCase: LocalUseCase
) : ViewModel() {

    private var exoPlayer: ExoPlayer? = null
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    fun setTransition(
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
    ) {
        _uiState.update {
            it.copy(
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }

    fun loadHomePartOne() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = false) }

            combine(
                newsUseCase.getAdsBanner(),
                newsUseCase.getBreakingNews(),
                newsUseCase.getHotTopics(),
                localUseCase.getFavoriteTitlesFlow()
            ) { ads, breaking, hotTopics, favorites ->
                _uiState.value.copy(
                    isLoading = false,
                    adsBanner = ads,
                    breakingNews = breaking.withFavorites(favorites),
                    hotTopics = hotTopics.withFavorites(favorites)
                )
            }.catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = true to (e.message ?: "Unknown error")
                    )
                }
            }.collect { newState -> _uiState.update { newState } }
        }
    }

    fun loadHomePartTwo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = false) }

            combine(
                newsUseCase.getIframeCampaign(),
                newsUseCase.getLiveReport(),
                newsUseCase.getAllCommonSections(),
                localUseCase.getFavoriteTitlesFlow()
            ) { iframe, live, commonList, favorites ->
                _uiState.value.copy(
                    isLoading = false,
                    iframeCampaign = iframe,
                    liveReport = live.withFavorites(favorites),
                    articleList = commonList.map { it.withFavorites(favorites) }
                )
            }.catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = true to (e.message ?: "Unknown error")
                    )
                }
            }.collect { newState -> _uiState.update { newState } }
        }
    }

    fun modifyFavorite(item: CommonArticle?, isFavorite: Boolean) {
        viewModelScope.launch {
            if (item == null) return@launch

            try {
                if (!isFavorite) localUseCase.insertFavorite(item)
                else localUseCase.deleteFavorite(item)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = true to "Gagal menambahkan ke favorit") }
            }
        }
    }

    fun playAudioFromUrl(context: Context, url: String?) {
        if (url.isNullOrBlank()) return
        exoPlayer?.release()
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(url)
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    fun stopAudio() {
        exoPlayer?.pause()
    }

    fun releaseAudio() {
        exoPlayer?.release()
        exoPlayer = null
    }

    fun resetError() {
        _uiState.update { it.copy(error = false to "") }
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }
}

enum class NewsTab(val title: String) {
    FIRST("Berita Utama"),
    NEW("Terbaru"),
    CHOICE("Pilihanku"),
    FREE("Bebas"),
    FAVORITE("Favorit Pembaca"),
}

enum class Category(val title: String) {
    BRIEF("Brief"),
    VISUAL("Visual"),
    VIDEO("Video"),
    ROW("Row"),
    MULTIMEDIA("Multimedia")
}
