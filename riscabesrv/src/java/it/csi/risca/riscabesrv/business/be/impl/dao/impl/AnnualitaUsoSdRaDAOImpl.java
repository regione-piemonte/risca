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
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdRaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdRaDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class AnnualitaUsoSdRaDAOImpl extends RiscaBeSrvGenericDAO<AnnualitaUsoSdRaDTO> implements AnnualitaUsoSdRaDAO {

	public static final String QUERY_INSERT = "INSERT INTO risca_w_annualita_uso_sd_ra "
			+ " (id_annualita_uso_sd_ra, id_annualita_uso_sd, id_riduzione_aumento, "
			+ " gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ " VALUES(:idAnnualitaUsoSdRa, :idAnnualitaUsoSd, :idRiduzioneAumento, "
			+ " :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid) ";
	
	public static final String QUERY_INSERT_WORKING = "INSERT INTO risca_w_annualita_uso_sd_ra "
			+ " (id_annualita_uso_sd_ra, id_annualita_uso_sd, id_riduzione_aumento) "
			+ " VALUES(:idAnnualitaUsoSdRa, :idAnnualitaUsoSd, :idRiduzioneAumento) ";

	private static final String QUERY_DELETE_WORKING_BY_ID_ANNUALITA_SD = " DELETE FROM risca_w_annualita_uso_sd_ra WHERE id_annualita_uso_sd = :idAnnualitaUsoSd";
	
	private static final String QUERY_INSERT_ANNUALITA_USO_SD_RA_FROM_WORKING =" insert into RISCA_R_ANNUALITA_USO_SD_RA (id_annualita_uso_sd_ra, id_annualita_uso_sd, id_riduzione_aumento, "
			+ " gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ " select id_annualita_uso_sd_ra, id_annualita_uso_sd, id_riduzione_aumento, "
			+ " :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid "
			+ " from RISCA_W_ANNUALITA_USO_SD_RA where id_annualita_uso_sd = :idAnnualitaUsoSd ";
	
	@Override
	public AnnualitaUsoSdRaDTO saveAnnualitaUsoSdRa(AnnualitaUsoSdRaDTO dto)
			throws DataAccessException, SQLException {
		LOGGER.debug("[AnnualitaUsoSdRaDAOImpl::saveAnnualitaUsoSdRa] BEGIN");
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		
		Long genId = findNextSequenceValue("seq_risca_r_annualita_uso_sd_ra");
		map.put("idAnnualitaUsoSdRa", genId);
		map.put("idAnnualitaUsoSd", dto.getIdAnnualitaUsoSd());
		map.put("idRiduzioneAumento", dto.getIdRiduzioneAumento());

		map.put("gestAttoreIns", dto.getGestAttoreIns());
		map.put("gestDataIns", now);
		map.put("gestAttoreUpd", dto.getGestAttoreUpd());
		map.put("gestDataUpd", now);
		map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
		
		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT;
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setIdAnnualitaUsoSdRa(genId);

		LOGGER.debug("[AnnualitaUsoSdRaDAOImpl::saveAnnualitaUsoSdRa] END");
		return dto;
	}
	
	public AnnualitaUsoSdRaDTO saveAnnualitaUsoSdRaWorking(AnnualitaUsoSdRaDTO dto)
			throws DataAccessException, SQLException {
		LOGGER.debug("[AnnualitaUsoSdRaDAOImpl::saveAnnualitaUsoSdRaWorking] BEGIN");
		Map<String, Object> map = new HashMap<>();

		Long genId = findNextSequenceValue("seq_risca_r_annualita_uso_sd_ra");
		map.put("idAnnualitaUsoSdRa", genId);
		map.put("idAnnualitaUsoSd", dto.getIdAnnualitaUsoSd());
		map.put("idRiduzioneAumento", dto.getIdRiduzioneAumento());

		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT_WORKING;
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setIdAnnualitaUsoSdRa(genId);

		LOGGER.debug("[AnnualitaUsoSdRaDAOImpl::saveAnnualitaUsoSdRaWorking] END");
		return dto;
	}
	
	@Override
	public Integer deleteAnnualitaUsoSdRaWorkingByIdAnnualitaUsoSd(Long idAnnualitaUsoSd) throws DAOException {
		LOGGER.debug("[AnnualitaUsoSdRaDAOImpl::deleteAnnualitaUsoSdRaWorkingByIdAnnualitaUsoSd] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAnnualitaUsoSd", idAnnualitaUsoSd);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_WORKING_BY_ID_ANNUALITA_SD, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[AnnualitaUsoSdRaDAOImpl::deleteAnnualitaUsoSdRaWorkingByIdAnnualitaUsoSd] ERROR : " + e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AnnualitaUsoSdRaDAOImpl::deleteAnnualitaUsoSdRaWorkingByIdAnnualitaUsoSd] END");
		}

		return res;
	}
	
	@Override
	public Integer copyAnnualitaUsoSdRaFromWorkingByAnnualitaUsoSd(Long idAnnualitaUsoSd, String attore) throws DAOException {
		LOGGER.debug("[AnnualitaUsoSdRaDAOImpl::copyAnnualitaUsoSdRaFromWorkingByAnnualitaSd] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idAnnualitaUsoSd", idAnnualitaUsoSd);
			map.put("gestAttoreIns", attore);
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_INSERT_ANNUALITA_USO_SD_RA_FROM_WORKING, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[AnnualitaUsoSdRaDAOImpl::copyAnnualitaUsoSdRaFromWorkingByAnnualitaSd] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AnnualitaUsoSdRaDAOImpl::copyAnnualitaUsoSdRaFromWorkingByAnnualitaSd] END");
		}

		return res;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<AnnualitaUsoSdRaDTO> getRowMapper() throws SQLException {
		return new AnnualitaUsoSdRowMapper();
	}

	@Override
	public RowMapper<AnnualitaUsoSdRaDTO> getExtendedRowMapper() throws SQLException {
		return new AnnualitaUsoSdRowMapper();
	}

	public static class AnnualitaUsoSdRowMapper implements RowMapper<AnnualitaUsoSdRaDTO> {

		public AnnualitaUsoSdRowMapper() throws SQLException {
			// Instantiate class
		}

		@Override
		public AnnualitaUsoSdRaDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			AnnualitaUsoSdRaDTO bean = new AnnualitaUsoSdRaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AnnualitaUsoSdRaDTO bean) throws SQLException {
			bean.setIdAnnualitaUsoSdRa(rs.getLong("id_annualita_uso_sd_ra"));
			bean.setIdAnnualitaUsoSd(rs.getLong("id_annualita_uso_sd"));
			bean.setIdRiduzioneAumento(rs.getLong("id_riduzione_aumento"));
			
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
