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

public class PagopaScompRichIuvDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_pagopa_scomp_rich_iuv deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_pagopa_scomp_rich_iuv supera il limite massimo consentito per Integer")
	@JsonProperty("id_pagopa_scomp_rich_iuv")
	private Long idPagopaScompRichIuv;

    @Min(value = 1, message = "L' id_lotto deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_lotto supera il limite massimo consentito per Integer")
	@JsonProperty("id_lotto")
	private Long idLotto;

    @Size(max = 20, min = 0, message = "nap deve essere compreso tra 0 e 20 caratteri.")
	@JsonProperty("nap")
	private String nap;

    @Min(value = 1, message = "L' id_bil_acc deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_bil_acc supera il limite massimo consentito per Integer")
	@JsonProperty("id_bil_acc")
	private Long idBilAcc;
    
    @Digits(integer = 13, fraction = 2, message = "importo_per_nap deve avere al massimo 3 cifre intere e 2 decimali")
	@JsonProperty("importo_per_nap")
	private BigDecimal importoPerNap;

    @Size(max = 20, min = 0, message = "cod_bil_acc deve essere compreso tra 0 e 20 caratteri.")
	@JsonProperty("cod_bil_acc")
	private String codBilAcc;

    @Size(max = 4, min = 0, message = "anno deve essere compreso tra 0 e 4 caratteri.")
	@JsonProperty("anno")
	private String anno;

    @Size(max = 140, min = 0, message = "dati_spec_risc deve essere compreso tra 0 e 140 caratteri.")
	@JsonProperty("dati_spec_risc")
	private String datiSpecRisc;

    @Digits(integer = 13, fraction = 2, message = "importo_per_acc_orig deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("importo_per_acc_orig")
	private BigDecimal importoPerAccOrig;

    @Digits(integer = 11, fraction = 2, message = "importo_per_acc deve avere al massimo 11 cifre intere e 2 decimali")
	@JsonProperty("importo_per_acc")
	private BigDecimal importoPerAcc;

    @Min(value = 1, message = "L' id_tipo_bil_acc deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_bil_acc supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_bil_acc")
	private Long idTipoBilAcc;

    @Digits(integer = 13, fraction = 2, message = "sum_imp_per_acc_orig deve avere al massimo 3 cifre intere e 2 decimali")
	@JsonProperty("sum_imp_per_acc_orig")
	private BigDecimal sumImpPerAccOrig;

    @Digits(integer = 13, fraction = 2, message = "perc_tot_imp deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("perc_tot_imp")
	private BigDecimal percTotImp;

    @Size(max = 2000, min = 0, message = "note_backoffice deve essere compreso tra 0 e 2000 caratteri.")
	@JsonProperty("note_backoffice")
	private String noteBackoffice;


	public Long getIdPagopaScompRichIuv() {
		return idPagopaScompRichIuv;
	}

	public void setIdPagopaScompRichIuv(Long idPagopaScompRichIuv) {
		this.idPagopaScompRichIuv = idPagopaScompRichIuv;
	}

	public Long getIdLotto() {
		return idLotto;
	}

	public void setIdLotto(Long idLotto) {
		this.idLotto = idLotto;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public Long getIdBilAcc() {
		return idBilAcc;
	}

	public void setIdBilAcc(Long idBilAcc) {
		this.idBilAcc = idBilAcc;
	}

	public BigDecimal getImportoPerNap() {
		return importoPerNap;
	}

	public void setImportoPerNap(BigDecimal importoPerNap) {
		this.importoPerNap = importoPerNap;
	}

	public String getCodBilAcc() {
		return codBilAcc;
	}

	public void setCodBilAcc(String codBilAcc) {
		this.codBilAcc = codBilAcc;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getDatiSpecRisc() {
		return datiSpecRisc;
	}

	public void setDatiSpecRisc(String datiSpecRisc) {
		this.datiSpecRisc = datiSpecRisc;
	}

	public BigDecimal getImportoPerAccOrig() {
		return importoPerAccOrig;
	}

	public void setImportoPerAccOrig(BigDecimal importoPerAccOrig) {
		this.importoPerAccOrig = importoPerAccOrig;
	}

	public BigDecimal getImportoPerAcc() {
		return importoPerAcc;
	}

	public void setImportoPerAcc(BigDecimal importoPerAcc) {
		this.importoPerAcc = importoPerAcc;
	}

	public Long getIdTipoBilAcc() {
		return idTipoBilAcc;
	}

	public void setIdTipoBilAcc(Long idTipoBilAcc) {
		this.idTipoBilAcc = idTipoBilAcc;
	}

	public BigDecimal getSumImpPerAccOrig() {
		return sumImpPerAccOrig;
	}

	public void setSumImpPerAccOrig(BigDecimal sumImpPerAccOrig) {
		this.sumImpPerAccOrig = sumImpPerAccOrig;
	}

	public BigDecimal getPercTotImp() {
		return percTotImp;
	}

	public void setPercTotImp(BigDecimal percTotImp) {
		this.percTotImp = percTotImp;
	}

	public String getNoteBackoffice() {
		return noteBackoffice;
	}

	public void setNoteBackoffice(String noteBackoffice) {
		this.noteBackoffice = noteBackoffice;
	}





	@Override
	public String toString() {
		return "PagopaScompRichIuvDTO [idPagopaScompRichIuv=" + idPagopaScompRichIuv + ", idLotto=" + idLotto + ", nap="
				+ nap + ", idBilAcc=" + idBilAcc + ", importoPerNap=" + importoPerNap + ", codBilAcc=" + codBilAcc
				+ ", anno=" + anno + ", datiSpecRisc=" + datiSpecRisc + ", importoPerAccOrig=" + importoPerAccOrig
				+ ", importoPerAcc=" + importoPerAcc + ", idTipoBilAcc=" + idTipoBilAcc + ", sumImpPerAccOrig="
				+ sumImpPerAccOrig + ", percTotImp=" + percTotImp + ", noteBackoffice=" + noteBackoffice
				+ ", getGestAttoreIns()=" + getGestAttoreIns() + ", getGestDataIns()=" + getGestDataIns()
				+ ", getGestAttoreUpd()=" + getGestAttoreUpd() + ", getGestDataUpd()=" + getGestDataUpd()
				+ ", getGestUid()=" + getGestUid() + "]";
	}

	public void validate(String fruitore) throws DatiInputErratiException {
		
	    HashMap<String, String> fieldsMap =  new HashMap<String, String>();
			fieldsMap = ValidatorDto.getInvalidMandatoryFields("",
				"id_lotto", getIdLotto() != null,
				"nap", getNap() != null,
				"importo_per_nap", getImportoPerNap() != null,			
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
