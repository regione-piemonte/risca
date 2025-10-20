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

import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * StatiRiscossioneDTO
 *
 * @author CSI PIEMONTE
 */


public class StatiRiscossioneDTO {

    @Min(value = 1, message = "L'id_stato_riscossione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L'id_stato_riscossione supera il limite massimo consentito per Integer")
	@JsonProperty("id_stato_riscossione")
    private Long idStatoRiscossione;

    @JsonIgnore
    private Long idAmbito;

    @Size(max = 20, min = 1, message = "cod_stato_riscossione deve essere compreso tra 0 e 30 caratteri. ")
    @JsonProperty("cod_stato_riscossione")
    private String codStatoRiscossione;
    
    @Size(max = 100, min = 1, message = "des_stato_riscossione deve essere compreso tra 0 e 30 caratteri. ")
    @JsonProperty("des_stato_riscossione")
    private String desStatoRiscossione;
    
    @Min(value = 0, message = "Il flg_default deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_default deve essere al massimo 1.")
	@JsonProperty("flg_default")
    private int flgDefault;
	
    @JsonProperty("ambito")
    private AmbitoDTO ambito;
	
	
    public Long getIdStatoRiscossione() {
		return idStatoRiscossione;
	}
    
	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodStatoRiscossione() {
		return codStatoRiscossione;
	}

	public void setCodStatoRiscossione(String codStatoRiscossione) {
		this.codStatoRiscossione = codStatoRiscossione;
	}

	public String getDesStatoRiscossione() {
		return desStatoRiscossione;
	}

	public void setDesStatoRiscossione(String desStatoRiscossione) {
		this.desStatoRiscossione = desStatoRiscossione;
	}

	public void setIdStatoRiscossione(Long idStatoRiscossione) {
		this.idStatoRiscossione = idStatoRiscossione;
	}
	
    public int getFlgDefault() {
		return flgDefault;
	}

	public void setFlgDefault(int flgDefault) {
		this.flgDefault = flgDefault;
	}
	
	
    public AmbitoDTO getAmbito() {
		return ambito;
	}

	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatiRiscossioneDTO other = (StatiRiscossioneDTO) obj;
		return Objects.equals(ambito, other.ambito) && Objects.equals(codStatoRiscossione, other.codStatoRiscossione)
				&& Objects.equals(desStatoRiscossione, other.desStatoRiscossione) && flgDefault == other.flgDefault
				&& Objects.equals(idAmbito, other.idAmbito)
				&& Objects.equals(idStatoRiscossione, other.idStatoRiscossione);
	}

    @Override
	public int hashCode() {
		return Objects.hash(ambito, codStatoRiscossione, desStatoRiscossione, flgDefault, idAmbito, idStatoRiscossione);
	}

    
    /**
     * @return string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatiRiscossioneDTO {");
        sb.append("         idStatoRiscossione:").append(idStatoRiscossione);
        sb.append(",        idAmbito:").append(idAmbito);
        sb.append(",        codStatoRiscossione:'").append(codStatoRiscossione).append("'");
        sb.append(",        desStatoRiscossione:'").append(desStatoRiscossione).append("'");
        sb.append(",        flgDefault:").append(flgDefault);
        sb.append("}");
        return sb.toString();
    }

}
