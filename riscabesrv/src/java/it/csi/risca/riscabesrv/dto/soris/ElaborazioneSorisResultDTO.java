/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto.soris;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;

public class ElaborazioneSorisResultDTO {
	
	@JsonProperty("num_nuove_cartelle_lette")
	int numNuoveCartelleLette;
	
	@JsonProperty("num_nuove_cartelle_acquisite")
	int numNuoveCartelleAcquisite;
	
	@JsonProperty("num_nuove_cartelle_importi_ko")
	int numNuoveCartelleImportiKO;
	
	@JsonProperty("num_pagam_scart")
	int numPagamScart;
	
	@JsonProperty("num_pagam_letti")
	int numPagamLetti;
	
	@JsonProperty("num_pagam_caric")
	int numPagamCaric;
	
	@JsonProperty("num_pagam_caricati_da_visionare")
	int numPagamCaricatiDaVisionare;
	
	@JsonProperty("num_annullamenti_letti")
	int numAnnullamentiLetti;
	
	@JsonProperty("num_annullamenti_effettuati")
	int numAnnullamentiEffettuati;
	
	boolean ret ;
	
	public boolean isRet() {
		return ret;
	}

	public void setRet(boolean ret) {
		this.ret = ret;
	}

	public int getNumNuoveCartelleLette() {
		return numNuoveCartelleLette;
	}

	public void setNumNuoveCartelleLette(int numNuoveCartelleLette) {
		this.numNuoveCartelleLette = numNuoveCartelleLette;
	}

	public int getNumNuoveCartelleAcquisite() {
		return numNuoveCartelleAcquisite;
	}

	public void setNumNuoveCartelleAcquisite(int numNuoveCartelleAcquisite) {
		this.numNuoveCartelleAcquisite = numNuoveCartelleAcquisite;
	}

	public int getNumNuoveCartelleImportiKO() {
		return numNuoveCartelleImportiKO;
	}

	public void setNumNuoveCartelleImportiKO(int numNuoveCartelleImportiKO) {
		this.numNuoveCartelleImportiKO = numNuoveCartelleImportiKO;
	}

	public int getNumPagamCaricatiDaVisionare() {
		return numPagamCaricatiDaVisionare;
	}

	public void setNumPagamCaricatiDaVisionare(int numPagamCaricatiDaVisionare) {
		this.numPagamCaricatiDaVisionare = numPagamCaricatiDaVisionare;
	}

	public int getNumAnnullamentiLetti() {
		return numAnnullamentiLetti;
	}

	public void setNumAnnullamentiLetti(int numAnnullamentiLetti) {
		this.numAnnullamentiLetti = numAnnullamentiLetti;
	}

	public int getNumAnnullamentiEffettuati() {
		return numAnnullamentiEffettuati;
	}

	public void setNumAnnullamentiEffettuati(int numAnnullamentiEffettuati) {
		this.numAnnullamentiEffettuati = numAnnullamentiEffettuati;
	}

	List<StatoDebitorioExtendedDTO> sdList;

	public int getNumPagamScart() {
		return numPagamScart;
	}

	public void setNumPagamScart(int numPagamScart) {
		this.numPagamScart = numPagamScart;
	}

	public int getNumPagamLetti() {
		return numPagamLetti;
	}

	public void setNumPagamLetti(int numPagamLetti) {
		this.numPagamLetti = numPagamLetti;
	}

	public int getNumPagamCaric() {
		return numPagamCaric;
	}

	public void setNumPagamCaric(int numPagamCaric) {
		this.numPagamCaric = numPagamCaric;
	}

	public List<StatoDebitorioExtendedDTO> getSdList() {
		return sdList;
	}

	public void setSdList(List<StatoDebitorioExtendedDTO> sdList) {
		this.sdList = sdList;
	}

}
