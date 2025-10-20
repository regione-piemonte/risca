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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.File450DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.File450DTO;

/**
 * The type File450 dao.
 *
 */
public class File450DAOImpl extends RiscaBeSrvGenericDAO<File450DTO> implements File450DAO {

	
	public static final String QUERY_INSERT = 	"insert into risca_t_file_450 (id_file_450, nome_file, data_creazione, data_conferma, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+"values(:idFile450, :nomeFile, :dataCreazione, :dataConferma, :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid) ";

		
	private static final String QUERY_SELECT_FILE_450= "SELECT * FROM risca_t_file_450 where id_file_450 = :idFile450 ";
	
	@Override
	public File450DTO loadFile450ByIdFile450(Long idFile450) throws DAOException {
		LOGGER.debug("[File450DAOImpl::loadFile450ByIdFile450] BEGIN");
		LOGGER.debug("[File450DAOImpl::loadFile450ByIdFile450] idFile450: "+idFile450);
        Map<String, Object> map = new HashMap<>();
        File450DTO file450 = null;
        try {
            map.put("idFile450", idFile450);
            MapSqlParameterSource params = getParameterValue(map);
            file450=  template.queryForObject(QUERY_SELECT_FILE_450, params, getRowMapper());
		} catch (Exception e) {
            LOGGER.error("[File450DAOImpl::loadFile450ByIdFile450] Errore generale ", e);
            LOGGER.debug("[File450DAOImpl::loadFile450ByIdFile450] END");
            return null;
		}
        LOGGER.debug("[File450DAOImpl::loadFile450ByIdFile450] END");
        return file450;
	}

	@Override
	public File450DTO saveFile450(File450DTO dto) throws DAOException {
		LOGGER.debug("[File450DAOImpl::saveFile450] BEGIN");
		try {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		String gestUID = null;
		Long genId = findNextSequenceValue("seq_risca_t_file_450");
		map.put("idFile450", genId);
		map.put("nomeFile", dto.getNomeFile());
		map.put("dataCreazione", dto.getDataCreazione());
		map.put("dataConferma", dto.getDataConferma());
			gestUID = generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now);
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", gestUID);
		
		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT;
		
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setGestDataIns(now);
		dto.setGestDataUpd(now);
	    dto.setGestUid(gestUID);
	    dto.setIdFile450(genId);
	    return dto;
	    
		}
	    catch (SQLException e) {
			LOGGER.error("[File450DAOImpl::saveFile450] Errore nell'esecuzione della query", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[File450DAOImpl::saveFile450] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} finally {
			LOGGER.debug("[File450DAOImpl::saveFile450] END");
		}
		
		
	}
	
	
	@Override
	public long countAnnoDataCreazioneByAnnoCorrente() throws DAOException, SystemException {
		LOGGER.debug("[File450DAOImpl::countAnnoDataCreazioneByAnnoCorrente] START");

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		sql.append("select count(*) from (select EXTRACT(year from rtf.data_creazione) as anno ");		
		sql.append("from risca_t_file_450 rtf ) anno where anno = EXTRACT(year from CURRENT_DATE) ");

		long conteggio = 0;
		
		try {
			
			conteggio = template.queryForObject(sql.toString(), paramMap, Long.class);

		} catch (RuntimeException ex) {
			LOGGER.error("[File450DAOImpl::countAnnoDataCreazioneByAnnoCorrente] esecuzione query", ex);
			throw new DAOException("Query failed anno creazione non trovato");
		} finally {
			LOGGER.debug("[File450DAOImpl::countAnnoDataCreazioneByAnnoCorrente] END");
		}
		return conteggio;
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
	public RowMapper<File450DTO> getRowMapper() throws SQLException {
		return new File450RowMapper();
	}


	/**
	 * The type Configurazione row mapper.
	 */
	public static class File450RowMapper implements RowMapper<File450DTO> {

		/**
		 * Instantiates a new  row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public File450RowMapper() throws SQLException {
			 // Instantiate class
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
		public File450DTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			File450DTO bean = new File450DTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, File450DTO bean) throws SQLException {
			bean.setIdFile450(rs.getLong("id_file_450"));
			bean.setNomeFile(rs.getString("nome_file"));
			bean.setDataConferma(rs.getDate("data_conferma"));
			bean.setDataCreazione(rs.getDate("data_creazione"));
		}
	  }
		

		@Override
		public RowMapper<File450DTO> getExtendedRowMapper() throws SQLException {

			return new File450ExtendedRowMapper();
		}
		
		
	    /**
	     * The type Tipo invio extended row mapper.
	     */
	    public static class File450ExtendedRowMapper implements RowMapper<File450DTO> {

	        /**
	         * Instantiates a new Tipo invio extended row mapper.
	         *
	         * @throws SQLException the sql exception
	         */
	        public File450ExtendedRowMapper() throws SQLException {
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
	        public File450DTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	File450DTO bean = new File450DTO();
	            populateBean(rs, bean);
	            return bean;
	        }

	        private void populateBean(ResultSet rs, File450DTO bean) throws SQLException {
	        	bean.setIdFile450(rs.getLong("id_file_450"));
				bean.setNomeFile(rs.getString("nome_file"));
				bean.setDataConferma(rs.getDate("data_conferma"));
				bean.setDataCreazione(rs.getDate("data_creazione"));
	        }
	    }


		@Override
		public String getTableNameAString() {
			// TODO Auto-generated method stub
			return null;
		}
	}


	

