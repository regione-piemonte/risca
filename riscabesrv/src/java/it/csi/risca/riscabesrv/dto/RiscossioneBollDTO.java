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

/**
 * RiscossioneBollDTO
 *
 * @author CSI PIEMONTE
 */
public class RiscossioneBollDTO {

	@JsonProperty("dati_riscossione")
	private String datiRiscossione;

	@JsonProperty("id_riscossione")
	private Long idRiscossione;

	@JsonProperty("id_tipo_riscossione")
	private Long idTipoRiscossione;

	@JsonProperty("id_stato_riscossione")
	private Long idStatoRiscossione;

	@JsonProperty("id_soggetto")
	private Long idSoggetto;

	@JsonProperty("id_gruppo_soggetto")
	private Long idGruppoSoggetto;

	@JsonProperty("id_tipo_autorizza")
	private Long idTipoAutorizza;

	@JsonProperty("cod_riscossione")
	private String codRiscossione;

	@JsonProperty("cod_riscossione_prov")
	private String codRiscossioneProv;

	@JsonProperty("cod_riscossione_prog")
	private String codRiscossioneProg;

	@JsonProperty("id_sigla_riscossione")
	private Long idSiglaRiscossione;

	@JsonProperty("cod_riscossione_lettera_prov")
	private String codRiscossioneLetteraProv;

	@JsonProperty("num_pratica")
	private String numPratica;

	@JsonProperty("flg_prenotata")
	private int flgPrenotata;

	@JsonProperty("motivo_prenotazione")
	private String motivoPrenotazione;

	@JsonProperty("note_riscossione")
	private String noteRiscossione;

	@JsonProperty("data_ini_concessione")
	private Date dataIniConcessione;

	@JsonProperty("data_scad_concessione")
	private Date dataScadConcessione;

	@JsonProperty("data_ini_sosp_canone")
	private Date dataIniSospCanone;

	@JsonProperty("data_fine_sosp_canone")
	private Date dataFineSospCanone;

	@JsonProperty("json_dt")
	private String jsonDt;

	@JsonProperty("id_componente_dt")
	private Long idComponenteDt;

	@JsonProperty("dati_tipo_riscossione")
	private String datiTipoRiscossione;

	@JsonProperty("cod_tipo_riscossione")
	private String codTipoRiscossione;

	@JsonProperty("des_tipo_riscossione")
	private String desTipoRiscossione;

	@JsonProperty("dati_stato_riscossione")
	private String datiStatoRiscossione;

	@JsonProperty("cod_stato_riscossione")
	private String codStatoRiscossione;

	@JsonProperty("des_stato_riscossione")
	private String desStatoRiscossione;

	@JsonProperty("dati_tipo_autorizza")
	private String datiTipoAutorizza;

	@JsonProperty("cod_tipo_autorizza")
	private String codTipoAutorizza;

	@JsonProperty("des_tipo_autorizza")
	private String desTipoAutorizza;

	@JsonProperty("dati_ambito")
	private String datiAmbito;

	@JsonProperty("cod_ambito")
	private String codAmbito;

	@JsonProperty("des_ambito")
	private String desAmbito;

	@JsonProperty("dati_indirizzo_principale")
	private String datiIndirizzoPrincipale;

	@JsonProperty("id_riscossione_p")
	private Long idRiscossioneP;

	@JsonProperty("id_recapito_p")
	private Long idRecapitoP;

	@JsonProperty("cod_tipo_recapito_p")
	private String codTipoRecapitoP;

	@JsonProperty("des_tipo_recapito_p")
	private String desTipoRecapitoP;

	@JsonProperty("cod_tipo_invio_p")
	private String codTipoInvioP;

	@JsonProperty("des_tipo_invio_p")
	private String desTipoInvioP;

	@JsonProperty("dati_indirizzo_alternativo")
	private String datiIndirizzoAlternativo;

	@JsonProperty("id_riscossione_a")
	private Long idRiscossioneA;

