# Spec — 01 Núcleo de Datos (piloto)

> **Estado**: `APPROVED` — puerta **G-SPEC** aprobada el **2026-10-16** por el responsable.
> **Enmendado (G-DATA, 2026-10-16)**: §4.3/§4.4/§4.5 y R-002/R-006/R-036/R-037 corregidos tras
> validar el esquema físico real contra la BD `Rentas` (ver §4.5 y `docs/analisis-ATPPuestos.md`).
> **Sintaxis**: EARS (ISO/IEC 25030). Cada requisito incluye criterio de verificación.
> **Fuente legacy**: KB `ATPPuestos` (GeneXus **18.0.13.186702**), índice "Booting".
> **Módulo**: `01-nucleo-datos` — **piloto** del proceso SDD.
> **Entradas**: `docs/analisis-ATPPuestos.md` (F0), `openspec/features/00-alcance/{spec,plan}.md`,
> y extracción directa de la KB vía MCP (`genexus_read` / `genexus_structure`, solo lectura).
> **Salidas esperadas**: entidades JPA, repositorios, servicios de consulta, controladores REST
> bajo `/api/v1` y (fase 2) la pantalla Angular `ListadoLocalidades`.

## 1. Propósito

Validar **de punta a punta** la cadena de replatforming con un conjunto acotado y sin lógica de
negocio compleja: `KB → spec EARS → entidad JPA (nombre físico exacto) → servicio → REST →
Angular → prueba`. Es el molde que se replicará en los módulos 02–10.

El módulo cubre las **tablas maestras geográficas y de catálogo** (provincias, localidades, calles,
caminos, áreas, jurisdicciones, feriados, productos) y los **dos procedimientos de búsqueda** que
las consumen.

## 2. Definiciones

- **Master data**: catálogo de baja volatilidad, clave natural de negocio, sin transaccionalidad compleja.
- **Paridad observable**: igualdad de comportamiento entrada→salida→efecto en BD, no de estructura de código.
- **Supuesto de esquema (ASM)**: definición física derivada de la metadata de la KB cuando aún no hay acceso a la BD `Rentas` (regla R-203).
- **Nivel (Level)**: agrupación de atributos subordinados a la clave del nivel superior (clave compuesta en tabla hija).

## 3. Alcance

### 3.1 IN — objetos legacy reimplementados

| Grupo | Objetos legacy | Evidencia |
|---|---|---|
| Transacciones maestras (8) | `Provincia`, `Localidades`, `Calles`, `CAMINOS`, `Areas`, `Jurisdic`, `Feriados`, `Productos` | extracción `Structure` (§4.1) |
| Procedimientos de búsqueda (2) | `Bus_DesLoc`, `BuscarCamino` | extracción `Rules`/`Source` (§4.2) |
| ABM de escritura (8 maestras) | Alta/baja/modificación de las transacciones del piloto | decisión **P-01** (2026-10-16): ABM completo |
| Web funcional relacionado (1) | `ListadoLocalidades` + pantallas ABM | inventario F0 §WebPanels; **fase 2** (web, replicando CSS/JS/HTML del origen) |

### 3.2 OUT — no se reimplementa en este módulo

- ~~ABM de escritura (alta/baja/modificación) de las maestras: el piloto expone **solo lectura**.~~ **Resuelto (P-01, 2026-10-16)**: el piloto incluye **ABM completo** de escritura vía REST (§5.4) y en fase 2 las pantallas ABM replican el CSS/JS/HTML del origen GeneXus.
- Objetos de otros módulos que **consumen** estas tablas: `F2276` (usa `Bus_DesLoc`) y `CodigoBarra` (usa `BuscarCamino`) pertenecen a los módulos 05 y de códigos de barra respectivamente.
- Design System Unanimo, `Gx00xx`, EOs/SDTs de plataforma (ver `00-alcance` §4).

## 4. Modelo extraído de la KB (fuente de verdad)

### 4.1 Transacciones (atributos, tipos lógicos y PK)

