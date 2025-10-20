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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoDilazioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente.RataSdAmbienteDAOImpl;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper.RecapitoPostelDaoRowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.tributi.RataSdTributiDAOImpl;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RataSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoDilazioneDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

public class RataSdDAOImpl extends RiscaBeSrvGenericDAO<RataSdDTO> implements RataSdDAO {

	@Autowired
	private RataSdAmbienteDAOImpl rataSdAmbienteDAOImpl; 

	@Autowired
	private RataSdTributiDAOImpl rataSdTributiDAOImpl;

	@Autowired
	private TipoDilazioneDAO tipoDilazioneDAO;

	@Autowired
	private AmbitiDAO ambitiDAO;
	@Autowired
	private MessaggiDAO messaggiDAO;
	private static final String AMBIENTE = "AMBIENTE";
	private static final String OPERE_PUBBLICHE = "Opere pubbliche";
	private static final String ATTIVITA_ESTRATTIVE = "Attivita estrattive";
	private static final String TRIBUTI = "Tributi";
	private Boolean devMode;

	public static final String QUERY_SELECT_RATA_SD_BY_ID = "SELECT * FROM risca_r_rata_sd where id_rata_sd = :idRataSd ";
	
	public static final String QUERY_SELECT_RATA_SD_BY_STATO_DEBITORIO = "SELECT * FROM risca_r_rata_sd where id_rata_sd_padre IS NULL "
			+ "AND id_stato_debitorio = :idStatoDebitorio ";

	public static final String QUERY_INSERT = "INSERT INTO risca_r_rata_sd "
			+ "(id_rata_sd, id_stato_debitorio, id_rata_sd_padre, canone_dovuto, interessi_maturati, data_scadenza_pagamento, gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ "VALUES(:idRataSd, :idStatoDebitorio, :idRataSdPadre, :canoneDovuto, :interessiMaturati, :dataScadenzaPagamento, :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid)";

	public static final String QUERY_INSERT_W = "INSERT INTO risca_w_rata_sd "
			+ "(id_rata_sd, id_stato_debitorio, id_rata_sd_padre, canone_dovuto, interessi_maturati, data_scadenza_pagamento) "
			+ "VALUES(:idRataSd, :idStatoDebitorio, :idRataSdPadre, :canoneDovuto, :interessiMaturati, :dataScadenzaPagamento)";

	public static final String QUERY_SELECT_RATA_SD_WORKING_BY_STATO_DEBITORIO = "SELECT * FROM risca_w_rata_sd where id_stato_debitorio = :idStatoDebitorio ";

	public static final String QUERY_UPDATE_RATA_SD_WORKING_COMPENSAZIONE = "update risca_w_rata_sd "
			+ "set canone_dovuto = TRUNC(COALESCE(canone_dovuto, 0) - COALESCE(:compensazione, 0)) "
			+ "where id_stato_debitorio = :idStatoDebitorio";

	public static final String QUERY_SELECT_RATA_SD_BY_STATO_DEBITORIO_FOR_LIST = "SELECT * FROM risca_r_rata_sd where  "
			+ " id_stato_debitorio = :idStatoDebitorio ";

	public static final String QUERY_UPDATE = "update risca_r_rata_sd "
			+ " SET id_stato_debitorio = :idStatoDebitorio, id_rata_sd_padre = :idRataSdPadre , "
			+ " canone_dovuto = :canoneDovuto , interessi_maturati = :interessiMaturati, "
			+ " data_scadenza_pagamento = :dataScadenzaPagamento, "
			+ "  gest_attore_upd = :gestAttoreUpd , gest_data_upd= :gestDataUpd "
			+ " where id_rata_sd = :idRataSd";

	private static final String QUERY_UPDATE_RATA_SD_FROM_WORKING = " update RISCA_R_RATA_SD a "
			+ " set ( data_scadenza_pagamento, canone_dovuto, gest_attore_upd, gest_data_upd) =  "
			+ " ( "
			+ "     select b.data_scadenza_pagamento, b.canone_dovuto, :gestAttoreUpd, to_timestamp(:gestDataUpd, 'YYYY-MM-DD HH24:MI:SS.MS') "
			+ "     from RISCA_W_RATA_SD b "
			+ "     where b.id_stato_debitorio = :idStatoDebitorio "
			+ "     and b.id_rata_sd = a.id_rata_sd "
			+ " ) "
			+ " where EXISTS "
			+ " ( "
			+ "		select 'XXX' "
			+ "		from RISCA_W_RATA_SD b "
			+ "		where b.id_stato_debitorio = :idStatoDebitorio "
			+ "		and b.id_rata_sd = a.id_rata_sd "
			+ " ) ";

	private static final String QUERY_DELETE_RATA_SD_WORKING_BY_SD = " delete from RISCA_W_RATA_SD where id_stato_debitorio = :idStatoDebitorio ";

	public static final String QUERY_DELETE_N_RATA_SD_BY_STATO_DEBITORIO = " delete from risca_r_rata_sd where id_stato_debitorio = :idStatoDebitorio "
			+ " and id_rata_sd_padre is not null ";

	public static final String QUERY_UPDATE_DATA_SCADENZA =" UPDATE risca_r_rata_sd SET data_scadenza_pagamento  = :dataScadenzaPagamento"
			+ " where id_stato_debitorio = :idStatoDebitorio";