	@JsonProperty("id_recapito_a")
	private Long idRecapitoA;

	@JsonProperty("cod_tipo_recapito_a")
	private String codTipoRecapitoA;

	@JsonProperty("des_tipo_recapito_a")
	private String desTipoRecapitoA;

	@JsonProperty("cod_tipo_invio_a")
	private String codTipoInvioA;

	@JsonProperty("des_tipo_invio_a")
	private String desTipoInvioA;

	@JsonProperty("dati_rimborso")
	private String datiRimborso;

	@JsonProperty("sum_imp_rimborso_riscossione")
	private BigDecimal sumImpRimborsoRiscossione;

	@JsonProperty("dati_stato_debitorio")
	private String datiStatoDebitorio;

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("desc_periodo_pagamento")
	private String descPeriodoPagamento;

	@JsonProperty("stato_contribuzione")
	private String statoContribuzione;

	@JsonProperty("id_stato_contribuzione")
	private Long idStatoContribuzione;

	@JsonProperty("id_titolare")
	private String idTitolare;

	public String getDatiRiscossione() {
		return datiRiscossione;
	}

	public void setDatiRiscossione(String datiRiscossione) {
		this.datiRiscossione = datiRiscossione;
	}

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public Long getIdTipoRiscossione() {
		return idTipoRiscossione;
	}

	public void setIdTipoRiscossione(Long idTipoRiscossione) {
		this.idTipoRiscossione = idTipoRiscossione;
	}

	public Long getIdStatoRiscossione() {
		return idStatoRiscossione;
	}

