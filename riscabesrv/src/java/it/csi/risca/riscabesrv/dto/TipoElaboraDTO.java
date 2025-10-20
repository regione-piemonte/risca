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
 * The type Tipo Elabora  dto.
 *
 * @author CSI PIEMONTE
 */
public class TipoElaboraDTO {
	
    @Min(value = 1, message = "L' id_tipo_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_elabora")
    private Long idTipoElabora;
	
    @Min(value = 1, message = "L' id_ambito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_ambito supera il limite massimo consentito per Integer")
	@JsonProperty("id_ambito")
    private Long idAmbito;
	
    @Min(value = 1, message = "L' id_funzionalita deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_funzionalita supera il limite massimo consentito per Integer")
	@JsonProperty("id_funzionalita")
    private Long idFunzionalita;
	
    @Size(max = 40, min = 0, message = "cod_tipo_elabora deve essere compreso tra 0 e 40 caratteri. ")
    @JsonProperty("cod_tipo_elabora")
    private String codTipoElabora;

    @Size(max = 40, min = 0, message = "des_tipo_elabora deve essere compreso tra 0 e 40 caratteri. ")
    @JsonProperty("des_tipo_elabora")
    private String desTipoElabora;
    
    @Min(value = 1, message = "L' ordina_tipo_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' ordina_tipo_elabora supera il limite massimo consentito per Integer")
    @JsonProperty("ordina_tipo_elabora")
    private Long ordinaTipoElabora;
    
    @Min(value = 0, message = "Il flg_default deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_default deve essere al massimo 1.")
    @JsonProperty("flg_default")
    private Integer flgDefault; 
    
    @Min(value = 0, message = "Il flg_visibile deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_visibile deve essere al massimo 1.")
    @JsonProperty("flg_visibile")
    private Integer flgVisibile; 
    
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita ;

    @JsonProperty("data_inizio_validita")
    private Date dataInizioValidita ;

	public Long getIdTipoElabora() {
		return idTipoElabora;
	}

	public void setIdTipoElabora(Long idTipoElabora) {
		this.idTipoElabora = idTipoElabora;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public Long getIdFunzionalita() {
		return idFunzionalita;
	}

	public void setIdFunzionalita(Long idFunzionalita) {
		this.idFunzionalita = idFunzionalita;
	}

	public String getCodTipoElabora() {
		return codTipoElabora;
	}

	public void setCodTipoElabora(String codTipoElabora) {
		this.codTipoElabora = codTipoElabora;
	}

	public String getDesTipoElabora() {
		return desTipoElabora;
	}

	public void setDesTipoElabora(String desTipoElabora) {
		this.desTipoElabora = desTipoElabora;
	}

	public Long getOrdinaTipoElabora() {
		return ordinaTipoElabora;
	}

	public void setOrdinaTipoElabora(Long ordinaTipoElabora) {
		this.ordinaTipoElabora = ordinaTipoElabora;
	}

	public Integer getFlgDefault() {
		return flgDefault;
	}

	public void setFlgDefault(Integer flgDefault) {
		this.flgDefault = flgDefault;
	}

	public Integer getFlgVisibile() {
		return flgVisibile;
	}

	public void setFlgVisibile(Integer flgVisibile) {
		this.flgVisibile = flgVisibile;
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