| Transacción | Ruta en la KB | Estructura |
|---|---|---|
| `Provincia` | `Root Module/Transacciones/Provincia` | `ProvCod* : NUMERIC(4)`, `ProvNombre : VARCHAR(40)` |
| `Localidades` | `Root Module/Transacciones/Localidades` | `LocCod* : NUMERIC(5)`, `LocNom : CHARACTER(20)`, `LocNomProv : CHARACTER(15)`, `LocCodPost : NUMERIC(4)`, `ProvCod : NUMERIC(4)` |
| `Calles` | `Root Module/Transacciones/Calles` | `CallCod* : NUMERIC(4)`, `CallNom : CHARACTER(22)` |
| `CAMINOS` | `Root Module/Varios/CAMINOS` | `CamID* : CHARACTER(3)`, `CanRuta : CHARACTER(100)` |
| `Areas` | `Root Module/Transacciones/Areas` | `AreaId* : NUMERIC(10)`, `AreaDesc : CHARACTER(50)` |
| `Jurisdic` | `Root Module/Transacciones/Formulas/Jurisdic` | `JurCod* : NUMERIC(3)`, `JurNom : CHARACTER(20)` |
| `Feriados` | `Root Module/Feriados/Feriados` | `FerFecha* : DATE(8)`, `FerDescripcion : CHARACTER(100)`, `FerFchReg : DATETIME(8,5)`, `FerUsuario : CHARACTER(10)`, `FerTipo : CHARACTER(1)` |
| `Productos` | `Root Module/Transacciones/Productos` | ver §4.1.1 |

`*` = clave primaria. **Ninguna** de estas transacciones declara reglas ni eventos (verificado con
`genexus_read` parts `Rules`/`Events`: vacías). Son tablas de **datos maestros sin lógica declarativa**.

> **Nota (G-DATA 2026-10-16):** los tipos y longitudes anteriores son los **lógicos** de la KB. Los
> **tipos y anchuras físicos** reales de `Rentas` difieren (p. ej. `char(n)` fija, `datetime`,
> `decimal(10,0)`) y están **validados en §4.5** (fuente de autoridad para el mapeo JPA). La
> `Productos` física es `PRODUCTO` (raíz) y `PRODUCT1` (nivel 1).

#### 4.1.1 `Productos` (transacción de dos niveles)

Nivel 0 (raíz) — tabla física `Producto`:

| Atributo | Tipo | Notas |
|---|---|---|
| `ProdCod*` | NUMERIC(3) | PK |
| `ProdDesc` | CHARACTER(25) | |
| `ProdTipCal` | NUMERIC(1) | Tipo de cálculo |
| `TPPriID` | NUMERIC(1) | Tipo de producción |
| `ProdIMax1` | NUMERIC(12,2) | **Fórmula** (derivado) |
| `ProdIMax2` | NUMERIC(12,2) | **Fórmula** |
| `ProdIMax3` | NUMERIC(12,2) | **Fórmula** |
| `ProdIMax4` | NUMERIC(12,2) | **Fórmula** |
| `UMedNroId` | CHARACTER(3) | |
| `UMedNombre` | VARCHAR(40) | |
| `ProdAcop` | CHARACTER(1) | ¿Acopiador? |
| `ProdAnula` | NUMERIC(1) | Anulado |

Nivel 1 — tabla física `Product1`, clave compuesta `ProdCod + ProdFchVig`:

| Atributo | Tipo | Notas |
|---|---|---|
| `ProdFchVig*` | DATE(8) | PK del nivel |
| `ProdImpu1` | NUMERIC(12,2) | Importe fijo entrada |
| `ProdImpu2` | NUMERIC(12,2) | Imp. cosecha mec. entrada |
| `ProdImpu3` | NUMERIC(12,2) | Importe fijo salida |
| `ProdImpu4` | NUMERIC(12,2) | Imp. cosecha mec. salida |

**Fórmulas (semántica GeneXus `max(atributo, condición, default, atributo)` = valor del 4.º argumento
en la fila cuyo 1.º argumento es máximo bajo la condición; `default` si no hay filas):**

