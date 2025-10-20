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

import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraExtendedDTO;

/**
 * The interface RegistroElabora dao.
 *
 * @author CSI PIEMONTE
 */
public interface RegistroElaboraDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save registro elabora.
	 *
	 * @param dto RegistroElaboraDTO
	 * @return RegistroElaboraDTO
	 * @throws Exception 
	 */
	RegistroElaboraDTO saveRegistroElabora(RegistroElaboraDTO dto) throws Exception;

	/**
	 * Load list of RegistroElaboraExtendedDTO by idElabora, idAmbito, esito
	 * 
	 * @param idElabora
	 * @param idAmbito
	 * @param esito
	 * @return List<RegistroElaboraExtendedDTO>
	 * @throws Exception 
	 */
	List<RegistroElaboraExtendedDTO> loadRegistroElaboraByElaboraAndAmbito(String idElabora, String idAmbito,
			Integer esito, String codFaseElabora) throws Exception;

}
