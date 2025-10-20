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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ProvinciaExtendedDTO;

public class RiscossioneSearchDTO {

	
	 
	@JsonProperty("cod_utente")
	private String codUtente;
	
	@JsonProperty("id_tipo_utente")
	private Integer idTipoUtente;
	
	@JsonProperty("flag_restituito")
	private boolean flagRestituito;
	
	@JsonProperty("flag_sospesa")
	private boolean flagSospesa;
	
	@JsonProperty("cod_riscossione")
	private String codRiscossione;
	
	@JsonProperty("num_avviso_pagamento")
	private String numAvvisoPagamento;
	
	@JsonProperty("num_avviso")
	private String num_avviso;
	
	@JsonProperty("des_tipo_soggetto")
	private String desTipoSoggetto;

	@JsonProperty("numero_pratica")
	private String numeroPratica;

	@JsonProperty("id_soggetto")
	private Long idSoggetto;

	@JsonProperty("id_gruppo")
	private Long idGruppo;

	@JsonProperty("ragione_sociale")
	private String ragioneSociale;
	
	@JsonProperty("codice_fiscale")
	private String codiceFiscale;
	
	@JsonProperty("partita_iva")
	private String partitaIva;
	
	@JsonProperty("nazione")
	private NazioniDTO nazione;
	
	@JsonProperty("comune_di_residenza")
	private ComuneExtendedDTO comuneDiResidenza;

	@JsonProperty("provincia")
	private ProvinciaExtendedDTO provincia;
	
	@JsonProperty("provincia_di_competenza")
	private ProvinciaExtendedDTO provinciaDiCompetenza;
	
	@JsonProperty("indirizzo")
	private String indirizzo;
	
	@JsonProperty("id_tipo_titolo")
	private Long idTipoTitolo;

	@JsonProperty("id_tipo_provvedimento")
	private Long idTipoProvvedimento;

	@JsonProperty("numero_titolo")
	private String numeroTitolo;
	
	@JsonProperty("data_titolo_da")
	private String dataTitoloDa;
	
	@JsonProperty("data_titolo_a")
	private String dataTitoloA;
	
	@JsonProperty("id_stato_riscossione")
	private Long idStatoRiscossione;
	
	@JsonProperty("scadenza_concessione_da")
	private String scadenzaConcessioneDa;

	@JsonProperty("scadenza_concessione_a")
	private String scadenzaConcessioneA;
	
	@JsonProperty("data_rinuncia_revoca_da")
	private String dataRinunciaRevocaDa;
	
	@JsonProperty("data_rinuncia_revoca_a")
	private String dataRinunciaRevocaA;
	
	@JsonProperty("provvedimenti")
	private List<ProvvedimentoSearchDTO> provvedimenti;
	
	@JsonProperty("istanze")
	private List<TipoIstanzaDTO> istanze;

	@JsonProperty("dati_tecnici")
	private String datiTecnici;

	@JsonProperty("nap")
	private String nap;
	
	@JsonProperty("codice_avviso")
	private String codiceAvviso;
	
	@JsonProperty("pratica_sospesa")
	private Integer praticaSospesa;
	
	@JsonProperty("anno_canone")
	private Integer annoCanone;
	
	@JsonProperty("canone")
	private BigDecimal canone;
	
	@JsonProperty("restituito_al_mittente")
	private Integer restituitoAlMittente;
	
	@JsonProperty("citta_estera_nascita")
	private String cittaEsteraNascita;
	
	@JsonProperty("id_ambito")
	private Long idAmbito;
	
	@JsonProperty("id_tipo_riscossione")
	private Long idTipoRiscossione;
	
	private String competenzaTerritoriale;
	
	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodRiscossione() {
		return codRiscossione;
	}

	public void setCodRiscossione(String codRiscossione) {
		this.codRiscossione = codRiscossione;
	}

	public String getDesTipoSoggetto() {
		return desTipoSoggetto;
	}

	public void setDesTipoSoggetto(String desTipoSoggetto) {
		this.desTipoSoggetto = desTipoSoggetto;
	}

	public String getNumeroPratica() {
		return numeroPratica;
	}

