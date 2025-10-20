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

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type RpNonPremarcati dto.
 *
 * @author CSI PIEMONTE
 */
public class RpNonPremarcatiDTO {

    @Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;

    @Digits(integer = 6, fraction = 0, message = "progr deve avere massimo 6 cifre intere.")
	@JsonProperty("progr")
	private int progr;

    @Size(max = 2, min = 0, message = "identif_inizio_flusso deve essere compreso tra 0 e 2 caratteri. ")
	@JsonProperty("identif_inizio_flusso")
	private String identifInizioFlusso;

    @Size(max = 8, min = 0, message = "data_rif_dati deve essere compreso tra 0 e 8 caratteri.  ")
	@JsonProperty("data_rif_dati")
	private String dataRifDati;

    @Size(max = 12, min = 0, message = "numero_conto deve essere compreso tra 0 e 12 caratteri.  ")
	@JsonProperty("numero_conto")
	private String numeroConto;

    @Size(max = 1, min = 0, message = "cuas deve essere compreso tra 0 e 1 caratteri.  ")
	@JsonProperty("cuas")
	private String cuas;

    @Size(max = 1, min = 0, message = "valuta deve essere compreso tra 0 e 1 caratteri.  ")
	@JsonProperty("valuta")
	private String valuta;

    @Digits(integer = 13, fraction = 2, message = "importo deve avere massimo 13 cifre intere 2 decimali.")
	@JsonProperty("importo")
	private BigDecimal importo;

    @Size(max = 8, min = 0, message = "data_acc deve essere compreso tra 0 e 8 caratteri.  ")
	@JsonProperty("data_acc")
	private String dataAcc;

    @Size(max = 8, min = 0, message = "data_all deve essere compreso tra 0 e 8 caratteri.  ")
	@JsonProperty("data_all")
	private String dataAll;

    @Size(max = 6, min = 0, message = "fraz_uff deve essere compreso tra 0 e 6 caratteri.  ")
	@JsonProperty("fraz_uff")
	private String frazUff;

    @Size(max = 3, min = 0, message = "tipo_doc deve essere compreso tra 0 e 3 caratteri.  ")
	@JsonProperty("tipo_doc")
	private String tipoDoc;

    @Size(max = 16, min = 0, message = "quinto_campo deve essere compreso tra 0 e 16 caratteri.  ")
	@JsonProperty("quinto_campo")
	private String quintoCampo;

    @Size(max = 7, min = 0, message = "numero_avviso deve essere compreso tra 0 e 7 caratteri.  ")
	@JsonProperty("numero_avviso")
	private String numeroAvviso;

    @Size(max = 50, min = 0, message = "iden_imm deve essere compreso tra 0 e 50 caratteri.  ")
	@JsonProperty("iden_imm")
	private String idenImm;

    @Size(max = 4, min = 0, message = "vcy deve essere compreso tra 0 e 4 caratteri.  ")
	@JsonProperty("vcy")
	private String vcy;

    @Size(max = 2, min = 0, message = "identif deve essere compreso tra 0 e 2 caratteri.  ")
	@JsonProperty("identif")
	private String identif;

    @Digits(integer = 9, fraction = 0, message = "num_vers deve avere massimo 9 cifre intere.")
	@JsonProperty("num_vers")
	private Long numVers;

    @Digits(integer = 7, fraction = 0, message = "id_file_poste deve avere massimo 9 cifre intere.")
	@JsonProperty("id_file_poste")
	private Long idFilePoste;

    @Min(value = 1, message = "L' id_immagine deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_immagine supera il limite massimo consentito per Integer")
	@JsonProperty("id_immagine")
	private Long idImmagine;

    @Min(value = 0, message = "flg_elaborato deve essere maggiore di 0")
    @Max(value = 1, message = "flg_elaborato  deve essere minore di 1")
	@JsonProperty("flg_elaborato")
	private int flgElaborato;

    @Digits(integer = 6, fraction = 0, message = "prog_riga deve avere massimo 6 cifre intere.")
	@JsonProperty("prog_riga")
	private int progRiga;
    
    @Size(max = 1, min = 0, message = "flg_validita deve essere compreso tra 0 e 30 caratteri.  ")
	@JsonProperty("flg_validita")
	private String flgValidita;

    @Size(max = 150, min = 0, message = "file_immagine deve essere compreso tra 0 e 30 caratteri.")
	@JsonProperty("file_immagine")
	private String fileImmagine;

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	public String getIdentifInizioFlusso() {
		return identifInizioFlusso;
	}

	public void setIdentifInizioFlusso(String identifInizioFlusso) {
		this.identifInizioFlusso = identifInizioFlusso;
	}

	public String getDataRifDati() {
		return dataRifDati;
	}

	public void setDataRifDati(String dataRifDati) {
		this.dataRifDati = dataRifDati;
	}

	public String getNumeroConto() {
		return numeroConto;
	}

	public void setNumeroConto(String numeroConto) {
		this.numeroConto = numeroConto;
	}

	public String getCuas() {
		return cuas;
	}

	public void setCuas(String cuas) {
		this.cuas = cuas;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public String getDataAcc() {
		return dataAcc;
	}

	public void setDataAcc(String dataAcc) {
		this.dataAcc = dataAcc;
	}

	public String getDataAll() {
		return dataAll;
	}

	public void setDataAll(String dataAll) {
		this.dataAll = dataAll;
	}

	public String getFrazUff() {
		return frazUff;
	}

	public void setFrazUff(String frazUff) {
		this.frazUff = frazUff;
	}

	public String getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getQuintoCampo() {
		return quintoCampo;
	}

	public void setQuintoCampo(String quintoCampo) {
		this.quintoCampo = quintoCampo;
	}

	public String getNumeroAvviso() {
		return numeroAvviso;
	}

	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}

	public String getIdenImm() {
		return idenImm;
	}

	public void setIdenImm(String idenImm) {
		this.idenImm = idenImm;
	}

	public String getVcy() {
		return vcy;
	}

	public void setVcy(String vcy) {
		this.vcy = vcy;
	}

	public String getIdentif() {
		return identif;
	}

	public void setIdentif(String identif) {
		this.identif = identif;
	}

	public Long getNumVers() {
		return numVers;
	}

	public void setNumVers(Long numVers) {
		this.numVers = numVers;
	}

	public Long getIdFilePoste() {
		return idFilePoste;
	}

	public void setIdFilePoste(Long idFilePoste) {
		this.idFilePoste = idFilePoste;
	}

	public Long getIdImmagine() {
		return idImmagine;
	}

	public void setIdImmagine(Long idImmagine) {
		this.idImmagine = idImmagine;
	}

	public int getFlgElaborato() {
		return flgElaborato;
	}

	public void setFlgElaborato(int flgElaborato) {
		this.flgElaborato = flgElaborato;
	}

	public int getProgRiga() {
		return progRiga;
	}

	public void setProgRiga(int progRiga) {
		this.progRiga = progRiga;
	}

	public String getFlgValidita() {
		return flgValidita;
	}

	public void setFlgValidita(String flgValidita) {
		this.flgValidita = flgValidita;
	}

	public String getFileImmagine() {
		return fileImmagine;
	}

	public void setFileImmagine(String fileImmagine) {
		this.fileImmagine = fileImmagine;
	}

}