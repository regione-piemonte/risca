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
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RiscossioneDTO
 *
 * @author CSI PIEMONTE
 */
public class RiscossioneDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L'ID di riscossione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L'ID di riscossione supera il limite massimo consentito per Integer")
	@JsonProperty("id_riscossione")
    private Long idRiscossione;

    @Min(value = 1, message = "L'id_tipo_riscossione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L'id_tipo_riscossione supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_riscossione")
    private Long idTipoRiscossione;
    
	@JsonProperty("stato_riscossione")
	private StatiRiscossioneDTO statiRiscossione;
	
	@JsonIgnore
    private Long idStatoRiscossione;
	
	@JsonProperty("soggetto")
    private SoggettiExtendedDTO soggetto;
	
	@JsonIgnore
    private Long idSoggetto;
	
	@JsonProperty("gruppo_soggetto")
    private GruppiDTO gruppoSoggetto;
	
	@JsonIgnore
    private Long idGruppoSoggetto;
	
	@JsonProperty("tipo_autorizza")
    private TipiAutorizzazioneExtendedDTO tipoAutorizza;
	
	@JsonIgnore
    private Long idTipoAutorizza;
	
    @Size(max = 40, min = 0, message = "cod_riscossione deve essere compreso tra 0 e 30 caratteri.")
	@JsonProperty("cod_riscossione")
    private String codRiscossione;
    
    @Size(max = 2, min = 0, message = "cod_riscossione_prov deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("cod_riscossione_prov")
    private String codRiscossioneProv;
    
    @Size(max = 5, min = 0, message = "cod_riscossione_prog deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("cod_riscossione_prog")
    private String codRiscossioneProg;
    
    @Min(value = 1, message = "L' id_sigla_riscossione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_sigla_riscossione supera il limite massimo consentito per Integer")
	@JsonProperty("id_sigla_riscossione")
    private Long idSiglaRiscossione;
	
    @Size(max = 1, min = 0, message = "cod_riscossione_lettera_prov deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("cod_riscossione_lettera_prov")
    private String codRiscossioneLetteraProv;
    
    @Size(max = 40, min = 0, message = "num_pratica deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("num_pratica")
    private String numPratica;
    
    @Min(value = 0, message = "Il flg_prenotata deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_prenotata deve essere al massimo 1.")
    @JsonProperty("flg_prenotata")
    private int flgPrenotata;
    
    @Size(max = 200, min = 0, message = "motivo_prenotazione deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("motivo_prenotazione")
    private String motivoPrenotazione;
    
    @Size(max = 500, min = 0, message = "note_riscossione deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("note_riscossione")
    private String noteRiscossione;
    
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2})$", message = "Formato data_ini_concessione non valido. Utilizzare yyyy-MM-dd.")
	@JsonProperty("data_ini_concessione")
    private String dataIniConcessione;
	
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2})$", message = "Formato data_scad_concessione non valido. Utilizzare yyyy-MM-dd.")
	@JsonProperty("data_scad_concessione")
    private String dataScadConcessione;
	
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2})$", message = "Formato data_ini_sosp_canone non valido. Utilizzare yyyy-MM-dd.")
	@JsonProperty("data_ini_sosp_canone")
    private String dataIniSospCanone;
	
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2})$", message = "Formato data_fine_sosp_canone non valido. Utilizzare yyyy-MM-dd.")
	@JsonProperty("data_fine_sosp_canone")
    private String dataFineSospCanone;
	
	@JsonProperty("json_dt")
    private String jsonDt;
	
	@JsonProperty("recapiti_riscossione")
	private List<RecapitiExtendedDTO> recapitiRiscossione;
	
    @Min(value = 1, message = "L' id_componente_dt deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_componente_dt supera il limite massimo consentito per Integer")
	@JsonProperty("id_componente_dt")
    private Long idComponenteDt;
	
	@JsonProperty("provvedimentoIstanza")
	private List<ProvvedimentoDTO> provvedimento;

	@JsonProperty("riscossione_recapito")
	private List<RiscossioneRecapitoDTO> riscossioneRecapito;

	@JsonProperty("ambito")
	private String ambito;
	
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2})$", message = "Formato data_inizio_titolarita non valido. Utilizzare yyyy-MM-dd.")
	@JsonProperty("data_inizio_titolarita")
    private String dataInizioTitolarita;
	
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2})$", message = "Formato data_rinuncia_revoca non valido. Utilizzare yyyy-MM-dd.")
	@JsonProperty("data_rinuncia_revoca")
    private String dataRinunciaRevoca;
	

	public String getDataRinunciaRevoca() {
		return dataRinunciaRevoca;
	}

	public void setDataRinunciaRevoca(String dataRinunciaRevoca) {
		this.dataRinunciaRevoca = dataRinunciaRevoca;
	}

	public void setRecapitiRiscossione(List<RecapitiExtendedDTO> recapitiRiscossione) {
		this.recapitiRiscossione = recapitiRiscossione;
	}

	public String getDataInizioTitolarita() {
		return dataInizioTitolarita;
	}

	public void setDataInizioTitolarita(String dataInizioTitolarita) {
		this.dataInizioTitolarita = dataInizioTitolarita;
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

    public Long getIdComponenteDt() {
		return idComponenteDt;
	}

	public void setIdComponenteDt(Long idComponenteDt) {
		this.idComponenteDt = idComponenteDt;
	}

	public Long getIdStatoRiscossione() {
		return idStatoRiscossione;
	}

	public void setIdStatoRiscossione(Long idStatoRiscossione) {
		this.idStatoRiscossione = idStatoRiscossione;
	}

	public SoggettiExtendedDTO getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(SoggettiExtendedDTO soggetto) {
		this.soggetto = soggetto;
	}

	public GruppiDTO getGruppoSoggetto() {
		return gruppoSoggetto;
	}

	public void setGruppoSoggetto(GruppiDTO gruppoSoggetto) {
		this.gruppoSoggetto = gruppoSoggetto;
	}

	public TipiAutorizzazioneExtendedDTO getTipoAutorizza() {
		return tipoAutorizza;
	}

	public void setTipoAutorizza(TipiAutorizzazioneExtendedDTO tipoAutorizza) {
		this.tipoAutorizza = tipoAutorizza;
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

	public String getDataIniConcessione() {
		return dataIniConcessione;
	}

	public void setDataIniConcessione(String dataIniConcessione) {
		this.dataIniConcessione = dataIniConcessione;
	}

	public String getDataScadConcessione() {
		return dataScadConcessione;
	}

	public void setDataScadConcessione(String dataScadConcessione) {
		this.dataScadConcessione = dataScadConcessione;
	}

	public String getDataIniSospCanone() {
		return dataIniSospCanone;
	}

	public void setDataIniSospCanone(String dataIniSospCanone) {
		this.dataIniSospCanone = dataIniSospCanone;
	}

	public String getDataFineSospCanone() {
		return dataFineSospCanone;
	}

	public void setDataFineSospCanone(String dataFineSospCanone) {
		this.dataFineSospCanone = dataFineSospCanone;
	}

	public String getJsonDt() {
		return jsonDt;
	}

	public void setJsonDt(String jsonDt) {
		this.jsonDt = jsonDt;
	}

	
	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}
	
	public List<ProvvedimentoDTO> getProvvedimento() {
		return provvedimento;
	}

	public void setProvvedimento(List<ProvvedimentoDTO> provvedimento) {
		this.provvedimento = provvedimento;
	}

	public List<RiscossioneRecapitoDTO> getRiscossioneRecapito() {
		return riscossioneRecapito;
	}

	public void setRiscossioneRecapito(List<RiscossioneRecapitoDTO> riscossioneRecapito) {
		this.riscossioneRecapito = riscossioneRecapito;
	}
	
	public List<RecapitiExtendedDTO> getRecapitiRiscossione() {
		return recapitiRiscossione;
	}

	public void setRecapiti(List<RecapitiExtendedDTO> recapitiRiscossione) {
		this.recapitiRiscossione = recapitiRiscossione;
	}
	
	public StatiRiscossioneDTO getStatiRiscossione() {
		return statiRiscossione;
	}

	public void setStatiRiscossione(StatiRiscossioneDTO statiRiscossione) {
		this.statiRiscossione = statiRiscossione;
	}
}
