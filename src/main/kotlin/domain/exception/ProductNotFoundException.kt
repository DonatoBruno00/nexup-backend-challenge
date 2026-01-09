package domain.exception

class ProductNotFoundException(
    message: String = "Product not found"
) : RuntimeException(message)
