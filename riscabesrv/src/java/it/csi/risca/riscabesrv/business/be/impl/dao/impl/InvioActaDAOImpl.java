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
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.InvioActaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.InvioActaDTO;

/**
 * The type InvioActaDAO dao.
 *
 * @author CSI PIEMONTE
 */
public class InvioActaDAOImpl extends RiscaBeSrvGenericDAO<InvioActaDTO> implements InvioActaDAO {

	private static final String QUERY_LOAD_INVIO_ACTA_BY_NAP = " select * from risca_t_invio_acta where nap = :nap ";

	private static final String QUERY_LOAD_INVIO_ACTA_BY_ID_ACCERTAMENTO = " select * from risca_t_invio_acta where id_accertamento = :idAccertamento ";

	private static final String QUERY_SAVE_INVIO_ACTA = "INSERT INTO risca_t_invio_acta "
			+ "(id_invio_acta, nap, id_spedizione_acta, nome_file, flg_multiclassificazione, flg_archiviata_acta, data_invio, uuid_message, "
			+ "data_esito_acta, cod_esito_acquisizione_acta, desc_esito_acquisizione_acta,  "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid, cod_esito_invio_acta, desc_esito_invio_acta, id_accertamento) "
			+ "VALUES(:idInvioActa, :nap, :idSpedizioneActa, :nomeFile, :flgMulticlassificazione, :flgArchiviataActa, :dataInvio, :uuidMessage, "
			+ ":dataEsitoActa, :codEsitoAcquisizioneActa, :descEsitoAcquisizioneActa,  "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid, :codEsitoInvioActa, :descEsitoInvioActa, :idAccertamento)";

	private static final String QUERY_UPDATE_INVIO_ACTA = "UPDATE risca_t_invio_acta "
			+ "SET nap = :nap, id_spedizione_acta = :idSpedizioneActa, nome_file = :nomeFile, flg_multiclassificazione = :flgMulticlassificazione, "
			+ "flg_archiviata_acta = :flgArchiviataActa, data_invio = :dataInvio, uuid_message = :uuidMessage, data_esito_acta = :dataEsitoActa, "
			+ "cod_esito_acquisizione_acta = :codEsitoAcquisizioneActa, desc_esito_acquisizione_acta = :descEsitoAcquisizioneActa, "
			+ "gest_attore_ins = :gestAttoreIns, gest_data_ins = :gestDataIns, gest_attore_upd = :gestAttoreUpd, gest_data_upd = :gestDataUpd, "
			+ "gest_uid = :gestUid, cod_esito_invio_acta = :codEsitoInvioActa, desc_esito_invio_acta = :descEsitoInvioActa, id_accertamento = :idAccertamento "
			+ "WHERE id_invio_acta = :idInvioActa ";

	private static final String QUERY_LOAD_DOCUMENTI_DA_ARCHIVIARE = "select * from risca_t_invio_acta rtia "
			+ "where id_spedizione_acta = :idSpedizioneActa and flg_archiviata_acta = 0 ";

	private static final String QUERY_LOAD_DOCUMENTI_WARN_ERROR = "select * from risca_t_invio_acta "
			+ " where id_spedizione_acta = :idSpedizioneActa " + " and cod_esito_acquisizione_acta <> '000' ";

	private static final String QUERY_DELETE_BY_ID = " delete from risca_t_invio_acta where id_invio_acta = :idInvioActa ";
	private static final String QUERY_DELETE_BY_NAP = " delete from risca_t_invio_acta where nap = :nap ";
	private static final String QUERY_DELETE_BY_ACCERTAMENTO = " delete from risca_t_invio_acta where id_accertamento = :idAccertamento ";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvioActaDTO loadInvioActaByNap(String nap) {
		LOGGER.debug("[InvioActaDAOImpl::loadInvioActaByNap] BEGIN");
		try {
			String query = QUERY_LOAD_INVIO_ACTA_BY_NAP;
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);

			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(getQuery(query, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[InvioActaDAOImpl::loadInvioActaByNap] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			// LOGGER.error("[InvioActaDAOImpl::loadInvioActaByNap] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[InvioActaDAOImpl::loadInvioActaByNap] END");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvioActaDTO loadInvioActaByIdAccertamento(Long idAccertamento) {
		LOGGER.debug("[InvioActaDAOImpl::loadInvioActaByIdAccertamento] BEGIN");
		try {
			String query = QUERY_LOAD_INVIO_ACTA_BY_ID_ACCERTAMENTO;
			Map<String, Object> map = new HashMap<>();
			map.put("idAccertamento", idAccertamento);

			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(getQuery(query, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[InvioActaDAOImpl::loadInvioActaByIdAccertamento] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[InvioActaDAOImpl::loadInvioActaByIdAccertamento] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[InvioActaDAOImpl::loadInvioActaByIdAccertamento] END");
		}
	}

	@Override
	public InvioActaDTO saveInvioActa(InvioActaDTO dto) {
		LOGGER.debug("[InvioActaDAOImpl::saveInvioActa] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_t_invio_acta");

			map.put("idInvioActa", genId);
			map.put("nap", dto.getNap());
			map.put("idSpedizioneActa", dto.getIdSpedizioneActa());
			map.put("nomeFile", dto.getNomeFile());
			map.put("flgMulticlassificazione", dto.getFlgMulticlassificazione());
			map.put("flgArchiviataActa", dto.getFlgArchiviataActa());
			map.put("dataInvio", dto.getDataInvio());
			map.put("uuidMessage", dto.getUuidMessage());
			map.put("dataEsitoActa", dto.getDataEsitoActa());
			map.put("codEsitoInvioActa", dto.getCodEsitoInvioActa());
			map.put("descEsitoInvioActa", dto.getDescEsitoInvioActa());
			map.put("codEsitoAcquisizioneActa", dto.getCodEsitoAcquisizioneActa());
			map.put("descEsitoAcquisizioneActa", dto.getDescEsitoAcquisizioneActa());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			map.put("idAccertamento", dto.getIdAccertamento());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_SAVE_INVIO_ACTA, null, null), params, keyHolder);
			dto.setIdInvioActa(genId);

		} catch (Exception e) {
			LOGGER.error("[InvioActaDAOImpl::saveInvioActa] ERROR : " + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[InvioActaDAOImpl::saveInvioActa] END");
		}

		return dto;
	}

	@Override
	public InvioActaDTO updateInvioActa(InvioActaDTO dto) {
		LOGGER.debug("[InvioActaDAOImpl::updateInvioActa] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();

			map.put("nap", dto.getNap());
			map.put("idSpedizioneActa", dto.getIdSpedizioneActa());
			map.put("nomeFile", dto.getNomeFile());
			map.put("flgMulticlassificazione", dto.getFlgMulticlassificazione());
			map.put("flgArchiviataActa", dto.getFlgArchiviataActa());
			map.put("dataInvio", dto.getDataInvio());
			map.put("uuidMessage", dto.getUuidMessage());
			map.put("dataEsitoActa", dto.getDataEsitoActa());
			map.put("codEsitoInvioActa", dto.getCodEsitoInvioActa());
			map.put("descEsitoInvioActa", dto.getDescEsitoInvioActa());
			map.put("codEsitoAcquisizioneActa", dto.getCodEsitoAcquisizioneActa());
			map.put("descEsitoAcquisizioneActa", dto.getDescEsitoAcquisizioneActa());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", dto.getGestDataIns());
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			map.put("idAccertamento", dto.getIdAccertamento());
			map.put("idInvioActa", dto.getIdInvioActa());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_UPDATE_INVIO_ACTA, null, null), params, keyHolder);
			dto.setGestDataUpd(now);
		} catch (Exception e) {
			LOGGER.error("[InvioActaDAOImpl::updateInvioActa] ERROR : " + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} finally {
			LOGGER.debug("[InvioActaDAOImpl::updateInvioActa] END");
		}

		return dto;
	}

	@Override
	public List<InvioActaDTO> loadDocumentiDaArchiviareBySpedizioneActa(Long idSpedizioneActa) {
		LOGGER.debug("[InvioActaDAOImpl::loadDocumentiDaArchiviareBySpedizioneActa] BEGIN");
		try {
			String query = QUERY_LOAD_DOCUMENTI_DA_ARCHIVIARE;
			Map<String, Object> map = new HashMap<>();
			map.put("idSpedizioneActa", idSpedizioneActa);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(query, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error(
					"[InvioActaDAOImpl::loadDocumentiDaArchiviareBySpedizioneActa] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[InvioActaDAOImpl::loadDocumentiDaArchiviareBySpedizioneActa] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[InvioActaDAOImpl::loadDocumentiDaArchiviareBySpedizioneActa] END");
		}
	}

	@Override
	public List<InvioActaDTO> loadDocumentiWarningError(Long idSpedizioneActa) {
		LOGGER.debug("[InvioActaDAOImpl::loadDocumentiWarningError] BEGIN");
		try {
			String query = QUERY_LOAD_DOCUMENTI_WARN_ERROR;
			Map<String, Object> map = new HashMap<>();
			map.put("idSpedizioneActa", idSpedizioneActa);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(query, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[InvioActaDAOImpl::loadDocumentiWarningError] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[InvioActaDAOImpl::loadDocumentiWarningError] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[InvioActaDAOImpl::loadDocumentiWarningError] END");
		}
	}
	
	@Override
	public Integer deleteInvioActaById(Long idInvioActa) throws DAOException {
		LOGGER.debug("[InvioActaDAOImpl::deleteInvioActaById] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idInvioActa", idInvioActa);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_BY_ID, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[InvioActaDAOImpl::deleteInvioActaById] ERROR : " +e);
			throw new DAOException(e.getMessage());
		} finally {
			LOGGER.debug("[InvioActaDAOImpl::deleteInvioActaById] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteInvioActaByNap(String nap) throws DAOException {
		LOGGER.debug("[InvioActaDAOImpl::deleteInvioActaByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_BY_NAP, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[InvioActaDAOImpl::deleteInvioActaByNap] ERROR : " +e);
			throw new DAOException(e.getMessage());
		} finally {
			LOGGER.debug("[InvioActaDAOImpl::deleteInvioActaByNap] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteInvioActaByAccertamento(Long idAccertamento) throws DAOException {
		LOGGER.debug("[InvioActaDAOImpl::deleteInvioActaByAccertamento] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAccertamento", idAccertamento);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_BY_ACCERTAMENTO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[InvioActaDAOImpl::deleteInvioActaByAccertamento] ERROR : " +e);
			throw new DAOException(e.getMessage());
		} finally {
			LOGGER.debug("[InvioActaDAOImpl::deleteInvioActaByAccertamento] END");
		}

		return res;
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
	public RowMapper<InvioActaDTO> getRowMapper() throws SQLException {
		return new InvioActaRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<InvioActaDTO> getExtendedRowMapper() throws SQLException {
		return new InvioActaRowMapper();
	}

	/**
	 * The type InvioActa row mapper.
	 */
	public static class InvioActaRowMapper implements RowMapper<InvioActaDTO> {

		/**
		 * Instantiates a new Elabora row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public InvioActaRowMapper() throws SQLException {
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
		public InvioActaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			InvioActaDTO bean = new InvioActaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, InvioActaDTO bean) throws SQLException {

			bean.setIdInvioActa(rs.getLong("id_invio_acta"));
			bean.setNap(rs.getString("nap"));
			bean.setIdSpedizioneActa(rs.getLong("id_spedizione_acta"));
			bean.setNomeFile(rs.getString("nome_file"));
			bean.setFlgMulticlassificazione(rs.getInt("flg_multiclassificazione"));
			bean.setFlgArchiviataActa(rs.getInt("flg_archiviata_acta"));
			bean.setDataInvio(rs.getDate("data_invio"));
			bean.setUuidMessage(rs.getString("uuid_message"));
			bean.setDataEsitoActa(rs.getDate("data_esito_acta"));
			bean.setCodEsitoInvioActa(rs.getString("cod_esito_invio_acta"));
			bean.setDescEsitoInvioActa(rs.getString("desc_esito_invio_acta"));
			bean.setCodEsitoAcquisizioneActa(rs.getString("cod_esito_acquisizione_acta"));
			bean.setDescEsitoAcquisizioneActa(rs.getString("desc_esito_acquisizione_acta"));
			bean.setIdAccertamento(getResultSetLong(rs, "id_accertamento"));
			
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));

		}

		private Long getResultSetLong(ResultSet resultset, String colName) throws SQLException {
			long v = resultset.getLong(colName);
			if (resultset.wasNull()) {
				return null;
			}
			return Long.valueOf(v);
		}

	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}