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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.dto.GestAttoreDTO;

/**
 * The type RuoloSorisFr7 dto.
 *
 * @author CSI PIEMONTE
 */
public class RuoloSorisFr7DTO extends GestAttoreDTO {


	@Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;


	@Min(value = 1, message = "L' id_ruolo_soris_fr7 deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_ruolo_soris_fr7 supera il limite massimo consentito per Integer")
	@JsonProperty("id_ruolo_soris_fr7")
	private Long idRuoloSorisFr7;

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

	@Size(max = 7, min = 0, message = "utenza deve essere compreso tra 0 e 7 caratteri. ")
	@JsonProperty("utenza")
	private String utenza;

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
	
	@Size(max = 34, min = 0, message = "p_ident_prenot_ruolo deve essere compreso tra 0 e 34 caratteri. ")
	@JsonProperty("p_ident_prenot_ruolo")
	private String pIdentPrenotRuolo;
	
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
	
	@Size(max = 200, min = 0, message = "note deve essere compreso tra 0 e 200 caratteri. ")
	@JsonProperty("note")
	private String note;
	
	@JsonProperty("data_evento")
	private String dataEvento;
	
	public String getDataEvento() {
		return dataEvento;
	}



	public void setDataEvento(String dataEvento) {
		this.dataEvento = dataEvento;
	}



	public Long getIdElabora() {
		return idElabora;
	}



	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}



	public Long getIdRuoloSorisFr7() {
		return idRuoloSorisFr7;
	}



	public void setIdRuoloSorisFr7(Long idRuoloSorisFr7) {
		this.idRuoloSorisFr7 = idRuoloSorisFr7;
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



	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}


	@Override
	public String toString() {
		return "RuoloSorisFr7DTO [idRuoloSorisFr7=" + idRuoloSorisFr7 + ", idRuolo=" + idRuolo + ", idFileSoris="
				+ idFileSoris + ", idPagamento=" + idPagamento + ", tipoRecord=" + tipoRecord 
				+ ", utenza=" + utenza +  ", progrRecord=" + progrRecord  +  ", progrRecord=" + progrRecord 
				+  ", codAmbito=" + codAmbito + ", codEnteCreditore=" + codEnteCreditore 
				+ ", annoRuolo=" + annoRuolo + ", numeroRuolo=" + numeroRuolo 
				+ ", pTipoUfficio=" + pTipoUfficio + ", pCodiceUfficio=" + pCodiceUfficio 
				+  ", pAnnoRiferimento=" + pAnnoRiferimento +  ", cod_ambito=" + codAmbito 
				+  ", pIdentPrenotRuolo=" + pIdentPrenotRuolo + ", chiave_inf_annullare=" + chiaveInfAnnullare 
				+  ", chiave_inf_correttiva=" + chiaveInfCorrettiva + ", tipo_evento=" + tipoEvento 
				+  ", motivo_rich_annul=" + motivoRichAnnul + ", ente_richiedente=" + enteRichiedente 
				+  ", data_richiesta=" + dataRichiesta 
				+ ", note=" + note+"]";

	}

}