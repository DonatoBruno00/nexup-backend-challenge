package usecase

import domain.exception.SupermarketNotFoundException
import domain.valueobject.Amount
import domain.valueobject.SupermarketId
import repository.SupermarketRepository

class GetTotalRevenueUseCase(
    private val supermarketRepository: SupermarketRepository
) {

    fun execute(supermarketId: SupermarketId): Amount {
        val supermarket = supermarketRepository.findById(supermarketId)
            ?: throw SupermarketNotFoundException()

        return supermarket.totalRevenue()
    }
}
