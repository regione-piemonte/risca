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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiduzioneAumentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.UsoRidaumSdDAO;
import it.csi.risca.riscabesrv.dto.RiduzioneAumentoDTO;
import it.csi.risca.riscabesrv.dto.UsoRidaumSdDTO;
import it.csi.risca.riscabesrv.dto.UsoRidaumSdExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class UsoRidaumSdDAOImpl  extends RiscaBeSrvGenericDAO<UsoRidaumSdDTO> implements UsoRidaumSdDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	@Autowired
	private RiduzioneAumentoDAO riduzioneAumentoDAO;  
	
	public static final String QUERY_SELECT_USO_RIDAUM_SD = "SELECT * FROM risca_r_annualita_uso_sd_ra "
			+ "WHERE id_annualita_uso_sd = :idAnnualitaUsoSd  ";

	public static final String QUERY_INSERT = "INSERT INTO risca_r_annualita_uso_sd_ra "
			+ "(id_annualita_uso_sd_ra, id_annualita_uso_sd, id_riduzione_aumento,  gest_attore_ins,gest_data_ins,"
			+ " gest_attore_upd, gest_data_upd, gest_uid) "
			+ "VALUES(:idUsoRidaumSd, :idAnnualitaUsoRidaum, :idRiduzioneAumento, "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid) ";
	
	public static final String QUERY_UPDATE = "UPDATE risca_r_annualita_uso_sd_ra "
			+ " SET   id_annualita_uso_sd = :idAnnualitaUsoRidaum, id_riduzione_aumento = :idRiduzioneAumento ,"
			+ " gest_data_upd = :gestDataUpd , "
			+ " gest_attore_upd = :gestAttoreUpd "
	        + " where id_annualita_uso_sd_ra  = :idUsoRidaumSd ";
	
	private static final String QUERY_DELETE_ANNUALITA_US_SD_RD_BY_ID = "DELETE FROM risca_r_annualita_uso_sd_ra WHERE id_annualita_uso_sd_ra = :idAnnualitaUsoSdRA";


	
	@Override
	public 	List<UsoRidaumSdExtendedDTO> loadUsoRidaumSdByIdAnnualitaUsoSd(long idAnnualitaUsoSd) {

		LOGGER.debug("[UsoRidaumSdDAOImpl::loadUsoRidaumSdByIdAnnualitaUsoSd] BEGIN");
        Map<String, Object> map = new HashMap<>();
    	List<UsoRidaumSdExtendedDTO> usoRidaumSdDTO = null;
        try {
            map.put("idAnnualitaUsoSd", idAnnualitaUsoSd);
            MapSqlParameterSource params = getParameterValue(map);
            usoRidaumSdDTO =  template.query(QUERY_SELECT_USO_RIDAUM_SD, params, getExtendedRowMapper());
            for (UsoRidaumSdExtendedDTO usoRidaumSdExtendedDTO : usoRidaumSdDTO) {
                RiduzioneAumentoDTO riduzioneAumento = riduzioneAumentoDAO.loadRiduzioneAumentoByIdRiduzioneAumento(usoRidaumSdExtendedDTO.getIdRiduzioneAumento());
                if(riduzioneAumento != null) {
                	usoRidaumSdExtendedDTO.setRiduzioneAumento(riduzioneAumento);
                }
			}

            
		}catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[UsoRidaumSdDAOImpl::loadUsoRidaumSdByIdAnnualitaUsoSd]Data not found for idAnnualitaUsoSd: "+ idAnnualitaUsoSd);
			return null;
		} catch (Exception e) {
            LOGGER.error("[UsoRidaumSdDAOImpl::loadUsoRidaumSdByIdAnnualitaUsoSd] Errore generale ", e);
            LOGGER.debug("[UsoRidaumSdDAOImpl::loadUsoRidaumSdByIdAnnualitaUsoSd] END");
            return null;
		}
        LOGGER.debug("[UsoRidaumSdDAOImpl::loadUsoRidaumSdByIdAnnualitaUsoSd] END");
        return usoRidaumSdDTO;
        
	}
	
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<UsoRidaumSdDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new UsoRidaumRowMapper();
	}

	@Override
	public RowMapper<UsoRidaumSdExtendedDTO> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new UsoRidaumSdExtendedRowMapper();
	}

    /**
     * The type UsoRidaumRowMapper .
     */
    public static class UsoRidaumSdExtendedRowMapper implements RowMapper<UsoRidaumSdExtendedDTO> {

        /**
         * Instantiates a new UsoRidaumRowMapper .
         *
         * @throws SQLException the sql exception
         */
        public UsoRidaumSdExtendedRowMapper() throws SQLException {
            // Instantiate class
        }

        /**
         * Implementations must implement this method to map each row of data
         * in the ResultSet. This method should not call {@code next()} on
         * the ResultSet; it is only supposed to map values of the current row.
         *
         * @param rs     the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return the result object for the current row (may be {@code null})
         * @throws SQLException if a SQLException is encountered getting
         *                      column values (that is, there's no need to catch SQLException)
         */
        @Override
        public UsoRidaumSdExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	UsoRidaumSdExtendedDTO bean = new UsoRidaumSdExtendedDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, UsoRidaumSdExtendedDTO bean) throws SQLException {
        	bean.setIdAnnualitaUsoRidaum(rs.getLong("id_annualita_uso_sd"));
        	bean.setIdRiduzioneAumento(rs.getLong("id_riduzione_aumento"));
        	bean.setIdAnnualitaUsoSdRa(rs.getLong("id_annualita_uso_sd_ra"));
        }
    }

    /**
     * The type UsoRidaumRowMapper .
     */
    public static class UsoRidaumRowMapper implements RowMapper<UsoRidaumSdDTO> {

        /**
         * Instantiates a new UsoRidaumRowMapper .
         *
         * @throws SQLException the sql exception
         */
        public UsoRidaumRowMapper() throws SQLException {
            // Instantiate class
        }

        /**
         * Implementations must implement this method to map each row of data
         * in the ResultSet. This method should not call {@code next()} on
         * the ResultSet; it is only supposed to map values of the current row.
         *
         * @param rs     the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return the result object for the current row (may be {@code null})
         * @throws SQLException if a SQLException is encountered getting
         *                      column values (that is, there's no need to catch SQLException)
         */
        @Override
        public UsoRidaumSdDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	UsoRidaumSdDTO bean = new UsoRidaumSdDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, UsoRidaumSdDTO bean) throws SQLException {
        	bean.setIdAnnualitaUsoRidaum(rs.getLong("id_annualita_uso_sd"));
        	bean.setIdRiduzioneAumento(rs.getLong("id_riduzione_aumento"));
        	bean.setIdAnnualitaUsoSdRa(rs.getLong("id_annualita_uso_sd_ra"));
        }
    }

	@Override
	public UsoRidaumSdExtendedDTO saveUsoRidaumSD(
			UsoRidaumSdExtendedDTO dto) throws Exception {
		LOGGER.debug("[UsoRidaumSdDAOImpl::saveUsoRidaumSD] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_r_annualita_uso_sd_ra");
			map.put("idUsoRidaumSd", genId);
			map.put("idAnnualitaUsoRidaum", dto.getIdAnnualitaUsoRidaum());
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
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			dto.setIdAnnualitaUsoSdRa(genId);
		} catch (DataAccessException e) {
			LOGGER.error("[UsoRidaumSdDAOImpl::saveUsoRidaumSD] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error("[UsoRidaumSdDAOImpl::saveUsoRidaumSD] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}
		LOGGER.debug("[UsoRidaumSdDAOImpl::saveUsoRidaumSD] END");
		return dto;
		
	}

	@Override
	public UsoRidaumSdExtendedDTO updateUsoRidaumSD(UsoRidaumSdExtendedDTO dto)
			throws Exception {
		LOGGER.debug("[UsoRidaumSdDAOImpl::updateUsoRidaumSD] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idUsoRidaumSd", dto.getIdAnnualitaUsoSdRa());
			map.put("idAnnualitaUsoRidaum", dto.getIdAnnualitaUsoRidaum());
			map.put("idRiduzioneAumento", dto.getIdRiduzioneAumento());
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_UPDATE;
			template.update(getQuery(query, null, null), params);
			dto.setGestDataUpd(now);
		} catch (Exception e) {
			LOGGER.error("[UsoRidaumSdDAOImpl::saveUsoRidaumSD] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}
		LOGGER.debug("[UsoRidaumSdDAOImpl::updateUsoRidaumSD] END");
		return dto;
	}

	@Override
	public UsoRidaumSdExtendedDTO deleteUsoRidaumSD(UsoRidaumSdExtendedDTO usoRidaumSdExtendedDTO)
			throws DataAccessException, SQLException {
		LOGGER.debug("[UsoRidaumSdDAOImpl::deleteUsoRidaumSD] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAnnualitaUsoSdRA", usoRidaumSdExtendedDTO.getIdAnnualitaUsoSdRa());
			MapSqlParameterSource params = getParameterValue(map);
			template.update(getQuery(QUERY_DELETE_ANNUALITA_US_SD_RD_BY_ID, null, null), params);
			
		} catch (DataAccessException e) {
			LOGGER.error("[UsoRidaumSdDAOImpl::deleteUsoRidaumSD] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[UsoRidaumSdDAOImpl::deleteUsoRidaumSD] END");
		}
		return usoRidaumSdExtendedDTO;
	}

}
