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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * The type SorisFr1 dto.
 *
 * @author CSI PIEMONTE
 */
public class SorisFr1DTO   {


	
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
    
    @Size(max = 1, min = 0, message = "tipo_evento deve essere compreso tra 0 e 1 caratteri. ")
   	@JsonProperty("tipo_evento")
	private String tipoEvento;
   	
   	
    @JsonProperty("data_evento")
    private Date dataEvento;
    
   	@JsonProperty("importo_carico")
   	private BigDecimal importoCarico;
	
	
    @Size(max = 3, min = 0, message = "codice_divisa deve essere compreso tra 0 e 3 caratteri. ")
   	@JsonProperty("codice_divisa")
	private String codiceDivisa;
   	
    @JsonProperty("data_scad_reg")
    private Date dataScadReg;
	
    @Size(max = 17, min = 0, message = "iden_prec_avv_bon deve essere compreso tra 0 e 17 caratteri. ")
   	@JsonProperty("iden_prec_avv_bon")
	private String idenPrecAvvBon;
    
    @Size(max = 1, min = 0, message = "esito_notifica deve essere compreso tra 0 e 1 caratteri. ")
   	@JsonProperty("esito_notifica")
	private String esitoNotifica;
	
    @Size(max = 3, min = 0, message = "cod_ambito_delegato deve essere compreso tra 0 e 3 caratteri. ")
   	@JsonProperty("cod_ambito_delegato")
	private String codAmbitoDelegato;
    
    @Size(max = 20, min = 0, message = "ident_proc_esecutiva deve essere compreso tra 0 e 20 caratteri. ")
   	@JsonProperty("ident_proc_esecutiva")
	private String identProcEsecutiva;
    
    @Size(max = 2, min = 0, message = "tipo_spesa_proc_esec deve essere compreso tra 0 e 2 caratteri. ")
   	@JsonProperty("tipo_spesa_proc_esec")
	private String tipoSpesaProcEsec;
    
    @Size(max = 32, min = 0, message = "tab_deposito deve essere compreso tra 0 e 32 caratteri. ")
   	@JsonProperty("tab_deposito")
	private String tabDeposito;
    
    @Size(max = 16, min = 0, message = "tab_spese deve essere compreso tra 0 e 16 caratteri. ")
   	@JsonProperty("tab_spese")
	private String tabSpese;   
    
   	@JsonProperty("importo_conf_proc_esec")
	private Integer importoConfProcEsec;
	
	@JsonProperty("spese_proc_esec")
	private Integer speseProcEsec;
	
	@JsonProperty("spese_proc_esec_p_lista")
	private Integer speseProcEsecPLista;
	
	@JsonProperty("t_spese_proc_esec")
	private Integer tSpeseProcEsec;
	
	@JsonProperty("t_spese_proc_esec_p_lista")
	private Integer tSpeseProcEsecPLista;
	
	@JsonProperty("importo_tot_proc_esec")
	private Integer importoTotProcEsec;
	
	@Size(max = 1, min = 0, message = "attivazione_proc_esec deve essere compreso tra 0 e 1 caratteri. ")
   	@JsonProperty("attivazione_proc_esec")
	private String attivazioneProcEsec;
	
	@Size(max = 1, min = 0, message = "integrazione_proc_esec deve essere compreso tra 0 e 1 caratteri. ")
   	@JsonProperty("integrazione_proc_esec")
	private String integrazioneProcEsec;
	
	@Size(max = 1, min = 0, message = "info_evento_notifica deve essere compreso tra 0 e 1 caratteri. ")
   	@JsonProperty("info_evento_notifica")
	private String infoEventoNotifica;
	
	@Size(max = 10, min = 0, message = "filler deve essere compreso tra 0 e 10 caratteri. ")
   	@JsonProperty("filler")
	private String filler;
	
	@JsonProperty("utenza")
	private String utenza;
		
	
	
	private Long idRuolo;
	
	@JsonProperty("flg_rateizzazione")
	private Boolean flgRateizzazione;






	public Boolean getFlgRateizzazione() {
		return flgRateizzazione;
	}





	public void setFlgRateizzazione(Boolean flgRateizzazione) {
		this.flgRateizzazione = flgRateizzazione;
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





	public String getTipoEvento() {
		return tipoEvento;
	}





	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}





	public Date getDataEvento() {
		return dataEvento;
	}





	public void setDataEvento(Date dataEvento) {
		this.dataEvento = dataEvento;
	}





	public BigDecimal getImportoCarico() {
		return importoCarico;
	}





	public void setImportoCarico(BigDecimal importoCarico) {
		this.importoCarico = importoCarico;
	}





	public String getCodiceDivisa() {
		return codiceDivisa;
	}





	public void setCodiceDivisa(String codiceDivisa) {
		this.codiceDivisa = codiceDivisa;
	}





	public Date getDataScadReg() {
		return dataScadReg;
	}





