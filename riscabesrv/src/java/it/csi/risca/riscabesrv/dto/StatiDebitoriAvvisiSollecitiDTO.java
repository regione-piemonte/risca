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

public class StatiDebitoriAvvisiSollecitiDTO extends GestAttoreDTO {

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("desc_periodo_pagamento")
	private String descPeriodoPagamento;

	@JsonProperty("id_attivita_stato_deb")
	private Long idAttivitaStatoDeb;

	@JsonProperty("id_soggetto_sd")
	private Long idSoggettoSd;

	@JsonProperty("id_gruppo_soggetto_sd")
	private Long idGruppoSoggettoSd;
	
	@JsonProperty("id_titolare")
	private String idTitolare;

	@JsonProperty("num_titolo")
	private String numTitolo;

	@JsonProperty("data_provvedimento")
	private Date dataProvvedimento;

	@JsonProperty("flg_dilazione")
	private int flgDilazione;

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("cod_ambito")
	private String codAmbito;

	@JsonProperty("des_ambito")
	private String desAmbito;

	@JsonProperty("des_tipo_titolo")
	private String desTipoTitolo;

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

	@JsonProperty("cf_soggetto")
	private String cfSoggetto;

	@JsonProperty("id_tipo_soggetto")
	private Long idTipoSoggetto;

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

	@JsonProperty("cod_tipo_sede_p")
	private String codTipoSedeP;

	@JsonProperty("des_tipo_sede_p")
	private String desTipoSedeP;

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

	@JsonProperty("cod_tipo_sede_a")
	private String codTipoSedeA;

	@JsonProperty("des_tipo_sede_a")
	private String desTipoSedeA;

	@JsonProperty("somma_importo_versato")
	private BigDecimal sommaImportoVersato;

	@JsonProperty("id_rata_sd")
	private Long idRataSd;

	@JsonProperty("data_scadenza_pagamento")
	private Date dataScadenzaPagamento;

	@JsonProperty("annualita_di_pagamento")
	private String annualitaDiPagamento;

	@JsonProperty("canone_dovuto")
	private BigDecimal canoneDovuto;

	@JsonProperty("rata_interessi_maturati")
	private BigDecimal rataInteressiMaturati;

	@JsonProperty("importo_versato")
	private BigDecimal importoVersato;

	@JsonProperty("importo_rimborsato")
	private BigDecimal importoRimborsato;

	@JsonProperty("dett_pag_imp_vers")
	private BigDecimal dettPagImpVers;

	@JsonProperty("interessi_maturati")
	private BigDecimal interessiMaturati;

	@JsonProperty("rata_pag")
	private Long rataPag;

	@JsonProperty("id_pagamento")
	private Long idPagamento;

	@JsonProperty("data_versamento_dett")
	private Date dataVersamentoDett;

	@JsonProperty("giorni_ritardo")
	private int giorniRitardo;

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

	public Long getIdAttivitaStatoDeb() {
		return idAttivitaStatoDeb;
	}

	public void setIdAttivitaStatoDeb(Long idAttivitaStatoDeb) {
		this.idAttivitaStatoDeb = idAttivitaStatoDeb;
	}

	public Long getIdSoggettoSd() {
		return idSoggettoSd;
	}

	public void setIdSoggettoSd(Long idSoggettoSd) {
		this.idSoggettoSd = idSoggettoSd;
	}

	public Long getIdGruppoSoggettoSd() {
		return idGruppoSoggettoSd;
	}

	public void setIdGruppoSoggettoSd(Long idGruppoSoggettoSd) {
		this.idGruppoSoggettoSd = idGruppoSoggettoSd;
	}

	public String getNumTitolo() {
		return numTitolo;
	}

	public void setNumTitolo(String numTitolo) {
		this.numTitolo = numTitolo;
	}

	public Date getDataProvvedimento() {
		return dataProvvedimento;
	}

	public void setDataProvvedimento(Date dataProvvedimento) {
		this.dataProvvedimento = dataProvvedimento;
	}

	public int getFlgDilazione() {
		return flgDilazione;
	}

