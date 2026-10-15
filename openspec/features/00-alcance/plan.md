# Plan — 00 Alcance del Replatforming

> Descomposición del alcance IN en **módulos migrables**, con prioridad y dependencias.
> Estado: `DRAFT` (acompaña a `spec.md`, mismo gate G-SPEC).

## 1. Estrategia: backend primero, web después

El dominio es **transaccional con fuerte dependencia de datos** (40 tablas, muchas con
nombres físicos heredados). Se migra primero **datos + servicios** (verificable sin UI) y
luego la **capa web**, reutilizando los contratos REST ya estabilizados.

## 2. Módulos propuestos (orden de ejecución)

| # | Módulo | Contenido (objetos legacy) | Depende de | Prioridad |
|---|---|---|---|---|
| 01 | `nucleo-datos` | Tablas/transacciones maestras: `Provincia`, `Localidades`, `Calles`, `CAMINOS`, `Areas`, `Jurisdic`, `Feriados`, `Productos` + procs de búsqueda (`Bus_DesLoc`, `BuscarCamino`) | — | **Alta (piloto)** |
| 02 | `personas` | `Personas`, `Contribuyentes` + `ValidaCuit`, `BuscarInfoPorCuitLite`, `DevRazSocial` | 01 | Alta |
| 03 | `receptorias` | `Receptorias`, `RECEPTOR`, `Comisiones`/`Comisio1` + `BusComi`, `BusRece`, `BusResponsable` | 01, 02 | Alta |
| 04 | `turnos` | `PPrim_Turnos`, `ProPri_T`, `DetProd*` + `BusTur`, `NumTur`, `EstadoDeForm`, `VerificaGenMan` | 02, 03 | Alta |
| 05 | `form2276` | Formulario 2276 (19 procs `F2276*`) + WebPanels `F2276*` + WebComponents `F2276_*` | 02, 03, 04 | **Alta (núcleo)** |
| 06 | `comisiones` | `ComisionGenerarSn`, `CerrarComision`, `CerrarTurno`, `TurnoGenerarSN`, `RProPri_TurXCom` | 03, 04 | Media |
| 07 | `actas` | `ActaDeterminaTipoFormulario`, `ActaResumenTurno`, `PPActaDetalle`, `ActaCargarDetalle`, `ActaListadoWC` | 04, 05 | Media |
| 08 | `pagos` | `FormuPagos`, `FormSucu`, `Flete`, `WPFormularioPago`, `VolanteDePago*` | 03, 04 | Media |
| 09 | `seguridad` | `SCRTY`, `Seg_*`, login WS (`SDTEntrada/SalidaLoginWS`), `ControlAcceso(WS)`, `Encripta`, GAM | — | **Alta (transversal)** |
| 10 | `web-shell` | `Home`, `Inicio`, MasterPages, menús (`DP*Menu*`, `SidebarItemsDP`), `UserInfo*`, `RecentLinks` | 09 | Media |

> **Transversal**: `nucleo-comunes` (`ImpALetras`, `ImporteALetras`, `CalculoImpIB`,
> `CacularVencimientoXDias`, `Usuario`) se implementa dentro de `01` como utilidades y se
> reutiliza en el resto.

## 3. Regla de piloto (módulo 01)

`01-nucleo-datos` es el **piloto de proceso**: valida de punta a punta la cadena
KB → spec EARS → entidad JPA (nombres físicos exactos) → servicio → REST → Angular → prueba,
con un conjunto acotado y sin lógica de negocio compleja. Si el proceso funciona, se replica.

## 4. Mapeo de artefactos por módulo

Cada módulo `openspec/features/<NN>-<modulo>/` contiene:

- `spec.md` — requisitos EARS + criterios de verificación (producido por `analista-gx`).
- `plan.md` — mapeo JPA (tabla física ↔ entidad ↔ atributo ↔ tipo) y alcance de código.
- `tasks.md` — desglose verificable y casillado.

## 5. Fuera de alcance (resumen de `spec.md` §4)

EOs estándar, SDTs mobile/plataforma, Design System Unanimo, WebPanels `Gx00xx`,
y módulos `Deprecated`/`iOS`/`Social`/`Store`/`Purchases`/`Media`/`Offline`/`Synchronization`.

## 6. Riesgos del alcance

| Riesgo | Mitigación |
|---|---|
| Mapping tabla física ↔ transacción no 1:1 | Tabla de correspondencia explícita en `plan.md` de cada módulo (G-DATA). |
| Sin acceso a BD `Rentas` | Derivar esquema de la KB; marcar supuestos (R-203). |
| Seguridad/GAM desconocido | Caracterizar en módulo 09 antes de exponer APIs protegidas. |
| Volumen de web (69 WebPanels) | Migrar solo web funcional; `Gx00xx` se sustituye por CRUD genérico. |

## 7. Secuencia inmediata tras la aprobación

1. Aprobación G-SPEC de este alcance.
2. Resolver preguntas abiertas §7 de `spec.md` (BD, seguridad, reportes).
3. Arrancar **`01-nucleo-datos`** con `/extract-spec`.
