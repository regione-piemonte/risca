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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipoRiscossioneDTO
 *
 * @author CSI PIEMONTE
 */

public class TipoRiscossioneDTO {
	
	@JsonProperty("id_tipo_riscossione")
    private Long idTipoRiscossione;

    @JsonProperty("id_ambito")
    private Long idAmbito;

    @JsonProperty("cod_tipo_riscossione")
    private String codTipoRiscossione;
    
    @JsonProperty("des_tipo_riscossione")
    private String desTipoRiscossione;

    @JsonProperty("data_fine_validita")
    private Date dataFineValidita ;

    @JsonProperty("data_inizio_validita")
    private Date dataInizioValidita ;
    
    @JsonProperty("ordina_tipo_risco")
    private Long ordinaTipoRisco;
    
    @JsonProperty("flg_default")
    private int flgDefault;
    
    public Long getIdTipoRiscossione() {
		return idTipoRiscossione;
	}

	public void setIdTipoRiscossione(Long idTipoRiscossione) {
		this.idTipoRiscossione = idTipoRiscossione;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodTipoRiscossione() {
		return codTipoRiscossione;
	}

	public void setCodTipoRiscossione(String codTipoRiscossione) {
		this.codTipoRiscossione = codTipoRiscossione;
	}

	public String getDesTipoRiscossione() {
		return desTipoRiscossione;
	}

	public void setDesTipoRiscossione(String desTipoRiscossione) {
		this.desTipoRiscossione = desTipoRiscossione;
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
	

    public Long getOrdinaTipoRisco() {
		return ordinaTipoRisco;
	}

	public void setOrdinaTipoRisco(Long ordinaTipoRisco) {
		this.ordinaTipoRisco = ordinaTipoRisco;
	}

	public int getFlgDefault() {
		return flgDefault;
	}

	public void setFlgDefault(int flgDefault) {
		this.flgDefault = flgDefault;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codTipoRiscossione, dataFineValidita, dataInizioValidita, desTipoRiscossione, flgDefault,
				idAmbito, idTipoRiscossione, ordinaTipoRisco);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoRiscossioneDTO other = (TipoRiscossioneDTO) obj;
		return Objects.equals(codTipoRiscossione, other.codTipoRiscossione)
				&& Objects.equals(dataFineValidita, other.dataFineValidita)
				&& Objects.equals(dataInizioValidita, other.dataInizioValidita)
				&& Objects.equals(desTipoRiscossione, other.desTipoRiscossione) && flgDefault == other.flgDefault
				&& Objects.equals(idAmbito, other.idAmbito)
				&& Objects.equals(idTipoRiscossione, other.idTipoRiscossione)
				&& Objects.equals(ordinaTipoRisco, other.ordinaTipoRisco);
	}

	@Override
	public String toString() {
		return "TipoRiscossioneDTO [idTipoRiscossione=" + idTipoRiscossione + ", idAmbito=" + idAmbito
				+ ", codTipoRiscossione=" + codTipoRiscossione + ", desTipoRiscossione=" + desTipoRiscossione
				+ ", dataFineValidita=" + dataFineValidita + ", dataInizioValidita=" + dataInizioValidita
				+ ", ordinaTipoRisco=" + ordinaTipoRisco + ", flgDefault=" + flgDefault + "]";
	}

	
}
