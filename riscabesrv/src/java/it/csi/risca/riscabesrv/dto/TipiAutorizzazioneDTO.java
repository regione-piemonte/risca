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
 * TipiAutorizzazioneDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiAutorizzazioneDTO {

    @Min(value = 1, message = "L'id_tipo_autorizza deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L'id_tipo_autorizza supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_autorizza")
    private Long idTipoAutorizza;

    @JsonProperty("cod_tipo_autorizza")
    private String codTipoAutorizza;
    
    @JsonProperty("des_tipo_autorizza")
    private String desTipoAutorizza;
	
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita ;

    @JsonProperty("data_inizio_validita")
    private Date dataInizioValidita ;
    
    @JsonProperty("ordina_tipo_autorizza")
    private Long ordinaTipoAutorizza;
    

    @JsonProperty("flg_default")
    private int flgDefault;
    
	public Long getIdTipoAutorizza() {
		return idTipoAutorizza;
	}

	public void setIdTipoAutorizza(Long idTipoAutorizza) {
		this.idTipoAutorizza = idTipoAutorizza;
	}

	public String getCodTipoAutorizza() {
		return codTipoAutorizza;
	}

	public void setCodTipoAutorizza(String codTipoAutorizza) {
		this.codTipoAutorizza = codTipoAutorizza;
	}

	public String getDesTipoAutorizza() {
		return desTipoAutorizza;
	}

	public void setDesTipoAutorizza(String desTipoAutorizza) {
		this.desTipoAutorizza = desTipoAutorizza;
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

	public Long getOrdinaTipoAutorizza() {
		return ordinaTipoAutorizza;
	}

	public void setOrdinaTipoAutorizza(Long ordinaTipoAutorizza) {
		this.ordinaTipoAutorizza = ordinaTipoAutorizza;
	}

	public int getFlgDefault() {
		return flgDefault;
	}

	public void setFlgDefault(int flgDefault) {
		this.flgDefault = flgDefault;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codTipoAutorizza, dataFineValidita, dataInizioValidita, desTipoAutorizza, flgDefault,
				idTipoAutorizza, ordinaTipoAutorizza);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipiAutorizzazioneDTO other = (TipiAutorizzazioneDTO) obj;
		return Objects.equals(codTipoAutorizza, other.codTipoAutorizza)
				&& Objects.equals(dataFineValidita, other.dataFineValidita)
				&& Objects.equals(dataInizioValidita, other.dataInizioValidita)
				&& Objects.equals(desTipoAutorizza, other.desTipoAutorizza) && flgDefault == other.flgDefault
				&& Objects.equals(idTipoAutorizza, other.idTipoAutorizza)
				&& Objects.equals(ordinaTipoAutorizza, other.ordinaTipoAutorizza);
	}

	@Override
	public String toString() {
		return "TipiAutorizzazioneDTO [idTipoAutorizza=" + idTipoAutorizza + ", codTipoAutorizza=" + codTipoAutorizza
				+ ", desTipoAutorizza=" + desTipoAutorizza + ", dataFineValidita=" + dataFineValidita
				+ ", dataInizioValidita=" + dataInizioValidita + ", ordinaTipoAutorizza=" + ordinaTipoAutorizza
				+ ", flgDefault=" + flgDefault + "]";
	}

}
