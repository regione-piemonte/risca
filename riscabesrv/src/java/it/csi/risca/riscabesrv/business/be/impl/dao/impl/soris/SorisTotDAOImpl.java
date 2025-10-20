/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.soris;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisTotDAO;
import it.csi.risca.riscabesrv.dto.soris.SorisTotDTO;

/**
 * The type SorisTot dao.
 *
 * @author CSI PIEMONTE
 */
public class SorisTotDAOImpl extends RiscaBeSrvGenericDAO<SorisTotDTO> implements SorisTotDAO {

	private static final String QUERY_INSERT = " INSERT INTO risca_w_soris_tot "
			+ " (id_elabora, campo_tot) "
			+ " VALUES(:idElabora, :campoTot) ";

	
	@Override
	public SorisTotDTO saveSorisTot(SorisTotDTO dto) {
		LOGGER.debug("[SorisTotDAOImpl::saveSorisTot] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", dto.getIdElabora());
			map.put("campoTot", dto.getCampoTot());			

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SorisTotDAOImpl::saveSorisTot] ERROR : " + e);
			return null;
		} finally {
			LOGGER.debug("[SorisTotDAOImpl::saveSorisTot] END");
		}

		return dto;
	}

	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowMapper<SorisTotDTO> getRowMapper() throws SQLException {
		return new SorisTotRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SorisTotDTO> getExtendedRowMapper() throws SQLException {
		return new SorisTotRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class SorisTotRowMapper implements RowMapper<SorisTotDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SorisTotRowMapper() throws SQLException {
			// Instantiate class
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
		public SorisTotDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SorisTotDTO bean = new SorisTotDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SorisTotDTO bean) throws SQLException {
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setCampoTot(rs.getString("campo_tot"));			
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}



}