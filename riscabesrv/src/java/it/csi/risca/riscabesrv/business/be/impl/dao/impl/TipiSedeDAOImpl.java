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

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiSedeDAO;
import it.csi.risca.riscabesrv.dto.TipiSedeDTO;


/**
 * The type Tipo sede dao.
 *
 * @author CSI PIEMONTE
 */
public class TipiSedeDAOImpl extends RiscaBeSrvGenericDAO<TipiSedeDTO> implements TipiSedeDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
    
    private static final String QUERY_TIPI_SEDE = "SELECT rdts.* "
            + "FROM risca_d_tipo_sede rdts ";
    private static final String WHERE_TIPI_SEDE_BY_TIPO_SOGGETTO = "WHERE rdts.ind_default = :tipoSoggetto ";
    
    private static final String QUERY_ORDER_TIPI_SEDE =    "ORDER BY rdts.ordina_tipo_sede ASC	";


	@Override
	public List<TipiSedeDTO> loadTipiSede(String tipoSoggetto) {
		LOGGER.debug("[TipiSedeDAOImpl::loadTipiSede] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<TipiSedeDTO> listTipiSede = new ArrayList<TipiSedeDTO>();
        Map<String, Object> map = new HashMap<>();
        try {
        	if(tipoSoggetto != null) {
				map.put("tipoSoggetto", tipoSoggetto);
            	listTipiSede = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_SEDE+WHERE_TIPI_SEDE_BY_TIPO_SOGGETTO+QUERY_ORDER_TIPI_SEDE, map);
        	}else {
    			listTipiSede = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_SEDE+ QUERY_ORDER_TIPI_SEDE, null);
        	}
			
		} catch (Exception e) {
            LOGGER.error("[TipiSedeDAOImpl::loadTipiSede] Errore nell'esecuzione del metodo", e);
            throw e;
		}
        LOGGER.debug("[TipiSedeDAOImpl::loadTipiSede] END");
        return listTipiSede;
	}


	@Override
	public String getPrimaryKeySelect() {

		return null;
	}

	@Override
	public RowMapper<TipiSedeDTO> getRowMapper() throws SQLException {
		 return new TipoSedeRowMapper();
	}

    /**
     * The type Tipo sede row mapper.
     */
    public static class TipoSedeRowMapper implements RowMapper<TipiSedeDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoSedeRowMapper() throws SQLException {
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
        public TipiSedeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiSedeDTO bean = new TipiSedeDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiSedeDTO bean) throws SQLException {
            bean.setIdTipoSede(rs.getLong("id_tipo_sede"));
            bean.setCodTipoSede(rs.getString("cod_tipo_sede"));
            bean.setDesTipoSede(rs.getString("des_tipo_sede"));
            bean.setOrdinaTipoSede(rs.getLong("ordina_tipo_sede"));
            bean.setIndDefault(rs.getString("ind_default"));
        }
    }

	@Override
	public RowMapper<TipiSedeDTO> getExtendedRowMapper() throws SQLException {

		return new TipiSedeExtendedRowMapper();
	}
	
	
    /**
     * The type Tipo sede extended row mapper.
     */
    public static class TipiSedeExtendedRowMapper implements RowMapper<TipiSedeDTO> {

        /**
         * Instantiates a new Tipo sede extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiSedeExtendedRowMapper() throws SQLException {
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
        public TipiSedeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiSedeDTO bean = new TipiSedeDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiSedeDTO bean) throws SQLException {
            bean.setIdTipoSede(rs.getLong("id_tipo_sede"));
            bean.setCodTipoSede(rs.getString("cod_tipo_sede"));
            bean.setDesTipoSede(rs.getString("des_tipo_sede"));
            bean.setOrdinaTipoSede(rs.getLong("ordina_tipo_sede"));
            bean.setIndDefault(rs.getString("ind_default"));
        }
    }


}
