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

import it.csi.risca.riscabesrv.dto.ambiente.BollCanoneUsoDTO;

/**
 * RiscossioneBsDTO
 *
 * @author CSI PIEMONTE
 */
public class RiscossioneBollData {

	private RiscossioneBollDTO riscossioneBoll;

	private ProvvedimentoDTO provvedimento;

	private ProvvedimentoDTO istanza;

	private SoggettiExtendedDTO titolare;

	private RataSdDTO rataSd;

	private String dataScadenzaEmasIso;

	private String periodoPag;

	private CalcoloNapDTO nap;

	private Long idSpedizione;

	private Long idElabora;

	private String dataProtocollo;

	private String numProtocollo;

	private String dataScadenzaPagamento;

	private String corpoIdrico;

	private String comune;

	private String soggettoGruppo;

	// ******************************************
	// Dati necessari per Bollettazione ordinaria
	// ******************************************

	private BigDecimal canoneCalcolato;

	private Integer anno;

	private int flgRiscossioneEsente;

	private BigDecimal canonePratica;

	private BigDecimal canoneDovutoAnnoPrec;

	private BigDecimal sommCompensPerPrat;

	private Long idAmbito;

	private List<BollCanoneUsoDTO> riscossioneUsi;
	
	// ******************************************
	// Dati necessari per Bollettazione grande idroelettrico
	// ******************************************

	private BigDecimal totEnergProd;
	
	private String tipoElabora;

	public RiscossioneBollDTO getRiscossioneBoll() {
		return riscossioneBoll;
	}

	public void setRiscossioneBoll(RiscossioneBollDTO riscossioneBoll) {
		this.riscossioneBoll = riscossioneBoll;
	}

	public ProvvedimentoDTO getProvvedimento() {
		return provvedimento;
	}

	public void setProvvedimento(ProvvedimentoDTO provvedimento) {
		this.provvedimento = provvedimento;
	}

	public ProvvedimentoDTO getIstanza() {
		return istanza;
	}

	public void setIstanza(ProvvedimentoDTO istanza) {
		this.istanza = istanza;
	}

	public SoggettiExtendedDTO getTitolare() {
		return titolare;
	}

	public void setTitolare(SoggettiExtendedDTO titolare) {
		this.titolare = titolare;
	}

	public RataSdDTO getRataSd() {
		return rataSd;
	}

	public void setRataSd(RataSdDTO rataSd) {
		this.rataSd = rataSd;
	}

	public String getDataScadenzaEmasIso() {
		return dataScadenzaEmasIso;
	}

	public void setDataScadenzaEmasIso(String dataScadenzaEmasIso) {
		this.dataScadenzaEmasIso = dataScadenzaEmasIso;
	}

	public String getPeriodoPag() {
		return periodoPag;
	}

	public void setPeriodoPag(String periodoPag) {
		this.periodoPag = periodoPag;
	}

	public CalcoloNapDTO getNap() {
		return nap;
	}

	public void setNap(CalcoloNapDTO nap) {
		this.nap = nap;
	}

	public Long getIdSpedizione() {
		return idSpedizione;
	}

	public void setIdSpedizione(Long idSpedizione) {
		this.idSpedizione = idSpedizione;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public String getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	public String getNumProtocollo() {
		return numProtocollo;
	}

	public void setNumProtocollo(String numProtocollo) {
		this.numProtocollo = numProtocollo;
	}

	public String getDataScadenzaPagamento() {
		return dataScadenzaPagamento;
	}

	public void setDataScadenzaPagamento(String dataScadenzaPagamento) {
		this.dataScadenzaPagamento = dataScadenzaPagamento;
	}

	public String getCorpoIdrico() {
		return corpoIdrico;
	}

	public void setCorpoIdrico(String corpoIdrico) {
		this.corpoIdrico = corpoIdrico;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public BigDecimal getCanoneCalcolato() {
		return canoneCalcolato;
	}

	public void setCanoneCalcolato(BigDecimal canoneCalcolato) {
		this.canoneCalcolato = canoneCalcolato;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public int getFlgRiscossioneEsente() {
		return flgRiscossioneEsente;
	}

	public void setFlgRiscossioneEsente(int flgRiscossioneEsente) {
		this.flgRiscossioneEsente = flgRiscossioneEsente;
	}

	public BigDecimal getCanonePratica() {
		return canonePratica;
	}

	public void setCanonePratica(BigDecimal canonePratica) {
		this.canonePratica = canonePratica;
	}

	public BigDecimal getCanoneDovutoAnnoPrec() {
		return canoneDovutoAnnoPrec;
	}

	public void setCanoneDovutoAnnoPrec(BigDecimal canoneDovutoAnnoPrec) {
		this.canoneDovutoAnnoPrec = canoneDovutoAnnoPrec;
	}

	public BigDecimal getSommCompensPerPrat() {
		return sommCompensPerPrat;
	}

	public void setSommCompensPerPrat(BigDecimal sommCompensPerPrat) {
		this.sommCompensPerPrat = sommCompensPerPrat;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public List<BollCanoneUsoDTO> getRiscossioneUsi() {
		return riscossioneUsi;
	}

	public void setRiscossioneUsi(List<BollCanoneUsoDTO> riscossioneUsi) {
		this.riscossioneUsi = riscossioneUsi;
	}

	public String getSoggettoGruppo() {
		return soggettoGruppo;
	}

	public void setSoggettoGruppo(String soggettoGruppo) {
		this.soggettoGruppo = soggettoGruppo;
	}

	public BigDecimal getTotEnergProd() {
		return totEnergProd;
	}

	public void setTotEnergProd(BigDecimal totEnergProd) {
		this.totEnergProd = totEnergProd;
	}

	public String getTipoElabora() {
		return tipoElabora;
	}

	public void setTipoElabora(String tipoElabora) {
		this.tipoElabora = tipoElabora;
	}

}
