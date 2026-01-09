package usecase

import TestData
import domain.entity.Stock
import domain.valueobject.Quantity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.impl.ProductRepositoryImpl
import repository.impl.SupermarketRepositoryImpl
import java.time.Instant
import kotlin.test.assertEquals

class GetTopSellingProductsUseCaseTest {

    private lateinit var supermarketRepository: SupermarketRepositoryImpl
    private lateinit var productRepository: ProductRepositoryImpl
    private lateinit var useCase: GetTopSellingProductsUseCase

    @BeforeEach
    fun setUp() {
        supermarketRepository = SupermarketRepositoryImpl()
        productRepository = ProductRepositoryImpl()
        useCase = GetTopSellingProductsUseCase(supermarketRepository, productRepository)

        TestData.allProducts.forEach { productRepository.save(it) }
    }

    @Test
    fun `should return top 5 selling products across all supermarkets`() {
        val stock = Stock()
        TestData.allProducts.forEach { stock.increaseBy(it.id(), Quantity.of(100)) }

        val supermarketA = TestData.supermarketA(stock)
        supermarketA.registerSale(TestData.carne.id(), Quantity.of(10), TestData.carne.price(), Instant.now())
        supermarketA.registerSale(TestData.pescado.id(), Quantity.of(8), TestData.pescado.price(), Instant.now())
        supermarketA.registerSale(TestData.pollo.id(), Quantity.of(6), TestData.pollo.price(), Instant.now())

        val stockB = Stock()
        TestData.allProducts.forEach { stockB.increaseBy(it.id(), Quantity.of(100)) }

        val supermarketB = TestData.supermarketB(stockB)
        supermarketB.registerSale(TestData.carne.id(), Quantity.of(5), TestData.carne.price(), Instant.now())
        supermarketB.registerSale(TestData.cerdo.id(), Quantity.of(12), TestData.cerdo.price(), Instant.now())
        supermarketB.registerSale(TestData.ternera.id(), Quantity.of(3), TestData.ternera.price(), Instant.now())
        supermarketB.registerSale(TestData.cordero.id(), Quantity.of(1), TestData.cordero.price(), Instant.now())

        supermarketRepository.save(supermarketA)
        supermarketRepository.save(supermarketB)

        val result = useCase.execute()
        assertEquals("Carne: 15 - Cerdo: 12 - Pescado: 8 - Pollo: 6 - Ternera: 3", result)
    }

    @Test
    fun `should return empty string when no sales exist`() {
        val supermarket = TestData.supermarketA()
        supermarketRepository.save(supermarket)

        val result = useCase.execute()

        assertEquals("", result)
    }

    @Test
    fun `should return less than 5 if fewer products sold`() {
        val stock = Stock()
        stock.increaseBy(TestData.carne.id(), Quantity.of(100))
        stock.increaseBy(TestData.pescado.id(), Quantity.of(100))

        val supermarket = TestData.supermarketA(stock)
        supermarket.registerSale(TestData.carne.id(), Quantity.of(5), TestData.carne.price(), Instant.now())
        supermarket.registerSale(TestData.pescado.id(), Quantity.of(3), TestData.pescado.price(), Instant.now())
        supermarketRepository.save(supermarket)

        val result = useCase.execute()

        assertEquals("Carne: 5 - Pescado: 3", result)
    }
}
