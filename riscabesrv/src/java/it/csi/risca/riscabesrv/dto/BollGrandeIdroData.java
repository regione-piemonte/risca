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

/**
 * GrandeIdroelettricoData
 *
 * @author CSI PIEMONTE
 */
public class BollGrandeIdroData {

	private ElaboraDTO elabora;

	private RiscossioneDTO riscossione;

	private BigDecimal totEnergProdAnno;

	private BigDecimal totRicaviAnno;

	private String flgUsoGiVariabile;

	private String flgUsoGiMonEneGra;

	private String anno;

	public ElaboraDTO getElabora() {
		return elabora;
	}

	public void setElabora(ElaboraDTO elabora) {
		this.elabora = elabora;
	}

	public RiscossioneDTO getRiscossione() {
		return riscossione;
	}

	public void setRiscossione(RiscossioneDTO riscossione) {
		this.riscossione = riscossione;
	}

	public BigDecimal getTotEnergProdAnno() {
		return totEnergProdAnno;
	}

	public void setTotEnergProdAnno(BigDecimal totEnergProdAnno) {
		this.totEnergProdAnno = totEnergProdAnno;
	}

	public BigDecimal getTotRicaviAnno() {
		return totRicaviAnno;
	}

	public void setTotRicaviAnno(BigDecimal totRicaviAnno) {
		this.totRicaviAnno = totRicaviAnno;
	}

	public String getFlgUsoGiVariabile() {
		return flgUsoGiVariabile;
	}

	public void setFlgUsoGiVariabile(String flgUsoGiVariabile) {
		this.flgUsoGiVariabile = flgUsoGiVariabile;
	}

	public String getFlgUsoGiMonEneGra() {
		return flgUsoGiMonEneGra;
	}

	public void setFlgUsoGiMonEneGra(String flgUsoGiMonEneGra) {
		this.flgUsoGiMonEneGra = flgUsoGiMonEneGra;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

}
