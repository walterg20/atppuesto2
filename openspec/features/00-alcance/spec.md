# Spec — 00 Alcance del Replatforming

> **Estado**: `APPROVED` — aprobado por el responsable (puerta **G-SPEC**, 2026-10-15).
> Módulo piloto autorizado: `01-nucleo-datos`.
> **Sintaxis**: EARS (ISO/IEC 25030). Cada requisito tiene criterio de verificación.
> **Fuente**: `docs/analisis-ATPPuestos.md` (F0).
> **Módulo**: `00-alcance` (transversal — no genera código; define el universo del proyecto).

## 1. Propósito

Delimitar qué parte de la KB GeneXus `ATPPuestos` se reimplementa en el stack objetivo
(Java 17 + Spring Boot 3.x / Angular 17+ Standalone) y qué parte queda explícitamente fuera.

## 2. Definiciones

- **Objeto de negocio**: transacción, procedimiento, WebPanel/WebComponent funcional o DataProvider con lógica de dominio.
- **Artefacto de plataforma**: objeto provisto por GeneXus, Unanimo o plantillas mobile (EOs estándar, SDTs de patrón, ThemeClass, Image, Stencil, DesignSystem).
- **Paridad observable**: igualdad de comportamiento entrada→salida→efecto en BD, no de estructura de código.
- **Supuesto de esquema**: definición física derivada de la metadata de la KB cuando no hay acceso directo a la BD.

## 3. Requisitos — Alcance IN (se reimplementa)

- **R-001 (Ubicuo)** El sistema deberá reimplementar las **32 transacciones** del dominio (p. ej. `Personas`, `Contribuyentes`, `Provincia`, `Localidades`, `Jurisdic`, `Calles`, `CAMINOS`, `Areas`, `Comisiones`, `F2276`, `PPrim_Turnos`, `RECEPTOR`, `Receptorias`, `Feriados`, `Productos`, `FormuPagos`, `Flete`, `SERCTY`/`SCRTY`).
  - *Verificación*: las 32 transacciones aparecen en la matriz de paridad con su entidad JPA asociada.
- **R-002 (Ubicuo)** El sistema deberá reimplementar los **procedimientos de negocio** de los grupos: Formulario 2276, Núcleo/comunes, Puestos/turnos, Seguridad, Comisiones/turnos, Búsquedas, Reportes, Actas y Códigos de barra.
  - *Verificación*: cada procedimiento in-scope mapea a un servicio `@Service` y a un caso de prueba.
- **R-003 (Ubicuo)** El sistema deberá reimplementar la **capa web funcional**: WebPanels de negocio (Form2276, Comisiones, Actas, Turnos, Localidades, Inicio/Home) y WebComponents asociados.
  - *Verificación*: cada pantalla in-scope tiene componente Angular y flujo E2E.
- **R-004 (Ubicuo)** El sistema deberá reutilizar el **esquema físico SQL Server existente**, preservando nombres exactos de tabla y columna.
  - *Verificación*: `@Table(name)`/`@Column(name)` coinciden con el esquema; sin DDL destructivo.
- **R-005 (Ubicuo)** El sistema deberá exponer los servicios de consulta/login legados (`SDTEntradaLoginWS`/`SDTSalidaLoginWS`, `Seg_ControlAccesoWS`) como API REST bajo `/api/v1`.
  - *Verificación*: contrato REST documentado y probado contra los SDT de entrada/salida.
- **R-006 (Dirigido por estado)** MIENTRAS se migre un módulo, el sistema deberá declarar una **matriz de paridad** (requisito legacy ↔ requisito EARS ↔ prueba).
  - *Verificación*: existe la matriz por módulo antes de la puerta G-TEST.

## 4. Requisitos — Alcance OUT (no se reimplementa)

- **R-101 (Ubicuo)** El sistema **no deberá** reimplementar los **ExternalObject estándar de GeneXus/Unanimo** (Beacons, Camera, CardScanner, Contacts, Geolocation, Maps, Notifications, Purchases, Store*, Synchronization, Social, Socket, Interop, DynamicCall*).
  - *Justificación*: capacidades de plataforma sin equivalente de dominio; se sustituyen por APIs web estándar cuando aplique.
  - *Verificación*: no aparecen en `backend/` ni `frontend/`.
