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
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipoRiscossioneExtendedDTO
 *
 * @author CSI PIEMONTE
 */
public class TipoRiscossioneExtendedDTO extends TipoRiscossioneDTO {
	
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
        TipoRiscossioneExtendedDTO that = (TipoRiscossioneExtendedDTO) o;
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
        final StringBuilder sb = new StringBuilder("TipoRiscossioneExtendedDTO {");
        sb.append(super.toString());
        sb.append("         ambito:").append(ambito);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Gets dto.
     *
     * @return TipoUsoDTO dto
     */
    @JsonIgnore
    public TipoRiscossioneDTO getDTO() {
    	TipoRiscossioneDTO dto = new TipoRiscossioneDTO();
        dto.setIdTipoRiscossione(this.getIdTipoRiscossione());
        if (null != this.getAmbito()) {
            dto.setIdAmbito(this.getAmbito().getIdAmbito());
        }
        dto.setCodTipoRiscossione(this.getCodTipoRiscossione());
        dto.setDesTipoRiscossione(this.getDesTipoRiscossione());
        return dto;
    }


}
