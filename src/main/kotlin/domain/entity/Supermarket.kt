package domain.entity

import domain.valueobject.Amount
import domain.valueobject.ProductId
import domain.valueobject.Quantity
import domain.valueobject.SaleId
import domain.valueobject.SupermarketId
import java.time.Instant

data class Supermarket(
    private val id: SupermarketId,
    private val name: String,
    private val stock: Stock,
    private val sales: MutableList<Sale> = mutableListOf()
) {

    fun registerSale(
        productId: ProductId,
        quantity: Quantity,
        unitPrice: Amount,
        occurredAt: Instant
    ): Amount {
        stock.decreaseBy(productId, quantity)

        val totalAmount = unitPrice.multiply(quantity)

        val newSale = Sale(
            saleId = SaleId.new(),
            productId = productId,
            quantity = quantity,
            totalAmount = totalAmount,
            occurredAt = occurredAt
        )

        sales.add(newSale)

        return totalAmount
    }

    fun id(): SupermarketId = id

    fun name(): String = name

    fun stockOf(productId: ProductId): Quantity = 
        stock.quantityOf(productId)

    fun soldQuantityOf(productId: ProductId): Quantity {
        val salesOfProduct = sales.filter { sale -> sale.productId() == productId }
        
        return salesOfProduct.fold(Quantity.zero()) { totalQuantity, sale -> 
            totalQuantity + sale.quantity() 
        }
    }

    fun revenueOf(productId: ProductId): Amount {
        val salesOfProduct = sales.filter { sale -> sale.productId() == productId }
        
        return salesOfProduct.fold(Amount.zero()) { totalRevenue, sale -> 
            totalRevenue + sale.totalAmount() 
        }
    }

    fun totalRevenue(): Amount {
        return sales.fold(Amount.zero()) { totalRevenue, sale -> 
            totalRevenue + sale.totalAmount() 
        }
    }

    fun salesQuantitiesByProduct(): Map<ProductId, Quantity> {
        return sales
            .groupBy { sale -> sale.productId() }
            .mapValues { (_, salesOfProduct) ->
                salesOfProduct.fold(Quantity.zero()) { totalQuantity, sale -> 
                    totalQuantity + sale.quantity() 
                }
            }
    }
}
