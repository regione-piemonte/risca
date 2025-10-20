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
import it.csi.risca.riscabesrv.business.be.impl.dao.LottoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.LottoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Lotto dao.
 *
 * @author CSI PIEMONTE
 */
public class LottoDAOImpl extends RiscaBeSrvGenericDAO<LottoDTO> implements LottoDAO {

	private static final String QUERY_INSERT_LOTTO = "INSERT INTO risca_t_lotto "
			+ "(id_lotto, id_elabora, nome_lotto, data_lotto, flg_inviato, flg_ricevuto, flg_elaborato, "
			+ "cod_esito_da_pagopa, desc_esito_da_pagopa, cod_esito_acquisizione_pagopa, desc_esito_acquisizione_pagopa,"
			+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(:idLotto, :idElabora, :nomeLotto, :dataLotto, :flgInviato, :flgRicevuto, "
			+ ":flgElaborato, :codEsitoDaPagopa, :descEsitoDaPagopa, :codEsitoAcquisizionePagopa, :descEsitoAcquisizionePagopa,"
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid )";
	
	private static final String QUERY_UPD_FLG_INVIATO_BY_ID_LOTTO = "update risca_t_lotto "
			+ " set flg_inviato = :flgInviato, cod_esito_da_pagopa = :codEsito, desc_esito_da_pagopa = :descEsito, gest_data_upd = :gestDataUpd "
			+ " where id_lotto = :idLotto";
	
	private static final String QUERY_UPD_FLG_RICEVUTO_ELABORATO_BY_NOME_LOTTO = "update risca_t_lotto "
			+ "set flg_ricevuto = :flgRicevuto, flg_elaborato = :flgElaborato, "
			+ "cod_esito_acquisizione_pagopa = :codEsitoAcquisizionePagopa, "
			+ "desc_esito_acquisizione_pagopa = :descEsitoAcquisizionePagopa, "
			+ "gest_data_upd = :gestDataUpd "
			+ "where id_elabora = :idElabora "
			+ "and nome_lotto = :nomeLotto ";

	private static final String QUERY_LOAD_LOTTO_INVIATO_BY_NAME = "Select * "
			+ "from risca_t_lotto "
			+ "where id_elabora = :idElabora "
			+ "and nome_lotto = :nomeLotto "
			+ "and flg_inviato = 1 "
			+ "and flg_ricevuto = 0 "
			+ "and flg_elaborato = 0";
	
	private static final String QUERY_FIND_LOTTI_ATTESI = "select distinct * from risca_t_lotto "
			+ "where id_elabora = :idElabora and flg_ricevuto = 0 ";
	
	private static final String QUERY_FIND_LOTTI_RICEVUTI = "select distinct * from risca_t_lotto "
			+ "where id_elabora = :idElabora "
			+ "and flg_ricevuto = 1 "
			// + "and flg_elaborato = 1 "
			+ "and flg_inviato = 1 "
			+ "and id_lotto in (:listaLotti) ";
	
	private static final String QUERY_FIND_LOTTI_ANOMALI = "select distinct * from risca_t_lotto "
			+ "where id_elabora = :idElabora "
			+ "and flg_ricevuto = 1 "
			+ "and flg_elaborato = 0 "
			+ "and flg_inviato = 1 "
			+ "and id_lotto in (:listaLotti) ";
			
