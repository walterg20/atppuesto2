# Spec — 01 Geográficos (piloto parcial)

> **Estado**: `DRAFT` — pendiente de aprobación (puerta **G-SPEC**)
> **Sintaxis**: EARS (ISO/IEC 25030)
> **Fuente legacy**: KB `ATPPuestos` (GeneXus 18.0.13.186702)
> **Grupo**: `01-geograficos` (rama `spec/01-geograficos`)
> **Objetivo**: Exponer catálogos geográficos y búsquedas lookup con paridad observable mínima.

## 1. Propósito
Reimplementar entidades, repositorios, servicios y endpoints REST **solo lectura** para: `Provincia`, `Localidades`, `Calles`, `CAMINOS`, `Jurisdic`, `Areas`, y los procedimientos `Bus_DesLoc` y `BuscarCamino`.

## 2. Alcance IN
| Objeto | Tipo | Tabla física | Ruta KB |
|---|---|---|---|
| Provincia | Transacción | `PROVINCI` | Root Module/Transacciones/Provincia |
| Localidades | Transacción | `Localida` | Root Module/Transacciones/Localidades |
| Calles | Transacción | `Calles` | Root Module/Transacciones/Calles |
| CAMINOS | Transacción | `CAMINOS` | Root Module/Varios/CAMINOS |
| Jurisdic | Transacción | `Jurisdic` | Root Module/Transacciones/Formulas/Jurisdic |
| Areas | Transacción | `Areas` | Root Module/Transacciones/Areas |
| Bus_DesLoc | Procedimiento | — | Root Module/F2776/Procedimientos/Bus_DesLoc |
| BuscarCamino | Procedimiento | — | (extraído) |

## 3. Supuestos (ASM)
| ID | Supuesto | Riesgo |
|---|---|---|
| ASM-01 | Nombre físico de columna = nombre lógico del atributo. MCP no lo expone. Validar en **G-DATA** contra `Rentas`. | Medio |
| ASM-02 | `NUMERIC(n,0)` → `int/smallint/bigint` según rango. | Medio |
| ASM-03 | `DATE/DATETIME` mapeados según tipos reales en `Rentas`. | Bajo |

## 4. Requisitos EARS
- **R-001 (Ubicuo)** El sistema modelará `Provincia`, `Localidades`, `Calles`, `CAMINOS`, `Jurisdic`, `Areas` como entidades JPA con `@Table(name)` exacto a la tabla física.
- **R-002 (Ubicuo)** Se preservarán PK/FK. `Localidades.ProvCod` → `Provincia.ProvCod` (FK).
- **R-010 (Dirigido por evento)** CUANDO se invoque `Bus_DesLoc` con `LocCod`, el sistema devolverá `LocNom` de `Localida` con ese `LocCod`, o resultado vacío si no existe.
- **R-011 (Dirigido por evento)** CUANDO se invoque `BuscarCamino` con `CamID`, el sistema devolverá `TRIM(CanRuta)` de `CAMINOS` con ese `CamID`, o vacío si no existe.
- **R-020 (Ubicuo)** Solo lectura bajo `/api/v1`: `provincias`, `localidades`, `calles`, `caminos`, `jurisdicciones`, `areas`.
- **R-022 (Ubicuo)** Endpoints dedicados: `GET /api/v1/localidades/{locCod}/nombre` (Bus_DesLoc), `GET /api/v1/caminos/{camId}/ruta` (BuscarCamino).
- **R-023 (Comportamiento no deseado)** Si no existe → `404` con cuerpo uniforme.
- **R-201 (Ubicuo)** KB GeneXus **solo lectura**.
- **R-204 (Ubicuo)** Conexión BD por variables de entorno (`${DB_*}`), sin secretos en repo.
- **R-205 (Ubicuo)** Respeta nombres físicos de tabla (regla 8-char) y mapeo exacto.

## 5. Criterios de verificación
- [ ] Entidades con `@Table` exacto y PK correctas.
- [ ] FK `Localidades→Provincia` verificada.
- [ ] `Bus_DesLoc` equivale a `For Each Where LocCod=&LocCod` (devuelve `LocNom` o vacío).
- [ ] `BuscarCamino` devuelve `TRIM(CanRuta)` o vacío.
- [ ] Endpoints `/api/v1/*` responden 200/404 según corresponda.
- [ ] ASM-01–ASM-03 validados en G-DATA (o documentados si faltan credenciales).

## 6. Estado
**G-SPEC**: `DRAFT` — pendiente aprobación.