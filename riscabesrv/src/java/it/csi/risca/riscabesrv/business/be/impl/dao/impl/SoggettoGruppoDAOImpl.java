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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettoGruppoDAO;
import it.csi.risca.riscabesrv.dto.GruppiSoggettoDTO;

public class SoggettoGruppoDAOImpl extends RiscaBeSrvGenericDAO<GruppiSoggettoDTO> implements SoggettoGruppoDAO { 

    private final String className = this.getClass().getSimpleName();
    
	private static final String QUERY_LOAD_SOGGETTO_GRUPPI_BY_ID_SOGGETTO = "SELECT rrsg.id_gruppo_soggetto FROM risca_r_soggetto_gruppo rrsg "
			+ "WHERE rrsg.id_soggetto = :idSoggetto ";
	
	private static final String ID_GRUPPO_SOGGETTO ="id_gruppo_soggetto";
	
	private static final String QUERY_LOAD_SOGGETTI_GRUPPO_BY_ID = 
			  "SELECT * "
			+ "FROM risca_r_soggetto_gruppo "
			+ "WHERE id_gruppo_soggetto = :idGruppoSoggetto";
	
	private static final String QUERY_DELETE_SOGGETTO_GRUPPO = "DELETE FROM risca_r_soggetto_gruppo rrsg WHERE rrsg.id_soggetto = :idSoggetto";

	private static final String QUERY_LOAD_FLG_CAPO_GRUPPO = " select flg_capo_gruppo from risca_r_soggetto_gruppo rrsg  "
			+ " where rrsg.id_gruppo_soggetto = (select id_gruppo_soggetto from risca_r_soggetto_gruppo where id_soggetto = :idSoggetto) " ;

	
	
	@Override
	public List<Long> loadIdSoggettoGruppoByIdSoggetto(Long idSoggetto) throws Exception {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        List<Long> res = new ArrayList<>();
		    Map<String, Object> map = new HashMap<>();
		    map.put("idSoggetto", idSoggetto);
			try {
				res = findListLongByQuery(className, methodName, QUERY_LOAD_SOGGETTO_GRUPPI_BY_ID_SOGGETTO, map, ID_GRUPPO_SOGGETTO );
			} catch (Exception e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
				throw  e;
			} finally {
		        LOGGER.debug(getClassFunctionEndInfo(className, methodName));
			}
			return res;
	}
	
	@Override
	public List<GruppiSoggettoDTO> loadSoggettoGruppoByIdGruppoSoggetto(Long idGruppoSoggetto) throws Exception {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        List<GruppiSoggettoDTO>  res = new ArrayList<>();
		    Map<String, Object> map = new HashMap<>();
		    map.put("idGruppoSoggetto", idGruppoSoggetto);
			try {
				res = findSimpleDTOListByQuery(methodName, methodName, QUERY_LOAD_SOGGETTI_GRUPPO_BY_ID, map) ;
			} catch (Exception e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
				throw  e;
			} finally {
		        LOGGER.debug(getClassFunctionEndInfo(className, methodName));
			}
			return res;
			
	}

