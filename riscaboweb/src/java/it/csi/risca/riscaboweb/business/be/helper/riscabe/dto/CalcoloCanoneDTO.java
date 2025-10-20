package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CalcoloCanoneDTO {
	
	@JsonProperty("calcolo_canone")
    private BigDecimal calcoloCanone;
	
	public BigDecimal getCalcoloCanone() {
		return calcoloCanone;
	}

	public void setCalcoloCanone(BigDecimal calcoloCanone) {
		this.calcoloCanone = calcoloCanone;
	}

}
