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

import it.csi.risca.riscabesrv.business.be.impl.dao.OutputColonnaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.OutputColonnaDTO;

/**
 * The type OutputColonnaDAOImpl
 *
 * @author CSI PIEMONTE
 */
public class OutputColonnaDAOImpl extends RiscaBeSrvGenericDAO<OutputColonnaDTO> implements OutputColonnaDAO {

	private static final String QUERY_LOAD_OUTPUT_COLONNA_BY_FOGLIO = "select * "
			+ "from risca_d_output_colonna "
			+ "where id_output_foglio = :idOutputFoglio "
			+ "order by progressivo ";
	
	@Override
	public List<OutputColonnaDTO> loadOutputColonnaByFoglio(Long idOutputFoglio) throws Exception {
		LOGGER.debug("[OutputFoglioDAOImpl::loadOutputColonnaByFoglio] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idOutputFoglio", idOutputFoglio);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_OUTPUT_COLONNA_BY_FOGLIO, null, null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[OutputFoglioDAOImpl::loadOutputColonnaByFoglio] Errore nell'esecuzione della query", e);
			throw new Exception(e);
		} catch (DataAccessException e) {
			LOGGER.error("[OutputFoglioDAOImpl::loadOutputColonnaByFoglio] Errore nell'accesso ai dati", e);
			throw new Exception(e);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::loadOutputColonnaByFoglio] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<OutputColonnaDTO> getRowMapper() throws SQLException {
		return new OutputColonnaMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return new OutputColonnaMapper();
	}
	 /**
     * The type output file row mapper.
     */
    public static class OutputColonnaMapper implements RowMapper<OutputColonnaDTO> {

        /**
         * Instantiates a new Output colonna row mapper.
         *
         * @throws SQLException the sql exception
         */
        public OutputColonnaMapper() throws SQLException {
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
        public OutputColonnaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	OutputColonnaDTO bean = new OutputColonnaDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, OutputColonnaDTO bean) throws SQLException {
            bean.setIdOutputColonna(rs.getLong("id_output_colonna"));
            bean.setIdOutputFoglio(rs.getLong("id_output_foglio"));
            bean.setIdTipoDatoColonna(rs.getLong("id_tipo_dato_colonna"));
            bean.setDescEtichetta(rs.getString("desc_etichetta"));
            bean.setProgressivo(rs.getInt("progressivo"));
        }
    }
	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}
}
