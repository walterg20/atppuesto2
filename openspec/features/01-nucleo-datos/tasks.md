# Tasks — 01 Núcleo de Datos (piloto)

> Checklist verificable del módulo `01-nucleo-datos`. Cada tarea es atómica y trazable a un
> requisito de `spec.md`. **Ninguna tarea de código (T-1xx+) se inicia sin aprobar `spec.md`
> (puerta G-SPEC)** — guardarraíl G2.
> Estado: `APPROVED` (2026-10-16) — P-01 = **ABM completo**, P-03 = **productos anidado**.

## A. Extracción y especificación (F2 — análisis)

- [x] A.1 Confirmar KB `ATPPuestos` conectada y ambiente `Gen18`. *(2026-10-15)*
- [x] A.2 Extraer las 8 transacciones maestras (`Structure`, `Rules`, `Events`). *(sin reglas/eventos)*
- [x] A.3 Extraer `Bus_DesLoc` y `BuscarCamino` (`Rules` + `Source` + variables).
- [x] A.4 Obtener el **nombre físico de tabla** de cada transacción (`get_indexes` → `result.name`).
- [x] A.5 Obtener índices/PK/FK de las 8 tablas.
- [x] A.6 Verificar consumidores (`impact`): `Bus_DesLoc`←`F2276`, `BuscarCamino`←`CodigoBarra`.
- [x] A.7 Redactar `spec.md` en EARS (R-001…R-006, R-010/R-011, R-020…R-023, **R-030…R-037**, R-201…R-205). *(2026-10-16)*
- [x] A.8 Redactar `plan.md` (mapeo JPA + contratos lectura/escritura + matriz de paridad). *(2026-10-16)*
- [x] A.9 Redactar `tasks.md`. *(2026-10-16)*
- [x] A.10 Revisar los tres archivos contra la extracción de la KB y resolver P-01…P-03. *(2026-10-16)*

## B. Aprobación (puerta G-SPEC)

- [x] B.1 Resolver los pendientes P-01…P-03 (`spec.md` §7). *(2026-10-16 — P-01 ABM completo; P-02 credenciales en `.env`; P-03 anidado)*
- [x] B.2 Marcar `spec.md`/`plan.md`/`tasks.md` como `APPROVED` y registrar la fecha. *(2026-10-16)*
- [x] B.3 Registrar la decisión en `.engram/` (`mem_save`, tipo `decision`). *(2026-10-16)*

## C. Validación de esquema (puerta G-DATA) — *completado 2026-10-16*

- [x] C.1 Confirmar credenciales de `Rentas` en `.env` (`DB_USER`/`DB_PASSWORD`). *(conexión OK vía `sqlcmd`)*
- [x] C.2 **ASM-01**: validar el nombre físico real de cada columna (`INFORMATION_SCHEMA.COLUMNS`). *(→ nombre lógico truncado a 10 chars; `FerDescripcion→FerDescrip`)*
- [x] C.3 **ASM-02/03/05**: validar tipos reales. *(→ `Area.AreaId decimal(10,0)`; `FerFecha`/`ProdFchVig` `datetime`; `FerFchReg datetime`)*
- [x] C.4 **ASM-04**: validar `varchar` vs. `char` en `CHARACTER(n)`. *(→ `char(n)` fijo salvo `ProvNombre`, `AreaEmails`, `AreaRespons`)*
- [x] C.5 Ajustar `spec.md` (§4.3/§4.4/§4.5, R-002/R-006/R-036/R-037), `plan.md` (§2.2/§2.3/§3.3/§5/§6) y registrar hallazgos en `docs/analisis-ATPPuestos.md`. *(columnas legacy, sin `IDENTITY`, sin `DEFAULT`, FK física ausente)*

## D. Scaffold backend (T-0xx)

- [x] T-001 Crear `backend/` con Spring Boot 3.x + **Maven Wrapper** (`mvnw`, `mvnw.cmd`, `.mvn/wrapper`). *(Spring Boot 3.5.16, Maven 3.9.11, JDK 17)*
- [x] T-002 Añadir dependencias: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, `mssql-jdbc`, `spring-boot-starter-test`. *(+ `h2` test-scope para arranque sin BD)*
- [x] T-003 Configurar `application.yml` con `${DB_URL}`/`${DB_USER}`/`${DB_PASSWORD}` y `ddl-auto=none` (R-204, R-205).
- [x] T-004 Crear paquete base `ar.com.atp.puestos` con subpaquetes `nucleo/{domain,repository,service,web,web/dto}` y `shared`.
- [x] T-005 Añadir un `HealthController` (`/api/v1/health`) y verificar arranque sin BD (perfil de test). *(BUILD SUCCESS; 2 pruebas OK)*

