package domain.valueobject

import java.util.UUID

@JvmInline
value class SaleId private constructor(private val value: String) {
    fun value(): String = value

    companion object {
        fun new(): SaleId = SaleId(UUID.randomUUID().toString())
        fun of(value: String): SaleId = SaleId(value)
    }
}
