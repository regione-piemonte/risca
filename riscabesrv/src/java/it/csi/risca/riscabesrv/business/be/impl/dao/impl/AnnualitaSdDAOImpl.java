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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente.AnnualitaSdAmbienteDAOImpl;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.tributi.AnnualitaSdTributiDAOImpl;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdDTO;
import it.csi.risca.riscabesrv.dto.DTGrandeIdroDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatiTecniciAmbienteDTO;
import it.csi.risca.riscabesrv.dto.ambiente.TipoUsoDatoTecnicoDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

public class AnnualitaSdDAOImpl extends RiscaBeSrvGenericDAO<AnnualitaSdDTO> implements AnnualitaSdDAO {

	private final String className = this.getClass().getSimpleName();
	
	@Autowired
	private AnnualitaUsoSdDAO annualitaUsoSdDAO;
	
	@Autowired 
	private AnnualitaSdAmbienteDAOImpl annualitaSdAmbienteDaoImpl;
	
	@Autowired 
	private AnnualitaSdTributiDAOImpl annualitaSdTributiDaoImpl;
	
	@Autowired
	private AmbitiDAO ambitiDAO;
	
	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;
	
	private static final String AMBIENTE = "AMBIENTE";
	private static final String OPERE_PUBBLICHE = "Opere pubbliche";
	private static final String ATTIVITA_ESTRATTIVE = "Attivita estrattive";
	private static final String TRIBUTI = "TRIBUTI";
    private Boolean devMode;
	public static final String COD_TIPO_USO_GRANDE_IDROELETTRICO = "GRANDE_IDROELETTRICO";
	
	public static final String QUERY_SELECT_ANNUALITA_SD = "SELECT * FROM risca_r_annualita_sd "
			+ "WHERE id_stato_debitorio = :idStatoDebitorio ORDER BY anno ";
	
	public static final String QUERY_SELECT_ANNUALITA_SD_BY_ID = "SELECT * FROM risca_r_annualita_sd "
			+ "WHERE id_annualita_sd = :idAnnualitaSd ";
	
	
	public static final String QUERY_SELECT_ANNUALITA_SD_SPECIALI = "SELECT rras.* FROM risca_t_stato_debitorio rtsd "
			+ "INNER JOIN risca_r_annualita_sd rras ON rtsd.id_stato_debitorio = rras.id_stato_debitorio "
			+ "WHERE rtsd.id_stato_debitorio = :idStatoDebitorio "
			+ "AND rtsd.flg_invio_speciale = 1 ";
	
	public static final String QUERY_SELECT_ANNUALITA_SD_INTERE = "SELECT * FROM risca_r_annualita_sd "
			+ "WHERE id_stato_debitorio = :idStatoDebitorio "
			+ "AND flg_rateo_prima_annualita = 0  "
			+ "AND (numero_mesi is null or numero_mesi = 0 or numero_mesi = 12) ";
	
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
	
	public static final String QUERY_UPDATE_JSONDT_SD = "update risca_r_annualita_sd "
			+ "	set  json_dt_riscossione = to_jsonb(:jsonDtAnnualitaSd::json), "
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
	
	public static final String QUERY_SELECT_ANNUALITA_SD_WORKING = "SELECT * FROM risca_w_annualita_sd "
			+ "WHERE id_stato_debitorio = :idStatoDebitorio ";
	
	private static final String QUERY_DELETE_ANNUALITA_SD_WORKING_BY_ID = "DELETE FROM risca_w_annualita_sd WHERE id_annualita_sd = :idAnnualitaSd";
	
	private static final String QUERY_INSERT_ANNUALITA_SD_FROM_WORKING = "insert into RISCA_R_ANNUALITA_SD (id_annualita_sd, id_stato_debitorio, anno, json_dt_riscossione, "
			+ "canone_annuo, flg_rateo_prima_annualita, numero_mesi, data_inizio, id_componente_dt, "
			+ "gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ "select id_annualita_sd, id_stato_debitorio, anno, json_dt_riscossione,  "
			+ "canone_annuo, flg_rateo_prima_annualita, numero_mesi, data_inizio, id_componente_dt, "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid "
			+ "from RISCA_W_ANNUALITA_SD where id_stato_debitorio = :idStatoDebitorio ";
	
