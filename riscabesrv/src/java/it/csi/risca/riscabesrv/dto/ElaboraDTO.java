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
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElaboraDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_dettaglio_pag deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_dettaglio_pag supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;

	@JsonProperty("ambito")
	private AmbitoDTO ambito;

	@JsonProperty("tipo_elabora")
	private TipoElaboraExtendedDTO tipoElabora;

	@JsonProperty("stato_elabora")
	private StatoElaborazioneDTO statoElabora;

	@JsonProperty("data_richiesta")
	private Date dataRichiesta;

    @Size(max = 500, min = 0, message = "nome_file_generato deve essere compreso tra 0 e 500 caratteri.")
	@JsonProperty("nome_file_generato")
	private String nomeFileGenerato = null;

	@JsonProperty("parametri")
	private List<ParametroElaboraDTO> parametri;

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public AmbitoDTO getAmbito() {
		return ambito;
	}

	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}

	public TipoElaboraDTO getTipoElabora() {
		return tipoElabora;
	}

	public void setTipoElabora(TipoElaboraExtendedDTO tipoElabora) {
		this.tipoElabora = tipoElabora;
	}

	public StatoElaborazioneDTO getStatoElabora() {
		return statoElabora;
	}

	public void setStatoElabora(StatoElaborazioneDTO statoElabora) {
		this.statoElabora = statoElabora;
	}

	public Date getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}


	public List<ParametroElaboraDTO> getParametri() {
		return parametri;
	}

	public void setParametri(List<ParametroElaboraDTO> parametri) {
		this.parametri = parametri;
	}

	public String getNomeFileGenerato() {
		return nomeFileGenerato;
	}

	public void setNomeFileGenerato(String nomeFileGenerato) {
		this.nomeFileGenerato = nomeFileGenerato;
	}

}
