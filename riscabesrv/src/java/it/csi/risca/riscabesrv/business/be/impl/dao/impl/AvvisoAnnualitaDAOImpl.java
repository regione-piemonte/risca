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
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoAnnualitaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AvvisoAnnualitaDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type AvvisoDatiAmmin dao.
 *
 * @author CSI PIEMONTE
 */
public class AvvisoAnnualitaDAOImpl extends RiscaBeSrvGenericDAO<AvvisoAnnualitaDTO> implements AvvisoAnnualitaDAO {

	public static final String QUERY_INSERT = "INSERT INTO risca_r_avviso_annualita "
			+ "(nap, codice_utenza, anno_rich_pagamento, fraz_totale_canone_anno, "
			+ "totale_canone_anno_calc, etichetta20_calc, valore20_calc, vuoto, "
			+ "gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ "VALUES(:nap, :codiceUtenza, :annoRichPagamento, :frazTotaleCanoneAnno, "
			+ ":totaleCanoneAnnoCalc, :etichetta20Calc, :valore20Calc, :vuoto, "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid)";
	
	public static final String QUERY_INSERT_W = "INSERT INTO risca_w_avviso_annualita "
			+ "(nap, codice_utenza, anno_rich_pagamento, fraz_totale_canone_anno, "
			+ "totale_canone_anno_calc, etichetta20_calc, valore20_calc, vuoto) "
			+ "VALUES(:nap, :codiceUtenza, :annoRichPagamento, :frazTotaleCanoneAnno, "
			+ ":totaleCanoneAnnoCalc, :etichetta20Calc, :valore20Calc, :vuoto)";

	public static final String UPDATE_W_TOT_CANONE_ANNO_CALC = "update risca_w_avviso_annualita "
			+ "set totale_canone_anno_calc = :sumCanoneUsoPcalc, valore20_calc = :frazionato "
			+ "where nap = :nap "
			+ "and codice_utenza = :codRiscossione "
			+ "and anno_rich_pagamento = :annoRichPagamento";
	
	public static final String QUERY_LOAD_W_BY_NAP = "select * "
			+ "from risca_w_avviso_annualita "
			+ "where nap = :nap "
			+ "order by codice_utenza, anno_rich_pagamento";
	
	private static final String QUERY_INSERT_AVVISO_ANNUALITA_FROM_WORKING_BY_NAP = " INSERT INTO risca_r_avviso_annualita "
			+ " (nap, codice_utenza, anno_rich_pagamento, fraz_totale_canone_anno, totale_canone_anno_calc, etichetta20_calc, valore20_calc, vuoto,  "
			+ " gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ " select nap, codice_utenza, anno_rich_pagamento, fraz_totale_canone_anno, totale_canone_anno_calc, etichetta20_calc, valore20_calc, vuoto, "
			+ " :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, null "
			+ " from risca_w_avviso_annualita "
			+ " where nap = :nap ";
	
	private static final String QUERY_DELETE_AVVISO_ANNUALITA_WORKING_BY_NAP = " delete from RISCA_W_AVVISO_ANNUALITA where nap = :nap ";
	
	@Override
	public AvvisoAnnualitaDTO saveAvvisoAnnualita(AvvisoAnnualitaDTO dto) throws DAOException {
		LOGGER.debug("[AvvisoAnnualitaDAOImpl::saveAvvisoAnnualita] BEGIN");
		try {
			dto = saveAvvisoAnnualita(dto, false);
		} catch (Exception e) {
			LOGGER.error("[AvvisoAnnualitaDAOImpl::saveAvvisoAnnualita] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoAnnualitaDAOImpl::saveAvvisoAnnualita] END");
		}

		return dto;
	}
	
	@Override
	public AvvisoAnnualitaDTO saveAvvisoAnnualitaWorking(AvvisoAnnualitaDTO dto) throws DAOException {
		LOGGER.debug("[AvvisoAnnualitaDAOImpl::saveAvvisoAnnualitaWorking] BEGIN");
		try {
			dto = saveAvvisoAnnualita(dto, true);
		} catch (Exception e) {
			LOGGER.error("[AvvisoAnnualitaDAOImpl::saveAvvisoAnnualitaWorking] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoAnnualitaDAOImpl::saveAvvisoAnnualitaWorking] END");
		}

		return dto;
	}

