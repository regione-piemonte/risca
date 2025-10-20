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
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiAmminDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AvvisoDatiAmminDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type AvvisoDatiAmmin dao.
 *
 * @author CSI PIEMONTE
 */
public class AvvisoDatiAmminDAOImpl extends RiscaBeSrvGenericDAO<AvvisoDatiAmminDTO> implements AvvisoDatiAmminDAO {

	public static final String QUERY_INSERT = "INSERT INTO risca_r_avviso_dati_ammin "
			+ "(nap, codice_utenza, id_stato_debitorio, corpo_idrico, comune_di_presa, periodo_di_contribuzione, "
			+ "numero_protocollo_sped, data_protocollo_sped, etichetta21_calc, valore21_calc, etichetta22_calc, "
			+ "valore22_calc, totale_utenza_calc, scad_conc_eti_calc, scad_conc_calc, scadenza_concessione_calc, "
			+ "provvedimento_calc, testo, data_scad_emas_iso, imp_compens_canone, rec_canone, num_pratica, "
			+ "descr_utilizzo, tot_energ_prod, gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ "VALUES(:nap, :codiceUtenza, :idStatoDebitorio, :corpoIdrico, :comuneDiPresa, :periodoDiContribuzione, "
			+ ":dataProtocolloSped, :etichetta21Calc, :valore21Calc, :etichetta22Calc, :valore22Calc, :totaleUtenzaCalc, "
			+ ":scadConcEtiCalc, :scadConcCalc, :scadenzaConcessioneCalc, :provvedimentoCalc, :testo, :dataScadEmasIso, "
			+ ":impCompensCanone, :recCanone, :numPratica, :descrUtilizzo, :totEnergProd, :gestAttoreIns, :gestDataIns, "
			+ ":gestAttoreUpd, :gestDataUpd, :gestUid)";
	
	public static final String QUERY_INSERT_W = "INSERT INTO risca_w_avviso_dati_ammin "
			+ "(nap, codice_utenza, id_stato_debitorio, corpo_idrico, comune_di_presa, periodo_di_contribuzione, "
			+ "numero_protocollo_sped, data_protocollo_sped, etichetta21_calc, valore21_calc, etichetta22_calc, "
			+ "valore22_calc, totale_utenza_calc, scad_conc_eti_calc, scad_conc_calc, scadenza_concessione_calc, "
			+ "provvedimento_calc, testo, data_scad_emas_iso, imp_compens_canone, rec_canone, num_pratica, "
			+ "descr_utilizzo, tot_energ_prod) "
			+ "VALUES(:nap, :codiceUtenza, :idStatoDebitorio, :corpoIdrico, :comuneDiPresa, :periodoDiContribuzione, "
			+ ":numeroProtocolloSped, :dataProtocolloSped, :etichetta21Calc, :valore21Calc, :etichetta22Calc, :valore22Calc, :totaleUtenzaCalc, "
			+ ":scadConcEtiCalc, :scadConcCalc, :scadenzaConcessioneCalc, :provvedimentoCalc, :testo, :dataScadEmasIso, "
			+ ":impCompensCanone, :recCanone, :numPratica, :descrUtilizzo, :totEnergProd)";

	private static final String QUERY_UDPDATE_W_PERIODO_CONTRIB = "update risca_w_avviso_dati_ammin "
			+ "set periodo_di_contribuzione = :periodoDiContribuzione "
			+ "where nap = :nap "
			+ "and codice_utenza = :codiceUtenza";
	
	private static final String QUERY_UPDATE_W_TOT_UTENZA_CALC = "update risca_w_avviso_dati_ammin "
			+ " set totale_utenza_calc = COALESCE(totale_utenza_calc ::DECIMAL, 0) + :canoneAnnuo "
			+ " where codice_utenza = :codiceUtenza " + " and nap = :nap ";
	
	private static final String QUERY_UPDATE_W_COMPENSAZIONI = "update risca_w_avviso_dati_ammin "
			+ "set totale_utenza_calc = COALESCE(totale_utenza_calc ::DECIMAL, 0) - :compensazione,  "
			+ "imp_compens_canone = COALESCE(imp_compens_canone ::DECIMAL, 0) + :compensazione  "
			+ "where nap = :nap "
			+ "and codice_utenza = :codiceUtenza";
	
	private static final String QUERY_LOAD_W_BY_NAP = "select * "
			+ "from risca_w_avviso_dati_ammin  "
			+ "where nap = :nap "
			+ "order by codice_utenza";
	
	private static final String QUERY_INSERT_AVVISO_DATI_AMMIN_FROM_WORKING_BY_NAP = " INSERT INTO risca_r_avviso_dati_ammin "
			+ "(nap, codice_utenza, id_stato_debitorio, corpo_idrico, comune_di_presa, periodo_di_contribuzione, numero_protocollo_sped, data_protocollo_sped,  "
			+ " etichetta21_calc, valore21_calc, etichetta22_calc, valore22_calc, totale_utenza_calc, scad_conc_eti_calc, scad_conc_calc, scadenza_concessione_calc,  "
			+ " provvedimento_calc, testo, data_scad_emas_iso, imp_compens_canone, rec_canone, num_pratica, descr_utilizzo, tot_energ_prod,  "
			+ " gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ " select nap, codice_utenza, id_stato_debitorio, corpo_idrico, comune_di_presa, periodo_di_contribuzione, numero_protocollo_sped, data_protocollo_sped,  "
			+ " etichetta21_calc, valore21_calc, etichetta22_calc, valore22_calc, totale_utenza_calc, scad_conc_eti_calc, scad_conc_calc, scadenza_concessione_calc,  "
			+ " provvedimento_calc, testo, data_scad_emas_iso, imp_compens_canone, rec_canone, num_pratica, descr_utilizzo, tot_energ_prod,  "
			+ " :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, null "
			+ " from risca_w_avviso_dati_ammin "
			+ " where nap = :nap";
	
	private static final String QUERY_DELETE_AVVISO_DATI_AMMIN_WORKING_BY_NAP = " delete from risca_w_avviso_dati_ammin where nap = :nap ";
	
	@Override
	public AvvisoDatiAmminDTO saveAvvisoDatiAmmin(AvvisoDatiAmminDTO dto) throws DAOException {
		LOGGER.debug("[AvvisoDatiAmminDAOImpl::saveAvvisoDatiAmmin] BEGIN");
		try {
			dto = saveAvvisoDatiAmmin(dto, false);
		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiAmminDAOImpl::saveAvvisoDatiAmmin] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiAmminDAOImpl::saveAvvisoDatiAmmin] END");
		}

