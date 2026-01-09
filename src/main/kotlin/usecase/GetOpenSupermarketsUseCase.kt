package usecase

import repository.SupermarketRepository
import java.time.DayOfWeek
import java.time.LocalTime

class GetOpenSupermarketsUseCase(
    private val supermarketRepository: SupermarketRepository
) {

    // Formato: "NombreSupermercado (id), NombreSupermercado2 (id2)"
    // Retorna string vacío si no hay supermercados abiertos
    fun execute(day: DayOfWeek, time: LocalTime): String {
        return supermarketRepository.findAll()
            .filter { it.isOpenAt(day, time) }
            .joinToString(", ") { "${it.name()} (${it.id().value()})" }
    }
}
