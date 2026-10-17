# Arnés de Control Principal - Replatforming GeneXus a Java/Angular

## Misión
Orquestar y supervisar la migración del sistema legacy de GeneXus (15 y 18) hacia una arquitectura moderna y desacoplada compuesta por un Backend en Java 17 Spring Boot (API REST) y un Frontend en Angular 17+ Standalone.

## Stack Tecnológico Target
- **Backend**: Java 17+ / Spring Boot 3.x / Spring Data JPA / Jakarta Bean Validation / SQL Server.
- **Frontend**: Angular 17+ (Standalone Components, Reactive Forms, Signals, Angular Material).
- **Especificaciones**: OpenSpec bajo norma ISO/IEC 25030 y sintaxis EARS.
- **Memoria Persistente**: Engram (`.engram/`).

## Reglas de Control y Guardarraíles Estrictos
1. **Entorno Legacy Lectura Exclusiva**: Queda estrictamente PROHIBIDO modificar, editar, compilar o eliminar cualquier objeto o tabla en la Base de Conocimiento (KB) original de GeneXus.
2. **Spec-Driven Development (SDD)**: Ningún subagente escribirá código de aplicación sin una especificación previa aprobada en `openspec/`.
3. **Paridad de Base de Datos**: Reutilizar la estructura relacional de SQL Server existente mapeando tablas y columnas exactas con `@Table(name = "...")` y `@Column(name = "...")`.
4. **Límite de Archivos**: Mantener todos los archivos de configuración por debajo de las 500 líneas para optimizar el consumo de tokens.
5. **Seguridad**: No incluir contraseñas, tokens o secretos en texto plano.
6. **Colección Postman Sincronizada (G-POSTMAN)**: Al trabajar en el backend, TODO endpoint agregado, modificado o eliminado DEBE reflejarse en `docs/postman/ATPPuestos-API.postman_collection.json` en el mismo commit. Es una regla obligatoria de mantenimiento. Ver `docs/postman/README.md`.

## Estructura del Repositorio Target
```text
/
├── agents.md                  ← Arnés de control principal
├── .engram/                   ← Memoria persistente del agente
├── openspec/                  ← Especificaciones formales (EARS)
│   ├── constitution/          ← Reglas del stack
│   └── features/              ← Especificaciones por módulo
├── docs/                      ← Diseño UI/UX (design.md), análisis, y colección Postman
│   └── postman/               ← Colección Postman de la API (G-POSTMAN, obligatoria)
├── backend/                   ← Proyecto Java Spring Boot
└── frontend/                  ← Proyecto Angular Standalone
```
