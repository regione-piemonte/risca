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

public class RuoloDTO extends GestAttoreDTO {


	@JsonProperty("id_ruolo")
	private Long idRuolo;
  	
  	@JsonProperty("id_accertamento")
	private Long idAccertamento;
  	
	@JsonProperty("data_creazione_ruolo")
	private Date dataCreazioneRuolo;

	@JsonProperty("num_bil_acc_canone")
	private String numBilAccCanone;
	
	@JsonProperty("num_bil_acc_interessi")
	private String numBilAccInteressi;
	
	@JsonProperty("num_bil_acc_spese")
	private String numBilAccSpese;
	
	@JsonProperty("importo_canone_mancante")
	private BigDecimal importoCanoneMancante;
	
	@JsonProperty("importo_interessi_mancanti")
	private BigDecimal importoInteressiMancanti;
	
	@JsonProperty("importo_spese_mancanti")
	private BigDecimal importoSpeseMancanti;
	
	@JsonProperty("codice_ente_creditore")
	private String codiceEnteCreditore;
	
	@JsonProperty("tipo_ufficio")
	private String tipoUfficio;
	
	@JsonProperty("codice_ufficio")
	private String codiceUfficio;
	
	@JsonProperty("anno_scadenza")
	private Integer annoScadenza;
	
	@JsonProperty("identif_tipologia_atto")
	private Integer identifTipologiaAtto;
	
	@JsonProperty("numero_partita")
	private Integer numeroPartita;
	
	@JsonProperty("progressivo_partita")
	private Integer progressivoPartita;
	
	@JsonProperty("codice_tipo_atto")
	private String codiceTipoAtto;
	
	@JsonProperty("motivazione_iscrizione")
	private String motivazioneIscrizione;
	
	@JsonProperty("p_ident_prenot_ruolo")
	private String pIdentPrenotRuolo;
	
	@JsonProperty("note")
	private String note;
	


	public Long getIdRuolo() {
		return idRuolo;
	}


	public void setIdRuolo(Long idRuolo) {
		this.idRuolo = idRuolo;
	}


	public Long getIdAccertamento() {
		return idAccertamento;
	}


	public void setIdAccertamento(Long idAccertamento) {
		this.idAccertamento = idAccertamento;
	}


	public Date getDataCreazioneRuolo() {
		return dataCreazioneRuolo;
	}


	public void setDataCreazioneRuolo(Date dataCreazioneRuolo) {
		this.dataCreazioneRuolo = dataCreazioneRuolo;
	}


	public String getNumBilAccCanone() {
		return numBilAccCanone;
	}


	public void setNumBilAccCanone(String numBilAccCanone) {
		this.numBilAccCanone = numBilAccCanone;
	}


	public String getNumBilAccInteressi() {
		return numBilAccInteressi;
	}


	public void setNumBilAccInteressi(String numBilAccInteressi) {
		this.numBilAccInteressi = numBilAccInteressi;
	}


	public String getNumBilAccSpese() {
		return numBilAccSpese;
	}


	public void setNumBilAccSpese(String numBilAccSpese) {
		this.numBilAccSpese = numBilAccSpese;
	}


	public BigDecimal getImportoCanoneMancante() {
		return importoCanoneMancante;
	}


	public void setImportoCanoneMancante(BigDecimal importoCanoneMancante) {
		this.importoCanoneMancante = importoCanoneMancante;
	}


	public BigDecimal getImportoInteressiMancanti() {
		return importoInteressiMancanti;
	}


	public void setImportoInteressiMancanti(BigDecimal importoInteressiMancanti) {
		this.importoInteressiMancanti = importoInteressiMancanti;
	}


	public BigDecimal getImportoSpeseMancanti() {
		return importoSpeseMancanti;
	}


	public void setImportoSpeseMancanti(BigDecimal importoSpeseMancanti) {
		this.importoSpeseMancanti = importoSpeseMancanti;
	}


	public String getCodiceEnteCreditore() {
		return codiceEnteCreditore;
	}


	public void setCodiceEnteCreditore(String codiceEnteCreditore) {
		this.codiceEnteCreditore = codiceEnteCreditore;
	}


	public String getTipoUfficio() {
		return tipoUfficio;
	}


	public void setTipoUfficio(String tipoUfficio) {
		this.tipoUfficio = tipoUfficio;
	}


	public String getCodiceUfficio() {
		return codiceUfficio;
	}


	public void setCodiceUfficio(String codiceUfficio) {
		this.codiceUfficio = codiceUfficio;
	}


	public Integer getAnnoScadenza() {
		return annoScadenza;
	}


	public void setAnnoScadenza(Integer annoScadenza) {
		this.annoScadenza = annoScadenza;
	}


	public Integer getIdentifTipologiaAtto() {
		return identifTipologiaAtto;
	}


	public void setIdentifTipologiaAtto(Integer identifTipologiaAtto) {
		this.identifTipologiaAtto = identifTipologiaAtto;
	}


	public Integer getNumeroPartita() {
		return numeroPartita;
	}


	public void setNumeroPartita(Integer numeroPartita) {
		this.numeroPartita = numeroPartita;
	}


	public Integer getProgressivoPartita() {
		return progressivoPartita;
	}


	public void setProgressivoPartita(Integer progressivoPartita) {
		this.progressivoPartita = progressivoPartita;
	}


	public String getCodiceTipoAtto() {
		return codiceTipoAtto;
	}


	public void setCodiceTipoAtto(String codiceTipoAtto) {
		this.codiceTipoAtto = codiceTipoAtto;
	}


	public String getMotivazioneIscrizione() {
		return motivazioneIscrizione;
	}


	public void setMotivazioneIscrizione(String motivazioneIscrizione) {
		this.motivazioneIscrizione = motivazioneIscrizione;
	}


	public String getpIdentPrenotRuolo() {
		return pIdentPrenotRuolo;
	}


	public void setpIdentPrenotRuolo(String pIdentPrenotRuolo) {
		this.pIdentPrenotRuolo = pIdentPrenotRuolo;
	}
	
	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	@Override
	public String toString() {
		return "RuoloDTO [idRuolo=" + idRuolo + ", idAccertamento=" + idAccertamento + ", dataCreazioneRuolo="
				+ dataCreazioneRuolo + ", numBilAccCanone=" + numBilAccCanone + ", numBilAccInteressi=" + numBilAccInteressi
				+ ", numBilAccSpese=" + numBilAccSpese + ", importoCanoneMancante=" + importoCanoneMancante + ", importoInteressiMancanti=" + importoInteressiMancanti
				+ ", importoSpeseMancanti=" + importoSpeseMancanti + ", codiceEnteCreditore=" + codiceEnteCreditore + ", getGestAttoreIns()="
				+ ", tipoUfficio="+tipoUfficio+", codiceUfficio="+codiceUfficio+", annoScadenza="+annoScadenza+", identifTipologiaAtto="+identifTipologiaAtto
				+ ", numeroPartita="+numeroPartita+", progressivoPartita="+progressivoPartita+", codiceTipoAtto="+codiceTipoAtto+", motivazioneIscrizione="+motivazioneIscrizione
				+ ", pIdentPrenotRuolo="+pIdentPrenotRuolo+", note="+note
				+ getGestAttoreIns() + ", getGestDataIns()=" + getGestDataIns() + ", getGestAttoreUpd()="
				+ getGestAttoreUpd() + ", getGestDataUpd()=" + getGestDataUpd() + ", getGestUid()=" + getGestUid()
				+ "]";
	}

}
