/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto.ambiente;

import java.math.BigDecimal;

import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.CanoneUsoDTO;

public class BollCanoneUsoDTO {

	private CanoneUsoDTO canoneUso;

	private TipoUsoDatoTecnicoDTO tipoUsoDatoTecnico;

	private BigDecimal canonePerUso;

	private BigDecimal canonePerUsoNonFraz;

	private AnnualitaSdDTO annualitaSd;

	private int numMesi;

	public CanoneUsoDTO getCanoneUso() {
		return canoneUso;
	}

	public void setCanoneUso(CanoneUsoDTO canoneUso) {
		this.canoneUso = canoneUso;
	}

	public TipoUsoDatoTecnicoDTO getTipoUsoDatoTecnico() {
		return tipoUsoDatoTecnico;
	}

	public void setTipoUsoDatoTecnico(TipoUsoDatoTecnicoDTO tipoUsoDatoTecnico) {
		this.tipoUsoDatoTecnico = tipoUsoDatoTecnico;
	}

	public BigDecimal getCanonePerUso() {
		return canonePerUso;
	}

	public void setCanonePerUso(BigDecimal canonePerUso) {
		this.canonePerUso = canonePerUso;
	}

	public BigDecimal getCanonePerUsoNonFraz() {
		return canonePerUsoNonFraz;
	}

	public void setCanonePerUsoNonFraz(BigDecimal canonePerUsoNonFraz) {
		this.canonePerUsoNonFraz = canonePerUsoNonFraz;
	}

	public AnnualitaSdDTO getAnnualitaSd() {
		return annualitaSd;
	}

	public void setAnnualitaSd(AnnualitaSdDTO annualitaSd) {
		this.annualitaSd = annualitaSd;
	}

	public int getNumMesi() {
		return numMesi;
	}

	public void setNumMesi(int numMesi) {
		this.numMesi = numMesi;
	}

}
