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

import java.math.BigDecimal;
import java.util.List;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagExtendedDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagSearchResultDTO;

/**
 * The interface Dettaglio Pag DAO.
 *
 * @author CSI PIEMONTE
 */
public interface DettaglioPagDAO {

    /**
     * list DettaglioPagExtendedDTO.
     *
     * @param idRataSd
     * @return DettaglioPagExtendedDTO DTO
     * @throws Exception 
     */
	List<DettaglioPagExtendedDTO> getDettaglioPagByIdRate(long idRataSd) throws Exception;
	
    /**
     * save DettaglioPagDTO.
     *
     * @param DettaglioPagDTO
     * @return DettaglioPagDTO DTO
     * @throws Exception 
     */
	DettaglioPagDTO saveDettaglioPag(DettaglioPagDTO dettaglioPagDTO) throws Exception;
	

    /**
     * list DettaglioPagExtendedDTO.
     *
     * @param idRiscossione
     *  @param idStatoDebitorio
     * @return DettaglioPagExtendedDTO DTO
     * @throws DAOException , Exception 
     */
	List<DettaglioPagExtendedDTO> getDettaglioPagByIdRiscossioneAndIdSD(Long  idRiscossione, Long idStatoDebitorio) throws DAOException;
	

    /**
     * list DettaglioPagSearchResultDTO.
     *
     * @param idRiscossione
     *  @param idStatoDebitorio
     * @return DettaglioPagSearchResultDTO DTO
     * @throws DAOException  
     */
	List<DettaglioPagSearchResultDTO> getDettaglioPagByIdPagamento(Long  idPagamento) throws DAOException;
	
	

    /**
     * Long
     *
     * @param idDettaglioPag
     * @return long
     * @throws DAOException  
     */
	Long deleteDettaglioPagById(Long idDettaglioPag) throws DAOException;
	
	void updateDettaglioPagByListIdStatoDebSetInteressiMaturati(List<Long> listIdStatoDebitorio, BigDecimal interessiMaturati, String fruitore) throws DAOException;


    /**
     *  DettaglioPagExtendedDTO.
     *
     * @param idDettaglioPag
     * @return DettaglioPagExtendedDTO DTO
     * @throws Exception 
     */
	DettaglioPagExtendedDTO getDettaglioPagById(long idDettaglioPag) throws Exception;
	
	void updateDettaglioPagSetInteressiMaturatiByIdPagamentoAndIdRataStatoDeb(BigDecimal interessiMaturati, Long idPagamento, Long idRataStatoDeb, String fruitore) throws DAOException;

	DettaglioPagExtendedDTO updateDettaglioPag(DettaglioPagExtendedDTO dettagliPagamento);
	
	List<DettaglioPagDTO> getDettagliPagByListaIdPag(List<Long> listIdPagamento);
	
	List<DettaglioPagDTO> getDettagliPagByListaIdRata(List<Long> listIdRata);
	
	public DettaglioPagDTO updateDettaglioPagAnnullamentoSoris(DettaglioPagDTO dto);
	
}
