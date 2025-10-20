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

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type SorisFr3 dto.
 *
 * @author CSI PIEMONTE
 */
public class SorisFr3DTO {
	

	
    @Size(max = 3, min = 0, message = "tipo_record deve essere compreso tra 0 e 3 caratteri. ")
	@JsonProperty("tipo_record")
	private String tipoRecord;
    
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
	
	
    @Size(max = 3, min = 0, message = "codice_divisa deve essere compreso tra 0 e 3 caratteri. ")
   	@JsonProperty("codice_divisa")
	private String codiceDivisa;

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
	
	
    @Size(max = 1, min = 0, message = "modalita_pagam deve essere compreso tra 0 e 1 caratteri. ")
   	@JsonProperty("modalita_pagam")
	private String modalitaPagam;
	
    @Size(max = 2, min = 0, message = "filler1 deve essere compreso tra 0 e 2 caratteri. ")
   	@JsonProperty("filler1")
	private String filler1;
	
    @JsonProperty("data_registr")
    private Date dataRegist;
	
   	@JsonProperty("num_operaz_contabile")
	private String numOperazContabile;
	
    @Size(max = 3, min = 0, message = "progr_inter_op_contab deve essere compreso tra 0 e 3 caratteri. ")
   	@JsonProperty("progr_inter_op_contab")
	private String progrInterOpContab;
	
    @Size(max = 3, min = 0, message = "filler2 deve essere compreso tra 0 e 3 caratteri. ")
   	@JsonProperty("filler2")
	private String filler2;
	
    @JsonProperty("data_decorrenza")
    private Date dataDecorrenza;
	
	
    @Size(max = 3, min = 0, message = "numero_riversam deve essere compreso tra 0 e 3 caratteri. ")
   	@JsonProperty("numero_riversam")
	private String numeroRiversam;
    
    @Size(max = 1, min = 0, message = "restituzione_rimb_spese deve essere compreso tra 0 e 1 caratteri. ")
   	@JsonProperty("restituzione_rimb_spese")
	private String restituzioneRimbSpese;
	
  	@JsonProperty("importo_carico_orig")
	private Integer importoCaricoOrig;
  	
    @Size(max = 28, min = 0, message = "identif_provv_magg_rate deve essere compreso tra 0 e 28 caratteri. ")
   	@JsonProperty("identif_provv_magg_rate")
	private String identifProvvMaggRate;
	
  	@JsonProperty("importo_carico_resid_cond")
	private Integer importoCaricoResidCond;
	
    @Size(max = 16, min = 0, message = "filler3 deve essere compreso tra 0 e 16 caratteri. ")
   	@JsonProperty("filler3")
	private String filler3;
	   
	@JsonProperty("utenza")
	private String utenza;
		
	
	
	private Long idRuolo;
	
	public String getUtenza() {
		return utenza;
	}




	public void setUtenza(String utenza) {
		this.utenza = utenza;
	}




	public Long getIdRuolo() {
		return idRuolo;
	}




	public void setIdRuolo(Long idRuolo) {
		this.idRuolo = idRuolo;
	}




