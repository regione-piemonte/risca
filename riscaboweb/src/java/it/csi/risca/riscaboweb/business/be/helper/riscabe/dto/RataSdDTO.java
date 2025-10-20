package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RataSdDTO {

	@JsonProperty("id_rata_sd")
	private Long idRataSd;

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("id_rata_sd_padre")
	private Long idRataSdPadre;

	@JsonProperty("canone_dovuto")
	private BigDecimal canoneDovuto;

	@JsonProperty("interessi_maturati")
	private BigDecimal interessiMaturati;

	@JsonProperty("data_scadenza_pagamento")
	private String dataScadenzaPagamento;


	
//	@JsonProperty("dettaglio_pag")
//    private List<DettaglioPagExtendedDTO> dettaglioPag;
	
	

	public Long getIdRataSd() {
		return idRataSd;
	}

	public void setIdRataSd(Long idRataSd) {
		this.idRataSd = idRataSd;
	}

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public Long getIdRataSdPadre() {
		return idRataSdPadre;
	}

	public void setIdRataSdPadre(Long idRataSdPadre) {
		this.idRataSdPadre = idRataSdPadre;
	}

	public BigDecimal getCanoneDovuto() {
		return canoneDovuto;
	}

	public void setCanoneDovuto(BigDecimal canoneDovuto) {
		this.canoneDovuto = canoneDovuto;
	}

	public BigDecimal getInteressiMaturati() {
		return interessiMaturati;
	}

	public void setInteressiMaturati(BigDecimal interessiMaturati) {
		this.interessiMaturati = interessiMaturati;
	}

	public String getDataScadenzaPagamento() {
		return dataScadenzaPagamento;
	}

	public void setDataScadenzaPagamento(String dataScadenzaPagamento) {
		this.dataScadenzaPagamento = dataScadenzaPagamento;
	}

	
}
