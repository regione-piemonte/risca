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

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnualitaUsoSdDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_annualita_uso_sd deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_annualita_uso_sd supera il limite massimo consentito per Integer")
	@JsonProperty("id_annualita_uso_sd")
	private Long idAnnualitaUsoSd;

    @Min(value = 1, message = "L' id_annualita_sd deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_annualita_sd supera il limite massimo consentito per Integer")
	@JsonProperty("id_annualita_sd")
	private Long idAnnualitaSd;

    @Min(value = 1, message = "L' id_tipo_uso deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_uso supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_uso")
	private Long idTipoUso;

    @Digits(integer = 9, fraction = 2, message = "canone_uso deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("canone_uso")
	private BigDecimal canoneUso;
	
    @Digits(integer = 9, fraction = 2, message = "canone_unitario deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("canone_unitario")
	private BigDecimal canoneUnitario;

	@JsonProperty("tipo_uso")
	private TipoUsoExtendedDTO tipoUso;
	
	@JsonProperty("uso_ridaum")
    private List<UsoRidaumSdExtendedDTO>  usoRidaumSd;
	
	public Long getIdAnnualitaUsoSd() {
		return idAnnualitaUsoSd;
	}

	public void setIdAnnualitaUsoSd(Long idAnnualitaUsoSd) {
		this.idAnnualitaUsoSd = idAnnualitaUsoSd;
	}

	public Long getIdAnnualitaSd() {
		return idAnnualitaSd;
	}

	public void setIdAnnualitaSd(Long idAnnualitaSd) {
		this.idAnnualitaSd = idAnnualitaSd;
	}

	public Long getIdTipoUso() {
		return idTipoUso;
	}

	public void setIdTipoUso(Long idTipoUso) {
		this.idTipoUso = idTipoUso;
	}

	public BigDecimal getCanoneUso() {
		return canoneUso;
	}

	public void setCanoneUso(BigDecimal canoneUso) {
		this.canoneUso = canoneUso;
	}
	
	public BigDecimal getCanoneUnitario() {
		return canoneUnitario;
	}

	public void setCanoneUnitario(BigDecimal canoneUnitario) {
		this.canoneUnitario = canoneUnitario;
	}


	public List<UsoRidaumSdExtendedDTO> getUsoRidaumSd() {
		return usoRidaumSd;
	}

	public void setUsoRidaumSd(List<UsoRidaumSdExtendedDTO> usoRidaumSd) {
		this.usoRidaumSd = usoRidaumSd;
	}

	public TipoUsoExtendedDTO getTipoUso() {
		return tipoUso;
	}

	public void setTipoUso(TipoUsoExtendedDTO tipoUso) {
		this.tipoUso = tipoUso;
	}

}
