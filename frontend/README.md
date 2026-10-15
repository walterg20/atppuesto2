# Frontend — Angular 17+ Standalone

> **Placeholder.** El scaffold real (`ng new` / estructura standalone, `package.json`) se crea
> al arrancar el primer módulo web, una vez aprobada la spec de alcance (puerta **G-SPEC**).

## Reglas (ver `openspec/constitution/stack.md`)

- Angular 17+ con **Standalone Components** y **Signals**.
- Formularios reactivos (`FormGroup`); control de flujo `@if` / `@for` / `@switch`.
- UI con Angular Material; el tema se deriva de `docs/design.md`.
- Consumo de APIs con `HttpClient` en servicios `@Injectable`.
- `API_BASE_URL` por variable de entorno (ver `.env.example`).

## Estructura prevista

```text
frontend/
├── package.json / angular.json / tsconfig.json
└── src/app/
    ├── core/            # servicios singleton, interceptores, guards
    ├── shared/          # componentes/pipes/directivas reutilizables
    ├── layout/          # shell: toolbar, sidenav, menú
    └── features/        # componentes por módulo
```
