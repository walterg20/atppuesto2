# F0 — Análisis e inventario de la KB GeneXus `ATPPuestos`

> Documento de diagnóstico previo a cualquier especificación (SDD).
> La KB legacy se trató en **modo lectura** (abrir/seleccionar/leer). No se compiló, generó ni modificó ningún objeto.

## 1. Entorno y accesos

| Elemento | Valor |
|---|---|
| KB (origen, solo lectura) | `F:\Modelo\Gx18\ATPPuestos` (`ATPPuestos.gxw`) |
| GeneXus | 18.0.13 (U13) — `C:\Program Files (x86)\GeneXus\GeneXus18` |
| Driver MCP | `native-sdk`, resolución `strict`, MCP `genexus-mcp` 3.9.2 |
| Ambiente activo | `Gen18` — generador **Default (Java)** — web path `Data007\web` |
| BD de metadatos KB | `GX_KB_ATPPuestos` @ `WALTERG20\SQLEXPRESS2019` (Integrated Security) |
| BD de la aplicación | **`Rentas`** @ `devmssqlatp.ecom.com.ar` (SQL Server, driver-auth, **sin credenciales almacenadas**) |
| Tamaño KB en disco | 6.738 archivos / ~513 MB |
| Índice MCP | 1.615 entradas (objetos + atributos + clases) |
| Theme CSS | `ATPEvo2.css` (3.754 líneas) + `Unanimo_chameleon` |
| Reachability BD app | `devmssqlatp.ecom.com.ar:1433` → resuelve `10.5.2.180`, TCP **alcanzable**, ping ICMP bloqueado |

Diagnóstico `genexus_doctor` → **DoctorOk** (sin advertencias). KB abierta, seleccionada y con lease propio del worker.

## 2. Inventario por tipo de objeto (1.615 entradas)

| Tipo | Cant. | Tipo | Cant. |
|---|---:|---|---:|
| Attribute | 421 | Transaction | 32 |
| ThemeClass | 274 | Module | 21 |
| Image | 192 | UserControl | 17 |
| Domain | 127 | WebComponent | 16 |
| Index | 88 | Group | 14 |
| Procedure | 77 | MasterPage | 8 |
| SDT | 76 | DesignSystem | 8 |
| WebPanel | 69 | File | 6 |
| ExternalObject | 61 | DataProvider | 6 |
| Folder | 43 | Stencil | 5 |
| Table | 40 | Category | 4 |

Otros: `ThemeForWeb` 2 (`ATPEvo2`, `GeneXusXSD`), `WorkWith` 2, `Pattern Settings` 2, `Document` 1 (`Main`), `Language` 1, `DataStoreCategory` 1, `GeneratorCategory` 1.

> **Lectura clave**: del total, el *footprint propio del negocio* es acotado (40 tablas, 32 transacciones, 77 procedimientos, 69 WebPanels, 6 DataProviders). El resto son artefactos de patrones/base GeneXus: `ThemeClass` (274), `Image` (192), `ExternalObject` (61) y buena parte de los `SDT` (76) provienen del **Design System Unanimo** y de plantillas mobile (`SD*`), no del dominio.

## 3. Modelo de datos (40 tablas / 421 atributos / 127 dominios / 88 índices)

**Hallazgo crítico de paridad**: los nombres **físicos** de tabla son heredados (muchos de 8 caracteres, era GX15) mientras que las transacciones ya usan nombres largos. El mapeo `Tabla física ↔ Transacción` no es 1:1 trivial.

```
Areas   Calles   CAMINOS   Comisio1   Comision   ConAcop1   ConAcopi   ContJur
Contribu   DetProd1   DetProd2   DetProdP   F2276   F22761   F2276Pas   Feriados
Fletes   FormSucu   FormuPag   Ges_SucN   Ges_Sucu   Jurisdic   Localida   Lotes227
Mensajes   NumLog   ORIGRET1   ORIGRETE   Personas   PPRIM_TU   Product1   Producto
ProPri_T   PROVINCI   PuePPrim   RECEPTO1   RECEPTOR   SCRTY   SNSinDev   VERSIONE
```

Transacciones (32): `Areas, Calles, CAMINOS, Comisiones, Contribuyentes, F2276, F2276_FormDef, F2276_Lotes, Feriados, Flete, FormSucursal, FormuPagos, Jurisdic, Localidades, Mensajes, ORIGRETE, Personas, PPrim_Turnos, PPrim_TurnosNue, Productos, ProPri_T, Provincia, PuePPrim, RECEPTOR, Receptorias, SCRTY, SNSinDev, TConAcopiador, TDetProdPri, TGes_Sucu, TNumLog, VERSIONE`.

