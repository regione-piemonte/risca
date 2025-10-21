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

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnualitaUsoSdExtendedDTO extends AnnualitaUsoSdDTO {

	@JsonProperty("id_unita_misura")
	private Long idUnitaMisura;

	@JsonProperty("sigla_unita_misura")
	private String siglaUnitaMisura;

	@JsonProperty("des_unita_misura")
	private String desUnitaMisura;

	@JsonProperty("des_tipo_uso")
	private String desTipoUso;

	@JsonProperty("uso_t_anno")
	private Long usoTAnno;

	@JsonProperty("json_regola")
	private String jsonRegola;

	public Long getIdUnitaMisura() {
		return idUnitaMisura;
	}

	public void setIdUnitaMisura(Long idUnitaMisura) {
		this.idUnitaMisura = idUnitaMisura;
	}

	public String getSiglaUnitaMisura() {
		return siglaUnitaMisura;
	}

	public void setSiglaUnitaMisura(String siglaUnitaMisura) {
		this.siglaUnitaMisura = siglaUnitaMisura;
	}

	public String getDesUnitaMisura() {
		return desUnitaMisura;
	}

	public void setDesUnitaMisura(String desUnitaMisura) {
		this.desUnitaMisura = desUnitaMisura;
	}

	public String getDesTipoUso() {
		return desTipoUso;
	}

	public void setDesTipoUso(String desTipoUso) {
		this.desTipoUso = desTipoUso;
	}

	public Long getUsoTAnno() {
		return usoTAnno;
	}

	public void setUsoTAnno(Long usoTAnno) {
		this.usoTAnno = usoTAnno;
	}

	public String getJsonRegola() {
		return jsonRegola;
	}

	public void setJsonRegola(String jsonRegola) {
		this.jsonRegola = jsonRegola;
	}

	@Override
	public String toString() {
		return "AnnualitaUsoSdExtendedDTO [idUnitaMisura=" + idUnitaMisura + ", siglaUnitaMisura=" + siglaUnitaMisura
				+ ", desUnitaMisura=" + desUnitaMisura + ", desTipoUso=" + desTipoUso + ", usoTAnno=" + usoTAnno
				+ ", jsonRegola=" + jsonRegola + "]";
	}

}
