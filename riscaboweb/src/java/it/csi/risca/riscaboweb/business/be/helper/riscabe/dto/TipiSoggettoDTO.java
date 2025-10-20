package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipiSoggettoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiSoggettoDTO {
	
	@JsonProperty("id_tipo_soggetto")
    private Long idTipoSoggetto;

    @JsonProperty("cod_tipo_soggetto")
    private String codTipoSoggetto;
    
    @JsonProperty("des_tipo_soggetto")
    private String desTipoSoggetto;
    
    @JsonProperty("ordina_tipo_soggetto")
    private Long ordinaTipoSoggetto;

	public Long getIdTipoSoggetto() {
		return idTipoSoggetto;
	}

	public void setIdTipoSoggetto(Long idTipoSoggetto) {
		this.idTipoSoggetto = idTipoSoggetto;
	}

	public String getCodTipoSoggetto() {
		return codTipoSoggetto;
	}

	public void setCodTipoSoggetto(String codTipoSoggetto) {
		this.codTipoSoggetto = codTipoSoggetto;
	}

	public String getDesTipoSoggetto() {
		return desTipoSoggetto;
	}

	public void setDesTipoSoggetto(String desTipoSoggetto) {
		this.desTipoSoggetto = desTipoSoggetto;
	}
	
	public Long getOrdinaTipoSoggetto() {
		return ordinaTipoSoggetto;
	}

	public void setOrdinaTipoSoggetto(Long ordinaTipoSoggetto) {
		this.ordinaTipoSoggetto = ordinaTipoSoggetto;
	}

    /**
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipiSoggettoDTO that = (TipiSoggettoDTO) o;
        return Objects.equals(idTipoSoggetto, that.idTipoSoggetto) && Objects.equals(codTipoSoggetto, that.codTipoSoggetto) && Objects.equals(desTipoSoggetto, that.desTipoSoggetto) && Objects.equals(ordinaTipoSoggetto, that.ordinaTipoSoggetto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoSoggetto, ordinaTipoSoggetto, codTipoSoggetto, desTipoSoggetto);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TipoRiscossioneDTO {");
        sb.append("         idTipoSoggetto:").append(idTipoSoggetto);
        sb.append(",        codTipoSoggetto:'").append(codTipoSoggetto).append("'");
        sb.append(",        desTipoSoggetto:'").append(desTipoSoggetto).append("'");
        sb.append(",        ordinaTipoSoggetto:").append(ordinaTipoSoggetto);
        sb.append("}");
        return sb.toString();
    }
}
