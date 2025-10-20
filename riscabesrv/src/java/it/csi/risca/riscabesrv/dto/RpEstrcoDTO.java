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
 * The type RpEstrco dto.
 *
 * @author CSI PIEMONTE
 */
public class RpEstrcoDTO {

    @Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;

    @Digits(integer = 6, fraction = 0, message = "numero_progressivo deve avere massimo 6 cifre intere.")
	@JsonProperty("numero_progressivo")
	private Long numeroProgressivo;

    @Size(max = 2, min = 0, message = "tipo_record deve essere compreso tra 0 e 2 caratteri. ")
	@JsonProperty("tipo_record")
	private String tipoRecord;

    @Size(max = 5, min = 0, message = "mittente deve essere compreso tra 0 e 5 caratteri. ")
	@JsonProperty("mittente")
	private String mittente;

    @Size(max = 5, min = 0, message = "ricevente deve essere compreso tra 0 e 5 caratteri. ")
	@JsonProperty("ricevente")
	private String ricevente;

    @Size(max = 6, min = 0, message = "data_creazione deve essere compreso tra 0 e 6 caratteri. ")
	@JsonProperty("data_creazione")
	private String dataCreazione;

    @Size(max = 20, min = 0, message = "nome_supporto deve essere compreso tra 0 e 20 caratteri. ")
	@JsonProperty("nome_supporto")
	private String nomeSupporto;

    @Size(max = 5, min = 0, message = "centro_applicativo deve essere compreso tra 0 e 5 caratteri. ")
	@JsonProperty("centro_applicativo")
	private String centroApplicativo;

    @Digits(integer = 6, fraction = 0, message = "numero_rendicontazioni deve avere massimo 6 cifre intere.")
	@JsonProperty("numero_rendicontazioni")
	private Long numeroRendicontazioni;

    @Digits(integer = 6, fraction = 0, message = "numero_record deve avere massimo 6 cifre intere.")
	@JsonProperty("numero_record")
	private Long numeroRecord;

    @Digits(integer = 6, fraction = 0, message = "giornata_applicativa deve avere massimo 6 cifre intere.")
	@JsonProperty("giornata_applicativa")
	private Long giornataApplicativa;

    @Size(max = 5, min = 0, message = "codice_abi deve essere compreso tra 0 e 5 caratteri. ")
	@JsonProperty("codice_abi")
	private String codiceAbi;

    @Size(max = 5, min = 0, message = "causale deve essere compreso tra 0 e 5 caratteri. ")
	@JsonProperty("causale")
	private String causale;

    @Size(max = 16, min = 0, message = "descrizione deve essere compreso tra 0 e 16 caratteri. ")
	@JsonProperty("descrizione")
	private String descrizione;

    @Size(max = 2, min = 0, message = "tipo_conto deve essere compreso tra 0 e 2 caratteri. ")
	@JsonProperty("tipo_conto")
	private String tipoConto;

    @Size(max = 23, min = 0, message = "coordinate_bancarie deve essere compreso tra 0 e 23 caratteri. ")
	@JsonProperty("coordinate_bancarie")
	private String coordinateBancarie;

    @Size(max = 3, min = 0, message = "codice_divisa deve essere compreso tra 0 e 3 caratteri. ")
	@JsonProperty("codice_divisa")
	private String codiceDivisa;

    @Size(max = 6, min = 0, message = "data_contabile deve essere compreso tra 0 e 6 caratteri. ")
	@JsonProperty("data_contabile")
	private String dataContabile;

    @Size(max = 1, min = 0, message = "segno deve essere compreso tra 0 e 1 caratteri. ")
	@JsonProperty("segno")
	private String segno;


	@JsonProperty("saldo_iniziale_quad")
	private BigDecimal saldoInizialeQuad;

    @Size(max = 6, min = 0, message = "data_valuta deve essere compreso tra 0 e 6 caratteri. ")
	@JsonProperty("data_valuta")
	private String dataValuta;

    @Size(max = 6, min = 0, message = "data_registrazione deve essere compreso tra 0 e 6 caratteri. ")
	@JsonProperty("data_registrazione")
	private String dataRegistrazione;

    @Size(max = 1, min = 0, message = "segno_movimento deve essere compreso tra 0 e 1 caratteri. ")
	@JsonProperty("segno_movimento")
	private String segnoMovimento;

    @Digits(integer = 13, fraction = 2, message = "importo_movimento deve avere massimo 13 cifre intere.")
	@JsonProperty("importo_movimento")
	private BigDecimal importoMovimento;

    @Size(max = 2, min = 0, message = "causale_abi deve essere compreso tra 0 e 2 caratteri. ")
	@JsonProperty("causale_abi")
	private String causaleAbi;

