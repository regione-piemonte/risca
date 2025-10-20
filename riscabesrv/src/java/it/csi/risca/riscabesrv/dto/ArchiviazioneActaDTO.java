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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ArchiviazioneActaDTO
 *
 * @author CSI PIEMONTE
 */
public class ArchiviazioneActaDTO {

	@JsonProperty("id_ambito")
	private Long idAmbito;

	@JsonProperty("cod_ambito")
	private String codAmbito;

	@JsonProperty("id_spedizione")
	private Long idSpedizione;

	@JsonProperty("id_tipo_spedizione")
	private Long idTipoSpedizione;

	@JsonProperty("id_elabora_spedizione")
	private Long idElaboraSpedizione;

	@JsonProperty("cod_tipo_elabora")
	private String codTipoElabora;

	@JsonProperty("data_protocollo")
	private Date dataProtocollo;

	@JsonProperty("data_scadenza")
	private Date dataScadenza;

	@JsonProperty("anno")
	private Integer anno;

	@JsonProperty("cod_tipo_spedizione")
	private String codTipoSpedizione;

	@JsonProperty("id_spedizione_acta")
	private Long idSpedizioneActa;

	@JsonProperty("id_elabora_spedizione_acta")
	private Long idElaboraSpedizioneActa;

	@JsonProperty("nome_dirigente_protempore")
	private String nomeDirigenteProtempore;

	@JsonProperty("id_accertamento")
	private Long idAccertamento;

	@JsonProperty("cod_tipo_accertamento")
	private String codTipoAccertamento;

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("num_protocollo")
	private String numProtocollo;

	@JsonProperty("id_soggetto")
	private Long idSoggetto;

	@JsonProperty("cf_soggetto")
	private String cfSoggetto;

	@JsonProperty("nome")
	private String nome;

	@JsonProperty("cognome")
	private String cognome;

	@JsonProperty("den_soggetto")
	private String denSoggetto;

	@JsonProperty("id_tipo_soggetto")
	private Long idTipoSoggetto;

	@JsonProperty("cod_tipo_soggetto")
	private String codTipoSoggetto;

	@JsonProperty("codice_utenza")
	private String codiceUtenza;

	@JsonProperty("id_invio_acta")
	private Long idInvioActa;

	@JsonProperty("flg_multiclassificazione")
	private int flgMulticlassificazione;

	@JsonProperty("flg_archiviata_acta")
	private int flgArchiviataActa;

	@JsonProperty("data_invio")
	private Date dataInvio;

	@JsonProperty("cod_esito_invio_acta")
	private String codEsitoInvioActa;

	@JsonProperty("cod_esito_acquisizione_acta")
	private String codEsitoAcquisizioneActa;

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodAmbito() {
		return codAmbito;
	}

	public void setCodAmbito(String codAmbito) {
		this.codAmbito = codAmbito;
	}

	public Long getIdSpedizione() {
		return idSpedizione;
	}

	public void setIdSpedizione(Long idSpedizione) {
		this.idSpedizione = idSpedizione;
	}

	public Long getIdTipoSpedizione() {
		return idTipoSpedizione;
	}

	public void setIdTipoSpedizione(Long idTipoSpedizione) {
		this.idTipoSpedizione = idTipoSpedizione;
	}

	public Long getIdElaboraSpedizione() {
		return idElaboraSpedizione;
	}

	public void setIdElaboraSpedizione(Long idElaboraSpedizione) {
		this.idElaboraSpedizione = idElaboraSpedizione;
	}

	public String getCodTipoElabora() {
		return codTipoElabora;
	}

	public void setCodTipoElabora(String codTipoElabora) {
		this.codTipoElabora = codTipoElabora;
	}

	public Date getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getCodTipoSpedizione() {
		return codTipoSpedizione;
	}

	public void setCodTipoSpedizione(String codTipoSpedizione) {
		this.codTipoSpedizione = codTipoSpedizione;
	}

	public Long getIdSpedizioneActa() {
		return idSpedizioneActa;
	}

	public void setIdSpedizioneActa(Long idSpedizioneActa) {
		this.idSpedizioneActa = idSpedizioneActa;
	}

	public Long getIdElaboraSpedizioneActa() {
		return idElaboraSpedizioneActa;
	}

	public void setIdElaboraSpedizioneActa(Long idElaboraSpedizioneActa) {
		this.idElaboraSpedizioneActa = idElaboraSpedizioneActa;
	}

	public String getNomeDirigenteProtempore() {
		return nomeDirigenteProtempore;
	}

	public void setNomeDirigenteProtempore(String nomeDirigenteProtempore) {
		this.nomeDirigenteProtempore = nomeDirigenteProtempore;
	}

	public Long getIdAccertamento() {
		return idAccertamento;
	}

	public void setIdAccertamento(Long idAccertamento) {
		this.idAccertamento = idAccertamento;
	}

	public String getCodTipoAccertamento() {
		return codTipoAccertamento;
	}

	public void setCodTipoAccertamento(String codTipoAccertamento) {
		this.codTipoAccertamento = codTipoAccertamento;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public String getNumProtocollo() {
		return numProtocollo;
	}

	public void setNumProtocollo(String numProtocollo) {
		this.numProtocollo = numProtocollo;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public String getCfSoggetto() {
		return cfSoggetto;
	}

	public void setCfSoggetto(String cfSoggetto) {
		this.cfSoggetto = cfSoggetto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getDenSoggetto() {
		return denSoggetto;
	}

	public void setDenSoggetto(String denSoggetto) {
		this.denSoggetto = denSoggetto;
	}

	public Long getIdTipoSoggetto() {
		return idTipoSoggetto;
	}

	public void setIdTipoSoggetto(Long idTipoSoggetto) {
		this.idTipoSoggetto = idTipoSoggetto;
	}

	public String getCodTipoSoggetto() {
		return codTipoSoggetto;
	}

	public void setCodTipoSoggetto(String codTipoSoggetto) {
		this.codTipoSoggetto = codTipoSoggetto;
	}

	public String getCodiceUtenza() {
		return codiceUtenza;
	}

	public void setCodiceUtenza(String codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}

	public Long getIdInvioActa() {
		return idInvioActa;
	}

	public void setIdInvioActa(Long idInvioActa) {
		this.idInvioActa = idInvioActa;
	}

	public int getFlgMulticlassificazione() {
		return flgMulticlassificazione;
	}

	public void setFlgMulticlassificazione(int flgMulticlassificazione) {
		this.flgMulticlassificazione = flgMulticlassificazione;
	}

	public int getFlgArchiviataActa() {
		return flgArchiviataActa;
	}

	public void setFlgArchiviataActa(int flgArchiviataActa) {
		this.flgArchiviataActa = flgArchiviataActa;
	}

	public Date getDataInvio() {
		return dataInvio;
	}

	public void setDataInvio(Date dataInvio) {
		this.dataInvio = dataInvio;
	}

	public String getCodEsitoInvioActa() {
		return codEsitoInvioActa;
	}

	public void setCodEsitoInvioActa(String codEsitoInvioActa) {
		this.codEsitoInvioActa = codEsitoInvioActa;
	}

	public String getCodEsitoAcquisizioneActa() {
		return codEsitoAcquisizioneActa;
	}

	public void setCodEsitoAcquisizioneActa(String codEsitoAcquisizioneActa) {
		this.codEsitoAcquisizioneActa = codEsitoAcquisizioneActa;
	}

}
