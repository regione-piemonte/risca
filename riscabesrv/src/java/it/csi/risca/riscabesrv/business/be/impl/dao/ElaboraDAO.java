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

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;

/**
 * The interface Elabora dao.
 *
 * @author CSI PIEMONTE
 */
public interface ElaboraDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Load elaboration requests by id_ambito, codTipoElabora, codStatoElabora.
	 *
	 * @param idAmbito        idAmbito
	 * @param codTipoElabora  codTipoElabora
	 * @param codStatoElabora codStatoElabora
	 * @return List<ElaboraDTO> list
	 */
	List<ElaboraDTO> loadElabora(String idAmbito, List<String> codTipoElabora, List<String> codStatoElabora,
			String dataRichiestaInizio, String dataRichiestaFine, String codFunzionalita, Integer offset, Integer limit,
			String sort, Integer flgVisibile);

	/**
	 * count elaboration requests by id_ambito, codTipoElabora, codStatoElabora.
	 *
	 * @param idAmbito        idAmbito
	 * @param codTipoElabora  codTipoElabora
	 * @param codStatoElabora codStatoElabora
	 * @return Integer
	 */
	Integer countAllElabora(String idAmbito, List<String> codTipoElabora, List<String> codStatoElabora,
			String dataRichiestaInizio, String dataRichiestaFine, String codFunzionalita, Integer flgVisibile);

	ElaboraDTO loadElaboraById(Long idElabora, Boolean download);

	List<ElaboraDTO> verifyElabora(String idAmbito, List<Integer> idTipoElabora, List<String> codStatoElabora);

	/**
	 * Save elabora.
	 *
	 * @param dto ElaboraDTO
	 * @return ElaboraDTO
	 * @throws Exception
	 */
	ElaboraDTO saveElabora(ElaboraDTO dto) throws Exception;

	/**
	 * Update elabora.
	 *
	 * @param dto ElaboraDTO
	 * @return ElaboraDTO
	 */
	ElaboraDTO updateElabora(ElaboraDTO dto) throws Exception;

	/**
	 * verifyBollOrdinaria.
	 *
	 * @param dto ElaboraDTO
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
	void verifyBollOrdinaria(ElaboraDTO elabora) throws BusinessException, Exception;

	long countElaboraNotturno() throws DAOException, SystemException;

	List<ElaboraDTO> loadElaboraByCF(String codiceFiscale) throws Exception;

}