    @Size(max = 2, min = 0, message = "causale_interna deve essere compreso tra 0 e 2 caratteri. ")
	@JsonProperty("causale_interna")
	private String causaleInterna;

    @Size(max = 16, min = 0, message = "numero_assegno deve essere compreso tra 0 e 16 caratteri. ")
	@JsonProperty("numero_assegno")
	private String numeroAssegno;

    @Size(max = 16, min = 0, message = "riferimento_banca deve essere compreso tra 0 e 16 caratteri. ")
	@JsonProperty("riferimento_banca")
	private String riferimentoBanca;

    @Size(max = 9, min = 0, message = "riferimento_cliente deve essere compreso tra 0 e 9 caratteri. ")
	@JsonProperty("riferimento_cliente")
	private String riferimentoCliente;

    @Size(max = 1000, min = 0, message = "descrizione_movimento deve essere compreso tra 0 e 1000 caratteri. ")
	@JsonProperty("descrizione_movimento")
	private String descrizioneMovimento;

    @Size(max = 1, min = 0, message = "cin deve essere compreso tra 0 e 1 caratteri. ")
	@JsonProperty("cin")
	private String cin;

    @Size(max = 5, min = 0, message = "abi deve essere compreso tra 0 e 5 caratteri. ")
	@JsonProperty("abi")
	private String abi;

    @Size(max = 5, min = 0, message = "cab deve essere compreso tra 0 e 5 caratteri. ")
	@JsonProperty("cab")
	private String cab;

    @Size(max = 12, min = 0, message = "numero_cc deve essere compreso tra 0 e 12 caratteri. ")
	@JsonProperty("numero_cc")
	private String numeroCc;

    @Digits(integer = 6, fraction = 0, message = "id_file_poste deve avere massimo 6 cifre intere.")
	@JsonProperty("id_file_poste")
	private Long idFilePoste;

    @Digits(integer = 6, fraction = 0, message = "progressivo_movimento deve avere massimo 6 cifre intere.")
	@JsonProperty("progressivo_movimento")
	private Long progressivoMovimento;
    
    @Digits(integer = 6, fraction = 0, message = "prog_riga deve avere massimo 6 cifre intere.")
	@JsonProperty("prog_riga")
	private int progRiga;

    @Size(max = 1, min = 0, message = "flg_validita deve essere compreso tra 0 e 1 caratteri. ")
	@JsonProperty("flg_validita")
	private String flgValidita;

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public Long getNumeroProgressivo() {
		return numeroProgressivo;
	}

