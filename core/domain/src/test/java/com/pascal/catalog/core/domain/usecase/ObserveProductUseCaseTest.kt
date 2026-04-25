package com.pascal.catalog.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.pascal.catalog.core.domain.usecase.common.FakeRepository
import com.pascal.catalog.core.domain.usecase.common.sampleProduct
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ObserveProductUseCaseTest {

    @Test
    fun `get product from repository`() = runTest {
        val products = listOf(
            sampleProduct(id = 1, title = "Backpack"),
            sampleProduct(id = 2, title = "Shirt")
        )

        val repository = FakeRepository(products)
        val result = ObserveProductsUseCase(repository)()
        val items = result.first()

        assertThat(items).hasSize(2)
        assertThat(items.last().title).isEqualTo("Shirt")
    }
}