- `ProdIMax1 = max(ProdFchVig, ProdFchVig <= serverdate(), 0, ProdImpu1)` → `ProdImpu1` de la vigencia **más reciente** con `ProdFchVig <= hoy`; `0` si no hay ninguna.
- `ProdIMax2 = max(ProdFchVig, ProdFchVig <= serverdate(), 0, ProdImpu2)`
- `ProdIMax3 = max(ProdFchVig, ProdFchVig <= serverdate(), 0, ProdImpu3)`
- `ProdIMax4 = max(ProdFchVig, ProdFchVig <= serverdate(), 0, ProdImpu4)`

### 4.2 Procedimientos de búsqueda

| Proc | Firma | Fuente (verbatim) | Consumidor |
|---|---|---|---|
| `Bus_DesLoc` | `parm(in:&LocCod, out:&LocNom);` | `for each where LocCod = &LocCod` / `&LocNom = LocNom` / `endfor` | `F2276` |
| `BuscarCamino` | `Parm(in:&CamID, out:&CanRuta);` | `For Each Where CamID = &CamID` / `&CanRuta = Trim(CanRuta)` / `EndFor` | `CodigoBarra` |

Variables tipadas: `Bus_DesLoc` → `&LocCod NUMERIC(5)`, `&LocNom CHARACTER(20)`;
`BuscarCamino` → `&CamID CHARACTER(3)`, `&CanRuta CHARACTER(100)`.

### 4.3 Relaciones e índices

- **FK**: `Localidades.ProvCod → Provincia.ProvCod` es una **asociación lógica** (índice `ILocalida` sobre `ProvCod`); **no existe FK física** en la BD `Rentas` (validado en G-DATA, §4.5). La integridad referencial es responsabilidad de la aplicación.
- **FK físicas reales** hacia las tablas del piloto: solo `Rastrillo → CALLES` y `VElectronica → AREAS` (fuera del piloto).
- **PK física**: cada tabla tiene índice único automático sobre su clave (`IProvincia`/`ProvCod`, `ILocalidades`/`LocCod`, `ICalles`/`CallCod`, `PK_CAMINOS_54DBDB6F`/`CamID`, `IAreas`/`AreaId`, `IJurisdic`/`JurCod`; `Feriados`/`FerFecha`; `Producto`/`ProdCod`; `Product1`/`ProdCod+ProdFchVig`).
- **Nombre de tabla física**: regla observada = nombre de transacción **truncado a 8 caracteres**
  (`Localidades→Localida`, `Productos→Producto`, `Provincia→PROVINCI`). Confirmado con
  `genexus_structure get_indexes` (`result.name`).
- **Nombre de columna física** (G-DATA, §4.5): igual al nombre lógico del atributo **truncado a 10
  caracteres**. Caso observado: `FerDescripcion → FerDescrip`. El resto de columnas del piloto
  coinciden con el nombre lógico.

### 4.4 Supuestos de esquema (ASM) — **validados contra `Rentas` (G-DATA, 2026-10-16)**

| ID | Supuesto original | Resultado | Evidencia / corrección |
|---|---|---|---|
| ASM-01 | El **nombre físico de columna** es igual al nombre lógico del atributo. | **Matizado** | Se cumple salvo **truncado a 10 caracteres**: `FerDescripcion → FerDescrip`. El resto coincide. |
| ASM-02 | `NUMERIC(n,0)` mapea a `smallint`/`int`/`bigint` (no a `decimal`). | **Parcialmente falso** | `Areas.AreaId` es `decimal(10,0)` → `BigDecimal`. El resto sí son `smallint`/`int`. |
| ASM-03 | `DATE(8)` mapea a `date` (no a `datetime`). | **Falso** | `Feriados.FerFecha` y `Product1.ProdFchVig` son **`datetime`** → `LocalDateTime`. |
| ASM-04 | `CHARACTER(n)` mapea a `varchar(n)`. | **Falso** | Es **`char(n)` fijo** (rellena con espacios → prever `trim`). Excepciones `varchar`: `ProvNombre(40)`, `AreaEmails(255)`, `AreaRespons(100)`. |
| ASM-05 | `DATETIME(8,5)` mapea a `datetime2`/`datetime`. | **Confirmado** | `Feriados.FerFchReg` es `datetime`. |

