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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Ambito dto.
 *
 * @author CSI PIEMONTE
 */
public class AmbitoDTO extends GestAttoreDTO{
	
    @Min(value = 1, message = "L' id_ambito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_ambito supera il limite massimo consentito per Integer")
    @JsonProperty("id_ambito")
    private Long idAmbito;

    @Size(max = 20, min = 1, message = "cod_ambito deve essere compreso tra 0 e 20 caratteri.")
    @JsonProperty("cod_ambito")
    private String codAmbito;

    @Size(max = 100, min = 1, message = "des_ambito deve essere compreso tra 0 e 100 caratteri.")
    @JsonProperty("des_ambito")
    private String desAmbito;

    /**
     * Gets id ambito.
     *
     * @return the id ambito
     */
    public Long getIdAmbito() {
        return idAmbito;
    }

    /**
     * Sets id ambito.
     *
     * @param idAmbito the id ambito
     */
    public void setIdAmbito(Long idAmbito) {
        this.idAmbito = idAmbito;
    }

    /**
     * Gets cod ambito.
     *
     * @return the cod ambito
     */
    public String getCodAmbito() {
        return codAmbito;
    }

    /**
     * Sets cod ambito.
     *
     * @param codAmbito the cod ambito
     */
    public void setCodAmbito(String codAmbito) {
        this.codAmbito = codAmbito;
    }

    /**
     * Gets des ambito.
     *
     * @return the des ambito
     */
    public String getDesAmbito() {
        return desAmbito;
    }

    /**
     * Sets des ambito.
     *
     * @param desAmbito the des ambito
     */
    public void setDesAmbito(String desAmbito) {
        this.desAmbito = desAmbito;
    }


    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(codAmbito, desAmbito, idAmbito);
    }

    /**
     * @param obj Object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AmbitoDTO other = (AmbitoDTO) obj;
        return  Objects.equals(codAmbito, other.codAmbito)
                && Objects.equals(desAmbito, other.desAmbito)
                && Objects.equals(idAmbito, other.idAmbito);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AmbitoDTO\n[idAmbito=").append(idAmbito).append("\n codiceAmbito=").append(codAmbito)
                .append("\n descrizioneAmbito=").append(desAmbito).append("]");
        return builder.toString();
    }


}