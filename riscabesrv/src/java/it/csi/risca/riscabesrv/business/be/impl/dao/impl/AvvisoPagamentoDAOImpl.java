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
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoPagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AvvisoPagamentoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type AvvisoPagamento dao.
 *
 * @author CSI PIEMONTE
 */
public class AvvisoPagamentoDAOImpl extends RiscaBeSrvGenericDAO<AvvisoPagamentoDTO> implements AvvisoPagamentoDAO {

	private static final String QUERY_SELECT_MAX_PROGRESSIVO = "SELECT COALESCE(MAX(prog_nap_avviso_pagamento), 0) as maxProg "
			+ " FROM risca_t_avviso_pagamento WHERE nap LIKE :partialNap";
	
	private static final String QUERY_SELECT_MAX_PROGRESSIVO_WORKING = "SELECT COALESCE(MAX(prog_nap_avviso_pagamento), 0) as maxProg "
			+ " FROM risca_w_avviso_pagamento WHERE nap LIKE :partialNap";

	public static final String QUERY_INSERT = "INSERT INTO risca_t_avviso_pagamento "
			+ "(nap, id_spedizione, prog_nap_avviso_pagamento, imp_totale_dovuto, "
			+ "gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ " VALUES(:nap, :idSpedizione, :progNapAvvisoPagamento, :impTotaleDovuto, "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid) ";

	public static final String QUERY_INSERT_W = "INSERT INTO risca_w_avviso_pagamento "
			+ "(nap, id_spedizione, prog_nap_avviso_pagamento, imp_totale_dovuto) "
			+ " VALUES(:nap, :idSpedizione, :progNapAvvisoPagamento, :impTotaleDovuto) ";

	public static final String QUERY_UPDATE_W_IMPTOTALE_DOVUTO = "update risca_w_avviso_pagamento "
			+ "set imp_totale_dovuto = COALESCE(imp_totale_dovuto, 0) + :canoneAnnuo " 
			+ "where nap = :nap "
			+ "and id_spedizione = :idSpedizione";

	public static final String QUERY_UPDATE_W_COMPENSAZIONE = " update risca_w_avviso_pagamento "
			+ " set imp_totale_dovuto = COALESCE(imp_totale_dovuto, 0) - :compensazione "
			+ " where nap = :nap ";
	
	private static final String QUERY_DELETE_AVVISO_PAG_SENZA_RISCOSSIONI = "delete from RISCA_W_AVVISO_PAGAMENTO b "
			+ " where NOT EXISTS (select 1 "
			+ "	from RISCA_W_AVVISO_DATI_AMMIN a "
			+ "	where b.nap = a.nap)";
	
	private static final String QUERY_SELECT_AVVISO_PAGAMENTO = "SELECT * FROM risca_t_avviso_pagamento where nap = :nap ";
	
	private static final String QUERY_SELECT_AVVISI_PAGAMENTO_BY_SPEDIZIONE = " select * from RISCA_W_AVVISO_PAGAMENTO "
			+ " where id_spedizione = :idSpedizione "
			+ " order by nap ";
	
	private static final String QUERY_INSERT_AVVISO_PAGAMENTO_FROM_WORKING_BY_NAP = " INSERT INTO risca_t_avviso_pagamento "
			+ "(nap, id_spedizione, prog_nap_avviso_pagamento, imp_totale_dovuto, gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ "select nap, :idSpedizione, prog_nap_avviso_pagamento, imp_totale_dovuto, :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid "
			+ "from RISCA_W_AVVISO_PAGAMENTO "
			+ "where nap = :nap ";
	
	private static final String QUERY_DELETE_AVVISO_PAGAMENTO_WORKING_BY_NAP = " delete from risca_w_avviso_pagamento where nap = :nap ";
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getMaxProgressivo(String partialNap) {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::getMaxProgressivo] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("partialNap", partialNap + "%");

			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(getQuery(QUERY_SELECT_MAX_PROGRESSIVO, null, null), params,
					new IntegerRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error("[AvvisoPagamentoDAOImpl::getMaxProgressivo] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AvvisoPagamentoDAOImpl::getMaxProgressivo] END");
		}
	}
	
