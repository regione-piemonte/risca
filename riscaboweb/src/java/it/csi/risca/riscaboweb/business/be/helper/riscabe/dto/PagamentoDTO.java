/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PagamentoDTO
 *
 * @author CSI PIEMONTE
 */
public class PagamentoDTO  {
	
	@JsonProperty("id_pagamento")
	private Long idPagamento;
	
	@JsonProperty("id_ambito")
	private Long idAmbito;
	
	@JsonProperty("id_tipologia_pag")
	private Long idTipologiaPag;
	
	@JsonProperty("id_tipo_modalita_pag")
	private Long idTipoModalitaPag;
	
	@JsonProperty("id_file_poste")
	private Long idFilePoste;

	@JsonProperty("id_immagine")
	private Long idImmagine;
	
	@JsonProperty("causale")
	private String causale;

	@JsonProperty("data_op_val")
	private Date dataOpVal;

	@JsonProperty("importo_versato")
	private BigDecimal importoVersato;
	
	@JsonProperty("data_download")
	private Date dataDownload;
	
	@JsonProperty("quinto_campo")
	private String quintoCampo;

	@JsonProperty("cro")
	private String cro;
	
	@JsonProperty("note")
	private String note;

	@JsonProperty("numero_pagamento")
	private Long numeroPagamento;

	@JsonProperty("soggetto_versamento")
	private String soggettoVersamento;

	@JsonProperty("indirizzo_versamento")
	private String indirizzoVersamento;

	@JsonProperty("civico_versamento")
	private String civicoVersamento;
	
	@JsonProperty("frazione_versamento")
	private String frazioneVersamento;
	
	@JsonProperty("comune_versamento")
	private String comuneVersamento;
	
	@JsonProperty("cap_versamento")
	private String capVersamento;
	
	@JsonProperty("prov_versamento")
	private String provVersamento;
	
	@JsonProperty("flg_rimborsato")
	private Integer flgRimborsato;
	
	@JsonProperty("imp_da_assegnare")
	private BigDecimal impDaAssegnare;

	@JsonProperty("codice_avviso")
	private String codiceAvviso;


	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Long getIdTipologiaPag() {
		return idTipologiaPag;
	}

	public void setIdTipologiaPag(Long idTipologiaPag) {
		this.idTipologiaPag = idTipologiaPag;
	}

	public Long getIdTipoModalitaPag() {
		return idTipoModalitaPag;
	}

	public void setIdTipoModalitaPag(Long idTipoModalitaPag) {
		this.idTipoModalitaPag = idTipoModalitaPag;
	}

	public Long getIdFilePoste() {
		return idFilePoste;
	}

	public void setIdFilePoste(Long idFilePoste) {
		this.idFilePoste = idFilePoste;
	}

	public Long getIdImmagine() {
		return idImmagine;
	}

	public void setIdImmagine(Long idImmagine) {
		this.idImmagine = idImmagine;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public Date getDataOpVal() {
		return dataOpVal;
	}

	public void setDataOpVal(Date dataOpVal) {
		this.dataOpVal = dataOpVal;
	}

	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public Date getDataDownload() {
		return dataDownload;
	}

	public void setDataDownload(Date dataDownload) {
		this.dataDownload = dataDownload;
	}

	public String getQuintoCampo() {
		return quintoCampo;
	}

	public void setQuintoCampo(String quintoCampo) {
		this.quintoCampo = quintoCampo;
	}

	public String getCro() {
		return cro;
	}

	public void setCro(String cro) {
		this.cro = cro;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getNumeroPagamento() {
		return numeroPagamento;
	}

	public void setNumeroPagamento(Long numeroPagamento) {
		this.numeroPagamento = numeroPagamento;
	}

	public String getSoggettoVersamento() {
		return soggettoVersamento;
	}

	public void setSoggettoVersamento(String soggettoVersamento) {
		this.soggettoVersamento = soggettoVersamento;
	}

	public String getIndirizzoVersamento() {
		return indirizzoVersamento;
	}

	public void setIndirizzoVersamento(String indirizzoVersamento) {
		this.indirizzoVersamento = indirizzoVersamento;
	}

	public String getCivicoVersamento() {
		return civicoVersamento;
	}

	public void setCivicoVersamento(String civicoVersamento) {
		this.civicoVersamento = civicoVersamento;
	}

	public String getFrazioneVersamento() {
		return frazioneVersamento;
	}

	public void setFrazioneVersamento(String frazioneVersamento) {
		this.frazioneVersamento = frazioneVersamento;
	}

	public String getComuneVersamento() {
		return comuneVersamento;
	}

	public void setComuneVersamento(String comuneVersamento) {
		this.comuneVersamento = comuneVersamento;
	}

	public String getCapVersamento() {
		return capVersamento;
	}

	public void setCapVersamento(String capVersamento) {
		this.capVersamento = capVersamento;
	}

	public String getProvVersamento() {
		return provVersamento;
	}

	public void setProvVersamento(String provVersamento) {
		this.provVersamento = provVersamento;
	}

	public Integer getFlgRimborsato() {
		return flgRimborsato;
	}

	public void setFlagRimborsato(Integer flgRimborsato) {
		this.flgRimborsato = flgRimborsato;
	}

	public BigDecimal getImpDaAssegnare() {
		return impDaAssegnare;
	}

	public void setImpDaAssegnare(BigDecimal impDaAssegnare) {
		this.impDaAssegnare = impDaAssegnare;
	}

	public String getCodiceAvviso() {
		return codiceAvviso;
	}

	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}


	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public void setFlgRimborsato(Integer flgRimborsato) {
		this.flgRimborsato = flgRimborsato;
	}
	

}
