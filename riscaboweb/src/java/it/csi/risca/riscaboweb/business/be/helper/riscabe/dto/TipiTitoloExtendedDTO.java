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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipoTitoloExtendedDTO
 *
 * @author CSI PIEMONTE
 */
public class TipiTitoloExtendedDTO extends TipiTitoloDTO {
	
	@JsonProperty("ambito")
    private AmbitoDTO ambito;
	
    /**
     * Gets ambito.
     *
     * @return ambito ambito
     */
    public AmbitoDTO getAmbito() {
		return ambito;
	}

    /**
     * Sets ambito.
     *
     * @param ambito ambito
     */
	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}

    /**
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        TipiTitoloExtendedDTO that = (TipiTitoloExtendedDTO) o;
        return Objects.equals(ambito, that.ambito);
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ambito);
    }



    /**
     * @return string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TipiTitoloExtendedDTO {");
        sb.append(super.toString());
        sb.append("         ambito:").append(ambito);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Gets dto.
     *
     * @return TipoTitoloDTO dto
     */
    @JsonIgnore
    public TipiTitoloDTO getDTO() {
    	TipiTitoloDTO dto = new TipiTitoloDTO();
        dto.setIdTipoTitolo(this.getIdTipoTitolo());
        if (null != this.getAmbito()) {
            dto.setIdAmbito(this.getAmbito().getIdAmbito());
        }
        dto.setCodTipoTitolo(this.getCodTipoTitolo());
        dto.setDesTipoTitolo(this.getDesTipoTitolo());
        return dto;
    }


}