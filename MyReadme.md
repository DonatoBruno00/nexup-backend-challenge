## Nexup Backend Challenge

## 🎯 Sobre este commit

Este primer commit define el **modelo de dominio**. Sin lógica de aplicación, sin persistencia. Solo las reglas del negocio bien protegidas.

El `README.md` original lo dejé intacto. Este documento es para explicar mis decisiones de diseño.

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
