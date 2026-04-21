package com.pascal.catalog.core.domain.usecase

import com.pascal.catalog.core.domain.repository.ProductRepository
import javax.inject.Inject

class ObserveCartCountUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
    operator fun invoke() = repository.observeCartCount()
}
