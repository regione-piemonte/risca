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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.dto.utils.ValidatorDto;
import it.csi.risca.riscabesrv.util.ErrorMessages;

public class RataSdDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_rata_sd deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_rata_sd supera il limite massimo consentito per Integer")
	@JsonProperty("id_rata_sd")
	private Long idRataSd;

    @Min(value = 1, message = "L' id_stato_debitorio deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_stato_debitorio supera il limite massimo consentito per Integer")
	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

    @Min(value = 1, message = "L' id_rata_sd_padre deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_rata_sd_padre supera il limite massimo consentito per Integer")
	@JsonProperty("id_rata_sd_padre")
	private Long idRataSdPadre;

    @Digits(integer = 9, fraction = 2, message = "canone_dovuto deve avere al massimo9 cifre intere e 2 decimali")
	@JsonProperty("canone_dovuto")
	private BigDecimal canoneDovuto;

    @Digits(integer = 9, fraction = 2, message = "I'interessi_maturati deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("interessi_maturati")
	private BigDecimal interessiMaturati;

    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2})$", message = "Formato data non valido. Utilizzare yyyy-MM-dd.")
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



//	public List<DettaglioPagExtendedDTO> getDettaglioPag() {
//		return dettaglioPag;
//	}
//
//	public void setDettaglioPag(List<DettaglioPagExtendedDTO> dettaglioPag) {
//		this.dettaglioPag = dettaglioPag;
//	}



	public void validate() throws DatiInputErratiException {
		HashMap<String, String> fieldsMap =  new HashMap<String, String>();
		fieldsMap = ValidatorDto.getInvalidMandatoryFields("",
				"id_stato_debitorio", getIdStatoDebitorio()
				);


		if(!fieldsMap.isEmpty()) {	
			for (Map.Entry<String, String> field: fieldsMap.entrySet()) {
			
				ErrorObjectDTO error = new ErrorObjectDTO();
				error.setCode(ErrorMessages.CODE_E001_ERRORE_VALIDAZIONE_DATI_INGRESSO);
				error.setTitle(ErrorMessages.MESSAGE_E001_ERRORE_VALIDAZIONE_DATI_INGRESSO+" Campo: "+field.getKey()+" errore: "+field.getValue());
				throw new DatiInputErratiException(error);
			}
		
			
		}		
		
	}

	@Override
	public String toString() {
		return "RataSdDTO [idRataSd=" + idRataSd + ", idStatoDebitorio=" + idStatoDebitorio + ", idRataSdPadre="
				+ idRataSdPadre + ", canoneDovuto=" + canoneDovuto + ", interessiMaturati=" + interessiMaturati
				+ ", dataScadenzaPagamento=" + dataScadenzaPagamento + ", getGestAttoreIns()=" + getGestAttoreIns()
				+ ", getGestDataIns()=" + getGestDataIns() + ", getGestAttoreUpd()=" + getGestAttoreUpd()
				+ ", getGestDataUpd()=" + getGestDataUpd() + ", getGestUid()=" + getGestUid() + "]";
	}

	
}
