package domain.entity

import domain.valueobject.Amount
import domain.valueobject.ProductId
import domain.valueobject.Quantity
import domain.valueobject.SaleId
import java.time.Instant

data class Sale(
    private val saleId: SaleId,
    private val productId: ProductId,
    private val quantity: Quantity,
    private val totalAmount: Amount,
    private val occurredAt: Instant
) {
    fun id(): SaleId = saleId
    fun productId(): ProductId = productId
    fun quantity(): Quantity = quantity
    fun totalAmount(): Amount = totalAmount
    fun occurredAt(): Instant = occurredAt
}
