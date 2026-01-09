package usecase

import domain.valueobject.ProductId
import domain.valueobject.Quantity
import repository.ProductRepository
import repository.SupermarketRepository

class GetTopSellingProductsUseCase(
    private val supermarketRepository: SupermarketRepository,
    private val productRepository: ProductRepository
) {

    fun execute(): String {
        val quantitiesByProduct = mutableMapOf<ProductId, Quantity>()

        supermarketRepository.findAll().forEach { supermarket ->
            supermarket.salesQuantitiesByProduct().forEach { (productId, quantity) ->
                val current = quantitiesByProduct[productId] ?: Quantity.zero()
                quantitiesByProduct[productId] = current + quantity
            }
        }

        // Formato: "Producto1: cantidad - Producto2: cantidad - ..."
        // Retorna string vacío si no hay ventas
        return quantitiesByProduct.entries
            .sortedByDescending { it.value.toInt() }
            .take(5)
            .mapNotNull { (productId, quantity) ->
                val product = productRepository.findById(productId)
                product?.let { "${it.name()}: ${quantity.toInt()}" }
            }
            .joinToString(" - ")
    }
}
