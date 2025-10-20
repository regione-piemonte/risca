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

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaListaCaricoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.PagopaListaCaricoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type PagopaListaCarico dao.
 *
 * @author CSI PIEMONTE
 */
public class PagopaListaCaricoDAOImpl extends RiscaBeSrvGenericDAO<PagopaListaCaricoDTO>
		implements PagopaListaCaricoDAO {

	private static final String QUERY_INSERT = "INSERT INTO risca_r_pagopa_lista_carico "
			+ "(id_pagopa_lista_carico, id_lotto, nap, ind_tipo_aggiornamento, motivazione, "
			+ "importo_new, cod_esito_da_pagopa, desc_esito_da_pagopa, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(:idPagopaListaCarico, :idLotto, :nap, :indTipoAggiornamento, :motivazione, "
			+ ":importoNew, :codEsitoDaPagopa, :descEsitoDaPagopa, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";

	private static final String UPDATE_ESITO_BY_NAP_LOTTO = "update risca_r_pagopa_lista_carico "
			+ "set cod_esito_da_pagopa = :codEsito, desc_esito_da_pagopa = :descEsito, "
			+ "gest_attore_upd = :gestAttoreUpd, gest_data_upd = :gestDataUpd " 
			+ "where nap = :nap "
			+ "and id_lotto = :idLotto";

	private static final String QUERY_SELECT_BY_LOTTO_E_NAP = "select * from risca_r_pagopa_lista_carico "
			+ " where nap = :nap "
			+ " and id_lotto = :idLotto";

	@Override
	public PagopaListaCaricoDTO savePagopaListaCarico(PagopaListaCaricoDTO dto) {
		LOGGER.debug("[PagopaListaCaricoDAOImpl::savePagopaListaCarico] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_r_pagopa_lista_carico");

			map.put("idPagopaListaCarico", genId);
			map.put("idLotto", dto.getIdLotto());
			map.put("nap", dto.getNap());
			map.put("indTipoAggiornamento", dto.getIndTipoAggiornamento());
			map.put("motivazione", dto.getMotivazione());
			map.put("importoNew", dto.getImportoNew());
			map.put("codEsitoDaPagopa", dto.getCodEsitoDaPagopa());
			map.put("descEsitoDaPagopa", dto.getDescEsitoDaPagopa());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
			dto.setIdPagopaListaCarico(genId);
		} catch (Exception e) {
			LOGGER.error("[PagopaListaCaricoDAOImpl::savePagopaListaCarico] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[PagopaListaCaricoDAOImpl::savePagopaListaCarico] END");
		}

		return dto;
	}

	@Override
	public Integer updateEsitoListaCaricoByNap(String codEsito, String descEsito, String nap, Long idLotto,
			String attore) throws DAOException {
		LOGGER.debug("[PagopaListaCaricoDAOImpl::updateEsitoListaCaricoByNap] BEGIN");
		Integer res = null;
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("codEsito", codEsito);
			map.put("descEsito", descEsito);
			map.put("nap", nap);
			map.put("idLotto", idLotto);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(UPDATE_ESITO_BY_NAP_LOTTO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[PagopaListaCaricoDAOImpl::updateEsitoListaCaricoByNap] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[PagopaListaCaricoDAOImpl::updateEsitoListaCaricoByNap] END");
		}

		return res;
	}

	@Override
	public PagopaListaCaricoDTO loadPagopaListaCaricoByLottoNap(Long idLotto, String nap) {
		LOGGER.debug("[PagopaListaCaricoDAOImpl::loadPagopaListaCaricoByLottoNap] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			map.put("idLotto", idLotto);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(getQuery(QUERY_SELECT_BY_LOTTO_E_NAP, null, null), params, getRowMapper());

		} catch (SQLException e) {
			LOGGER.error("[PagopaListaCaricoDAOImpl::loadPagopaListaCaricoByLottoNap] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[PagopaListaCaricoDAOImpl::loadPagopaListaCaricoByLottoNap] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[PagopaListaCaricoDAOImpl::loadPagopaListaCaricoByLottoNap] END");
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
	public RowMapper<PagopaListaCaricoDTO> getRowMapper() throws SQLException {
		return new PagopaListaCaricoRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<PagopaListaCaricoDTO> getExtendedRowMapper() throws SQLException {
		return new PagopaListaCaricoRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class PagopaListaCaricoRowMapper implements RowMapper<PagopaListaCaricoDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public PagopaListaCaricoRowMapper() throws SQLException {
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
		public PagopaListaCaricoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			PagopaListaCaricoDTO bean = new PagopaListaCaricoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, PagopaListaCaricoDTO bean) throws SQLException {
			bean.setIdPagopaListaCarico(rs.getLong("id_pagopa_lista_carico"));
			bean.setIdLotto(rs.getLong("id_lotto"));
			bean.setNap(rs.getString("nap"));
			bean.setIndTipoAggiornamento(rs.getString("ind_tipo_aggiornamento"));
			bean.setMotivazione(rs.getString("motivazione"));
			bean.setImportoNew(rs.getBigDecimal("importo_new"));
			bean.setCodEsitoDaPagopa(rs.getString("cod_esito_da_pagopa"));
			bean.setDescEsitoDaPagopa(rs.getString("desc_esito_da_pagopa"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}