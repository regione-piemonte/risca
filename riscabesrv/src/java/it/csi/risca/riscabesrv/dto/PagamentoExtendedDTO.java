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
 * PagamentoExtendedDTO
 *
 * @author CSI PIEMONTE
 */
public class PagamentoExtendedDTO extends PagamentoDTO {
	
	@JsonProperty("dettaglio_pag")
	private List<DettaglioPagDTO> dettaglioPag;
	
	@JsonProperty("pag_non_propri")
	private List<PagNonPropriExtendedDTO> pagNonPropri;
	
	@JsonProperty("ambito")
    private AmbitoDTO ambito;

	@JsonProperty("tipo_modalita_pag")
	private TipoModalitaPagDTO tipoModalitaPag;
	
	
	public List<DettaglioPagDTO> getDettaglioPag() {
		return dettaglioPag;
	}

	public void setDettaglioPag(List<DettaglioPagDTO> dettaglioPag) {
		this.dettaglioPag = dettaglioPag;
	}

	public List<PagNonPropriExtendedDTO> getPagNonPropri() {
		return pagNonPropri;
	}

	public void setPagNonPropri(List<PagNonPropriExtendedDTO> pagNonPropri) {
		this.pagNonPropri = pagNonPropri;
	}

	public TipoModalitaPagDTO getTipoModalitaPag() {
		return tipoModalitaPag;
	}

	public void setTipoModalitaPag(TipoModalitaPagDTO tipoModalitaPag) {
		this.tipoModalitaPag = tipoModalitaPag;
	}

	public AmbitoDTO getAmbito() {
		return ambito;
	}

	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}
	

}
