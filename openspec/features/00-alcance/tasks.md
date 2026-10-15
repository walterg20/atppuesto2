# Tasks — 00 Alcance del Replatforming

> Checklist del módulo transversal `00-alcance`. No produce código.
> Estado: `DRAFT`. Marcar al completar.

## A. Redacción y revisión de la spec

- [ ] A.1 Redactar `spec.md` en sintaxis EARS (R-001…R-006, R-101…R-106, R-201…R-203). — *hecho (borrador)*
- [ ] A.2 Revisar cada requisito contra `docs/analisis-ATPPuestos.md` (cobertura y exactitud).
- [ ] A.3 Incorporar feedback del responsable del proyecto.

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

- [ ] D.1 Acceso a la BD `Rentas` (credenciales vs. derivar de la KB) — **bloqueante F2**.
- [ ] D.2 Modelo de seguridad/login (paridad GAM vs. modernización JWT/OIDC).
- [ ] D.3 Requisito de paridad de reportes/impresión.
- [ ] D.4 Cerrar la lista de "pendientes de decisión" (R-106).

## E. Aprobación (puerta G-SPEC)

- [ ] E.1 Aprobación explícita del alcance por el responsable.
- [ ] E.2 Fijar prioridad de módulos (`plan.md` §2) y confirmar el piloto `01-nucleo-datos`.
- [ ] E.3 Registrar la aprobación en `.engram/` (memoria persistente).

## F. Traspaso

- [ ] F.1 Emitir `/extract-spec` para `01-nucleo-datos`.
