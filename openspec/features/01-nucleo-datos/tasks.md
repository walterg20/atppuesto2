# Tasks — 01 Núcleo de Datos (piloto)

> Checklist verificable del módulo `01-nucleo-datos`. Cada tarea es atómica y trazable a un
> requisito de `spec.md`. **Ninguna tarea de código (T-1xx+) se inicia sin aprobar `spec.md`
> (puerta G-SPEC)** — guardarraíl G2.
> Estado: `DRAFT`.

## A. Extracción y especificación (F2 — análisis)

- [x] A.1 Confirmar KB `ATPPuestos` conectada y ambiente `Gen18`. *(2026-10-15)*
- [x] A.2 Extraer las 8 transacciones maestras (`Structure`, `Rules`, `Events`). *(sin reglas/eventos)*
- [x] A.3 Extraer `Bus_DesLoc` y `BuscarCamino` (`Rules` + `Source` + variables).
- [x] A.4 Obtener el **nombre físico de tabla** de cada transacción (`get_indexes` → `result.name`).
- [x] A.5 Obtener índices/PK/FK de las 8 tablas.
- [x] A.6 Verificar consumidores (`impact`): `Bus_DesLoc`←`F2276`, `BuscarCamino`←`CodigoBarra`.
- [ ] A.7 Redactar `spec.md` en EARS (R-001…R-006, R-010/R-011, R-020…R-023, R-201…R-205). — *hecho (borrador)*
- [ ] A.8 Redactar `plan.md` (mapeo JPA + contratos + matriz de paridad). — *hecho (borrador)*
- [ ] A.9 Redactar `tasks.md`. — *hecho (borrador)*
- [ ] A.10 Revisar los tres archivos contra la extracción de la KB y resolver D-01…D-03.

## B. Aprobación (puerta G-SPEC)

- [ ] B.1 Resolver los pendientes P-01…P-03 (`spec.md` §7).
- [ ] B.2 Marcar `spec.md` como `APPROVED` y registrar la fecha.
- [ ] B.3 Registrar la decisión en `.engram/` (`mem_save`, tipo `decision`).

## C. Validación de esquema (puerta G-DATA)

- [ ] C.1 Confirmar si hay credenciales de `Rentas` (`devmssqlatp.ecom.com.ar:1433`).
- [ ] C.2 **ASM-01**: validar el nombre físico real de cada columna (`INFORMATION_SCHEMA.COLUMNS`).
- [ ] C.3 **ASM-02/03/05**: validar tipos reales (`NUMERIC`→`smallint`/`int`/`bigint`; `DATE`→`date`; `DATETIME`→`datetime2`).
- [ ] C.4 **ASM-04**: validar `varchar` vs. `char` en `CHARACTER(n)`.
- [ ] C.5 Ajustar las entidades si difieren; si no hay credenciales, dejar ASM documentados (R-203).

## D. Scaffold backend (T-0xx)

- [ ] T-001 Crear `backend/` con Spring Boot 3.x + **Maven Wrapper** (`mvnw`, `mvnw.cmd`, `.mvn/wrapper`).
- [ ] T-002 Añadir dependencias: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, `mssql-jdbc`, `spring-boot-starter-test`.
- [ ] T-003 Configurar `application.yml` con `${DB_URL}`/`${DB_USER}`/`${DB_PASSWORD}` y `ddl-auto=none` (R-204, R-205).
- [ ] T-004 Crear paquete base `ar.com.atp.puestos` con subpaquetes `nucleo/{domain,repository,service,web,web/dto}` y `shared`.
- [ ] T-005 Añadir un `HealthController` (`/api/v1/health`) y verificar arranque sin BD (perfil de test).

## E. Entidades y repositorios (T-1xx)

