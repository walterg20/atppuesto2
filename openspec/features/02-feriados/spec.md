# Spec — 02 Feriados

> **Estado**: `DRAFT` — pendiente de aprobación (puerta **G-SPEC**)
> **Sintaxis**: EARS (ISO/IEC 25030)
> **Fuente legacy**: KB `ATPPuestos` (GeneXus 18.0.13.186702)
> **Grupo**: `02-feriados` (rama `spec/02-feriados`)

## 1. Propósito
Reimplementar entidad, repositorio, servicio y endpoints REST **solo lectura** para `Feriados`.

## 2. Alcance IN
| Objeto | Tipo | Tabla física | Ruta KB |
|---|---|---|---|
| Feriados | Transacción | `Feriados` | Root Module/Feriados/Feriados |

## 3. Supuestos (ASM)
| ID | Supuesto | Riesgo |
|---|---|---|
| ASM-01 | Nombre físico de columna = nombre lógico del atributo. Validar en **G-DATA** contra `Rentas`. | Medio |
| ASM-03 | `DATE(8)` → `date`, `DATETIME(8,5)` → `datetime/datetime2`. Validar en G-DATA. | Bajo |

## 4. Estructura extraída
| Atributo | Tipo lógico | Notas |
|---|---|---|
| `FerFecha*` | DATE(8) | PK |
| `FerDescripcion` | CHARACTER(100) | Descripción |
| `FerFchReg` | DATETIME(8,5) | Fecha registro (auditoría) |
| `FerUsuario` | CHARACTER(10) | Usuario registro (auditoría) |
| `FerTipo` | CHARACTER(1) | Tipo de feriado |

## 5. Requisitos EARS
- **R-001 (Ubicuo)** Modelar `Feriado` JPA con `@Table(name="Feriados")`, PK `FerFecha`.
- **R-006 (Ubicuo)** Tratar `Feriados` como tabla con clave `FerFecha` y campos de auditoría (`FerFchReg`, `FerUsuario`), `FerTipo`.
- **R-021 (Ubicuo)** Exponer consulta por rango de fechas: `GET /api/v1/feriados?desde=YYYY-MM-DD&hasta=YYYY-MM-DD` (solo lectura).
- **R-023 (Comportamiento no deseado)** Si rango inválido o sin resultados → respuesta según contrato uniforme.
- **R-201/R-204/R-205** KB solo lectura, BD por variables de entorno, mapeo físico exacto.

## 6. Criterios de verificación
- [ ] Entidad con `@Table(name="Feriados")` y PK `FerFecha`.
- [ ] Endpoint con filtro por rango de fechas (incluye límites).
- [ ] Unicidad de `FerFecha` verificada.
- [ ] ASM validados en G-DATA. 

## 7. Estado
**G-SPEC**: `DRAFT` — pendiente aprobación.