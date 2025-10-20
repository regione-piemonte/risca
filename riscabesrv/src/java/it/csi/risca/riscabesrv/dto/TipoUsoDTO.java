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
 * TipoUsoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipoUsoDTO {
	
    @Min(value = 1, message = "L' id_tipo_uso deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_uso supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_uso")
    private Long idTipoUso;
	
    @Min(value = 1, message = "L' id_ambito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_ambito supera il limite massimo consentito per Integer")
    @JsonProperty("id_ambito")
    private Long idAmbito;

    @Min(value = 1, message = "L' id_unita_misura deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_unita_misura supera il limite massimo consentito per Integer")
    @JsonProperty("id_unita_misura")
    private Long idUnitaMisura;
    
    @Min(value = 1, message = "L' id_accerta_bilancio deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_accerta_bilancio supera il limite massimo consentito per Integer")
    @JsonProperty("id_accerta_bilancio")
    private Long idAccertaBilancio;

    @Size(max = 40, min = 0, message = "cod_tipo_uso deve essere compreso tra 0 e 40 caratteri.")
    @JsonProperty("cod_tipo_uso")
    private String codTipouso;
    
    @Size(max = 40, min = 0, message = "des_tipo_uso deve essere compreso tra 0 e 40 caratteri.  ")
    @JsonProperty("des_tipo_uso")
    private String desTipouso;
    
	@JsonProperty("id_tipo_uso_padre")
    private String idTipoUsoPadre;

    @Size(max = 40, min = 0, message = "flg_uso_principale deve essere compreso tra 0 e 40 caratteri. ")
	@JsonProperty("flg_uso_principale")
    private String flgUsoPrincipale;
	
    @Min(value = 1, message = "L' ordina_tipo_uso deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' ordina_tipo_uso supera il limite massimo consentito per Integer")
	@JsonProperty("ordina_tipo_uso")
    private Long ordinaTipoUso;
	
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita ;

    @JsonProperty("data_inizio_validita")
    private Date dataInizioValidita ;
    
    @Min(value = 0, message = "Il flg_default deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_default deve essere al massimo 1.")
    @JsonProperty("flg_default")
    private int flgDefault;

	public Long getIdTipoUso() {
		return idTipoUso;
	}

	public void setIdTipoUso(Long idTipoUso) {
		this.idTipoUso = idTipoUso;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public Long getIdUnitaMisura() {
		return idUnitaMisura;
	}

	public void setIdUnitaMisura(Long idUnitaMisura) {
		this.idUnitaMisura = idUnitaMisura;
	}

	public Long getIdAccertaBilancio() {
		return idAccertaBilancio;
	}

	public void setIdAccertaBilancio(Long idAccertaBilancio) {
		this.idAccertaBilancio = idAccertaBilancio;
	}

	public String getCodTipouso() {
		return codTipouso;
	}

	public void setCodTipouso(String codTipouso) {
		this.codTipouso = codTipouso;
	}

	public String getDesTipouso() {
		return desTipouso;
	}

	public void setDesTipouso(String desTipouso) {
		this.desTipouso = desTipouso;
	}

	public String getIdTipoUsoPadre() {
		return idTipoUsoPadre;
	}

	public void setIdTipoUsoPadre(String idTipoUsoPadre) {
		this.idTipoUsoPadre = idTipoUsoPadre;
	}

	public String getFlgUsoPrincipale() {
		return flgUsoPrincipale;
	}

	public void setFlgUsoPrincipale(String flgUsoPrincipale) {
		this.flgUsoPrincipale = flgUsoPrincipale;
	}

	public Long getOrdinaTipoUso() {
		return ordinaTipoUso;
	}

	public void setOrdinaTipoUso(Long ordinaTipoUso) {
		this.ordinaTipoUso = ordinaTipoUso;
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

	public int getFlgDefault() {
		return flgDefault;
	}

	public void setFlgDefault(int flgDefault) {
		this.flgDefault = flgDefault;
	}

}
