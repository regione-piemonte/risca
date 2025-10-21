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
 * TipiInvioDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiInvioDTO {

	@JsonProperty("id_tipo_invio")
    private Long idTipoInvio;

    @JsonProperty("cod_tipo_invio")
    private String codTipoInvio;
    
    @JsonProperty("des_tipo_invio")
    private String desTipoInvio;
    
    @JsonProperty("ordina_tipo_invio")
    private Long ordinaTipoInvio;
	
	
	public Long getIdTipoInvio() {
		return idTipoInvio;
	}

	public void setIdTipoInvio(Long idTipoInvio) {
		this.idTipoInvio = idTipoInvio;
	}

	public String getCodTipoInvio() {
		return codTipoInvio;
	}

	public void setCodTipoInvio(String codTipoInvio) {
		this.codTipoInvio = codTipoInvio;
	}

	public String getDesTipoInvio() {
		return desTipoInvio;
	}

	public void setDesTipoInvio(String desTipoInvio) {
		this.desTipoInvio = desTipoInvio;
	}

	public Long getOrdinaTipoInvio() {
		return ordinaTipoInvio;
	}

	public void setOrdinaTipoInvio(Long ordinaTipoInvio) {
		this.ordinaTipoInvio = ordinaTipoInvio;
	}

    /**
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipiInvioDTO that = (TipiInvioDTO) o;
        return Objects.equals(idTipoInvio, that.idTipoInvio) && Objects.equals(codTipoInvio, that.codTipoInvio) && Objects.equals(desTipoInvio, that.desTipoInvio) && Objects.equals(ordinaTipoInvio, that.ordinaTipoInvio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoInvio, codTipoInvio, desTipoInvio, ordinaTipoInvio);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TipiSedeDTO {");
        sb.append("         idTipoInvio:").append(idTipoInvio);
        sb.append(",        codTipoInvio:'").append(codTipoInvio).append("'");
        sb.append(",        desTipoInvio:'").append(desTipoInvio).append("'");
        sb.append(",        ordinaTipoInvio:").append(ordinaTipoInvio);
        sb.append("}");
        return sb.toString();
    }
}
