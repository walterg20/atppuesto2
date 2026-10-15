# Constitución — Flujo SDD y Puertas de Calidad

> Cómo fluye el trabajo desde la KB legacy hasta código con paridad verificada.

## 1. Fases

```text
F0 Diagnóstico  →  F1 Constitución  →  F2+ Ciclo por módulo SDD
```

- **F0 — Diagnóstico**: inventario de la KB, ambientes, BD. Entregable: `docs/analisis-ATPPuestos.md`. ✅ completado.
- **F1 — Constitución**: monorepo, `openspec/`, `docs/`, scaffold `backend/` + `frontend/`, `.env.example`, git. ✅ (esta fase).
- **F2+ — Ciclo por módulo**: repetir el flujo de §2 para cada módulo del alcance.

## 2. Ciclo por módulo

```text
(0) Alcance        → definir in/out y prioridad             [spec: 00-alcance]
(1) Análisis       → analista-gx extrae de la KB
(2) Spec EARS      → spec.md + plan.md + tasks.md           [PUERTA: spec aprobada]
(3) Implementación → implementador-backend + implementador-frontend (en paralelo)
(4) Verificación   → verificador-qa (tests, paridad SQL, seguridad)
(5) Cierre         → actualizar matriz de paridad + resumen en .engram/ + archivar
```

## 3. Puertas de calidad (gates)

| Puerta | Entre fases | Criterio de paso |
|---|---|---|
| **G-SPEC** | (2) → (3) | `spec.md` en sintaxis EARS, cada requisito con criterio de verificación; `plan.md` con mapeo JPA exacto; `tasks.md` casillado y verificable. **Aprobación humana.** |
| **G-DATA** | (1) → (2) | Esquema físico identificado (tabla/columna/tipo). Si no hay acceso a la BD real, derivar de la KB y marcarlo como supuesto. |
| **G-TEST** | (4) → (5) | Suite verde; criterios de aceptación cubiertos; paridad SQL revisada vs. Navigation Report. |
| **G-SEC** | (4) → (5) | Sin secretos en repo; sin vulnerabilidades conocidas en dependencias; validación de entrada presente. |

## 4. Roles (subagentes `.opencode/agents/`)

| Subagente | Produce | Escribe en |
|---|---|---|
| `coordinador` | Plan de subtareas, validación de entregables, `tasks.md` | `openspec/features/*/tasks.md` |
| `analista-gx` | `spec.md`, `plan.md`, `tasks.md` | `openspec/features/<modulo>/` |
| `implementador-backend` | Entidades JPA, repositorios, servicios, controladores | `backend/` |
| `implementador-frontend` | Componentes Angular Standalone | `frontend/` |
| `verificador-qa` | Tests, auditoría de paridad y seguridad | `backend/`, `frontend/` |

El **coordinador no escribe código**; delega y controla.

## 5. Uso de OpenSpec CLI

La carpeta `openspec/` combina la estructura nativa de OpenSpec (`changes/`, `specs/`, `config.yaml`) con nuestro layout (`constitution/`, `features/`).

- Validar: `openspec validate` / `openspec list` cuando aplique.
- El contexto del proyecto para la IA vive en `openspec/config.yaml` (campo `context`).
- No ejecutar `openspec init` de nuevo: recrea la estructura nativa y podría pisar `agents.md`/`AGENTS.md` (colisión de mayúsculas en Windows).

## 6. Memoria persistente (`.engram/`)

- Al cerrar cada módulo: guardar un resumen (decisión/hallazgo) con `mem_save`.
- Al cerrar sesión: `mem_session_summary` + `engram sync`.
- `.engram/` es portable y trackeable; la BD real de Engram vive fuera del repo.
