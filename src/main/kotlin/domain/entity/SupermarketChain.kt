package domain.entity

import domain.exception.SupermarketNotFoundException
import domain.valueobject.ProductId
import domain.valueobject.SupermarketId

class SupermarketChain(
    private val name: String,
    private val supermarkets: MutableMap<SupermarketId, Supermarket> = mutableMapOf(),
    private val products: MutableMap<ProductId, Product> = mutableMapOf()
) {

    fun name(): String = name

    fun addProduct(product: Product) {
        products[product.id()] = product
    }

    fun productBy(productId: ProductId): Product? = 
        products[productId]

    fun addSupermarket(supermarket: Supermarket) {
        supermarkets[supermarket.id()] = supermarket
    }

    fun supermarketBy(supermarketId: SupermarketId): Supermarket =
        supermarkets[supermarketId] ?: throw SupermarketNotFoundException()

    fun allSupermarkets(): List<Supermarket> = 
        supermarkets.values.toList()

    fun allProducts(): List<Product> = 
        products.values.toList()
}
