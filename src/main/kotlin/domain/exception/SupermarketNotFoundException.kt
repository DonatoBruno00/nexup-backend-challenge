package domain.exception

class SupermarketNotFoundException(
    message: String = "Supermarket not found"
) : RuntimeException(message)
