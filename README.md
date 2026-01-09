## Nexup Backend Challenge

El enunciado original está en [`CHALLENGE.md`](CHALLENGE.md). Este documento explica mis decisiones de diseño.

---

## 📦 Estructura del Proyecto

```
domain/
├── entity/
│   ├── Product          # Producto con id, nombre y precio
│   ├── Supermarket      # Aggregate root: maneja stock, ventas y horarios
│   ├── Stock            # Cantidades por producto, protege stock negativo
│   └── Sale             # Registro inmutable de una venta
├── exception/
│   ├── InsufficientStockException
│   ├── ProductNotFoundException
│   └── SupermarketNotFoundException
└── valueobject/
    ├── ProductId
    ├── SupermarketId
    ├── SaleId
    ├── Quantity
    ├── Amount
    └── Schedule         # Horarios de apertura/cierre y días

repository/
├── ProductRepository    # Interface
├── SupermarketRepository
└── impl/
    ├── ProductRepositoryImpl      # In-memory
    └── SupermarketRepositoryImpl

usecase/
├── RegisterSaleUseCase           # Registrar venta
├── GetSoldQuantityUseCase        # Cantidad vendida de un producto (toda la cadena)
├── GetProductRevenueUseCase      # Ingresos de un producto en un supermercado
├── GetTotalRevenueUseCase        # Ingresos totales de un supermercado
├── GetTopSellingProductsUseCase  # Top 5 productos más vendidos (toda la cadena)
├── GetChainTotalRevenueUseCase   # Ingresos totales de toda la cadena
├── GetTopRevenueSupermarketUseCase # Supermercado con mayor ingresos
└── GetOpenSupermarketsUseCase    # Supermercados abiertos en día/hora (bonus)
```

---

## 🧩 Entidades

- **Product**: describe un producto (id, nombre, precio). No conoce stock ni ventas.
- **Supermarket**: aggregate root. Maneja su stock, registra ventas, calcula ingresos.
- **Stock**: cantidades por producto. Lanza `InsufficientStockException` si no hay stock.
- **Sale**: hecho inmutable de una venta (producto, cantidad, monto, fecha).

---

## 🔹 Value Objects

Evité usar primitivos directamente (*Primitive Obsession*). Cada Value Object:
- Constructor privado → solo se crea via `of()`, `new()`, `zero()`
- Campo privado → acceso controlado por getters
- Valida invariantes (ej: `Quantity >= 0`, `Amount >= 0`)

---

## ⚠️ Excepciones

- **InsufficientStockException**: no hay stock suficiente para la venta
- **ProductNotFoundException**: el producto no existe
- **SupermarketNotFoundException**: el supermercado no existe

---

## 🎯 Casos de Uso

| UseCase | Entrada | Salida |
|---------|---------|--------|
| `RegisterSaleUseCase` | supermarketId, productId, quantity | Amount (total) |
| `GetSoldQuantityUseCase` | productId | Quantity |
| `GetProductRevenueUseCase` | supermarketId, productId | Amount |
| `GetTotalRevenueUseCase` | supermarketId | Amount |
| `GetTopSellingProductsUseCase` | - | String ("Producto: cantidad - ...") |
| `GetChainTotalRevenueUseCase` | - | Amount |
| `GetTopRevenueSupermarketUseCase` | - | String? ("Nombre (id). Ingresos: X") |
| `GetOpenSupermarketsUseCase` | day, time | String ("Nombre (id), ...") |

---

## 🧪 Tests

Cada UseCase tiene su archivo de tests con casos:
- Flujo exitoso
- Excepciones de dominio
- Casos borde (sin ventas, sin supermercados, etc.)

`TestData` centraliza los datos de prueba para mantener consistencia.

---

## 💡 Decisiones de Diseño

### Enfoque: Dominio primero (Domain-driven-desing)

Decidí armar el proyecto priorizando el **dominio de la aplicación** y principios de **Clean Code**:

- **Naming descriptivo**: los nombres de variables, funciones y clases son lo más declarativos posible, especialmente cerca de las entidades. Por ejemplo: `soldQuantityOf()`, `revenueOf()`, `decreaseBy()`, `increaseBy()`.

- **Value Objects en lugar de primitivos**: evité el *Primitive Obsession*. Un `ProductId` no es un `String`, un `Quantity` no es un `Int`. Esto hace el código más seguro y expresivo.

- **Lógica en el dominio**: `Supermarket` contiene la lógica de negocio (registrar ventas, calcular ingresos). Los UseCases son coordinadores simples que buscan entidades, delegan al dominio, y persisten.

- **UseCases para evitar god objects**: separé las operaciones en UseCases independientes en lugar de centralizar todo en una clase. Si `Supermarket` sumara más responsabilidades, debería modularizarse (ej: extraer `SalesManager`, `StockManager`).

- **Excepciones de dominio**: en lugar de retornar `null` o códigos de error, el dominio lanza excepciones específicas que describen qué salió mal.

- **Mínimos comentarios**: un código limpio debería ser descriptivo y legible sin necesidad de comentarios. Por eso casi no los usé. Solo dejé algunos mínimos en estructuras de iteración complejas (como `fold` o `groupBy`) donde el propósito no es inmediatamente obvio.

### En una aplicación productiva...

Este proyecto está **desacoplado de las capas de comunicación con el exterior**. No hay controllers, DTOs de request/response, ni frameworks web.

En una app productiva, agregaría:
- **Controllers** (Spring Boot, Ktor) que reciben requests HTTP
- **DTOs** para mapear JSON ↔ Value Objects
- **Repositorios** con persistencia real (JPA, MongoDB)
- **Manejo de excepciones** global para traducir excepciones de dominio a respuestas HTTP

Pero el **dominio y los UseCases permanecerían intactos**. Esa es la ventaja de esta arquitectura: el core del negocio no depende de la infraestructura.

---

## 📚 Referencias

- [Primitive Obsession](https://refactoring.guru/es/smells/primitive-obsession)
- [Replace Data Value with Object](https://refactoring.guru/es/replace-data-value-with-object)
- [God Object - Wikipedia](https://en.wikipedia.org/wiki/God_object)