- [ ] T-101 Entidad `Provincia` → `@Table(name="PROVINCI")` (R-001, R-002, R-205).
- [ ] T-102 Entidad `Localidad` → `@Table(name="Localida")` + `@ManyToOne` `Provincia` sobre `ProvCod` (R-003).
- [ ] T-103 Entidad `Calle` → `@Table(name="Calles")`.
- [ ] T-104 Entidad `Camino` → `@Table(name="CAMINOS")`.
- [ ] T-105 Entidad `Area` → `@Table(name="Areas")` (`AreaId` → `Long`).
- [ ] T-106 Entidad `Jurisdiccion` → `@Table(name="Jurisdic")`.
- [ ] T-107 Entidad `Feriado` → `@Table(name="Feriados")` (PK `FerFecha`; auditoría).
- [ ] T-108 Entidad `Producto` → `@Table(name="Producto")` + `@OneToMany` a `ProductoVigencia` (R-004).
- [ ] T-109 Entidad `ProductoVigencia` → `@Table(name="Product1")` con `@IdClass` (`ProdCod`+`ProdFchVig`).
- [ ] T-110 Repositorios Spring Data para las 9 entidades (incl. `ProductoVigenciaRepository.findVigenteHastaHoy`).

## F. Servicios (T-2xx)

- [ ] T-201 `NucleoConsultaService`: listados y búsquedas por clave (catálogos).
- [ ] T-202 `BusquedaService.busDesLoc(locCod)` equivalente a `Bus_DesLoc` (R-010).
- [ ] T-203 `BusquedaService.buscarCamino(camID)` con `TRIM(CanRuta)` (R-011).
- [ ] T-204 `ProductoService.importesVigentes(prodCod)` implementa `ProdIMax1..4` con `0` por defecto (R-005).

## G. Controladores REST (T-3xx)

- [ ] T-301 `ProvinciaController` (`GET /provincias`, `/provincias/{provCod}`).
- [ ] T-302 `LocalidadController` (`GET /localidades`, `/localidades/{locCod}`, `/localidades/{locCod}/nombre`).
- [ ] T-303 `CalleController` (`GET /calles`).
- [ ] T-304 `CaminoController` (`GET /caminos`, `/caminos/{camId}/ruta`).
- [ ] T-305 `AreaController` (`GET /areas`).
- [ ] T-306 `JurisdiccionController` (`GET /jurisdicciones`).
- [ ] T-307 `FeriadoController` (`GET /feriados?desde&hasta`).
- [ ] T-308 `ProductoController` (`GET /productos`, `/productos/{prodCod}`, `/productos/{prodCod}/importes-vigentes`).
- [ ] T-309 Manejo uniforme de errores `{ code, message }` + `404` (R-023).

## H. Pruebas y verificación (T-4xx — puerta G-TEST)

- [ ] T-401 `ProductoServiceTest` parametrizado para `ProdIMax1..4` (sin vigencias / pasadas / futuras / hoy).
- [ ] T-402 `BusquedaServiceTest` para `Bus_DesLoc` y `BuscarCamino` (existente/inexistente/`Trim`).
- [ ] T-403 Pruebas de contrato (MockMvc) para R-020…R-023 (200/404).
- [ ] T-404 Pruebas de integración de lectura por entidad (G-DATA), marcadas para ejecutarse solo con BD.
- [ ] T-405 Completar la **matriz de paridad** (`plan.md` §5) y adjuntar evidencia (G-TEST).

## I. Web (fase 2 — T-5xx)

- [ ] T-501 Scaffold `frontend/` (Angular 17+ standalone) + `HttpClient` + proxy a `/api/v1`.
- [ ] T-502 Componente `localidad-list` (standalone, Reactive Forms, Angular Material) con filtro por provincia.
- [ ] T-503 Prueba E2E (Playwright) de `ListadoLocalidades` contra el backend.

## J. Cierre

- [ ] J.1 Actualizar `docs/analisis-ATPPuestos.md` con los hallazgos de G-DATA.
- [ ] J.2 `mem_session_summary` + `engram sync`.
- [ ] J.3 Marcar `01-nucleo-datos` como cerrado y habilitar el módulo `02-personas`.