	private AvvisoAnnualitaDTO saveAvvisoAnnualita(AvvisoAnnualitaDTO dto, boolean working) {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		
		map.put("nap", dto.getNap());
		map.put("codiceUtenza", dto.getCodiceUtenza());
		map.put("annoRichPagamento", dto.getAnnoRichPagamento());
		map.put("frazTotaleCanoneAnno", dto.getFrazTotaleCanoneAnno());
		map.put("totaleCanoneAnnoCalc", dto.getTotaleCanoneAnnoCalc());
		map.put("etichetta20Calc", dto.getEtichetta20Calc());
		map.put("valore20Calc", dto.getValore20Calc());
		map.put("vuoto", dto.getVuoto());

		if(!working) {
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
		}

		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT;
		if(working) {
			query = QUERY_INSERT_W;
		}
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setGestDataIns(now);
		dto.setGestDataUpd(now);
		return dto;
	}
	
	@Override
	public Integer updateWorkingAvvisoAnnualitaTotaleCanoneAnnoCalc(BigDecimal sumCanoneUsoPcalc,
			BigDecimal frazionato, String nap, String codRiscossione, int annoRichPagamento) throws DAOException {
		LOGGER.debug("[AvvisoAnnualitaDAOImpl::updateWAvvisoAnnualitaTotaleCanoneAnnoCalc] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("sumCanoneUsoPcalc", sumCanoneUsoPcalc);
			map.put("frazionato", frazionato);
			map.put("nap", nap);
			map.put("codRiscossione", codRiscossione);
			map.put("annoRichPagamento", annoRichPagamento);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(UPDATE_W_TOT_CANONE_ANNO_CALC, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[AvvisoAnnualitaDAOImpl::updateWAvvisoAnnualitaTotaleCanoneAnnoCalc] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoAnnualitaDAOImpl::updateWAvvisoAnnualitaTotaleCanoneAnnoCalc] END");
		}

		return res;
	}
	
	@Override
	public List<AvvisoAnnualitaDTO> loadAvvisoAnnualitaWorkingByNap(String nap) {
		LOGGER.debug("[AvvisoAnnualitaDAOImpl::loadAvvisoAnnualitaWorkingByNap] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_W_BY_NAP, null, null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[AvvisoAnnualitaDAOImpl::loadAvvisoAnnualitaWorkingByNap] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[AvvisoAnnualitaDAOImpl::loadAvvisoAnnualitaWorkingByNap] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AvvisoAnnualitaDAOImpl::loadAvvisoAnnualitaWorkingByNap] END");
		}
	}
	
	@Override
	public Integer copyAvvisoAnnualitaFromWorkingByNap(String nap, String attore) throws DAOException {
		LOGGER.debug("[AvvisoAnnualitaDAOImpl::copyAvvisoAnnualitaFromWorkingByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("nap", nap);
			map.put("gestAttoreIns", attore);
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_INSERT_AVVISO_ANNUALITA_FROM_WORKING_BY_NAP, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AvvisoAnnualitaDAOImpl::copyAvvisoAnnualitaFromWorkingByNap] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoAnnualitaDAOImpl::copyAvvisoAnnualitaFromWorkingByNap] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteAvvisoAnnualitaWorkingByNap(String nap) throws DAOException {
		LOGGER.debug("[AvvisoAnnualitaDAOImpl::deleteAvvisoAnnualitaWorkingByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_AVVISO_ANNUALITA_WORKING_BY_NAP, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AvvisoAnnualitaDAOImpl::deleteAvvisoAnnualitaWorkingByNap] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoAnnualitaDAOImpl::deleteAvvisoAnnualitaWorkingByNap] END");
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
	public RowMapper<AvvisoAnnualitaDTO> getRowMapper() throws SQLException {
		return new AvvisoAnnualitaRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<AvvisoAnnualitaDTO> getExtendedRowMapper() throws SQLException {
		return new AvvisoAnnualitaRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class AvvisoAnnualitaRowMapper implements RowMapper<AvvisoAnnualitaDTO> {
		
		/**
		 * Instantiates a new AvvisoDatiAmmin row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public AvvisoAnnualitaRowMapper() throws SQLException {
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
		public AvvisoAnnualitaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AvvisoAnnualitaDTO bean = new AvvisoAnnualitaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AvvisoAnnualitaDTO bean) throws SQLException {
			bean.setNap(rs.getString("nap"));
			bean.setCodiceUtenza(rs.getString("codice_utenza"));
			bean.setAnnoRichPagamento(rs.getInt("anno_rich_pagamento"));
			bean.setFrazTotaleCanoneAnno(rs.getBigDecimal("fraz_totale_canone_anno"));
			bean.setTotaleCanoneAnnoCalc(rs.getBigDecimal("totale_canone_anno_calc"));
			bean.setEtichetta20Calc(rs.getString("etichetta20_calc"));
			bean.setValore20Calc(rs.getBigDecimal("valore20_calc"));
			bean.setVuoto(rs.getString("vuoto"));
			
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

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}