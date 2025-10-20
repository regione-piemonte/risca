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

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.dto.utils.ValidatorDto;
import it.csi.risca.riscabesrv.util.ErrorMessages;

public class RimborsoSdUtilizzatoDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_stato_debitorio deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_stato_debitorio supera il limite massimo consentito per Integer")
	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

    @Min(value = 1, message = "L' id_rimborso deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_rimborso supera il limite massimo consentito per Integer")
	@JsonProperty("id_rimborso")
	private Long idRimborso;

    @Digits(integer = 9, fraction = 2, message = "I'imp_utilizzato deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("imp_utilizzato")
	private BigDecimal impUtilizzato;

  
	
	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public Long getIdRimborso() {
		return idRimborso;
	}

	public void setIdRimborso(Long idRimborso) {
		this.idRimborso = idRimborso;
	}

	public BigDecimal getImpUtilizzato() {
		return impUtilizzato;
	}

	public void setImpUtilizzato(BigDecimal impUtilizzato) {
		this.impUtilizzato = impUtilizzato;
	}

	
public void validate(String fruitore) throws DatiInputErratiException {
		
	    HashMap<String, String> fieldsMap =  new HashMap<String, String>();
			fieldsMap = ValidatorDto.getInvalidMandatoryFields("",
				"id_rimborso", getIdRimborso() != null,
				"id_stato_debitorio", getIdStatoDebitorio() != null,
				"imp_utilizzato", getImpUtilizzato() != null,			
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
