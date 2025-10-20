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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DatoTecnicoDTO
 *
 * @author CSI PIEMONTE
 */

public class DTGrandeIdroDTO {
	
	@JsonProperty("perc_quota_var")
    private Integer percQuotaVar;
	
    @JsonProperty("coeff_energ_grat")
    private BigDecimal coeffEnergGrat;
    
    @JsonProperty("pnm_per_energ_grat")
    private BigDecimal pnmPerEnergGrat;
    
    @JsonProperty("extra_profitti")
    private BigDecimal extraProfitti;
    
	@JsonProperty("tot_energ_prod")
    private BigDecimal toTEnergProd;
	
	@JsonProperty("tot_ricavi_anno")
    private BigDecimal totRicaviAnno ;
	
	@JsonProperty("prezzo_med_ora_pond")
    private BigDecimal prezzoMedOraPond;

	public Integer getPercQuotaVar() {
		return percQuotaVar;
	}

	public void setPercQuotaVar(Integer percQuotaVar) {
		this.percQuotaVar = percQuotaVar;
	}

	public BigDecimal getCoeffEnergGrat() {
		return coeffEnergGrat;
	}

	public void setCoeffEnergGrat(BigDecimal coeffEnergGrat) {
		this.coeffEnergGrat = coeffEnergGrat;
	}

	public BigDecimal getPnmPerEnergGrat() {
		return pnmPerEnergGrat;
	}

	public void setPnmPerEnergGrat(BigDecimal pnmPerEnergGrat) {
		this.pnmPerEnergGrat = pnmPerEnergGrat;
	}

	public BigDecimal getExtraProfitti() {
		return extraProfitti;
	}

	public void setExtraProfitti(BigDecimal extraProfitti) {
		this.extraProfitti = extraProfitti;
	}

	public BigDecimal getToTEnergProd() {
		return toTEnergProd;
	}

	public void setToTEnergProd(BigDecimal toTEnergProd) {
		this.toTEnergProd = toTEnergProd;
	}



	public BigDecimal getTotRicaviAnno() {
		return totRicaviAnno;
	}

	public void setTotRicaviAnno(BigDecimal totRicaviAnno) {
		this.totRicaviAnno = totRicaviAnno;
	}

	public BigDecimal getPrezzoMedOraPond() {
		return prezzoMedOraPond;
	}

	public void setPrezzoMedOraPond(BigDecimal prezzoMedOraPond) {
		this.prezzoMedOraPond = prezzoMedOraPond;
	}
	
	
}
