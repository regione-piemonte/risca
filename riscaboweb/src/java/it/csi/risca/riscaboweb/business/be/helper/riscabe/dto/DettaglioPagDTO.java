package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DettaglioPagDTO
 *
 * @author CSI PIEMONTE
 */


public class DettaglioPagDTO {

	@JsonProperty("id_dettaglio_pag")
    private Long idDettaglioPag;
	
	@JsonProperty("id_rata_sd")
    private Long idRataSd;

	@JsonProperty("importo_versato")
    private BigDecimal importoVersato;
	
	@JsonProperty("interessi_maturati")
    private BigDecimal interessiMaturati;
	
	@JsonProperty("id_pagamento")
    private Long idPagamento;



	public Long getIdDettaglioPag() {
		return idDettaglioPag;
	}

	public void setIdDettaglioPag(Long idDettaglioPag) {
		this.idDettaglioPag = idDettaglioPag;
	}

	public Long getIdRataSd() {
		return idRataSd;
	}

	public void setIdRataSd(Long idRataSd) {
		this.idRataSd = idRataSd;
	}

	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public BigDecimal getInteressiMaturati() {
		return interessiMaturati;
	}

	public void setInteressiMaturati(BigDecimal interessiMaturati) {
		this.interessiMaturati = interessiMaturati;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}




	
}
