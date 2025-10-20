/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao;

import java.util.List;

import it.csi.risca.riscabesrv.dto.RegioneExtendedDTO;



/**
 * The interface Regioni dao.
 *
 * @author CSI PIEMONTE
 */
public interface RegioniDAO {


	   /**
  * Load Regioni list.
  *
  * @return List<RegioneExtendedDTO> list
	 * @throws Exception 
  */
     List<RegioneExtendedDTO> loadRegioni() throws Exception;
     
   /**
	* Load Regione by cod regione.
	*
	* @return RegioneExtendedDTO 
 * @throws Exception 
	* */
	RegioneExtendedDTO loadRegioniByCodRegione(String codRegione) throws Exception;
}
