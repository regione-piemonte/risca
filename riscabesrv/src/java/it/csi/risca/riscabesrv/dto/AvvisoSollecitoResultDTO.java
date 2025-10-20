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

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvvisoSollecitoResultDTO {

	@JsonProperty("status")
	private String status;

	@JsonProperty("stepError")
	private String stepError;

	@JsonProperty("errorMessage")
	private String errorMessage;

	@JsonProperty("progrOdsDatiTitolare")
	private int progrOdsDatiTitolare = 0;
	@JsonProperty("progrOdsDatiAmmin")
	private int progrOdsDatiAmmin = 0;
	@JsonProperty("progrOdsDettVers")
	private int progrOdsDettVers = 0;
	@JsonProperty("progrOdsDestinatari")
	private int progrOdsDestinatari = 0;
	@JsonProperty("progrOdsDatiPagopa")
	private int progrOdsDatiPagopa = 0;
	@JsonProperty("progrTxtCompleto")
	private int progrTxtCompleto = 0;
	@JsonProperty("progrTxtParzCarta")
	private int progrTxtParzCarta = 0;

	@JsonProperty("contatoreSdPerSoggetto")
	private int contatoreSdPerSoggetto = 0;

	@JsonProperty("subtotSpeseNotifSogg")
	private double subtotSpeseNotifSogg = 0;
	@JsonProperty("subtotImportiVers")
	private double subtotImportiVers = 0;
	@JsonProperty("subtotInteressiVers")
	private double subtotInteressiVers = 0;
	@JsonProperty("interessiVersDett")
	private double interessiVersDett = 0;

	@JsonProperty("accertamento")
	private AccertamentoDTO accertamento;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStepError() {
		return stepError;
	}

	public void setStepError(String stepError) {
		this.stepError = stepError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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

}
