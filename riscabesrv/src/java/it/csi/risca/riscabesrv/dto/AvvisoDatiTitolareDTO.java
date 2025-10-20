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

public class AvvisoDatiTitolareDTO extends GestAttoreDTO {

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("id_soggetto")
	private Long idSoggetto;

	@JsonProperty("id_gruppo_soggetto")
	private Long idGruppoSoggetto;

	@JsonProperty("nome_titolare_ind_post")
	private String nomeTitolareIndPost;

	@JsonProperty("indirizzo_ind_post")
	private String indirizzoIndPost;

	@JsonProperty("presso_ind_post")
	private String pressoIndPost;

	@JsonProperty("comune_ind_post")
	private String comuneIndPost;

	@JsonProperty("cap_ind_post")
	private String capIndPost;

	@JsonProperty("prov_ind_post")
	private String provIndPost;

	@JsonProperty("importo_da_versare")
	private BigDecimal importoDaVersare;

	@JsonProperty("scadenza_pagamento")
	private String scadenzaPagamento;

	@JsonProperty("n_utenze")
	private Integer nUtenze;

	@JsonProperty("annualita_pagamento")
	private Integer annualitaPagamento;

	@JsonProperty("stati_pagamenti")
	private String statiPagamenti;

	@JsonProperty("dilazione")
	private String dilazione;

	@JsonProperty("quinto_campo")
	private String quintoCampo;

	@JsonProperty("codice_fiscale_eti_calc")
	private String codiceFiscaleEtiCalc;

	@JsonProperty("codice_fiscale_calc")
	private String codiceFiscaleCalc;

	@JsonProperty("n_avviso_calc")
	private String nAvvisoCalc;

	@JsonProperty("frase_stato_pag_calc")
	private String fraseStatoPagCalc;

	@JsonProperty("codice_utenza_calc")
	private String codiceUtenzaCalc;

	@JsonProperty("scadenza_pagamento2")
	private String scadenzaPagamento2;

	@JsonProperty("vuoto3")
	private String vuoto3;

	@JsonProperty("modalita_invio")
	private String modalitaInvio;

	@JsonProperty("pec_email")
	private String pecEmail;

	@JsonProperty("numero_protocollo_sped")
	private String numeroProtocolloSped;

	@JsonProperty("data_protocollo_sped")
	private Date dataProtocolloSped;

	@JsonProperty("id_titolare")
	private String idTitolare;

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public String getNomeTitolareIndPost() {
		return nomeTitolareIndPost;
	}

	public void setNomeTitolareIndPost(String nomeTitolareIndPost) {
		this.nomeTitolareIndPost = nomeTitolareIndPost;
	}

	public String getIndirizzoIndPost() {
		return indirizzoIndPost;
	}

	public void setIndirizzoIndPost(String indirizzoIndPost) {
		this.indirizzoIndPost = indirizzoIndPost;
	}

	public String getPressoIndPost() {
		return pressoIndPost;
	}

	public void setPressoIndPost(String pressoIndPost) {
		this.pressoIndPost = pressoIndPost;
	}

	public String getComuneIndPost() {
		return comuneIndPost;
	}

	public void setComuneIndPost(String comuneIndPost) {
		this.comuneIndPost = comuneIndPost;
	}

	public String getCapIndPost() {
		return capIndPost;
	}

	public void setCapIndPost(String capIndPost) {
		this.capIndPost = capIndPost;
	}

	public String getProvIndPost() {
		return provIndPost;
	}

	public void setProvIndPost(String provIndPost) {
		this.provIndPost = provIndPost;
	}

	public BigDecimal getImportoDaVersare() {
		return importoDaVersare;
	}

	public void setImportoDaVersare(BigDecimal importoDaVersare) {
		this.importoDaVersare = importoDaVersare;
	}

	public String getScadenzaPagamento() {
		return scadenzaPagamento;
	}

	public void setScadenzaPagamento(String scadenzaPagamento) {
		this.scadenzaPagamento = scadenzaPagamento;
	}

	public Integer getnUtenze() {
		return nUtenze;
	}

	public void setnUtenze(Integer nUtenze) {
		this.nUtenze = nUtenze;
	}

	public Integer getAnnualitaPagamento() {
		return annualitaPagamento;
	}

	public void setAnnualitaPagamento(Integer annualitaPagamento) {
		this.annualitaPagamento = annualitaPagamento;
	}

	public String getStatiPagamenti() {
		return statiPagamenti;
	}

	public void setStatiPagamenti(String statiPagamenti) {
		this.statiPagamenti = statiPagamenti;
	}

