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

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatoDebitorioDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_stato_debitorio deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_stato_debitorio supera il limite massimo consentito per Integer")
	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

    @Min(value = 1, message = "L' id_riscossione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_riscossione supera il limite massimo consentito per Integer")
	@JsonProperty("id_riscossione")
	private Long idRiscossione;

    @Size(max = 100, min = 0, message = "des_tipo_titolo deve essere compreso tra 0 e 100 caratteri.  ")
	@JsonProperty("des_tipo_titolo")
	private String desTipoTitolo;

    @Min(value = 1, message = "L' id_soggetto deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_soggetto")
	private Long idSoggetto;

    @Min(value = 1, message = "L' id_gruppo_soggetto deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_gruppo_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_gruppo_soggetto")
	private Long idGruppoSoggetto;
	
    @Min(value = 1, message = "L' id_recapito_stato_debitorio deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_recapito_stato_debitorio supera il limite massimo consentito per Integer")
	@JsonProperty("id_recapito_stato_debitorio")
	private Long idRecapito;
	
	@JsonIgnore
	private Long idAttivitaStatoDeb;
	
	@JsonIgnore
	private Long idStatoContribuzione;
	
    @Min(value = 1, message = "L' id_avviso_pagamento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_avviso_pagamento supera il limite massimo consentito per Integer")
	@JsonProperty("id_avviso_pagamento")
	private Long idAvvisoPagamento;
	
    @Min(value = 1, message = "L' id_tipo_dilazione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_dilazione supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_dilazione")
	private Long idTipoDilazione;

    @Size(max = 20, min = 0, message = "nap deve essere compreso tra 0 e 20 caratteri.  ")
	@JsonProperty("nap")
	private String nap;

    @Size(max = 100, min = 0, message = "num_titolo deve essere compreso tra 0 e 100 caratteri.  ")
	@JsonProperty("num_titolo") 
	private String numTitolo;

	@JsonProperty("data_provvedimento")
	private Date dataProvvedimento;

    @Size(max = 15, min = 0, message = "num_richiesta_protocollo deve essere compreso tra 0 e 15 caratteri.  ")
	@JsonProperty("num_richiesta_protocollo")
	private String numRichiestaProtocollo;

	@JsonProperty("data_richiesta_protocollo")
	private Date dataRichiestaProtocollo;

	@JsonProperty("data_ultima_modifica")
	private Date dataUltimaModifica;

    @Size(max = 250, min = 0, message = "des_usi deve essere compreso tra 0 e 250 caratteri.  ")
	@JsonProperty("des_usi")
	private String desUsi;

    @Size(max = 500, min = 0, message = "note deve essere compreso tra 0 e 500 caratteri.  ")
	@JsonProperty("note")
	private String note;

    @Digits(integer = 9, fraction = 2, message = "I'imp_recupero_canone deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("imp_recupero_canone")
	private BigDecimal impRecuperoCanone;

    @Digits(integer =9, fraction = 2, message = "I'imp_recupero_interessi deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("imp_recupero_interessi")
	private BigDecimal impRecuperoInteressi;

    @Digits(integer = 9, fraction = 2, message = "I'imp_spese_notifica deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("imp_spese_notifica")
	private BigDecimal impSpeseNotifica;

    @Digits(integer = 9, fraction = 2, message = "I'imp_compensazione_canone deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("imp_compensazione_canone")
	private BigDecimal impCompensazioneCanone;

    @Size(max = 30, min = 0, message = "desc_periodo_pagamento deve essere compreso tra 0 e 30 caratteri.  ")
	@JsonProperty("desc_periodo_pagamento")
	private String descPeriodoPagamento;

    @Size(max = 200, min = 0, message = "desc_motivo_annullo deve essere compreso tra 0 e 200 caratteri.  ")
	@JsonProperty("desc_motivo_annullo")
	private String descMotivoAnnullo;

    @Min(value = 0, message = "Il flg_annullato deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_annullato deve essere al massimo 1.")
	@JsonProperty("flg_annullato")
	private int flgAnnullato;

    @Min(value = 0, message = "Il flg_restituito_mittente deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_restituito_mittente deve essere al massimo 1.")
	@JsonProperty("flg_restituito_mittente")
	private int flgRestituitoMittente;

    @Min(value = 0, message = "Il flg_invio_speciale deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_invio_speciale deve essere al massimo 1.")
	@JsonProperty("flg_invio_speciale")
	private int flgInvioSpeciale;

    @Min(value = 0, message = "Il flg_dilazione deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_dilazione deve essere al massimo 1.")
	@JsonProperty("flg_dilazione")
	private int flgDilazione;

    @Min(value = 0, message = "Il flg_addebito_anno_successivo deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_addebito_anno_successivo deve essere al massimo 1.")
	@JsonProperty("flg_addebito_anno_successivo")
	private int flgAddebitoAnnoSuccessivo;
	
    @Size(max = 100, min = 0, message = "tipo_titolo deve essere compreso tra 0 e 100 caratteri.  ")
	@JsonProperty("tipo_titolo")
	private String tipoTitolo;
	
    @Min(value = 1, message = " num_annualita deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "num_annualita supera il limite massimo consentito per Integer")
	@JsonProperty("num_annualita")
	private Long numAnnualita;
	
	@JsonProperty("anno")
	private int anno;

    @Digits(integer = 13, fraction = 2, message = "canone_dovuto deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("canone_dovuto")
	private BigDecimal canoneDovuto;

    @Digits(integer = 13, fraction = 2, message = "I'importo_dovuto deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("importo_dovuto")
	private BigDecimal importoDovuto;

    @Digits(integer = 13, fraction = 2, message = "I'importo_versato deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("importo_versato")
	private BigDecimal importoVersato;


    @Digits(integer = 13, fraction = 2, message = "I'imp_mancante_imp_eccedente deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("imp_mancante_imp_eccedente")
	private BigDecimal impMancanteImpEccedente;

    @Digits(integer = 13, fraction = 2, message = "I'importo_eccedente deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("importo_eccedente")
	private BigDecimal importoEccedente;
	
    @Size(max = 100, min = 0, message = "nota_rinnovo deve essere compreso tra 0 e 100 caratteri.  ")
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
