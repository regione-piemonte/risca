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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoModalitaPagDAO;
import it.csi.risca.riscabesrv.dto.TipoModalitaPagDTO;


/**
 * The type Tipo modalita pag dao.
 *
 * @author CSI PIEMONTE
 */
public class TipoModalitaPagDAOImpl extends RiscaBeSrvGenericDAO<TipoModalitaPagDTO> implements TipoModalitaPagDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	 private static final String QUERY_TIPO_MODALITA_PAG  = "SELECT rdtmp.* "
	            + "FROM risca_d_tipo_modalita_pag rdtmp ";
    private static final String QUERY_TIPO_MODALITA_PAG_BY_COD = QUERY_TIPO_MODALITA_PAG 
    + "where rdtmp.cod_tipo_modalita_pag = :codTipoModalitaPag ";
    
	@Override
	public TipoModalitaPagDTO loadTipoModalitaPagByCodTipoModalitaPag(String codTipoModalitaPag) {
		LOGGER.debug("[TipoModalitaPagDAOImpl::loadTipoModalitaPagByCodTipoModalitaPag] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codTipoModalitaPag", codTipoModalitaPag);

			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(QUERY_TIPO_MODALITA_PAG_BY_COD, params, getRowMapper());

		} catch (SQLException e) {
			LOGGER.error(
					"[TipoModalitaPagDAOImpl::loadTipoModalitaPagByCodTipoModalitaPag] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[TipoModalitaPagDAOImpl::loadTipoModalitaPagByCodTipoModalitaPag] Data not found for codTipoModalitaPag: "+ codTipoModalitaPag);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[TipoModalitaPagDAOImpl::loadTipoModalitaPagByCodTipoModalitaPag] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[TipoModalitaPagDAOImpl::loadTipoModalitaPagByCodTipoModalitaPag] END");
		}
	}
	
	@Override
	public List<TipoModalitaPagDTO> loadAllTipiModalitaPagamenti() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
        List<TipoModalitaPagDTO> res = new ArrayList<>();
		try {
			res = findSimpleDTOListByQuery(CLASSNAME, methodName, QUERY_TIPO_MODALITA_PAG, null);

		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName, e),e);
			throw  e;
		} finally {
	        LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
		}
		return res;
	}

	@Override
	public String getPrimaryKeySelect() {

		return null;
	}

	@Override
	public RowMapper<TipoModalitaPagDTO> getRowMapper() throws SQLException {
		 return new TipoModalitaPagRowMapper();
	}

    /**
     * The type Tipo modalita pag row mapper.
     */
    public static class TipoModalitaPagRowMapper implements RowMapper<TipoModalitaPagDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoModalitaPagRowMapper() throws SQLException {
            // Instantiate class
        }

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
        public TipoModalitaPagDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipoModalitaPagDTO bean = new TipoModalitaPagDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipoModalitaPagDTO bean) throws SQLException {
            bean.setIdTipoModalitaPag(rs.getLong("id_tipo_modalita_pag"));
            bean.setCodModalitaPag(rs.getString("cod_tipo_modalita_pag"));
            bean.setDesModalitaPag(rs.getString("des_tipo_modalita_pag"));
        }
    }

	@Override
	public RowMapper<TipoModalitaPagDTO> getExtendedRowMapper() throws SQLException {

		return new TipoModalitaPagExtendedRowMapper();
	}
	
	
    /**
     * The type Tipo modalita pag extended row mapper.
     */
    public static class TipoModalitaPagExtendedRowMapper implements RowMapper<TipoModalitaPagDTO> {

        /**
         * Instantiates a new Tipo modalita pag extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoModalitaPagExtendedRowMapper() throws SQLException {
            // Instantiate class
        }

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
        public TipoModalitaPagDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	TipoModalitaPagDTO bean = new TipoModalitaPagDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipoModalitaPagDTO bean) throws SQLException {
            bean.setIdTipoModalitaPag(rs.getLong("id_tipo_modalita_pag"));
            bean.setCodModalitaPag(rs.getString("cod_tipo_modalita_pag"));
            bean.setDesModalitaPag(rs.getString("des_tipo_modalita_pag"));
        }
    }






}
