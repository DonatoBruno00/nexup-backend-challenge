package usecase

import TestData
import domain.entity.Stock
import domain.exception.ProductNotFoundException
import domain.valueobject.Quantity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.impl.ProductRepositoryImpl
import repository.impl.SupermarketRepositoryImpl
import java.time.Instant
import kotlin.test.assertEquals

class GetSoldQuantityUseCaseTest {

    private lateinit var supermarketRepository: SupermarketRepositoryImpl
    private lateinit var productRepository: ProductRepositoryImpl
    private lateinit var useCase: GetSoldQuantityUseCase

    @BeforeEach
    fun setUp() {
        supermarketRepository = SupermarketRepositoryImpl()
        productRepository = ProductRepositoryImpl()
        useCase = GetSoldQuantityUseCase(supermarketRepository, productRepository)
    }

    @Test
    fun `should return total sold quantity across all supermarkets`() {
        productRepository.save(TestData.carne)

        val stock = Stock()
        stock.increaseBy(TestData.carne.id(), Quantity.of(100))

        val supermarketA = TestData.supermarketA(stock)
        supermarketA.registerSale(TestData.carne.id(), Quantity.of(5), TestData.carne.price(), Instant.now())
        supermarketA.registerSale(TestData.carne.id(), Quantity.of(3), TestData.carne.price(), Instant.now())
        supermarketRepository.save(supermarketA)

        val stockB = Stock()
        stockB.increaseBy(TestData.carne.id(), Quantity.of(100))
        val supermarketB = TestData.supermarketB(stockB)
        supermarketB.registerSale(TestData.carne.id(), Quantity.of(7), TestData.carne.price(), Instant.now())
        supermarketRepository.save(supermarketB)

        val result = useCase.execute(TestData.carne.id())

        assertEquals(Quantity.of(15), result)
    }

    @Test
    fun `should return zero when no sales exist`() {
        productRepository.save(TestData.carne)
        val supermarket = TestData.supermarketA()
        supermarketRepository.save(supermarket)

        val result = useCase.execute(TestData.carne.id())

        assertEquals(Quantity.zero(), result)
    }

    @Test
    fun `should throw ProductNotFoundException when product does not exist`() {
        assertThrows<ProductNotFoundException> {
            useCase.execute(TestData.carne.id())
        }
    }
}
