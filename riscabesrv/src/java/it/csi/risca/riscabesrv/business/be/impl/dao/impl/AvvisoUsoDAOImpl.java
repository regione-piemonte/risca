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
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoUsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AvvisoUsoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type AvvisoDatiAmmin dao.
 *
 * @author CSI PIEMONTE
 */
public class AvvisoUsoDAOImpl extends RiscaBeSrvGenericDAO<AvvisoUsoDTO> implements AvvisoUsoDAO {

	public static final String QUERY_INSERT = "INSERT INTO risca_r_avviso_uso "
			+ "(nap, codice_utenza, anno_rich_pagamento, uso_denominazione, unita_di_misura, "
			+ "quantita, canone_unitario, canone_uso, unita_mis1_calc, quantita_calc, "
			+ "condizioni_particolari_calc, uso_denominazione_p_calc, unita_mis_p_calc, "
			+ "quantita_p_calc, unita_di_misura_p_calc, canone_unitario_p_calc, "
			+ "canone_uso_p_calc, perc_falda_prof, gest_attore_ins, gest_data_ins, "
			+ "gest_attore_upd, gest_data_upd, gest_uid) "
			+ "VALUES(:nap, :codiceUtenza, :annoRichPagamento, :usoDenominazione, :unitaDiMisura, "
			+ ":quantita, :canoneUnitario, :canoneUso, :unitaMis1Calc, :quantitaCalc, "
			+ ":condizioniParticolariCalc, :usoDenominazionePCalc, :unitaMisPCalc, "
			+ ":quantitaPCalc, :unitaDiMisuraPCalc, :canoneUnitarioPCalc, :canoneUsoPCalc, "
			+ ":percFaldaProf, :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, "
			+ ":gestUid)";
	
	public static final String QUERY_INSERT_W = "INSERT INTO risca_w_avviso_uso "
			+ "(nap, codice_utenza, anno_rich_pagamento, uso_denominazione, unita_di_misura, "
			+ "quantita, canone_unitario, canone_uso, unita_mis1_calc, quantita_calc, "
			+ "condizioni_particolari_calc, uso_denominazione_p_calc, unita_mis_p_calc, "
			+ "quantita_p_calc, unita_di_misura_p_calc, canone_unitario_p_calc, "
			+ "canone_uso_p_calc, perc_falda_prof) "
			+ "VALUES(:nap, :codiceUtenza, :annoRichPagamento, :usoDenominazione, :unitaDiMisura, "
			+ ":quantita, :canoneUnitario, :canoneUso, :unitaMis1Calc, :quantitaCalc, "
			+ ":condizioniParticolariCalc, :usoDenominazionePCalc, :unitaMisPCalc, "
			+ ":quantitaPCalc, :unitaDiMisuraPCalc, :canoneUnitarioPCalc, :canoneUsoPCalc, "
			+ ":percFaldaProf)";
	
	public static final String QUERY_SUM_W_CANONE_USO_P_CALC= 
			  " SELECT SUM(canone_uso_p_calc) as sum_canone_uso_p_calc"
			+ " FROM risca_w_avviso_uso "
			+ " WHERE nap = :nap "
			+ " AND codice_utenza = :codiceUtenza "
			+ " AND anno_rich_pagamento = :anno ";
	
	public static final String QUERY_LOAD_W_BY_NAP = "select * "
			+ "from risca_w_avviso_uso "
			+ "where nap = :nap "
			+ "order by codice_utenza, anno_rich_pagamento, uso_denominazione ";
	
