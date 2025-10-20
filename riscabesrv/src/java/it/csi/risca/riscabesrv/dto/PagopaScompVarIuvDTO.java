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

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.dto.utils.ValidatorDto;
import it.csi.risca.riscabesrv.util.ErrorMessages;

public class PagopaScompVarIuvDTO {

	@JsonProperty("id_pagopa_scomp_var_iuv")
	private Long idPagopaScompVarIuv;

	@JsonProperty("id_lotto")
	private Long idLotto;

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("id_bil_acc")
	private Long idBilAcc;

	@JsonProperty("importo_per_nap")
	private BigDecimal importoPerNap;

	@JsonProperty("cod_bil_acc")
	private String codBilAcc;

	@JsonProperty("anno")
	private String anno;

	@JsonProperty("dati_spec_risc")
	private String datiSpecRisc;

	@JsonProperty("importo_per_acc_orig")
	private BigDecimal importoPerAccOrig;

	@JsonProperty("importo_per_acc")
	private BigDecimal importoPerAcc;

	@JsonProperty("id_tipo_bil_acc")
	private Long idTipoBilAcc;

	@JsonProperty("sum_imp_per_acc_orig")
	private BigDecimal sumImpPerAccOrig;

	@JsonProperty("perc_tot_imp")
	private BigDecimal percTotImp;

	@JsonProperty("note_backoffice")
	private String noteBackoffice;

	@JsonProperty("gest_attore_ins")
	private String gestAttoreIns;

	@JsonProperty("gest_data_ins")
	private Date gestDataIns;

	@JsonProperty("gest_attore_upd")
	private String gestAttoreUpd;

	@JsonProperty("gest_data_upd")
	private Date gestDataUpd;

	@JsonProperty("gest_uid")
	private String gestUid;

	public Long getIdPagopaScompVarIuv() {
		return idPagopaScompVarIuv;
	}

	public void setIdPagopaScompVarIuv(Long idPagopaScompVarIuv) {
		this.idPagopaScompVarIuv = idPagopaScompVarIuv;
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

	public String getGestAttoreIns() {
		return gestAttoreIns;
	}

	public void setGestAttoreIns(String gestAttoreIns) {
		this.gestAttoreIns = gestAttoreIns;
	}

	public Date getGestDataIns() {
		return gestDataIns;
	}

	public void setGestDataIns(Date gestDataIns) {
		this.gestDataIns = gestDataIns;
	}

	public String getGestAttoreUpd() {
		return gestAttoreUpd;
	}

	public void setGestAttoreUpd(String gestAttoreUpd) {
		this.gestAttoreUpd = gestAttoreUpd;
	}

	public Date getGestDataUpd() {
		return gestDataUpd;
	}

	public void setGestDataUpd(Date gestDataUpd) {
		this.gestDataUpd = gestDataUpd;
	}

	public String getGestUid() {
		return gestUid;
	}

	public void setGestUid(String gestUid) {
		this.gestUid = gestUid;
	}

	@Override
	public String toString() {
		return "PagopaScompRichIuvDTO [idPagopaScompVarIuv=" + idPagopaScompVarIuv + ", idLotto=" + idLotto + ", nap="
				+ nap + ", idBilAcc=" + idBilAcc + ", importoPerNap=" + importoPerNap + ", codBilAcc=" + codBilAcc
				+ ", anno=" + anno + ", datiSpecRisc=" + datiSpecRisc + ", importoPerAccOrig=" + importoPerAccOrig
				+ ", importoPerAcc=" + importoPerAcc + ", idTipoBilAcc=" + idTipoBilAcc + ", sumImpPerAccOrig="
				+ sumImpPerAccOrig + ", percTotImp=" + percTotImp + ", noteBackoffice=" + noteBackoffice
				+ ", gestAttoreIns=" + gestAttoreIns + ", gestDataIns=" + gestDataIns + ", gestAttoreUpd="
				+ gestAttoreUpd + ", gestDataUpd=" + gestDataUpd + ", gestUid=" + gestUid + "]";
	}

	public void validate(String fruitore) throws DatiInputErratiException {

		HashMap<String, String> fieldsMap = new HashMap<String, String>();
		fieldsMap = ValidatorDto.getInvalidMandatoryFields("", "id_lotto", getIdLotto() != null, "nap",
				getNap() != null, "importo_per_nap", getImportoPerNap() != null, "gest_attore_ins",
				getGestAttoreIns() != null, "gest_attore_upd", getGestAttoreUpd() != null);

		if (!fieldsMap.isEmpty()) {
			for (Map.Entry<String, String> field : fieldsMap.entrySet()) {

				ErrorObjectDTO error = new ErrorObjectDTO();
				error.setCode(ErrorMessages.CODE_E001_ERRORE_VALIDAZIONE_DATI_INGRESSO);
				error.setTitle(ErrorMessages.MESSAGE_E001_ERRORE_VALIDAZIONE_DATI_INGRESSO + " Campo: " + field.getKey()
						+ " errore: " + field.getValue());
				throw new DatiInputErratiException(error);
			}

		}
	}

}
