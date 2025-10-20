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
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TernaUtenzeDAO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type TernaUtenze dao.
 *
 * @author CSI PIEMONTE
 */
public class TernaUtenzeDAOImpl extends RiscaBeSrvGenericDAO<String> implements TernaUtenzeDAO {

	private static final String SELECT_DISTINCT_COD_UTENZA = " select DISTINCT trim(cod_utenza) cod_utenza from RISCA_T_TERNA_UTENZE "
			+ " order by cod_utenza ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> loadDistinctCodUtenza() throws DAOException {
		try {
			LOGGER.debug("[TernaUtenzeDAOImpl::loadDistinctCodUtenza] BEGIN");
			MapSqlParameterSource params = new MapSqlParameterSource();
			return template.query(SELECT_DISTINCT_COD_UTENZA, params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("[TernaUtenzeDAOImpl::loadDistinctCodUtenza] error: ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public RowMapper<String> getRowMapper() {
		return new StringRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return new StringRowMapper();
	}

	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	public static class StringRowMapper implements RowMapper<String> {

		/**
		 * Instantiates a new String row mapper.
		 */
		public StringRowMapper() {
			// Instatiate class
		}

		/**
		 * Implementations must implement this method to map each row of data in the
		 * ResultSet. This method should not call {@code next()} on the ResultSet; it is
		 * only supposed to map values of the current row.
		 *
		 * @param rs     the ResultSet to map (pre-initialized for the current row)
		 * @param rowNum the number of the current row
		 * @return the result object for the current row (may be {@code null})
		 * @throws SQLException if a SQLException is encountered getting column values
		 *                      (that is, there's no need to catch SQLException)
		 */
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("cod_utenza");
		}
	}

	@Override
	public Long decodeId(String fromTableName, String searchFieldCriteris) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long findNextSequenceValue(String sequenceName) throws DataAccessException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

}