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
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RataSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;

/**
 * The interface RataSd dao.
 *
 * @author CSI PIEMONTE
 */
public interface RataSdDAO extends BaseRiscaBeSrvDAO {
	
	RataSdDTO loadRataSdById(Long idRataSd);

	/**
	 * Load RataSdDTO by idStatoDebitorio
	 * @param idStatoDebitorio
	 * @param esito
	 * @return RataSdDTO
	 */
	RataSdDTO loadRataSdByStatoDebitorio(Long idStatoDebitorio);
	
	RataSdDTO loadRataSdWorkingByStatoDebitorio(Long idStatoDebitorio);
	
	/**
	 * Save rata sd.
	 *
	 * @param dto RataSdDTO
	 * @return RataSdDTO
	 */
	RataSdDTO saveRataSd(RataSdDTO dto) throws DAOException;
	
	RataSdDTO saveRataSdWorking(RataSdDTO dto, boolean generateId) throws DAOException;
	
	Integer updateRataSdWorkingCanoneDovutoCompensazione(Long idStatoDebitorio, BigDecimal compensazione) throws DAOException;
		
	List<RataSdDTO> loadListRataSdByStatoDebitorio(Long idStatoDebitorio) throws DAOException;
	
	List<Integer> saveNRataSd(StatoDebitorioExtendedDTO statoDebitorio, Long idAmbito) throws DAOException, BusinessException, SystemException;

	void updateRataSd(RataSdDTO rata) throws DAOException, Exception;
	

	List<Integer> updateNRataSd(StatoDebitorioExtendedDTO statoDebitorio, Long idAmbito) throws DAOException, BusinessException, SystemException;

	void deleteNRataSd(Long idStatoDebitorio, Long idAmbito) throws DAOException, BusinessException;
	
	Integer updateRataSdFromRataSdWorking(Long idStatoDebitorio, String attore) throws DAOException;
	
	Integer deleteRataSdWorkingByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException;
	
	void updateDataScadenzaByIdSD(Long idStatoDebitorio, Date dataScadPag) throws DAOException;
	
	Integer copyRataSdFromWorkingByStatoDebitorio(Long idStatoDebitorio, String attore) throws DAOException;

	Long getIdAmbitoByIdRata(Long idRataSd)throws DAOException;

	void updateRataSdSetInteressiMaturati(List<Long> idStatoDebitorio, String fruitore)throws DAOException;
	
	List<RataSdExtendedDTO> findRateConTotaleVersatoRestituito(List<Long> idStatoDebitorio) throws DAOException, SystemException;

	public RowMapper<RataSdExtendedDTO> getExtendedStatoContribuzioneRowMapper() throws SQLException;
	
	public String findDataScadenzaPagamentoPerDilazione(Long idStatoDebitorio) throws DAOException, SystemException;

	public String findDataScadenzaPagamento(Long idStatoDebitorio) throws DAOException, SystemException;

	void updateRataSdSetInteressiMaturatiByIdRata(BigDecimal interessiTotaliCalcolati, Long idRataSd, String fruitore)throws DAOException;

	public List<RataSdExtendedDTO> findRateByIdStatoDebitorioForDilazione(Long idStatoDebitorio) throws DAOException, SystemException;

	public BigDecimal findSumInteressiMaturatiByIdRataPadreAndIdStatoDebitorio(Long idRataSdPadre,Long idStatoDebitorio) throws DAOException, SystemException;
	public Long findAnnoDataScadPagamentoByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException, SystemException;

}
