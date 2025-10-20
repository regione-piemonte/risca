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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.OutputFoglioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.OutputFoglioDTO;

/**
 * The type OutputFoglioDAOImpl
 *
 * @author CSI PIEMONTE
 */
public class OutputFoglioDAOImpl extends RiscaBeSrvGenericDAO<OutputFoglioDTO> implements OutputFoglioDAO {

	private static final String QUERY_LOAD_OUTPUT_FOGLIO_BY_FILE = " select * "
			+ " from risca_d_output_foglio "
			+ " where id_output_file = :idOutputFile "
			+ " order by id_output_foglio ";
	
	@Override
	public List<OutputFoglioDTO> loadOutputFoglioByFile(Long idOutputFile) throws Exception {
		LOGGER.debug("[OutputFoglioDAOImpl::loadOutputFoglioByFile] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idOutputFile", idOutputFile);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_OUTPUT_FOGLIO_BY_FILE, null, null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[OutputFoglioDAOImpl::loadOutputFoglioByFile] Errore nell'esecuzione della query", e);
			throw new Exception(e);
		} catch (DataAccessException e) {
			LOGGER.error("[OutputFoglioDAOImpl::loadOutputFoglioByFile] Errore nell'accesso ai dati", e);
			throw new Exception(e);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::loadOutputFoglioByFile] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<OutputFoglioDTO> getRowMapper() throws SQLException {
		return new OutputFoglioMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return new OutputFoglioMapper();
	}
	 /**
     * The type output foglio row mapper.
     */
    public static class OutputFoglioMapper implements RowMapper<OutputFoglioDTO> {

        /**
         * Instantiates a new Fonte row mapper.
         *
         * @throws SQLException the sql exception
         */
        public OutputFoglioMapper() throws SQLException {
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
        public OutputFoglioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	OutputFoglioDTO bean = new OutputFoglioDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, OutputFoglioDTO bean) throws SQLException {
            bean.setIdOutputFoglio(rs.getLong("id_output_foglio"));
            bean.setIdOutputFile(rs.getLong("id_output_file"));
            bean.setNomeFoglio(rs.getString("nome_foglio"));
            bean.setNumeroCampi(rs.getInt("numero_campi"));
        }
    }
	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}
}