	public void setNumeroPratica(String numeroPratica) {
		this.numeroPratica = numeroPratica;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdGruppo() {
		return idGruppo;
	}

	public void setIdGruppo(Long idGruppo) {
		this.idGruppo = idGruppo;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public NazioniDTO getNazione() {
		return nazione;
	}

	public void setNazione(NazioniDTO nazione) {
		this.nazione = nazione;
	}

	public ComuneExtendedDTO getComuneDiResidenza() {
		return comuneDiResidenza;
	}

	public void setComuneDiResidenza(ComuneExtendedDTO comuneDiResidenza) {
		this.comuneDiResidenza = comuneDiResidenza;
	}


	public ProvinciaExtendedDTO getProvinciaDiCompetenza() {
		return provinciaDiCompetenza;
	}

	public void setProvinciaDiCompetenza(ProvinciaExtendedDTO provinciaDiCompetenza) {
		this.provinciaDiCompetenza = provinciaDiCompetenza;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Long getIdTipoTitolo() {
		return idTipoTitolo;
	}

	public void setIdTipoTitolo(Long idTipoTitolo) {
		this.idTipoTitolo = idTipoTitolo;
	}

	public Long getIdTipoProvvedimento() {
		return idTipoProvvedimento;
	}

	public void setIdTipoProvvedimento(Long idTipoProvvedimento) {
		this.idTipoProvvedimento = idTipoProvvedimento;
	}

	public String getNumeroTitolo() {
		return numeroTitolo;
	}

	public void setNumeroTitolo(String numeroTitolo) {
		this.numeroTitolo = numeroTitolo;
	}

	public String getDataTitoloDa() {
		return dataTitoloDa;
	}

	public void setDataTitoloDa(String dataTitoloDa) {
		this.dataTitoloDa = dataTitoloDa;
	}

	public String getDataTitoloA() {
		return dataTitoloA;
	}

	public void setDataTitoloA(String dataTitoloA) {
		this.dataTitoloA = dataTitoloA;
	}

	public Long getIdStatoRiscossione() {
		return idStatoRiscossione;
	}

	public void setIdStatoRiscossione(Long idStatoRiscossione) {
		this.idStatoRiscossione = idStatoRiscossione;
	}
	
	public String getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String codUtente) {
		this.codUtente = codUtente;
	}

	public boolean isFlagRestituito() {
		return flagRestituito;
	}

	public void setFlagRestituito(boolean flagRestituito) {
		this.flagRestituito = flagRestituito;
	}

	public boolean isFlagSospesa() {
		return flagSospesa;
	}

	public void setFlagSospesa(boolean flagSospesa) {
		this.flagSospesa = flagSospesa;
	}

	public String getScadenzaConcessioneDa() {
		return scadenzaConcessioneDa;
	}

	public void setScadenzaConcessioneDa(String scadenzaConcessioneDa) {
		this.scadenzaConcessioneDa = scadenzaConcessioneDa;
	}

	public String getScadenzaConcessioneA() {
		return scadenzaConcessioneA;
	}

	public void setScadenzaConcessioneA(String scadenzaConcessioneA) {
		this.scadenzaConcessioneA = scadenzaConcessioneA;
	}


	public String getDataRinunciaRevocaDa() {
		return dataRinunciaRevocaDa;
	}

	public void setDataRinunciaRevocaDa(String dataRinunciaRevocaDa) {
		this.dataRinunciaRevocaDa = dataRinunciaRevocaDa;
	}

	public String getDataRinunciaRevocaA() {
		return dataRinunciaRevocaA;
	}

	public void setDataRinunciaRevocaA(String dataRinunciaRevocaA) {
		this.dataRinunciaRevocaA = dataRinunciaRevocaA;
	}

	public List<ProvvedimentoSearchDTO> getProvvedimenti() {
		return provvedimenti;
	}

	public void setProvvedimenti(List<ProvvedimentoSearchDTO> provvedimenti) {
		this.provvedimenti = provvedimenti;
	}

	public List<TipoIstanzaDTO> getIstanze() {
		return istanze;
	}

	public void setIstanze(List<TipoIstanzaDTO> istanze) {
		this.istanze = istanze;
	}

	public String getDatiTecnici() {
		return datiTecnici;
	}

	public void setDatiTecnici(String datiTecnici) {
		this.datiTecnici = datiTecnici;
	}

	public String getNumAvvisoPagamento() {
		return numAvvisoPagamento;
	}

	public void setNumAvvisoPagamento(String numAvvisoPagamento) {
		this.numAvvisoPagamento = numAvvisoPagamento;
	}

	public String getNum_avviso() {
		return num_avviso;
	}

	public void setNum_avviso(String num_avviso) {
		this.num_avviso = num_avviso;
	}

	public Integer getIdTipoUtente() {
		return idTipoUtente;
	}

	public void setIdTipoUtente(Integer idTipoUtente) {
		this.idTipoUtente = idTipoUtente;
	}

	public ProvinciaExtendedDTO getProvincia() {
		return provincia;
	}

	public void setProvincia(ProvinciaExtendedDTO provincia) {
		this.provincia = provincia;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public String getCodiceAvviso() {
		return codiceAvviso;
	}

	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}

	public Integer getPraticaSospesa() {
		return praticaSospesa;
	}

	public void setPraticaSospesa(Integer praticaSospesa) {
		this.praticaSospesa = praticaSospesa;
	}

	public Integer getAnnoCanone() {
		return annoCanone;
	}

	public void setAnnoCanone(Integer annoCanone) {
		this.annoCanone = annoCanone;
	}

	public BigDecimal getCanone() {
		return canone;
	}

	public void setCanone(BigDecimal canone) {
		this.canone = canone;
	}

	public Integer getRestituitoAlMittente() {
		return restituitoAlMittente;
	}

	public void setRestituitoAlMittente(Integer restituitoAlMittente) {
		this.restituitoAlMittente = restituitoAlMittente;
	}

	public String getCittaEsteraNascita() {
		return cittaEsteraNascita;
	}

	public void setCittaEsteraNascita(String cittaEsteraNascita) {
		this.cittaEsteraNascita = cittaEsteraNascita;
	}

	public String getCompetenzaTerritoriale() {
		return competenzaTerritoriale;
	}

	public void setCompetenzaTerritoriale(String competenzaTerritoriale) {
		this.competenzaTerritoriale = competenzaTerritoriale;
	}

	public Long getIdTipoRiscossione() {
		return idTipoRiscossione;
	}

	public void setIdTipoRiscossione(Long idTipoRiscossione) {
		this.idTipoRiscossione = idTipoRiscossione;
	}
	

}
