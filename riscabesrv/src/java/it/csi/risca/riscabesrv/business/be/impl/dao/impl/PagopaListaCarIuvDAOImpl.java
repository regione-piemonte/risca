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
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaListaCarIuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.PagopaListaCarIuvDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type PagopaListaCarIuv dao.
 *
 * @author CSI PIEMONTE
 */
public class PagopaListaCarIuvDAOImpl extends RiscaBeSrvGenericDAO<PagopaListaCarIuvDTO>
		implements PagopaListaCarIuvDAO {

	private static final String QUERY_INSERT = "INSERT INTO risca_r_pagopa_lista_car_iuv "
			+ "(id_pagopa_lista_car_iuv, id_lotto, nap, anno, importo, "
			+ "data_scadenza, data_inizio_validita, data_fine_validita, causale, tipo_soggetto, cod_fiscale, "
			+ "ragione_sociale, cognome, nome, note, cod_esito_da_pagopa, desc_esito_da_pagopa, codice_avviso, iuv, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(:idPagopaListaCarIuv, :idLotto, :nap, :anno, :importo, "
			+ ":dataScadenza, :dataInizioValidita, :dataFineValidita, :causale, :tipoSoggetto, :codFiscale, "
			+ ":ragioneSociale, :cognome, :nome, :note, :codEsitoDaPagopa, :descEsitoDaPagopa, :codiceAvviso, :iuv, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";

	private static final String UPDATE_ESITO_BY_NAP = "update risca_r_pagopa_lista_car_iuv "
			+ "set cod_esito_da_pagopa = :codEsito, "
			+ "desc_esito_da_pagopa = :descEsito, "
			+ "codice_avviso = :codiceAvviso, "
			+ "iuv = :iuv "
			+ "where nap = :nap";
	private static final String QUERY_SELECT_BY_NAP = "select * from risca_r_pagopa_lista_car_iuv "
			+ " where nap = :nap";
	
	@Override
	public PagopaListaCarIuvDTO savePagopaListaCarIuv(PagopaListaCarIuvDTO dto) {
		LOGGER.debug("[PagopaListaCarIuvDAOImpl::savePagopaListaCarIuv] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_r_pagopa_lista_car_iuv");

			map.put("idPagopaListaCarIuv", genId);
			map.put("idLotto", dto.getIdLotto());
			map.put("nap", dto.getNap());
			map.put("anno", dto.getAnno());
			map.put("importo", dto.getImporto());
			map.put("dataScadenza", dto.getDataScadenza());
			map.put("dataInizioValidita", dto.getDataInizioValidita());
			map.put("dataFineValidita", dto.getDataFineValidita());
			map.put("causale", dto.getCausale());
			map.put("tipoSoggetto", dto.getTipoSoggetto());
			map.put("codFiscale", dto.getCodFiscale());
			map.put("ragioneSociale", dto.getRagioneSociale());
			map.put("cognome", dto.getCognome());
			map.put("nome", dto.getNome());
			map.put("note", dto.getNote());
			map.put("codEsitoDaPagopa", dto.getCodEsitoDaPagopa());
			map.put("descEsitoDaPagopa", dto.getDescEsitoDaPagopa());
			map.put("codiceAvviso", dto.getCodiceAvviso());
			map.put("iuv", dto.getIuv());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", dto.getGestUid());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
			dto.setIdPagopaListaCarIuv(genId);
		} catch (Exception e) {
			LOGGER.error("[PagopaListaCarIuvDAOImpl::savePagopaListaCarIuv] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[PagopaListaCarIuvDAOImpl::savePagopaListaCarIuv] END");
		}

		return dto;
	}
	
	@Override
	public Integer updateEsitoListaCarIuvByNap(String codEsito, String descEsito, String codiceAvviso, String iuv, String nap) throws DAOException {
		LOGGER.debug("[PagopaListaCarIuvDAOImpl::updateEsitoListaCarIuvByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codEsito", codEsito);
			map.put("descEsito", descEsito);
			map.put("codiceAvviso", codiceAvviso);
			map.put("iuv", iuv);
			map.put("nap", nap);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(UPDATE_ESITO_BY_NAP, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[PagopaListaCarIuvDAOImpl::updateEsitoListaCarIuvByNap] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[PagopaListaCarIuvDAOImpl::updateEsitoListaCarIuvByNap] END");
		}

		return res;
	}
	
	@Override
	public List<PagopaListaCarIuvDTO> loadPagopaListaCarIuvByNap(String nap) {
		LOGGER.debug("[PagopaListaCarIuvDAOImpl::loadPagopaListaCarIuvByNap] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			MapSqlParameterSource params = getParameterValue(map);
			
			return template.query(getQuery(QUERY_SELECT_BY_NAP, null, null), params, getRowMapper());
			
		} catch (SQLException e) {
			LOGGER.error("[PagopaListaCarIuvDAOImpl::loadPagopaListaCarIuvByNap] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[PagopaListaCarIuvDAOImpl::loadPagopaListaCarIuvByNap] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[PagopaListaCarIuvDAOImpl::loadPagopaListaCarIuvByNap] END");
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
	public RowMapper<PagopaListaCarIuvDTO> getRowMapper() throws SQLException {
		return new PagopaListaCarIuvRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<PagopaListaCarIuvDTO> getExtendedRowMapper() throws SQLException {
		return new PagopaListaCarIuvRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class PagopaListaCarIuvRowMapper implements RowMapper<PagopaListaCarIuvDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public PagopaListaCarIuvRowMapper() throws SQLException {
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
		public PagopaListaCarIuvDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			PagopaListaCarIuvDTO bean = new PagopaListaCarIuvDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, PagopaListaCarIuvDTO bean) throws SQLException {

			bean.setIdPagopaListaCarIuv(rs.getLong("id_pagopa_lista_car_iuv"));
			bean.setIdLotto(rs.getLong("id_lotto"));
			bean.setNap(rs.getString("nap"));
			bean.setAnno(rs.getString("anno"));
			bean.setImporto(rs.getBigDecimal("importo"));
			bean.setDataScadenza(rs.getDate("data_scadenza"));
			bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
			bean.setDataFineValidita(rs.getDate("data_fine_validita"));
			bean.setCausale(rs.getString("causale"));
			bean.setTipoSoggetto(rs.getString("tipo_soggetto"));
			bean.setCodFiscale(rs.getString("cod_fiscale"));
			bean.setRagioneSociale(rs.getString("ragione_sociale"));
			bean.setCognome(rs.getString("cognome"));
			bean.setNome(rs.getString("nome"));
			bean.setNote(rs.getString("note"));
			bean.setCodEsitoDaPagopa(rs.getString("cod_esito_da_pagopa"));
			bean.setDescEsitoDaPagopa(rs.getString("desc_esito_da_pagopa"));
			bean.setCodiceAvviso(rs.getString("codice_avviso"));
			bean.setIuv(rs.getString("iuv"));
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