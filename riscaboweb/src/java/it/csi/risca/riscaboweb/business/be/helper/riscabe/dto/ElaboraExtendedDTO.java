package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElaboraExtendedDTO extends ElaboraDTO {

	@JsonProperty("registro_elabora")
	private RegistroElaboraExtendedDTO registroElabora;
	
	public RegistroElaboraExtendedDTO getRegistroElabora() {
		return registroElabora;
	}

	public void setRegistroElabora(RegistroElaboraExtendedDTO registroElabora) {
		this.registroElabora = registroElabora;
	}


}