## 4. Lógica de negocio (77 procedimientos)

Agrupados por prefijo funcional (el prefijo revela el subsistema):

| Grupo | # | Objetos |
|---|---:|---|
| **Formulario 2276** (`F2276*`) | 19 | Anular, BusDom, BusLoc, BusLocYProv, BusProTarCM, BusProTarifa, BusUlt, CalImpFl, CalTarFl, DevRazSocial, DiarioReceptorias, GenNroForm, RecUltNroForm, RegPasePuestoV2, Reporteimpresion, BuscarSn, GenerarSn, VolanteDePago, VolanteDePagoXTurno |
| **Núcleo / comunes** (`Nuc_*`) | 6 | CacularVencimientoXDias, CalculoImpIB, ImpALetras, ImporteALetras, Usuario, ValidaCuit |
| **Puestos / turnos** (`PPrim_*`, `ProPri_*`) | 11 | BuscarLote, BusTur, CerrarComision, CerrarTurno, EstadoDeForm, NumTur, TurnoEstado, VerificaGenMan, VerIns, VerManual, VerSoloIns |
| **Seguridad** (`Seg_*`) | 8 | BusComi, BusPer, BusPuesto, BusRece, BusResponsable, ControlAcceso, ControlAccesoWS, Encripta |
| **Comisiones / turnos** | 4 | ComisionGenerarSn, ComisionVolanteDePago, TurnoGenerarSN |
| **Búsquedas** | 5 | Bus_DesLoc, BuscarCamino, BuscarInfoPorCuitLite, BuscarOrigenRetencion, BuscarVersionActual |
| **Reportes** | 3 | RPPriTur_TurnosForm, RProPri_TurXCom, RPT_ActaTraslado |
| **Actas** | 2 | ActaDeterminaTipoFormulario, ActaResumenTurno |
| **Códigos de barra** | 3 | CodigoBarra, CodBarToPKF276, PK276ToCodBar |
| **Contexto / offline (patrón mobile)** | 3 | LoadContext, SetContext, OfflineEventReplicator |
| **Seguridad aplicativa** (`IsAuthorized`, `ListPrograms`) | 2 | — |
| **Notificaciones / devices (Unanimo-SD)** | 8 | Add/RemoveDeviceGroups, Add/RemoveDeviceTargetFilter, SendEvent(Targets), SendNotification(Targets) |
| **OAuth / otros** | 3 | RefreshToken, GoogleDirectionsServiceRequest, MarcarGuia |

**Data Providers (6)**: `DPDolphinMenuActas`, `DPDolphinMenuData`, `DPDolphinMenuDataRecepteria`, `DPMenuComisiones`, `SidebarItemsDP`, `SmoothNavMenuDataDP` → alimentan menús/navegación (no lógica de dominio).

## 5. Capa web

- **WebPanels (69)**: 26 son **generados por patrón Work With** (`Gx0010`…`Gx0140`, `Gx00A0`…). El resto es funcional: `ActaCargarDetalle`, `ActaListadoWC`, `ActaPuesto_Chofer`, `Comisiones_ViewComisiones`, `Comisiones_WWComisiones`, `ComisionesListado`, `ControlDeCargas`, `F2276_RegistroPase`, `F2276_ViewF2276`, `F2276CargarDetalle`, `F2276DetalleLite`, `F2276DetallePrompt`, `F2276ListadoControl`, `F2276Listados`, `Form506Detalle`, `Home`, `Inicio`, `ListadoLocalidades`, `Mensaje`, `NotAuthorized`, `PPActaDetalle`, `TurnosListado`, `TurnosListadoAbiertos`, `ViewPPrim_Turnos`, `ViewTDetProdPri`, `WPFormularioPago`, `WWPPrim_Turnos`, `WWTDetProdPri`.
- **WebComponents (16)**: `ActaListadoWC_turno`, `Comisiones_General`, `Comisiones_IntegrantesWC`, `F2276_F22761WC`, `F2276_General`, `F2276ListadosWCPopUp`, `F2276ListadoWC`, `Login`, `PPrim_TurnosF2276_FormDefWC`, `PPrim_TurnosF2276WC`, `PPrim_TurnosGeneral`, `RecentLinks`, `TabbedView`, `TabsMenu`, `UserInfo`, `UserInfoNuevo`.
- **MasterPages (8)**: `AppMasterPage`, `AppMasterSD`, `MasterNuevo`, `MasterPagePuente`, `MasterPrompt`, `MasterUnanimoSidebar`, `PromptMasterPage`, `RwdMasterPage`.
- **UserControls (17)**: pares nativos + Angular (`Alert`/`AlertAngular`, `Button`, `Dropdown`/`DropdownAngular`, `Icon`/`IconAngular`, `Select`/`SelectAngular`, `Sidebar`/`SidebarAngular`, `Slider`, `Step`/`StepAngular`, `Switch`, `Treeview`/`TreeviewAngular`).
- **WorkWith (2)**: `WorkWithWebPPrim_Turnos`, `WorkWithWebTDetProdPri`.