	public static final String QUERY_SELECT_ANNUALITA_SD_GRANDE_IDRO = " select * "
			+ " from RISCA_R_ANNUALITA_SD "
			+ " where id_annualita_sd = (select max(id_annualita_sd) "
			+ "		from RISCA_R_ANNUALITA_SD "
			+ "		where id_stato_debitorio = :idStatoDebitorio) ";
	
	/**
	 * {@inheritDoc}
	 * @throws SQLException 
	 */
	@Override
	public List<AnnualitaSdDTO> loadAnnualitaSd(Long idStatoDebitorio, Boolean speciali, Boolean intere) throws SQLException {
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
			return Collections.emptyList();
		} catch (SQLException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSd] Errore nell'esecuzione della query", e);
            throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSd] Errore nell'accesso ai dati", e);
            throw e;
		} finally {
			LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSd] END");
		}
	}
	
	@Override
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
	@Override
	public AnnualitaSdDTO insertAnnualitaSd(AnnualitaSdDTO dto, Long idAmbito) throws Exception {
		LOGGER.debug("[AnnualitaSdDAOImpl::insertAnnualitaSd] BEGIN");
		String ambito = "";
		AnnualitaSdDTO annualitaSd = new AnnualitaSdDTO();
		Utils utils = new Utils();
	    if(utils.isLocalMod()){
	    	ambito = AMBIENTE;
	    	annualitaSd = annualitaSdAmbienteDaoImpl.insertAnnualitaSd(dto);
		}else {
				//TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
				LOGGER.debug("[AnnualitaSdDAOImpl::insertAnnualitaSd] verifica ambito");
				AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
				ambito = ambitoDTO.getCodAmbito();
				LOGGER.debug("[AnnualitaSdDAOImpl::insertAnnualitaSd] ambito: " + ambito);
				switch (ambito) {
				  case AMBIENTE:
					LOGGER.debug("[AnnualitaSdDAOImpl::insertAnnualitaSd] ambito: AMBIENTE");
					annualitaSd = annualitaSdAmbienteDaoImpl.insertAnnualitaSd(dto);
				    break;
				  case OPERE_PUBBLICHE:
					//TO DO
				    break;
				  case ATTIVITA_ESTRATTIVE:
					//TO DO
				    break;
				  case TRIBUTI:
					  LOGGER.debug("[AnnualitaSdDAOImpl::insertAnnualitaSd] ambito: TRIBUTI");
					  annualitaSd = annualitaSdTributiDaoImpl.insertAnnualitaSd(dto);
					  break;
				}
			}	   
			LOGGER.debug("[AnnualitaSdDAOImpl::insertAnnualitaSd] END");

		return dto;
	}
	
	@Override
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
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
			bean.setDataInizio(rs.getDate("data_inizio"));
			bean.setIdComponenteDt(rs.getInt("id_componente_dt"));
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
	public AnnualitaSdDTO updateAnnualitaSd(AnnualitaSdDTO dto) throws DataAccessException, SQLException {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOGGER.debug("[AnnualitaSdDAOImpl::updateAnnualitaSd] BEGIN");
		return dto;
	}
	
	@Override
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

	@Override
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
				LOGGER.error("[AnnualitaSdDAOImpl::deleteAnnualitaSd] Errore nell'accesso ai dati", e);
				throw new Exception(Constants.ERRORE_GENERICO);
			} catch (Exception e) {
				LOGGER.error("[AnnualitaSdDAOImpl::deleteAnnualitaSd] Errore nell'accesso ai dati", e);
				throw new Exception(Constants.ERRORE_GENERICO);
			} finally {
				LOGGER.debug("[AnnualitaSdDAOImpl::deleteAnnualitaSd] END");
			}
			return annualitaSdDTO;
		
	}
	
	/**
	 * {@inheritDoc}
	 * @throws SQLException 
	 */
	@Override
	public List<AnnualitaSdDTO> loadAnnualitaSdWorking(Long idStatoDebitorio) throws SQLException {
		LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSdWorking] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);

			return template.query(getQuery(QUERY_SELECT_ANNUALITA_SD_WORKING, null, null), params, getExtendedRowMapper());
			
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSdWorking]Data not found for idStatoDebitorio: "+ idStatoDebitorio);
			return Collections.emptyList();
		} catch (SQLException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSdWorking] Errore nell'esecuzione della query", e);
            throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSdWorking] Errore nell'accesso ai dati", e);
            throw e;
		} finally {
			LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSdWorking] END");
		}
	}
	
	@Override
	public Integer deleteAnnualitaSdWorkingById(Long idAnnualitaSd) throws DAOException {
		LOGGER.debug("[AnnualitaSdDAOImpl::deleteAvvisoAnnualitaWorkingByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAnnualitaSd", idAnnualitaSd);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_ANNUALITA_SD_WORKING_BY_ID, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[AnnualitaSdDAOImpl::deleteAvvisoAnnualitaWorkingByNap] ERROR : " + e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AnnualitaSdDAOImpl::deleteAvvisoAnnualitaWorkingByNap] END");
		}

		return res;
	}
	
	@Override
	public Integer copyAnnualitaSdFromWorkingByStatoDebitorio(Long idStatoDebitorio, String attore) throws DAOException {
		LOGGER.debug("[AnnualitaSdDAOImpl::copyAnnualitaSdFromWorkingByStatoDebitorio] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("gestAttoreIns", attore);
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_INSERT_ANNUALITA_SD_FROM_WORKING, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AnnualitaSdDAOImpl::copyAnnualitaSdFromWorkingByStatoDebitorio] ERROR : " +e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AnnualitaSdDAOImpl::copyAnnualitaSdFromWorkingByStatoDebitorio] END");
		}

		return res;
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return getTableName();
	}

	@Override
	public AnnualitaSdDTO updateDatiTecniciGrandeIdro(Integer idAnnualitaSd, DTGrandeIdroDTO dTGrandeIdroDTO)
			throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		if (idAnnualitaSd == null || dTGrandeIdroDTO == null) {
			throw new BusinessException(
					"ID AnnualitaSd ed i dati tecnici del grande idroelettrico devono essere specificati.");
		}
		AnnualitaSdDTO annualitaSdDTO = null;
		try {
			annualitaSdDTO = loadAnnualitaSdById(idAnnualitaSd);
			ObjectMapper objectMapper = new ObjectMapper();

			// Leggi il JSON come un albero di nodi
			JsonNode rootNode = objectMapper.readTree(annualitaSdDTO.getJsonDtAnnualitaSd());

			DatiTecniciAmbienteDTO datiTecniciAmbiente = null;
			TipoUsoDatoTecnicoDTO tipoUsoGI = null;
			boolean isBollettazione = false;
			if (rootNode.has("riscossione")) {
				datiTecniciAmbiente = Utils.creaDatiTecniciFromJsonDt(annualitaSdDTO.getJsonDtAnnualitaSd(),
						"dati_tecnici");
				tipoUsoGI = datiTecniciAmbiente.getUsi().get(COD_TIPO_USO_GRANDE_IDROELETTRICO);
				isBollettazione = true;
			} else {
				datiTecniciAmbiente = objectMapper.readValue(annualitaSdDTO.getJsonDtAnnualitaSd(),
						DatiTecniciAmbienteDTO.class);
				tipoUsoGI = datiTecniciAmbiente.getUsi().get(COD_TIPO_USO_GRANDE_IDROELETTRICO);
			}
			if (tipoUsoGI != null && datiTecniciAmbiente != null) {
				updateTipoUsoField(tipoUsoGI, dTGrandeIdroDTO);
				Map<String, Object> map = new HashMap<>();
				map.put("idStatoDebitorio", annualitaSdDTO.getIdStatoDebitorio());
				MapSqlParameterSource params = getParameterValue(map);
				Long idRiscossione = template.query(
						StatoDebitorioDAOImpl.QUERY_SELECT_ID_RISCOSSIONE_BY_ID_STATO_DEBITORIO, params,
						new ResultSetExtractor<Long>() {
							@Override
							public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
								Long idRiscossione = null;
								while (rs.next()) {
									idRiscossione = rs.getLong("id_riscossione");
								}
								return idRiscossione;
							}
						});
				String jsonDtAnnualitaSd = isBollettazione ? creaJsonDt(idRiscossione, annualitaSdDTO, datiTecniciAmbiente.toJsonObj()) : datiTecniciAmbiente.toJsonObj().toString() ;
				annualitaSdDTO.setJsonDtAnnualitaSd(jsonDtAnnualitaSd);
				annualitaSdDTO = updateJsonDTAnnualitaSd(annualitaSdDTO);
			}
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception("Errore durante l'aggiornamento dei dati tecnici.", e);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

		return annualitaSdDTO;
	}

	private AnnualitaSdDTO updateJsonDTAnnualitaSd(AnnualitaSdDTO annualitaSdDTO) {
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		Map<String, Object> map = new HashMap<>();
		map.put("idAnnualitaSd", annualitaSdDTO.getIdAnnualitaSd());
		map.put("jsonDtAnnualitaSd", annualitaSdDTO.getJsonDtAnnualitaSd());
		map.put("gestDataUpd", now);
		MapSqlParameterSource params = getParameterValue(map);
		template.update(getQuery(QUERY_UPDATE_JSONDT_SD, null, null), params);
		return annualitaSdDTO;
	}

	private void updateTipoUsoField(TipoUsoDatoTecnicoDTO tipoUso, DTGrandeIdroDTO dTGrandeIdroDTO) {
		tipoUso.setCoeffEnergGrat(dTGrandeIdroDTO.getCoeffEnergGrat());
		tipoUso.setExtraProfitti(dTGrandeIdroDTO.getExtraProfitti());
		tipoUso.setPnmPerEnergGrat(dTGrandeIdroDTO.getPnmPerEnergGrat());
		tipoUso.setPercQuotaVar(dTGrandeIdroDTO.getPercQuotaVar());
		tipoUso.setToTEnergProd(dTGrandeIdroDTO.getToTEnergProd());
		tipoUso.setTotRicaviAnno(dTGrandeIdroDTO.getTotRicaviAnno());
		tipoUso.setPrezzoMedOraPond(dTGrandeIdroDTO.getPrezzoMedOraPond());

	}

	private String creaJsonDt(Long idRiscossione,AnnualitaSdDTO annualitaSdDTO, JSONObject jsonDT) {
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        JSONObject jsonRisc = new JSONObject();
        JSONObject json =  new JSONObject();
		json.put("gest_UID", annualitaSdDTO.getGestUid());
		json.put("id_riscossione", idRiscossione);
		json.put("data_modifica", formatter.format(now));
		json.put("data_inserimento",formatter.format(now));
		json.put("dati_tecnici", jsonDT);
        jsonRisc.put("riscossione", json);
        
        return jsonRisc.toString();
	}

	@Override
	public AnnualitaSdDTO loadAnnualitaSdById(Integer idAnnualitaSd) {
		LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSdById] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAnnualitaSd", idAnnualitaSd);
			MapSqlParameterSource params = getParameterValue(map);
			
			return template.queryForObject(getQuery(QUERY_SELECT_ANNUALITA_SD_BY_ID, null, null), params, getExtendedRowMapper());
			
		} catch (SQLException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSdById] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSdById] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSdById] END");
		}
	}
	
	@Override
	public AnnualitaSdDTO loadAnnualitaSdGrandeIdro(Long idStatoDebitorio) throws SQLException {
		LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSdGrandeIdro] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(QUERY_SELECT_ANNUALITA_SD_GRANDE_IDRO, params, getRowMapper());

		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSdGrandeIdro]Data not found for idStatoDebitorio: "
					+ idStatoDebitorio);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSdGrandeIdro] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaSdDAOImpl::loadAnnualitaSdGrandeIdro] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[AnnualitaSdDAOImpl::loadAnnualitaSd] END");
		}
	}
}
