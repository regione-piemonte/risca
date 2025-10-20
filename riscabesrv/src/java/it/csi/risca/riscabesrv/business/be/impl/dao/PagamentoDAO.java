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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.PagamentoDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;

/**
 * The interface Pagamento dao.
 *
 * @author CSI PIEMONTE
 */
public interface PagamentoDAO {

    PagamentoDTO getPagamentoWithMaxDataOpVal(Long idStatoDebitorio) throws SQLException;   
    
	List<PagamentoExtendedDTO> getPagamentoByIdRiscossione(Long idRiscossione) throws SQLException;

	PagamentoExtendedDTO savePagamento(PagamentoExtendedDTO pagamentoExtendedDTO, String fruitore) throws SQLException;
	
	PagamentoExtendedDTO updatePagamento(PagamentoExtendedDTO pagamentoExtendedDTO, String fruitore);
	
	Long deleteByIdPagamento(Long idPagamento);
	
	List<PagamentoExtendedDTO>  getPagamentiByIdStatoDebitorio(Long idStatoDebitorio) throws SQLException;
	
	PagamentoExtendedDTO getPagamentoByIdPagamento(Long idPagamento) throws SQLException;
	
	List<PagamentoDTO>  getPagamentiByCodiceAvviso(String codiceAvviso);
	
	List<PagamentoDTO> getPagamentiByQuintoCampo(String quintoCampo);
	
	List<PagamentoDTO> getPagamentiForPosteEstrco(String dataValuta, BigDecimal importoMovimento,
			String descrizioneMovimento, Long idTipoModalitaPag, String cro);

	List<PagamentoExtendedDTO> getPagamentiDaVisionare(Integer offset, Integer limit, String sort) throws Exception;

	Integer countPagamentiDaVisionare() throws Exception;
	
	String getDataPagamentoMax(Long idStatoDebitorio);
	
	public List<PagamentoExtendedDTO> getPagamentiPerRiscCoattivaEFallimByIdRataSdAndIdStatoDeb(Long idRataSd, Long idStatoDebitorio) throws DAOException, SystemException;
	
	public List<PagamentoExtendedDTO> getPagamentiByIdStatoDebForDilazione(Long idStatoDebitorio) throws  DAOException, SystemException;
	
    public List<Long> countEsistenzaPagamentoPerSoris(String dataEvento, BigDecimal sumImportoCaricoRisco, Long idFileSoris) throws Exception;

	public List<Long> getListaIdPagamentiByAnnoDataRif(String anno, String dataRif, List<Long> listaIdPagamento);
	
	public Integer countEsistenzaPagamentoDaAnnullarePerSoris(String dataEvento, Long idPagamento ) throws Exception;

	public PagamentoExtendedDTO updateAnnullamentoPagamentoSoris(PagamentoExtendedDTO dto, String fruitore, Long idFileSoris);
}