## E. Entidades y repositorios (T-1xx)

- [x] T-101 Entidad `Provincia` → `@Table(name="PROVINCI")`; incluir columnas legacy `ProvCodAfip` (`@Column(nullable=false)`, default `0`) (R-001, R-002, R-036, R-205).
- [x] T-102 Entidad `Localidad` → `@Table(name="Localida")` + `@ManyToOne` `Provincia` sobre `ProvCod` (asociación lógica, sin FK física); incluir `LocCodAfip` legacy (R-003, R-036).
- [x] T-103 Entidad `Calle` → `@Table(name="CALLES")` + legacy `CallAlt1/2/3`.
- [x] T-104 Entidad `Camino` → `@Table(name="CAMINOS")`.
- [x] T-105 Entidad `Area` → `@Table(name="Areas")` (**`AreaId` → `BigDecimal`**, `decimal(10,0)`) + legacy `AreaBaja/AreaEmails(NOT NULL)/AreaAdjun/AreaRespons` (R-036).
- [x] T-106 Entidad `Jurisdiccion` → `@Table(name="Jurisdic")`.
- [x] T-107 Entidad `Feriado` → `@Table(name="FERIADOS")` (PK `FerFecha` `LocalDateTime`; `FerDescripcion`→`@Column(name="FerDescrip")`; auditoría `NOT NULL`).
- [x] T-108 Entidad `Producto` → `@Table(name="PRODUCTO")` + `@OneToMany` a `ProductoVigencia` (R-004); legacy `ProdReppa`; `ProdIMax*`/`UMedNombre` `@Transient`.
- [x] T-109 Entidad `ProductoVigencia` → `@Table(name="PRODUCT1")` con **`@EmbeddedId` + `@MapsId`** (`ProductoVigenciaId`: `ProdCod`+`ProdFchVig` `LocalDateTime`); `ProdImpu1/2` `NOT NULL`; legacy `ProdImpu5/ProdFeLog/ProdUsu`. *(implementado con `@EmbeddedId`+`@MapsId` en lugar de `@IdClass`: evita el `INSERT` con `NULL` en la FK-mapeada-como-PK del patrón id compuesto unidireccional; validado contra DDL H2)*
- [x] T-110 Repositorios Spring Data para las 9 entidades (incl. `ProductoVigenciaRepository.findVigenteHastaHoy`). *(`ProductoRepository` con `@Query` explícita para `order by`)*

## F. Servicios (T-2xx)

- [x] T-201 `NucleoConsultaService`: listados y búsquedas por clave (catálogos).
- [x] T-202 `BusquedaService.busDesLoc(locCod)` equivalente a `Bus_DesLoc` (R-010). *(método `nombreLocalidad`)*
- [x] T-203 `BusquedaService.buscarCamino(camID)` con `TRIM(CanRuta)` (R-011). *(método `rutaCamino`)*
- [x] T-204 `ProductoService.importesVigentes(prodCod)` implementa `ProdIMax1..4` con `0` por defecto (R-005).
- [x] T-205 `NucleoEscrituraService`: alta/modificación/baja de maestras; **PK provista en el body** (sin `IDENTITY`) y detección de PK duplicada → excepción de conflicto (R-030, R-031, R-037); aplicación de defaults a columnas legacy `NOT NULL` (`0`/`\"\"`) (R-036).
- [x] T-206 Auditoría de `Feriado`: rellenar `FerFchReg` (server time) y `FerUsuario` en servidor (R-033). *(`app.auditoria.usuario`, default `SISTEMA`)*
- [x] T-207 `ProductoService.guardar(producto, vigencias)`: persistencia atómica raíz+hijas, unicidad de `ProdFchVig` (R-034); recálculo derivados sin persistir (R-035).

## G. Controladores REST (T-3xx)

