package usecase

import TestData
import domain.entity.Stock
import domain.valueobject.Quantity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.impl.SupermarketRepositoryImpl
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetTopRevenueSupermarketUseCaseTest {

    private lateinit var supermarketRepository: SupermarketRepositoryImpl
    private lateinit var useCase: GetTopRevenueSupermarketUseCase

    @BeforeEach
    fun setUp() {
        supermarketRepository = SupermarketRepositoryImpl()
        useCase = GetTopRevenueSupermarketUseCase(supermarketRepository)
    }

    @Test
    fun `should return supermarket with highest revenue`() {
        val stockA = Stock()
        stockA.increaseBy(TestData.carne.id(), Quantity.of(100))
        val supermarketA = TestData.supermarketA(stockA)
        supermarketA.registerSale(TestData.carne.id(), Quantity.of(5), TestData.carne.price(), Instant.now())
        supermarketRepository.save(supermarketA)

        val stockB = Stock()
        stockB.increaseBy(TestData.pescado.id(), Quantity.of(100))
        val supermarketB = TestData.supermarketB(stockB)
        supermarketB.registerSale(TestData.pescado.id(), Quantity.of(10), TestData.pescado.price(), Instant.now())
        supermarketRepository.save(supermarketB)

        val result = useCase.execute()
        
        assertEquals("Supermercado B (2). Ingresos totales: 200.0", result)
    }

    @Test
    fun `should return null when no supermarkets exist`() {
        val result = useCase.execute()

        assertNull(result)
    }
}
