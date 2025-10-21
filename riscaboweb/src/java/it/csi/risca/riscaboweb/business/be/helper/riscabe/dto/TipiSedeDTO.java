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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipiSedeDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiSedeDTO {
	
	@JsonProperty("id_tipo_sede")
    private Long idTipoSede;

    @JsonProperty("cod_tipo_sede")
    private String codTipoSede;
    
    @JsonProperty("des_tipo_sede")
    private String desTipoSede;
    
    @JsonProperty("ordina_tipo_sede")
    private Long ordinaTipoSede;
    
    @JsonProperty("ind_default")
    private String indDefault;
    
	
	public Long getIdTipoSede() {
		return idTipoSede;
	}

	public void setIdTipoSede(Long idTipoSede) {
		this.idTipoSede = idTipoSede;
	}

	public String getCodTipoSede() {
		return codTipoSede;
	}

	public void setCodTipoSede(String codTipoSede) {
		this.codTipoSede = codTipoSede;
	}

	public String getDesTipoSede() {
		return desTipoSede;
	}

	public void setDesTipoSede(String desTipoSede) {
		this.desTipoSede = desTipoSede;
	}

	public Long getOrdinaTipoSede() {
		return ordinaTipoSede;
	}

	public void setOrdinaTipoSede(Long ordinaTipoSede) {
		this.ordinaTipoSede = ordinaTipoSede;
	}

	public String getIndDefault() {
		return indDefault;
	}

	public void setIndDefault(String indDefault) {
		this.indDefault = indDefault;
	}


    /**
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipiSedeDTO that = (TipiSedeDTO) o;
        return Objects.equals(idTipoSede, that.idTipoSede) && Objects.equals(codTipoSede, that.codTipoSede) && Objects.equals(desTipoSede, that.desTipoSede) && Objects.equals(ordinaTipoSede, that.ordinaTipoSede) && Objects.equals(indDefault, that.indDefault);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoSede, codTipoSede, desTipoSede, ordinaTipoSede, indDefault);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TipiSedeDTO {");
        sb.append("         idTipoSede:").append(idTipoSede);
        sb.append(",        codTipoSede:'").append(codTipoSede).append("'");
        sb.append(",        desTipoSede:'").append(desTipoSede).append("'");
        sb.append(",        ordinaTipoSede:").append(ordinaTipoSede);
        sb.append(",        indDefault").append(indDefault);
        sb.append("}");
        return sb.toString();
    }
}
