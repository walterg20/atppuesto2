# Comando: /verify-quality

## Descripción
Ejecuta la suite de verificación automatizada e inspección de calidad a cargo del subagente `verificador-qa`.

## Instrucciones de Ejecución
1. Invocar al subagente `verificador-qa`.
2. Ejecutar `./gradlew test` o `./mvnw test` en la carpeta `backend/`.
3. Validar componentes y flujos de interfaz en `frontend/`.
4. Reportar discrepancias de comportamiento o problemas de rendimiento SQL.
