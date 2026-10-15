# Tasks — 00 Alcance del Replatforming

> Checklist del módulo transversal `00-alcance`. No produce código.
> Estado: `DRAFT`. Marcar al completar.

## A. Redacción y revisión de la spec

- [ ] A.1 Redactar `spec.md` en sintaxis EARS (R-001…R-006, R-101…R-106, R-201…R-203). — *hecho (borrador)*
- [ ] A.2 Revisar cada requisito contra `docs/analisis-ATPPuestos.md` (cobertura y exactitud).
- [x] A.3 Incorporar feedback del responsable del proyecto. *(2026-10-15 — decisiones D1–D3)*

## B. Definición del alcance IN

- [ ] B.1 Listar nominalmente los objetos IN y asignar módulo destino (`plan.md` §2).
- [ ] B.2 Confirmar las 32 transacciones y su nombre físico de tabla asociado.
- [ ] B.3 Confirmar los procedimientos de negocio in-scope (excluir los 19 de notificaciones/device).
- [ ] B.4 Confirmar los WebPanels/WebComponents funcionales in-scope.

## C. Definición del alcance OUT

- [ ] C.1 Confirmar la exclusión de EOs estándar y SDTs de plataforma/mobile.
- [ ] C.2 Confirmar la exclusión del Design System Unanimo y de los `Gx00xx`.
- [ ] C.3 Confirmar la exclusión de `Deprecated`/`iOS`/`Social`/`Store`/`Purchases`/`Media`/`Offline`/`Synchronization`.

## D. Resolución de ambigüedades y decisiones bloqueantes

- [x] D.1 Acceso a la BD `Rentas`: **el responsable proveerá credenciales**; hasta entonces el esquema se deriva de la KB (R-203). *(2026-10-15)*
- [x] D.2 Seguridad/login: **paridad estricta** con el modelo legacy GAM/`SCRTY`/`Seg_ControlAccesoWS`. *(2026-10-15)*
- [x] D.3 Reportes/impresión: **paridad de impresión exacta (PDF)**. *(2026-10-15)*
- [ ] D.4 Cerrar la lista de "pendientes de decisión" (R-106) — se completa por módulo durante el análisis.

## E. Aprobación (puerta G-SPEC)

- [x] E.1 Aprobación explícita del alcance por el responsable. *(2026-10-15)*
- [x] E.2 Prioridad de módulos fijada (`plan.md` §2); piloto `01-nucleo-datos` confirmado. *(2026-10-15)*
- [x] E.3 Aprobación y decisiones registradas en `.engram/`. *(2026-10-15)*

## F. Traspaso

- [x] F.1 Emitir `/extract-spec` para `01-nucleo-datos`: extracción de la KB (8 transacciones + 2 procs) y `spec.md`/`plan.md`/`tasks.md` redactados. *(2026-10-15)*
- [ ] F.2 Aprobar el **G-SPEC del módulo `01-nucleo-datos`**. ← **en curso**