	public void setFlgDilazione(int flgDilazione) {
		this.flgDilazione = flgDilazione;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
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

	public String getDesTipoTitolo() {
		return desTipoTitolo;
	}

	public void setDesTipoTitolo(String desTipoTitolo) {
		this.desTipoTitolo = desTipoTitolo;
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

	public String getIdTitolare() {
		return idTitolare;
	}

	public void setIdTitolare(String idTitolare) {
		this.idTitolare = idTitolare;
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

	public String getCfSoggetto() {
		return cfSoggetto;
	}

	public void setCfSoggetto(String cfSoggetto) {
		this.cfSoggetto = cfSoggetto;
	}

	public Long getIdTipoSoggetto() {
		return idTipoSoggetto;
	}

	public void setIdTipoSoggetto(Long idTipoSoggetto) {
		this.idTipoSoggetto = idTipoSoggetto;
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

	public String getCodTipoSedeP() {
		return codTipoSedeP;
	}

	public void setCodTipoSedeP(String codTipoSedeP) {
		this.codTipoSedeP = codTipoSedeP;
	}

	public String getDesTipoSedeP() {
		return desTipoSedeP;
	}

	public void setDesTipoSedeP(String desTipoSedeP) {
		this.desTipoSedeP = desTipoSedeP;
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

	public String getCodTipoSedeA() {
		return codTipoSedeA;
	}

	public void setCodTipoSedeA(String codTipoSedeA) {
		this.codTipoSedeA = codTipoSedeA;
	}

	public String getDesTipoSedeA() {
		return desTipoSedeA;
	}

	public void setDesTipoSedeA(String desTipoSedeA) {
		this.desTipoSedeA = desTipoSedeA;
	}

	public BigDecimal getSommaImportoVersato() {
		return sommaImportoVersato;
	}

	public void setSommaImportoVersato(BigDecimal sommaImportoVersato) {
		this.sommaImportoVersato = sommaImportoVersato;
	}

	public Long getIdRataSd() {
		return idRataSd;
	}

	public void setIdRataSd(Long idRataSd) {
		this.idRataSd = idRataSd;
	}

	public Date getDataScadenzaPagamento() {
		return dataScadenzaPagamento;
	}

	public void setDataScadenzaPagamento(Date dataScadenzaPagamento) {
		this.dataScadenzaPagamento = dataScadenzaPagamento;
	}

	public String getAnnualitaDiPagamento() {
		return annualitaDiPagamento;
	}

	public void setAnnualitaDiPagamento(String annualitaDiPagamento) {
		this.annualitaDiPagamento = annualitaDiPagamento;
	}

	public BigDecimal getCanoneDovuto() {
		return canoneDovuto;
	}

	public void setCanoneDovuto(BigDecimal canoneDovuto) {
		this.canoneDovuto = canoneDovuto;
	}

	public BigDecimal getRataInteressiMaturati() {
		return rataInteressiMaturati;
	}

	public void setRataInteressiMaturati(BigDecimal rataInteressiMaturati) {
		this.rataInteressiMaturati = rataInteressiMaturati;
	}

	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public BigDecimal getImportoRimborsato() {
		return importoRimborsato;
	}

	public void setImportoRimborsato(BigDecimal importoRimborsato) {
		this.importoRimborsato = importoRimborsato;
	}

	public BigDecimal getDettPagImpVers() {
		return dettPagImpVers;
	}

	public void setDettPagImpVers(BigDecimal dettPagImpVers) {
		this.dettPagImpVers = dettPagImpVers;
	}

	public BigDecimal getInteressiMaturati() {
		return interessiMaturati;
	}

	public void setInteressiMaturati(BigDecimal interessiMaturati) {
		this.interessiMaturati = interessiMaturati;
	}

	public Long getRataPag() {
		return rataPag;
	}

	public void setRataPag(Long rataPag) {
		this.rataPag = rataPag;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Date getDataVersamentoDett() {
		return dataVersamentoDett;
	}

	public void setDataVersamentoDett(Date dataVersamentoDett) {
		this.dataVersamentoDett = dataVersamentoDett;
	}

	public int getGiorniRitardo() {
		return giorniRitardo;
	}

	public void setGiorniRitardo(int giorniRitardo) {
		this.giorniRitardo = giorniRitardo;
	}

}
