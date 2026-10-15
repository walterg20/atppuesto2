# ATPPuestos2 — Replatforming GeneXus → Java/Angular

Reimplementación del sistema legacy **GeneXus 18** (`ATPPuestos`) en una arquitectura
desacoplada: **Backend Java 17 + Spring Boot 3.x (API REST)** y **Frontend Angular 17+ Standalone**.

El arnés de control está en [`agents.md`](./agents.md).

## Estado de las fases

| Fase | Descripción | Estado |
|---|---|---|
| **F0** | Diagnóstico e inventario de la KB | ✅ `docs/analisis-ATPPuestos.md` |
| **F1** | Constitución del monorepo | ✅ (esta estructura) |
| **F1.1** | Alcance migrable in/out (EARS) | 📝 `openspec/features/00-alcance/` (borrador) |
| **F2+** | Ciclo SDD por módulo | ⏳ pendiente de aprobación del alcance |

## Estructura

```text
.
├── agents.md                  # Arnés de control principal
├── .engram/                   # Memoria persistente (portable, trackeable)
├── openspec/                  # Especificaciones formales (EARS)
│   ├── config.yaml            # Contexto del proyecto para OpenSpec
│   ├── constitution/          # Reglas del stack y del proceso
│   │   ├── stack.md
│   │   ├── conventions.md
│   │   └── sdd-workflow.md
│   └── features/              # Especificaciones por módulo
│       └── 00-alcance/        # {spec,plan,tasks}.md
├── docs/                      # Documentación
│   ├── analisis-ATPPuestos.md # Diagnóstico F0
│   └── design.md              # Sistema de diseño (derivado de ATPEvo2.css)
├── backend/                   # Java 17 + Spring Boot 3.x (Maven Wrapper)
└── frontend/                  # Angular 17+ Standalone
```

## Reglas no negociables

1. **La KB GeneXus legacy es SOLO LECTURA.** Prohibido compilar/modificar/eliminar objetos.
2. **Spec-Driven Development**: sin spec aprobada en `openspec/features/<modulo>/`, no hay código.
3. **Paridad de BD**: reutilizar el esquema SQL Server preservando nombres físicos exactos.
4. **Sin secretos en el repo**; solo variables de entorno (ver `.env.example`).
5. Archivos de configuración **< 500 líneas**.

Detalle completo en [`openspec/constitution/`](./openspec/constitution/).

## Entorno de datos

- KB: `F:\Modelo\Gx18\ATPPuestos` (GeneXus 18 U13) — solo lectura.
- BD de la aplicación: `Rentas` @ `devmssqlatp.ecom.com.ar` (**acceso pendiente de definir**).
- BD de metadatos KB: `GX_KB_ATPPuestos` @ `WALTERG20\SQLEXPRESS2019`.

## Puesta en marcha (WIP)

```bash
# Backend (cuando exista el scaffold)
cd backend && ./mvnw spring-boot:run

# Frontend (cuando exista el scaffold)
cd frontend && npm ci && npm start
```

---

_Proyecto iniciado F1 — 2026. La KB original se conserva intacta como fuente de verdad._
