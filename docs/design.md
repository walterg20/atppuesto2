# Sistema de Diseño — Derivado de la UI Legacy (`ATPEvo2`)

> Base para la capa Angular. **Derivado**, no copiado: no se reutilizan ThemeClass ni imágenes
> del Design System Unanimo (ver `openspec/features/00-alcance/spec.md` R-103).
> **Fuente analizada**: `ATPEvo2.css` (3.754 líneas) del ambiente `Gen18`.
> Estado: `DRAFT` — se refina con el primer módulo web.

## 1. Principios

1. **Paridad de identidad, no de markup**: se preserva la *percepción* (marca, jerarquía, densidad) adaptándola a componentes Material.
2. **Densidad administrativa**: es un sistema de gestión (grillas, formularios, listados). Se prioriza legibilidad y uso de teclado sobre efectos.
3. **Accesibilidad AA**: contraste ≥ 4.5:1 en texto; foco visible; navegación por teclado.

## 2. Color

### 2.1 Tokens observados en el CSS legacy

| Rol observado | Valor legacy | Fuente |
|---|---|---|
| **Marca / primario** | `#C60B44` (carmesí) | 23 apariciones |
| Texto principal | `#333333`, `#000000` | — |
| Texto secundario | `#555555`, `#747474`, `#797979` | — |
| Superficie base | `#FFFFFF` | 99 apariciones |
| Superficie alterna | `#F0F0F0`, `#E5E5E5`, `#E4E5E6` | — |
| Bordes / divisor | `#D3D4D3`, `#CCCCCC`, `#C0C0C0` | — |
| Deshabilitado | `#A9A9A9`, `#969696`, `#828282` | — |
| Acento informativo | `#1D6893`, `#00008B` | — |
| Acento alerta | `#9D1F1F`, `#FF0000`, `#FF8000` | — |

### 2.2 Tokens propuestos (Angular Material)

```scss
// Marca
$atp-primary:        #C60B44;   // carmesí institucional
$atp-primary-dark:   #9D1F1F;   // estados pressed / encabezados
$atp-primary-contrast:#FFFFFF;

// Neutrales (superficie / texto)
$atp-surface:        #FFFFFF;
$atp-surface-alt:    #F0F0F0;
$atp-border:         #D3D4D3;
$atp-text:           #333333;
$atp-text-muted:     #747474;
$atp-disabled:       #A9A9A9;

// Semánticos
$atp-info:           #1D6893;
$atp-warn:           #FF8000;
$atp-error:          #C0392B;   // ajustado desde #FF0000 para contraste AA
$atp-success:        #2E7D32;   // nuevo (el legacy no define uno claro)
```

> **Desvío intencional**: el legacy usa `#FF0000` puro para error (contraste bajo sobre blanco).
> Se adopta `#C0392B` por accesibilidad; documentado como decisión.

## 3. Tipografía

- **Legacy**: `Trebuchet MS`, `Verdana`, `Arial`, `Tahoma`, `Microsoft Sans Serif`.
- **Propuesta moderna**: pila del sistema con fallback sin descarga de fuentes:

```scss
$atp-font: "Segoe UI", "Trebuchet MS", Verdana, Arial, sans-serif;
$atp-font-mono: "Cascadia Mono", Consolas, "Courier New", monospace;
```

| Token | Tamaño | Uso |
|---|---|---|
| `display` | 24px / 600 | Título de módulo |
| `title` | 18px / 600 | Encabezado de sección |
| `body` | 14px / 400 | Texto de grilla y formularios |
| `caption` | 12px / 400 | Ayudas y metadatos |

## 4. Espaciado y forma

- Escala base **4px**: `4, 8, 12, 16, 24, 32`.
- Radio de borde: **4px** (consistente con UI administrativa compacta).
- Elevación: sombras Material estándar (`mat-elevation-z1/z2`); sin decoración superflua.

## 5. Componentes (mapa legacy → Material)

| Legacy (clases CSS) | Componente objetivo |
|---|---|
| `Grid`, `GridNoData`, `FreeStyleGrid` | `MatTable` + `MatPaginator` + estados vacíos |
| `Form`, `input-group`, `has-float-label` | `MatFormField` + Reactive Forms |
| `BaseRBtn`, botones de acción | `MatButton` (`primary`/`stroked`/`icon`) |
| `WorkWith*`, `ResponsiveSlide*` | patrón Listado+Detalle (drawer/dialog) |
| `Calendar*` | `MatDatepicker` |
| Navegación "Dolphin"/`Sidebar` | `MatSidenav` + `MatToolbar` |
| `gx-responsive-popup` | `MatDialog` |
| Alertas/mensajes (`Mensaje`) | `MatSnackBar` / `MatDialog` de confirmación |

## 6. Patrones de interacción

- **Listados**: filtros en la parte superior, grilla paginada, acciones por fila.
- **Alta/edición**: formulario en diálogo o panel lateral; validación en línea.
- **Confirmaciones destructivas**: diálogo modal con acción explícita.
- **Estados**: toda vista de datos define `loading`, `empty` y `error`.
- **Errores de API**: mensaje legible + código de correlación; nunca trazas crudas al usuario.

## 7. Accesibilidad (AA)

- Contraste de texto ≥ 4.5:1; componentes ≥ 3:1.
- Foco visible en todos los controles; orden de tabulación lógico.
- Etiquetas asociadas a campos (`label`/`aria-label`); errores anunciados.
- Grillas navegables por teclado; `aria-sort` en columnas ordenables.

## 8. Pendientes

- Extraer la paleta exacta de `Unanimo_chameleon`/DesignSystems (F0 los listó como fuente) para confirmar matices.
- Definir modo oscuro (no presente en el legacy) — decisión pendiente.
- Validar la tipografía con el responsable (¿se requiere la fuente institucional exacta?).
