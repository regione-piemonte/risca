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
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.EmailStandardDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.EmailStandardDTO;

/**
 * The type EmailStandard dao Impl.
 *
 * @author CSI PIEMONTE
 */
public class EmailStandardDAOImpl extends RiscaBeSrvGenericDAO<EmailStandardDTO> implements EmailStandardDAO {

	private static final String QUERY_LOAD_EMAIL_STANDARD_BY_COD = " select * from risca_d_email_standard rdes  where cod_email_standard  = :codEmailStandard ";

	@Override
	public EmailStandardDTO loadEmailStandardByCodEmail(String codEmailStandard) {
		LOGGER.debug("[EmailStandardDAOImpl::loadEmailStandardByCodEmail] BEGIN");
		EmailStandardDTO EmailStandardDTO = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codEmailStandard", codEmailStandard);
			MapSqlParameterSource params = getParameterValue(map);
			EmailStandardDTO = template.queryForObject(QUERY_LOAD_EMAIL_STANDARD_BY_COD, params, getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[EmailStandardDAOImpl::loadEmailStandardByCodEmail]No record found in database for codEmailStandard: "
							+ codEmailStandard);
			return EmailStandardDTO;
		} catch (SQLException e) {
			LOGGER.error("[EmailStandardDAOImpl::loadEmailStandardByCodEmail] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[EmailStandardDAOImpl::loadEmailStandardByCodEmail] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[EmailStandardDAOImpl::loadEmailStandardByCodEmail] END");
		}
		return EmailStandardDTO;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<EmailStandardDTO> getRowMapper() throws SQLException {
		return new EmailStandardRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}

	/**
	 * The type fonte row mapper.
	 */
	public static class EmailStandardRowMapper implements RowMapper<EmailStandardDTO> {

		/**
		 * Instantiates a new Fonte row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public EmailStandardRowMapper() throws SQLException {
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
		public EmailStandardDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailStandardDTO bean = new EmailStandardDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, EmailStandardDTO bean) throws SQLException {
			bean.setIdEmailStandard(rs.getLong("id_email_standard"));
			bean.setIdFaseElabora(rs.getLong("id_fase_elabora"));
			bean.setCodEmailStandard(rs.getString("cod_email_standard"));
			bean.setDesEmailStandard(rs.getString("des_email_standard"));
			bean.setOggettoEmail(rs.getString("oggetto_email"));
			bean.setTestoEmail(rs.getString("testo_email"));
			bean.setAllegatoEmail(rs.getString("allegato_email"));
		}
	}

}