	private static final String QUERY_INSERT_RATA_FROM_WORKING = "insert into RISCA_R_RATA_SD ( id_rata_sd, id_stato_debitorio, id_rata_sd_padre,  "
			+ "canone_dovuto, interessi_maturati, data_scadenza_pagamento, "
			+ "gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ "select id_rata_sd, id_stato_debitorio, id_rata_sd_padre,  "
			+ "canone_dovuto, interessi_maturati, data_scadenza_pagamento, "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid "
			+ "from RISCA_W_RATA_SD where id_stato_debitorio = :idStatoDebitorio ";

	public static final String QUERY_SELECT_ID_AMBITO_BY_ID_RATA =
			" SELECT rts.id_ambito FROM risca_t_soggetto rts "
					+ "	WHERE rts.id_soggetto = ( "
					+ "				    SELECT rtsd.id_soggetto "
					+ "				    FROM risca_t_stato_debitorio rtsd "
					+ "				    JOIN risca_r_rata_sd rrrs ON rrrs.id_stato_debitorio = rtsd.id_stato_debitorio "
					+ "				    WHERE rrrs.id_rata_sd =  :idRataSd "
					+ "				)";

	@Override
	public RataSdDTO loadRataSdById(Long idRataSd) {
		LOGGER.debug("[RataSdDAOImpl::loadRataSdById] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRataSd", idRataSd);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(getQuery(QUERY_SELECT_RATA_SD_BY_ID, null, null), params,
					getRowMapper());

		} catch(EmptyResultDataAccessException e) {
			LOGGER.debug("[RataSdDAOImpl::loadRataSdById] No record found in database for idRataSd "+ idRataSd, e);
			return new RataSdDTO();
		} catch (SQLException e) {
			LOGGER.error("[RataSdDAOImpl::loadRataSdById] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RataSdDAOImpl::loadRataSdById] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[RataSdDAOImpl::loadRataSdById] END");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RataSdDTO loadRataSdByStatoDebitorio(Long idStatoDebitorio) {
		LOGGER.debug("[RataSdDAOImpl::loadRataSdByStatoDebitorio] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(getQuery(QUERY_SELECT_RATA_SD_BY_STATO_DEBITORIO, null, null), params,
					getRowMapper());

		} catch(EmptyResultDataAccessException e) {
			LOGGER.debug("[RataSdDAOImpl::loadRataSdByStatoDebitorio] No record found in database for idStatoDebitorio "+ idStatoDebitorio, e);
			return new RataSdDTO();
		} catch (SQLException e) {
			LOGGER.error("[RataSdDAOImpl::loadRataSdByStatoDebitorio] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RataSdDAOImpl::loadRataSdByStatoDebitorio] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[RataSdDAOImpl::loadRataSdByStatoDebitorio] END");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RataSdDTO loadRataSdWorkingByStatoDebitorio(Long idStatoDebitorio) {
		LOGGER.debug("[RataSdDAOImpl::loadRataSdWorkingByStatoDebitorio] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(getQuery(QUERY_SELECT_RATA_SD_WORKING_BY_STATO_DEBITORIO, null, null), params,
					getRowMapper());

		} catch(EmptyResultDataAccessException e) {
			LOGGER.debug("[RataSdDAOImpl::loadRataSdWorkingByStatoDebitorio] No record found in database for idStatoDebitorio "+ idStatoDebitorio, e);
			return new RataSdDTO();
		} catch (SQLException e) {
			LOGGER.error("[RataSdDAOImpl::loadRataSdWorkingByStatoDebitorio] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RataSdDAOImpl::loadRataSdWorkingByStatoDebitorio] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[RataSdDAOImpl::loadRataSdWorkingByStatoDebitorio] END");
		}
	}

	@Override
	public RataSdDTO saveRataSd(RataSdDTO dto) throws DAOException {
		LOGGER.debug("[RataSdDAOImpl::saveRataSd] BEGIN");
		try {
			dto = saveRataSd(dto, true, false);
		} catch (Exception e) {
			LOGGER.error("[RataSdDAOImpl::saveRataSd] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RataSdDAOImpl::saveRataSd] END");
		}

		return dto;
	}

	@Override
	public RataSdDTO saveRataSdWorking(RataSdDTO dto, boolean generateId) throws DAOException {
		LOGGER.debug("[RataSdDAOImpl::saveRataSdWorking] BEGIN");
		try {
			dto = saveRataSd(dto, generateId, true);
		} catch (Exception e) {
			LOGGER.error("[RataSdDAOImpl::saveRataSdWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RataSdDAOImpl::saveRataSdWorking] END");
		}
		return dto;
	}

	private RataSdDTO saveRataSd(RataSdDTO dto, boolean generateId, boolean working) throws SQLException, ParseException {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();

		Long genId = null;
		if(generateId) {
			genId = findNextSequenceValue("seq_risca_r_rata_sd");
			map.put("idRataSd", genId);
		} else {
			map.put("idRataSd", dto.getIdRataSd());
		}
		map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
		map.put("idRataSdPadre", dto.getIdRataSdPadre());
		map.put("canoneDovuto", dto.getCanoneDovuto());
		map.put("interessiMaturati", dto.getInteressiMaturati());


		try {
			SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");
			Date dataScadPag = df.parse(dto.getDataScadenzaPagamento());
			map.put("dataScadenzaPagamento", dataScadPag);
		} catch (ParseException e) {
			LOGGER.error("[RataSdDAOImpl::saveRataSdWorking] Error parsing DataScadenzaPagamento: " + dto.getDataScadenzaPagamento());
			throw new SQLException();
		}

		if(!working) {
			map.put("dataRichiesta", now);
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
		if(generateId) {
			dto.setIdRataSd(genId);
		}
		dto.setGestDataIns(now);
		dto.setGestDataUpd(now);
		return dto;
	}

	@Override
	public Integer updateRataSdWorkingCanoneDovutoCompensazione(Long idStatoDebitorio, BigDecimal compensazione) throws DAOException {
		LOGGER.debug("[RataSdDAOImpl::updateRataSdWorkingCanoneDovutoCompensazione] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("compensazione", compensazione);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_RATA_SD_WORKING_COMPENSAZIONE, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[RataSdDAOImpl::updateRataSdWorkingCanoneDovutoCompensazione] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RataSdDAOImpl::updateRataSdWorkingCanoneDovutoCompensazione] END");
		}

		return res;
	}

	@Override
	public Integer updateRataSdFromRataSdWorking(Long idStatoDebitorio, String attore)
			throws DAOException {
		LOGGER.debug("[RataSdDAOImpl::updateRataSdFromRataSdWorking] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);

			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_UPDATE_RATA_SD_FROM_WORKING;
			return template.update(getQuery(query, null, null), params);

		} catch (Exception e) {
			LOGGER.error("[RataSdDAOImpl::updateRataSdFromRataSdWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RataSdDAOImpl::updateRataSdFromRataSdWorking] END");
		}
	}

	@Override
	public Integer deleteRataSdWorkingByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException {
		LOGGER.debug("[RataSdDAOImpl::deleteRataSdWorkingByIdStatoDebitorio] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_RATA_SD_WORKING_BY_SD, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[RataSdDAOImpl::deleteRataSdWorkingByIdStatoDebitorio] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RataSdDAOImpl::deleteRataSdWorkingByIdStatoDebitorio] END");
		}

		return res;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<RataSdDTO> getRowMapper() throws SQLException {
		return new RataSdRowMapper();
	}

	@Override
	public RowMapper<RataSdDTO> getExtendedRowMapper() throws SQLException {
		return new RataSdRowMapper();
	}

	@Override
	public RowMapper<RataSdExtendedDTO> getExtendedStatoContribuzioneRowMapper() throws SQLException {
		return new RataSdExtendedRowMapper();
	}

	public static class RataSdRowMapper implements RowMapper<RataSdDTO> {

		public RataSdRowMapper() throws SQLException {}

		@Override
		public RataSdDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			RataSdDTO bean = new RataSdDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RataSdDTO bean) throws SQLException {
			bean.setIdRataSd(rs.getLong("id_rata_sd"));
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			if(rs.getLong("id_rata_sd_padre") > 0)
				bean.setIdRataSdPadre(rs.getLong("id_rata_sd_padre"));
			bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
			bean.setInteressiMaturati(rs.getBigDecimal("interessi_maturati"));
			SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
			if(rs.getDate("data_scadenza_pagamento") != null)
				bean.setDataScadenzaPagamento(formatter.format(rs.getDate("data_scadenza_pagamento")));

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


	public static class RataSdExtendedRowMapper implements RowMapper<RataSdExtendedDTO> {

		public RataSdExtendedRowMapper() throws SQLException {}

		@Override
		public RataSdExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			RataSdExtendedDTO bean = new RataSdExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RataSdExtendedDTO bean) throws SQLException {
			bean.setIdRataSd(rs.getLong("id_rata_sd"));
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));		
			bean.setTotVersato(rs.getBigDecimal("tot_versato"));
			bean.setTotaleRestituito(rs.getBigDecimal("totale_restituito"));
			bean.setDataScadenzaPagamento(rs.getString("data_scadenza_pagamento"));
			bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
			bean.setImpSpeseNotifica(rs.getBigDecimal("imp_spese_notifica"));
			bean.setIdStatoContribuzione(Utils.checkLongNull(rs.getLong("id_stato_contribuzione")));
			bean.setFlgAccertamento(rs.getInt("flg_accertamento"));
			bean.setIdAttivitaStatoDebitorio(rs.getLong("id_attivita_stato_deb"));
			bean.setFlgDilazione(rs.getInt("flg_dilazione"));
			bean.setNap(rs.getString("nap"));

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
	public List<RataSdDTO> loadListRataSdByStatoDebitorio(Long idStatoDebitorio) throws DAOException {
		LOGGER.debug("[RataSdDAOImpl::loadRataSdByStatoDebitorio] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);

			return template.query(getQuery(QUERY_SELECT_RATA_SD_BY_STATO_DEBITORIO_FOR_LIST, null, null), params,
					getRowMapper());

		} catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[RataSdDAOImpl::loadRataSdByStatoDebitorio] No record found in database for idStatoDebitorio "+ idStatoDebitorio, e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error("[RataSdDAOImpl::loadRataSdByStatoDebitorio] Errore nell'esecuzione della query", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} catch (DataAccessException e) {
			LOGGER.error("[RataSdDAOImpl::loadRataSdByStatoDebitorio] Errore nell'accesso ai dati", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RataSdDAOImpl::loadRataSdByStatoDebitorio] END");
		}
	}


