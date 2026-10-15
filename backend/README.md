# Backend — Java 17 + Spring Boot 3.x

> **Placeholder.** El scaffold real (Maven Wrapper, `pom.xml`, paquetes base) se crea al
> arrancar el primer módulo, una vez aprobada la spec de alcance (puerta **G-SPEC**).

## Reglas (ver `openspec/constitution/stack.md`)

- Java 17, Spring Boot 3.x, Spring Data JPA, Jakarta Bean Validation.
- Build con **Maven Wrapper** (`./mvnw`).
- Paridad de BD: `@Table(name=...)` / `@Column(name=...)` con el **nombre físico exacto**.
- DTOs como `record`; lógica transaccional en `@Service` con `@Transactional`.
- REST bajo `/api/v1`.
- Conexión a datos por variables de entorno (ver `.env.example`); sin secretos en el repo.

## Estructura prevista

```text
backend/
├── mvnw / mvnw.cmd
├── pom.xml
└── src/main/java/ar/com/atp/puestos/
    ├── config/
    ├── <modulo>/          # entity / repository / service / web
    └── common/
```
