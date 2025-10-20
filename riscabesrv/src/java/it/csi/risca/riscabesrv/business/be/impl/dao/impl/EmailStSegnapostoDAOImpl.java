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

import it.csi.risca.riscabesrv.business.be.impl.dao.EmailStSegnapostoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.EmailStSegnapostoDTO;

/**
 * The type EmailStandard dao Impl.
 *
 * @author CSI PIEMONTE
 */
public class EmailStSegnapostoDAOImpl extends RiscaBeSrvGenericDAO<EmailStSegnapostoDTO>
		implements EmailStSegnapostoDAO {

	private static final String QUERY_LOAD_EMAIL_ST_SEGNAPOSTO = " SELECT id_email_st_segnaposto, sp.id_email_st_punti_valori, segnaposto, sezione, id_output_colonna		 "
			+ "FROM RISCA_R_EMAIL_ST_SEGNAPOSTO sp, RISCA_R_EMAIL_ST_PUNTI_VALORI pv "
			+ "WHERE sp.id_email_st_punti_valori = pv.id_email_st_punti_valori  " + "AND sp.sezione = :sezione "
			+ "AND pv.id_email_standard  = :idEmailStandard ";

	@Override
	public List<EmailStSegnapostoDTO> loadEmailStSegnapostoByEmailSezione(Long idEmailStandard, String sezione) {
		LOGGER.debug("[EmailStSegnapostoDAOImpl::loadEmailStSegnapostoByEmailSezione] BEGIN");
		List<EmailStSegnapostoDTO> segnaposto = new ArrayList<EmailStSegnapostoDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idEmailStandard", idEmailStandard);
			map.put("sezione", sezione);
			MapSqlParameterSource params = getParameterValue(map);
			segnaposto = template.query(QUERY_LOAD_EMAIL_ST_SEGNAPOSTO, params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error(
					"[EmailStSegnapostoDAOImpl::loadEmailStSegnapostoByEmailSezione] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[EmailStSegnapostoDAOImpl::loadEmailStSegnapostoByEmailSezione] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[EmailStSegnapostoDAOImpl::loadEmailStPuntiValoriByEmailFoglio] END");
		}
		return segnaposto;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<EmailStSegnapostoDTO> getRowMapper() throws SQLException {
		return new EmailStSegnapostoRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}

	/**
	 * The type EmailStPuntiValori row mapper.
	 */
	public static class EmailStSegnapostoRowMapper implements RowMapper<EmailStSegnapostoDTO> {

		/**
		 * Instantiates a new Fonte row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public EmailStSegnapostoRowMapper() throws SQLException {
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
		public EmailStSegnapostoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailStSegnapostoDTO bean = new EmailStSegnapostoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, EmailStSegnapostoDTO bean) throws SQLException {
			bean.setIdEmailStSegnaposto(rs.getLong("id_email_st_segnaposto"));
			bean.setIdEmailStPuntiValori(rs.getLong("id_email_st_punti_valori"));
			bean.setSegnaposto(rs.getString("segnaposto"));
			bean.setSezione(rs.getString("sezione"));
			bean.setIdOutputColonna(rs.getLong("id_output_colonna"));
		}
	}

}
