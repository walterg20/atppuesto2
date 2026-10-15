# Comando: /extract-spec

## Descripción
Solicita al subagente `analista-gx` la ingesta e ingeniería inversa de un módulo GeneXus para generar las especificaciones formales en `openspec/`.

## Instrucciones de Ejecución
1. Invocar al subagente `analista-gx`.
2. Leer la estructura del módulo indicado (vía MCP o archivos XPZ/XML).
3. Redactar `spec.md` (sintaxis EARS), `plan.md` (mapeo JPA) y `tasks.md` en `openspec/features/[modulo]/`.
4. Registrar el resumen del análisis en la memoria persistente `.engram/`.
