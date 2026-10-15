# Constitución — Convenciones y Guardarraíles

> Reglas operativas de trabajo del repositorio. Aplican a humanos y a subagentes.

## 1. Guardarraíles absolutos

| # | Regla | Consecuencia de violación |
|---|---|---|
| G1 | **Entorno legacy GeneXus = SOLO LECTURA.** Prohibido modificar, compilar, "Build All", o eliminar objetos/tablas en la KB original. Las conversiones se hacen sobre copias. | Bloqueo de la tarea; la KB es la fuente de verdad inmutable. |
| G2 | **Spec-Driven Development.** Ningún código de aplicación sin una spec aprobada en `openspec/features/<modulo>/`. | El código no se acepta ni se mergea. |
| G3 | **Sin secretos en el repositorio.** Credenciales, tokens y cadenas de conexión solo por variables de entorno. | Remoción del commit y rotación del secreto. |
| G4 | **Archivos de configuración < 500 líneas.** | Refactor/partición antes de continuar. |
| G5 | **No heredar artefactos** del replatform anterior (`F:\Replatform\ATPPuestos`). Es solo línea base de comparación. | — |

## 2. Estructura de carpetas

```text
/
├── agents.md                  ← Arnés de control principal
├── .engram/                   ← Memoria persistente del agente
├── openspec/                  ← Especificaciones formales (EARS)
│   ├── constitution/          ← Reglas del stack y del proceso
│   └── features/              ← Especificaciones por módulo
├── docs/                      ← Documentación y sistema de diseño (design.md)
├── backend/                   ← Proyecto Java Spring Boot
└── frontend/                  ← Proyecto Angular Standalone
```

## 3. Nomenclatura

- **Módulos de spec**: `openspec/features/<NN>-<modulo-kebab>/` (p. ej. `01-provincias`).
- **Artefactos por módulo**: `spec.md`, `plan.md`, `tasks.md`.
- **Rutas REST**: `/api/v1/<recurso-plural>`.
- **Entidades JPA**: nombre lógico en PascalCase; `@Table`/`@Column` con el nombre **físico exacto**.
- **Paquetes Java**: `ar.com.atp.puestos.<capa>` (a confirmar en el primer módulo).
- **Componentes Angular**: `kebab-case` + sufijo (`-list`, `-form`, `-detail`).
- **IDs de specs/requisitos**: `R-<NNN>` para requisitos EARS; `T-<NNN>` para tareas.

## 4. Idiomas

- Documentación, specs y mensajes de commit: **español**.
- Código (identificadores), nombres de tabla/columna y rutas técnicas: **inglés** o el nombre físico heredado.
- Commits: *Conventional Commits* (`feat:`, `fix:`, `docs:`, `chore:`, `spec:`).

## 5. Requisitos en sintaxis EARS

Cada requisito de negocio se redacta en EARS, en una de estas formas:

- **Ubicuo**: `El sistema deberá <respuesta>.`
- **Dirigido por evento**: `CUANDO <disparador>, el sistema deberá <respuesta>.`
- **Dirigido por estado**: `MIENTRAS <estado>, el sistema deberá <respuesta>.`
- **Comportamiento no deseado**: `SI <condición>, ENTONCES el sistema deberá <respuesta>.`
- **Opcional**: `DONDE <característica>, el sistema deberá <respuesta>.`

Todo requisito incluye **criterio de verificación** (cómo se comprueba: test, inspección, demo).

## 6. Paridad funcional (ISO/IEC 25030)

- La paridad se mide por **comportamiento observable** (entradas → salidas → efectos en BD), no por igualdad estructural del código.
- Cada módulo declara su **matriz de paridad**: requisito legacy ↔ requisito nuevo ↔ prueba.
- Los desvíos intencionales se documentan como **decisiones** con su justificación.
