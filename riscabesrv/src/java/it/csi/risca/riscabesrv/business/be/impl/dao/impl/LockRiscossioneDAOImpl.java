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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.LockRiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.LockRiscossioneDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

public class LockRiscossioneDAOImpl  extends RiscaBeSrvGenericDAO<LockRiscossioneDTO> implements LockRiscossioneDAO {

	
	public static final String QUERY_LOAD_LOCK_RISCOSSIONE = "select rtlr.* from risca_w_lock_riscossione rtlr " ;
	
	public static final String QUERY_LOAD_LOCK_RISCOSSIONE_BY_ID_RISCOSIONE = QUERY_LOAD_LOCK_RISCOSSIONE + 
			"where rtlr.id_riscossione = :idRiscossione ";
	
	public static final String QUERY_INSERT_LOCK_RISCOSSIONE = "INSERT INTO risca_w_lock_riscossione (id_riscossione, "
			+ "utente_lock,cf_utente_lock, gest_data_ins, gest_attore_ins)"
			+ "VALUES(:idRiscossione, :utenteLock, :cfUtenteLock,  :gestDataIns, :gestAttoreIns) ";
	
	public static final String QUERY_DELETE_LOCK_RISCOSSIONE="DELETE from risca_w_lock_riscossione WHERE id_riscossione = :idRiscossione";
  
	@Autowired
    private MessaggiDAO messaggiDAO;
	
	@Override
	public List<LockRiscossioneDTO> getAllLockRiscossione() throws SQLException {
		LOGGER.debug("[LockRiscossioneDAOImpl::getAllLockRiscossione] BEGIN");
		try {
			return template.query(getQuery(QUERY_LOAD_LOCK_RISCOSSIONE, null, null),
					getRowMapper());
		} catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[LockRiscossioneDAOImpl::getAllLockRiscossione] No record found in database  ", e);
		    return Collections.emptyList();
		} catch (SQLException e) {
			LOGGER.error("[LockRiscossioneDAOImpl::getAllLockRiscossione] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[LockRiscossioneDAOImpl::getAllLockRiscossione] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[LockRiscossioneDAOImpl::getAllLockRiscossione] END");
		}
	}

	@Override
	public LockRiscossioneDTO getLockRiscossioneByIdRiscossione(Long idRiscossione) throws SQLException {
		LOGGER.debug("[LockRiscossioneDAOImpl::getLockRiscossioneByIdRiscossione] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRiscossione", idRiscossione);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(getQuery(QUERY_LOAD_LOCK_RISCOSSIONE_BY_ID_RISCOSIONE, null, null), params,
					getRowMapper());

		} catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[LockRiscossioneDAOImpl::getLockRiscossioneByIdRiscossione] No record found in database for idRiscossione "+ idRiscossione, e);
		    return null;
		} catch (SQLException e) {
			LOGGER.error("[LockRiscossioneDAOImpl::getLockRiscossioneByIdRiscossione] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[LockRiscossioneDAOImpl::getLockRiscossioneByIdRiscossione] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[LockRiscossioneDAOImpl::getLockRiscossioneByIdRiscossione] END");
		}
	}

	@Override
	public LockRiscossioneDTO saveLockRiscossione(LockRiscossioneDTO dto) {
		LOGGER.debug("[LockRiscossioneDAOImpl::saveLockRiscossione] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idRiscossione", dto.getIdRiscossione());
			map.put("utenteLock", dto.getUtenteLock());
			map.put("cfUtenteLock", dto.getCfUtenteLock()); 
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_INSERT_LOCK_RISCOSSIONE;
	
			template.update(getQuery(query, null, null), params, keyHolder);

			dto.setGestDataIns(now);
			return dto;
				
		} catch (DataAccessException e) {
			LOGGER.error("[LockRiscossioneDAOImpl::saveLockRiscossione] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[LockRiscossioneDAOImpl::saveLockRiscossione] END");
		}
	}

	@Override
	public void deleteLockRiscossione(Long idRiscossione) throws SQLException {
		LOGGER.debug("[LockRiscossioneDAOImpl::deleteLockRiscossione] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRiscossione", idRiscossione);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
	
			template.update(getQuery(QUERY_DELETE_LOCK_RISCOSSIONE, null, null), params, keyHolder);				
		} catch (DataAccessException e) {
			LOGGER.error("[LockRiscossioneDAOImpl::deleteLockRiscossione] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[LockRiscossioneDAOImpl::deleteLockRiscossione] END");
		}
	}
	
	@Override
	public void verifyLockRiscossione(Long idRiscossione, String fruitore ,String cf) throws SQLException, BusinessException, SystemException {
		LOGGER.debug("[LockRiscossioneDAOImpl::verifyLockRiscossione] BEGIN");
		try {
	
			if (fruitore != null && !(fruitore.equals("BATCH") || fruitore.equals("BATCH_PORTING"))) {
				LockRiscossioneDTO lockRiscossioneDTO =  getLockRiscossioneByIdRiscossione(idRiscossione);
				if(lockRiscossioneDTO == null) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.I026);
					throw new  BusinessException(401, messaggiDTO.getCodMessaggio(),Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio())); 
	
				}else {
					if(!lockRiscossioneDTO.getCfUtenteLock().equalsIgnoreCase(cf)) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.I024);
					throw new  BusinessException(401, messaggiDTO.getCodMessaggio(),Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio())); 
				    }
				}


			}
		} catch (DataAccessException e) {
			LOGGER.error("[LockRiscossioneDAOImpl::verifyLockRiscossione] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} catch (SQLException e) {
			LOGGER.error("[LockRiscossioneDAOImpl::verifyLockRiscossione] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[LockRiscossioneDAOImpl::verifyLockRiscossione] END");
		}
		
	}
	
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<LockRiscossioneDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new LockRiscossioneRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public static class LockRiscossioneRowMapper implements RowMapper<LockRiscossioneDTO> {

		public LockRiscossioneRowMapper() throws SQLException {}

		@Override
		public LockRiscossioneDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			LockRiscossioneDTO bean = new LockRiscossioneDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, LockRiscossioneDTO bean) throws SQLException {
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
		    bean.setUtenteLock(rs.getString("utente_lock"));
		    bean.setCfUtenteLock(rs.getString("cf_utente_lock"));
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));

			
		}
				
		private boolean rsHasColumn(ResultSet rs, String column){
		    try{
		        rs.findColumn(column);
		        return true;
		    } catch (SQLException sqlex){
		        //Column not present in resultset
		    }
		    return false;
		}
		
	}



}
