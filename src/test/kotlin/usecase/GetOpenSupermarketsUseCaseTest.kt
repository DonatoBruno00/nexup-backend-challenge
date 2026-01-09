package usecase

import domain.entity.Stock
import domain.entity.Supermarket
import domain.valueobject.Schedule
import domain.valueobject.SupermarketId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.impl.SupermarketRepositoryImpl
import java.time.DayOfWeek
import java.time.LocalTime
import kotlin.test.assertEquals

class GetOpenSupermarketsUseCaseTest {

    private lateinit var supermarketRepository: SupermarketRepositoryImpl
    private lateinit var useCase: GetOpenSupermarketsUseCase

    @BeforeEach
    fun setUp() {
        supermarketRepository = SupermarketRepositoryImpl()
        useCase = GetOpenSupermarketsUseCase(supermarketRepository)
    }

    @Test
    fun `should return open supermarkets at given day and time`() {
        val schedule = Schedule.of(
            openingTime = LocalTime.of(8, 0),
            closingTime = LocalTime.of(20, 0),
            openDays = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY)
        )

        val supermarketA = Supermarket(
            SupermarketId.of("1"), "Supermercado A", Stock(), mutableListOf(), schedule
        )
        val supermarketB = Supermarket(
            SupermarketId.of("2"), "Supermercado B", Stock(), mutableListOf(), schedule
        )
        supermarketRepository.save(supermarketA)
        supermarketRepository.save(supermarketB)

        val result = useCase.execute(DayOfWeek.MONDAY, LocalTime.of(10, 0))

        assertEquals("Supermercado A (1), Supermercado B (2)", result)
    }

    @Test
    fun `should return empty string when no supermarkets are open`() {
        val schedule = Schedule.of(
            openingTime = LocalTime.of(8, 0),
            closingTime = LocalTime.of(20, 0),
            openDays = setOf(DayOfWeek.MONDAY)
        )

        val supermarket = Supermarket(
            SupermarketId.of("1"), "Supermercado A", Stock(), mutableListOf(), schedule
        )
        supermarketRepository.save(supermarket)

        val result = useCase.execute(DayOfWeek.SUNDAY, LocalTime.of(10, 0))

        assertEquals("", result)
    }

    @Test
    fun `should return empty string when time is outside schedule`() {
        val schedule = Schedule.of(
            openingTime = LocalTime.of(8, 0),
            closingTime = LocalTime.of(20, 0),
            openDays = setOf(DayOfWeek.MONDAY)
        )

        val supermarket = Supermarket(
            SupermarketId.of("1"), "Supermercado A", Stock(), mutableListOf(), schedule
        )
        supermarketRepository.save(supermarket)

        val result = useCase.execute(DayOfWeek.MONDAY, LocalTime.of(21, 0))

        assertEquals("", result)
    }
}
