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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDettVersDAO;
import it.csi.risca.riscabesrv.dto.SollDettVersDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type SollDatiAmministr dao.
 *
 * @author CSI PIEMONTE
 */
public class SollDettVersDAOImpl extends RiscaBeSrvGenericDAO<SollDettVersDTO> implements SollDettVersDAO {

	public static final String QUERY_INSERT_W = " INSERT INTO risca_w_soll_dett_vers "
			+ "(id_soll_dett_vers, id_accertamento, codice_utenza, scadenza_pagamento, importo_versato, "
			+ "data_versamento, interessi_maturati, giorni_ritardo) "
			+ "VALUES(:idSollDettVers, :idAccertamento, :codiceUtenza, :scadenzaPagamento, :importoVersato, "
			+ ":dataVersamento, :interessiMaturati, :giorniRitardo) ";

	public static final String QUERY_DELETE_W_BY_ELABORA = " delete from risca_w_soll_dett_vers "
			+ " where id_accertamento in "
			+ " (SELECT id_accertamento FROM RISCA_W_ACCERTAMENTO  WHERE id_elabora = :idElabora)";
	
	public static final String QUERY_DELETE_BY_ID_ACCERTAMENTO = "DELETE FROM RISCA_R_SOLL_DETT_VERS WHERE id_accertamento = :idAccertamento";

	public static final String QUERY_COPY_FROM_WORKING = "INSERT INTO RISCA_R_SOLL_DETT_VERS("
			+ "id_soll_dett_vers, id_accertamento,codice_utenza,  "
			+ "scadenza_pagamento, importo_versato, data_versamento, interessi_maturati, giorni_ritardo, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd , gest_attore_upd , gest_uid) "
			+ "SELECT id_soll_dett_vers, id_accertamento, codice_utenza, scadenza_pagamento, "
			+ "importo_versato, data_versamento, interessi_maturati, giorni_ritardo, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid "
			+ "FROM RISCA_W_SOLL_DETT_VERS a WHERE a.id_accertamento in ( "
			+ "	SELECT b.id_accertamento FROM RISCA_W_ACCERTAMENTO b " + "	WHERE b.id_elabora = :idElabora) ";

	@Override
	public SollDettVersDTO saveSollDettVersWorking(SollDettVersDTO dto) throws DAOException {
		LOGGER.debug("[SollDettVersDAOImpl::saveSollDettVersWorking] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Long genId = findNextSequenceValue("seq_risca_r_soll_dett_vers");

			map.put("idSollDettVers", genId);
			map.put("idAccertamento", dto.getIdAccertamento());
			map.put("codiceUtenza", dto.getCodiceUtenza());
			map.put("scadenzaPagamento", dto.getScadenzaPagamento());
			map.put("importoVersato", dto.getImportoVersato());
			map.put("dataVersamento", dto.getDataVersamento());
			map.put("interessiMaturati", dto.getInteressiMaturati());
			map.put("giorniRitardo", dto.getGiorniRitardo());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_W, null, null), params, keyHolder);
			dto.setIdSollDettVers(genId);

			return dto;
		} catch (Exception e) {
			LOGGER.error("[SollDettVersDAOImpl::saveSollDettVersWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDettVersDAOImpl::saveSollDettVersWorking] END");
		}
	}

	@Override
	public Integer deleteSollDettVersWorkingByElabora(Long idElabora) throws DAOException {
		LOGGER.debug("[SollDettVersDAOImpl::deleteSollDettVersWorkingByElabora] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_W_BY_ELABORA, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDettVersDAOImpl::deleteSollDettVersWorkingByElabora] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDettVersDAOImpl::deleteSollDettVersWorkingByElabora] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteSollDettVersByIdAccertamento(Long idAccertamento) throws DAOException {
		LOGGER.debug("[SollDettVersDAOImpl::deleteSollDettVersByIdAccertamento] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAccertamento", idAccertamento);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_BY_ID_ACCERTAMENTO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDettVersDAOImpl::deleteSollDettVersByIdAccertamento] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDettVersDAOImpl::deleteSollDettVersByIdAccertamento] END");
		}

		return res;
	}

	@Override
	public Integer copySollDettVersFromWorking(Long idElabora, String attore) throws DAOException {
		LOGGER.debug("[SollDettVersDAOImpl::copySollDettVersFromWorking] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestAttoreIns", attore);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataIns", now);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_COPY_FROM_WORKING, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDettVersDAOImpl::copySollDettVersFromWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDettVersDAOImpl::copySollDettVersFromWorking] END");
		}
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
	public RowMapper<SollDettVersDTO> getRowMapper() throws SQLException {
		return new SollDettVersRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SollDettVersDTO> getExtendedRowMapper() throws SQLException {
		return new SollDettVersRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class SollDettVersRowMapper implements RowMapper<SollDettVersDTO> {

		/**
		 * Instantiates a new AvvisoDatiTitolare row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SollDettVersRowMapper() throws SQLException {
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
		public SollDettVersDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SollDettVersDTO bean = new SollDettVersDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SollDettVersDTO bean) throws SQLException {
			bean.setIdSollDettVers(getLong(rs, "id_soll_dett_vers"));
			bean.setIdAccertamento(getLong(rs, "id_accertamento"));
			bean.setCodiceUtenza(rs.getString("codice_utenza"));
			bean.setScadenzaPagamento(rs.getString("scadenza_pagamento"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setDataVersamento(rs.getDate("data_versamento"));
			bean.setInteressiMaturati(rs.getBigDecimal("interessi_maturati"));
			bean.setGiorniRitardo(rs.getInt("giorni_ritardo"));
		}

		private boolean rsHasColumn(ResultSet rs, String column) {
			try {
				rs.findColumn(column);
				return true;
			} catch (SQLException sqlex) {
				// Column not present in resultset
			}
			return false;
		}

		private Long getLong(ResultSet rs, String strColName) throws SQLException {
			long nValue = rs.getLong(strColName);
			return rs.wasNull() ? null : nValue;
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}