package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoOggettoAppDTO {

	@JsonProperty("id_tipo_oggetto_app")
    private Long idTipoOggettoApp;

    @JsonProperty("cod_tipo_oggetto_app")
    private String codTipoOggettoApp;

    @JsonProperty("des_tipo_oggetto_app")
    private String desTipoOggettoApp;

    
	public Long getIdTipoOggettoApp() {
		return idTipoOggettoApp;
	}

	public void setIdTipoOggettoApp(Long idTipoOggettoApp) {
		this.idTipoOggettoApp = idTipoOggettoApp;
	}

	public String getCodTipoOggettoApp() {
		return codTipoOggettoApp;
	}

	public void setCodTipoOggettoApp(String codTipoOggettoApp) {
		this.codTipoOggettoApp = codTipoOggettoApp;
	}

	public String getDesTipoOggettoApp() {
		return desTipoOggettoApp;
	}

	public void setDesTipoOggettoApp(String desTipoOggettoApp) {
		this.desTipoOggettoApp = desTipoOggettoApp;
	}
    
    
    
    
    
	
	
	
}
