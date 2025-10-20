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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiPagopaDAO;
import it.csi.risca.riscabesrv.dto.SollDatiPagopaDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type SollDatiAmministr dao.
 *
 * @author CSI PIEMONTE
 */
public class SollDatiPagopaDAOImpl extends RiscaBeSrvGenericDAO<SollDatiPagopaDTO> implements SollDatiPagopaDAO {

	public static final String QUERY_INSERT_W = " INSERT INTO risca_w_soll_dati_pagopa"
			+ "(id_soll_dati_pagopa, id_accertamento, codice_utente, codice_utenza, iuv, codice_avviso, "
			+ "scadenza_pagamento, importo_da_versare, nap, nome_titolare_ind_post, presso_ind_post, "
			+ "indirizzo_ind_post, cap_ind_post, comune_ind_post, prov_ind_post, pec_email) "
			+ "VALUES(:idSollDatiPagopa, :idAccertamento, :codiceUtente, :codiceUtenza, :iuv, :codiceAvviso, "
			+ ":scadenzaPagamento, :importoDaVersare, :nap, :nomeTitolareIndPost, :pressoIndPost, "
			+ ":indirizzoIndPost, :capIndPost, :comuneIndPost, :provIndPost, :pecEmail) ";

	public static final String QUERY_DELETE_W_BY_ELABORA = " delete from risca_w_soll_dati_pagopa "
			+ " where id_accertamento in "
			+ " (SELECT id_accertamento FROM RISCA_W_ACCERTAMENTO  WHERE id_elabora = :idElabora)";
	
	public static final String QUERY_DELETE_BY_ID_ACCERTAMENTO = "DELETE FROM RISCA_R_SOLL_DATI_PAGOPA WHERE id_accertamento = :idAccertamento";

	public static final String QUERY_COPY_FROM_WORKING = " INSERT INTO RISCA_R_SOLL_DATI_PAGOPA ( "
			+ "id_soll_dati_pagopa, id_accertamento, codice_utente, codice_utenza, "
			+ "iuv, codice_avviso, scadenza_pagamento, importo_da_versare, nap, "
			+ "nome_titolare_ind_post, presso_ind_post, indirizzo_ind_post, "
			+ "cap_ind_post, comune_ind_post, prov_ind_post, pec_email, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd , gest_attore_upd , gest_uid) "
			+ "SELECT id_soll_dati_pagopa, id_accertamento, codice_utente, codice_utenza, "
			+ "iuv, codice_avviso, scadenza_pagamento, importo_da_versare, nap, "
			+ "nome_titolare_ind_post, presso_ind_post, indirizzo_ind_post, "
			+ "cap_ind_post, comune_ind_post, prov_ind_post, pec_email, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid "
			+ "FROM RISCA_W_SOLL_DATI_PAGOPA a WHERE a.id_accertamento in ( "
			+ "SELECT b.id_accertamento FROM RISCA_W_ACCERTAMENTO b WHERE b.id_elabora = :idElabora)";
	
	public static final String QUERY_LOAD_WORKING_BY_IUV = " select * from risca_w_soll_dati_pagopa where iuv = :iuv ";

	public static final String QUERY_UPDATE_IMPORTO_WORKING_BY_IUV = "update risca_w_soll_dati_pagopa "
			+ " set importo_da_versare = :importoDaVersare "
			+ " where iuv = :iuv ";
	
	@Override
	public SollDatiPagopaDTO saveSollDatiPagopaWorking(SollDatiPagopaDTO dto) throws DAOException {
		LOGGER.debug("[SollDatiPagopaDAOImpl::saveSollDatiPagopaWorking] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Long genId = findNextSequenceValue("seq_risca_r_soll_dati_pagopa");

			map.put("idSollDatiPagopa", genId);
			map.put("idAccertamento", dto.getIdAccertamento());
			map.put("codiceUtente", dto.getCodiceUtente());
			map.put("codiceUtenza", dto.getCodiceUtenza());
			map.put("iuv", dto.getIuv());
			map.put("codiceAvviso", dto.getCodiceAvviso());
			map.put("scadenzaPagamento", dto.getScadenzaPagamento());
			map.put("importoDaVersare", dto.getImportoDaVersare());
			map.put("nap", dto.getNap());
			map.put("nomeTitolareIndPost", dto.getNomeTitolareIndPost());
			map.put("pressoIndPost", dto.getPressoIndPost());
			map.put("indirizzoIndPost", dto.getIndirizzoIndPost());
			map.put("capIndPost", dto.getCapIndPost());
			map.put("comuneIndPost", dto.getComuneIndPost());
			map.put("provIndPost", dto.getProvIndPost());
			map.put("pecEmail", dto.getPecEmail());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_W, null, null), params, keyHolder);
			dto.setIdSollDatiPagopa(genId);