> La existencia de variantes **Angular** de los UserControls indica web UX ya preparada para Angular (`WebUX=SMOOTH`, `DefaultWebFormEditor=1`).

## 6. SDT y External Objects

- **SDT (76)**: mayoría de patrón/plataforma (`Beacon*`, `Analytics*`, `CardInformation`, `Purchase*`, `Store*`, `Media*`, `Map*`, `Geolocation*`, `Synchronization*`, `Notification*`, `OAuth*`). De dominio: `F2276DetalleSDT`, `F2276Totales`, `SDTActaPK`, `SDTDatosChoferActa`, `SDTEntradaLoginWS`, `SDTSalidaLoginWS`, `SdtF2276`, `ProgramNames`, `Context`.
- **ExternalObject (61)**: casi todos son EOs **estándar de GeneXus** para mobile/Unanimo (`Beacons`, `Camera`, `CardScanner`, `Contacts`, `Geolocation`, `Maps`, `Notifications`, `Purchases`, `Store*`, `Synchronization`, `Facebook`, `Twitter`, `WeChat`, `Socket`, `Interop`, `DynamicCall*`…). De dominio: `Server`, `DataBase`, `Log`, `ConfigurationManager`, `GlobalEvents`, `ClientInformation`.

## 7. UI / Design System

- **Theme web**: `ATPEvo2` (exportado como `ATPEvo2.css`, 3.754 líneas) — base para derivar el sistema de diseño moderno.
- **DesignSystems (8)**: `Unanimo`, `UnanimoAngular`, `UnanimoATP`, `UnanimoControls`, `UnanimoControlsATP`, `UnanimoMobile`, `UnanimoWeb`, `UnanimoWebAtp`.
- **Stencils (5)**: `Card`, `CardWithFooter`, `Header`, `OneColumnCards`, `WW_HeaderWithActions`.
- **Imágenes**: 192. **Clases de tema**: 274. **Fuentes**: Source Sans Pro (4 pesos) + `DolphinStyleMenuRender.js`.
- Navegación: menú "Dolphin" (DataProviders dedicados) + `Sidebar`/`SmoothNavMenu`.

## 8. Organización en módulos y carpetas

- **Modules (21)**: `Root Module`, `Client`, `Server`, `Common`, `Configuration`, `General`, `UI`, `UI`(dup), `v2`, `Media`, `Notifications`(dup), `OAuth`, `Offline`, `Social`, `Store`, `Synchronization`, `SD`, `GeneXus`, `GeneXusUnanimo`, `iOS`.
- **Folders (43)**: `Nucleo`, `Transacciones`, `SDTs`(dup), `Procedimientos`, `Seguridad`, `Security`, `Librerias`, `Utiles`, `Varios`, `Disenos`, `Stencils`, `DesignSystems`, `Icon`, `Controls`, `Login`, `Purchases`, `Turnos`, `Comisiones`, `ComisionesWW`, `F2276WW`, `F2776`, `Feriados`, `Formulas`, `GeneralWeb`, `GoogleDirections`, `Guias`, `Deprecated`, `ContextManagement`, `Alert`, `Dropdown`, `Select`, `Sidebar`, `Step`, `TabsMenu`, `Treeview`, `SmoothNavMenu`, `DolphinStyleMenu`, `DirectionsServices`, `SmartDevicesApi`, `SubtypeGroups`, `Common`, `GeneXus`.

> Nombres duplicados (`UI`, `Notifications`, `SDTs`, `Common`, `GeneXus`) corresponden a nodos con distinto padre — no son duplicados lógicos.

## 9. Bases de datos

