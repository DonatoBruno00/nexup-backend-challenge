import domain.entity.Product
import domain.entity.Stock
import domain.entity.Supermarket
import domain.valueobject.Amount
import domain.valueobject.ProductId
import domain.valueobject.SupermarketId

object TestData {
    val carne = Product(ProductId.of("1"), "Carne", Amount.of(10.0))
    val pescado = Product(ProductId.of("2"), "Pescado", Amount.of(20.0))
    val pollo = Product(ProductId.of("3"), "Pollo", Amount.of(30.0))
    val cerdo = Product(ProductId.of("4"), "Cerdo", Amount.of(45.0))
    val ternera = Product(ProductId.of("5"), "Ternera", Amount.of(50.0))
    val cordero = Product(ProductId.of("6"), "Cordero", Amount.of(65.0))

    val allProducts = listOf(carne, pescado, pollo, cerdo, ternera, cordero)
    
    fun supermarketA(stock: Stock = Stock()) =
        Supermarket(SupermarketId.of("1"), "Supermercado A", stock)

    fun supermarketB(stock: Stock = Stock()) =
        Supermarket(SupermarketId.of("2"), "Supermercado B", stock)

    fun supermarketC(stock: Stock = Stock()) =
        Supermarket(SupermarketId.of("3"), "Supermercado C", stock)
}
