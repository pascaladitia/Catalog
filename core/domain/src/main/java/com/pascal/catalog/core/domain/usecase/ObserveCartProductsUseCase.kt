package com.pascal.catalog.core.domain.usecase

import com.pascal.catalog.core.domain.repository.ProductRepository
import javax.inject.Inject

class ObserveCartProductsUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
    operator fun invoke() = repository.observeCartProducts()
}