Hallazgos adicionales (detalle en §4.5): existen **columnas legacy/huérfanas** en la BD ausentes en
la KB; hay columnas **`NOT NULL` sin `DEFAULT`**; y **ninguna PK es `IDENTITY`** (no autoincremental).

### 4.5 Esquema físico validado (G-DATA) — fuente de autoridad para el mapeo

Tablas reales en `Rentas` (nombres truncados a 8 chars), con tipo físico, nulabilidad y tipo Java:

| Tabla | Columna | Tipo físico | Nula | ¿En KB? | Tipo Java |
|---|---|---|---|---|---|
| `PROVINCI` | `ProvCod` | `smallint` | NO (PK) | sí | `Integer` |
| | `ProvNombre` | `varchar(40)` | NO | sí | `String` |
| | `ProvCodAfip` | `int` | **NO** | **no (legacy)** | `Integer` ← default `0` al alta |
| `Localida` | `LocCod` | `int` | NO (PK) | sí | `Integer` |
| | `LocNom` | `char(50)` | sí | sí | `String` (trim) |
| | `LocNomProv` | `char(19)` | sí | sí | `String` (trim) |
| | `LocCodPost` | `smallint` | sí | sí | `Integer` |
| | `ProvCod` | `smallint` | sí | sí (FK lógica) | `Integer` / `@ManyToOne` |
| | `LocCodAfip` | `int` | **NO** | **no (legacy)** | `Integer` ← default `0` al alta |
| `CALLES` | `CallCod` | `int` | NO (PK) | sí | `Integer` |
| | `CallNom` | `char(50)` | sí | sí | `String` (trim) |
| | `CallAlt1/2/3` | `char(50)` | sí | **no (legacy)** | `String` |
| `CAMINOS` | `CamID` | `char(3)` | NO (PK) | sí | `String` |
| | `CanRuta` | `char(100)` | sí | sí | `String` (trim) |
| `Areas` | `AreaId` | `decimal(10,0)` | NO (PK) | sí | `BigDecimal` |
| | `AreaDesc` | `char(50)` | sí | sí | `String` (trim) |
| | `AreaBaja` | `smallint` | sí | **no (legacy)** | `Integer` |
| | `AreaEmails` | `varchar(255)` | **NO** | **no (legacy)** | `String` ← default `""` al alta |
| | `AreaAdjun` | `bit` | sí | **no (legacy)** | `Boolean` |
| | `AreaRespons` | `varchar(100)` | sí | **no (legacy)** | `String` |
| `Jurisdic` | `JurCod` | `smallint` | NO (PK) | sí | `Integer` |
| | `JurNom` | `char(20)` | sí | sí | `String` (trim) |
| `FERIADOS` | `FerFecha` | `datetime` | NO (PK) | sí | `LocalDateTime` |
| | `FerDescrip` | `char(100)` | **NO** | sí (`FerDescripcion`) | `String` (trim) |
| | `FerFchReg` | `datetime` | **NO** | sí (auditoría) | `LocalDateTime` |
| | `FerUsuario` | `char(10)` | **NO** | sí (auditoría) | `String` |
| | `FerTipo` | `char(1)` | sí | sí | `String` |
| `PRODUCTO` | `ProdCod` | `smallint` | NO (PK) | sí | `Integer` |
| | `ProdDesc` | `char(25)` | **NO** | sí | `String` (trim) |
| | `ProdTipCal` | `smallint` | **NO** | sí | `Integer` |
| | `TPPriID` | `smallint` | **NO** | sí | `Integer` |
| | `UMedNroId` | `char(3)` | sí | sí | `String` |
| | `ProdAcop` | `char(1)` | sí | sí | `String` |
| | `ProdReppa` | `char(1)` | sí | **no (legacy)** | `String` |
| | `ProdAnula` | `smallint` | **NO** | sí | `Integer` |
| `PRODUCT1` | `ProdCod` | `smallint` | NO (PK) | sí | `Integer` |
| | `ProdFchVig` | `datetime` | NO (PK) | sí | `LocalDateTime` |
| | `ProdImpu1/2` | `money` | **NO** | sí | `BigDecimal` |
| | `ProdImpu3/4` | `money` | sí | sí | `BigDecimal` |
| | `ProdImpu5` | `money` | sí | **no (legacy)** | `BigDecimal` |
| | `ProdFeLog` | `datetime` | sí | **no (legacy)** | `LocalDateTime` |
| | `ProdUsu` | `char(10)` | sí | **no (legacy)** | `String` |

