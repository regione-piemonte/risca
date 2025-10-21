/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UnitaMisuraDTO
 *
 * @author CSI PIEMONTE
 */

public class RiduzioneAumentoDTO {
	
	@JsonProperty("id_riduzione_aumento")
    private Long idRiduzioneAumento;

    @JsonProperty("sigla_riduzione_aumento")
    private String siglaRiduzioneAumento;
    
    @JsonProperty("des_riduzione_aumento")
    private String desRiduzioneAumento;
    
    @JsonProperty("perc_riduzione_aumento")
    private Long percRiduzioneAumento;
    
    @JsonProperty("flg_riduzione_aumento")
    private String flgRiduzioneAumento;
    
    @JsonProperty("flg_manuale")
    private String flgManuale;
    
    @JsonProperty("flg_da_applicare")
    private String flgDaApplicare;  

    @JsonProperty("ordina_riduzione_aumento")
    private Long ordinaRiduzioneAumento;
    
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita ;

    @JsonProperty("data_inizio_validita")
    private Date dataInizioValidita ;

	public Long getIdRiduzioneAumento() {
		return idRiduzioneAumento;
	}

	public void setIdRiduzioneAumento(Long idRiduzioneAumento) {
		this.idRiduzioneAumento = idRiduzioneAumento;
	}

	public String getSiglaRiduzioneAumento() {
		return siglaRiduzioneAumento;
	}

	public void setSiglaRiduzioneAumento(String siglaRiduzioneAumento) {
		this.siglaRiduzioneAumento = siglaRiduzioneAumento;
	}

	public String getDesRiduzioneAumento() {
		return desRiduzioneAumento;
	}

	public void setDesRiduzioneAumento(String desRiduzioneAumento) {
		this.desRiduzioneAumento = desRiduzioneAumento;
	}

	public Long getPercRiduzioneAumento() {
		return percRiduzioneAumento;
	}

	public void setPercRiduzioneAumento(Long percRiduzioneAumento) {
		this.percRiduzioneAumento = percRiduzioneAumento;
	}

	public String getFlgRiduzioneAumento() {
		return flgRiduzioneAumento;
	}

	public void setFlgRiduzioneAumento(String flgRiduzioneAumento) {
		this.flgRiduzioneAumento = flgRiduzioneAumento;
	}

	public String getFlgManuale() {
		return flgManuale;
	}

	public void setFlgManuale(String flgManuale) {
		this.flgManuale = flgManuale;
	}

	public String getFlgDaApplicare() {
		return flgDaApplicare;
	}

	public void setFlgDaApplicare(String flgDaApplicare) {
		this.flgDaApplicare = flgDaApplicare;
	}

	public Long getOrdinaRiduzioneAumento() {
		return ordinaRiduzioneAumento;
	}

	public void setOrdinaRiduzioneAumento(Long ordinaRiduzioneAumento) {
		this.ordinaRiduzioneAumento = ordinaRiduzioneAumento;
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
	
}
