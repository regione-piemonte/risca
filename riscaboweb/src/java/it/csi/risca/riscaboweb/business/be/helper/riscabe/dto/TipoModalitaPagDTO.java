package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoModalitaPagDTO {

	@JsonProperty("id_tipo_modalita_pag")
    private Long idTipoModalitaPag;
	
    @JsonProperty("cod_modalita_pag")
    private String codModalitaPag;
	
    @JsonProperty("des_modalita_pag")
    private String desModalitaPag;

	public Long getIdTipoModalitaPag() {
		return idTipoModalitaPag;
	}

	public void setIdTipoModalitaPag(Long idTipoModalitaPag) {
		this.idTipoModalitaPag = idTipoModalitaPag;
	}

	public String getCodModalitaPag() {
		return codModalitaPag;
	}

	public void setCodModalitaPag(String codModalitaPag) {
		this.codModalitaPag = codModalitaPag;
	}

	public String getDesModalitaPag() {
		return desModalitaPag;
	}

	public void setDesModalitaPag(String desModalitaPag) {
		this.desModalitaPag = desModalitaPag;
	}
    
	


}
