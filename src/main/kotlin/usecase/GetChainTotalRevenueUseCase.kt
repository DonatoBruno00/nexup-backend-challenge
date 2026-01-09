package usecase

import domain.valueobject.Amount
import repository.SupermarketRepository

class GetChainTotalRevenueUseCase(
    private val supermarketRepository: SupermarketRepository
) {

    fun execute(): Amount {
        return supermarketRepository.findAll()
            .fold(Amount.zero()) { total, supermarket ->
                total + supermarket.totalRevenue()
            }
    }
}