	private static final String QUERY_INSERT_AVVISO_USO_FROM_WORKING_BY_NAP = " INSERT INTO risca_r_avviso_uso "
			+ " (nap, codice_utenza, anno_rich_pagamento, uso_denominazione, unita_di_misura, quantita, canone_unitario, canone_uso, unita_mis1_calc,  "
			+ " quantita_calc, condizioni_particolari_calc, uso_denominazione_p_calc, unita_mis_p_calc, quantita_p_calc, unita_di_misura_p_calc,  "
			+ " canone_unitario_p_calc, canone_uso_p_calc, perc_falda_prof,  "
			+ " gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ " select nap, codice_utenza, anno_rich_pagamento, uso_denominazione, unita_di_misura, quantita, canone_unitario, canone_uso, unita_mis1_calc,  "
			+ " quantita_calc, condizioni_particolari_calc, uso_denominazione_p_calc, unita_mis_p_calc, quantita_p_calc, unita_di_misura_p_calc,  "
			+ " canone_unitario_p_calc, canone_uso_p_calc, perc_falda_prof,  "
			+ " :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, null "
			+ " from risca_w_avviso_uso "
			+ " where nap = :nap ";
	
	private static final String QUERY_DELETE_AVVISO_USO_WORKING_BY_NAP = " delete from RISCA_W_AVVISO_USO where nap = :nap ";

	
	@Override
	public AvvisoUsoDTO saveAvvisoUso(AvvisoUsoDTO dto) throws DAOException {
		LOGGER.debug("[AvvisoUsoDAOImpl::saveAvvisoUso] BEGIN");
		try {
			dto = saveAvvisoUso(dto, false);
		} catch (Exception e) {
			LOGGER.error("[AvvisoUsoDAOImpl::saveAvvisoUso] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoUsoDAOImpl::saveAvvisoUso] END");
		}

		return dto;
	}
	
	@Override
	public AvvisoUsoDTO saveAvvisoUsoWorking(AvvisoUsoDTO dto) throws DAOException {
		LOGGER.debug("[AvvisoUsoDAOImpl::saveAvvisoUsoWorking] BEGIN");
		try {
			dto = saveAvvisoUso(dto, true);
		} catch (Exception e) {
			LOGGER.error("[AvvisoUsoDAOImpl::saveAvvisoUsoWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoUsoDAOImpl::saveAvvisoUsoWorking] END");
		}

		return dto;
	}

	private AvvisoUsoDTO saveAvvisoUso(AvvisoUsoDTO dto, boolean working) {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		
		map.put("nap", dto.getNap());
		map.put("codiceUtenza", dto.getCodiceUtenza());
		map.put("annoRichPagamento", dto.getAnnoRichPagamento());
		map.put("usoDenominazione", dto.getUsoDenominazione());
		map.put("unitaDiMisura", dto.getUnitaDiMisura());
		map.put("quantita", dto.getQuantita());
		map.put("canoneUnitario", dto.getCanoneUnitario());
		map.put("canoneUso", dto.getCanoneUso());
		map.put("unitaMis1Calc", dto.getUnitaMis1Calc());
		map.put("quantitaCalc", dto.getQuantitaCalc());
		map.put("condizioniParticolariCalc", dto.getCondizioniParticolariCalc());
		map.put("usoDenominazionePCalc", dto.getUsoDenominazionePCalc());
		map.put("unitaMisPCalc", dto.getUnitaMisPCalc());
		map.put("quantitaPCalc", dto.getQuantitaPCalc());
		map.put("unitaDiMisuraPCalc", dto.getUnitaDiMisuraPCalc());
		map.put("canoneUnitarioPCalc", dto.getCanoneUnitarioPCalc());
		map.put("canoneUsoPCalc", dto.getCanoneUsoPCalc());
		map.put("percFaldaProf", dto.getPercFaldaProf());
		
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
	public BigDecimal sumCanoneUsoPcalc(String nap, String codiceUtenza, int anno) throws DAOException {
		LOGGER.debug("[AvvisoUsoDAOImpl::sumCanoneUsoPcalc] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap",nap);
			map.put("codiceUtenza",codiceUtenza);
			map.put("anno",anno);
			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(getQuery(QUERY_SUM_W_CANONE_USO_P_CALC, null, null), params, new BigDecimalRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error("[AvvisoUsoDAOImpl::sumCanoneUsoPcalc] ERROR : " ,e);
				throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoUsoDAOImpl::sumCanoneUsoPcalc] END");
		}
	}
	