Reglas de persistencia derivadas (G-DATA):

- **Sin `IDENTITY`**: ninguna PK es autoincremental → el `POST` **recibe la PK** en el body (R-037);
  duplicado → `409` (R-031).
- **Sin `DEFAULT`**: toda columna `NOT NULL` debe enviarse en el alta. Las legacy `NOT NULL`
  (`ProvCodAfip`, `LocCodAfip`, `AreaEmails`) se rellenan con `0`/`0`/`""` si el cliente no las
  provee (R-036).
- **`char(n)` fija**: leer con `trim` para eliminar el relleno (ASM-04 / R-002).
- **Columnas legacy**: se mapean todas (fidelidad de lectura y para no romper el `INSERT`), pero no
  se exponen como atributos de negocio (detalle en `plan.md` §2.4).

## 5. Requisitos funcionales (EARS)

### 5.1 Entidades y persistencia

- **R-001 (Ubicuo)** El sistema deberá modelar las **ocho transacciones maestras** del piloto como entidades JPA mapeadas al esquema físico existente (§4.1), sin generar DDL.
  - *Verificación*: existe una entidad por transacción; `@Table(name=…)` coincide con §4.3; prueba de integración de lectura contra la BD real (G-DATA).
- **R-002 (Ubicuo)** El sistema deberá preservar **claves primarias**, **tipos y longitudes** originales de cada atributo (§4.5), aplicando `trim` a las columnas `char(n)` para eliminar el relleno.
  - *Verificación*: tabla de correspondencia atributo↔columna↔tipo en `plan.md` §2; una prueba por entidad (incluye lectura de una columna `char(n)`).
- **R-003 (Ubicuo)** El sistema deberá modelar la relación `Localidades → Provincia` como **asociación** sobre `ProvCod` (clave foránea), sin exponerla como texto libre.
  - *Verificación*: consulta de localidad devuelve la provincia asociada; prueba con `LocCod` inexistente.
- **R-004 (Ubicuo)** El sistema deberá modelar `Productos` en **dos niveles** (`Producto` raíz y `Product1` vigencias) con clave compuesta `ProdCod+ProdFchVig`, preservando la carga de vigencias junto al producto.
  - *Verificación*: un producto con N vigencias persiste y recupera N filas hijas en orden por `ProdFchVig`.
- **R-005 (Ubicuo)** El sistema deberá calcular los importes vigentes `ProdIMax1..4` como atributos **derivados** (§4.1.1), evaluados contra la fecha del servidor, retornando `0` cuando no exista vigencia aplicable.
  - *Verificación*: pruebas parametrizadas con (a) sin vigencias → `0`, (b) vigencias pasadas/futuras → toma la más reciente `<= hoy`, (c) vigencia exactamente hoy.
- **R-006 (Ubicuo)** El sistema deberá tratar `Feriados` como tabla de fecha clave (`FerFecha`, física `datetime`) con campos de auditoría (`FerFchReg`, `FerUsuario`) y `FerTipo`, mapeando el atributo lógico `FerDescripcion` a la columna física truncada `FerDescrip` (§4.5).
  - *Verificación*: consulta por rango de fechas; prueba de unicidad de `FerFecha`.

### 5.2 Procedimientos de búsqueda

