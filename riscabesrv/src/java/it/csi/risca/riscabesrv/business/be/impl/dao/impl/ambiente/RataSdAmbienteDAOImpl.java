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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.RataSdDAOImpl;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoDilazioneDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class RataSdAmbienteDAOImpl extends RiscaBeSrvGenericDAO<RataSdDTO>  {

	public static final String QUERY_SELECT_RATA_SD_BY_STATO_DEBITORIO = "SELECT * FROM risca_r_rata_sd where id_rata_sd_padre IS NULL "
			+ "AND id_stato_debitorio = :idStatoDebitorio ";
	
	public static final String QUERY_SELECT_RATA_SD_BY_STATO_DEBITORIO_AND_ID_RATA_PADRE = "SELECT * FROM risca_r_rata_sd where id_rata_sd_padre = :idRataSdPadre  "
			+ "AND id_stato_debitorio = :idStatoDebitorio ";
	
	public static final String QUERY_INSERT = "INSERT INTO risca_r_rata_sd "
			+ "(id_rata_sd, id_stato_debitorio, id_rata_sd_padre, canone_dovuto, interessi_maturati, data_scadenza_pagamento, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(nextval('seq_risca_r_rata_sd'), :idStatoDebitorio, :idRataSdPadre, :canoneDovuto, :interessiMaturati, :dataScadenzaPagamento, :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";
	
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
			+ " gest_attore_upd = :gestAttoreUpd , gest_data_upd= :gestDataUpd "
			+ " where id_rata_sd = :idRataSd";
	
    @Autowired
    private MessaggiDAO messaggiDAO;
	
	public List<Integer> saveNRataSd(TipoDilazioneDTO tipoDilazione, StatoDebitorioExtendedDTO statoDebitorio) throws DAOException, BusinessException, SystemException {
		List<Integer> id_rata = new ArrayList<Integer>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		int numMesi = 1;
		int numRate = 1;
		try {
		//QUERY PER RECUPERARE LA DATA TRAMITE ID STATO DEBITORIO
			if(tipoDilazione != null) {
				  numMesi = tipoDilazione.getNumMesi();
				  numRate = tipoDilazione.getNumRate();
				}
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", statoDebitorio.getIdStatoDebitorio());
			MapSqlParameterSource params = getParameterValue(map);
			RataSdDTO rataSdPadre = template.queryForObject(QUERY_SELECT_RATA_SD_BY_STATO_DEBITORIO, params, getRowMapper());
			if(rataSdPadre.getCanoneDovuto().compareTo(tipoDilazione.getImportoMagg()) > 0  || tipoDilazione.getImportoMin().compareTo( rataSdPadre.getCanoneDovuto()) > 0 
					|| statoDebitorio.getAnnualitaSd().size() <=  tipoDilazione.getNumAnnualitaMagg()
					) {
				messaggiDAO.loadMessaggiByCodMessaggio("E083");
				throw new  BusinessException(401, "E083","Attenzione, dilazione non applicabile");
			}
			//FARE CICLO FOR IN BASE AL NUMERO RATE
			// - impostare il nuovo oggetto
			// - salvarlo	
			if(rataSdPadre != null) {
				//BigDecimal canoneDovutoRateizzato = rataSdPadre.getCanoneDovuto().divide(BigDecimal.valueOf(numRate)).setScale(2, RoundingMode.HALF_UP);;
				map.put("idRataSdPadre", rataSdPadre.getIdRataSd());
				//map.put("canoneDovuto", canoneDovutoRateizzato);
				map.put("interessiMaturati", null);
				map.put("gestAttoreIns", rataSdPadre.getGestAttoreIns());
				map.put("gestDataIns", now);
				map.put("gestAttoreUpd", rataSdPadre.getGestAttoreUpd());
				map.put("gestDataUpd", now);
				map.put("gestUid", generateGestUID(rataSdPadre.getGestAttoreIns() + rataSdPadre.getGestAttoreUpd() + now));
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
				for(int i=0; i<numRate; i++) {
					if(rataSdPadre.getDataScadenzaPagamento() != null) {
						if(i == 0) {
							String dataScadenzaPagamento = rataSdPadre.getDataScadenzaPagamento();
							Date dataScadPag;
							try {
								dataScadPag = form.parse(dataScadenzaPagamento);
								map.put("dataScadenzaPagamento", dataScadPag);
							} catch (ParseException e) {
								LOGGER.error("[RataSdAmbienteDAOImpl::saveNRataSd] Errore parse data prima rata ", e);
							}
							
						}
							
						else {
							  //convert String to LocalDate
							  LocalDate localDate = LocalDate.parse(rataSdPadre.getDataScadenzaPagamento(), formatter);
							  LocalDate newDate = localDate.plusMonths((long) numMesi * i);
							  String dsp = newDate.toString();
							  Date dspagamento;
							  try {
								dspagamento = form.parse(dsp);
								map.put("dataScadenzaPagamento", dspagamento);
							} catch (ParseException e) {
								LOGGER.error("[RataSdAmbienteDAOImpl::saveNRataSd] Errore parse data rate successive ", e);
							}
							  

						}
					}

					if(i == 0) {
						BigDecimal canoneDovutoRateizzato = rataSdPadre.getCanoneDovuto().divide(BigDecimal.valueOf(numRate)).setScale(2, RoundingMode.HALF_UP);;
						map.put("canoneDovuto", canoneDovutoRateizzato);
					}
					else {
						BigDecimal canoneDovutoRateizzato = rataSdPadre.getCanoneDovuto().divide(BigDecimal.valueOf(numRate)).setScale(2, RoundingMode.HALF_DOWN);;
						map.put("canoneDovuto", canoneDovutoRateizzato);
					}
					
					KeyHolder keyHolder = new GeneratedKeyHolder();

					params = getParameterValue(map);
					Integer id = template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
					id_rata.add(id);
				}
			}

		} catch (DataAccessException e) {
			LOGGER.error("[RataSdAmbienteDAOImpl::saveNRataSd] Errore nell'accesso ai dati",
					e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error(
					"[RataSdAmbienteDAOImpl::saveNRataSd] Errore nell'esecuzione della query",
					e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		}
		

		return id_rata;
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


	public List<Integer> updateNRataSd(TipoDilazioneDTO tipoDilazione, StatoDebitorioExtendedDTO statoDebitorio) throws DAOException, BusinessException, SystemException {
		List<Integer> id_rata = new ArrayList<Integer>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		int numMesi = 1;
		int numRate = 1;
		try {
		//QUERY PER RECUPERARE LA DATA TRAMITE ID STATO DEBITORIO
			if(tipoDilazione != null) {
				  numMesi = tipoDilazione.getNumMesi();
				  numRate = tipoDilazione.getNumRate();
				}
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", statoDebitorio.getIdStatoDebitorio());
			MapSqlParameterSource params = getParameterValue(map);
			RataSdDTO rataSdPadre = template.queryForObject(QUERY_SELECT_RATA_SD_BY_STATO_DEBITORIO, params, getRowMapper());
			if(rataSdPadre.getCanoneDovuto().compareTo(tipoDilazione.getImportoMagg()) > 0  || tipoDilazione.getImportoMin().compareTo( rataSdPadre.getCanoneDovuto()) > 0
				|| statoDebitorio.getAnnualitaSd().size() <= tipoDilazione.getNumAnnualitaMagg()
				) {
				messaggiDAO.loadMessaggiByCodMessaggio("E083");
				throw new  BusinessException(401, "E083","Attenzione, dilazione non applicabile");
			}
		
			if(rataSdPadre != null) {
//				BigDecimal canoneDovutoRateizzato = rataSdPadre.getCanoneDovuto().divide(BigDecimal.valueOf(numRate)).setScale(2, RoundingMode.HALF_UP);;
				
				map.put("idRataSdPadre", rataSdPadre.getIdRataSd());
//				map.put("canoneDovuto", canoneDovutoRateizzato);
				map.put("interessiMaturati", null);
				map.put("gestAttoreUpd", rataSdPadre.getGestAttoreUpd());
				map.put("gestDataUpd", now);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			    params = getParameterValue(map);
				List<RataSdDTO> rateFigli = template.query(QUERY_SELECT_RATA_SD_BY_STATO_DEBITORIO_AND_ID_RATA_PADRE, params, getRowMapper());
				
				for(int i=0; i<numRate; i++) {
					map.put("idRataSd", rateFigli.get(i).getIdRataSd());
					if(rataSdPadre.getDataScadenzaPagamento() != null) {
						if(i == 0) {
							String dataScadenzaPagamento = rataSdPadre.getDataScadenzaPagamento();
							Date dataScadPag;
							try {
								dataScadPag = form.parse(dataScadenzaPagamento);
								map.put("dataScadenzaPagamento", dataScadPag);
							} catch (ParseException e) {
								LOGGER.error("[RataSdAmbienteDAOImpl::updateNRataSd] Errore parse data prima rata ", e);
							}
							
						}
							
						else {
							  //convert String to LocalDate
							  LocalDate localDate = LocalDate.parse(rataSdPadre.getDataScadenzaPagamento(), formatter);
							  LocalDate newDate = localDate.plusMonths((long) numMesi * i);
							  String dsp = newDate.toString();
							  Date dspagamento;
							  try {
								dspagamento = form.parse(dsp);
								map.put("dataScadenzaPagamento", dspagamento);
							} catch (ParseException e) {
								LOGGER.error("[RataSdAmbienteDAOImpl::updateNRataSd] Errore parse data rate successive ", e);
							}
							  

						}
					}
					if(i == 0) {
						BigDecimal canoneDovutoRateizzato = rataSdPadre.getCanoneDovuto().divide(BigDecimal.valueOf(numRate)).setScale(2, RoundingMode.HALF_UP);;
						map.put("canoneDovuto", canoneDovutoRateizzato);
					}
					else {
						BigDecimal canoneDovutoRateizzato = rataSdPadre.getCanoneDovuto().divide(BigDecimal.valueOf(numRate)).setScale(2, RoundingMode.HALF_DOWN);;
						map.put("canoneDovuto", canoneDovutoRateizzato);
					}

					KeyHolder keyHolder = new GeneratedKeyHolder();

					params = getParameterValue(map);
					Integer id = template.update(getQuery(QUERY_UPDATE, null, null), params, keyHolder);
					id_rata.add(id);
				}
			}

		} catch (DataAccessException e) {
			LOGGER.error("[RataSdAmbienteDAOImpl::updateNRataSd] Errore nell'accesso ai dati",
					e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error(
					"[RataSdAmbienteDAOImpl::updateNRataSd] Errore nell'esecuzione della query",
					e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		}
		

		return id_rata;
	}
	
	public void deleteNRataSd(Long idStatoDebitorio) throws DAOException{
		LOGGER.debug("[RataSdAmbienteDAOImpl::deleteNRataSd] BEGIN");

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
		    template.update(getQuery(RataSdDAOImpl.QUERY_DELETE_N_RATA_SD_BY_STATO_DEBITORIO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[RataSdAmbienteDAOImpl::deleteNRataSd] ERROR : " , e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RataSdAmbienteDAOImpl::deleteNRataSd] END");
		}

	}
}