- **R-102 (Ubicuo)** El sistema **no deberá** reimplementar los **SDT de patrón mobile/plataforma** (Beacon*, Analytics*, Card/Purchase/Store/Media/Map/Geolocation/Synchronization/Notification/OAuth*).
  - *Verificación*: solo los SDT de dominio (F2276*, SDTActa*, SDTEntrada/SalidaLoginWS, Context, ProgramNames) migran.
- **R-103 (Ubicuo)** El sistema **no deberá** reimplementar el **Design System Unanimo** (274 ThemeClass, 192 imágenes, Stencils, DesignSystems). En su lugar se derivará un sistema de diseño propio (`docs/design.md`) y se usará Angular Material.
  - *Verificación*: no se copian ThemeClass; `docs/design.md` documenta el mapeo visual.
- **R-104 (Ubicuo)** El sistema **no deberá** reimplementar los **WebPanels generados por patrón** (`Gx0010`…`Gx0140`, `Gx00A0`…).
  - *Verificación*: sustituidos por componentes de listado/CRUD propios.
- **R-105 (Ubicuo)** El sistema **no deberá** reimplementar las carpetas/módulos `Deprecated`, `iOS`, `Social`, `Store`, `Purchases`, `Media`, `Offline`, `Synchronization`.
  - *Verificación*: ausentes del alcance y de la matriz de paridad.
- **R-106 (Comportamiento no deseado)** SI un objeto resulta ambiguo entre IN y OUT, ENTONCES el sistema deberá marcarlo como **pendiente de decisión** y no incluirlo silenciosamente.
  - *Verificación*: existe una lista de "pendientes de decisión" con dueño y fecha.

## 5. Requisitos — Restricciones de proceso

- **R-201 (Ubicuo)** La KB GeneXus deberá permanecer en **solo lectura** durante todo el proyecto.
  - *Verificación*: no hay operaciones de escritura/compilación sobre la KB en el historial.
- **R-202 (Ubicuo)** El sistema deberá cumplir la normativa **ISO/IEC 25030** para la evaluación de paridad funcional.
  - *Verificación*: matriz de paridad y criterios de aceptación trazables por módulo.
- **R-203 (Comportamiento no deseado)** SI no hay acceso a la BD real `Rentas`, ENTONCES el sistema deberá derivar el esquema de la **metadata de la KB** y registrar cada suposición.
  - *Verificación*: las entidades JPA citan la transacción/atributo origen y marcan los supuestos.

## 6. Criterios de aceptación del alcance

- [x] R-001…R-006 aprobados por el responsable del proyecto. *(2026-10-15)*
- [x] R-101…R-106 aprobados; sin objetos ambiguos sin clasificar. *(2026-10-15)*
- [x] Priorización de módulos acordada; piloto `01-nucleo-datos` confirmado (ver `plan.md`). *(2026-10-15)*
- [ ] Existe el **listado nominal** de objetos IN con su módulo destino — se produce por módulo durante la fase de análisis (`analista-gx`).

## 7. Decisiones adoptadas (resueltas 2026-10-15)

| # | Pregunta | Decisión | Impacto |
|---|---|---|---|
| D1 | Esquema físico de `Rentas` | **El responsable proveerá credenciales** de conexión (se cargan en `.env`, nunca en el repo). Hasta disponer de ellas, el esquema se deriva de la KB y cada supuesto se marca (R-203). | Gate **G-DATA**: validar entidad ↔ tabla real en cuanto lleguen las credenciales. |
| D2 | Seguridad / login | **Paridad estricta con el modelo legacy**: GAM/`SCRTY`/`Seg_ControlAccesoWS` se reimplementan con el mismo esquema de usuarios y roles. | Módulo `09-seguridad` migra el modelo tal cual; sin JWT/OIDC. |
| D3 | Reportes / impresión | **Paridad de impresión exacta (PDF)**: volantes, actas y listados deben replicar el formato impreso original. | Requiere capturar plantillas/formularios de impresión en cada módulo que los emita. |

> La provisión de credenciales de `Rentas` es la única entrada externa pendiente; **no bloquea**
> el análisis y las specs (que se derivan de la KB), pero sí la validación final de paridad de datos.
