package repository

import domain.entity.Supermarket
import domain.valueobject.SupermarketId

interface SupermarketRepository {
    fun findById(id: SupermarketId): Supermarket?
    fun save(supermarket: Supermarket)
}
