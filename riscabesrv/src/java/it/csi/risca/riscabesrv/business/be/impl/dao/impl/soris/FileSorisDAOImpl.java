/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.soris;

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

import it.csi.risca.riscabesrv.business.be.impl.dao.soris.FileSorisDAO;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.soris.FileSorisDTO;

/**
 * The type FileSoris dao.
 *
 */
public class FileSorisDAOImpl extends RiscaBeSrvGenericDAO<FileSorisDTO> implements FileSorisDAO {

	public static final String QUERY_INSERT = "insert into risca_t_file_soris (id_file_soris, nome_file_soris, data_file, data_scarico, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "values(:idFileSoris, :nomeFileSoris, :dataFile, :dataScarico, :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid) ";

	@Override
	public FileSorisDTO saveFileSoris(FileSorisDTO dto) {
		LOGGER.debug("[FileSorisDAOImpl::saveFileSoris] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			String gestUID = null;
			Long genId = findNextSequenceValue("seq_risca_t_file_soris");
			map.put("idFileSoris", genId);
			map.put("nomeFileSoris", dto.getNomeFileSoris());
			map.put("dataFile", dto.getDataFile());
			map.put("dataScarico", dto.getDataScarico());
			gestUID = generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now);
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", gestUID);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			dto.setGestUid(gestUID);
			dto.setIdFileSoris(genId);
			return dto;

		} catch (SQLException e) {
			LOGGER.error("[FileSorisDAOImpl::saveFileSoris] Errore nell'esecuzione della query", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[FileSorisDAOImpl::saveFileSoris] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} finally {
			LOGGER.debug("[FileSorisDAOImpl::saveFileSoris] END");
		}

	}
	
	@Override
	public Integer countFileSorisByNomeFile(String nomeFileSoris)  throws DAOException, SystemException {
		LOGGER.debug("[FileSorisDAOImpl::countFileSorisByNomeFile] START");
		LOGGER.debug("[FileSorisDAOImpl::countFileSorisByNomeFile] nomeFileSoris: "+nomeFileSoris);

		StringBuilder sql = new StringBuilder();
		 Map<String, Object> map = new HashMap<>();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		
		sql.append("select count(*) from risca_t_file_soris ");		
		sql.append(" where nome_file_soris like :nomeFileSoris||'%' ");

		Integer conteggio = 0;
		
		try {
			map.put("nomeFileSoris", nomeFileSoris);
            MapSqlParameterSource params = getParameterValue(map);
			conteggio = template.queryForObject(sql.toString(), params, Integer.class);

		} catch (RuntimeException ex) {
			LOGGER.error("[FileSorisDAOImpl::countFileSorisByNomeFile] esecuzione query", ex);
			throw new DAOException("Query failed anno creazione non trovato");
		} finally {
			LOGGER.debug("[FileSorisDAOImpl::countFileSorisByNomeFile] END");
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
	public RowMapper<FileSorisDTO> getRowMapper() throws SQLException {
		return new FileSorisRowMapper();
	}

	@Override
	public RowMapper<FileSorisDTO> getExtendedRowMapper() throws SQLException {

		return new FileSorisRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class FileSorisRowMapper implements RowMapper<FileSorisDTO> {

		/**
		 * Instantiates  extended row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public FileSorisRowMapper() throws SQLException {
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
		public FileSorisDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			FileSorisDTO bean = new FileSorisDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, FileSorisDTO bean) throws SQLException {
			bean.setIdFileSoris(rs.getLong("id_file_soris"));
			bean.setNomeFileSoris(rs.getString("nome_file_soris"));
			bean.setDataFile(rs.getDate("data_file"));
			bean.setDataScarico(rs.getDate("data_scarico"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}


}
