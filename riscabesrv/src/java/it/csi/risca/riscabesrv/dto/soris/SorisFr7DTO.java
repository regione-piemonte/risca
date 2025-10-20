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

import java.util.Date;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type SorisFr7 dto.
 *
 * @author CSI PIEMONTE
 */
public class SorisFr7DTO {

	
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
    

    @Size(max = 153, min = 0, message = "chiave_inf_annullare deve essere compreso tra 0 e 153 caratteri. ")
   	@JsonProperty("chiave_inf_annullare")
    private String chiaveInfAnnullare;
	   
    @Size(max = 153, min = 0, message = "chiave_inf_correttiva deve essere compreso tra 0 e 153 caratteri. ")
   	@JsonProperty("chiave_inf_correttiva")
    private String chiaveInfCorrettiva;
	
    
    @Size(max = 1, min = 0, message = "tipo_evento deve essere compreso tra 0 e 1 caratteri. ")
   	@JsonProperty("tipo_evento")
    private String tipoEvento;
	
    @Size(max = 1, min = 0, message = "motivo_rich_annul deve essere compreso tra 0 e 1 caratteri. ")
   	@JsonProperty("motivo_rich_annul")
    private String motivoRichAnnul;
    
    @Size(max = 5, min = 0, message = "ente_richiedente deve essere compreso tra 0 e 5 caratteri. ")
   	@JsonProperty("ente_richiedente")
    private String enteRichiedente;
   	
    @JsonProperty("data_richiesta")
    private Date dataRichiesta;
	
    @Size(max = 61, min = 0, message = "filler deve essere compreso tra 0 e 61 caratteri. ")
   	@JsonProperty("filler")
    private String filler;
    
	@JsonProperty("id_ruolo")
	private Long idRuolo;
	
	@JsonProperty("id_pagamento")
	private Long idPagamento;
	
	@JsonProperty("utenza")
	private String utenza;
	
	@JsonProperty("anno_ruolo")
	private String annoRuolo;
	
	@JsonProperty("numero_ruolo")
	private String numeroRuolo;
	
	@JsonProperty("p_tipo_ufficio")
	private String pTipoUfficio;

	@JsonProperty("p_codice_ufficio")
	private String pCodiceUfficio;
	
	@JsonProperty("p_anno_riferimento")
	private String pAnnoRiferimento;
	
	@JsonProperty("p_ident_prenot_ruolo")
	private String pIdentPrenotRuolo;
    
	public String getDataEvento() {
		return dataEvento;
	}




	public void setDataEvento(String dataEvento) {
		this.dataEvento = dataEvento;
	}




	@JsonProperty("data_evento")
	private String dataEvento;
	
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







	public String getChiaveInfAnnullare() {
		return chiaveInfAnnullare;
	}




	public void setChiaveInfAnnullare(String chiaveInfAnnullare) {
		this.chiaveInfAnnullare = chiaveInfAnnullare;
	}




	public String getChiaveInfCorrettiva() {
		return chiaveInfCorrettiva;
	}




	public void setChiaveInfCorrettiva(String chiaveInfCorrettiva) {
		this.chiaveInfCorrettiva = chiaveInfCorrettiva;
	}




	public String getTipoEvento() {
		return tipoEvento;
	}




	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}




	public String getMotivoRichAnnul() {
		return motivoRichAnnul;
	}




	public void setMotivoRichAnnul(String motivoRichAnnul) {
		this.motivoRichAnnul = motivoRichAnnul;
	}




	public String getEnteRichiedente() {
		return enteRichiedente;
	}




	public void setEnteRichiedente(String enteRichiedente) {
		this.enteRichiedente = enteRichiedente;
	}




	public Date getDataRichiesta() {
		return dataRichiesta;
	}




	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}




	public String getFiller() {
		return filler;
	}




	public void setFiller(String filler) {
		this.filler = filler;
	}




	public void setProgrRecord(Integer progrRecord) {
		this.progrRecord = progrRecord;
	}
	
	


	public Long getIdRuolo() {
		return idRuolo;
	}




	public void setIdRuolo(Long idRuolo) {
		this.idRuolo = idRuolo;
	}




	public Long getIdPagamento() {
		return idPagamento;
	}




	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}




	public String getUtenza() {
		return utenza;
	}




	public void setUtenza(String utenza) {
		this.utenza = utenza;
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




	public String getpIdentPrenotRuolo() {
		return pIdentPrenotRuolo;
	}




	public void setpIdentPrenotRuolo(String pIdentPrenotRuolo) {
		this.pIdentPrenotRuolo = pIdentPrenotRuolo;
	}




	@Override
	public String toString() {
		return "SorisFr1DTO [tipoRecord=" + tipoRecord + ", progrRecord=" + progrRecord + ", codAmbito="
				+ codAmbito + ", cod_ente_creditore=" + codEnteCreditore + ", chiave_inf_annullare=" + chiaveInfAnnullare
				+  ", chiave_inf_correttiva=" + chiaveInfCorrettiva + ", tipo_evento=" + tipoEvento
				+  ", motivo_rich_annul=" + motivoRichAnnul + ", ente_richiedente=" + enteRichiedente
				+  ", data_richiesta=" + dataRichiesta + ", filler=" + filler+ "]";
		
			
	}

}