	public void setNumeroProgressivo(Long numeroProgressivo) {
		this.numeroProgressivo = numeroProgressivo;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	public String getMittente() {
		return mittente;
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	public String getRicevente() {
		return ricevente;
	}

	public void setRicevente(String ricevente) {
		this.ricevente = ricevente;
	}

	public String getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(String dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public String getNomeSupporto() {
		return nomeSupporto;
	}

	public void setNomeSupporto(String nomeSupporto) {
		this.nomeSupporto = nomeSupporto;
	}

	public String getCentroApplicativo() {
		return centroApplicativo;
	}

	public void setCentroApplicativo(String centroApplicativo) {
		this.centroApplicativo = centroApplicativo;
	}

	public Long getNumeroRendicontazioni() {
		return numeroRendicontazioni;
	}

	public void setNumeroRendicontazioni(Long numeroRendicontazioni) {
		this.numeroRendicontazioni = numeroRendicontazioni;
	}

	public Long getNumeroRecord() {
		return numeroRecord;
	}

	public void setNumeroRecord(Long numeroRecord) {
		this.numeroRecord = numeroRecord;
	}

	public Long getGiornataApplicativa() {
		return giornataApplicativa;
	}

	public void setGiornataApplicativa(Long giornataApplicativa) {
		this.giornataApplicativa = giornataApplicativa;
	}

	public String getCodiceAbi() {
		return codiceAbi;
	}

	public void setCodiceAbi(String codiceAbi) {
		this.codiceAbi = codiceAbi;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getTipoConto() {
		return tipoConto;
	}

	public void setTipoConto(String tipoConto) {
		this.tipoConto = tipoConto;
	}

	public String getCoordinateBancarie() {
		return coordinateBancarie;
	}

	public void setCoordinateBancarie(String coordinateBancarie) {
		this.coordinateBancarie = coordinateBancarie;
	}

	public String getCodiceDivisa() {
		return codiceDivisa;
	}

	public void setCodiceDivisa(String codiceDivisa) {
		this.codiceDivisa = codiceDivisa;
	}

	public String getDataContabile() {
		return dataContabile;
	}

	public void setDataContabile(String dataContabile) {
		this.dataContabile = dataContabile;
	}

	public String getSegno() {
		return segno;
	}

	public void setSegno(String segno) {
		this.segno = segno;
	}

	public BigDecimal getSaldoInizialeQuad() {
		return saldoInizialeQuad;
	}

	public void setSaldoInizialeQuad(BigDecimal saldoInizialeQuad) {
		this.saldoInizialeQuad = saldoInizialeQuad;
	}

	public String getDataValuta() {
		return dataValuta;
	}

	public void setDataValuta(String dataValuta) {
		this.dataValuta = dataValuta;
	}

	public String getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(String dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public String getSegnoMovimento() {
		return segnoMovimento;
	}

	public void setSegnoMovimento(String segnoMovimento) {
		this.segnoMovimento = segnoMovimento;
	}

	public BigDecimal getImportoMovimento() {
		return importoMovimento;
	}

	public void setImportoMovimento(BigDecimal importoMovimento) {
		this.importoMovimento = importoMovimento;
	}

	public String getCausaleAbi() {
		return causaleAbi;
	}

	public void setCausaleAbi(String causaleAbi) {
		this.causaleAbi = causaleAbi;
	}

	public String getCausaleInterna() {
		return causaleInterna;
	}

	public void setCausaleInterna(String causaleInterna) {
		this.causaleInterna = causaleInterna;
	}

	public String getNumeroAssegno() {
		return numeroAssegno;
	}

	public void setNumeroAssegno(String numeroAssegno) {
		this.numeroAssegno = numeroAssegno;
	}

	public String getRiferimentoBanca() {
		return riferimentoBanca;
	}

	public void setRiferimentoBanca(String riferimentoBanca) {
		this.riferimentoBanca = riferimentoBanca;
	}

	public String getRiferimentoCliente() {
		return riferimentoCliente;
	}

	public void setRiferimentoCliente(String riferimentoCliente) {
		this.riferimentoCliente = riferimentoCliente;
	}

	public String getDescrizioneMovimento() {
		return descrizioneMovimento;
	}

	public void setDescrizioneMovimento(String descrizioneMovimento) {
		this.descrizioneMovimento = descrizioneMovimento;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getAbi() {
		return abi;
	}

	public void setAbi(String abi) {
		this.abi = abi;
	}

	public String getCab() {
		return cab;
	}

	public void setCab(String cab) {
		this.cab = cab;
	}

	public String getNumeroCc() {
		return numeroCc;
	}

	public void setNumeroCc(String numeroCc) {
		this.numeroCc = numeroCc;
	}

	public Long getIdFilePoste() {
		return idFilePoste;
	}

	public void setIdFilePoste(Long idFilePoste) {
		this.idFilePoste = idFilePoste;
	}

	public Long getProgressivoMovimento() {
		return progressivoMovimento;
	}

	public void setProgressivoMovimento(Long progressivoMovimento) {
		this.progressivoMovimento = progressivoMovimento;
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

	@Override
	public String toString() {
		return "RpEstrcoDTO [idElabora=" + idElabora + ", numeroProgressivo=" + numeroProgressivo + ", tipoRecord="
				+ tipoRecord + ", mittente=" + mittente + ", ricevente=" + ricevente + ", dataCreazione="
				+ dataCreazione + ", nomeSupporto=" + nomeSupporto + ", centroApplicativo=" + centroApplicativo
				+ ", numeroRendicontazioni=" + numeroRendicontazioni + ", numeroRecord=" + numeroRecord
				+ ", giornataApplicativa=" + giornataApplicativa + ", codiceAbi=" + codiceAbi + ", causale=" + causale
				+ ", descrizione=" + descrizione + ", tipoConto=" + tipoConto + ", coordinateBancarie="
				+ coordinateBancarie + ", codiceDivisa=" + codiceDivisa + ", dataContabile=" + dataContabile
				+ ", segno=" + segno + ", saldoInizialeQuad=" + saldoInizialeQuad + ", dataValuta=" + dataValuta
				+ ", dataRegistrazione=" + dataRegistrazione + ", segnoMovimento=" + segnoMovimento
				+ ", importoMovimento=" + importoMovimento + ", causaleAbi=" + causaleAbi + ", causaleInterna="
				+ causaleInterna + ", numeroAssegno=" + numeroAssegno + ", riferimentoBanca=" + riferimentoBanca
				+ ", riferimentoCliente=" + riferimentoCliente + ", descrizioneMovimento=" + descrizioneMovimento
				+ ", cin=" + cin + ", abi=" + abi + ", cab=" + cab + ", numeroCc=" + numeroCc + ", idFilePoste="
				+ idFilePoste + ", progressivoMovimento=" + progressivoMovimento + ", progRiga=" + progRiga
				+ ", flgValidita=" + flgValidita + "]";
	}

}