	public String getDilazione() {
		return dilazione;
	}

	public void setDilazione(String dilazione) {
		this.dilazione = dilazione;
	}

	public String getQuintoCampo() {
		return quintoCampo;
	}

	public void setQuintoCampo(String quintoCampo) {
		this.quintoCampo = quintoCampo;
	}

	public String getCodiceFiscaleEtiCalc() {
		return codiceFiscaleEtiCalc;
	}

	public void setCodiceFiscaleEtiCalc(String codiceFiscaleEtiCalc) {
		this.codiceFiscaleEtiCalc = codiceFiscaleEtiCalc;
	}

	public String getCodiceFiscaleCalc() {
		return codiceFiscaleCalc;
	}

	public void setCodiceFiscaleCalc(String codiceFiscaleCalc) {
		this.codiceFiscaleCalc = codiceFiscaleCalc;
	}

	public String getnAvvisoCalc() {
		return nAvvisoCalc;
	}

	public void setnAvvisoCalc(String nAvvisoCalc) {
		this.nAvvisoCalc = nAvvisoCalc;
	}

	public String getFraseStatoPagCalc() {
		return fraseStatoPagCalc;
	}

	public void setFraseStatoPagCalc(String fraseStatoPagCalc) {
		this.fraseStatoPagCalc = fraseStatoPagCalc;
	}

	public String getCodiceUtenzaCalc() {
		return codiceUtenzaCalc;
	}

	public void setCodiceUtenzaCalc(String codiceUtenzaCalc) {
		this.codiceUtenzaCalc = codiceUtenzaCalc;
	}

	public String getScadenzaPagamento2() {
		return scadenzaPagamento2;
	}

	public void setScadenzaPagamento2(String scadenzaPagamento2) {
		this.scadenzaPagamento2 = scadenzaPagamento2;
	}

	public String getVuoto3() {
		return vuoto3;
	}

	public void setVuoto3(String vuoto3) {
		this.vuoto3 = vuoto3;
	}

	public String getModalitaInvio() {
		return modalitaInvio;
	}

	public void setModalitaInvio(String modalitaInvio) {
		this.modalitaInvio = modalitaInvio;
	}

	public String getPecEmail() {
		return pecEmail;
	}

	public void setPecEmail(String pecEmail) {
		this.pecEmail = pecEmail;
	}

	public String getNumeroProtocolloSped() {
		return numeroProtocolloSped;
	}

	public void setNumeroProtocolloSped(String numeroProtocolloSped) {
		this.numeroProtocolloSped = numeroProtocolloSped;
	}

	public Date getDataProtocolloSped() {
		return dataProtocolloSped;
	}

	public void setDataProtocolloSped(Date dataProtocolloSped) {
		this.dataProtocolloSped = dataProtocolloSped;
	}


	public String getIdTitolare() {
		return idTitolare;
	}

	public void setIdTitolare(String idTitolare) {
		this.idTitolare = idTitolare;
	}

	@Override
	public String toString() {
		return "AvvisoDatiTitolareDTO [nap=" + nap + ", idSoggetto=" + idSoggetto + ", idGruppoSoggetto="
				+ idGruppoSoggetto + ", nomeTitolareIndPost=" + nomeTitolareIndPost + ", indirizzoIndPost="
				+ indirizzoIndPost + ", pressoIndPost=" + pressoIndPost + ", comuneIndPost=" + comuneIndPost
				+ ", capIndPost=" + capIndPost + ", provIndPost=" + provIndPost + ", importoDaVersare="
				+ importoDaVersare + ", scadenzaPagamento=" + scadenzaPagamento + ", nUtenze=" + nUtenze
				+ ", annualitaPagamento=" + annualitaPagamento + ", statiPagamenti=" + statiPagamenti + ", dilazione="
				+ dilazione + ", quintoCampo=" + quintoCampo + ", codiceFiscaleEtiCalc=" + codiceFiscaleEtiCalc
				+ ", codiceFiscaleCalc=" + codiceFiscaleCalc + ", nAvvisoCalc=" + nAvvisoCalc + ", fraseStatoPagCalc="
				+ fraseStatoPagCalc + ", codiceUtenzaCalc=" + codiceUtenzaCalc + ", scadenzaPagamento2="
				+ scadenzaPagamento2 + ", vuoto3=" + vuoto3 + ", modalitaInvio=" + modalitaInvio + ", pecEmail="
				+ pecEmail + ", numeroProtocolloSped=" + numeroProtocolloSped + ", dataProtocolloSped="
				+ dataProtocolloSped + ", idTitolare=" + idTitolare + "]";
	}


}
