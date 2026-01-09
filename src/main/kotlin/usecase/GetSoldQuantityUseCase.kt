package usecase

import domain.exception.ProductNotFoundException
import domain.valueobject.ProductId
import domain.valueobject.Quantity
import repository.ProductRepository
import repository.SupermarketRepository

class GetSoldQuantityUseCase(
    private val supermarketRepository: SupermarketRepository,
    private val productRepository: ProductRepository
) {

    fun execute(productId: ProductId): Quantity {
        productRepository.findById(productId)
            ?: throw ProductNotFoundException()

        return supermarketRepository.findAll()
            .fold(Quantity.zero()) { total, supermarket ->
                total + supermarket.soldQuantityOf(productId)
            }
    }
}