| Rol | Motor | Servidor | Base | Auth | Notas |
|---|---|---|---|---|---|
| Metadatos KB | SQL Server | `WALTERG20\SQLEXPRESS2019` | `GX_KB_ATPPuestos` | Integrated | `.mdf/.ldf` dentro de la carpeta KB |
| Aplicación (runtime) | SQL Server | `devmssqlatp.ecom.com.ar` (10.5.2.180) | **`Rentas`** | Driver (sin credenciales en KB/model.ini) | TCP 1433 alcanzable; no hay BD local `Rentas` |
| Ambiente alterno | Oracle | (referenciado, sin `CS_*` en model.ini) | — | — | Modelo `Design`/`006`, aparentemente inactivo |

En `model.ini`: `[MODEL 007] DataStore=Default (SQL Server)`, `CS_DBNAME=Rentas`, `CS_SERVER=devmssqlatp.ecom.com.ar`, `CS_USETOCONNECT=Driver` (pide credenciales al conectar; **no** hay usuario/clave persistidos → nada que filtrar al repo).

## 10. Observaciones y riesgos

1. **Sin acceso a la BD real**: `Rentas` es remota y sin credenciales almacenadas → el esquema para la paridad (nombres físicos de tabla/columna) debe derivarse de la **metadata de la KB** (transacciones/atributos), no de un `SELECT` a la base. Validar acceso con el usuario antes de F2.
2. **Nombres físicos legacy** (8 chars) vs. nombres lógicos actuales → riesgo de mapping incorrecto; requiere tabla de correspondencia explícita.
3. **Contaminación de metadatos**: EO/SDT/Tema/Imágenes de Unanimo y plantillas mobile inflan el conteo; el alcance real de negocio es sensiblemente menor → conviene **definir el alcance migrable** antes de especificar.
4. **Generador objetivo ya es Java** (`Default (Java)`) en el ambiente `Gen18` — alineado con el target Spring Boot.
5. **Doble KB con el mismo nombre** (`GX_KB_ATPPuestos` en la instancia de GX18 y en la de GX15) → usar siempre `WALTERG20\SQLEXPRESS2019` para GX18.
6. **Seguridad**: la KB referencia `SCRTY`, `Seg_ControlAccesoWS`, login WS (SDTs de entrada/salida) y GAM (`Security`). Hay que caracterizar el modelo de autenticación/autorización antes de reimplementar (F2).
7. **Regla de oro respetada**: lectura exclusiva; abrir/seleccionar y consultar no altera la KB.

## 11. Alcance tentativo del replatform (a validar)

Candidato a migrar (backend Java + frontend Angular):
- 40 tablas / 32 transacciones (dominio: personas, contribuyentes, formularios, comisiones, turnos, receptorías, feriados, jurisdicciones, localidades).
- ~50 procedimientos de negocio (excluyendo los 19 de notificaciones/device y los de patrón mobile).
- Servicios web de login/consulta (`SDTEntrada/SalidaLoginWS`, `Seg_ControlAccesoWS`).
- WebPanels funcionales + WebComponents de Form2276 / Comisiones / Actas / Turnos.
- Derivar sistema de diseño desde `ATPEvo2.css` + DesignSystems Unanimo.

**Fuera de alcance probable** (regenerar/descartar, no reimplementar): EOs estándar de plataforma, SDTs de patrón mobile, carpetas `Deprecated`, WebPanels generados `Gx00xx`, `iOS`/`Social`/`Store`/`Purchases`/`Media`.

## 12. Próximos pasos

- **F1**: constituir el monorepo (`openspec/constitution/`, `docs/`, `backend/`, `frontend/`, `.env.example`, `.gitignore`, `git init`).
- **F1.1**: definir el alcance migrable (in/out) y volcarlo como especificación EARS.
- **F2**: ciclo SDD por módulo — empezar por el núcleo de datos (transacciones + reglas) y luego web.

### Cómo regenerar estos datos (determinista)

- Inventario: `genexus_list_objects` (MCP) paginado con `projection=minimal`, `sort=name`.
- Esquema por transacción: `genexus_analyze mode=context target=<Transacción>`.
- Fuente: `genexus_read`; búsqueda cruzada: `genexus_search_source`.
- Impacto/dependencias: `genexus_analyze mode=impact|dependencies target=<objeto>`.

---
_Generado en F0 — 2026-10-15. Fuente: KB GeneXus 18 `ATPPuestos` en modo lectura._
