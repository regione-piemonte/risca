package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipiRecapitoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiRecapitoDTO {
	
	@JsonProperty("id_tipo_recapito")
    private Long idTipoRecapito;
	  
    @JsonProperty("cod_tipo_recapito")
    private String codTipoRecapito;
    
    @JsonProperty("des_tipo_recapito")
    private String desTipoRecapito;
	
	public Long getIdTipoRecapito() {
		return idTipoRecapito;
	}

	public void setIdTipoRecapito(Long idTipoRecapito) {
		this.idTipoRecapito = idTipoRecapito;
	}

	public String getCodTipoRecapito() {
		return codTipoRecapito;
	}

	public void setCodTipoRecapito(String codTipoRecapito) {
		this.codTipoRecapito = codTipoRecapito;
	}

	public String getDesTipoRecapito() {
		return desTipoRecapito;
	}

	public void setDesTipoRecapito(String desTipoRecapito) {
		this.desTipoRecapito = desTipoRecapito;
	}

}
