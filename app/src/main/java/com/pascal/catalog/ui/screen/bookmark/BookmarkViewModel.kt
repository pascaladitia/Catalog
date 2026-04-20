package com.pascal.catalog.ui.screen.bookmark

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.pascal.catalog.domain.model.CommonArticle
import com.pascal.catalog.domain.usecase.local.LocalUseCase
import com.pascal.catalog.ui.screen.bookmark.state.BookmarkUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val localUseCase: LocalUseCase
): ViewModel() {

    private var exoPlayer: ExoPlayer? = null

    private val _uiState = MutableStateFlow(BookmarkUIState())
    val uiState: StateFlow<BookmarkUIState> = _uiState.asStateFlow()

    fun loadFavorite() {
        viewModelScope.launch {
            try {
                val result = localUseCase.getFavorite()
                _uiState.update { it.copy(articles = result) }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(error = true to "Gagal mendapatkan favorit") }
            }
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

                loadFavorite()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(error = true to "Gagal menambahkan ke favorit") }
            }
        }
    }

    fun deleteFavorite(item: List<CommonArticle>?) {
        viewModelScope.launch {
            if (item.isNullOrEmpty()) {
                _uiState.update { it.copy(error = true to "Daftar favorite kosong") }
                return@launch
            }

            try {
                localUseCase.clearFavorite()
                loadFavorite()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = true to "Gagal hapus semua favorit") }
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