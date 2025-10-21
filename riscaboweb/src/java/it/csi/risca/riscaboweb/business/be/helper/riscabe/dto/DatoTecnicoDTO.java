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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DatoTecnicoDTO
 *
 * @author CSI PIEMONTE
 */

public class DatoTecnicoDTO {
	
	@JsonProperty("id_dato_tecnico")
    private Long idDatoTecnico;

    @JsonProperty("cod_dato_tecnico")
    private String codDatoTecnico;
    
    @JsonProperty("des_dato_tecnico")
    private String desDatoTecnico;

    @JsonProperty("id_unita_misura")
    private Long idUnitaMisura;
    
	@JsonProperty("id_tipo_dato_tecnico")
    private Long idTipoDatoTecnico;
	
	@JsonProperty("flg_calcolato")
    private String flgCalcolato;
	
	public Long getIdDatoTecnico() {
		return idDatoTecnico;
	}

	public void setIdDatoTecnico(Long idDatoTecnico) {
		this.idDatoTecnico = idDatoTecnico;
	}

	public String getCodDatoTecnico() {
		return codDatoTecnico;
	}

	public void setCodDatoTecnico(String codDatoTecnico) {
		this.codDatoTecnico = codDatoTecnico;
	}

	public String getDesDatoTecnico() {
		return desDatoTecnico;
	}

	public void setDesDatoTecnico(String desDatoTecnico) {
		this.desDatoTecnico = desDatoTecnico;
	}

	public Long getIdUnitaMisura() {
		return idUnitaMisura;
	}

	public void setIdUnitaMisura(Long idUnitaMisura) {
		this.idUnitaMisura = idUnitaMisura;
	}

	public Long getIdTipoDatoTecnico() {
		return idTipoDatoTecnico;
	}

	public void setIdTipoDatoTecnico(Long idTipoDatoTecnico) {
		this.idTipoDatoTecnico = idTipoDatoTecnico;
	}

	public String getFlgCalcolato() {
		return flgCalcolato;
	}

	public void setFlgCalcolato(String flgCalcolato) {
		this.flgCalcolato = flgCalcolato;
	}
	

}
