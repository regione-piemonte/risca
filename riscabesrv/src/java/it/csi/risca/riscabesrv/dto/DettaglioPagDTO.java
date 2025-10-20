/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DettaglioPagDTO
 *
 * @author CSI PIEMONTE
 */


public class DettaglioPagDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_dettaglio_pag deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_dettaglio_pag supera il limite massimo consentito per Integer")
	@JsonProperty("id_dettaglio_pag")
    private Long idDettaglioPag;
	
    @Min(value = 1, message = "L' id_rata_sd deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_rata_sd supera il limite massimo consentito per Integer")
	@JsonProperty("id_rata_sd")
    private Long idRataSd;

    @Digits(integer = 13, fraction = 2, message = "importo_versato deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("importo_versato")
    private BigDecimal importoVersato;
	
    @Digits(integer = 13, fraction = 2, message = "interessi_maturati deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("interessi_maturati")
    private BigDecimal interessiMaturati;
	
    @Min(value = 1, message = "L' id_pagamento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_pagamento supera il limite massimo consentito per Integer")
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
