package domain.entity

import domain.exception.InsufficientStockException
import domain.valueobject.ProductId
import domain.valueobject.Quantity

class Stock(
    private val items: MutableMap<ProductId, Quantity> = mutableMapOf()
) {

    fun quantityOf(productId: ProductId): Quantity =
        items[productId] ?: Quantity.zero()

    fun decreaseBy(productId: ProductId, quantityToDecrease: Quantity) {
        val currentQuantity = quantityOf(productId)

        val notEnoughStock = currentQuantity.toInt() < quantityToDecrease.toInt()
        if (notEnoughStock) {
            throw InsufficientStockException()
        }

        items[productId] = currentQuantity - quantityToDecrease
    }

    fun increaseBy(productId: ProductId, quantityToAdd: Quantity) {
        val currentQuantity = quantityOf(productId)
        items[productId] = currentQuantity + quantityToAdd
    }
}