		return dto;
	}
	
	@Override
	public AvvisoDatiAmminDTO saveAvvisoDatiAmminWorking(AvvisoDatiAmminDTO dto) throws DAOException {
		LOGGER.debug("[AvvisoDatiAmminDAOImpl::saveAvvisoDatiAmminWorking] BEGIN");
		try {
			dto = saveAvvisoDatiAmmin(dto, true);
		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiAmminDAOImpl::saveAvvisoDatiAmminWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiAmminDAOImpl::saveAvvisoDatiAmminWorking] END");
		}

		return dto;
	}

	private AvvisoDatiAmminDTO saveAvvisoDatiAmmin(AvvisoDatiAmminDTO dto, boolean working) {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		
		map.put("nap", dto.getNap());
		map.put("codiceUtenza", dto.getCodiceUtenza());
		map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
		map.put("corpoIdrico", dto.getCorpoIdrico());
		map.put("comuneDiPresa", dto.getComuneDiPresa());
		map.put("periodoDiContribuzione", dto.getPeriodoDiContribuzione());
		map.put("numeroProtocolloSped", dto.getNumeroProtocolloSped());
		map.put("dataProtocolloSped", dto.getDataProtocolloSped());
		map.put("etichetta21Calc", dto.getEtichetta21Calc());
		map.put("valore21Calc", dto.getValore21Calc());
		map.put("etichetta22Calc", dto.getEtichetta22Calc());
		map.put("valore22Calc", dto.getValore22Calc());
		map.put("totaleUtenzaCalc", dto.getTotaleUtenzaCalc());
		map.put("scadConcEtiCalc", dto.getScadConcEtiCalc());
		map.put("scadConcCalc", dto.getScadConcCalc());
		map.put("scadenzaConcessioneCalc", dto.getScadenzaConcessioneCalc());
		map.put("provvedimentoCalc", dto.getProvvedimentoCalc());
		map.put("testo", dto.getTesto());
		map.put("dataScadEmasIso", dto.getDataScadEmasIso());
		map.put("impCompensCanone", dto.getImpCompensCanone());
		map.put("recCanone", dto.getRecCanone());
		map.put("numPratica", dto.getNumPratica());
		map.put("descrUtilizzo", dto.getDescrUtilizzo());
		map.put("totEnergProd", dto.getTotEnergProd());
		
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
	public Integer updateWorkingAvvisoDatiAmminPeriodoContrib(String periodoDiContribuzione, String nap,
			String codiceUtenza) throws DAOException {
		LOGGER.debug("[AvvisoDatiAmminDAOImpl::updateWAvvisoDatiAmminPeriodoContrib] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("periodoDiContribuzione", periodoDiContribuzione);
			map.put("nap", nap);
			map.put("codiceUtenza", codiceUtenza);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UDPDATE_W_PERIODO_CONTRIB, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiAmminDAOImpl::updateWAvvisoDatiAmminPeriodoContrib] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiAmminDAOImpl::updateWAvvisoDatiAmminPeriodoContrib] END");
		}

		return res;
	}
	
	@Override
	public Integer updateWorkingAvvisoDatiAmminTotaleUtenza(BigDecimal canoneAnnuo, String nap,
			String codiceUtenza) throws DAOException {
		LOGGER.debug("[AvvisoDatiAmminDAOImpl::updateWAvvisoDatiAmminTotaleUtenza] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("canoneAnnuo", canoneAnnuo);
			map.put("nap", nap);
			map.put("codiceUtenza", codiceUtenza);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_W_TOT_UTENZA_CALC, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiAmminDAOImpl::updateWAvvisoDatiAmminTotaleUtenza] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiAmminDAOImpl::updateWAvvisoDatiAmminTotaleUtenza] END");
		}

		return res;
	}
	
	@Override
	public Integer updateWorkingAvvisoDatiAmminCompensazione(BigDecimal compensazione, String nap,
			String codiceUtenza) throws DAOException {
		LOGGER.debug("[AvvisoDatiAmminDAOImpl::updateWorkingAvvisoDatiAmminCompensazioni] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("compensazione", compensazione);
			map.put("nap", nap);
			map.put("codiceUtenza", codiceUtenza);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_W_COMPENSAZIONI, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiAmminDAOImpl::updateWorkingAvvisoDatiAmminCompensazioni] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiAmminDAOImpl::updateWorkingAvvisoDatiAmminCompensazioni] END");
		}

		return res;
	}
	

	@Override
	public List<AvvisoDatiAmminDTO> loadAvvisoDatiAmminWorkingByNap(String nap) {
		LOGGER.debug("[AvvisoDatiAmminDAOImpl::loadAvvisiDatiAmminWorkingByNap] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_W_BY_NAP, null, null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[AvvisoDatiAmminDAOImpl::loadAvvisiDatiAmminWorkingByNap] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[AvvisoDatiAmminDAOImpl::loadAvvisiDatiAmminWorkingByNap] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AvvisoDatiAmminDAOImpl::loadAvvisiDatiAmminWorkingByNap] END");
		}
	}

	
	@Override
	public Integer copyAvvisoDatiAmminFromWorkingByNap(String nap, String attore) throws DAOException {
		LOGGER.debug("[AvvisoDatiAmminDAOImpl::copyAvvisoDatiAmminFromWorkingByNap] BEGIN");
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
			res = template.update(getQuery(QUERY_INSERT_AVVISO_DATI_AMMIN_FROM_WORKING_BY_NAP, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AvvisoDatiAmminDAOImpl::copyAvvisoDatiAmminFromWorkingByNap] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiAmminDAOImpl::copyAvvisoDatiAmminFromWorkingByNap] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteAvvisoDatiAmminWorkingByNap(String nap) throws DAOException {
		LOGGER.debug("[AvvisoDatiAmminDAOImpl::deleteAvvisoDatiAmminWorkingByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_AVVISO_DATI_AMMIN_WORKING_BY_NAP, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AvvisoDatiAmminDAOImpl::deleteAvvisoDatiAmminWorkingByNap] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiAmminDAOImpl::deleteAvvisoDatiAmminWorkingByNap] END");
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
	public RowMapper<AvvisoDatiAmminDTO> getRowMapper() throws SQLException {
		return new AvvisoDatiAmminRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<AvvisoDatiAmminDTO> getExtendedRowMapper() throws SQLException {
		return new AvvisoDatiAmminRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class AvvisoDatiAmminRowMapper implements RowMapper<AvvisoDatiAmminDTO> {
		
		/**
		 * Instantiates a new AvvisoDatiAmmin row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public AvvisoDatiAmminRowMapper() throws SQLException {
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
		public AvvisoDatiAmminDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AvvisoDatiAmminDTO bean = new AvvisoDatiAmminDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AvvisoDatiAmminDTO bean) throws SQLException {
			bean.setNap(rs.getString("nap"));
			bean.setCodiceUtenza(rs.getString("codice_utenza"));
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setCorpoIdrico(rs.getString("corpo_idrico"));
			bean.setComuneDiPresa(rs.getString("comune_di_presa"));
			bean.setPeriodoDiContribuzione(rs.getString("periodo_di_contribuzione"));
			bean.setNumeroProtocolloSped(rs.getString("numero_protocollo_sped"));
			bean.setDataProtocolloSped(rs.getDate("data_protocollo_sped"));
			bean.setEtichetta21Calc(rs.getString("etichetta21_calc"));
			bean.setValore21Calc(rs.getString("valore21_calc"));
			bean.setEtichetta22Calc(rs.getString("etichetta22_calc"));
			bean.setValore22Calc(rs.getString("valore22_calc"));
			bean.setTotaleUtenzaCalc(rs.getString("totale_utenza_calc"));
			bean.setScadConcEtiCalc(rs.getString("scad_conc_eti_calc"));
			bean.setScadConcCalc(rs.getString("scad_conc_calc"));
			bean.setScadenzaConcessioneCalc(rs.getString("scadenza_concessione_calc"));
			bean.setProvvedimentoCalc(rs.getString("provvedimento_calc"));
			bean.setTesto(rs.getString("testo"));
			bean.setDataScadEmasIso(rs.getDate("data_scad_emas_iso"));
			bean.setImpCompensCanone(rs.getString("imp_compens_canone"));
			bean.setRecCanone(rs.getString("rec_canone"));
			bean.setNumPratica(rs.getString("num_pratica"));
			bean.setDescrUtilizzo(rs.getString("descr_utilizzo"));
			bean.setTotEnergProd(rs.getBigDecimal("tot_energ_prod"));
			
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