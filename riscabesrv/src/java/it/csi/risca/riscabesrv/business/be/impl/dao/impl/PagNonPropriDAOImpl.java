/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.impl.dao.PagNonPropriDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.PagNonPropriDTO;
import it.csi.risca.riscabesrv.dto.PagNonPropriExtendedDTO;

public class PagNonPropriDAOImpl  extends RiscaBeSrvGenericDAO<PagNonPropriDTO> implements PagNonPropriDAO {

	private final String className = this.getClass().getSimpleName();
	
	public static final String QUERY_INSERT ="INSERT INTO risca_r_pag_non_propri (id_pag_non_propri,"
			+ " id_pagamento, id_tipo_imp_non_propri, importo_versato, gest_data_ins, "
			+ " gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid)"
			+ " VALUES(:idPagNonPropri, :idPagamento, :idTipoImpNonPropri,:importoVersato, :gestDataIns,:gestAttoreIns,"
			+ " :gestDataUpd, :gestAttoreUpd, :gestUid)";

	public static final String QUERY_UPDATE ="UPDATE risca_r_pag_non_propri SET id_pagamento= :idPagamento,"
			+ " id_tipo_imp_non_propri= :idTipoImpNonPropri, "
			+ " importo_versato= :importoVersato, "
			+ " gest_data_upd= :gestDataUpd,"
			+ " gest_attore_upd= :gestAttoreUpd, "
			+ " gest_uid= :gestUid "
			+ " WHERE id_pag_non_propri=:idPagNonPropri";
			
	public static final String QUERY_DELETE_BY_ID_PAGAMENTO ="DELETE FROM risca_r_pag_non_propri "
			+ " WHERE id_pagamento = :idPagamento";
	
	
	@Override
	public PagNonPropriExtendedDTO savePagNonPropri(PagNonPropriExtendedDTO dto) throws Exception {
		LOGGER.debug("[PagNonPropriDAOImpl::savePagNonPropri] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_r_pag_non_propri");
			map.put("idPagNonPropri", genId);
			map.put("idPagamento", dto.getIdPagamento());
			map.put("idTipoImpNonPropri", dto.getTipoImpNonPropri().getIdTipoImpNonPropri());
			map.put("importoVersato", dto.getImportoVersato());

			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_INSERT;
	
			template.update(getQuery(query, null, null), params, keyHolder);

			dto.setIdPagNonPropri(genId);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			return dto;
				
		} catch (SQLException e) {
			LOGGER.error("[PagNonPropriDAOImpl::savePagNonPropri] Errore nell'esecuzione della query", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[PagNonPropriDAOImpl::savePagNonPropri] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} finally {
			LOGGER.debug("[PagNonPropriDAOImpl::savePagNonPropri] END");
		}
	}

	@Override
	public PagNonPropriExtendedDTO updatePagNonPropri(PagNonPropriExtendedDTO dto)
			throws Exception {
		LOGGER.debug("[PagNonPropriDAOImpl::savePagNonPropri] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idPagNonPropri", dto.getIdPagNonPropri());
			map.put("idPagamento", dto.getIdPagamento());
			map.put("idTipoImpNonPropri", dto.getTipoImpNonPropri().getIdTipoImpNonPropri());
			map.put("importoVersato", dto.getImportoVersato());
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_UPDATE;
	
			template.update(getQuery(query, null, null), params, keyHolder);

			dto.setGestDataUpd(now);
			return dto;
				
		} catch (DataAccessException e) {
			LOGGER.error("[PagNonPropriDAOImpl::savePagNonPropri] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} finally {
			LOGGER.debug("[PagNonPropriDAOImpl::savePagNonPropri] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<PagNonPropriDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePagNonPropriByIdPagamento(Long idPagamento) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idPagamento",idPagamento);
			MapSqlParameterSource params = getParameterValue(map);
			template.update(QUERY_DELETE_BY_ID_PAGAMENTO, params);

		} catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e ;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

}
