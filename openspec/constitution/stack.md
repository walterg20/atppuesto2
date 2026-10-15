# Constitución — Stack Tecnológico

> Reglas no negociables del stack objetivo. Toda spec y todo código deben cumplirlas.
> Última revisión: F1 (constitución inicial).

## 1. Backend

| Área | Decisión | Obligatorio |
|---|---|---|
| Lenguaje | Java 17 (LTS) | Sí |
| Framework | Spring Boot 3.x | Sí |
| Persistencia | Spring Data JPA / Hibernate | Sí |
| Validación | Jakarta Bean Validation (`@NotNull`, `@Size`, `@PastOrPresent`, …) | Sí |
| DTOs | Java `record` inmutables | Sí |
| Transacciones | `@Transactional` en capa `@Service` | Sí |
| Build | **Maven Wrapper** (`./mvnw`) — decisión F1 | Sí |
| BD | SQL Server (driver `mssql-jdbc`) | Sí |
| API | REST/JSON, versionada bajo `/api/v1` | Sí |

### Reglas de mapeo de datos (paridad)
1. Reutilizar el **esquema existente**; no generar DDL propio salvo migraciones explícitas aprobadas.
2. Nombres físicos exactos: `@Table(name = "...")`, `@Column(name = "...")`.
3. Los nombres físicos legacy son de **8 caracteres** (p. ej. `PROVINCI`, `Jurisdic`); el nombre lógico largo de la transacción **no** es el nombre de tabla.
4. Tipos: respetar longitud/precisión originales (VARCHAR/NUMERIC/DATETIME) al mapear.
5. No usar `hibernate.hbm2ddl.auto` en modo destructivo contra la BD real.

## 2. Frontend

| Área | Decisión | Obligatorio |
|---|---|---|
| Framework | Angular 17+ Standalone Components | Sí |
| Estado/Reactividad | **Signals** | Sí |
| Formularios | Reactive Forms (`FormGroup`) | Sí |
| UI | Angular Material (tema derivado de `docs/design.md`) | Sí |
| HTTP | `HttpClient` en servicios `@Injectable` | Sí |
| Control de flujo | `@if`, `@for`, `@switch` (no `*ngIf`/`*ngFor`) | Sí |
| Tipado | TypeScript `strict` | Sí |

La fidelidad visual se rige por `docs/design.md`, derivado del theme legacy `ATPEvo2.css`.

## 3. Pruebas

| Capa | Herramienta |
|---|---|
| Backend | JUnit 5 + Mockito |
| Frontend (unit) | Jasmine/Karma o el runner del scaffold |
| Frontend (E2E) | Playwright (o Cypress, si se justifica) |
| Paridad SQL | Comparar consultas Hibernate vs. *Navigation Report* de GeneXus |

## 4. Entorno de desarrollo

- Aislado con el wrapper local (`./mvnw`, `npm ci`); **no** modificar el entorno global del sistema.
- El entorno legacy GeneXus es **solo lectura**.
- Sin secretos en el repositorio; usar variables de entorno (ver `.env.example`).
- Archivos de configuración **< 500 líneas** (optimización de tokens).

## 5. Referencias
- Inventario y diagnóstico F0: `docs/analisis-ATPPuestos.md`
- Proceso y puertas de calidad: `openspec/constitution/sdd-workflow.md`
- Convenciones de código: `openspec/constitution/conventions.md`
