package usecase

import TestData
import domain.entity.Stock
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

class GetTotalRevenueUseCaseTest {

    private lateinit var supermarketRepository: SupermarketRepositoryImpl
    private lateinit var productRepository: ProductRepositoryImpl
    private lateinit var useCase: GetTotalRevenueUseCase

    @BeforeEach
    fun setUp() {
        supermarketRepository = SupermarketRepositoryImpl()
        productRepository = ProductRepositoryImpl()
        useCase = GetTotalRevenueUseCase(supermarketRepository)
    }

    @Test
    fun `should return total revenue for a supermarket`() {
        productRepository.save(TestData.carne)
        productRepository.save(TestData.pescado)

        val stock = Stock()
        stock.increaseBy(TestData.carne.id(), Quantity.of(100))
        stock.increaseBy(TestData.pescado.id(), Quantity.of(100))

        val supermarket = TestData.supermarketA(stock)
        supermarket.registerSale(TestData.carne.id(), Quantity.of(5), TestData.carne.price(), Instant.now())
        supermarket.registerSale(TestData.pescado.id(), Quantity.of(2), TestData.pescado.price(), Instant.now())
        supermarketRepository.save(supermarket)

        val result = useCase.execute(supermarket.id())

        assertEquals(Amount.of(90.0), result)
    }

    @Test
    fun `should return zero when no sales exist`() {
        val supermarket = TestData.supermarketA()
        supermarketRepository.save(supermarket)

        val result = useCase.execute(supermarket.id())

        assertEquals(Amount.zero(), result)
    }

    @Test
    fun `should throw SupermarketNotFoundException when supermarket does not exist`() {
        assertThrows<SupermarketNotFoundException> {
            useCase.execute(TestData.supermarketA().id())
        }
    }
}
