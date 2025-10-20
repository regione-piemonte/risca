package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsoRidaumSdExtendedDTO extends UsoRidaumSdDTO{

	@JsonProperty("riduzione_aumento")
    private RiduzioneAumentoDTO riduzioneAumento;

	public RiduzioneAumentoDTO getRiduzioneAumento() {
		return riduzioneAumento;
	}

	public void setRiduzioneAumento(RiduzioneAumentoDTO riduzioneAumento) {
		this.riduzioneAumento = riduzioneAumento;
	}
	
}
