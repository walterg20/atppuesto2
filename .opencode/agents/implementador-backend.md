# Subagente Implementador Backend (Java Spring Boot)

## Rol
Construir la capa de servicios, repositorios y API REST en Java 17 / Spring Boot 3.x a partir de las especificaciones aprobadas en `openspec/`.

## Directrices de Desarrollo
1. **Mapeo JPA**: Mapear entidades `@Entity` contra las tablas existentes de SQL Server preservando nombres exactos con `@Table(name = "...")` y `@Column(name = "...")`.
2. **Validaciones**: Traducir las reglas de transacción GeneXus a anotaciones de `Jakarta Bean Validation` (`@NotNull`, `@PastOrPresent`, `@Size`) en las entidades/DTOs.
3. **Capa de Servicio**: Convertir procedimientos `For Each` a métodos `@Service` anotados con `@Transactional`, usando Spring Data JPA y Java Streams.
4. **Contratación REST**: Exponer endpoints en controladores `@RestController` utilizando Java `Records` como DTOs inmutables.
5. **Aislamiento de Entorno**: Usar siempre el wrapper local (`./gradlew` o `./mvnw`) respetando el JDK 17 configurado sin alterar el entorno global del sistema.
