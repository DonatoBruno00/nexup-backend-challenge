package domain.valueobject

@JvmInline
value class ProductId private constructor(private val value: String) {
    fun value(): String = value

    companion object {
        fun of(value: String): ProductId = ProductId(value)
    }
}
