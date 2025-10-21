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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatoDebitorioDTO {

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("id_riscossione")
	private Long idRiscossione;

	@JsonProperty("des_tipo_titolo")
	private String desTipoTitolo;

	@JsonProperty("id_soggetto")
	private Long idSoggetto;

	@JsonProperty("id_gruppo_soggetto")
	private Long idGruppoSoggetto;
	
	@JsonProperty("id_recapito_stato_debitorio")
	private Long idRecapito;
	
	@JsonIgnore
	private Long idAttivitaStatoDeb;
	
	@JsonIgnore
	private Long idStatoContribuzione;
	
	@JsonProperty("id_avviso_pagamento")
	private Long idAvvisoPagamento;
	
	@JsonProperty("id_tipo_dilazione")
	private Long idTipoDilazione;

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("num_titolo") 
	private String numTitolo;

	@JsonProperty("data_provvedimento")
	private Date dataProvvedimento;

	@JsonProperty("num_richiesta_protocollo")
	private String numRichiestaProtocollo;

	@JsonProperty("data_richiesta_protocollo")
	private Date dataRichiestaProtocollo;

	@JsonProperty("data_ultima_modifica")
	private Date dataUltimaModifica;

	@JsonProperty("des_usi")
	private String desUsi;

	@JsonProperty("note")
	private String note;

	@JsonProperty("imp_recupero_canone")
	private BigDecimal impRecuperoCanone;

	@JsonProperty("imp_recupero_interessi")
	private BigDecimal impRecuperoInteressi;

	@JsonProperty("imp_spese_notifica")
	private BigDecimal impSpeseNotifica;

	@JsonProperty("imp_compensazione_canone")
	private BigDecimal impCompensazioneCanone;

	@JsonProperty("desc_periodo_pagamento")
	private String descPeriodoPagamento;

	@JsonProperty("desc_motivo_annullo")
	private String descMotivoAnnullo;

	@JsonProperty("flg_annullato")
	private int flgAnnullato;

	@JsonProperty("flg_restituito_mittente")
	private int flgRestituitoMittente;

	@JsonProperty("flg_invio_speciale")
	private int flgInvioSpeciale;

	@JsonProperty("flg_dilazione")
	private int flgDilazione;

	@JsonProperty("flg_addebito_anno_successivo")
	private int flgAddebitoAnnoSuccessivo;

	@JsonProperty("tipo_titolo")
	private String tipoTitolo;
	
	@JsonProperty("num_annualita")
	private Long numAnnualita;
	
	@JsonProperty("anno")
	private int anno;
	
	@JsonProperty("canone_dovuto")
	private BigDecimal canoneDovuto;
	
	@JsonProperty("importo_dovuto")
	private BigDecimal importoDovuto;
	
	@JsonProperty("importo_versato")
	private BigDecimal importoVersato;


	@JsonProperty("imp_mancante_imp_eccedente")
	private BigDecimal impMancanteImpEccedente;
	
	@JsonProperty("importo_eccedente")
	private BigDecimal importoEccedente;

	@JsonProperty("nota_rinnovo")
	private String notaRinnovo;


	public String getTipoTitolo() {
		return tipoTitolo;
	}

	public void setTipoTitolo(String tipoTitolo) {
		this.tipoTitolo = tipoTitolo;
	}
	

	public BigDecimal getCanoneDovuto() {
		return canoneDovuto;
	}

	public void setCanoneDovuto(BigDecimal canoneDovuto) {
		this.canoneDovuto = canoneDovuto;
	}

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public String getDesTipoTitolo() {
		return desTipoTitolo;
	}

	public void setDesTipoTitolo(String desTipoTitolo) {
		this.desTipoTitolo = desTipoTitolo;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	public Long getIdAvvisoPagamento() {
		return idAvvisoPagamento;
	}

	public void setIdAvvisoPagamento(Long idAvvisoPagamento) {
		this.idAvvisoPagamento = idAvvisoPagamento;
	}

	public Long getIdTipoDilazione() {
		return idTipoDilazione;
	}

	public void setIdTipoDilazione(Long idTipoDilazione) {
		this.idTipoDilazione = idTipoDilazione;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public String getNumTitolo() {
		return numTitolo;
	}

	public void setNumTitolo(String numTitolo) {
		this.numTitolo = numTitolo;
	}

	public Date getDataProvvedimento() {
		return dataProvvedimento;
	}

	public void setDataProvvedimento(Date dataProvvedimento) {
		this.dataProvvedimento = dataProvvedimento;
	}

	public String getNumRichiestaProtocollo() {
		return numRichiestaProtocollo;
	}

	public void setNumRichiestaProtocollo(String numRichiestaProtocollo) {
		this.numRichiestaProtocollo = numRichiestaProtocollo;
	}

	public Date getDataRichiestaProtocollo() {
		return dataRichiestaProtocollo;
	}

	public void setDataRichiestaProtocollo(Date dataRichiestaProtocollo) {
		this.dataRichiestaProtocollo = dataRichiestaProtocollo;
	}

	public Date getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(Date dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	public String getDesUsi() {
		return desUsi;
	}

	public void setDesUsi(String desUsi) {
		this.desUsi = desUsi;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getImpRecuperoCanone() {
		return impRecuperoCanone;
	}

	public void setImpRecuperoCanone(BigDecimal impRecuperoCanone) {
		this.impRecuperoCanone = impRecuperoCanone;
	}

	public BigDecimal getImpRecuperoInteressi() {
		return impRecuperoInteressi;
	}

	public void setImpRecuperoInteressi(BigDecimal impRecuperoInteressi) {
		this.impRecuperoInteressi = impRecuperoInteressi;
	}

	public BigDecimal getImpSpeseNotifica() {
		return impSpeseNotifica;
	}

	public void setImpSpeseNotifica(BigDecimal impSpeseNotifica) {
		this.impSpeseNotifica = impSpeseNotifica;
	}

	public BigDecimal getImpCompensazioneCanone() {
		return impCompensazioneCanone;
	}

	public void setImpCompensazioneCanone(BigDecimal impCompensazioneCanone) {
		this.impCompensazioneCanone = impCompensazioneCanone;
	}

	public String getDescPeriodoPagamento() {
		return descPeriodoPagamento;
	}

	public void setDescPeriodoPagamento(String descPeriodoPagamento) {
		this.descPeriodoPagamento = descPeriodoPagamento;
	}

	public String getDescMotivoAnnullo() {
		return descMotivoAnnullo;
	}

	public void setDescMotivoAnnullo(String descMotivoAnnullo) {
		this.descMotivoAnnullo = descMotivoAnnullo;
	}

	public int getFlgAnnullato() {
		return flgAnnullato;
	}

	public void setFlgAnnullato(int flgAnnullato) {
		this.flgAnnullato = flgAnnullato;
	}

	public int getFlgRestituitoMittente() {
		return flgRestituitoMittente;
	}

	public void setFlgRestituitoMittente(int flgRestituitoMittente) {
		this.flgRestituitoMittente = flgRestituitoMittente;
	}

	public int getFlgInvioSpeciale() {
		return flgInvioSpeciale;
	}

	public void setFlgInvioSpeciale(int flgInvioSpeciale) {
		this.flgInvioSpeciale = flgInvioSpeciale;
	}

	public int getFlgDilazione() {
		return flgDilazione;
	}

	public void setFlgDilazione(int flgDilazione) {
		this.flgDilazione = flgDilazione;
	}

	public int getFlgAddebitoAnnoSuccessivo() {
		return flgAddebitoAnnoSuccessivo;
	}

	public void setFlgAddebitoAnnoSuccessivo(int flgAddebitoAnnoSuccessivo) {
		this.flgAddebitoAnnoSuccessivo = flgAddebitoAnnoSuccessivo;
	}

	public Long getNumAnnualita() {
		return numAnnualita;
	}

	public void setNumAnnualita(Long numAnnualita) {
		this.numAnnualita = numAnnualita;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}


	public BigDecimal getImportoDovuto() {
		return importoDovuto;
	}

	public void setImportoDovuto(BigDecimal importoDovuto) {
		this.importoDovuto = importoDovuto;
	}

	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public Long getIdRecapito() {
		return idRecapito;
	}

	public void setIdRecapito(Long idRecapito) {
		this.idRecapito = idRecapito;
	}

	public Long getIdAttivitaStatoDeb() {
		return idAttivitaStatoDeb;
	}

	public void setIdAttivitaStatoDeb(Long idAttivitaStatoDeb) {
		this.idAttivitaStatoDeb = idAttivitaStatoDeb;
	}

	public Long getIdStatoContribuzione() {
		return idStatoContribuzione;
	}

	public void setIdStatoContribuzione(Long idStatoContribuzione) {
		this.idStatoContribuzione = idStatoContribuzione;
	}

	public BigDecimal getImpMancanteImpEccedente() {
		return impMancanteImpEccedente;
	}

	public void setImpMancanteImpEccedente(BigDecimal impMancanteImpEccedente) {
		this.impMancanteImpEccedente = impMancanteImpEccedente;
	}

	public BigDecimal getImportoEccedente() {
		return importoEccedente;
	}

	public void setImportoEccedente(BigDecimal importoEccedente) {
		this.importoEccedente = importoEccedente;
	}

	public String getNotaRinnovo() {
		return notaRinnovo;
	}

	public void setNotaRinnovo(String notaRinnovo) {
		this.notaRinnovo = notaRinnovo;
	}

	
	
	
}
