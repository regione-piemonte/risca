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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UnitaMisuraDTO
 *
 * @author CSI PIEMONTE
 */

public class RiduzioneAumentoDTO {
	
    @Min(value = 1, message = "L' id_riduzione_aumento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_riduzione_aumento supera il limite massimo consentito per Integer")
	@JsonProperty("id_riduzione_aumento")
    private Long idRiduzioneAumento;

    @Size(max = 3, min = 0, message = "sigla_riduzione_aumento deve essere compreso tra 0 e 3 caratteri. ")
    @JsonProperty("sigla_riduzione_aumento")
    private String siglaRiduzioneAumento;
    
    @Size(max = 100, min = 0, message = "des_riduzione_aumento deve essere compreso tra 0 e 100 caratteri. ")
    @JsonProperty("des_riduzione_aumento")
    private String desRiduzioneAumento;
    
    @Min(value = 1, message = "perc_riduzione_aumento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "perc_riduzione_aumento supera il limite massimo consentito per Integer")
    @JsonProperty("perc_riduzione_aumento")
    private Long percRiduzioneAumento;
    
    @Min(value = 0, message = "Il flg_default deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_default deve essere al massimo 1.")
    @JsonProperty("flg_riduzione_aumento")
    private String flgRiduzioneAumento;
    
    @Min(value = 0, message = "Il flg_default deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_default deve essere al massimo 1.")
    @JsonProperty("flg_manuale")
    private String flgManuale;
    
    @Min(value = 0, message = "Il flg_default deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_default deve essere al massimo 1.")
    @JsonProperty("flg_da_applicare")
    private String flgDaApplicare;  

    @Min(value = 1, message = "L' ordina_riduzione_aumento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' ordina_riduzione_aumento supera il limite massimo consentito per Integer")
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
