package repository

import domain.entity.Product
import domain.valueobject.ProductId

interface ProductRepository {
    fun findById(id: ProductId): Product?
}
