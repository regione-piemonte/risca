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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettoDelegaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.DelegheDAOImpl.DelegheRowMapper;
import it.csi.risca.riscabesrv.dto.DelegatoDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettoDelegaDTO;
import it.csi.risca.riscabesrv.dto.SoggettoDelegaExtendedDTO;
import it.csi.risca.riscabesrv.util.ErrorMessages;

public class SoggettoDelegaDAOImpl  extends RiscaBeSrvGenericDAO<SoggettoDelegaDTO> implements SoggettoDelegaDAO {


    private final String className = this.getClass().getSimpleName();
    
	public static final String QUERY_SELECT_SOGGETTO_DELEGA_BY_ID_DELEGATO ="SELECT"
			+ "	rrsd.*, rtd.*"
			+ " FROM risca_r_soggetto_delega rrsd "
			+ " INNER JOIN risca_t_delegato rtd on rrsd.id_delegato = rtd.id_delegato "
			+ " WHERE rrsd.id_delegato = :idDelegato";

	@Override
	public List<SoggettoDelegaExtendedDTO> loadSoggettiDelegaByIdDelegato(Long idDelegato) throws Exception {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("idDelegato", idDelegato);
				MapSqlParameterSource params = getParameterValue(map);
				return template.query(getQuery(QUERY_SELECT_SOGGETTO_DELEGA_BY_ID_DELEGATO, null, null), params,
						getExtendedRowMapper());
				 
			}catch (EmptyResultDataAccessException e) {
				LOGGER.debug(getClassFunctionDebugString(className, methodName, ":Data not found for idDelegato: "+ idDelegato));
             return Collections.emptyList();
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
	public SoggettoDelegaDTO createSoggettoDelega(SoggettoDelegaDTO soggettoDelega) throws DAOException, SystemException {
		try {
			StringBuilder sql = new StringBuilder();	
			BigDecimal seqRiscaRSoggettoDelega = null;
			Map<String, Object> paramMapPk = new HashMap<String, Object>();
			sql.append(" SELECT nextval('seq_risca_r_soggetto_delega')");
			seqRiscaRSoggettoDelega = template.queryForObject(sql.toString(), paramMapPk, BigDecimal.class);
			
			LOGGER.debug("[SoggettoDelegaDAOImpl::INSERT] seqRiscaRSoggettoDelega = " + seqRiscaRSoggettoDelega);
			
			String queryInsert = "INSERT INTO risca_r_soggetto_delega (id_soggetto_delega, id_soggetto, id_delegato, data_abilitazione, data_disabilitazione, gest_data_ins, gest_attore_ins, " + 
		    		" gest_data_upd, gest_attore_upd, gest_uid ) " + 
		    		" VALUES(:idSoggettoDelega, :idSoggetto, :idDelegato,TO_DATE(:dataAbilitazione, 'YYYY-MM-DD') ,TO_DATE(:dataDisabilitazione, 'YYYY-MM-DD') ,  current_date, :gestAttoreIns,  " + 
		    		" current_date , :gestAttoreUpd, :gestUid ); ";
			

			LOGGER.debug("[SoggettoDelegaDAOImpl - createSoggettoDelega] query =" + queryInsert.toString());
			LOGGER.debug("[SoggettoDelegaDAOImpl - createSoggettoDelega] param  soggettoDelega = " + soggettoDelega);

			Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("idSoggettoDelega",seqRiscaRSoggettoDelega);
			paramMap.put("idSoggetto", soggettoDelega.getIdSoggetto());
			paramMap.put("idDelegato" , soggettoDelega.getIdDelegato());
			paramMap.put("dataAbilitazione", soggettoDelega.getDataAbilitazione());
			paramMap.put("dataDisabilitazione", soggettoDelega.getDataDisabilitazione());
			paramMap.put("gestAttoreIns", soggettoDelega.getGestAttoreIns());
			paramMap.put("gestAttoreUpd", soggettoDelega.getGestAttoreUpd());
			 Calendar cal = Calendar.getInstance();
	         Date now = cal.getTime();
			
			paramMap.put("gestUid", generateGestUID(soggettoDelega.getGestAttoreIns() + soggettoDelega.getGestAttoreUpd() + now));
	
			 template.update(queryInsert, paramMap);

			 soggettoDelega.setIdSoggettoDelega(seqRiscaRSoggettoDelega.longValue());			

			LOGGER.debug("[SoggettoDelegaDAOImpl::createSoggettoDelega]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOGGER.debug("[SoggettoDelegaDAOImpl::createSoggettoDelega]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DAOException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA);
			}
			ex.printStackTrace();
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex) {
			LOGGER.error(
					"[SoggettoDelegaDAOImpl::createSoggettoDelega] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOGGER.debug("[SoggettoDelegaDAOImpl::createSoggettoDelega] END ");
		}
		return soggettoDelega;
		
		
	}
	
	
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<SoggettoDelegaDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<SoggettoDelegaExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new SoggettoDelegaExtendedRowMapper();
	}
	
	public static class SoggettoDelegaExtendedRowMapper implements RowMapper<SoggettoDelegaExtendedDTO> {

		public SoggettoDelegaExtendedRowMapper() throws SQLException {}

		@Override
		public SoggettoDelegaExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			SoggettoDelegaExtendedDTO bean = new SoggettoDelegaExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SoggettoDelegaExtendedDTO bean) throws SQLException {
			bean.setIdSoggettoDelega(rs.getLong("id_soggetto_delega"));
			bean.setIdDelegato(rs.getLong("id_delegato"));
			bean.setDataAbilitazione(rs.getString("data_abilitazione"));
			bean.setDataDisabilitazione(rs.getString("data_disabilitazione"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			DelegheRowMapper delegheRowMapper = new DelegheRowMapper();
			DelegatoDTO Delegato = delegheRowMapper.mapRow(rs, 0);
			bean.setDelegato(Delegato);
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}
			
	}
}
