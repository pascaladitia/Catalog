package com.pascal.catalog.ui.screen.favorite

import androidx.lifecycle.ViewModel
import com.pascal.catalog.data.local.repository.LocalRepositoryImpl
import com.pascal.catalog.data.repository.NewsRepositoryImpl

class FavoriteViewModel(
    private val repositoryImpl: NewsRepositoryImpl,
    private val database: LocalRepositoryImpl
) : ViewModel() {


}