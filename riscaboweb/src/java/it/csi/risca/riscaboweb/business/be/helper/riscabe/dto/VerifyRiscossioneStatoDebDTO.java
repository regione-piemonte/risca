package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;
/**
 * VerifyRiscossioneStatoDebDTO
 *
 * @author CSI PIEMONTE
 */

import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifyRiscossioneStatoDebDTO {
	
	@JsonProperty("num_riscossioni")
    private Long  numRiscossioni;
	
	@JsonProperty("num_statdeb")
    private Long  numStatDeb;

	public Long getNumRiscossioni() {
		return numRiscossioni;
	}

	public void setNumRiscossioni(Long numRiscossioni) {
		this.numRiscossioni = numRiscossioni;
	}

	public Long getNumStatDeb() {
		return numStatDeb;
	}

	public void setNumStatDeb(Long numStatDeb) {
		this.numStatDeb = numStatDeb;
	}

}
