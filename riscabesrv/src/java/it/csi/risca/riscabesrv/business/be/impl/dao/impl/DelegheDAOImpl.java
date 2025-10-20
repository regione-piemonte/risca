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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.DelegheDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.DelegatoDTO;
import it.csi.risca.riscabesrv.dto.DelegatoExtendedDTO;
import it.csi.risca.riscabesrv.dto.LottoDTO;

public class DelegheDAOImpl extends RiscaBeSrvGenericDAO<DelegatoDTO> implements DelegheDAO {


    private final String className = this.getClass().getSimpleName();
    
	public static final String QUERY_SELECT_DELEGHE_BY_CF ="SELECT"
			+ "	rtd.*"
			+ " FROM risca_t_delegato rtd "
			+ " WHERE rtd.cf_delegato = :codiceFiscale";
	
	
	private static final String QUERY_INSERT_DELEGA = "INSERT INTO risca_t_delegato "
			+ "(id_delegato, cf_delegato, nominativo, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(:idDelegato, :cfDelegato, :nominativo, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid )";
	
	@Override
	public DelegatoDTO loadDelegheByCf(String codiceFiscale) throws Exception {
		  String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("codiceFiscale", codiceFiscale);
				MapSqlParameterSource params = getParameterValue(map);
				return template.queryForObject(getQuery(QUERY_SELECT_DELEGHE_BY_CF, null, null), params,
						getRowMapper());
			}catch (EmptyResultDataAccessException e) {
				LOGGER.debug(getClassFunctionDebugString(className, methodName, ":Data not found for codice Fiscale: "+ codiceFiscale));
                return null;
			}catch (SQLException e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
				throw new Exception(e);
			} catch (DataAccessException e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
				throw new Exception(e);
			} finally {
				LOGGER.debug(getClassFunctionEndInfo(className, methodName));	
			}
	}
	
	
	@Override
	public DelegatoExtendedDTO saveDelegato(DelegatoExtendedDTO dto) {
		LOGGER.debug("[DelegheDAOImpl::saveDelegato] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_t_delegato");

			map.put("idDelegato", genId);
			map.put("cfDelegato", dto.getCfDelegato());
			map.put("nominativo", dto.getNominativo());		
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_DELEGA, null, null), params, keyHolder);
			dto.setIdDelegato(genId);
		} catch (Exception e) {
			LOGGER.error("[DelegheDAOImpl::saveDelegato] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[DelegheDAOImpl::saveDelegato] END");
		}

		return dto;
	}
	
	
	
	
	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<DelegatoDTO> getRowMapper() throws SQLException {
		return new DelegheRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}

	public static class DelegheRowMapper implements RowMapper<DelegatoDTO> {

		public DelegheRowMapper() throws SQLException {}

		@Override
		public DelegatoDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			DelegatoDTO bean = new DelegatoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, DelegatoDTO bean) throws SQLException {
			bean.setIdDelegato(rs.getLong("id_delegato"));
			bean.setCfDelegato(rs.getString("cf_delegato"));
			bean.setNominativo(rs.getString("nominativo"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}
			
	}



}
