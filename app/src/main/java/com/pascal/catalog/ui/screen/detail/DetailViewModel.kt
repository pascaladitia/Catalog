@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.pascal.catalog.ui.screen.detail

import android.content.Context
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.pascal.catalog.domain.mapper.withFavorite
import com.pascal.catalog.domain.model.CommonArticle
import com.pascal.catalog.domain.usecase.local.LocalUseCase
import com.pascal.catalog.ui.screen.detail.event.DetailUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val localUseCase: LocalUseCase
) : ViewModel() {

    private var exoPlayer: ExoPlayer? = null

    private val _uiState = MutableStateFlow(DetailUIState())
    val uiState: StateFlow<DetailUIState> = _uiState.asStateFlow()

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

    fun setDetailArticle(item: CommonArticle?) {
        viewModelScope.launch {
            if (item == null) return@launch
            val isFavorite = localUseCase.getFavorite(item.title)
            _uiState.update { it.copy(articles = item.withFavorite(isFavorite)) }
        }
    }

    fun modifyFavorite(item: CommonArticle?, isFavorite: Boolean) {
        viewModelScope.launch {
            if (item == null) return@launch

            try {
                if (!isFavorite) {
                    localUseCase.insertFavorite(item)
                } else {
                    localUseCase.deleteFavorite(item)
                }
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