	@Override
	public List<Integer> saveNRataSd(StatoDebitorioExtendedDTO statoDebitorio, Long idAmbito) throws DAOException, BusinessException, SystemException {
		LOGGER.debug("[RataSdDAOImpl::saveNRataSd] BEGIN");
		String ambito = "";
		List<Integer> id_rata = new ArrayList<Integer>();
		TipoDilazioneDTO tipoDilazione = new TipoDilazioneDTO();


		Utils utils = new Utils();
		if(utils.isLocalMod()){
			ambito = AMBIENTE;
			tipoDilazione = tipoDilazioneDAO.loadTipoDilazioneByIdAmbito(1L);
			if(tipoDilazione == null)
				throw new  BusinessException(401, "E087","Attenzione non esiste il tipo dilazione");
			id_rata = rataSdAmbienteDAOImpl.saveNRataSd(tipoDilazione, statoDebitorio);

		}else {
			//TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			ambito = ambitoDTO.getCodAmbito();
			tipoDilazione = tipoDilazioneDAO.loadTipoDilazioneByIdAmbito(ambitoDTO.getIdAmbito());
			if(tipoDilazione == null)
				throw new  BusinessException(401, "E087","Attenzione non esiste il tipo dilazione");
			switch (ambito) {
			case AMBIENTE:
				id_rata = rataSdAmbienteDAOImpl.saveNRataSd(tipoDilazione, statoDebitorio);
				break;
			case OPERE_PUBBLICHE:
				//TO DO
				break;
			case ATTIVITA_ESTRATTIVE:
				//TO DO
				break;
			case TRIBUTI:
				id_rata = rataSdTributiDAOImpl.saveNRataSd(tipoDilazione, statoDebitorio);
				break;
			}
		}	   
		LOGGER.debug("[RataSdDAOImpl::saveNRataSd] END");
		return id_rata;
	}