- [x] T-301 `ProvinciaController` (`GET /provincias`, `/provincias/{provCod}`).
- [x] T-302 `LocalidadController` (`GET /localidades`, `/localidades/{locCod}`, `/localidades/{locCod}/nombre`).
- [x] T-303 `CalleController` (`GET /calles`).
- [x] T-304 `CaminoController` (`GET /caminos`, `/caminos/{camId}/ruta`).
- [x] T-305 `AreaController` (`GET /areas`).
- [x] T-306 `JurisdiccionController` (`GET /jurisdicciones`).
- [x] T-307 `FeriadoController` (`GET /feriados?desde&hasta`).
- [x] T-308 `ProductoController` (`GET /productos`, `/productos/{prodCod}`, `/productos/{prodCod}/importes-vigentes`).
- [x] T-309 Manejo uniforme de errores `{ code, message }` + `404` (R-023) + `409` (R-031) + `400` Bean Validation (R-032).
- [x] T-310 `POST` de las 8 maestras (201 + `Location`; **PK requerida en el body** R-037; 409 PK duplicada) — R-030/R-031/R-037.
- [x] T-310b Sembrar defaults de columnas legacy `NOT NULL` en el alta (`ProvCodAfip=0`, `LocCodAfip=0`, `AreaEmails=\"\"`) — R-036.
- [x] T-311 `PUT /provincias/{provCod}`, `/localidades/{locCod}`, `/calles/{callCod}` — R-030.
- [x] T-312 `PUT /caminos/{camId}`, `/areas/{areaId}`, `/jurisdicciones/{jurCod}` — R-030.
- [x] T-313 `PUT /feriados/{ferFecha}` con auditoría automática — R-030/R-033.
- [x] T-314 `PUT /productos/{prodCod}` con vigencias anidadas (P-03) — R-034.
- [x] T-315 `DELETE` de las 8 maestras (204 / 404) — R-030/R-031.

## H. Pruebas y verificación (T-4xx — puerta G-TEST)

- [ ] T-401 `ProductoServiceTest` parametrizado para `ProdIMax1..4` (sin vigencias / pasadas / futuras / hoy).
- [ ] T-402 `BusquedaServiceTest` para `Bus_DesLoc` y `BuscarCamino` (existente/inexistente/`Trim`).
- [ ] T-403 Pruebas de contrato (MockMvc) lectura para R-020…R-023 (200/404).
- [ ] T-404 Pruebas de integración de lectura por entidad (G-DATA), marcadas para ejecutarse solo con BD.
- [ ] T-405 Completar la **matriz de paridad** (`plan.md` §5) y adjuntar evidencia (G-TEST).
- [ ] T-406 Pruebas de contrato escritura: ciclo crear→modificar→eliminar por entidad + duplicado (409) + payload inválido (400) — R-030…R-032.
- [ ] T-407 `FeriadoControllerWriteIT`: auditoría automática (R-033). `ProductoControllerWriteIT`: atomicidad y unicidad de vigencias (R-034) y recálculo de derivados (R-035).

## I. Web (fase 2 — T-5xx) — diseño del origen (P-01)

- [ ] T-501 Scaffold `frontend/` (Angular 17+ standalone) + `HttpClient` + proxy a `/api/v1`.
- [ ] T-502 Extraer HTML/CSS/JS de los WebPanels legacy de las maestras vía MCP (`genexus_read` source) y documentar la referencia visual en `docs/`.
- [ ] T-503 Componente `localidad-list` (standalone, Reactive Forms) con filtro por provincia.
- [ ] T-504 Pantalla **ABM** de maestras simples (Provincia, Calles, Caminos, Áreas, Jurisdicciones) replicando el diseño del origen.
- [ ] T-505 Pantalla **ABM** `Feriados` y `Productos` (con grilla de vigencias anidadas).
- [ ] T-506 Prueba E2E (Playwright) de `ListadoLocalidades` + un ABM contra el backend.

## J. Cierre

- [x] J.1 Actualizar `docs/analisis-ATPPuestos.md` con los hallazgos de G-DATA. *(2026-10-16)*
- [ ] J.2 `mem_session_summary` + `engram sync`.
- [ ] J.3 Marcar `01-nucleo-datos` como cerrado y habilitar el módulo `02-personas`.
