package domain.valueobject

@JvmInline
value class Quantity private constructor(private val value: Int) {

    init {
        require(value >= 0) { "Quantity cannot be negative" }
    }

    fun toInt(): Int = value

    operator fun plus(other: Quantity): Quantity = Quantity(value + other.value)

    operator fun minus(other: Quantity): Quantity = Quantity(value - other.value)

    companion object {
        fun of(value: Int): Quantity = Quantity(value)
        fun zero(): Quantity = Quantity(0)
    }
}
