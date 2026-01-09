package domain.valueobject

@JvmInline
value class SupermarketId private constructor(private val value: String) {
    fun value(): String = value

    companion object {
        fun of(value: String): SupermarketId = SupermarketId(value)
    }
}
