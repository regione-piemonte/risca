package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VerifyInvioSpecialeDTO
 *
 * @author CSI PIEMONTE
 */
public class VerifyInvioSpecialeDTO {
	
	@JsonProperty("exist")
    private Boolean exist;

	public Boolean getExist() {
		return exist;
	}

	public void setExist(Boolean exist) {
		this.exist = exist;
	}

}
