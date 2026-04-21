package com.pascal.catalog.core.domain.usecase

import com.pascal.catalog.core.domain.repository.ProductRepository
import javax.inject.Inject

class RefreshCategoryUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(category: String) = repository.refreshCategory(category)
}
