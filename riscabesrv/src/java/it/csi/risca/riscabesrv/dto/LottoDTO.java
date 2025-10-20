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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.dto.utils.ValidatorDto;
import it.csi.risca.riscabesrv.util.ErrorMessages;

public class LottoDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_lotto deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_lotto supera il limite massimo consentito per Integer")
	@JsonProperty("id_lotto")
	private Long idLotto;

    @Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;

    @Size(max = 50, min = 0, message = "nome_lotto deve essere compreso tra 0 e 50 caratteri.")
	@JsonProperty("nome_lotto")
	private String nomeLotto;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_lotto")
	private Date dataLotto;

    @Min(value = 0, message = " flg_inviato deve essere 0 o 1 ")
    @Max(value = 1, message = " flg_inviato deve essere 0 o 1 ")
	@JsonProperty("flg_inviato")
	private Integer flgInviato;

    @Min(value = 0, message = " flg_ricevuto deve essere 0 o 1 ")
    @Max(value = 1, message = " flg_ricevuto deve essere 0 o 1 ")
	@JsonProperty("flg_ricevuto")
	private Integer flgRicevuto;

    @Min(value = 0, message = " flg_elaborato deve essere 0 o 1 ")
    @Max(value = 1, message = " flg_elaborato deve essere 0 o 1 ")
	@JsonProperty("flg_elaborato")
	private Integer flgElaborato;

    @Size(max = 3, min = 0, message = "cod_esito_da_pagopa deve essere compreso tra 0 e 3 caratteri.")
	@JsonProperty("cod_esito_da_pagopa")
	private String codEsitoDaPagopa;

    @Size(max = 200, min = 0, message = "desc_esito_da_pagopa deve essere compreso tra 0 e 200 caratteri.")
	@JsonProperty("desc_esito_da_pagopa")
	private String descEsitoDaPagopa;

    @Size(max = 3, min = 0, message = "cod_esito_acquisizione_pagopa deve essere compreso tra 0 e 3 caratteri.")
	@JsonProperty("cod_esito_acquisizione_pagopa")
	private String codEsitoAcquisizionePagopa;

    @Size(max = 200, min = 0, message = "desc_esito_acquisizione_pagopa deve essere compreso tra 0 e 200 caratteri.")
	@JsonProperty("desc_esito_acquisizione_pagopa")
	private String descEsitoAcquisizionePagopa;

	

	public Long getIdLotto() {
		return idLotto;
	}

	public void setIdLotto(Long idLotto) {
		this.idLotto = idLotto;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public String getNomeLotto() {
		return nomeLotto;
	}

	public void setNomeLotto(String nomeLotto) {
		this.nomeLotto = nomeLotto;
	}

	public Date getDataLotto() {
		return dataLotto;
	}

	public void setDataLotto(Date dataLotto) {
		this.dataLotto = dataLotto;
	}

	public Integer getFlgInviato() {
		return flgInviato;
	}

	public void setFlgInviato(Integer flgInviato) {
		this.flgInviato = flgInviato;
	}

	public Integer getFlgRicevuto() {
		return flgRicevuto;
	}

	public void setFlgRicevuto(Integer flgRicevuto) {
		this.flgRicevuto = flgRicevuto;
	}

	public Integer getFlgElaborato() {
		return flgElaborato;
	}

	public void setFlgElaborato(Integer flgElaborato) {
		this.flgElaborato = flgElaborato;
	}

	public String getCodEsitoDaPagopa() {
		return codEsitoDaPagopa;
	}

	public void setCodEsitoDaPagopa(String codEsitoDaPagopa) {
		this.codEsitoDaPagopa = codEsitoDaPagopa;
	}

	public String getDescEsitoDaPagopa() {
		return descEsitoDaPagopa;
	}

	public void setDescEsitoDaPagopa(String descEsitoDaPagopa) {
		this.descEsitoDaPagopa = descEsitoDaPagopa;
	}

	public String getCodEsitoAcquisizionePagopa() {
		return codEsitoAcquisizionePagopa;
	}

	public void setCodEsitoAcquisizionePagopa(String codEsitoAcquisizionePagopa) {
		this.codEsitoAcquisizionePagopa = codEsitoAcquisizionePagopa;
	}

	public String getDescEsitoAcquisizionePagopa() {
		return descEsitoAcquisizionePagopa;
	}

	public void setDescEsitoAcquisizionePagopa(String descEsitoAcquisizionePagopa) {
		this.descEsitoAcquisizionePagopa = descEsitoAcquisizionePagopa;
	}

	

	@Override
	public String toString() {
		return "LottoDTO [idLotto=" + idLotto + ", idElabora=" + idElabora + ", nomeLotto=" + nomeLotto + ", dataLotto="
				+ dataLotto + ", flgInviato=" + flgInviato + ", flgRicevuto=" + flgRicevuto + ", flgElaborato="
				+ flgElaborato + ", codEsitoDaPagopa=" + codEsitoDaPagopa + ", descEsitoDaPagopa=" + descEsitoDaPagopa
				+ ", codEsitoAcquisizionePagopa=" + codEsitoAcquisizionePagopa + ", descEsitoAcquisizionePagopa="
				+ descEsitoAcquisizionePagopa + "]";
	}

	public void validate(String fruitore) throws DatiInputErratiException {
		
	    HashMap<String, String> fieldsMap =  new HashMap<String, String>();
			fieldsMap = ValidatorDto.getInvalidMandatoryFields("",
				"id_elabora", getIdElabora() != null,
				"nome_lotto", getNomeLotto() != null,
				"data_lotto", getDataLotto() != null,			
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
