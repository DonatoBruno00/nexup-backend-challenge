package usecase

import domain.exception.ProductNotFoundException
import domain.exception.SupermarketNotFoundException
import domain.valueobject.Amount
import domain.valueobject.ProductId
import domain.valueobject.Quantity
import domain.valueobject.SupermarketId
import repository.ProductRepository
import repository.SupermarketRepository
import java.time.Instant

class RegisterSaleUseCase(
    private val supermarketRepository: SupermarketRepository,
    private val productRepository: ProductRepository
) {

    fun execute(
        supermarketId: SupermarketId,
        productId: ProductId,
        quantity: Quantity
    ): Amount {
        val supermarket = supermarketRepository.findById(supermarketId)
            ?: throw SupermarketNotFoundException()

        val product = productRepository.findById(productId)
            ?: throw ProductNotFoundException()

        val totalAmount = supermarket.registerSale(
            productId = productId,
            quantity = quantity,
            unitPrice = product.price(),
            occurredAt = Instant.now()
        )

        supermarketRepository.save(supermarket)

        return totalAmount
    }
}