- **R-010 (Dirigido por evento)** CUANDO se invoque `Bus_DesLoc` con `LocCod`, el sistema deberá devolver `LocNom` de la fila de `Localida` cuyo `LocCod` coincida, o un resultado **vacío** si no existe.
  - *Verificación*: prueba unitaria con existencia e inexistencia; la consulta generada es equivalente al `For Each ... Where LocCod = &LocCod`.
- **R-011 (Dirigido por evento)** CUANDO se invoque `BuscarCamino` con `CamID`, el sistema deberá devolver `TRIM(CanRuta)` de la fila de `CAMINOS` cuyo `CamID` coincida, o vacío si no existe.
  - *Verificación*: prueba con `CanRuta` con espacios y sin coincidencia; verifica el `Trim`.

### 5.3 API REST (contratos en `plan.md` §3)

- **R-020 (Ubicuo)** El sistema deberá exponer las maestras bajo `/api/v1` con operaciones de **consulta y escritura (ABM)**: `provincias`, `localidades`, `calles`, `caminos`, `areas`, `jurisdicciones`, `feriados`, `productos`.
  - *Verificación*: pruebas de contrato (códigos 200/404 en lectura; 201/400/404/409 en escritura) por endpoint.
- **R-021 (Ubicuo)** El sistema deberá permitir filtrar `localidades` por `ProvCod` y `feriados` por rango de fechas.
  - *Verificación*: pruebas con filtros combinados y sin filtro.
- **R-022 (Ubicuo)** El sistema deberá exponer `Bus_DesLoc` y `BuscarCamino` como recursos dedicados que reflejen su contrato de búsqueda (§5.2).
  - *Verificación*: `GET /api/v1/localidades/{locCod}/nombre` y `GET /api/v1/caminos/{camId}/ruta` con casos existente/inexistente.
- **R-023 (Comportamiento no deseado)** SI un recurso solicitado no existe, ENTONCES el sistema deberá responder `404` con un cuerpo de error uniforme (sin filtrar detalles internos).
  - *Verificación*: prueba de cada endpoint con ID inexistente.

### 5.4 Escritura (ABM — decisión P-01)

- **R-030 (Ubicuo)** El sistema deberá permitir **crear**, **modificar** y **eliminar** registros de las ocho maestras vía `POST`/`PUT`/`DELETE` sobre los mismos recursos de `/api/v1` (§5.3), preservando PK, tipos y longitudes originales (R-002).
  - *Verificación*: prueba de ciclo completo crear→modificar→eliminar por entidad contra BD real; 201 con `Location` en alta.
- **R-031 (Comportamiento no deseado)** SI un `POST` usa una clave primaria ya existente, ENTONCES el sistema deberá responder `409` con el cuerpo de error uniforme; SI el `PUT`/`DELETE` referencia una clave inexistente, `404`.
  - *Verificación*: prueba de duplicado y de referencia inexistente por entidad.
- **R-032 (Ubicuo)** El sistema deberá rechazar payloads inválidos (tipos, longitudes excedidas, nulos en atributos obligatorios) con `400` y detalle de violaciones usando Jakarta Bean Validation.
  - *Verificación*: prueba por DTO de escritura con al menos una violación de longitud y una de nulidad.
- **R-033 (Dirigido por contexto)** MIENTRAS se crea o modifique un `Feriado`, el sistema deberá rellenar automáticamente `FerFchReg` (fecha/hora del servidor) y `FerUsuario` (usuario autenticado o valor de configuración en fase 1).
  - *Verificación*: alta sin enviar auditoría → persiste con valores generados.
- **R-034 (Dirigido por contexto)** MIENTRAS se cree o modifique un `Producto`, el sistema deberá persistir el producto (nivel 0) y sus vigencias (nivel 1) **atómicamente**, validando que cada `ProdFchVig` sea única por `ProdCod`.
  - *Verificación*: prueba de alta de producto con N vigencias y de duplicado de vigencia (409).
- **R-035 (Ubicuo)** El sistema deberá recalcular `ProdIMax1..4` **derivados** (R-005) tras cada escritura de vigencias, sin persistirlos.
  - *Verificación*: tras agregar una vigencia más reciente, `GET /productos/{prodCod}/importes-vigentes` refleja el nuevo valor.
