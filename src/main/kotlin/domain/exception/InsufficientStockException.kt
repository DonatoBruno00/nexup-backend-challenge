package domain.exception

class InsufficientStockException(
    message: String = "Insufficient stock for the requested product"
) : RuntimeException(message)
