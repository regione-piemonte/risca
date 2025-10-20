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

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfiloPaDTO {

	@JsonProperty("id_profilo_pa")
	    private Long idProfiloPa;

	    @JsonProperty("cod_profilo_pa")
	    private String codProfiloPa;

	    @JsonProperty("des_profilo_pa")
	    private String desProfiloPa;

	    /**
	     * Gets id profilo pa.
	     *
	     * @return the id profilo pa
	     */
	    public Long getIdProfiloPa() {
			return idProfiloPa;
		}
	    
	    /**
	     * Sets id profilo pa.
	     *
	     * @param idProfiloPa the id profilo pa
	     */
		public void setIdProfiloPa(Long idProfiloPa) {
			this.idProfiloPa = idProfiloPa;
		}

	    /**
	     * Gets cod profilo pa.
	     *
	     * @return the cod profilo pa
	     */
		public String getCodProfiloPa() {
			return codProfiloPa;
		}

	    /**
	     * Sets cod profilo pa.
	     *
	     * @param codAmbito the cod profilo pa
	     */
		public void setCodProfiloPa(String codProfiloPa) {
			this.codProfiloPa = codProfiloPa;
		}

	    /**
	     * Gets des profilo pa.
	     *
	     * @return the des profilo pa
	     */
		public String getDesProfiloPa() {
			return desProfiloPa;
		}

	    /**
	     * Sets des profilo pa.
	     *
	     * @param desAmbito the des profilo pa
	     */
		public void setDesProfiloPa(String desProfiloPa) {
			this.desProfiloPa = desProfiloPa;
		}



}
