package usecase

import TestData
import domain.entity.Stock
import domain.exception.ProductNotFoundException
import domain.exception.SupermarketNotFoundException
import domain.valueobject.Amount
import domain.valueobject.Quantity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.impl.ProductRepositoryImpl
import repository.impl.SupermarketRepositoryImpl
import java.time.Instant
import kotlin.test.assertEquals

class GetProductRevenueUseCaseTest {

    private lateinit var supermarketRepository: SupermarketRepositoryImpl
    private lateinit var productRepository: ProductRepositoryImpl
    private lateinit var useCase: GetProductRevenueUseCase

    @BeforeEach
    fun setUp() {
        supermarketRepository = SupermarketRepositoryImpl()
        productRepository = ProductRepositoryImpl()
        useCase = GetProductRevenueUseCase(supermarketRepository, productRepository)
    }

    @Test
    fun `should return revenue for a product in a supermarket`() {
        productRepository.save(TestData.carne)

        val stock = Stock()
        stock.increaseBy(TestData.carne.id(), Quantity.of(100))

        val supermarket = TestData.supermarketA(stock)
        supermarket.registerSale(TestData.carne.id(), Quantity.of(5), TestData.carne.price(), Instant.now())
        supermarket.registerSale(TestData.carne.id(), Quantity.of(3), TestData.carne.price(), Instant.now())
        supermarketRepository.save(supermarket)

        val result = useCase.execute(supermarket.id(), TestData.carne.id())

        assertEquals(Amount.of(80.0), result)
    }

    @Test
    fun `should return zero when no sales exist for product`() {
        productRepository.save(TestData.carne)
        val supermarket = TestData.supermarketA()
        supermarketRepository.save(supermarket)

        val result = useCase.execute(supermarket.id(), TestData.carne.id())

        assertEquals(Amount.zero(), result)
    }

    @Test
    fun `should throw SupermarketNotFoundException when supermarket does not exist`() {
        productRepository.save(TestData.carne)

        assertThrows<SupermarketNotFoundException> {
            useCase.execute(TestData.supermarketA().id(), TestData.carne.id())
        }
    }

    @Test
    fun `should throw ProductNotFoundException when product does not exist`() {
        val supermarket = TestData.supermarketA()
        supermarketRepository.save(supermarket)

        assertThrows<ProductNotFoundException> {
            useCase.execute(supermarket.id(), TestData.carne.id())
        }
    }
}
