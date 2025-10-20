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

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.impl.dao.CsiLogAuditDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.CsiLogAuditDTO;

public class CsiLogAuditDAOImpl extends RiscaBeSrvGenericDAO<CsiLogAuditDTO> implements CsiLogAuditDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();

	private static final String QUERY_INSERT_CSI_LOG_AUDIT = "INSERT INTO csi_log_audit ( data_ora, id_app, "
			+ "ip_address, utente, operazione, ogg_oper, key_oper) VALUES(:dataOra, :idApp, :ipAddress, :utente, "
			+ ":operazione, :oggOper, :keyOper) ";

	@Override
	public CsiLogAuditDTO saveCsiLogAudit(CsiLogAuditDTO dto) {
		LOGGER.debug("[CsiLogAuditDaoImpl::saveCsiLogAudit] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("dataOra", now);
			map.put("idApp", dto.getIdApp());
			map.put("ipAddress", dto.getIpAddress());
			map.put("utente", dto.getUtente());
			map.put("operazione", dto.getOperazione());
			map.put("oggOper", dto.getOggOper());
			map.put("keyOper", dto.getKeyOper());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_CSI_LOG_AUDIT, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[CsiLogAuditDaoImpl::saveCsiLogAudit] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			 throw e;

		} finally {
			LOGGER.debug("[CsiLogAuditDaoImpl::saveCsiLogAudit] END");
		}

		return dto;
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<CsiLogAuditDTO> getRowMapper() throws SQLException {
		
		return new CsiLogAuditRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new CsiLogAuditExtendedRowMapper();
	}
	
	
	public static class CsiLogAuditRowMapper implements RowMapper<CsiLogAuditDTO> {

	    public CsiLogAuditRowMapper() throws SQLException {
	         // Instantiate class
	     }
		@Override
		public CsiLogAuditDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			CsiLogAuditDTO bean = new CsiLogAuditDTO();
	         populateBean(rs, bean);
	         return bean;
		}
		private void populateBean(ResultSet rs, CsiLogAuditDTO bean) throws SQLException {
			bean.setDataOra(rs.getDate("data_ora"));
			bean.setIdApp(rs.getString("id_app"));
			bean.setIpAddress(rs.getString("ip_address"));
			bean.setKeyOper(rs.getString("key_oper"));
			bean.setOggOper(rs.getString("ogg_oper"));
			bean.setOperazione(rs.getString("operazione"));
			bean.setUtente(rs.getString("utente"));
		}

	}

	public static class CsiLogAuditExtendedRowMapper implements RowMapper<CsiLogAuditDTO> {

	    public CsiLogAuditExtendedRowMapper() throws SQLException {
	         // Instantiate class
	     }
		@Override
		public CsiLogAuditDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			CsiLogAuditDTO bean = new CsiLogAuditDTO();
	         populateBean(rs, bean);
	         return bean;
		}
		private void populateBean(ResultSet rs, CsiLogAuditDTO bean) throws SQLException {
			bean.setDataOra(rs.getDate("data_ora"));
			bean.setIdApp(rs.getString("id_app"));
			bean.setIpAddress(rs.getString("ip_address"));
			bean.setKeyOper(rs.getString("key_oper"));
			bean.setOggOper(rs.getString("ogg_oper"));
			bean.setOperazione(rs.getString("operazione"));
			bean.setUtente(rs.getString("utente"));
		}

	}
}
