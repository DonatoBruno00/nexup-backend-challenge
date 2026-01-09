package domain.valueobject

import java.math.BigDecimal

@JvmInline
value class Amount private constructor(private val value: BigDecimal) {

    init {
        require(value >= BigDecimal.ZERO) { "Amount must be greater than or equal to zero" }
    }

    fun value(): BigDecimal = value

    fun multiply(quantity: Int): Amount = Amount(value * BigDecimal(quantity))
    fun multiply(quantity: Quantity): Amount = Amount(value * BigDecimal(quantity.toInt()))

    operator fun plus(other: Amount): Amount = Amount(value + other.value)

    companion object {
        fun of(value: BigDecimal): Amount = Amount(value)
        fun of(value: Double): Amount = Amount(BigDecimal.valueOf(value))
        fun zero(): Amount = Amount(BigDecimal.ZERO)
    }
}
