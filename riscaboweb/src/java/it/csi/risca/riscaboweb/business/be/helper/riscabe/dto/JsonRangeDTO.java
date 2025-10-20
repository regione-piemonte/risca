package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRangeDTO {

	@JsonProperty("soglia_min")
	private BigDecimal sogliaMin;

	@JsonProperty("soglia_max")
	private BigDecimal sogliaMax;

	@JsonProperty("canone_minimo")
	private BigDecimal canoneMinimoRange;

	public BigDecimal getSogliaMin() {
		return sogliaMin;
	}

	public void setSogliaMin(BigDecimal sogliaMin) {
		this.sogliaMin = sogliaMin;
	}

	public BigDecimal getSogliaMax() {
		return sogliaMax;
	}

	public void setSogliaMax(BigDecimal sogliaMax) {
		this.sogliaMax = sogliaMax;
	}

	public BigDecimal getCanoneMinimoRange() {
		return canoneMinimoRange;
	}

	public void setCanoneMinimoRange(BigDecimal canoneMinimoRange) {
		this.canoneMinimoRange = canoneMinimoRange;
	}


}
