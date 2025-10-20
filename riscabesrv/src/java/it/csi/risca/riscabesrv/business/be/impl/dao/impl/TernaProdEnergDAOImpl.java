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
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TernaProdEnergDAO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type TernaUtenze dao.
 *
 * @author CSI PIEMONTE
 */
public class TernaProdEnergDAOImpl extends RiscaBeSrvGenericDAO<BigDecimal> implements TernaProdEnergDAO {

	private static final String SELECT_ENERGIA_PRODOTTA = " select coalesce(SUM(energia_pro_kwh) / 1000, 0) as value "
			+ " from RISCA_R_TERNA_PROD_ENERG where cod_utenza = :codUtenza and TO_CHAR(data, 'YYYY') = :anno ";

	private static final String SELECT_RICAVI_ANNO = " select coalesce(SUM((a.energia_pro_kwh/1000) * b.prezzo_nord), 0) as value "
			+ " from RISCA_R_TERNA_PROD_ENERG a, RISCA_T_TERNA_PREZZI_ENERG b where a.cod_utenza = :codUtenza "
			+ " and TRUNC(a.data) = TRUNC(b.data) and LPAD(a.ora, 2, '0') = LPAD(b.ora, 2, '0') "
			+ " and TO_CHAR(a.data, 'YYYY') = :anno ";


	@Override
	public BigDecimal loadTotEnergProdAnno(String codUtenza, String anno) throws Exception {
		try {
			LOGGER.debug("[TernaProdEnergDAOImpl::loadTotEnergProdAnno] BEGIN");
			Map<String, Object> map = new HashMap<>();
			map.put("codUtenza", codUtenza);
			map.put("anno", anno);
			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(SELECT_ENERGIA_PRODOTTA, params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("[TernaProdEnergDAOImpl::loadTotEnergProdAnno] error: ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		}
	}

	@Override
	public BigDecimal loadTotRicaviAnno(String codUtenza, String anno) throws Exception {
		try {
			LOGGER.debug("[TernaProdEnergDAOImpl::loadTotRicaviAnno] BEGIN");
			Map<String, Object> map = new HashMap<>();
			map.put("codUtenza", codUtenza);
			map.put("anno", anno);
			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(SELECT_RICAVI_ANNO, params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("[TernaProdEnergDAOImpl::loadTotRicaviAnno] error: ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public RowMapper<BigDecimal> getRowMapper() {
		return new BigDecimalRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return new BigDecimalRowMapper();
	}

	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	public static class BigDecimalRowMapper implements RowMapper<BigDecimal> {

		/**
		 * Instantiates a new String row mapper.
		 */
		public BigDecimalRowMapper() {
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
		public BigDecimal mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getBigDecimal("value");
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