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
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipiProvvedimentoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiProvvedimentoDTO {

    @Min(value = 1, message = "L' id_tipo_provvedimento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_provvedimento supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_provvedimento")
    private Long idTipoProvvedimento;

    @JsonProperty("id_ambito")
    private Long idAmbito;

    @JsonProperty("cod_tipo_provvedimento")
    private String codTipoProvvedimento;
    
    @JsonProperty("des_tipo_provvedimento")
    private String desTipoProvvedimento;
    
	@JsonProperty("flg_istanza")
    private String flgIstanza;
	
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita ;

    @JsonProperty("data_inizio_validita")
    private Date dataInizioValidita ;

    @JsonProperty("ordina_tipo_provv")
    private Long ordinaTipoProvv;
    
    @JsonProperty("flg_default")
    private int flgDefault;
    
	public Long getIdTipoProvvedimento() {
		return idTipoProvvedimento;
	}

	public void setIdTipoProvvedimento(Long idTipoProvvedimento) {
		this.idTipoProvvedimento = idTipoProvvedimento;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodTipoProvvedimento() {
		return codTipoProvvedimento;
	}

	public void setCodTipoProvvedimento(String codTipoProvvedimento) {
		this.codTipoProvvedimento = codTipoProvvedimento;
	}

	public String getDesTipoProvvedimento() {
		return desTipoProvvedimento;
	}

	public void setDesTipoProvvedimento(String desTipoProvvedimento) {
		this.desTipoProvvedimento = desTipoProvvedimento;
	}
	
    public String getFlgIstanza() {
		return flgIstanza;
	}

	public void setFlgIstanza(String flgIstanza) {
		this.flgIstanza = flgIstanza;
	}

    public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public Long getOrdinaTipoProvv() {
		return ordinaTipoProvv;
	}

	public void setOrdinaTipoProvv(Long ordinaTipoProvv) {
		this.ordinaTipoProvv = ordinaTipoProvv;
	}

	public int getFlgDefault() {
		return flgDefault;
	}

	public void setFlgDefault(int flgDefault) {
		this.flgDefault = flgDefault;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codTipoProvvedimento, dataFineValidita, dataInizioValidita, desTipoProvvedimento,
				flgDefault, flgIstanza, idAmbito, idTipoProvvedimento, ordinaTipoProvv);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipiProvvedimentoDTO other = (TipiProvvedimentoDTO) obj;
		return Objects.equals(codTipoProvvedimento, other.codTipoProvvedimento)
				&& Objects.equals(dataFineValidita, other.dataFineValidita)
				&& Objects.equals(dataInizioValidita, other.dataInizioValidita)
				&& Objects.equals(desTipoProvvedimento, other.desTipoProvvedimento) && flgDefault == other.flgDefault
				&& Objects.equals(flgIstanza, other.flgIstanza) && Objects.equals(idAmbito, other.idAmbito)
				&& Objects.equals(idTipoProvvedimento, other.idTipoProvvedimento)
				&& Objects.equals(ordinaTipoProvv, other.ordinaTipoProvv);
	}

	@Override
	public String toString() {
		return "TipiProvvedimentoDTO [idTipoProvvedimento=" + idTipoProvvedimento + ", idAmbito=" + idAmbito
				+ ", codTipoProvvedimento=" + codTipoProvvedimento + ", desTipoProvvedimento=" + desTipoProvvedimento
				+ ", flgIstanza=" + flgIstanza + ", dataFineValidita=" + dataFineValidita + ", dataInizioValidita="
				+ dataInizioValidita + ", ordinaTipoProvv=" + ordinaTipoProvv + ", flgDefault=" + flgDefault + "]";
	}

	
}
