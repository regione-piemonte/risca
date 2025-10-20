/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto.soris;


import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.dto.GestAttoreDTO;

/**
 * The type RuoloSorisFr3 dto.
 *
 * @author CSI PIEMONTE
 */
public class RuoloSorisFr3DTO extends GestAttoreDTO {


	@Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;


	@Min(value = 1, message = "L' id_ruolo_soris_fr3 deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_ruolo_soris_fr3 supera il limite massimo consentito per Integer")
	@JsonProperty("id_ruolo_soris_fr3")
	private Long idRuoloSorisFr3;

	@JsonProperty("id_ruolo")
	private Long idRuolo;

	@Min(value = 1, message = "L' id_file_soris deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_file_soris supera il limite massimo consentito per Integer")
	@JsonProperty("id_file_soris")
	private Long idFileSoris;

	@Min(value = 1, message = "L' id_pagamento deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_pagamento supera il limite massimo consentito per Integer")
	@JsonProperty("id_pagamento")
	private Long idPagamento;
	
	@Size(max = 3, min = 0, message = "tipo_record deve essere compreso tra 0 e 3 caratteri. ")
	@JsonProperty("tipo_record")
	private String tipoRecord;

	@JsonProperty("utenza")
	private String utenza;


	@JsonProperty("progr_record")
	private Integer progrRecord;
	
	@Size(max = 3, min = 0, message = "cod_ambito deve essere compreso tra 0 e 3 caratteri. ")
	@JsonProperty("cod_ambito")
	private String codAmbito;
	
	@Size(max = 5, min = 0, message = "cod_ente_creditore deve essere compreso tra 0 e 5 caratteri. ")
	@JsonProperty("cod_ente_creditore")
	private String codEnteCreditore;
	
	@Size(max = 4, min = 0, message = "anno_ruolo deve essere compreso tra 0 e 4 caratteri. ")
	@JsonProperty("anno_ruolo")
	private String annoRuolo;

	@Size(max = 6, min = 0, message = "numero_ruolo deve essere compreso tra 0 e 6 caratteri. ")
	@JsonProperty("numero_ruolo")
	private String numeroRuolo;


	@Size(max = 1, min = 0, message = "p_tipo_ufficio deve essere compreso tra 0 e 1 caratteri. ")
	@JsonProperty("p_tipo_ufficio")
	private String pTipoUfficio;

	@Size(max = 6, min = 0, message = "p_codice_ufficio deve essere compreso tra 0 e 6 caratteri. ")
	@JsonProperty("p_codice_ufficio")
	private String pCodiceUfficio;
	
	@Size(max = 4, min = 0, message = "p_anno_riferimento deve essere compreso tra 0 e 4 caratteri. ")
	@JsonProperty("p_anno_riferimento")
	private String pAnnoRiferimento;

	@Size(max = 3, min = 0, message = "p_tipo_modello deve essere compreso tra 0 e 3 caratteri. ")
	@JsonProperty("p_tipo_modello")
	private String pTipoModello;

	@Size(max = 34, min = 0, message = "p_ident_prenot_ruolo deve essere compreso tra 0 e 34 caratteri. ")
	@JsonProperty("p_ident_prenot_ruolo")
	private String pIdentPrenotRuolo;

	@Size(max = 48, min = 0, message = "p_ident_atto deve essere compreso tra 0 e 48 caratteri. ")
	@JsonProperty("p_ident_atto")
	private String pIdentAtto;
	
	@JsonProperty("progr_articolo_ruolo")
	private Integer progrArticoloRuolo;

	@Size(max = 20, min = 0, message = "identif_cartella deve essere compreso tra 0 e 20 caratteri. ")
	@JsonProperty("identif_cartella")
	private String identifCartella;
	
	
	@JsonProperty("progr_articolo_cartella")
	private Integer progrArticoloCartella;

	@Size(max = 4, min = 0, message = "codice_entrata deve essere compreso tra 0 e 4 caratteri. ")
	@JsonProperty("codice_entrata")
	private String codiceEntrata;

	@Size(max = 1, min = 0, message = "tipo_entrata deve essere compreso tra 0 e 1 caratteri. ")
	@JsonProperty("tipo_entrata")
	private String tipoEntrata;

	@Size(max = 16, min = 0, message = "codice_fiscale deve essere compreso tra 0 e 16 caratteri. ")
	@JsonProperty("codice_fiscale")
	private String codiceFiscale;
	

	@JsonProperty("data_evento")
	private Date dataEvento;
	
	@JsonProperty("importo_carico_risco")
	private BigDecimal importoCaricoRisco;
	
	@JsonProperty("importo_interessi")
    private Integer importoInteressi; 

	@JsonProperty("importo_aggio_ente")
    private Integer importoAggioEnte; 

	@JsonProperty("importo_aggio_contrib")
    private Integer importoAggioContrib; 

	@JsonProperty("t_spese_proc_esec")
    private Integer tSpeseProcEsec; 
	
	@JsonProperty("t_spese_proc_esec_p_lista")
	private Integer tSpeseProcEsecPLista;

