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

public class AvvisoPagamentoDTO extends GestAttoreDTO {

    @Size(max = 20, min = 0, message = "Il nap deve essere compreso tra 0 e 20 caratteri. ")
	@JsonProperty("nap")
	private String nap;

    @Min(value = 1, message = "L' id_spedizione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_spedizione supera il limite massimo consentito per Integer")
	@JsonProperty("id_spedizione")
	private Long idSpedizione;

    @Digits(integer = 5, fraction = 0, message = "prog_nap_avviso_pagamento deve avere al massimo 5 cifre intere")
	@JsonProperty("prog_nap_avviso_pagamento")
	private Integer progNapAvvisoPagamento;

    @Digits(integer = 13, fraction = 2, message = "imp_totale_dovuto deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("imp_totale_dovuto")
	private BigDecimal impTotaleDovuto;

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public Long getIdSpedizione() {
		return idSpedizione;
	}

	public void setIdSpedizione(Long idSpedizione) {
		this.idSpedizione = idSpedizione;
	}

	public Integer getProgNapAvvisoPagamento() {
		return progNapAvvisoPagamento;
	}

	public void setProgNapAvvisoPagamento(Integer progNapAvvisoPagamento) {
		this.progNapAvvisoPagamento = progNapAvvisoPagamento;
	}

	public BigDecimal getImpTotaleDovuto() {
		return impTotaleDovuto;
	}

	public void setImpTotaleDovuto(BigDecimal impTotaleDovuto) {
		this.impTotaleDovuto = impTotaleDovuto;
	}

	public void validate()throws DatiInputErratiException {


		HashMap<String, String> fieldsMap =  new HashMap<String, String>();
		fieldsMap = ValidatorDto.getInvalidMandatoryFields("",
				"nap", getNap(),
				"id_spedizione", getIdSpedizione()
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
