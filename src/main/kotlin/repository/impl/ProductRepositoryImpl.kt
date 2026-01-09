package repository.impl

import domain.entity.Product
import domain.valueobject.ProductId
import repository.ProductRepository

class ProductRepositoryImpl : ProductRepository {

    private val products: MutableMap<ProductId, Product> = mutableMapOf()

    override fun findById(id: ProductId): Product? = products[id]

    fun save(product: Product) = products.put(product.id(), product)

    fun addAll(vararg products: Product) = products.forEach { save(it) }
}
