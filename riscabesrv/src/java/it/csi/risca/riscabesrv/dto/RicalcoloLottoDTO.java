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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * InvioLottoDTO
 *
 * @author CSI PIEMONTE
 */
public class RicalcoloLottoDTO {

	@JsonProperty("elaborazione")
	private ElaboraDTO elaborazione;

	@JsonProperty("lotto")
	private LottoDTO lotto;

	@JsonProperty("iuvDaInviare")
	private List<IuvDaInviareDTO> iuvDaInviare;

	public ElaboraDTO getElaborazione() {
		return elaborazione;
	}

	public void setElaborazione(ElaboraDTO elaborazione) {
		this.elaborazione = elaborazione;
	}

	public LottoDTO getLotto() {
		return lotto;
	}

	public void setLotto(LottoDTO lotto) {
		this.lotto = lotto;
	}

	public List<IuvDaInviareDTO> getIuvDaInviare() {
		return iuvDaInviare;
	}

	public void setIuvDaInviare(List<IuvDaInviareDTO> iuvDaInviare) {
		this.iuvDaInviare = iuvDaInviare;
	}

}
