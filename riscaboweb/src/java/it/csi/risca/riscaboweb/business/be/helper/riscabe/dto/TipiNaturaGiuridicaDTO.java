package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipiNaturaGiuridicaDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiNaturaGiuridicaDTO {

	@JsonProperty("id_tipo_natura_giuridica")
    private Long idTipoNaturaGiuridica;

    @JsonProperty("cod_tipo_natura_giuridica")
    private String codTipoNaturaGiuridica;
    
    @JsonProperty("des_tipo_natura_giuridica")
    private String desTipoNaturaGiuridica;

	@JsonProperty("sigla_tipo_natura_giuridica")
    private String siglaTipoNaturaGiuridica;
    
    @JsonProperty("ordina__natura_giuridica")
    private Long ordinaNaturaGiuridica;
	
    @JsonProperty("flg_pubblico")
    private String flgPubblico;
	
	public Long getIdTipoNaturaGiuridica() {
		return idTipoNaturaGiuridica;
	}

	public void setIdTipoNaturaGiuridica(Long idTipoNaturaGiuridica) {
		this.idTipoNaturaGiuridica = idTipoNaturaGiuridica;
	}

	public String getCodTipoNaturaGiuridica() {
		return codTipoNaturaGiuridica;
	}

	public void setCodTipoNaturaGiuridica(String codTipoNaturaGiuridica) {
		this.codTipoNaturaGiuridica = codTipoNaturaGiuridica;
	}

	public String getDesTipoNaturaGiuridica() {
		return desTipoNaturaGiuridica;
	}

	public void setDesTipoNaturaGiuridica(String desTipoNaturaGiuridica) {
		this.desTipoNaturaGiuridica = desTipoNaturaGiuridica;
	}

    public String getSiglaTipoNaturaGiuridica() {
		return siglaTipoNaturaGiuridica;
	}

	public void setSiglaTipoNaturaGiuridica(String siglaTipoNaturaGiuridica) {
		this.siglaTipoNaturaGiuridica = siglaTipoNaturaGiuridica;
	}

	public Long getOrdinaNaturaGiuridica() {
		return ordinaNaturaGiuridica;
	}

	public void setOrdinaNaturaGiuridica(Long ordinaNaturaGiuridica) {
		this.ordinaNaturaGiuridica = ordinaNaturaGiuridica;
	}
	
	public String getFlgPubblico() {
		return flgPubblico;
	}

	public void setFlgPubblico(String flgPubblico) {
		this.flgPubblico = flgPubblico;
	}
	

    /**
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipiNaturaGiuridicaDTO that = (TipiNaturaGiuridicaDTO) o;
        return Objects.equals(idTipoNaturaGiuridica, that.idTipoNaturaGiuridica) && Objects.equals(codTipoNaturaGiuridica, that.codTipoNaturaGiuridica) && Objects.equals(desTipoNaturaGiuridica, that.desTipoNaturaGiuridica) && Objects.equals(siglaTipoNaturaGiuridica, that.siglaTipoNaturaGiuridica) && Objects.equals(ordinaNaturaGiuridica, that.ordinaNaturaGiuridica) && Objects.equals(flgPubblico, that.flgPubblico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoNaturaGiuridica, codTipoNaturaGiuridica, desTipoNaturaGiuridica, siglaTipoNaturaGiuridica, ordinaNaturaGiuridica, flgPubblico);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TipiSedeDTO {");
        sb.append("         idTipoNaturaGiuridica:").append(idTipoNaturaGiuridica);
        sb.append(",        codTipoNaturaGiuridica:'").append(codTipoNaturaGiuridica).append("'");
        sb.append(",        desTipoNaturaGiuridica:'").append(desTipoNaturaGiuridica).append("'");
        sb.append(",        siglaTipoNaturaGiuridica:'").append(siglaTipoNaturaGiuridica).append("'");
        sb.append(",        ordinaNaturaGiuridica:").append(ordinaNaturaGiuridica);
        sb.append(",        flgPubblico:'").append(flgPubblico).append("'");
        sb.append("}");
        return sb.toString();
    }
}