	@Override
	public void deleteSoggettoGruppoByIdSoggetto(Long idSoggetto) throws Exception {
		  String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        try {
	        	Map<String, Object> map = new HashMap<>();
	        	map.put("idSoggetto", idSoggetto);
	        	MapSqlParameterSource params = getParameterValue(map);

	        	template.update(getQuery(QUERY_DELETE_SOGGETTO_GRUPPO, null, null), params);
	        } catch (DataAccessException e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
	            throw e;
	        } finally {
	            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		
	}
	
	

	@Override
	public List<GruppiSoggettoDTO> getGruppoSoggettoByCf(String cf) throws DAOException {
		LOGGER.debug("[SoggettoGruppoDAOImpl::getGruppoSoggettoByCf] BEGIN");
		
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		List<GruppiSoggettoDTO> listGruppoSoggetto = new ArrayList<GruppiSoggettoDTO>();				
		
		sql.append("select rts.id_soggetto, ");			
		sql.append("CASE when id_gruppo_soggetto  is null then 0 ");	
		sql.append("else id_gruppo_soggetto end as id_gruppo_soggetto  ");			
		sql.append("from  risca_t_soggetto rts left join risca_r_soggetto_gruppo rsg on rts.id_soggetto = rsg.id_soggetto  ");
		sql.append("where rts.id_soggetto in (select id_soggetto ");
		sql.append("from risca_t_soggetto rts ");
		sql.append("where rts.cf_soggetto = :CF_PI or rts.partita_iva_soggetto = :CF_PI ) ");

		paramMap.addValue("CF_PI", cf);
		
		try {
			listGruppoSoggetto = template.query(sql.toString(), paramMap, getExtendedRowMapper());

		} catch (Exception ex) {
			LOGGER.error("[SoggettoGruppoDAOImpl::getGruppoSoggettoByCf] esecuzione query", ex);
			return listGruppoSoggetto;
		} finally {
			LOGGER.debug("[SoggettoGruppoDAOImpl::getGruppoSoggettoByCf] END");
		}
		return listGruppoSoggetto;
	}

	@Override
	public long countSoggettoGruppoByIdSoggetto(Long idSoggetto) throws DAOException, SystemException {
		LOGGER.debug("[SoggettoGruppoDAOImpl::countRecapitoPrincipale] START");

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		sql.append("select count(*) from risca_r_soggetto_gruppo rrsg where rrsg.id_gruppo_soggetto = ");	
		sql.append("(select id_gruppo_soggetto from risca_r_soggetto_gruppo where id_soggetto = :ID_SOGGETTO ) ");

		long conteggio = 0;

		paramMap.addValue("ID_SOGGETTO", idSoggetto);
		
		try {
			
			conteggio = template.queryForObject(sql.toString(), paramMap, Long.class);

		} catch (RuntimeException ex) {
			LOGGER.error("[SoggettoGruppoDAOImpl::countRecapitoPrincipale] esecuzione query", ex);
			throw new DAOException("Query failed");
		} finally {
			LOGGER.debug("[SoggettoGruppoDAOImpl::countRecapitoPrincipale] END");
		}
		return conteggio;
	}
	
	@Override
	public List<String> findFlgCapogruppoByIdSoggetto(Long idSoggetto) throws DAOException, SystemException
	{
		LOGGER.debug("[SoggettoGruppoDAOImpl::findFlgCapogruppoByIdSoggetto] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idSoggetto", idSoggetto);

			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_FLG_CAPO_GRUPPO, null, null), params, new flgCapogruppoRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error("[SoggettoGruppoDAOImpl::findFlgCapogruppoByIdSoggetto] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[SoggettoGruppoDAOImpl::findFlgCapogruppoByIdSoggetto] END");
		}
	}
	
	

	@Override
	public String getPrimaryKeySelect() {
		return QUERY_LOAD_SOGGETTI_GRUPPO_BY_ID;
	}

	@Override
	public RowMapper<GruppiSoggettoDTO> getRowMapper() throws SQLException {
		return new GruppiSoggettoRowMapper();
	}

	@Override
	public RowMapper<GruppiSoggettoDTO> getExtendedRowMapper() throws SQLException {
		return new GruppiSoggettoExtendedRowMapper();
	}
	 /**
     * The type soggetti gruppo row mapper.
     */
    public static class GruppiSoggettoRowMapper implements RowMapper<GruppiSoggettoDTO> {

        /**
         * Instantiates a newGruppiSoggettoRowMapper.
         *
         * @throws SQLException the sql exception
         */


        /**
         * Implementations must implement this method to map each row of data
         * in the ResultSet. This method should not call {@code next()} on
         * the ResultSet; it is only supposed to map values of the current row.
         *
         * @param rs     the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return the result object for the current row (may be {@code null})
         * @throws SQLException if a SQLException is encountered getting
         *                      column values (that is, there's no need to catch SQLException)
         */
        @Override
        public GruppiSoggettoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GruppiSoggettoDTO bean = new GruppiSoggettoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, GruppiSoggettoDTO bean) throws SQLException {
            bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
            bean.setIdSoggetto(rs.getLong("id_soggetto"));
            bean.setFlgCapoGruppo(rs.getInt("flg_capo_gruppo"));
            bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
            bean.setGestDataIns(rs.getDate("gest_data_ins"));
            bean.setGestDataUpd(rs.getDate("gest_data_upd"));
            bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
            bean.setGestUid(rs.getString("gest_uid"));
        }
        
          
        
    }
    
    public static class flgCapogruppoRowMapper implements RowMapper<String> {

		/**
		 * Instantiates a new NomeLottoRowMapper row mapper.
		 */
		public flgCapogruppoRowMapper() {
			// Instatiate class
		}

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("flg_capo_gruppo");
		}
	}
    
    public static class GruppiSoggettoExtendedRowMapper implements RowMapper<GruppiSoggettoDTO> {

        /**
         * Instantiates a newGruppiSoggettoRowMapper.
         *
         * @throws SQLException the sql exception
         */


        /**
         * Implementations must implement this method to map each row of data
         * in the ResultSet. This method should not call {@code next()} on
         * the ResultSet; it is only supposed to map values of the current row.
         *
         * @param rs     the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return the result object for the current row (may be {@code null})
         * @throws SQLException if a SQLException is encountered getting
         *                      column values (that is, there's no need to catch SQLException)
         */
        @Override
        public GruppiSoggettoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GruppiSoggettoDTO bean = new GruppiSoggettoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, GruppiSoggettoDTO bean) throws SQLException {
            bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
            bean.setIdSoggetto(rs.getLong("id_soggetto"));         
        }
        
          
        
    }
}
