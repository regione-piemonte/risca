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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.EmailStPuntiValoriDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.EmailStPuntiValoriDTO;

/**
 * The type EmailStandard dao Impl.
 *
 * @author CSI PIEMONTE
 */
public class EmailStPuntiValoriDAOImpl extends RiscaBeSrvGenericDAO<EmailStPuntiValoriDTO>
		implements EmailStPuntiValoriDAO {

	private static final String QUERY_LOAD_EMAIL_ST_PUNTI_VALORI = " select * from risca_r_email_st_punti_valori rrespv "
			+ " inner join risca_d_email_standard rdes on rrespv.id_email_standard = rdes.id_email_standard  "
			+ " inner join risca_d_output_foglio rdof on rrespv.id_output_foglio = rdof.id_output_foglio  "
			+ " where rdes.cod_email_standard = :codEmailStandard " + " and rdof.nome_foglio = :nomeFoglio ";

	@Override
	public List<EmailStPuntiValoriDTO> loadEmailStPuntiValoriByEmailFoglio(String codEmailStandard, String nomeFoglio) {
		LOGGER.debug("[EmailStPuntiValoriDAOImpl::loadEmailStPuntiValoriByEmailFoglio] BEGIN");
		List<EmailStPuntiValoriDTO> puntiValori = new ArrayList<EmailStPuntiValoriDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codEmailStandard", codEmailStandard);
			map.put("nomeFoglio", nomeFoglio);
			MapSqlParameterSource params = getParameterValue(map);
			puntiValori = template.query(QUERY_LOAD_EMAIL_ST_PUNTI_VALORI, params, getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[EmailStPuntiValoriDAOImpl::loadEmailStPuntiValoriByEmailFoglio]No record found in database for codEmailStandard: "
							+ codEmailStandard);
			return puntiValori;
		} catch (SQLException e) {
			LOGGER.error(
					"[EmailStPuntiValoriDAOImpl::loadEmailStPuntiValoriByEmailFoglio] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[EmailStPuntiValoriDAOImpl::loadEmailStPuntiValoriByEmailFoglio] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[EmailStPuntiValoriDAOImpl::loadEmailStPuntiValoriByEmailFoglio] END");
		}
		return puntiValori;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<EmailStPuntiValoriDTO> getRowMapper() throws SQLException {
		return new EmailStPuntiValoriRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}

	/**
	 * The type EmailStPuntiValori row mapper.
	 */
	public static class EmailStPuntiValoriRowMapper implements RowMapper<EmailStPuntiValoriDTO> {

		/**
		 * Instantiates a new Fonte row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public EmailStPuntiValoriRowMapper() throws SQLException {
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
		public EmailStPuntiValoriDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailStPuntiValoriDTO bean = new EmailStPuntiValoriDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, EmailStPuntiValoriDTO bean) throws SQLException {
			bean.setIdEmailStPuntiValori(rs.getLong("id_email_st_punti_valori"));
			bean.setIdEmailStandard(rs.getLong("id_email_standard"));
			bean.setIdOutputColonna(rs.getLong("id_output_colonna"));
			bean.setIdOutputFoglio(rs.getLong("id_output_foglio"));
			bean.setPuntamento(rs.getString("puntamento"));
		}
	}

}
