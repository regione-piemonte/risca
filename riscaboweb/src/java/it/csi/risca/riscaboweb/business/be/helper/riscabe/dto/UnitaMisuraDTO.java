package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UnitaMisuraDTO
 *
 * @author CSI PIEMONTE
 */

public class UnitaMisuraDTO {

	@JsonProperty("id_unita_misura")
    private Long idUnitaMisura;

    @JsonProperty("sigla_unita_misura")
    private String siglaUnitaMisura;
    
    @JsonProperty("des_unita_misura")
    private String desUnitaMisura;

    @JsonProperty("ordina_unita_misura")
    private Long ordinaUnitaMisura;
	
	public Long getIdUnitaMisura() {
		return idUnitaMisura;
	}

	public void setIdUnitaMisura(Long idUnitaMisura) {
		this.idUnitaMisura = idUnitaMisura;
	}

	public String getSiglaUnitaMisura() {
		return siglaUnitaMisura;
	}

	public void setSiglaUnitaMisura(String siglaUnitaMisura) {
		this.siglaUnitaMisura = siglaUnitaMisura;
	}

	public String getDesUnitaMisura() {
		return desUnitaMisura;
	}

	public void setDesUnitaMisura(String desUnitaMisura) {
		this.desUnitaMisura = desUnitaMisura;
	}

	public Long getOrdinaUnitaMisura() {
		return ordinaUnitaMisura;
	}

	public void setOrdinaUnitaMisura(Long ordinaUnitaMisura) {
		this.ordinaUnitaMisura = ordinaUnitaMisura;
	}

}
