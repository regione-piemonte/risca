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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IuvDaInviareDTO {

	@JsonProperty("id_iuv_da_inviare")
	private Long idIuvDaInviare;

	@JsonProperty("id_iuv")
	private Long idIuv;

	@JsonProperty("id_elabora")
	private Long idElabora;

	@JsonProperty("id_lotto")
	private Long idLotto;

	@JsonProperty("flg_da_inviare")
	private Integer flgDaInviare;

	@JsonProperty("canone_dovuto")
	private BigDecimal canoneDovuto;

	@JsonProperty("imp_versato")
	private BigDecimal impVersato;

	@JsonProperty("data_scad_pag")
	private Date dataScadPag;

	@JsonProperty("motivazione")
	private String motivazione;

	@JsonProperty("importoNew")
	private BigDecimal importoNew;

	@JsonProperty("interessi")
	private BigDecimal interessi;

	@JsonProperty("interes_rit_pag")
	private BigDecimal interesRitPag;

	@JsonProperty("tot_spese_notif_per_nap")
	private BigDecimal totSpeseNotifPerNap;

	@JsonProperty("ind_tipo_aggiornamento")
	private String indTipoAggiornamento;

	@JsonProperty("flg_sd_annullato")
	private Integer flgSdAnnullato;

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

	List<PagopaScompVarIuvDTO> componentiImporto;

	public Long getIdIuvDaInviare() {
		return idIuvDaInviare;
	}

	public void setIdIuvDaInviare(Long idIuvDaInviare) {
		this.idIuvDaInviare = idIuvDaInviare;
	}

	public Long getIdIuv() {
		return idIuv;
	}

	public void setIdIuv(Long idIuv) {
		this.idIuv = idIuv;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public Long getIdLotto() {
		return idLotto;
	}

	public void setIdLotto(Long idLotto) {
		this.idLotto = idLotto;
	}

	public Integer getFlgDaInviare() {
		return flgDaInviare;
	}

	public void setFlgDaInviare(Integer flgDaInviare) {
		this.flgDaInviare = flgDaInviare;
	}

	public BigDecimal getCanoneDovuto() {
		return canoneDovuto;
	}

	public void setCanoneDovuto(BigDecimal canoneDovuto) {
		this.canoneDovuto = canoneDovuto;
	}

	public BigDecimal getImpVersato() {
		return impVersato;
	}

	public void setImpVersato(BigDecimal impVersato) {
		this.impVersato = impVersato;
	}

	public Date getDataScadPag() {
		return dataScadPag;
	}

	public void setDataScadPag(Date dataScadPag) {
		this.dataScadPag = dataScadPag;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}

	public BigDecimal getImportoNew() {
		return importoNew;
	}

	public void setImportoNew(BigDecimal importoNew) {
		this.importoNew = importoNew;
	}

	public BigDecimal getInteressi() {
		return interessi;
	}

	public void setInteressi(BigDecimal interessi) {
		this.interessi = interessi;
	}

	public BigDecimal getInteresRitPag() {
		return interesRitPag;
	}

	public void setInteresRitPag(BigDecimal interesRitPag) {
		this.interesRitPag = interesRitPag;
	}

	public BigDecimal getTotSpeseNotifPerNap() {
		return totSpeseNotifPerNap;
	}

	public void setTotSpeseNotifPerNap(BigDecimal totSpeseNotifPerNap) {
		this.totSpeseNotifPerNap = totSpeseNotifPerNap;
	}

	public String getIndTipoAggiornamento() {
		return indTipoAggiornamento;
	}

	public void setIndTipoAggiornamento(String indTipoAggiornamento) {
		this.indTipoAggiornamento = indTipoAggiornamento;
	}

	public Integer getFlgSdAnnullato() {
		return flgSdAnnullato;
	}

	public void setFlgSdAnnullato(Integer flgSdAnnullato) {
		this.flgSdAnnullato = flgSdAnnullato;
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

	public List<PagopaScompVarIuvDTO> getComponentiImporto() {
		return componentiImporto;
	}

	public void setComponentiImporto(List<PagopaScompVarIuvDTO> componentiImporto) {
		this.componentiImporto = componentiImporto;
	}

}
