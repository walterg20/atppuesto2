package ar.com.atp.puestos.nucleo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ar.com.atp.puestos.nucleo.domain.Localidad;
import ar.com.atp.puestos.nucleo.domain.Provincia;
import ar.com.atp.puestos.support.SqlCollation;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * T-404 (plan.md §5): paridad de lectura de la tabla legacy {@code Localida} y su FK logica
 * {@code ProvCod} (R-003, R-020, R-021). Solo se ejecuta con {@code ATP_DB_IT=true}.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=none")
@EnabledIfEnvironmentVariable(named = "ATP_DB_IT", matches = "true")
@Tag("db")
@DisplayName("T-404 - Localida (Localidad)")
class LocalidadRepositoryIT {

    @Autowired
    private LocalidadRepository repository;

    @Test
    @DisplayName("findAll() lee Localida y aplica RTRIM a LocNom (R-002)")
    void findAllTrimeaNombre() {
        List<Localidad> localidades = repository.findAll();

        assertThat(localidades).allSatisfy(l -> {
            assertThat(l.getLocCod()).isNotNull();
            if (l.getLocNom() != null) {
                assertThat(l.getLocNom()).isEqualTo(l.getLocNom().stripTrailing());
            }
        });
    }

    @Test
    @DisplayName("findByProvincia_ProvCodOrderByLocNomAsc filtra por la FK logica ProvCod (R-003/R-021)")
    void filtrarPorProvincia() {
        List<Localidad> todas = repository.findAll();
        Integer provCod = todas.stream()
                .map(Localidad::getProvincia)
                .filter(Objects::nonNull)
                .map(Provincia::getProvCod)
                .findFirst()
                .orElse(null);
        if (provCod == null) {
            return;
        }

        List<Localidad> deProvincia = repository.findByProvincia_ProvCodOrderByLocNomAsc(provCod);

        Set<Integer> esperado = todas.stream()
                .filter(l -> l.getProvincia() != null && provCod.equals(l.getProvincia().getProvCod()))
                .map(Localidad::getLocCod)
                .collect(Collectors.toSet());
        Set<Integer> obtenido = deProvincia.stream()
                .map(Localidad::getLocCod)
                .collect(Collectors.toSet());

        assertThat(obtenido).isEqualTo(esperado);

        List<String> nombres = deProvincia.stream()
                .map(Localidad::getLocNom)
                .filter(Objects::nonNull)
                .map(String::trim)
                .collect(Collectors.toList());
        assertThat(nombres).isSortedAccordingTo(SqlCollation.spanish());
    }
}
