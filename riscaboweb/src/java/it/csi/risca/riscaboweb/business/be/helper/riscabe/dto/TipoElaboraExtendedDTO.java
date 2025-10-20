package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoElaboraExtendedDTO extends TipoElaboraDTO {
	
	@JsonProperty("ambito")
    private AmbitoDTO ambito;
	
	@JsonProperty("funzionalita")
    private FunzionalitaDTO funzionalitaDTO;

	public AmbitoDTO getAmbito() {
		return ambito;
	}

	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}

	public FunzionalitaDTO getFunzionalitaDTO() {
		return funzionalitaDTO;
	}

	public void setFunzionalitaDTO(FunzionalitaDTO funzionalitaDTO) {
		this.funzionalitaDTO = funzionalitaDTO;
	}
	
	
}