			return dto;
		} catch (Exception e) {
			LOGGER.error("[SollDatiPagopaDAOImpl::saveSollDatiPagopaWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiPagopaDAOImpl::saveSollDatiPagopaWorking] END");
		}
	}

	@Override
	public Integer deleteSollDatiPagopaWorkingByElabora(Long idElabora) throws DAOException {
		LOGGER.debug("[SollDatiPagopaDAOImpl::deleteSollDatiPagopaWorkingByElabora] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_W_BY_ELABORA, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDatiPagopaDAOImpl::deleteSollDatiPagopaWorkingByElabora] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiPagopaDAOImpl::deleteSollDatiPagopaWorkingByElabora] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteSollDatiPagopaByIdAccertamento(Long idAccertamento) throws DAOException {
		LOGGER.debug("[SollDatiPagopaDAOImpl::deleteSollDatiPagopaByIdAccertamento] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAccertamento", idAccertamento);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_BY_ID_ACCERTAMENTO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDatiPagopaDAOImpl::deleteSollDatiPagopaByIdAccertamento] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiPagopaDAOImpl::deleteSollDatiPagopaByIdAccertamento] END");
		}

		return res;
	}

	@Override
	public Integer copySollDatiPagopaFromWorking(Long idElabora, String attore) throws DAOException {
		LOGGER.debug("[SollDatiPagopaDAOImpl::copySollDatiPagopaFromWorking] BEGIN");
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
			LOGGER.error("[SollDatiPagopaDAOImpl::copySollDatiPagopaFromWorking] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiPagopaDAOImpl::copySollDatiPagopaFromWorking] END");
		}
	}
	
	@Override
	public SollDatiPagopaDTO loadSollDatiPagopaWorkingByIuv(String iuv) {
		LOGGER.debug("[SollDatiPagopaDAOImpl::loadSollDatiPagopaWorkingByIuv] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("iuv", iuv);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(getQuery(QUERY_LOAD_WORKING_BY_IUV, null, null), params, getRowMapper());

		} catch (DataAccessException e) {
			LOGGER.error("[SollDatiPagopaDAOImpl::loadSollDatiPagopaWorkingByIuv] " + e.getMessage());
		} catch (SQLException e) {
			LOGGER.error("[SollDatiPagopaDAOImpl::loadSollDatiPagopaWorkingByIuv] Errore nell'esecuzione della query",
					e);
		} finally {
			LOGGER.debug("[SollDatiPagopaDAOImpl::loadSollDatiPagopaWorkingByIuv] END");
		}
		return null;
	}

	@Override
	public Integer updateSollDatiPagopaWorkingImportoByIuv(String iuv, BigDecimal importoDaVersare)
			throws DAOException {
		LOGGER.debug("[SollDatiPagopaDAOImpl::updateSollDatiPagopaWorkingImportoByIuv] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("importoDaVersare", importoDaVersare);
			map.put("iuv", iuv);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_UPDATE_IMPORTO_WORKING_BY_IUV, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDatiPagopaDAOImpl::updateSollDatiPagopaWorkingImportoByIuv] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiPagopaDAOImpl::updateSollDatiPagopaWorkingImportoByIuv] END");
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
	public RowMapper<SollDatiPagopaDTO> getRowMapper() throws SQLException {
		return new SollDatiPagopaRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SollDatiPagopaDTO> getExtendedRowMapper() throws SQLException {
		return new SollDatiPagopaRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class SollDatiPagopaRowMapper implements RowMapper<SollDatiPagopaDTO> {

		/**
		 * Instantiates a new AvvisoDatiTitolare row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SollDatiPagopaRowMapper() throws SQLException {
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
		public SollDatiPagopaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SollDatiPagopaDTO bean = new SollDatiPagopaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SollDatiPagopaDTO bean) throws SQLException {
			bean.setIdSollDatiPagopa(getLong(rs, "id_soll_dati_pagopa"));
			bean.setIdAccertamento(getLong(rs, "id_accertamento"));
			bean.setCodiceUtente(rs.getString("codice_utente"));
			bean.setCodiceUtenza(rs.getString("codice_utenza"));
			bean.setIuv(rs.getString("iuv"));
			bean.setCodiceAvviso(rs.getString("codice_avviso"));
			bean.setScadenzaPagamento(rs.getString("scadenza_pagamento"));
			bean.setImportoDaVersare(rs.getBigDecimal("importo_da_versare"));
			bean.setNap(rs.getString("nap"));
			bean.setNomeTitolareIndPost(rs.getString("nome_titolare_ind_post"));
			bean.setPressoIndPost(rs.getString("presso_ind_post"));
			bean.setIndirizzoIndPost(rs.getString("indirizzo_ind_post"));
			bean.setCapIndPost(rs.getString("cap_ind_post"));
			bean.setComuneIndPost(rs.getString("comune_ind_post"));
			bean.setProvIndPost(rs.getString("prov_ind_post"));
			bean.setPecEmail(rs.getString("pec_email"));
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