	public void setDataScadReg(Date dataScadReg) {
		this.dataScadReg = dataScadReg;
	}





	public String getIdenPrecAvvBon() {
		return idenPrecAvvBon;
	}





	public void setIdenPrecAvvBon(String idenPrecAvvBon) {
		this.idenPrecAvvBon = idenPrecAvvBon;
	}





	public String getEsitoNotifica() {
		return esitoNotifica;
	}





	public void setEsitoNotifica(String esitoNotifica) {
		this.esitoNotifica = esitoNotifica;
	}





	public String getCodAmbitoDelegato() {
		return codAmbitoDelegato;
	}





	public void setCodAmbitoDelegato(String codAmbitoDelegato) {
		this.codAmbitoDelegato = codAmbitoDelegato;
	}





	public String getIdentProcEsecutiva() {
		return identProcEsecutiva;
	}





	public void setIdentProcEsecutiva(String identProcEsecutiva) {
		this.identProcEsecutiva = identProcEsecutiva;
	}





	public String getTipoSpesaProcEsec() {
		return tipoSpesaProcEsec;
	}





	public void setTipoSpesaProcEsec(String tipoSpesaProcEsec) {
		this.tipoSpesaProcEsec = tipoSpesaProcEsec;
	}





	public String getTabDeposito() {
		return tabDeposito;
	}





	public void setTabDeposito(String tabDeposito) {
		this.tabDeposito = tabDeposito;
	}





	public String getTabSpese() {
		return tabSpese;
	}





	public void setTabSpese(String tabSpese) {
		this.tabSpese = tabSpese;
	}





	public Integer getImportoConfProcEsec() {
		return importoConfProcEsec;
	}





	public void setImportoConfProcEsec(Integer importoConfProcEsec) {
		this.importoConfProcEsec = importoConfProcEsec;
	}





	public Integer getSpeseProcEsec() {
		return speseProcEsec;
	}





	public void setSpeseProcEsec(Integer speseProcEsec) {
		this.speseProcEsec = speseProcEsec;
	}





	public Integer getSpeseProcEsecPLista() {
		return speseProcEsecPLista;
	}





	public void setSpeseProcEsecPLista(Integer speseProcEsecPLista) {
		this.speseProcEsecPLista = speseProcEsecPLista;
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





	public Integer getImportoTotProcEsec() {
		return importoTotProcEsec;
	}





	public void setImportoTotProcEsec(Integer importoTotProcEsec) {
		this.importoTotProcEsec = importoTotProcEsec;
	}





	public String getAttivazioneProcEsec() {
		return attivazioneProcEsec;
	}





	public void setAttivazioneProcEsec(String attivazioneProcEsec) {
		this.attivazioneProcEsec = attivazioneProcEsec;
	}





	public String getIntegrazioneProcEsec() {
		return integrazioneProcEsec;
	}





	public void setIntegrazioneProcEsec(String integrazioneProcEsec) {
		this.integrazioneProcEsec = integrazioneProcEsec;
	}





	public String getInfoEventoNotifica() {
		return infoEventoNotifica;
	}





	public void setInfoEventoNotifica(String infoEventoNotifica) {
		this.infoEventoNotifica = infoEventoNotifica;
	}





	public String getFiller() {
		return filler;
	}





	public void setFiller(String filler) {
		this.filler = filler;
	}





	public String getUtenza() {
		return utenza;
	}





	public void setUtenza(String utenza) {
		this.utenza = utenza;
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
				+  ", codice_fiscale=" + codiceFiscale + ", tipo_evento=" + tipoEvento
				+  ", data_evento=" + dataEvento + ", importo_carico=" + importoCarico
				+  ", codice_divisa=" + codiceDivisa + ", data_scad_reg=" + dataScadReg
				+  ", iden_prec_avv_bon=" + idenPrecAvvBon + ", esito_notifica=" + esitoNotifica
				+  ", cod_ambito_delegato=" + codAmbitoDelegato + ", ident_proc_esecutiva=" + identProcEsecutiva
				+  ", tipo_spesa_proc_esec=" + tipoSpesaProcEsec + ", tab_deposito=" + tabDeposito
				+  ", tab_spese=" + tabSpese + ", importo_conf_proc_esec=" + importoConfProcEsec
				+  ", spese_proc_esec=" + speseProcEsec + ", spese_proc_esec_p_lista=" + speseProcEsecPLista
				+  ", t_spese_proc_esec=" + tSpeseProcEsec + ", t_spese_proc_esec_p_lista=" + tSpeseProcEsecPLista
				+  ", importo_tot_proc_esec=" + importoTotProcEsec + ", attivazione_proc_esec=" + attivazioneProcEsec
				+  ", integrazione_proc_esec=" + integrazioneProcEsec + ", info_evento_notifica=" + infoEventoNotifica
				+ ", filler=" + filler + ", utenza "+utenza +"]";

	}

}