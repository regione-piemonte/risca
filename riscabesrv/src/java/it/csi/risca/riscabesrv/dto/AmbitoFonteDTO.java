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

import com.fasterxml.jackson.annotation.JsonIgnore;


public class AmbitoFonteDTO extends GestAttoreDTO{
	
	@JsonIgnore
    private Long idAmbito;

	@JsonIgnore
    private Long idFonte;

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
     * Gets id fonte.
     *
     * @return the id fonte
     */
    public Long getIdFonte() {
        return idFonte;
    }

    /**
     * Sets id fonte.
     *
     * @param idFonte the id fonte
     */
    public void setIdFonte(Long idFonte) {
        this.idFonte = idFonte;
    }
    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(idAmbito, idFonte);
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
        AmbitoFonteDTO other = (AmbitoFonteDTO) obj;
        return  Objects.equals(idFonte, other.idFonte)
                && Objects.equals(idAmbito, other.idAmbito);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AmbitoFonteDTO\n[idAmbito=").append(idAmbito).append("\n idFonte=").append(idFonte).append("]");
        return builder.toString();
    }


}