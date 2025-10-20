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

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiSoggettoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.UnitaMisuraDAOImpl.UnitaMisuraExtendedRowMapper;
import it.csi.risca.riscabesrv.dto.TipiSoggettoDTO;
import it.csi.risca.riscabesrv.dto.UnitaMisuraDTO;


/**
 * The type Tipo soggetto dao.
 *
 * @author CSI PIEMONTE
 */
public class TipiSoggettoDAOImpl extends RiscaBeSrvGenericDAO<TipiSoggettoDTO> implements TipiSoggettoDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
    
    private static final String QUERY_TIPI_SOGGETTO = "SELECT rdts.* "
            + "FROM risca_d_tipo_soggetto rdts ";
    
    private static final String QUERY_TIPI_SOGGETTO_BY_ID_TIPO_SOGGETTO = QUERY_TIPI_SOGGETTO
            + "where rdts.id_tipo_soggetto = :idTipoSoggetto ";
    
    private static final String QUERY_TIPI_SOGGETTO_BY_COD_TIPO_SOGGETTO = QUERY_TIPI_SOGGETTO
            + "where rdts.cod_tipo_soggetto = :codTipoSoggetto ";
	
	@Override
	public List<TipiSoggettoDTO> loadTipiSoggetto() {
		LOGGER.debug("[TipiSoggettoDAOImpl::loadTipiSoggetto] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<TipiSoggettoDTO> listTipiSoggetto = new ArrayList<TipiSoggettoDTO>();
        try {
			listTipiSoggetto = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_SOGGETTO, null);
		} catch (Exception e) {
			LOGGER.error("[TipiSoggettoDAOImpl::loadTipiSoggetto] ERROR : " +e);
			  throw e;
		}
        LOGGER.debug("[TipiSoggettoDAOImpl::loadTipiSoggetto] END");
        return listTipiSoggetto;
	}

	@Override
	public TipiSoggettoDTO loadTipiSoggettoByIdOrCodTipoSoggetto(String codTipoSoggetto) throws SQLException {
        LOGGER.debug("[TipiSoggettoDAOImpl::loadTipiSoggettoByIdOrCodTipoSoggetto] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            int id = 0;
            boolean bool = false;
            try {
            	id = Integer.parseInt(codTipoSoggetto);
            	bool = true;
            } catch (NumberFormatException nfe) {
                bool = false;
            }
       
        	if(bool) {
	            map.put("idTipoSoggetto", id);
	            MapSqlParameterSource params = getParameterValue(map);
	            return template.queryForObject(QUERY_TIPI_SOGGETTO_BY_ID_TIPO_SOGGETTO, params, getRowMapper());
	        	}
            else {
                map.put("codTipoSoggetto",codTipoSoggetto);
                MapSqlParameterSource params = getParameterValue(map);
                return template.queryForObject(QUERY_TIPI_SOGGETTO_BY_COD_TIPO_SOGGETTO, params, getRowMapper());
            }
        } catch (SQLException e) {
            LOGGER.error("[TipiSoggettoDAOImpl::loadTipiSoggettoByIdOrCodTipoSoggetto] Errore nell'esecuzione della query", e);
          throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[TipiSoggettoDAOImpl::loadTipiSoggettoByIdOrCodTipoSoggetto] Errore nell'accesso ai dati", e);
            throw e;
        } finally {
            LOGGER.debug("[TipiSoggettoDAOImpl::loadTipiSoggettoByIdOrCodTipoSoggetto] END");
        }
	}

	@Override
	public String getPrimaryKeySelect() {

		return null;
	}

	@Override
	public RowMapper<TipiSoggettoDTO> getRowMapper() throws SQLException {
		 return new TipoSoggettoRowMapper();
	}

    /**
     * The type Tipo soggetto row mapper.
     */
    public static class TipoSoggettoRowMapper implements RowMapper<TipiSoggettoDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoSoggettoRowMapper() throws SQLException {
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
        public TipiSoggettoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiSoggettoDTO bean = new TipiSoggettoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiSoggettoDTO bean) throws SQLException {
            bean.setIdTipoSoggetto(rs.getLong("id_tipo_soggetto"));
            bean.setCodTipoSoggetto(rs.getString("cod_tipo_soggetto"));
            bean.setDesTipoSoggetto(rs.getString("des_tipo_soggetto"));
            bean.setOrdinaTipoSoggetto(rs.getLong("ordina_tipo_soggetto"));
        }
    }

	@Override
	public RowMapper<TipiSoggettoDTO> getExtendedRowMapper() throws SQLException {

		return new TipiSoggettoExtendedRowMapper();
	}
	
	
    /**
     * The type Tipo soggetto extended row mapper.
     */
    public static class TipiSoggettoExtendedRowMapper implements RowMapper<TipiSoggettoDTO> {

        /**
         * Instantiates a new Tipo soggetto extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiSoggettoExtendedRowMapper() throws SQLException {
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
        public TipiSoggettoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiSoggettoDTO bean = new TipiSoggettoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiSoggettoDTO bean) throws SQLException {
            bean.setIdTipoSoggetto(rs.getLong("id_tipo_soggetto"));
            bean.setCodTipoSoggetto(rs.getString("cod_tipo_soggetto"));
            bean.setDesTipoSoggetto(rs.getString("des_tipo_soggetto"));
            bean.setOrdinaTipoSoggetto(rs.getLong("ordina_tipo_soggetto"));
        }
    }


}
