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

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipoAccertamentoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiAccertamentoDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_accertamento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_accertamento supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_accertamento")
    private Long idTipoAccertamento;

    @Size(max = 40, min = 0, message = "cod_tipo_accertamento deve essere compreso tra 0 e 40 caratteri.")
    @JsonProperty("cod_tipo_accertamento")
    private String codTipoAccertamento;

    @Size(max = 40, min = 0, message = "des_tipo_accertamento deve essere compreso tra 0 e 40 caratteri.")
    @JsonProperty("des_tipo_accertamento")
    private String desTipoAccertamento;

    @Min(value = 0, message = "Il flg_automatico deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_automatico deve essere al massimo 1.")
    @JsonProperty("flg_automatico")
    private int flgAutomatico;

    @Min(value = 0, message = "Il flg_manuale deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_manuale deve essere al massimo 1.")
    @JsonProperty("flg_manuale")
    private int flgManuale ;


	public Long getIdTipoAccertamento() {
		return idTipoAccertamento;
	}

	public void setIdTipoAccertamento(Long idTipoAccertamento) {
		this.idTipoAccertamento = idTipoAccertamento;
	}

	public String getCodTipoAccertamento() {
		return codTipoAccertamento;
	}

	public void setCodTipoAccertamento(String codTipoAccertamento) {
		this.codTipoAccertamento = codTipoAccertamento;
	}

	public String getDesTipoAccertamento() {
		return desTipoAccertamento;
	}

	public void setDesTipoAccertamento(String desTipoAccertamento) {
		this.desTipoAccertamento = desTipoAccertamento;
	}

	public int getFlgAutomatico() {
		return flgAutomatico;
	}

	public void setFlgAutomatico(int flgAutomatico) {
		this.flgAutomatico = flgAutomatico;
	}

	public int getFlgManuale() {
		return flgManuale;
	}

	public void setFlgManuale(int flgManuale) {
		this.flgManuale = flgManuale;
	}

}
