package usecase

import domain.exception.ProductNotFoundException
import domain.exception.SupermarketNotFoundException
import domain.valueobject.Amount
import domain.valueobject.ProductId
import domain.valueobject.SupermarketId
import repository.ProductRepository
import repository.SupermarketRepository

class GetProductRevenueUseCase(
    private val supermarketRepository: SupermarketRepository,
    private val productRepository: ProductRepository
) {

    fun execute(supermarketId: SupermarketId, productId: ProductId): Amount {
        val supermarket = supermarketRepository.findById(supermarketId)
            ?: throw SupermarketNotFoundException()

        productRepository.findById(productId)
            ?: throw ProductNotFoundException()

        return supermarket.revenueOf(productId)
    }
}