	public String getTipoRecord() {
		return tipoRecord;
	}




	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}





	public int getProgrRecord() {
		return progrRecord;
	}





	public void setProgrRecord(int progrRecord) {
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



	public String getCodiceDivisa() {
		return codiceDivisa;
	}





	public void setCodiceDivisa(String codiceDivisa) {
		this.codiceDivisa = codiceDivisa;
	}

	public void setProgrRecord(Integer progrRecord) {
		this.progrRecord = progrRecord;
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





	public String getModalitaPagam() {
		return modalitaPagam;
	}





	public void setModalitaPagam(String modalitaPagam) {
		this.modalitaPagam = modalitaPagam;
	}





	public String getFiller1() {
		return filler1;
	}





	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}





	public Date getDataRegist() {
		return dataRegist;
	}





	public void setDataRegist(Date dataRegist) {
		this.dataRegist = dataRegist;
	}





	public String getNumOperazContabile() {
		return numOperazContabile;
	}





	public void setNumOperazContabile(String numOperazContabile) {
		this.numOperazContabile = numOperazContabile;
	}





	public String getProgrInterOpContab() {
		return progrInterOpContab;
	}





	public void setProgrInterOpContab(String progrInterOpContab) {
		this.progrInterOpContab = progrInterOpContab;
	}





	public String getFiller2() {
		return filler2;
	}





	public void setFiller2(String filler2) {
		this.filler2 = filler2;
	}





	public Date getDataDecorrenza() {
		return dataDecorrenza;
	}





	public void setDataDecorrenza(Date dataDecorrenza) {
		this.dataDecorrenza = dataDecorrenza;
	}





	public String getNumeroRiversam() {
		return numeroRiversam;
	}





	public void setNumeroRiversam(String numeroRiversam) {
		this.numeroRiversam = numeroRiversam;
	}





	public String getRestituzioneRimbSpese() {
		return restituzioneRimbSpese;
	}





	public void setRestituzioneRimbSpese(String restituzioneRimbSpese) {
		this.restituzioneRimbSpese = restituzioneRimbSpese;
	}





	public Integer getImportoCaricoOrig() {
		return importoCaricoOrig;
	}





	public void setImportoCaricoOrig(Integer importoCaricoOrig) {
		this.importoCaricoOrig = importoCaricoOrig;
	}





	public String getIdentifProvvMaggRate() {
		return identifProvvMaggRate;
	}





	public void setIdentifProvvMaggRate(String identifProvvMaggRate) {
		this.identifProvvMaggRate = identifProvvMaggRate;
	}





	public Integer getImportoCaricoResidCond() {
		return importoCaricoResidCond;
	}





	public void setImportoCaricoResidCond(Integer importoCaricoResidCond) {
		this.importoCaricoResidCond = importoCaricoResidCond;
	}





	public String getFiller3() {
		return filler3;
	}





	public void setFiller3(String filler3) {
		this.filler3 = filler3;
	}





	@Override
	public String toString() {
		return "SorisFr1DTO [tipoRecord=" + tipoRecord + ", progrRecord=" + progrRecord + ", codAmbito="
				+ codAmbito + ", cod_ente_creditore=" + codEnteCreditore + ", anno_ruolo=" + annoRuolo
				+  ", numero_ruolo=" + numeroRuolo + ", p_tipo_ufficio=" + pTipoUfficio
				+  ", p_codice_ufficio=" + pCodiceUfficio + ", p_anno_riferimento=" + pAnnoRiferimento
				+  ", p_tipo_modello=" + pTipoModello + ", p_ident_prenot_ruolo=" + pIdentPrenotRuolo
				+  ", p_ident_atto=" + pIdentAtto + ", progr_articolo_ruolo=" + progrArticoloRuolo
				+  ", identif_cartella=" + identifCartella + ", progr_articolo_cartella=" + progrArticoloCartella
				+  ", codice_entrata=" + codiceEntrata + ", tipo_entrata=" + tipoEntrata
				+  ", codice_fiscale=" + codiceFiscale 
				+  ", data_evento=" + dataEvento + ", importo_carico_risco=" + importoCaricoRisco
				+  ", codice_divisa=" + codiceDivisa + ", importo_aggio_ente=" + importoAggioEnte
				+  ", importo_aggio_contrib=" + importoAggioContrib + ", t_spese_proc_esec=" + tSpeseProcEsec
				+  ", t_spese_proc_esec_p_lista=" + tSpeseProcEsecPLista + ", importo_carico_resid_cond=" + importoCaricoResidCond
				+  ", modalita_pagam=" + modalitaPagam + ", filler1=" + filler1
				+  ", data_registr=" + dataRegist + ", num_operaz_contabile=" + numOperazContabile
				+  ", progr_inter_op_contab=" + progrInterOpContab + ", filler2=" + filler2
				+  ", filler3=" + filler3 
				+  ", data_decorrenza=" + dataDecorrenza + ", numero_riversam=" + numeroRiversam
				+  ", restituzione_rimb_spese=" + restituzioneRimbSpese + ", importo_carico_orig=" + importoCaricoOrig
				+ ", identif_provv_magg_rate=" + identifProvvMaggRate + "]";
		
			
	}

}