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

/**
 * AvvisoSollecitoData dto
 *
 * @author CSI PIEMONTE
 */
public class AvvisoSollecitoData {

	private StatiDebitoriAvvisiSollecitiDTO avvisoSollecito;

	private SoggettiExtendedDTO titolare;

	private String descrAccert;
	private String corpoIdricoCaptazione;
	private String comuneDiCaptazione;
	private String soggettoGruppo;
	private Long idSpedizione;
	private ElaboraDTO elabora;
	private String dataProtocollo;
	private String numProtocollo;
	private String dataScadenzaPagamento;

	private int progrOdsDatiTitolare = 0;
	private int progrOdsDatiAmmin = 0;
	private int progrOdsDettVers = 0;
	private int progrOdsDestinatari = 0;
	private int progrOdsDatiPagopa = 0;
	private int progrTxtCompleto = 0;
	private int progrTxtParzCarta = 0;

	private int contatoreSdPerSoggetto = 0;
	private double speseNotifica = 0;
	private double subtotSpeseNotifSogg = 0;
	private double subtotImportiVers = 0;
	private double subtotInteressiVers = 0;
	private double interessiVersDett = 0;

	private String tipoElabora;

	private AccertamentoDTO accertamento;

	public StatiDebitoriAvvisiSollecitiDTO getAvvisoSollecito() {
		return avvisoSollecito;
	}

	public void setAvvisoSollecito(StatiDebitoriAvvisiSollecitiDTO avvisoSollecito) {
		this.avvisoSollecito = avvisoSollecito;
	}

	public SoggettiExtendedDTO getTitolare() {
		return titolare;
	}

	public void setTitolare(SoggettiExtendedDTO titolare) {
		this.titolare = titolare;
	}

	public String getDescrAccert() {
		return descrAccert;
	}

	public void setDescrAccert(String descrAccert) {
		this.descrAccert = descrAccert;
	}

	public String getCorpoIdricoCaptazione() {
		return corpoIdricoCaptazione;
	}

	public void setCorpoIdricoCaptazione(String corpoIdricoCaptazione) {
		this.corpoIdricoCaptazione = corpoIdricoCaptazione;
	}

	public String getComuneDiCaptazione() {
		return comuneDiCaptazione;
	}

	public void setComuneDiCaptazione(String comuneDiCaptazione) {
		this.comuneDiCaptazione = comuneDiCaptazione;
	}

	public String getSoggettoGruppo() {
		return soggettoGruppo;
	}

	public void setSoggettoGruppo(String soggettoGruppo) {
		this.soggettoGruppo = soggettoGruppo;
	}

	public Long getIdSpedizione() {
		return idSpedizione;
	}

	public void setIdSpedizione(Long idSpedizione) {
		this.idSpedizione = idSpedizione;
	}

	public ElaboraDTO getElabora() {
		return elabora;
	}

	public void setElabora(ElaboraDTO elabora) {
		this.elabora = elabora;
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

	public int getProgrOdsDatiTitolare() {
		return progrOdsDatiTitolare;
	}

	public void setProgrOdsDatiTitolare(int progrOdsDatiTitolare) {
		this.progrOdsDatiTitolare = progrOdsDatiTitolare;
	}

	public int getProgrOdsDatiAmmin() {
		return progrOdsDatiAmmin;
	}

	public void setProgrOdsDatiAmmin(int progrOdsDatiAmmin) {
		this.progrOdsDatiAmmin = progrOdsDatiAmmin;
	}

	public int getProgrOdsDettVers() {
		return progrOdsDettVers;
	}

	public void setProgrOdsDettVers(int progrOdsDettVers) {
		this.progrOdsDettVers = progrOdsDettVers;
	}

	public int getProgrOdsDestinatari() {
		return progrOdsDestinatari;
	}

	public void setProgrOdsDestinatari(int progrOdsDestinatari) {
		this.progrOdsDestinatari = progrOdsDestinatari;
	}

	public int getProgrOdsDatiPagopa() {
		return progrOdsDatiPagopa;
	}

	public void setProgrOdsDatiPagopa(int progrOdsDatiPagopa) {
		this.progrOdsDatiPagopa = progrOdsDatiPagopa;
	}

	public int getProgrTxtCompleto() {
		return progrTxtCompleto;
	}

	public void setProgrTxtCompleto(int progrTxtCompleto) {
		this.progrTxtCompleto = progrTxtCompleto;
	}

	public int getProgrTxtParzCarta() {
		return progrTxtParzCarta;
	}

	public void setProgrTxtParzCarta(int progrTxtParzCarta) {
		this.progrTxtParzCarta = progrTxtParzCarta;
	}

	public int getContatoreSdPerSoggetto() {
		return contatoreSdPerSoggetto;
	}

	public double getSpeseNotifica() {
		return speseNotifica;
	}

	public void setSpeseNotifica(double speseNotifica) {
		this.speseNotifica = speseNotifica;
	}

	public void setContatoreSdPerSoggetto(int contatoreSdPerSoggetto) {
		this.contatoreSdPerSoggetto = contatoreSdPerSoggetto;
	}

	public double getSubtotSpeseNotifSogg() {
		return subtotSpeseNotifSogg;
	}

	public void setSubtotSpeseNotifSogg(double subtotSpeseNotifSogg) {
		this.subtotSpeseNotifSogg = subtotSpeseNotifSogg;
	}

	public double getSubtotImportiVers() {
		return subtotImportiVers;
	}

	public void setSubtotImportiVers(double subtotImportiVers) {
		this.subtotImportiVers = subtotImportiVers;
	}

	public double getSubtotInteressiVers() {
		return subtotInteressiVers;
	}

	public void setSubtotInteressiVers(double subtotInteressiVers) {
		this.subtotInteressiVers = subtotInteressiVers;
	}

	public double getInteressiVersDett() {
		return interessiVersDett;
	}

	public void setInteressiVersDett(double interessiVersDett) {
		this.interessiVersDett = interessiVersDett;
	}

	public AccertamentoDTO getAccertamento() {
		return accertamento;
	}

	public void setAccertamento(AccertamentoDTO accertamento) {
		this.accertamento = accertamento;
	}

	public String getTipoElabora() {
		return tipoElabora;
	}

	public void setTipoElabora(String tipoElabora) {
		this.tipoElabora = tipoElabora;
	}

}