	@Override
	public List<AvvisoUsoDTO> loadAvvisoUsoWorkingByNap(String nap) {
		LOGGER.debug("[AvvisoUsoDAOImpl::loadAvvisoUsoWorkingByNap] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_W_BY_NAP, null, null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[AvvisoUsoDAOImpl::loadAvvisoUsoWorkingByNap] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[AvvisoUsoDAOImpl::loadAvvisoUsoWorkingByNap] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AvvisoUsoDAOImpl::loadAvvisoAnnualitaWorkingByNap] END");
		}
	}
	
	@Override
	public Integer copyAvvisoUsoFromWorkingByNap(String nap, String attore) throws DAOException {
		LOGGER.debug("[AvvisoUsoDAOImpl::copyAvvisoUsoFromWorkingByNap] BEGIN");
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
			res = template.update(getQuery(QUERY_INSERT_AVVISO_USO_FROM_WORKING_BY_NAP, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AvvisoUsoDAOImpl::copyAvvisoUsoFromWorkingByNap] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoUsoDAOImpl::copyAvvisoUsoFromWorkingByNap] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteAvvisoUsoWorkingByNap(String nap) throws DAOException {
		LOGGER.debug("[AvvisoUsoDAOImpl::deleteAvvisoUsoWorkingByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_AVVISO_USO_WORKING_BY_NAP, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AvvisoUsoDAOImpl::deleteAvvisoUsoWorkingByNap] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoUsoDAOImpl::deleteAvvisoUsoWorkingByNap] END");
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
	public RowMapper<AvvisoUsoDTO> getRowMapper() throws SQLException {
		return new AvvisoUsoRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<AvvisoUsoDTO> getExtendedRowMapper() throws SQLException {
		return new AvvisoUsoRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class AvvisoUsoRowMapper implements RowMapper<AvvisoUsoDTO> {
		
		/**
		 * Instantiates a new AvvisoDatiAmmin row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public AvvisoUsoRowMapper() throws SQLException {
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
		public AvvisoUsoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AvvisoUsoDTO bean = new AvvisoUsoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AvvisoUsoDTO bean) throws SQLException {	
			bean.setNap(rs.getString("nap"));
			bean.setCodiceUtenza(rs.getString("codice_utenza"));
			bean.setAnnoRichPagamento(rs.getInt("anno_rich_pagamento"));
			bean.setUsoDenominazione(rs.getString("uso_denominazione"));
			bean.setUnitaDiMisura(rs.getString("unita_di_misura"));
			bean.setQuantita(rs.getBigDecimal("quantita"));
			bean.setCanoneUnitario(rs.getBigDecimal("canone_unitario"));
			bean.setCanoneUso(rs.getBigDecimal("canone_uso"));
			bean.setUnitaMis1Calc(rs.getString("unita_mis1_calc"));
			bean.setQuantitaCalc(rs.getString("quantita_calc"));
			bean.setCondizioniParticolariCalc(rs.getString("condizioni_particolari_calc"));
			bean.setUsoDenominazionePCalc(rs.getString("uso_denominazione_p_calc"));
			bean.setUnitaMisPCalc(rs.getString("unita_mis_p_calc"));
			bean.setQuantitaPCalc(rs.getBigDecimal("quantita_p_calc"));
			bean.setUnitaDiMisuraPCalc(rs.getString("unita_di_misura_p_calc"));
			bean.setCanoneUnitarioPCalc(rs.getBigDecimal("canone_unitario_p_calc"));
			bean.setCanoneUsoPCalc(rs.getBigDecimal("canone_uso_p_calc"));
			bean.setPercFaldaProf(rs.getBigDecimal("perc_falda_prof"));

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
	
	public static class BigDecimalRowMapper implements RowMapper<BigDecimal> {

		/**
		 * Instantiates a new String row mapper.
		 */
		public BigDecimalRowMapper() {
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
		public BigDecimal mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getBigDecimal("sum_canone_uso_p_calc");
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}