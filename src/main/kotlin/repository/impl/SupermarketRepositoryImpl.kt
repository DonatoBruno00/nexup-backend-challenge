package repository.impl

import domain.entity.Supermarket
import domain.valueobject.SupermarketId
import repository.SupermarketRepository

class SupermarketRepositoryImpl : SupermarketRepository {

    private val supermarkets: MutableMap<SupermarketId, Supermarket> = mutableMapOf()

    override fun findById(id: SupermarketId): Supermarket? = 
        supermarkets[id]

    override fun findAll(): List<Supermarket> = 
        supermarkets.values.toList()

    override fun save(supermarket: Supermarket) {
        supermarkets[supermarket.id()] = supermarket
    }

    fun addAll(vararg supermarketList: Supermarket) {
        supermarketList.forEach { save(it) }
    }
}
