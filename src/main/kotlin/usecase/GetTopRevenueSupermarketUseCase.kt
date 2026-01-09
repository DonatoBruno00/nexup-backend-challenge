package usecase

import repository.SupermarketRepository

class GetTopRevenueSupermarketUseCase(
    private val supermarketRepository: SupermarketRepository
) {

    // Formato: "NombreSupermercado (id). Ingresos totales: X"
    // Retorna null si no hay supermercados
    fun execute(): String? {
        val topSupermarket = supermarketRepository.findAll()
            .maxByOrNull { it.totalRevenue().value() }
            ?: return null

        val revenue = topSupermarket.totalRevenue().value()
        return "${topSupermarket.name()} (${topSupermarket.id().value()}). Ingresos totales: $revenue"
    }
}
