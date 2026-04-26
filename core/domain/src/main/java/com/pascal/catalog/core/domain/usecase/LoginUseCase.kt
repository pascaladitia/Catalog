package com.pascal.catalog.core.domain.usecase

import com.pascal.catalog.core.domain.repository.ProductRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
    operator fun invoke(username: String, password: String) =
        repository.login(username, password)
}
