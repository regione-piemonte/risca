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
import it.csi.risca.riscabesrv.business.be.impl.dao.GruppoDelegaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.DelegheDAOImpl.DelegheRowMapper;
import it.csi.risca.riscabesrv.dto.DelegatoDTO;
import it.csi.risca.riscabesrv.dto.GruppoDelegaDTO;
import it.csi.risca.riscabesrv.dto.GruppoDelegaExtendedDTO;
import it.csi.risca.riscabesrv.util.ErrorMessages;

public class GruppoDelegaDAOImpl  extends RiscaBeSrvGenericDAO<GruppoDelegaDTO> implements GruppoDelegaDAO {


    private final String className = this.getClass().getSimpleName();
    
	public static final String QUERY_SELECT_GRUPPO_DELEGA_BY_ID_DELEGATO ="SELECT"
			+ "	rrgd.*, rtd.*"
			+ " FROM risca_r_gruppo_delega rrgd "
			+ " INNER JOIN risca_t_delegato rtd on rrgd.id_delegato = rtd.id_delegato "
			+ " WHERE rrgd.id_delegato = :idDelegato";

	@Override
	public List<GruppoDelegaExtendedDTO> loadGruppiDelegaByIdDelegato(Long idDelegato) throws Exception {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("idDelegato", idDelegato);
				MapSqlParameterSource params = getParameterValue(map);
				return template.query(getQuery(QUERY_SELECT_GRUPPO_DELEGA_BY_ID_DELEGATO, null, null), params,
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
	public GruppoDelegaDTO createGruppoDelega(GruppoDelegaDTO gruppoDelega) throws DAOException, SystemException {
		try {
			StringBuilder sql = new StringBuilder();	
			BigDecimal seqRiscaRGruppoDelega = null;
			Map<String, Object> paramMapPk = new HashMap<String, Object>();
			sql.append(" SELECT nextval('seq_risca_r_gruppo_delega')");
			seqRiscaRGruppoDelega = template.queryForObject(sql.toString(), paramMapPk, BigDecimal.class);
			
			LOGGER.debug("[GruppoDelegaDAOImpl::INSERT] seqRiscaRGruppoDelega = " + seqRiscaRGruppoDelega);
			
			String queryInsert = "INSERT INTO risca_r_gruppo_delega (id_gruppo_delega, id_gruppo_soggetto, id_delegato, data_abilitazione, data_disabilitazione, gest_data_ins, gest_attore_ins, " + 
		    		" gest_data_upd, gest_attore_upd, gest_uid ) " + 
		    		" VALUES(:idGruppoDelega, :idGruppoSoggetto, :idDelegato, TO_DATE(:dataAbilitazione, 'YYYY-MM-DD'), TO_DATE(:dataDisabilitazione, 'YYYY-MM-DD'),  current_date, :gestAttoreIns,  " + 
		    		" current_date , :gestAttoreUpd, :gestUid ); ";
			

			LOGGER.debug("[GruppoDelegaDAOImpl - createGruppoDelega] query =" + queryInsert.toString());
			LOGGER.debug("[GruppoDelegaDAOImpl - createGruppoDelega] param  gruppoDelega = " + gruppoDelega);

			Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("idGruppoDelega",seqRiscaRGruppoDelega);
			paramMap.put("idGruppoSoggetto", gruppoDelega.getIdGruppo());
			paramMap.put("idDelegato" , gruppoDelega.getIdDelegato());
			paramMap.put("dataAbilitazione", gruppoDelega.getDataAbilitazione());
			paramMap.put("dataDisabilitazione", gruppoDelega.getDataDisabilitazione());
			paramMap.put("gestAttoreIns", gruppoDelega.getGestAttoreIns());
			paramMap.put("gestAttoreUpd", gruppoDelega.getGestAttoreUpd());
			 Calendar cal = Calendar.getInstance();
	         Date now = cal.getTime();
			
			paramMap.put("gestUid", generateGestUID(gruppoDelega.getGestAttoreIns() + gruppoDelega.getGestAttoreUpd() + now));
	
			 template.update(queryInsert, paramMap);

			 gruppoDelega.setIdGruppoDelega(seqRiscaRGruppoDelega.longValue());			

			LOGGER.debug("[GruppoDelegaDAOImpl::createGruppoDelega]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOGGER.debug("[GruppoDelegaDAOImpl::createGruppoDelega]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DAOException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA);
			}
			ex.printStackTrace();
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex) {
			LOGGER.error(
					"[GruppoDelegaDAOImpl::createGruppoDelega] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOGGER.debug("[GruppoDelegaDAOImpl::createGruppoDelega] END ");
		}
		return gruppoDelega;
		
		
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<GruppoDelegaDTO> getRowMapper() throws SQLException {
		return new GruppoDelegaRowMapper();
	}
	
	public static class GruppoDelegaRowMapper implements RowMapper<GruppoDelegaDTO> {

		public GruppoDelegaRowMapper() throws SQLException {}

		@Override
		public GruppoDelegaDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			GruppoDelegaDTO bean = new GruppoDelegaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, GruppoDelegaDTO bean) throws SQLException {
			bean.setIdGruppoDelega(rs.getLong("id_gruppo_delega"));
			bean.setIdDelegato(rs.getLong("id_delegato"));
			bean.setDataAbilitazione(rs.getString("data_abilitazione"));
			bean.setDataDisabilitazione(rs.getString("data_disabilitazione"));
			bean.setIdGruppo(rs.getLong("id_gruppo_soggetto"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}
			
	}
	@Override
	public RowMapper<GruppoDelegaExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new GruppoDelegaExtendedRowMapper();
	}
	
	public static class GruppoDelegaExtendedRowMapper implements RowMapper<GruppoDelegaExtendedDTO> {

		public GruppoDelegaExtendedRowMapper() throws SQLException {}

		@Override
		public GruppoDelegaExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			GruppoDelegaExtendedDTO bean = new GruppoDelegaExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, GruppoDelegaExtendedDTO bean) throws SQLException {
			GruppoDelegaRowMapper gruppoDelegaRowMapper = new GruppoDelegaRowMapper();
			GruppoDelegaDTO gruppoDelegaDTO =  gruppoDelegaRowMapper.mapRow(rs, 0);
			bean.setIdGruppoDelega(gruppoDelegaDTO.getIdGruppoDelega());
			bean.setIdDelegato(gruppoDelegaDTO.getIdDelegato());
			bean.setDataAbilitazione(gruppoDelegaDTO.getDataAbilitazione());
			bean.setDataDisabilitazione(gruppoDelegaDTO.getDataDisabilitazione());
			bean.setIdGruppo(gruppoDelegaDTO.getIdGruppo());
			bean.setGestAttoreIns(gruppoDelegaDTO.getGestAttoreIns());
			bean.setGestDataIns(gruppoDelegaDTO.getGestDataIns());
			bean.setGestDataUpd(gruppoDelegaDTO.getGestDataUpd());
			bean.setGestAttoreUpd(gruppoDelegaDTO.getGestAttoreUpd());
			bean.setGestUid(gruppoDelegaDTO.getGestUid());
			DelegheRowMapper delegheRowMapper = new DelegheRowMapper();
			DelegatoDTO Delegato = delegheRowMapper.mapRow(rs, 0);
			bean.setDelegato(Delegato);
		}
			
	}
}
