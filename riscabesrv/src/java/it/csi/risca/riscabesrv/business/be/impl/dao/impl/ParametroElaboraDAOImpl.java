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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.impl.dao.ParametroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.ParametroElaboraDTO;

/**
 * The type ParametroElabora dao.
 *
 * @author CSI PIEMONTE
 */
public class ParametroElaboraDAOImpl extends RiscaBeSrvGenericDAO<ParametroElaboraDTO> implements ParametroElaboraDAO {

	private static final String QUERY_LOAD_PARAMETRO_ELABORA_BY_ELABORA_AND_RAGGRUPPAMENTO = "SELECT * FROM risca_r_parametro_elabora "
			+ "WHERE id_elabora = :idElabora " + "AND raggruppamento = :raggruppamento";

	private static final String QUERY_INSERT_PARAMETRO_ELABORA = "INSERT INTO risca_r_parametro_elabora(id_parametro_elabora, id_elabora, "
			+ "raggruppamento, chiave, valore)"
			+ "VALUES(:idParametroElabora, :idElabora, :raggruppamento, :chiave, :valore)";

	private static final String QUERY_UPDATE_PARAMETRO_ELABORA = "UPDATE risca_r_parametro_elabora SET id_elabora = :idElabora, "
			+ "raggruppamento = :raggruppamento, chiave = :chiave, valore = :valore "
			+ "WHERE id_parametro_elabora = :idParametroElabora";

	private static final String ORDER_BY_CHIAVE= " ORDER BY chiave " ;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ParametroElaboraDTO> loadParametroElaboraByElaboraRaggruppamento(String idElabora, String raggruppamento) {
		LOGGER.debug("[ParametroElaboraDAOImpl::loadParametroElaboraByElaboraRaggruppamento] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", Long.parseLong(idElabora));
			map.put("raggruppamento", raggruppamento);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_PARAMETRO_ELABORA_BY_ELABORA_AND_RAGGRUPPAMENTO +ORDER_BY_CHIAVE, null, null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[ParametroElaboraDAOImpl::loadParametroElaboraByElaboraRaggruppamento] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[ParametroElaboraDAOImpl::loadParametroElaboraByElaboraRaggruppamento] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[ParametroElaboraDAOImpl::loadParametroElaboraByElaboraRaggruppamento] END");
		}
	}

	@Override
	public ParametroElaboraDTO saveParametroElabora(ParametroElaboraDTO dto) throws Exception {
		LOGGER.debug("[ParametroElaboraDAOImpl::saveParametroElabora] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();

			Long genId = findNextSequenceValue("seq_risca_r_parametro_elabora");

			map.put("idParametroElabora", genId);
			map.put("idElabora", dto.getIdElabora());
			map.put("raggruppamento", dto.getRaggruppamento());
			map.put("chiave", dto.getChiave());
			map.put("valore", dto.getValore());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_PARAMETRO_ELABORA, null, null), params, keyHolder);
			dto.setIdParametroElabora(genId);
		} catch (Exception e) {
			LOGGER.error("[ParametroElaboraDAOImpl::saveParametroElabora] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;

		} finally {
			LOGGER.debug("[ParametroElaboraDAOImpl::saveParametroElabora] END");
		}

		return dto;
	}

	@Override
	public ParametroElaboraDTO updateParametroElabora(ParametroElaboraDTO dto) throws Exception {
		LOGGER.debug("[ParametroElaboraDAOImpl::updateParametroElabora] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();

			map.put("idElabora", dto.getIdElabora());
			map.put("raggruppamento", dto.getRaggruppamento());
			map.put("chiave", dto.getChiave());
			map.put("valore", dto.getValore());
			map.put("idParametroElabora", dto.getIdParametroElabora());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_UPDATE_PARAMETRO_ELABORA, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[ParametroElaboraDAOImpl::updateParametroElabora] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[ParametroElaboraDAOImpl::updateParametroElabora] END");
		}

		return dto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowMapper<ParametroElaboraDTO> getRowMapper() throws SQLException {
		return new ParametroElaboraRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<ParametroElaboraDTO> getExtendedRowMapper() throws SQLException {
		return new ParametroElaboraRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class ParametroElaboraRowMapper implements RowMapper<ParametroElaboraDTO> {

		/**
		 * Instantiates a new Elabora row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public ParametroElaboraRowMapper() throws SQLException {
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
		public ParametroElaboraDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ParametroElaboraDTO bean = new ParametroElaboraDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, ParametroElaboraDTO bean) throws SQLException {
			bean.setIdParametroElabora(rs.getLong("id_parametro_elabora"));
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setRaggruppamento(rs.getString("raggruppamento"));
			bean.setChiave(rs.getString("chiave"));
			bean.setValore(rs.getString("valore"));
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}