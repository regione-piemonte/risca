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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.IuvDaInviareDTO;

/**

 * The interface IuvDaInviare dao.
 *
 * @author CSI PIEMONTE
 */
public interface IuvDaInviareDAO extends BaseRiscaBeSrvDAO {
		
	
	IuvDaInviareDTO saveIuvDaInviare(IuvDaInviareDTO dto) throws DAOException;
	
	List<IuvDaInviareDTO> getIuvDaInviare(Long idElabora);
	
	Integer annullaIuvPerImportoVersato(Long idElabora, String attore);
	
	Integer annullaIuvPerPosizioneDebitoriaAnnullata(Long idElabora, String attore);
	
	Integer annullaIuvPerCambioTitolare(Long idElabora, String attore);
	
	Integer annullaIuvPerEmissioneARuolo(Long idElabora, String attore);
	
	Integer modificaImportoIuvPerCanoneDovuto(Long idElabora, String attore);
	
	Integer modificaImportoIuvPerImportoVersato(Long idElabora, String attore);
	
	Integer modificaImportoIuvPerAddebitoInteressi(Long idElabora, String attore);

	Integer countIuvDaInviareByIdIuv(Long idIuv, Long idElabora) throws DAOException;

	Integer updateIuvDaInviare(IuvDaInviareDTO iuvDaInviare)throws DAOException;
	
	Integer aggiornaIuvDiInizioAnno(Long idElabora, String attore)throws DAOException;

	Integer annullaInteressiByIdElabora(Long idElabora, String attore);

	Integer deleteIuvDaInviare(Long idAmbito) throws DAOException;
}
