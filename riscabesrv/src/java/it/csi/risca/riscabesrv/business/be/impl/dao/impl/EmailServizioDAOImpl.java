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

import it.csi.risca.riscabesrv.business.be.impl.dao.EmailServizioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.EmailServizioDTO;

/**
 * The type EmailServizio dao Impl.
 *
 * @author CSI PIEMONTE
 */
public class EmailServizioDAOImpl extends RiscaBeSrvGenericDAO<EmailServizioDTO> implements EmailServizioDAO {

	private static final String QUERY_LOAD_EMAIL_SERVIZIO_BY_COD = " select * from risca_d_email_servizio where cod_email_servizio = :codEmailServizio ";

	@Override
	public EmailServizioDTO loadEmailServizioByCodEmail(String codEmailServizio) {
		LOGGER.debug("[EmailServizioDAOImpl::loadEmailServizioByCodEmail] BEGIN");
		EmailServizioDTO EmailServizioDTO = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codEmailServizio", codEmailServizio);
			MapSqlParameterSource params = getParameterValue(map);
			EmailServizioDTO = template.queryForObject(QUERY_LOAD_EMAIL_SERVIZIO_BY_COD, params, getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[EmailServizioDAOImpl::loadEmailServizioByCodEmail]No record found in database for codEmailServizio: "
							+ codEmailServizio);
			return EmailServizioDTO;
		} catch (SQLException e) {
			LOGGER.error("[EmailServizioDAOImpl::loadEmailServizioByCodEmail] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[EmailServizioDAOImpl::loadEmailServizioByCodEmail] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[EmailServizioDAOImpl::loadEmailServizioByCodEmail] END");
		}
		return EmailServizioDTO;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<EmailServizioDTO> getRowMapper() throws SQLException {
		return new EmailServizioRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}

	/**
	 * The type fonte row mapper.
	 */
	public static class EmailServizioRowMapper implements RowMapper<EmailServizioDTO> {

		/**
		 * Instantiates a new Fonte row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public EmailServizioRowMapper() throws SQLException {
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
		public EmailServizioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailServizioDTO bean = new EmailServizioDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, EmailServizioDTO bean) throws SQLException {
			bean.setIdEmailServizio(rs.getLong("id_email_servizio"));
			bean.setCodEmailServizio(rs.getString("cod_email_servizio"));
			bean.setDesEmailServizio(rs.getString("des_email_servizio"));
			bean.setOggettoEmailServizio(rs.getString("oggetto_email_servizio"));
			bean.setIdFaseElabora(rs.getLong("id_fase_elabora"));
		}
	}

}
