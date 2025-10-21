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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AnnualitaUsoSdDTO {

	@JsonProperty("id_annualita_uso_sd")
	private Long idAnnualitaUsoSd;

	@JsonProperty("id_annualita_sd")
	private Long idAnnualitaSd;

	@JsonProperty("id_tipo_uso")
	private Long idTipoUso;

	@JsonProperty("canone_uso")
	private BigDecimal canoneUso;
	
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


	public TipoUsoExtendedDTO getTipoUso() {
		return tipoUso;
	}

	public void setTipoUso(TipoUsoExtendedDTO tipoUso) {
		this.tipoUso = tipoUso;
	}

	public List<UsoRidaumSdExtendedDTO> getUsoRidaumSd() {
		return usoRidaumSd;
	}

	public void setUsoRidaumSd(List<UsoRidaumSdExtendedDTO> usoRidaumSd) {
		this.usoRidaumSd = usoRidaumSd;
	}

	public BigDecimal getCanoneUnitario() {
		return canoneUnitario;
	}

	public void setCanoneUnitario(BigDecimal canoneUnitario) {
		this.canoneUnitario = canoneUnitario;
	}

	@Override
	public String toString() {
		return "AnnualitaUsoSdDTO [idAnnualitaUsoSd=" + idAnnualitaUsoSd + ", idAnnualitaSd=" + idAnnualitaSd
				+ ", idTipoUso=" + idTipoUso + ", canoneUso=" + canoneUso + ", canoneUnitario=" + canoneUnitario
				+ ", tipoUso=" + tipoUso + ", usoRidaumSd=" + usoRidaumSd + "]";
	}



}
