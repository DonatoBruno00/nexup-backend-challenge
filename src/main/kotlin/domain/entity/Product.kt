package domain.entity

import domain.valueobject.Amount
import domain.valueobject.ProductId

data class Product(
    private val id: ProductId,
    private val name: String,
    private val price: Amount
) {
    fun id(): ProductId = id
    fun name(): String = name
    fun price(): Amount = price
}
