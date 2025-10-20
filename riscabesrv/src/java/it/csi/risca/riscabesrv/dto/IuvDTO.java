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
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.dto.utils.ValidatorDto;
import it.csi.risca.riscabesrv.util.ErrorMessages;

public class IuvDTO extends GestAttoreDTO {

	@Min(value = 1, message = "L' id_soggetto deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_iuv")
	private Long idIuv;

    @Size(max = 20, min = 0, message = "nap deve essere compreso tra 0 e 20 caratteri.")
	@JsonProperty("nap")
	private String nap;

	@JsonProperty("stato_iuv")
	private StatoIuvDTO statoIuv;

    @Size(max = 35, min = 0, message = "iuv deve essere compreso tra 0 e 35 caratteri.")
	@JsonProperty("iuv")
	private String iuv;

    @Size(max = 35, min = 0, message = "codice_avviso deve essere compreso tra 0 e 35 caratteri.")
	@JsonProperty("codice_avviso")
	private String codiceAvviso;

    @Digits(integer = 13, fraction = 2, message = "importo deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("importo")
	private BigDecimal importo;

    @Size(max = 4, min = 0, message = "codice_versamento deve essere compreso tra 0 e 4 caratteri.")
	@JsonProperty("codice_versamento")
	private String codiceVersamento;


	public Long getIdIuv() {
		return idIuv;
	}

	public void setIdIuv(Long idIuv) {
		this.idIuv = idIuv;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public StatoIuvDTO getStatoIuv() {
		return statoIuv;
	}

	public void setStatoIuv(StatoIuvDTO statoIuv) {
		this.statoIuv = statoIuv;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCodiceAvviso() {
		return codiceAvviso;
	}

	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public String getCodiceVersamento() {
		return codiceVersamento;
	}

	public void setCodiceVersamento(String codiceVersamento) {
		this.codiceVersamento = codiceVersamento;
	}

	

	@Override
	public String toString() {
		return "IuvDTO [idIuv=" + idIuv + ", nap=" + nap + ", statoIuv=" + statoIuv + ", iuv=" + iuv + ", codiceAvviso="
				+ codiceAvviso + ", importo=" + importo + ", codiceVersamento=" + codiceVersamento + "]";
	}

	public void validate(String fruitore) throws DatiInputErratiException {
		
	    HashMap<String, String> fieldsMap =  new HashMap<String, String>();
			fieldsMap = ValidatorDto.getInvalidMandatoryFields("",
				"nap", getNap() != null,
				"id_stato_iuv", getStatoIuv() == null ? false : getStatoIuv().getIdStatoIuv(),
				"iuv", getIuv() != null,
				"codice_avvviso", getCodiceAvviso() != null ,
				"importo", getImporto() != null,
				"gest_attore_ins", getGestAttoreIns() != null,
				"gest_attore_upd", getGestAttoreUpd() != null
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



}