- **R-036 (Ubicuo)** El sistema deberá **mapear y persistir las columnas legacy** del esquema físico (§4.5) aunque no existan en la KB, para garantizar la fidelidad de lectura y que el alta no falle por columnas `NOT NULL` sin `DEFAULT`; en el alta, si el cliente no las envía, aplicará los valores por defecto `0` (`ProvCodAfip`, `LocCodAfip`) y `""` (`AreaEmails`).
  - *Verificación*: alta de `Provincia`/`Localidad`/`Area` sin las columnas legacy → persiste con el default; la lectura devuelve el registro.
- **R-037 (Ubicuo)** El sistema deberá **recibir la clave primaria** en el cuerpo del alta (no hay columnas `IDENTITY` en el esquema), validando su unicidad (R-031).
  - *Verificación*: `POST` sin PK → `400`; `POST` con PK existente → `409`.

## 6. Requisitos de proceso y no funcionales

- **R-201 (Ubicuo)** La KB GeneXus deberá permanecer en **solo lectura** durante el módulo.
  - *Verificación*: no hay operaciones de escritura/compilación sobre `F:\Modelo\Gx18\ATPPuestos`.
- **R-202 (Ubicuo)** El sistema deberá declarar la **matriz de paridad** legacy↔nuevo↔prueba (ver `plan.md` §5) antes de la puerta G-TEST.
  - *Verificación*: matriz completa y trazable.
- **R-203 (Comportamiento no deseado)** SI no hay acceso a la BD `Rentas`, ENTONCES el sistema deberá derivar el esquema de la KB y **marcar cada supuesto** (§4.4).
  - *Verificación*: entidades anotadas con la transacción/atributo origen; ASM-01…05 resueltos (validados contra `Rentas` en G-DATA).
- **R-204 (Ubicuo)** El sistema deberá tomar la cadena de conexión de **variables de entorno**, sin credenciales en el repositorio.
  - *Verificación*: `application.yml` referencia `${DB_*}`; escaneo sin secretos.
- **R-205 (Ubicuo)** El mapeo deberá respetar las **reglas de nombre físico legacy**: tablas truncadas a 8 caracteres y columnas a 10 caracteres (§4.3/§4.5).
  - *Verificación*: revisión de `@Table(name)`/`@Column(name)` contra §4.3 y §4.5.

## 7. Pendientes de decisión (R-106) — **resueltos 2026-10-16**

| ID | Pendiente | Dueño | Resolución |
|---|---|---|---|
| P-01 | ¿El piloto incluye ABM de escritura o solo lectura? | Responsable | **ABM completo** (R-030…R-035). Fase 2: pantallas ABM replicando CSS/JS/HTML del origen GeneXus. |
| P-02 | Confirmación de ASM-01…05 contra la BD `Rentas`. | Responsable | **Resuelto (G-DATA, 2026-10-16)** — ASM validados/corregidos contra `Rentas`; ver §4.4/§4.5. |
| P-03 | Formato del endpoint de `productos` con vigencias (anidado vs. plano). | Analista + Responsable | **Anidado**: `ProductoDto.vigencias[]` (fiel al nivel 1 de GeneXus, 1 viaja por alta/modificación). |

## 8. Criterios de aceptación

- [ ] R-001…R-006 implementados con prueba por entidad.
- [ ] R-010, R-011 con equivalencia verificada contra la lógica `For Each` original.
- [ ] R-020…R-023 con pruebas de contrato (lectura).
- [ ] R-030…R-037 con pruebas de contrato (escritura: 201/400/404/409) y ciclo ABM por entidad.
- [ ] R-201…R-205 verificados; **ASM-01…05 validados** (G-DATA 2026-10-16, ver §4.4/§4.5).
- [ ] Matriz de paridad (`plan.md` §5) completa, incluyendo filas de escritura.
- [ ] Suite verde (G-TEST) y sin secretos (G-SEC).
