package ar.com.atp.puestos.nucleo.service;

import ar.com.atp.puestos.nucleo.domain.Area;
import ar.com.atp.puestos.nucleo.domain.Calle;
import ar.com.atp.puestos.nucleo.domain.Camino;
import ar.com.atp.puestos.nucleo.domain.Feriado;
import ar.com.atp.puestos.nucleo.domain.Jurisdiccion;
import ar.com.atp.puestos.nucleo.domain.Localidad;
import ar.com.atp.puestos.nucleo.domain.Producto;
import ar.com.atp.puestos.nucleo.domain.ProductoVigencia;
import ar.com.atp.puestos.nucleo.domain.Provincia;
import ar.com.atp.puestos.nucleo.web.dto.AreaDto;
import ar.com.atp.puestos.nucleo.web.dto.CalleDto;
import ar.com.atp.puestos.nucleo.web.dto.CaminoDto;
import ar.com.atp.puestos.nucleo.web.dto.FeriadoDto;
import ar.com.atp.puestos.nucleo.web.dto.JurisdiccionDto;
import ar.com.atp.puestos.nucleo.web.dto.LocalidadDto;
import ar.com.atp.puestos.nucleo.web.dto.ProductoDto;
import ar.com.atp.puestos.nucleo.web.dto.ProductoVigenciaDto;
import ar.com.atp.puestos.nucleo.web.dto.ProvinciaDto;
import java.util.List;

/** Conversión entidad → DTO de lectura (plan §3.3). Métodos estáticos, sin estado. */
final class NucleoMapper {

    private NucleoMapper() {
    }

    static ProvinciaDto toDto(Provincia p) {
        return new ProvinciaDto(p.getProvCod(), p.getProvNombre());
    }

    static LocalidadDto toDto(Localidad l) {
        Provincia prov = l.getProvincia();
        return new LocalidadDto(
                l.getLocCod(),
                l.getLocNom(),
                l.getLocCodPost(),
                prov != null ? prov.getProvCod() : null,
                prov != null ? prov.getProvNombre() : null);
    }

    static CalleDto toDto(Calle c) {
        return new CalleDto(c.getCallCod(), c.getCallNom());
    }

    static CaminoDto toDto(Camino c) {
        return new CaminoDto(c.getCamID(), c.getCanRuta());
    }

    static AreaDto toDto(Area a) {
        return new AreaDto(a.getAreaId(), a.getAreaDesc());
    }

    static JurisdiccionDto toDto(Jurisdiccion j) {
        return new JurisdiccionDto(j.getJurCod(), j.getJurNom());
    }

    static FeriadoDto toDto(Feriado f) {
        return new FeriadoDto(
                f.getFerFecha(),
                f.getFerDescripcion(),
                f.getFerTipo(),
                f.getFerFchReg(),
                f.getFerUsuario());
    }

    static ProductoVigenciaDto toDto(ProductoVigencia v) {
        return new ProductoVigenciaDto(
                v.getProdFchVig(),
                v.getProdImpu1(),
                v.getProdImpu2(),
                v.getProdImpu3(),
                v.getProdImpu4());
    }

    static ProductoDto toDto(Producto p) {
        List<ProductoVigenciaDto> vigencias = p.getVigencias().stream()
                .map(NucleoMapper::toDto)
                .toList();
        return new ProductoDto(
                p.getProdCod(),
                p.getProdDesc(),
                p.getProdTipCal(),
                p.getTpPriID(),
                p.getUMedNroId(),
                p.getUMedNombre(),
                p.getProdAcop(),
                p.getProdAnula(),
                vigencias);
    }
}
