/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class AnnualitaSdAmbienteDAOImpl extends RiscaBeSrvGenericDAO<AnnualitaSdDTO> {

	@Autowired
	private AnnualitaUsoSdDAO annualitaUsoSdDAO;
	
	public static final String QUERY_SELECT_ANNUALITA_SD = "SELECT * FROM risca_r_annualita_sd "
			+ "WHERE id_stato_debitorio = :idStatoDebitorio ORDER BY anno ";
	
	public static final String QUERY_SELECT_ANNUALITA_SD_SPECIALI = "SELECT rras.* FROM risca_t_stato_debitorio rtsd "
			+ "INNER JOIN risca_r_annualita_sd rras ON rtsd.id_stato_debitorio = rras.id_stato_debitorio "
			+ "WHERE rtsd.id_stato_debitorio = :idStatoDebitorio "
			+ "AND rtsd.flg_invio_speciale = 1 ";
	
	public static final String QUERY_SELECT_ANNUALITA_SD_INTERE = "SELECT * FROM risca_r_annualita_sd "
			+ "WHERE id_stato_debitorio = :idStatoDebitorio "
			+ "AND flg_rateo_prima_annualita = 0  "
			+ "AND numero_mesi IS NULL ";
	
	public static final String QUERY_SELECT_ANNUALITA_SD_BY_NAP_COD_UTENZA_ANNO = 
			  "SELECT id_annualita_sd, id_stato_debitorio, anno, json_dt_riscossione, canone_annuo, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid, "
			+ "flg_rateo_prima_annualita, COALESCE(numero_mesi, 12) numero_mesi, data_inizio, id_componente_dt "
			+ "FROM RISCA_R_ANNUALITA_SD "
			+ "WHERE id_stato_debitorio = "
			+ "	  (select id_stato_debitorio "
			+ "	   from RISCA_W_AVVISO_DATI_AMMIN "
			+ "	   where nap = :nap "
			+ "	   and codice_utenza = :codiceUtenza) "
			+ "AND anno = :anno";
	
	public static final String QUERY_INSERT="INSERT INTO risca_r_annualita_sd "
			+ "(id_annualita_sd, id_stato_debitorio, anno, json_dt_riscossione, canone_annuo, flg_rateo_prima_annualita,numero_mesi,"
			+ "data_inizio, id_componente_dt , "
			+ "gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, "
			+ " gest_uid) "
			+ "VALUES(:idAnnualitaSd, :idStatoDebitorio, :anno, to_jsonb(:jsonDtAnnualitaSd::json) , :canoneAnnuo,:flagRateoPrimaAnnualita, :numeroMesi,"
			+ " :dataInizio, :idComponenteDt, "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid) ";
	
	public static final String QUERY_UPDATE = "update risca_r_annualita_sd "
			+ "	set id_stato_debitorio = :idStatoDebitorio, anno = :anno, json_dt_riscossione = to_jsonb(:jsonDtAnnualitaSd::json), "
			+ "	canone_annuo = :canoneAnnuo, flg_rateo_prima_annualita =:flagRateoPrimaAnnualita,"
			+ " numero_mesi =:numeroMesi, data_inizio =:dataInizio, id_componente_dt =:idComponenteDt,gest_attore_upd =:gestAttoreUpd,"
			+ " gest_data_upd = :gestDataUpd "
			+ "	where id_annualita_sd  = :idAnnualitaSd ";
	
	public static final String QUERY_UPDATE_WORKING = "update risca_w_annualita_sd "
			+ "	set id_stato_debitorio = :idStatoDebitorio, anno = :anno, json_dt_riscossione = to_jsonb(:jsonDtAnnualitaSd::json), "
			+ "	canone_annuo = :canoneAnnuo, flg_rateo_prima_annualita =:flagRateoPrimaAnnualita,"
			+ " numero_mesi =:numeroMesi, data_inizio =:dataInizio, id_componente_dt =:idComponenteDt "
			+ "	where id_annualita_sd  = :idAnnualitaSd ";
	
	public static final String QUERY_INSERT_WORKING="INSERT INTO risca_w_annualita_sd "
			+ "(id_annualita_sd, id_stato_debitorio, anno, json_dt_riscossione, canone_annuo, flg_rateo_prima_annualita,numero_mesi,"
			+ "data_inizio, id_componente_dt) "
			+ "VALUES(:idAnnualitaSd, :idStatoDebitorio, :anno, to_jsonb(:jsonDtAnnualitaSd::json) , :canoneAnnuo,:flagRateoPrimaAnnualita, :numeroMesi,"
			+ " :dataInizio, :idComponenteDt) ";
	
	private static final String QUERY_DELETE_ANNUALITA_SD_BY_ID = "DELETE FROM risca_r_annualita_sd WHERE id_annualita_sd = :idAnnualitSd";
	
	
	/**
	 * {@inheritDoc}
	 */

	public List<AnnualitaSdDTO> loadAnnualitaSd(Long idStatoDebitorio, Boolean speciali, Boolean intere) {
		LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSd] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);
			if(speciali != null && speciali.equals(true)) {
				return template.query(getQuery(QUERY_SELECT_ANNUALITA_SD_SPECIALI, null, null), params, getExtendedRowMapper());				
			} else if(intere != null && intere.equals(true)) {
				return template.query(getQuery(QUERY_SELECT_ANNUALITA_SD_INTERE, null, null), params, getExtendedRowMapper());
			} else {
				return template.query(getQuery(QUERY_SELECT_ANNUALITA_SD, null, null), params, getExtendedRowMapper());
			}
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSd]Data not found for idStatoDebitorio: "+ idStatoDebitorio);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSd] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSd] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSd] END");
		}
	}
	

	public AnnualitaSdDTO loadAnnualitaSdByNapCodiceUtenzaAnno(String nap, String codiceUtenza, int anno) {
		LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSdByNapCodiceUtenzaAnno] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			map.put("codiceUtenza", codiceUtenza);
			map.put("anno", anno);
			MapSqlParameterSource params = getParameterValue(map);
			
			return template.queryForObject(getQuery(QUERY_SELECT_ANNUALITA_SD_BY_NAP_COD_UTENZA_ANNO, null, null), params, getExtendedRowMapper());
			
		} catch (SQLException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSdByNapCodiceUtenzaAnno] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSdByNapCodiceUtenzaAnno] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSdByNapCodiceUtenzaAnno] END");
		}
	}

	public AnnualitaSdDTO insertAnnualitaSd(AnnualitaSdDTO dto) throws Exception {
		LOGGER.debug("[AnnualitaSdDAOImpl::insertAnnualitaSd] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			KeyHolder keyHolder = new GeneratedKeyHolder();
			Long genId  = findNextSequenceValue("SEQ_RISCA_R_ANNUALITA_SD");
			map.put("idAnnualitaSd", genId);
			map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
			map.put("anno", dto.getAnno());
			map.put("jsonDtAnnualitaSd", dto.getJsonDtAnnualitaSd());
			map.put("canoneAnnuo", dto.getCanoneAnnuo() != null? dto.getCanoneAnnuo().setScale(2, RoundingMode.HALF_UP):null);
			map.put("flagRateoPrimaAnnualita", dto.getFlgRateoPrimaAnnualita());
			map.put("numeroMesi", dto.getNumeroMesi());
			map.put("dataInizio", dto.getDataInizio()); 
			map.put("idComponenteDt", dto.getIdComponenteDt());    
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_INSERT;
			template.update(getQuery(query, null, null), params, keyHolder);
			dto.setIdAnnualitaSd(genId); 
			if(dto.getAnnualitaUsoSd() != null)
			if(!dto.getAnnualitaUsoSd().isEmpty())
				for (AnnualitaUsoSdDTO annualitaUsoSdDTO : dto.getAnnualitaUsoSd()) {
					annualitaUsoSdDTO.setIdAnnualitaSd(genId);
					annualitaUsoSdDAO.saveAnnualitaUsoSdDTO(annualitaUsoSdDTO);
				}
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::insertAnnualitaSd] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error("AnnualitaSdDAOImpl::insertAnnualitaSd] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (Exception e) {
			LOGGER.error("[AnnualitaSdDAOImpl::insertAnnualitaSd] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}
		LOGGER.debug("[AnnualitaSdDAOImpl::insertAnnualitaSd] BEGIN");
		return dto;
	}
	

	public AnnualitaSdDTO insertAnnualitaSdWorking(AnnualitaSdDTO dto) throws DataAccessException, SQLException {
		LOGGER.debug("[AnnualitaSdDAOImpl::insertAnnualitaSdWorking] BEGIN");
		Map<String, Object> map = new HashMap<>();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		Long genId  = findNextSequenceValue("SEQ_RISCA_R_ANNUALITA_SD");
	    map.put("idAnnualitaSd", genId);
	    map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
	    map.put("anno", dto.getAnno());
	    map.put("jsonDtAnnualitaSd", dto.getJsonDtAnnualitaSd());
	    map.put("canoneAnnuo", dto.getCanoneAnnuo());
	    map.put("flagRateoPrimaAnnualita", dto.getFlgRateoPrimaAnnualita());
	    map.put("numeroMesi", dto.getNumeroMesi());
	    map.put("dataInizio", dto.getDataInizio()); 
	    map.put("idComponenteDt", dto.getIdComponenteDt());    
		
		MapSqlParameterSource params = getParameterValue(map);
		String query = QUERY_INSERT_WORKING;
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setIdAnnualitaSd(genId); 
		
		LOGGER.debug("[AnnualitaSdDAOImpl::insertAnnualitaSdWorking] BEGIN");
		return dto;
	}
	
	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<AnnualitaSdDTO> getRowMapper() throws SQLException {

		return new AnnualitaSdRowMapper();
	}

	@Override
	public RowMapper<AnnualitaSdDTO> getExtendedRowMapper() throws SQLException {
		return new AnnualitaSdRowMapper();
	}

	public static class AnnualitaSdRowMapper implements RowMapper<AnnualitaSdDTO> {

		public AnnualitaSdRowMapper() throws SQLException {
			// Instantiate class
		}

		@Override
		public AnnualitaSdDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			AnnualitaSdDTO bean = new AnnualitaSdDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AnnualitaSdDTO bean) throws SQLException {
			bean.setIdAnnualitaSd(rs.getLong("id_annualita_sd"));
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setAnno(rs.getInt("anno"));
			bean.setJsonDtAnnualitaSd(rs.getString("json_dt_riscossione"));
			bean.setCanoneAnnuo(rs.getBigDecimal("canone_annuo"));
			bean.setFlgRateoPrimaAnnualita(rs.getInt("flg_rateo_prima_annualita"));
			bean.setNumeroMesi(rs.getInt("numero_mesi"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			bean.setDataInizio(rs.getDate("data_inizio"));
			bean.setIdComponenteDt(rs.getInt("id_componente_dt"));
		}

	}


	public AnnualitaSdDTO updateAnnualitaSd(AnnualitaSdDTO dto) throws Exception {
		LOGGER.debug("[AnnualitaSdDAOImpl::updateAnnualitaSd] BEGIN");


		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();

			map.put("idAnnualitaSd", dto.getIdAnnualitaSd());
			map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
			map.put("anno", dto.getAnno());
			map.put("jsonDtAnnualitaSd", dto.getJsonDtAnnualitaSd());
			map.put("canoneAnnuo", dto.getCanoneAnnuo());
			map.put("flagRateoPrimaAnnualita", dto.getFlgRateoPrimaAnnualita());
			map.put("numeroMesi", dto.getNumeroMesi());
			map.put("dataInizio", dto.getDataInizio()); 
			map.put("idComponenteDt", dto.getIdComponenteDt());    

			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_UPDATE;

			template.update(getQuery(query, null, null), params);
			for (AnnualitaUsoSdDTO annualitaUsoSdDTO : dto.getAnnualitaUsoSd()) {

				annualitaUsoSdDAO.updateAnnualitaUsoSdDTO(annualitaUsoSdDTO);
			}
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::updateAnnualitaSd] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error("AnnualitaSdDAOImpl::updateAnnualitaSd] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (Exception e) {
			LOGGER.error("[AnnualitaSdDAOImpl::updateAnnualitaSd] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}
		
		LOGGER.debug("[AnnualitaSdDAOImpl::updateAnnualitaSd] BEGIN");
		return dto;
	}
	

	public AnnualitaSdDTO updateAnnualitaSdWorking(AnnualitaSdDTO dto) throws DataAccessException, SQLException {
		LOGGER.debug("[AnnualitaSdDAOImpl::updateAnnualitaSdWorking] BEGIN");

		Map<String, Object> map = new HashMap<>();
		map.put("idAnnualitaSd", dto.getIdAnnualitaSd());
		map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
		map.put("anno", dto.getAnno());
		map.put("jsonDtAnnualitaSd", dto.getJsonDtAnnualitaSd());
		map.put("canoneAnnuo", dto.getCanoneAnnuo());
		map.put("flagRateoPrimaAnnualita", dto.getFlgRateoPrimaAnnualita());
		map.put("numeroMesi", dto.getNumeroMesi());
		map.put("dataInizio", dto.getDataInizio());
		map.put("idComponenteDt", dto.getIdComponenteDt());
		MapSqlParameterSource params = getParameterValue(map);
		
		template.update(getQuery(QUERY_UPDATE_WORKING, null, null), params);

		LOGGER.debug("[AnnualitaSdDAOImpl::updateAnnualitaSdWorking] BEGIN");
		return dto;
	}


	public AnnualitaSdDTO deleteAnnualitaSd(AnnualitaSdDTO annualitaSdDTO) throws Exception {

			LOGGER.debug("[AnnualitaSdDAOImpl::deleteAnnualitaSd] BEGIN");
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("idAnnualitSd", annualitaSdDTO.getIdAnnualitaSd());
				MapSqlParameterSource params = getParameterValue(map);
	
				for (AnnualitaUsoSdDTO annualitaUsoSdDTO : annualitaSdDTO.getAnnualitaUsoSd()) {
					annualitaUsoSdDAO.deleteAnnualitaUsoSdDTO(annualitaUsoSdDTO);
				}
				
				template.update(getQuery(QUERY_DELETE_ANNUALITA_SD_BY_ID, null, null), params);
				
			} catch (DataAccessException e) {
				LOGGER.error("[AnnualitaSdDAOImpl::deleteAnnualitaSd] ERROR : " ,e);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw new Exception(Constants.ERRORE_GENERICO);
			} catch (Exception e) {
				LOGGER.error("[AnnualitaSdDAOImpl::deleteAnnualitaSd] ERROR : " ,e);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw new Exception(Constants.ERRORE_GENERICO);
			} finally {
				LOGGER.debug("[AnnualitaSdDAOImpl::deleteAnnualitaSd] END");
			}
			return annualitaSdDTO;
		
	}

}
