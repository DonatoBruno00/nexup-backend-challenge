package usecase

import TestData
import domain.entity.Stock
import domain.valueobject.Amount
import domain.valueobject.Quantity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.impl.SupermarketRepositoryImpl
import java.time.Instant
import kotlin.test.assertEquals

class GetChainTotalRevenueUseCaseTest {

    private lateinit var supermarketRepository: SupermarketRepositoryImpl
    private lateinit var useCase: GetChainTotalRevenueUseCase

    @BeforeEach
    fun setUp() {
        supermarketRepository = SupermarketRepositoryImpl()
        useCase = GetChainTotalRevenueUseCase(supermarketRepository)
    }

    @Test
    fun `should return total revenue across all supermarkets`() {
        val stockA = Stock()
        stockA.increaseBy(TestData.carne.id(), Quantity.of(100))

        val supermarketA = TestData.supermarketA(stockA)
        supermarketA.registerSale(TestData.carne.id(), Quantity.of(5), TestData.carne.price(), Instant.now())
        supermarketRepository.save(supermarketA)

        val stockB = Stock()
        stockB.increaseBy(TestData.pescado.id(), Quantity.of(100))

        val supermarketB = TestData.supermarketB(stockB)
        supermarketB.registerSale(TestData.pescado.id(), Quantity.of(3), TestData.pescado.price(), Instant.now())
        supermarketRepository.save(supermarketB)

        val result = useCase.execute()

        // 5 * 10.0 + 3 * 20.0 = 110.0
        assertEquals(Amount.of(110.0), result)
    }

    @Test
    fun `should return zero when no supermarkets exist`() {
        val result = useCase.execute()

        assertEquals(Amount.zero(), result)
    }
}