	@JsonProperty("codice_divisa")
	private String codiceDivisa;
	
	@JsonProperty("modalita_pagam")
	private String modalitaPagam;
	
	@JsonProperty("data_registr")
	private Date dataRegistr;
	
	@JsonProperty("num_operaz_contabile")
	private String numOperazContabile;
	
	@JsonProperty("progr_inter_op_contab")
	private String progrInterOpContab;
	
	@JsonProperty("note")
	private String note;
	
	@JsonProperty("tot_riscosso")
	private BigDecimal totRiscosso;
	

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("id_rata_sd")
	private Long idRataSd;
	


	public Long getIdRataSd() {
		return idRataSd;
	}




	public void setIdRataSd(Long idRataSd) {
		this.idRataSd = idRataSd;
	}



	
	
	
	public BigDecimal getTotRiscosso() {
		return totRiscosso;
	}




	public void setTotRiscosso(BigDecimal totRiscosso) {
		this.totRiscosso = totRiscosso;
	}




	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}




	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}




	public Long getIdElabora() {
		return idElabora;
	}




	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}




	public Long getIdRuoloSorisFr3() {
		return idRuoloSorisFr3;
	}




	public void setIdRuoloSorisFr3(Long idRuoloSorisFr3) {
		this.idRuoloSorisFr3 = idRuoloSorisFr3;
	}




	public Long getIdRuolo() {
		return idRuolo;
	}




	public void setIdRuolo(Long idRuolo) {
		this.idRuolo = idRuolo;
	}




	public Long getIdFileSoris() {
		return idFileSoris;
	}




	public void setIdFileSoris(Long idFileSoris) {
		this.idFileSoris = idFileSoris;
	}




	public Long getIdPagamento() {
		return idPagamento;
	}




	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}




	public String getTipoRecord() {
		return tipoRecord;
	}




	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}




	public String getUtenza() {
		return utenza;
	}




	public void setUtenza(String utenza) {
		this.utenza = utenza;
	}




	public Integer getProgrRecord() {
		return progrRecord;
	}




	public void setProgrRecord(Integer progrRecord) {
		this.progrRecord = progrRecord;
	}




	public String getCodAmbito() {
		return codAmbito;
	}




	public void setCodAmbito(String codAmbito) {
		this.codAmbito = codAmbito;
	}




	public String getCodEnteCreditore() {
		return codEnteCreditore;
	}




	public void setCodEnteCreditore(String codEnteCreditore) {
		this.codEnteCreditore = codEnteCreditore;
	}




	public String getAnnoRuolo() {
		return annoRuolo;
	}




	public void setAnnoRuolo(String annoRuolo) {
		this.annoRuolo = annoRuolo;
	}




	public String getNumeroRuolo() {
		return numeroRuolo;
	}




	public void setNumeroRuolo(String numeroRuolo) {
		this.numeroRuolo = numeroRuolo;
	}




	public String getpTipoUfficio() {
		return pTipoUfficio;
	}




	public void setpTipoUfficio(String pTipoUfficio) {
		this.pTipoUfficio = pTipoUfficio;
	}




	public String getpCodiceUfficio() {
		return pCodiceUfficio;
	}




	public void setpCodiceUfficio(String pCodiceUfficio) {
		this.pCodiceUfficio = pCodiceUfficio;
	}




	public String getpAnnoRiferimento() {
		return pAnnoRiferimento;
	}




	public void setpAnnoRiferimento(String pAnnoRiferimento) {
		this.pAnnoRiferimento = pAnnoRiferimento;
	}




	public String getpTipoModello() {
		return pTipoModello;
	}




	public void setpTipoModello(String pTipoModello) {
		this.pTipoModello = pTipoModello;
	}




	public String getpIdentPrenotRuolo() {
		return pIdentPrenotRuolo;
	}




	public void setpIdentPrenotRuolo(String pIdentPrenotRuolo) {
		this.pIdentPrenotRuolo = pIdentPrenotRuolo;
	}




	public String getpIdentAtto() {
		return pIdentAtto;
	}




	public void setpIdentAtto(String pIdentAtto) {
		this.pIdentAtto = pIdentAtto;
	}




	public Integer getProgrArticoloRuolo() {
		return progrArticoloRuolo;
	}




	public void setProgrArticoloRuolo(Integer progrArticoloRuolo) {
		this.progrArticoloRuolo = progrArticoloRuolo;
	}




	public String getIdentifCartella() {
		return identifCartella;
	}




	public void setIdentifCartella(String identifCartella) {
		this.identifCartella = identifCartella;
	}




	public Integer getProgrArticoloCartella() {
		return progrArticoloCartella;
	}




	public void setProgrArticoloCartella(Integer progrArticoloCartella) {
		this.progrArticoloCartella = progrArticoloCartella;
	}




	public String getCodiceEntrata() {
		return codiceEntrata;
	}




	public void setCodiceEntrata(String codiceEntrata) {
		this.codiceEntrata = codiceEntrata;
	}




	public String getTipoEntrata() {
		return tipoEntrata;
	}




	public void setTipoEntrata(String tipoEntrata) {
		this.tipoEntrata = tipoEntrata;
	}




	public String getCodiceFiscale() {
		return codiceFiscale;
	}




	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}




	public Date getDataEvento() {
		return dataEvento;
	}




	public void setDataEvento(Date dataEvento) {
		this.dataEvento = dataEvento;
	}




	public BigDecimal getImportoCaricoRisco() {
		return importoCaricoRisco;
	}




	public void setImportoCaricoRisco(BigDecimal importoCaricoRisco) {
		this.importoCaricoRisco = importoCaricoRisco;
	}




	public Integer getImportoInteressi() {
		return importoInteressi;
	}




	public void setImportoInteressi(Integer importoInteressi) {
		this.importoInteressi = importoInteressi;
	}




	public Integer getImportoAggioEnte() {
		return importoAggioEnte;
	}




	public void setImportoAggioEnte(Integer importoAggioEnte) {
		this.importoAggioEnte = importoAggioEnte;
	}




	public Integer getImportoAggioContrib() {
		return importoAggioContrib;
	}




	public void setImportoAggioContrib(Integer importoAggioContrib) {
		this.importoAggioContrib = importoAggioContrib;
	}




	public Integer gettSpeseProcEsec() {
		return tSpeseProcEsec;
	}




	public void settSpeseProcEsec(Integer tSpeseProcEsec) {
		this.tSpeseProcEsec = tSpeseProcEsec;
	}




	public Integer gettSpeseProcEsecPLista() {
		return tSpeseProcEsecPLista;
	}




	public void settSpeseProcEsecPLista(Integer tSpeseProcEsecPLista) {
		this.tSpeseProcEsecPLista = tSpeseProcEsecPLista;
	}




	public String getCodiceDivisa() {
		return codiceDivisa;
	}




	public void setCodiceDivisa(String codiceDivisa) {
		this.codiceDivisa = codiceDivisa;
	}




	public String getModalitaPagam() {
		return modalitaPagam;
	}




	public void setModalitaPagam(String modalitaPagam) {
		this.modalitaPagam = modalitaPagam;
	}




	public Date getDataRegistr() {
		return dataRegistr;
	}




	public void setDataRegistr(Date dataRegistr) {
		this.dataRegistr = dataRegistr;
	}




	public String getNumOperazContabile() {
		return numOperazContabile;
	}




	public String getProgrInterOpContab() {
		return progrInterOpContab;
	}




	public void setProgrInterOpContab(String progrInterOpContab) {
		this.progrInterOpContab = progrInterOpContab;
	}




	public void setNumOperazContabile(String numOperazContabile) {
		this.numOperazContabile = numOperazContabile;
	}




	public String getNote() {
		return note;
	}




	public void setNote(String note) {
		this.note = note;
	}




	@Override
	public String toString() {
		return "RuoloSorisFr3DTO [idRuoloSorisFr3=" + idRuoloSorisFr3 + ", idRuolo=" + idRuolo + ", idFileSoris="
				+ idFileSoris + ", idPagamento=" + idPagamento + ", tipoRecord=" + tipoRecord 
				+ ", utenza=" + utenza +  ", progrRecord=" + progrRecord  +  ", progrRecord=" + progrRecord 
				+  ", codAmbito=" + codAmbito + ", codEnteCreditore=" + codEnteCreditore 
				+ ", annoRuolo=" + annoRuolo + ", numeroRuolo=" + numeroRuolo 
				+ ", pTipoUfficio=" + pTipoUfficio + ", pCodiceUfficio=" + pCodiceUfficio 
				+  ", pAnnoRiferimento=" + pAnnoRiferimento +  ", pTipoModello=" + pTipoModello 
				+  ", pIdentPrenotRuolo=" + pIdentPrenotRuolo + ", pIdentAtto=" + pIdentAtto 
				+  ", progrArticoloRuolo=" + progrArticoloRuolo + ", identifCartella=" + identifCartella 
				+  ", progrArticoloCartella=" + progrArticoloCartella + ", codiceEntrata=" + codiceEntrata 
				+  ", tipoEntrata=" + tipoEntrata + ", codiceFiscale=" + codiceFiscale 
				+  ", dataEvento=" + dataEvento + ", importoCaricoRisco=" + importoCaricoRisco 
				+  ", importoInteressi=" + importoInteressi + ", importoAggioEnte=" + importoAggioEnte 
				+  ", importoAggioContrib=" + importoAggioContrib + ", tSpeseProcEsec=" + tSpeseProcEsec 
				+  ", tSpeseProcEsecPLista=" + tSpeseProcEsecPLista + ", codiceDivisa=" + codiceDivisa 
				+  ", modalitaPagam=" + modalitaPagam + ", dataRegistr=" + dataRegistr 
				+  ", numOperazContabile=" + numOperazContabile + ", progrInterOpContab=" + progrInterOpContab 
				+ ", note=" + note+"]";

	}

}