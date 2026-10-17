package ar.com.atp.puestos.nucleo.repository;

import ar.com.atp.puestos.nucleo.domain.Localidad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio de {@link Localidad} → tabla {@code Localida} (R-003, R-020, R-021). */
public interface LocalidadRepository extends JpaRepository<Localidad, Integer> {

    /** Localidades de una provincia (filtro por la FK lógica {@code ProvCod}). */
    List<Localidad> findByProvincia_ProvCodOrderByLocNomAsc(Integer provCod);
}
