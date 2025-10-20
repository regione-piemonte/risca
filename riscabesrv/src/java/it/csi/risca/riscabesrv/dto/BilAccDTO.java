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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * BilAccDTO
 *
 * @author CSI PIEMONTE
 */

public class BilAccDTO extends GestAttoreDTO {

	@JsonProperty("id_bil_acc")
	private Long idBilAcc;

	@JsonProperty("id_ambito")
	private Long idAmbito;

	@JsonProperty("cod_bil_acc")
	private String codBilAcc;

	@JsonProperty("des_bil_acc")
	private String desBilAcc;

	@JsonProperty("anno")
	private Integer anno;

	@JsonProperty("note_backoffice")
	private String noteBackoffice;

	@JsonProperty("dati_spec_risc")
	private String datiSpecRisc;

	@JsonProperty("anno_competenza")
	private Integer annoCompetenza;

	@JsonProperty("data_fine_validita")
	private Date dataFineValidita;

	@JsonProperty("data_inizio_validita")
	private Date dataInizioValidita;

	

	@JsonProperty("id_accerta_bilancio")
	private Long idAccertaBilancio;
	
	@JsonIgnore
	private String numero_acc_bilancio;

	public Long getIdBilAcc() {
		return idBilAcc;
	}

	public void setIdBilAcc(Long idBilAcc) {
		this.idBilAcc = idBilAcc;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodBilAcc() {
		return codBilAcc;
	}

	public void setCodBilAcc(String codBilAcc) {
		this.codBilAcc = codBilAcc;
	}

	public String getDesBilAcc() {
		return desBilAcc;
	}

	public void setDesBilAcc(String desBilAcc) {
		this.desBilAcc = desBilAcc;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getNoteBackoffice() {
		return noteBackoffice;
	}

	public void setNoteBackoffice(String noteBackoffice) {
		this.noteBackoffice = noteBackoffice;
	}

	public String getDatiSpecRisc() {
		return datiSpecRisc;
	}

	public void setDatiSpecRisc(String datiSpecRisc) {
		this.datiSpecRisc = datiSpecRisc;
	}

	public Integer getAnnoCompetenza() {
		return annoCompetenza;
	}

	public void setAnnoCompetenza(Integer annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}

	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public Long getIdAccertaBilancio() {
		return idAccertaBilancio;
	}

	public void setIdAccertaBilancio(Long idAccertaBilancio) {
		this.idAccertaBilancio = idAccertaBilancio;
	}
	
	public String getNumero_acc_bilancio() {
		return numero_acc_bilancio;
	}

	public void setNumero_acc_bilancio(String numero_acc_bilancio) {
		this.numero_acc_bilancio = numero_acc_bilancio;
	}

}
