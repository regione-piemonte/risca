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

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagopaListaCaricoDTO {

	@JsonProperty("id_pagopa_lista_carico")
	private Long idPagopaListaCarico;

	@JsonProperty("id_lotto")
	private Long idLotto;

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("ind_tipo_aggiornamento")
	private String indTipoAggiornamento;
	
	@JsonProperty("motivazione")
	private String motivazione;

	@JsonProperty("importo_new")
	private BigDecimal importoNew;
	
	@JsonProperty("cod_esito_da_pagopa")
	private String codEsitoDaPagopa;

	@JsonProperty("desc_esito_da_pagopa")
	private String descEsitoDaPagopa;

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

	public Long getIdPagopaListaCarico() {
		return idPagopaListaCarico;
	}

	public void setIdPagopaListaCarico(Long idPagopaListaCarico) {
		this.idPagopaListaCarico = idPagopaListaCarico;
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

	public String getIndTipoAggiornamento() {
		return indTipoAggiornamento;
	}

	public void setIndTipoAggiornamento(String indTipoAggiornamento) {
		this.indTipoAggiornamento = indTipoAggiornamento;
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

}
