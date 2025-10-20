package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UtRimbDTO
 *
 * @author CSI PIEMONTE
 */


public class UtRimbDTO {
	
	@JsonProperty("cod_utenza")
	private String codUtenza;
	
	@JsonProperty("desc_periodo_pagamento")
	private String descPeriodoPagamento;

	public String getCodUtenza() {
		return codUtenza;
	}

	public void setCodUtenza(String codUtenza) {
		this.codUtenza = codUtenza;
	}

	public String getDescPeriodoPagamento() {
		return descPeriodoPagamento;
	}

	public void setDescPeriodoPagamento(String descPeriodoPagamento) {
		this.descPeriodoPagamento = descPeriodoPagamento;
	}
	

}
