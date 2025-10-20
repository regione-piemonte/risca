package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OggettoAppDTO {
	
	@JsonProperty("id_oggetto_app")
    private Long idOggettoApp;

    @JsonProperty("cod_oggetto_app")
    private String codOggettoApp;
    
    @JsonProperty("tipo_oggetto_app")
    private TipoOggettoAppDTO tipoOggettoAppDTO;
    
    @JsonProperty("des_oggetto_app")
    private String desOggettoApp;

	public Long getIdOggettoApp() {
		return idOggettoApp;
	}

	public void setIdOggettoApp(Long idOggettoApp) {
		this.idOggettoApp = idOggettoApp;
	}

	public String getCodOggettoApp() {
		return codOggettoApp;
	}

	public void setCodOggettoApp(String codOggettoApp) {
		this.codOggettoApp = codOggettoApp;
	}
	
	public TipoOggettoAppDTO getTipoOggettoAppDTO() {
		return tipoOggettoAppDTO;
	}

	public void setTipoOggettoAppDTO(TipoOggettoAppDTO tipoOggettoAppDTO) {
		this.tipoOggettoAppDTO = tipoOggettoAppDTO;
	}

	public String getDesOggettoApp() {
		return desOggettoApp;
	}

	public void setDesOggettoApp(String desOggettoApp) {
		this.desOggettoApp = desOggettoApp;
	}
    
    
}