	@Override
	public LottoDTO saveLotto(LottoDTO dto) {
		LOGGER.debug("[LottoDAOImpl::saveLotto] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_t_lotto");

			map.put("idLotto", genId);
			map.put("idElabora", dto.getIdElabora());
			map.put("nomeLotto", dto.getNomeLotto());
			map.put("dataLotto", dto.getDataLotto());
			map.put("flgInviato", dto.getFlgInviato());
			map.put("flgRicevuto", dto.getFlgRicevuto());
			map.put("flgElaborato", dto.getFlgElaborato());
			map.put("codEsitoDaPagopa", dto.getCodEsitoDaPagopa());
			map.put("descEsitoDaPagopa", dto.getDescEsitoDaPagopa());
			map.put("codEsitoAcquisizionePagopa", dto.getCodEsitoAcquisizionePagopa());
			map.put("descEsitoAcquisizionePagopa", dto.getDescEsitoAcquisizionePagopa());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_LOTTO, null, null), params, keyHolder);
			dto.setIdLotto(genId);
		} catch (Exception e) {
			LOGGER.error("[LottoDAOImpl::saveLotto] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[LottoDAOImpl::saveLotto] END");
		}

		return dto;
	}
	
	@Override
	public Integer updateEsitoLottoByIdLotto(Long idLotto, Integer flgInviato, String codEsito, String descEsito) throws DAOException {
		LOGGER.debug("[LottoDAOImpl::updateEsitoLottoByIdLotto] BEGIN");
		Integer res = null;
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idLotto", idLotto);
			map.put("flgInviato", flgInviato);
			map.put("codEsito", codEsito);
			map.put("descEsito", descEsito);
			map.put("gestDataUpd", now);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPD_FLG_INVIATO_BY_ID_LOTTO, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[LottoDAOImpl::updateEsitoLottoByIdLotto] ERROR : " ,e);
					throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[LottoDAOImpl::updateEsitoLottoByIdLotto] END");
		}

		return res;
	}
	
	@Override
	public Integer updateFlgRicevutoElaboratoByNomeLotto(Long idElabora, String nomeLotto, Integer flgRicevuto,
			Integer flgElaborato, String codEsitoAcquisizione, String descEsitoAcquisizione) throws DAOException {
		LOGGER.debug("[LottoDAOImpl::updateFlgRicevutoElaboratoByNomeLotto] BEGIN");
		Integer res = null;
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("nomeLotto", nomeLotto);
			map.put("gestDataUpd", now);
			map.put("flgRicevuto", flgRicevuto);
			map.put("flgElaborato", flgElaborato);
			map.put("codEsitoAcquisizionePagopa", codEsitoAcquisizione);
			map.put("descEsitoAcquisizionePagopa", descEsitoAcquisizione);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPD_FLG_RICEVUTO_ELABORATO_BY_NOME_LOTTO, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[LottoDAOImpl::updateFlgRicevutoElaboratoByNomeLotto] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[LottoDAOImpl::updateFlgRicevutoElaboratoByNomeLotto] END");
		}

		return res;
	}
	
	@Override
	public List<LottoDTO> loadLottoInviatoByName(Long idElabora, String nomeLotto) {
		LOGGER.debug("[LottoDAOImpl::loadLottoInviatoByName] BEGIN");        
        try {
        	Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("nomeLotto", nomeLotto);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_LOTTO_INVIATO_BY_NAME, null, null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[ParametroElaboraDAOImpl::loadParametroElaboraByElaboraRaggruppamento] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[ParametroElaboraDAOImpl::loadParametroElaboraByElaboraRaggruppamento] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[LottoDAOImpl::loadLottoInviatoByName] END");
		}
	}
	
	@Override
	public List<LottoDTO> findLottiAttesi(Long idElabora) {
		LOGGER.debug("[LottoDAOImpl::findLottiAttesi] BEGIN");        
        try {
        	Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_FIND_LOTTI_ATTESI, null, null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[ParametroElaboraDAOImpl::findLottiAttesi] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[ParametroElaboraDAOImpl::findLottiAttesi] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[LottoDAOImpl::findLottiAttesi] END");
		}
	}
	
	@Override
	public List<LottoDTO> findLottiRicevuti(Long idElabora, List<Long> listaLotti) {
		LOGGER.debug("[LottoDAOImpl::findLottiRicevuti] BEGIN");        
        try {
        	Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("listaLotti", listaLotti);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_FIND_LOTTI_RICEVUTI, null, null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[ParametroElaboraDAOImpl::findLottiRicevuti] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[ParametroElaboraDAOImpl::findLottiRicevuti] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[LottoDAOImpl::findLottiRicevuti] END");
		}
	}
	
	@Override
	public List<LottoDTO> findLottiAnomali(Long idElabora, List<Long> listaLotti) {
		LOGGER.debug("[LottoDAOImpl::findLottiRicevuti] BEGIN");        
        try {
        	Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("listaLotti", listaLotti);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_FIND_LOTTI_ANOMALI, null, null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[ParametroElaboraDAOImpl::findLottiRicevuti] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[ParametroElaboraDAOImpl::findLottiRicevuti] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[LottoDAOImpl::findLottiRicevuti] END");
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
	public RowMapper<LottoDTO> getRowMapper() throws SQLException {
		return new LottoRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<LottoDTO> getExtendedRowMapper() throws SQLException {
		return new LottoRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class LottoRowMapper implements RowMapper<LottoDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public LottoRowMapper() throws SQLException {
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
		public LottoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			LottoDTO bean = new LottoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, LottoDTO bean) throws SQLException {

			bean.setIdLotto(rs.getLong("id_lotto"));
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setNomeLotto(rs.getString("nome_lotto"));
			bean.setDataLotto(rs.getDate("data_lotto"));
			bean.setFlgInviato(rs.getInt("flg_inviato"));
			bean.setFlgRicevuto(rs.getInt("flg_ricevuto"));
			bean.setFlgElaborato(rs.getInt("flg_elaborato"));
			bean.setCodEsitoDaPagopa(rs.getString("cod_esito_da_pagopa"));
			bean.setDescEsitoDaPagopa(rs.getString("desc_esito_da_pagopa"));
			bean.setCodEsitoAcquisizionePagopa(rs.getString("cod_esito_acquisizione_pagopa"));
			bean.setDescEsitoAcquisizionePagopa(rs.getString("desc_esito_acquisizione_pagopa"));
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