	private boolean isDevMode() {
		if (devMode != null) {
			return devMode;
		} else {
			return false;
		}
	}

	@Override
	public void updateRataSd(RataSdDTO dto) throws Exception {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		try {
			map.put("idRataSd", dto.getIdRataSd());

			map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
			map.put("idRataSdPadre", dto.getIdRataSdPadre());
			map.put("canoneDovuto", dto.getCanoneDovuto());
			map.put("interessiMaturati", dto.getInteressiMaturati());

			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dataScadPag = df.parse(dto.getDataScadenzaPagamento());
				map.put("dataScadenzaPagamento", dataScadPag);
			} catch (ParseException e) {
				LOGGER.error("[RataSdDAOImpl::updateRataSd] Error parsing DataScadenzaPagamento: "
						+ dto.getDataScadenzaPagamento());
				throw new SQLException();
			}

			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_UPDATE;
			template.update(getQuery(query, null, null), params, keyHolder);
		} catch (DataAccessException e) {
			LOGGER.error("[RataSdAmbienteDAOImpl::updateRataSd] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}
	}


	@Override
	public List<Integer> updateNRataSd(StatoDebitorioExtendedDTO statoDebitorio, Long idAmbito) throws DAOException,  BusinessException, SystemException {
		LOGGER.debug("[RataSdDAOImpl::saveNRataSd] BEGIN");
		String ambito = "";
		List<Integer> id_rata = new ArrayList<Integer>();
		TipoDilazioneDTO tipoDilazione = new TipoDilazioneDTO();


		Utils utils = new Utils();
		if(utils.isLocalMod()){
			ambito = AMBIENTE;
			tipoDilazione = tipoDilazioneDAO.loadTipoDilazioneByIdAmbito(1L);
			if(tipoDilazione == null)
				throw new  BusinessException(401, "E087","Attenzione non esiste il tipo dilazione");
			id_rata = rataSdAmbienteDAOImpl.updateNRataSd(tipoDilazione,statoDebitorio);

		}else {
			//TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			ambito = ambitoDTO.getCodAmbito();
			tipoDilazione = tipoDilazioneDAO.loadTipoDilazioneByIdAmbito(ambitoDTO.getIdAmbito());
			if(tipoDilazione == null)
				throw new  BusinessException(401, "E087","Attenzione non esiste il tipo dilazione");
			switch (ambito) {
			case AMBIENTE:
				id_rata = rataSdAmbienteDAOImpl.updateNRataSd(tipoDilazione, statoDebitorio);
				break;
			case OPERE_PUBBLICHE:
				//TO DO
				break;
			case ATTIVITA_ESTRATTIVE:
				//TO DO
				break;
			case TRIBUTI:
				id_rata = rataSdTributiDAOImpl.updateNRataSd(tipoDilazione, statoDebitorio);
				break;
			}
		}	   
		LOGGER.debug("[RataSdDAOImpl::saveNRataSd] END");
		return id_rata;
	}

	@Override
	public void deleteNRataSd(Long idStatoDebitorio, Long idAmbito)
			throws DAOException, BusinessException {
		LOGGER.debug("[RataSdDAOImpl::deleteNRataSd] BEGIN");
		String ambito = "";


		Utils utils = new Utils();
		if(utils.isLocalMod()){
			ambito = AMBIENTE;
			rataSdAmbienteDAOImpl.deleteNRataSd(idStatoDebitorio);

		}else {
			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			ambito = ambitoDTO.getCodAmbito();
			switch (ambito) {
			case AMBIENTE:
				rataSdAmbienteDAOImpl.deleteNRataSd(idStatoDebitorio);
				break;
			case OPERE_PUBBLICHE:
				//TO DO
				break;
			case ATTIVITA_ESTRATTIVE:
				//TO DO
				break;
			case TRIBUTI:
				rataSdTributiDAOImpl.deleteNRataSd(idStatoDebitorio);
				break;
			}
		}	   
		LOGGER.debug("[RataSdDAOImpl::deleteNRataSd] END");

	}

	@Override
	public void updateDataScadenzaByIdSD(Long idStatoDebitorio, Date dataScadPag) throws DAOException {
		LOGGER.debug("[RataSdDAOImpl::updateDataScadenzaByIdSD] BEGIN");
		Map<String, Object> map = new HashMap<>();
		try {
			map.put("dataScadenzaPagamento", dataScadPag);
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_UPDATE_DATA_SCADENZA;
			template.update(getQuery(query, null, null), params, keyHolder);
		} catch (DataAccessException e) {
			LOGGER.error("[RataSdDAOImpl::updateDataScadenzaByIdSD] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		}
	}

	@Override
	public Integer copyRataSdFromWorkingByStatoDebitorio(Long idStatoDebitorio, String attore) throws DAOException {
		LOGGER.debug("[RataSdDAOImpl::copyRataSdFromWorkingByStatoDebitorio] BEGIN");
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
			res = template.update(getQuery(QUERY_INSERT_RATA_FROM_WORKING, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[RataSdDAOImpl::copyRataSdFromWorkingByStatoDebitorio] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RataSdDAOImpl::copyRataSdFromWorkingByStatoDebitorio] END");
		}

		return res;
	}

	@Override
	public Long getIdAmbitoByIdRata(Long idRataSd) throws DAOException {
		Map<String, Object> map = new HashMap<>();
		map.put("idRataSd", idRataSd);
		MapSqlParameterSource params = getParameterValue(map);
		Long idAmbito =   template.query(QUERY_SELECT_ID_AMBITO_BY_ID_RATA, params, new ResultSetExtractor<Long>(){
			public Long extractData(ResultSet rs) throws SQLException,DataAccessException {
				Long idAmbito = null;
				while(rs.next()){
					idAmbito = rs.getLong("id_ambito");
				}
				return idAmbito;
			}
		}); 
		return idAmbito; 
	}

	@Override
	public void updateRataSdSetInteressiMaturati(List<Long> listIdStatoDebitorio, String fruitore) throws DAOException {
		LOGGER.debug("[RataSdDAOImpl::updateRataSdSetInteressiMaturati] BEGIN");
		if (listIdStatoDebitorio == null || listIdStatoDebitorio.size() == 0) return;

		try
		{
			StringBuilder queryUpdate = new StringBuilder();
			queryUpdate.append(" update risca_r_rata_sd  ");
			queryUpdate.append(" SET ");
			queryUpdate.append(" interessi_maturati = NULL, gest_data_upd = CURRENT_TIMESTAMP , gest_attore_upd = :gestAttoreUpd ");
			queryUpdate.append("  where id_rata_sd  in( ");
			queryUpdate.append(" select id_rata_sd from risca_r_rata_sd,risca_t_stato_debitorio sd  ");
			queryUpdate.append(" where sd.id_stato_debitorio = risca_r_rata_sd.id_stato_debitorio  ");
			queryUpdate.append(" and risca_r_rata_sd.id_stato_debitorio in(:listIdStatoDebitorio))");


			LOGGER.debug("[RataSdDAOImpl::updateDettaglioPagSetInteressiMaturati] queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("listIdStatoDebitorio", listIdStatoDebitorio);
			paramMap.put("gestAttoreUpd", fruitore);

			template.update(queryUpdate.toString(), paramMap);  

			LOGGER.debug("[RataSdDAOImpl::updateDettaglioPagSetInteressiMaturati]  Inserimento effettuato. Stato = SUCCESS ");
		}
		catch (DataIntegrityViolationException ex)
		{
			LOGGER.debug("[RataSdDAOImpl::updateDettaglioPagSetInteressiMaturati]  Integrity Keys Violation");
			throw new DAOException(ex.getMessage());
		}
		catch (Exception e) {
			LOGGER.error("[RataSdDAOImpl::updateDettaglioPagSetInteressiMaturati] ERROR : " +e);
			throw new DAOException(e.getMessage());
		}  finally {
			LOGGER.debug("[RataSdDAOImpl::updateDettaglioPagSetInteressiMaturati] END");
		} 

	}


	@Override
	public List<RataSdExtendedDTO> findRateConTotaleVersatoRestituito(List<Long> listIdStatoDebitorio) throws DAOException, SystemException
	{
		LOGGER.debug("[RataSdDAOImpl::findRateConTotaleVersatoRestituito] BEGIN");
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();

		sql.append("  select ");
		sql.append("		pag.id_rata_sd, ");
		sql.append("		coalesce (pag.tot_versato,0) as tot_versato, ");
		sql.append("		std.id_stato_debitorio, ");
		sql.append("		coalesce (rim.totale_restituito,0) as totale_restituito, ");
		sql.append("		pag.data_scadenza_pagamento, ");
		sql.append("		coalesce (pag.canone_dovuto,0) as canone_dovuto,  ");
		sql.append("		coalesce (std.imp_spese_notifica,0)as imp_spese_notifica, ");
		sql.append("        std.id_stato_contribuzione,  ");
		sql.append("        coalesce (acc.flg_accertamento,0 )as flg_accertamento, ");
		sql.append("        std.id_attivita_stato_deb , ");
		sql.append("        coalesce (std.flg_dilazione,0) as  flg_dilazione, ");
		sql.append("        std.nap  ");
		sql.append("	from ");
		sql.append("		risca_t_stato_debitorio std ");
		sql.append("	left join ( ");
		sql.append("		select ");
		sql.append("			rat.id_rata_sd, ");
		sql.append("			rat.id_stato_debitorio, ");
		sql.append("			det.tot_versato, ");
		sql.append("			rat.data_scadenza_pagamento , ");
		sql.append("			rat.canone_dovuto ");
		sql.append("		from ");
		sql.append("			risca_r_rata_sd rat ");
		sql.append("		left join ( ");
		sql.append("			select ");
		sql.append("				ddd.id_rata_sd, ");
		sql.append("				SUM(ddd.importo_versato) as tot_versato ");
		sql.append("			from ");
		sql.append("				risca_r_dettaglio_pag ddd ");
		sql.append("			group by ");
		sql.append("				ddd.id_rata_sd ) det on ");
		sql.append("			rat.id_rata_sd = det.id_rata_sd ");
		sql.append("		where ");
		sql.append("			rat.id_rata_sd_padre is null ) pag on ");
		sql.append("		std.id_stato_debitorio = pag.id_stato_debitorio ");
		sql.append("	left join ( ");
		sql.append("		select ");
		sql.append("			id_stato_debitorio, ");
		sql.append("			SUM(lis.restituito) as totale_restituito ");
		sql.append("		from ");
		sql.append("			( ");
		sql.append("			select ");
		sql.append("				rim.id_rimborso, ");
		sql.append("				rim.id_stato_debitorio, ");
		sql.append("				case ");
		sql.append("				when rim.id_tipo_rimborso = 1 then rim.imp_rimborso ");
		sql.append("				else rim.imp_restituito ");
		sql.append("			end as restituito ");
		sql.append("		from ");
		sql.append("			risca_r_rimborso rim ) lis ");
		sql.append("	group by ");
		sql.append("		id_stato_debitorio ) rim on ");
		sql.append("	std.id_stato_debitorio = rim.id_stato_debitorio ");
		sql.append("left join ( ");
		sql.append("		select ");
		sql.append("			id_stato_debitorio, ");
		sql.append("			1 as flg_accertamento ");
		sql.append("		from ");
		sql.append("	     risca_t_accertamento  ");
		sql.append("		group by ");
		sql.append("			id_stato_debitorio ) acc on ");
		sql.append("		std.id_stato_debitorio = acc.id_stato_debitorio ");



		if(listIdStatoDebitorio != null) {
			sql.append("where ");
			sql.append(" pag.id_stato_debitorio in (:listIdStatoDebitorio) ");
		}

		sql.append("order by pag.id_stato_debitorio ");

		if(listIdStatoDebitorio != null) {
			paramMap.addValue("listIdStatoDebitorio", listIdStatoDebitorio);
		}

		LOGGER.debug("[RataSdDAOImpl - findRateConTotaleVersatoRestituito] query =" + sql.toString());

		List<RataSdExtendedDTO> rataSdList = null;
		try
		{
			rataSdList = template.query(sql.toString(), paramMap, getExtendedStatoContribuzioneRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[RataSdDAOImpl::findRateConTotaleVersatoRestituito]  NESSUN RISULTATO");
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[RataSdDAOImpl::findRateConTotaleVersatoRestituito] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[RataSdDAOImpl::findRateConTotaleVersatoRestituito] END");
		}
		return (List<RataSdExtendedDTO>) rataSdList;
	}


	@Override
	public String findDataScadenzaPagamento(Long idStatoDebitorio) throws DAOException, SystemException
	{
		LOGGER.debug("[RataSdDAOImpl::findDataScadenzaPagamento] BEGIN");
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();		

//		sql.append(" select distinct	CASE WHEN	 (first_value(data_scadenza_pagamento) over(partition by id_stato_debitorio ");
//		sql.append(" order by data_scadenza_pagamento desc)) is null then  ");
//		sql.append(" (select  rat.data_scadenza_pagamento from risca_r_rata_sd rat left join ( ");
//		sql.append(" select ddd.id_rata_sd, SUM(ddd.importo_versato) as tot_versato ");
//		sql.append(" from risca_r_dettaglio_pag ddd ");
//		sql.append(" group by ddd.id_rata_sd ) det  on rat.id_rata_sd = det.id_rata_sd   ");
//		sql.append(" where rat.id_rata_sd_padre is null ");
//		sql.append("  and rat.id_stato_debitorio = :id_stato_debitorio ");
//		sql.append(" ) else  (first_value(data_scadenza_pagamento) over(partition by id_stato_debitorio ");
//		sql.append(" order by data_scadenza_pagamento desc)) end as data_scadenza_pagamento from ");
//		sql.append("  risca_r_rata_sd	where id_stato_debitorio = :id_stato_debitorio ");
		
		//Rivista parte di analisi su data scadenza pagamento per dilazione
		sql.append(" select distinct	 ");
		sql.append(" (select  rat.data_scadenza_pagamento from risca_r_rata_sd rat left join ( ");
		sql.append(" select ddd.id_rata_sd, SUM(ddd.importo_versato) as tot_versato ");
		sql.append(" from risca_r_dettaglio_pag ddd ");
		sql.append(" group by ddd.id_rata_sd ) det  on rat.id_rata_sd = det.id_rata_sd   ");
		sql.append(" where rat.id_rata_sd_padre is null ");
		sql.append("  and rat.id_stato_debitorio = :id_stato_debitorio ");
		sql.append(" )data_scadenza_pagamento  ");
		sql.append(" from ");
		sql.append("  risca_r_rata_sd	where id_stato_debitorio = :id_stato_debitorio ");

		paramMap.addValue("id_stato_debitorio", idStatoDebitorio);


		LOGGER.debug("[RataSdDAOImpl - findDataScadenzaPagamento] query =" + sql.toString());

		String dataScadenzaPagamento = null;
		try
		{
			dataScadenzaPagamento = template.queryForObject(sql.toString(), paramMap, String.class);
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[RataSdDAOImpl::findDataScadenzaPagamento]  NESSUN RISULTATO");
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[RataSdDAOImpl::findDataScadenzaPagamento] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[RataSdDAOImpl::findDataScadenzaPagamento] END");
		}
		return dataScadenzaPagamento;
	}
	
	@Override
	public String findDataScadenzaPagamentoPerDilazione(Long idStatoDebitorio) throws DAOException, SystemException
	{
		LOGGER.debug("[RataSdDAOImpl::findDataScadenzaPagamentoPerDilazione] BEGIN");
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();		

		sql.append(" select distinct	CASE WHEN	 (first_value(data_scadenza_pagamento) over(partition by id_stato_debitorio ");
		sql.append(" order by data_scadenza_pagamento desc)) is null then  ");
		sql.append(" (select  rat.data_scadenza_pagamento from risca_r_rata_sd rat left join ( ");
		sql.append(" select ddd.id_rata_sd, SUM(ddd.importo_versato) as tot_versato ");
		sql.append(" from risca_r_dettaglio_pag ddd ");
		sql.append(" group by ddd.id_rata_sd ) det  on rat.id_rata_sd = det.id_rata_sd   ");
		sql.append(" where rat.id_rata_sd_padre is null ");
		sql.append("  and rat.id_stato_debitorio = :id_stato_debitorio ");
		sql.append(" ) else  (first_value(data_scadenza_pagamento) over(partition by id_stato_debitorio ");
		sql.append(" order by data_scadenza_pagamento desc)) end as data_scadenza_pagamento from ");
		sql.append("  risca_r_rata_sd	where id_stato_debitorio = :id_stato_debitorio ");

		paramMap.addValue("id_stato_debitorio", idStatoDebitorio);


		LOGGER.debug("[RataSdDAOImpl - findDataScadenzaPagamentoPerDilazione] query =" + sql.toString());

		String dataScadenzaPagamentoPerDilazione = null;
		try
		{
			dataScadenzaPagamentoPerDilazione = template.queryForObject(sql.toString(), paramMap, String.class);
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[RataSdDAOImpl::findDataScadenzaPagamentoPerDilazione]  NESSUN RISULTATO");
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[RataSdDAOImpl::findDataScadenzaPagamentoPerDilazione] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[RataSdDAOImpl::findDataScadenzaPagamentoPerDilazione] END");
		}
		return dataScadenzaPagamentoPerDilazione;
	}
	

	@Override
	public void updateRataSdSetInteressiMaturatiByIdRata(BigDecimal interessiTotaliCalcolati, Long idRataSd, String fruitore)
			throws DAOException {
		LOGGER.debug("[RataSdDAOImpl::updateRataSdSetInteressiMaturatiByIdRata] BEGIN");

		try
		{
			StringBuilder queryUpdate = new StringBuilder();
			queryUpdate.append(" update risca_r_rata_sd  ");
			queryUpdate.append(" SET ");
			queryUpdate.append(" interessi_maturati = :interessiMaturati , gest_data_upd = CURRENT_TIMESTAMP , gest_attore_upd = :gestAttoreUpd ");
			queryUpdate.append("  where id_rata_sd  = :idRataSd ");


			LOGGER.debug("[RataSdDAOImpl::updateDettaglioPagSetInteressiMaturati] queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("interessiMaturati", interessiTotaliCalcolati);
			paramMap.put("idRataSd", idRataSd);
			paramMap.put("gestAttoreUpd", fruitore);

			template.update(queryUpdate.toString(), paramMap);  

			LOGGER.debug("[RataSdDAOImpl::updateDettaglioPagSetInteressiMaturati]  Inserimento effettuato. Stato = SUCCESS ");
		}
		catch (DataIntegrityViolationException ex)
		{
			LOGGER.debug("[RataSdDAOImpl::updateRataSdSetInteressiMaturatiByIdRata]  Integrity Keys Violation");
			throw new DAOException(ex.getMessage());
		}
		catch (Exception e) {
			LOGGER.error("[RataSdDAOImpl::updateRataSdSetInteressiMaturatiByIdRata] ERROR : " +e);
			throw new DAOException(e.getMessage());
		}  finally {
			LOGGER.debug("[RataSdDAOImpl::updateRataSdSetInteressiMaturatiByIdRata] END");
		} 
		
	}
	
	@Override
	public List<RataSdExtendedDTO> findRateByIdStatoDebitorioForDilazione(Long idStatoDebitorio) throws DAOException, SystemException
	{
		LOGGER.debug("[RataSdDAOImpl::findRateByIdStatoDebitorioForDilazione] BEGIN");
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();

		sql.append("   select id_rata_sd, id_stato_debitorio, coalesce (canone_dovuto,0) as canone_dovuto, interessi_maturati, data_scadenza_pagamento, id_rata_sd_padre, ");
		sql.append(" ROW_NUMBER() OVER(ORDER BY data_scadenza_pagamento)as progr_rata ");
		sql.append("   from ");
		sql.append("   risca_r_rata_sd ");	
		sql.append("	where ");
		sql.append("	id_stato_debitorio = :ID_STATO_DEBITORIO ");
		sql.append("	and  id_rata_sd_padre IS NOT NULL ");
		sql.append("	order by data_scadenza_pagamento ");

        paramMap.addValue("ID_STATO_DEBITORIO", idStatoDebitorio);

		LOGGER.debug("[RataSdDAOImpl - findRateByIdStatoDebitorioForDilazione] query =" + sql.toString());

		List<RataSdExtendedDTO> rataSdList = null;
		try
		{
			rataSdList = template.query(sql.toString(), paramMap, new RataExtendedForDilazioneRowMapper() );
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[RataSdDAOImpl::findRateByIdStatoDebitorioForDilazione]  NESSUN RISULTATO");
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[RataSdDAOImpl::findRateByIdStatoDebitorioForDilazione] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[RataSdDAOImpl::findRateByIdStatoDebitorioForDilazione] END");
		}
		return (List<RataSdExtendedDTO>) rataSdList;
	}
	
	public static class RataExtendedForDilazioneRowMapper implements RowMapper<RataSdExtendedDTO> {

		public RataExtendedForDilazioneRowMapper() throws SQLException {}

		@Override
		public RataSdExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			RataSdExtendedDTO bean = new RataSdExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RataSdExtendedDTO bean) throws SQLException {
			bean.setIdRataSd(rs.getLong("id_rata_sd"));
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
			bean.setInteressiMaturati(rs.getBigDecimal("interessi_maturati"));
			bean.setDataScadenzaPagamento(rs.getString("data_scadenza_pagamento"));
			bean.setIdRataSdPadre(rs.getLong("id_rata_sd_padre"));
			bean.setProgrRata(rs.getInt("progr_rata"));
			
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
	public BigDecimal findSumInteressiMaturatiByIdRataPadreAndIdStatoDebitorio(Long idRataSdPadre,Long idStatoDebitorio) throws DAOException, SystemException
	{
		LOGGER.debug("[RataSdDAOImpl::findSumInteressiMaturatiByIdRataPadreAndIdStatoDebitorio] BEGIN");
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();		

		sql.append(" select sum ( coalesce (interessi_maturati,0)) as interessi_maturati ");
		sql.append(" from RISCA_R_RATA_SD  ");
		sql.append(" where  id_rata_sd_padre = :ID_RATA_SD_PADRE ");
		sql.append(" and  id_stato_debitorio  = :ID_STATO_DEBITORIO ");

		paramMap.addValue("ID_RATA_SD_PADRE", idRataSdPadre);
		paramMap.addValue("ID_STATO_DEBITORIO", idStatoDebitorio);


		LOGGER.debug("[RataSdDAOImpl - findSumInteressiMaturatiByIdRataPadreAndIdStatoDebitorio] query =" + sql.toString());

		BigDecimal interessiMaturati = null;
		try
		{
			interessiMaturati = template.queryForObject(sql.toString(), paramMap, BigDecimal.class);
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[RataSdDAOImpl::findSumInteressiMaturatiByIdRataPadreAndIdStatoDebitorio]  NESSUN RISULTATO");
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[RataSdDAOImpl::findSumInteressiMaturatiByIdRataPadreAndIdStatoDebitorio] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[RataSdDAOImpl::findSumInteressiMaturatiByIdRataPadreAndIdStatoDebitorio] END");
		}
		return interessiMaturati;
	}
	
	@Override
	public Long findAnnoDataScadPagamentoByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException, SystemException
	{
		LOGGER.debug("[RataSdDAOImpl::findAnnoDataScadPagamentoByIdStatoDebitorio] BEGIN");
		LOGGER.debug("[RataSdDAOImpl::findAnnoDataScadPagamentoByIdStatoDebitorio] idStatoDebitorio: "+idStatoDebitorio);
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();		
		
		sql.append(" select  EXTRACT(year from rrrs.data_scadenza_pagamento) as anno  ");
		sql.append(" from risca_r_rata_sd rrrs  ");
		sql.append(" where rrrs.id_stato_debitorio = :ID_STATO_DEBITORIO and id_rata_sd_padre is null ");

		paramMap.addValue("ID_STATO_DEBITORIO", idStatoDebitorio);

		LOGGER.debug("[RataSdDAOImpl - findAnnoDataScadPagamentoByIdStatoDebitorio] query =" + sql.toString());

		Long anno = null;
		try
		{
			anno = template.queryForObject(sql.toString(), paramMap, Long.class);
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[RataSdDAOImpl::findAnnoDataScadPagamentoByIdStatoDebitorio]  NESSUN RISULTATO");
			throw new DAOException("Nessuna data scadenza pagamentro trovata in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[RataSdDAOImpl::findAnnoDataScadPagamentoByIdStatoDebitorio] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[RataSdDAOImpl::findAnnoDataScadPagamentoByIdStatoDebitorio] END");
		}
		return anno;
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}
}