	public void setIdStatoRiscossione(Long idStatoRiscossione) {
		this.idStatoRiscossione = idStatoRiscossione;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	public Long getIdTipoAutorizza() {
		return idTipoAutorizza;
	}

	public void setIdTipoAutorizza(Long idTipoAutorizza) {
		this.idTipoAutorizza = idTipoAutorizza;
	}

	public String getCodRiscossione() {
		return codRiscossione;
	}

	public void setCodRiscossione(String codRiscossione) {
		this.codRiscossione = codRiscossione;
	}

	public String getCodRiscossioneProv() {
		return codRiscossioneProv;
	}

	public void setCodRiscossioneProv(String codRiscossioneProv) {
		this.codRiscossioneProv = codRiscossioneProv;
	}

	public String getCodRiscossioneProg() {
		return codRiscossioneProg;
	}

	public void setCodRiscossioneProg(String codRiscossioneProg) {
		this.codRiscossioneProg = codRiscossioneProg;
	}

	public Long getIdSiglaRiscossione() {
		return idSiglaRiscossione;
	}

	public void setIdSiglaRiscossione(Long idSiglaRiscossione) {
		this.idSiglaRiscossione = idSiglaRiscossione;
	}

	public String getCodRiscossioneLetteraProv() {
		return codRiscossioneLetteraProv;
	}

	public void setCodRiscossioneLetteraProv(String codRiscossioneLetteraProv) {
		this.codRiscossioneLetteraProv = codRiscossioneLetteraProv;
	}

	public String getNumPratica() {
		return numPratica;
	}

	public void setNumPratica(String numPratica) {
		this.numPratica = numPratica;
	}

	public int getFlgPrenotata() {
		return flgPrenotata;
	}

	public void setFlgPrenotata(int flgPrenotata) {
		this.flgPrenotata = flgPrenotata;
	}

	public String getMotivoPrenotazione() {
		return motivoPrenotazione;
	}

	public void setMotivoPrenotazione(String motivoPrenotazione) {
		this.motivoPrenotazione = motivoPrenotazione;
	}

	public String getNoteRiscossione() {
		return noteRiscossione;
	}

	public void setNoteRiscossione(String noteRiscossione) {
		this.noteRiscossione = noteRiscossione;
	}

	public Date getDataIniConcessione() {
		return dataIniConcessione;
	}

	public void setDataIniConcessione(Date dataIniConcessione) {
		this.dataIniConcessione = dataIniConcessione;
	}

	public Date getDataScadConcessione() {
		return dataScadConcessione;
	}

	public void setDataScadConcessione(Date dataScadConcessione) {
		this.dataScadConcessione = dataScadConcessione;
	}

	public Date getDataIniSospCanone() {
		return dataIniSospCanone;
	}

	public void setDataIniSospCanone(Date dataIniSospCanone) {
		this.dataIniSospCanone = dataIniSospCanone;
	}

	public Date getDataFineSospCanone() {
		return dataFineSospCanone;
	}

	public void setDataFineSospCanone(Date dataFineSospCanone) {
		this.dataFineSospCanone = dataFineSospCanone;
	}

	public String getJsonDt() {
		return jsonDt;
	}

	public void setJsonDt(String jsonDt) {
		this.jsonDt = jsonDt;
	}

	public Long getIdComponenteDt() {
		return idComponenteDt;
	}

	public void setIdComponenteDt(Long idComponenteDt) {
		this.idComponenteDt = idComponenteDt;
	}

	public String getDatiTipoRiscossione() {
		return datiTipoRiscossione;
	}

	public void setDatiTipoRiscossione(String datiTipoRiscossione) {
		this.datiTipoRiscossione = datiTipoRiscossione;
	}

	public String getCodTipoRiscossione() {
		return codTipoRiscossione;
	}

	public void setCodTipoRiscossione(String codTipoRiscossione) {
		this.codTipoRiscossione = codTipoRiscossione;
	}

	public String getDesTipoRiscossione() {
		return desTipoRiscossione;
	}

	public void setDesTipoRiscossione(String desTipoRiscossione) {
		this.desTipoRiscossione = desTipoRiscossione;
	}

	public String getDatiStatoRiscossione() {
		return datiStatoRiscossione;
	}

	public void setDatiStatoRiscossione(String datiStatoRiscossione) {
		this.datiStatoRiscossione = datiStatoRiscossione;
	}

	public String getCodStatoRiscossione() {
		return codStatoRiscossione;
	}

	public void setCodStatoRiscossione(String codStatoRiscossione) {
		this.codStatoRiscossione = codStatoRiscossione;
	}

	public String getDesStatoRiscossione() {
		return desStatoRiscossione;
	}

	public void setDesStatoRiscossione(String desStatoRiscossione) {
		this.desStatoRiscossione = desStatoRiscossione;
	}

	public String getDatiTipoAutorizza() {
		return datiTipoAutorizza;
	}

	public void setDatiTipoAutorizza(String datiTipoAutorizza) {
		this.datiTipoAutorizza = datiTipoAutorizza;
	}

	public String getCodTipoAutorizza() {
		return codTipoAutorizza;
	}

	public void setCodTipoAutorizza(String codTipoAutorizza) {
		this.codTipoAutorizza = codTipoAutorizza;
	}

	public String getDesTipoAutorizza() {
		return desTipoAutorizza;
	}

	public void setDesTipoAutorizza(String desTipoAutorizza) {
		this.desTipoAutorizza = desTipoAutorizza;
	}

	public String getDatiAmbito() {
		return datiAmbito;
	}

	public void setDatiAmbito(String datiAmbito) {
		this.datiAmbito = datiAmbito;
	}

	public String getCodAmbito() {
		return codAmbito;
	}

	public void setCodAmbito(String codAmbito) {
		this.codAmbito = codAmbito;
	}

	public String getDesAmbito() {
		return desAmbito;
	}

	public void setDesAmbito(String desAmbito) {
		this.desAmbito = desAmbito;
	}

	public String getDatiIndirizzoPrincipale() {
		return datiIndirizzoPrincipale;
	}

	public void setDatiIndirizzoPrincipale(String datiIndirizzoPrincipale) {
		this.datiIndirizzoPrincipale = datiIndirizzoPrincipale;
	}

	public Long getIdRiscossioneP() {
		return idRiscossioneP;
	}

	public void setIdRiscossioneP(Long idRiscossioneP) {
		this.idRiscossioneP = idRiscossioneP;
	}

	public Long getIdRecapitoP() {
		return idRecapitoP;
	}

	public void setIdRecapitoP(Long idRecapitoP) {
		this.idRecapitoP = idRecapitoP;
	}

	public String getCodTipoRecapitoP() {
		return codTipoRecapitoP;
	}

	public void setCodTipoRecapitoP(String codTipoRecapitoP) {
		this.codTipoRecapitoP = codTipoRecapitoP;
	}

	public String getDesTipoRecapitoP() {
		return desTipoRecapitoP;
	}

	public void setDesTipoRecapitoP(String desTipoRecapitoP) {
		this.desTipoRecapitoP = desTipoRecapitoP;
	}

	public String getCodTipoInvioP() {
		return codTipoInvioP;
	}

	public void setCodTipoInvioP(String codTipoInvioP) {
		this.codTipoInvioP = codTipoInvioP;
	}

	public String getDesTipoInvioP() {
		return desTipoInvioP;
	}

	public void setDesTipoInvioP(String desTipoInvioP) {
		this.desTipoInvioP = desTipoInvioP;
	}

	public String getDatiIndirizzoAlternativo() {
		return datiIndirizzoAlternativo;
	}

	public void setDatiIndirizzoAlternativo(String datiIndirizzoAlternativo) {
		this.datiIndirizzoAlternativo = datiIndirizzoAlternativo;
	}

	public Long getIdRiscossioneA() {
		return idRiscossioneA;
	}

	public void setIdRiscossioneA(Long idRiscossioneA) {
		this.idRiscossioneA = idRiscossioneA;
	}

	public Long getIdRecapitoA() {
		return idRecapitoA;
	}

	public void setIdRecapitoA(Long idRecapitoA) {
		this.idRecapitoA = idRecapitoA;
	}

	public String getCodTipoRecapitoA() {
		return codTipoRecapitoA;
	}

	public void setCodTipoRecapitoA(String codTipoRecapitoA) {
		this.codTipoRecapitoA = codTipoRecapitoA;
	}

	public String getDesTipoRecapitoA() {
		return desTipoRecapitoA;
	}

	public void setDesTipoRecapitoA(String desTipoRecapitoA) {
		this.desTipoRecapitoA = desTipoRecapitoA;
	}

	public String getCodTipoInvioA() {
		return codTipoInvioA;
	}

	public void setCodTipoInvioA(String codTipoInvioA) {
		this.codTipoInvioA = codTipoInvioA;
	}

	public String getDesTipoInvioA() {
		return desTipoInvioA;
	}

	public void setDesTipoInvioA(String desTipoInvioA) {
		this.desTipoInvioA = desTipoInvioA;
	}

	public String getDatiRimborso() {
		return datiRimborso;
	}

	public void setDatiRimborso(String datiRimborso) {
		this.datiRimborso = datiRimborso;
	}

	public BigDecimal getSumImpRimborsoRiscossione() {
		return sumImpRimborsoRiscossione;
	}

	public void setSumImpRimborsoRiscossione(BigDecimal sumImpRimborsoRiscossione) {
		this.sumImpRimborsoRiscossione = sumImpRimborsoRiscossione;
	}

	public String getDatiStatoDebitorio() {
		return datiStatoDebitorio;
	}

	public void setDatiStatoDebitorio(String datiStatoDebitorio) {
		this.datiStatoDebitorio = datiStatoDebitorio;
	}

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public String getDescPeriodoPagamento() {
		return descPeriodoPagamento;
	}

	public void setDescPeriodoPagamento(String descPeriodoPagamento) {
		this.descPeriodoPagamento = descPeriodoPagamento;
	}

	public String getStatoContribuzione() {
		return statoContribuzione;
	}

	public void setStatoContribuzione(String statoContribuzione) {
		this.statoContribuzione = statoContribuzione;
	}

	public Long getIdStatoContribuzione() {
		return idStatoContribuzione;
	}

	public void setIdStatoContribuzione(Long idStatoContribuzione) {
		this.idStatoContribuzione = idStatoContribuzione;
	}

	public String getIdTitolare() {
		return idTitolare;
	}

	public void setIdTitolare(String idTitolare) {
		this.idTitolare = idTitolare;
	}

	@Override
	public String toString() {
		return "RiscossioneBollDTO [datiRiscossione=" + datiRiscossione + ", idRiscossione=" + idRiscossione
				+ ", idTipoRiscossione=" + idTipoRiscossione + ", idStatoRiscossione=" + idStatoRiscossione
				+ ", idSoggetto=" + idSoggetto + ", idGruppoSoggetto=" + idGruppoSoggetto + ", idTipoAutorizza="
				+ idTipoAutorizza + ", codRiscossione=" + codRiscossione + ", codRiscossioneProv=" + codRiscossioneProv
				+ ", codRiscossioneProg=" + codRiscossioneProg + ", idSiglaRiscossione=" + idSiglaRiscossione
				+ ", codRiscossioneLetteraProv=" + codRiscossioneLetteraProv + ", numPratica=" + numPratica
				+ ", flgPrenotata=" + flgPrenotata + ", motivoPrenotazione=" + motivoPrenotazione + ", noteRiscossione="
				+ noteRiscossione + ", dataIniConcessione=" + dataIniConcessione + ", dataScadConcessione="
				+ dataScadConcessione + ", dataIniSospCanone=" + dataIniSospCanone + ", dataFineSospCanone="
				+ dataFineSospCanone + ", jsonDt=" + jsonDt + ", idComponenteDt=" + idComponenteDt
				+ ", datiTipoRiscossione=" + datiTipoRiscossione + ", codTipoRiscossione=" + codTipoRiscossione
				+ ", desTipoRiscossione=" + desTipoRiscossione + ", datiStatoRiscossione=" + datiStatoRiscossione
				+ ", codStatoRiscossione=" + codStatoRiscossione + ", desStatoRiscossione=" + desStatoRiscossione
				+ ", datiTipoAutorizza=" + datiTipoAutorizza + ", codTipoAutorizza=" + codTipoAutorizza
				+ ", desTipoAutorizza=" + desTipoAutorizza + ", datiAmbito=" + datiAmbito + ", codAmbito=" + codAmbito
				+ ", desAmbito=" + desAmbito + ", datiIndirizzoPrincipale=" + datiIndirizzoPrincipale
				+ ", idRiscossioneP=" + idRiscossioneP + ", idRecapitoP=" + idRecapitoP + ", codTipoRecapitoP="
				+ codTipoRecapitoP + ", desTipoRecapitoP=" + desTipoRecapitoP + ", codTipoInvioP=" + codTipoInvioP
				+ ", desTipoInvioP=" + desTipoInvioP + ", datiIndirizzoAlternativo=" + datiIndirizzoAlternativo
				+ ", idRiscossioneA=" + idRiscossioneA + ", idRecapitoA=" + idRecapitoA + ", codTipoRecapitoA="
				+ codTipoRecapitoA + ", desTipoRecapitoA=" + desTipoRecapitoA + ", codTipoInvioA=" + codTipoInvioA
				+ ", desTipoInvioA=" + desTipoInvioA + ", datiRimborso=" + datiRimborso + ", sumImpRimborsoRiscossione="
				+ sumImpRimborsoRiscossione + ", datiStatoDebitorio=" + datiStatoDebitorio + ", idStatoDebitorio="
				+ idStatoDebitorio + ", descPeriodoPagamento=" + descPeriodoPagamento + ", statoContribuzione="
				+ statoContribuzione + ", idStatoContribuzione=" + idStatoContribuzione + ", idTitolare=" + idTitolare
				+ "]";
	}

}
