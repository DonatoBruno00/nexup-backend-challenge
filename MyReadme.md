## Nexup Backend Challenge

El `README.md` original lo dejé intacto. Este documento es para explicar mis decisiones de diseño.

---

## Commit 1: Modelo de Dominio

Definí el modelo de dominio puro. Sin lógica de aplicación, sin persistencia. Solo las reglas del negocio bien protegidas.

## Commit 2: Caso de Uso - Registrar Venta

Agregué el primer caso de uso: `RegisterSaleUseCase`. 

### ¿Por qué Use Cases?

Elegí un enfoque orientado a casos de uso porque:

- **Separación clara**: cada funcionalidad del negocio es un caso de uso independiente
- **Testeable**: puedo probar cada operación de forma aislada con mocks simples
- **Escalable**: agregar nuevas funcionalidades = agregar nuevos UseCases sin tocar los existentes
- **El dominio se mantiene limpio**: las entidades no conocen repositorios ni infraestructura

El UseCase actúa como coordinador: busca las entidades, delega la lógica al dominio (`Supermarket.registerSale()`), y persiste los cambios.

### Estructura agregada

```
repository
├── ProductRepository (interface)
├── SupermarketRepository (interface)
└── impl
    ├── ProductRepositoryImpl (in-memory)
    └── SupermarketRepositoryImpl (in-memory)

usecase
├── RegisterSaleUseCase
└── GetSoldQuantityUseCase
```

### Tests

Creé `TestData` para centralizar los datos de prueba y mantener consistencia entre tests. Los tests cubren:
- Venta exitosa
- SupermarketNotFoundException
- ProductNotFoundException  
- InsufficientStockException

## Commit 3: Caso de Uso - Obtener Cantidad Vendida

Agregué `GetSoldQuantityUseCase`: dado un `ProductId`, retorna la cantidad total vendida de ese producto en todos los supermercados.

- Valida que el producto exista (lanza `ProductNotFoundException` si no)
- Itera sobre todos los supermercados y suma las cantidades vendidas
- Agregué `findAll()` al `SupermarketRepository`

## 📦 Estructura

```
domain
├── entity
│   ├── Product
│   ├── Supermarket
│   ├── SupermarketChain
│   ├── Stock
│   └── Sale
├── exception
│   ├── InsufficientStockException
│   ├── ProductNotFoundException
│   └── SupermarketNotFoundException
└── valueobject
    ├── ProductId
    ├── SupermarketId
    ├── SaleId
    ├── Quantity
    └── Amount
```

## 🧩 Entidades

- **Product**: describe un producto (id, nombre, precio). No conoce stock ni ventas.
- **Supermarket**: el aggregate root. Maneja su stock, registra ventas, mantiene coherencia.
- **SupermarketChain**: contenedor de supermercados y catálogo de productos. Las consultas agregadas (top 5, ingresos totales) van en UseCases.
- **Stock**: cantidades por producto. Protege que no haya stock negativo.
- **Sale**: hecho inmutable de una venta (producto, cantidad, monto, fecha).

## ⚠️ Excepciones

- **InsufficientStockException**: no hay stock suficiente.
- **ProductNotFoundException**: producto no existe.
- **SupermarketNotFoundException**: supermercado no existe.

## 🔹 Value Objects

Evité usar primitivos directamente (*Primitive Obsession*). Cada Value Object:
- Constructor privado → solo se crea via `of()`, `new()`, `zero()`
- Campo privado → acceso solo por getters
- Valida sus invariantes (ej: `Quantity` no puede ser negativa, `Amount` no puede ser negativo)

Referencias:
- https://refactoring.guru/es/smells/primitive-obsession
- https://refactoring.guru/es/replace-data-value-with-object

## 🔗 Relaciones

- `SupermarketChain` contiene muchos `Supermarket` y un catálogo de `Product`
- `Supermarket` tiene un `Stock` (1:1) y muchas `Sale`
- `Stock` mapea `Product` → `Quantity`
- `Sale` referencia un `Product`
