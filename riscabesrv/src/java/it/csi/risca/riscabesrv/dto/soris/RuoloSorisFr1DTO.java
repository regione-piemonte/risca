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

import it.csi.risca.riscabesrv.dto.GestAttoreDTO;

/**
 * The type RuoloSorisFr1 dto.
 *
 * @author CSI PIEMONTE
 */
public class RuoloSorisFr1DTO extends GestAttoreDTO {


	@Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;


	@Min(value = 1, message = "L' id_ruolo_soris_fr1 deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_ruolo_soris_fr1 supera il limite massimo consentito per Integer")
	@JsonProperty("id_ruolo_soris_fr1")
	private Long idRuoloSorisFr1;

	@JsonProperty("id_ruolo")
	private Long idRuolo;

	@Min(value = 1, message = "L' id_file_soris deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_file_soris supera il limite massimo consentito per Integer")
	@JsonProperty("id_file_soris")
	private Long idFileSoris;


	private String note;


	@JsonProperty("data_scad_reg")
	private Date dataScadReg;

	@Size(max = 3, min = 0, message = "codice_divisa deve essere compreso tra 0 e 3 caratteri. ")
	@JsonProperty("codice_divisa")
	private String codiceDivisa;

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



	@JsonProperty("utenza")
	private String utenza;

	@JsonProperty("flg_rateizzazione")
	private Boolean flgRateizzazione;


	@JsonIgnore
	private Boolean flgConfronto;


	public Boolean getFlgRateizzazione() {
		return flgRateizzazione;
	}



	public void setFlgRateizzazione(Boolean flgRateizzazione) {
		this.flgRateizzazione = flgRateizzazione;
	}



	public Date getDataScadReg() {
		return dataScadReg;
	}



	public void setDataScadReg(Date dataScadReg) {
		this.dataScadReg = dataScadReg;
	}



	public String getCodiceDivisa() {
		return codiceDivisa;
	}



	public void setCodiceDivisa(String codiceDivisa) {
		this.codiceDivisa = codiceDivisa;
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



	public String getUtenza() {
		return utenza;
	}



	public void setUtenza(String utenza) {
		this.utenza = utenza;
	}



	public Long getIdElabora() {
		return idElabora;
	}



	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public Long getIdRuoloSorisFr1() {
		return idRuoloSorisFr1;
	}



	public void setIdRuoloSorisFr1(Long idRuoloSorisFr1) {
		this.idRuoloSorisFr1 = idRuoloSorisFr1;
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





	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}


	public Boolean getFlgConfronto() {
		return flgConfronto;
	}



	public void setFlgConfronto(Boolean flgConfronto) {
		this.flgConfronto = flgConfronto;
	}

	@Override
	public String toString() {
		return "SorisFr1DTO [idRuoloSorisFr1=" + idRuoloSorisFr1 + ", idRuolo=" + idRuolo + ", idFileSoris="
				+ idFileSoris + ", flgRateizzazione=" + flgRateizzazione + ", note=" + note+"]";

	}



}