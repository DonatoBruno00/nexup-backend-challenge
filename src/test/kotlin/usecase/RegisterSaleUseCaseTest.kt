package usecase

import TestData
import domain.entity.Stock
import domain.exception.InsufficientStockException
import domain.exception.ProductNotFoundException
import domain.exception.SupermarketNotFoundException
import domain.valueobject.Amount
import domain.valueobject.Quantity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.impl.ProductRepositoryImpl
import repository.impl.SupermarketRepositoryImpl
import kotlin.test.assertEquals

class RegisterSaleUseCaseTest {

    private lateinit var productRepository: ProductRepositoryImpl
    private lateinit var supermarketRepository: SupermarketRepositoryImpl
    private lateinit var useCase: RegisterSaleUseCase

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepositoryImpl()
        supermarketRepository = SupermarketRepositoryImpl()
        useCase = RegisterSaleUseCase(supermarketRepository, productRepository)
    }

    @Test
    fun `should register sale successfully`() {
        productRepository.save(TestData.carne)

        val stock = Stock()
        stock.increaseBy(TestData.carne.id(), Quantity.of(10))
        val supermarket = TestData.supermarketA(stock)
        supermarketRepository.save(supermarket)

        val total = useCase.execute(supermarket.id(), TestData.carne.id(), Quantity.of(3))

        assertEquals(Amount.of(30.0), total)
    }

    @Test
    fun `should throw SupermarketNotFoundException when supermarket does not exist`() {
        val supermarket = TestData.supermarketA()

        assertThrows<SupermarketNotFoundException> {
            useCase.execute(supermarket.id(), TestData.carne.id(), Quantity.of(1))
        }
    }

    @Test
    fun `should throw ProductNotFoundException when product does not exist`() {
        val supermarket = TestData.supermarketA()
        supermarketRepository.save(supermarket)

        assertThrows<ProductNotFoundException> {
            useCase.execute(supermarket.id(), TestData.carne.id(), Quantity.of(1))
        }
    }

    @Test
    fun `should throw InsufficientStockException when stock is not enough`() {
        productRepository.save(TestData.carne)

        val stock = Stock()
        stock.increaseBy(TestData.carne.id(), Quantity.of(2))
        val supermarket = TestData.supermarketA(stock)
        supermarketRepository.save(supermarket)

        assertThrows<InsufficientStockException> {
            useCase.execute(supermarket.id(), TestData.carne.id(), Quantity.of(5))
        }
    }
}
