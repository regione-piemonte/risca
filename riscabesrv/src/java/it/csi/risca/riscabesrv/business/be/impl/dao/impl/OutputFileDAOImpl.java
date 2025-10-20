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

import it.csi.risca.riscabesrv.business.be.impl.dao.OutputFileDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.OutputFileDTO;

/**
 * The type OutputFileDAOImpl
 *
 * @author CSI PIEMONTE
 */
public class OutputFileDAOImpl extends RiscaBeSrvGenericDAO<OutputFileDTO> implements OutputFileDAO {

	private static final String QUERY_LOAD_OUTPUT_FILE = "SELECT * FROM risca_d_output_file "
			+ "WHERE id_output_file = :idOutputFile ";

	private static final String QUERY_LOAD_BY_AMBITO_TIPO_ELAB_FILE = " select rdof.* from risca_d_output_file rdof "
			+ "inner join risca_d_tipo_elabora rdte on rdte.id_tipo_elabora = rdof.id_tipo_elabora "
			+ "inner join risca_d_ambito rda on rda.id_ambito = rdte.id_ambito  " + "where rda.cod_ambito = :codAmbito "
			+ "and rdte.cod_tipo_elabora = :codTipoElabora " + "and nome_file = :nomeFile ";

	@Override
	public OutputFileDTO loadOutputFile(Long idOutputFile) {
		LOGGER.debug("[OutputFileDAOImpl::loadOutputFile] BEGIN");
		OutputFileDTO outputFileDTO = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idOutputFile", idOutputFile);
			MapSqlParameterSource params = getParameterValue(map);
			outputFileDTO = template.queryForObject(QUERY_LOAD_OUTPUT_FILE, params, getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[OutputFileDAOImpl::loadOutputFile] No record found in database for idOutputFile: " + idOutputFile,
					e);
			return outputFileDTO;
		} catch (SQLException e) {
			LOGGER.error("[OutputFileDAOImpl::loadOutputFile] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[OutputFileDAOImpl::loadFonteByCodFonte] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[FonteDaoImpl::loadOutputFile] END");
		}
		return outputFileDTO;
	}

	@Override
	public OutputFileDTO loadOutputFileByAmbitoTipoElabNomeFile(String codAmbito, String codTipoElabora,
			String nomeFile) throws Exception {
		LOGGER.debug("[OutputFileDAOImpl::loadOutputFileByAmbitoTipoElabNomeFile] BEGIN");
		OutputFileDTO outputFileDTO = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codAmbito", codAmbito);
			map.put("codTipoElabora", codTipoElabora);
			map.put("nomeFile", nomeFile);
			MapSqlParameterSource params = getParameterValue(map);
			outputFileDTO = template.queryForObject(QUERY_LOAD_BY_AMBITO_TIPO_ELAB_FILE, params, getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[OutputFileDAOImpl::loadOutputFileByAmbitoTipoElabNomeFile] No record found in database for codAmbito: "
							+ codAmbito + ", codTipoElabora: " + codTipoElabora + ", nomeFile: " + nomeFile,
					e);
			return outputFileDTO;
		} catch (SQLException e) {
			LOGGER.error(
					"[OutputFileDAOImpl::loadOutputFileByAmbitoTipoElabNomeFile] Errore nell'esecuzione della query",
					e);
			throw new Exception(e);
		} catch (DataAccessException e) {
			LOGGER.error("[OutputFileDAOImpl::loadOutputFileByAmbitoTipoElabNomeFile] Errore nell'accesso ai dati", e);
			throw new Exception(e);
		} finally {
			LOGGER.debug("[FonteDaoImpl::loadOutputFileByAmbitoTipoElabNomeFile] END");
		}
		return outputFileDTO;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<OutputFileDTO> getRowMapper() throws SQLException {
		return new OutputFileMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return new OutputFileMapper();
	}

	/**
	 * The type output file row mapper.
	 */
	public static class OutputFileMapper implements RowMapper<OutputFileDTO> {

		/**
		 * Instantiates a new Fonte row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public OutputFileMapper() throws SQLException {
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
		public OutputFileDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			OutputFileDTO bean = new OutputFileDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, OutputFileDTO bean) throws SQLException {
			bean.setIdOutputFile(rs.getLong("id_output_file"));
			bean.setIdTipoElabora(rs.getLong("id_tipo_elabora"));
			bean.setIdTipoPassoElabora(rs.getLong("id_tipo_passo_elabora"));
			bean.setNomeFile(rs.getString("nome_file"));
			bean.setTipoFile(rs.getString("tipo_file"));
			bean.setCodReport(rs.getString("cod_report"));
			bean.setDesReport(rs.getString("des_report"));
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}
}