	@Override
	public Integer getMaxProgressivoWorking(String partialNap) {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::getMaxProgressivoWorking] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("partialNap", partialNap + "%");

			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(getQuery(QUERY_SELECT_MAX_PROGRESSIVO_WORKING, null, null), params,
					new IntegerRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error("[AvvisoPagamentoDAOImpl::getMaxProgressivoWorking] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AvvisoPagamentoDAOImpl::getMaxProgressivoWorking] END");
		}
	}

	@Override
	public AvvisoPagamentoDTO saveAvvisoPagamento(AvvisoPagamentoDTO dto) throws DAOException {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::saveAvvisoPagamento] BEGIN");
		try {
			dto = saveAvvisoPagamento(dto, false);
		} catch (Exception e) {
			LOGGER.error("[AvvisoPagamentoDAOImpl::saveAvvisoPagamento] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoPagamentoDAOImpl::saveAvvisoPagamento] END");
		}

		return dto;
	}
	
	@Override
	public AvvisoPagamentoDTO saveAvvisoPagamentoWorking(AvvisoPagamentoDTO dto) throws DAOException {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::saveAvvisoPagamentoWorking] BEGIN");
		try {
			dto = saveAvvisoPagamento(dto, true);
		} catch (Exception e) {
			LOGGER.error("[AvvisoPagamentoDAOImpl::saveAvvisoPagamentoWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoPagamentoDAOImpl::saveAvvisoPagamentoWorking] END");
		}

		return dto;
	}

	private AvvisoPagamentoDTO saveAvvisoPagamento(AvvisoPagamentoDTO dto, boolean working) {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		String gestUID = null;
		map.put("nap", dto.getNap());
		map.put("idSpedizione", dto.getIdSpedizione());
		map.put("progNapAvvisoPagamento", dto.getProgNapAvvisoPagamento());
		map.put("impTotaleDovuto", dto.getImpTotaleDovuto());
		if (!working) {
			gestUID = generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now);
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", gestUID);
		}

		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT;
		if (working) {
			query = QUERY_INSERT_W;
		}
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setGestDataIns(now);
		dto.setGestDataUpd(now);
		if (!working) {
		  dto.setGestUid(gestUID);
		}
		return dto;
	}

	@Override
	public Integer updateWorkingAvvisoPagamentoImpTotaleDovuto(BigDecimal canoneAnnuo, String nap,
			Long idSpedizione) throws DAOException {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::updateWAvvisoAvvisoPagamentoImpTotaleDovuto] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("canoneAnnuo", canoneAnnuo);
			map.put("nap", nap);
			map.put("idSpedizione", idSpedizione);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_W_IMPTOTALE_DOVUTO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AvvisoPagamentoDAOImpl::updateWAvvisoAvvisoPagamentoImpTotaleDovuto] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoPagamentoDAOImpl::updateWAvvisoAvvisoPagamentoImpTotaleDovuto] END");
		}

		return res;
	}
	
	@Override
	public Integer updateWorkingAvvisoPagamentoCompensazione(BigDecimal compensazione, String nap) throws DAOException {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::updateWorkingAvvisoPagamentoCompensazioni] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("compensazione", compensazione);
			map.put("nap", nap);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_W_COMPENSAZIONE, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AvvisoPagamentoDAOImpl::updateWorkingAvvisoPagamentoCompensazioni] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoPagamentoDAOImpl::updateWorkingAvvisoPagamentoCompensazioni] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteWorkingAvvisoPagamentoSenzaRiscDaBollettare() throws DAOException {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::deleteWorkingAvvisoPagamentoSenzaRiscDaBollettare] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_AVVISO_PAG_SENZA_RISCOSSIONI, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[AvvisoPagamentoDAOImpl::deleteWorkingAvvisoPagamentoSenzaRiscDaBollettare] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoPagamentoDAOImpl::deleteWorkingAvvisoPagamentoSenzaRiscDaBollettare] END");
		}

		return res;
	}
	
	@Override
	public List<AvvisoPagamentoDTO> loadAvvisoPagamentoWorkingByIdSpedizione(Long idSpedizione) {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::loadAvvisoPagamentoByIdSpedizione] BEGIN");
		LOGGER.debug("[AvvisoPagamentoDAOImpl::loadAvvisoPagamentoByIdSpedizione] idSpedizione: "+idSpedizione);
        Map<String, Object> map = new HashMap<>();
        List<AvvisoPagamentoDTO> avvisiPagamento = null;
        try {
            map.put("idSpedizione", idSpedizione);
            MapSqlParameterSource params = getParameterValue(map);
            avvisiPagamento=  template.query(QUERY_SELECT_AVVISI_PAGAMENTO_BY_SPEDIZIONE, params, getRowMapper());
		} catch (Exception e) {
            LOGGER.error("[AvvisoPagamentoDAOImpl::loadAvvisoPagamentoByIdSpedizione] Errore generale ", e);
            LOGGER.debug("[AvvisoPagamentoDAOImpl::loadAvvisoPagamentoByIdSpedizione] END");
            return null;
		}
        LOGGER.debug("[AvvisoPagamentoDAOImpl::loadAvvisoPagamentoByIdSpedizione] END");
        return avvisiPagamento;
	}
	
	@Override
	public Integer copyAvvisoPagamentoFromWorkingByNap(String nap, Long idSpedizione, String attore) throws DAOException {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::copyAvvioPagamentoFromWorkingByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("nap", nap);
			map.put("idSpedizione", idSpedizione);
			map.put("gestAttoreIns", attore);
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_INSERT_AVVISO_PAGAMENTO_FROM_WORKING_BY_NAP, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AvvisoPagamentoDAOImpl::copyAvvioPagamentoFromWorkingByNap] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoPagamentoDAOImpl::copyAvvioPagamentoFromWorkingByNap] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteAvvisoPagamentoWorkingByNap(String nap) throws DAOException {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::deleteAvvisoPagamentoWorkingByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_AVVISO_PAGAMENTO_WORKING_BY_NAP, null, null), params,
					keyHolder);

		} catch (Exception e) {
			LOGGER.error("[AvvisoPagamentoDAOImpl::deleteAvvisoPagamentoWorkingByNap] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoPagamentoDAOImpl::deleteAvvisoPagamentoWorkingByNap] END");
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
	public RowMapper<AvvisoPagamentoDTO> getRowMapper() throws SQLException {
		return new AvvisoPagamentoRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<AvvisoPagamentoDTO> getExtendedRowMapper() throws SQLException {
		return new AvvisoPagamentoRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class AvvisoPagamentoRowMapper implements RowMapper<AvvisoPagamentoDTO> {

		/**
		 * Instantiates a new Elabora row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public AvvisoPagamentoRowMapper() throws SQLException {
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
		public AvvisoPagamentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AvvisoPagamentoDTO bean = new AvvisoPagamentoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AvvisoPagamentoDTO bean) throws SQLException {
			bean.setNap(rs.getString("nap"));
			bean.setIdSpedizione(rs.getLong("id_spedizione"));
			bean.setProgNapAvvisoPagamento(rs.getInt("prog_nap_avviso_pagamento"));
			bean.setImpTotaleDovuto(rs.getBigDecimal("imp_totale_dovuto"));
			
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
		}
		
		private boolean rsHasColumn(ResultSet rs, String column){
		    try{
		        rs.findColumn(column);
		        return true;
		    } catch (SQLException sqlex){
		        //Column not present in resultset
		    }
		    return false;
		}
	}

	public static class IntegerRowMapper implements RowMapper<Integer> {

		/**
		 * Instantiates a new String row mapper.
		 */
		public IntegerRowMapper() {
			// Instatiate class
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
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getInt("maxProg");
		}
	}

	@Override
	public AvvisoPagamentoDTO loadAvvisoPagamentoByIdNap(String nap) throws DAOException {
		LOGGER.debug("[AvvisoPagamentoDAOImpl::loadAvvisoPagamentoByIdNap] BEGIN");
		LOGGER.debug("[AvvisoPagamentoDAOImpl::loadAvvisoPagamentoByIdNap] nap: "+nap);
        Map<String, Object> map = new HashMap<>();
        AvvisoPagamentoDTO avvisoPagamento = null;
        try {
            map.put("nap", nap);
            MapSqlParameterSource params = getParameterValue(map);
            avvisoPagamento=  template.queryForObject(QUERY_SELECT_AVVISO_PAGAMENTO, params, getRowMapper());
		} catch (Exception e) {
            LOGGER.error("[AvvisoPagamentoDAOImpl::loadAvvisoPagamentoByIdNap] Errore generale ", e);
            LOGGER.debug("[AvvisoPagamentoDAOImpl::loadAvvisoPagamentoByIdNap] END");
            return null;
		}
        LOGGER.debug("[AvvisoPagamentoDAOImpl::loadAvvisoPagamentoByIdNap] END");
        return avvisoPagamento;
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}