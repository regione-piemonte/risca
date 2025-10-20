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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistroElaboraDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_registro_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_registro_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_registro_elabora")
	private Long idRegistroElabora;

    @Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;

    @Size(max = 20, min = 0, message = "cod_passo_elabora deve essere compreso tra 0 e 20 caratteri.")
	@JsonProperty("cod_passo_elabora")
	private String codPassoElabora;

    @Min(value = 0, message = " flg_esito_elabora deve essere 0 o 1 ")
    @Max(value = 1, message = " flg_esito_elabora deve essere 0 o 1 ")
	@JsonProperty("flg_esito_elabora")
	private int flgEsitoElabora;

    @Size(max = 500, min = 0, message = "nota_elabora deve essere compreso tra 0 e 500 caratteri. ")
	@JsonProperty("nota_elabora")
	private String notaElabora = null;


    @Size(max = 30, min = 0, message = "cod_fase_elabora deve essere compreso tra 0 e 30 caratteri.")
	@JsonProperty("cod_fase_elabora")
	private String codFaseElabora = null;

	public Long getIdRegistroElabora() {
		return idRegistroElabora;
	}

	public void setIdRegistroElabora(Long idRegistroElabora) {
		this.idRegistroElabora = idRegistroElabora;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public String getCodPassoElabora() {
		return codPassoElabora;
	}

	public void setCodPassoElabora(String codPassoElabora) {
		this.codPassoElabora = codPassoElabora;
	}

	public int getFlgEsitoElabora() {
		return flgEsitoElabora;
	}

	public void setFlgEsitoElabora(int flgEsitoElabora) {
		this.flgEsitoElabora = flgEsitoElabora;
	}

	public String getNotaElabora() {
		return notaElabora;
	}

	public void setNotaElabora(String notaElabora) {
		this.notaElabora = notaElabora;
	}

	
	public String getCodFaseElabora() {
		return codFaseElabora;
	}

	public void setCodFaseElabora(String codFaseElabora) {
		this.codFaseElabora = codFaseElabora;
	}

}
