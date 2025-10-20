package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VerifyStatoDebitorioDTO
 *
 * @author CSI PIEMONTE
 */
public class VerifyStatoDebitorioDTO {
	
	@JsonProperty("is_stato_debitorio_valid")
    private Boolean isStatoDebitorioValid;

	public Boolean getIsStatoDebitorioValid() {
		return isStatoDebitorioValid;
	}

	public void setIsStatoDebitorioValid(Boolean isStatoDebitorioValid) {
		this.isStatoDebitorioValid = isStatoDebitorioValid;
	}

}
