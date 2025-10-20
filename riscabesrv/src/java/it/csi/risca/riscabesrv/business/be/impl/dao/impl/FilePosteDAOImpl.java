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

import it.csi.risca.riscabesrv.business.be.impl.dao.FilePosteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.FilePosteDTO;

/**
 * The type FilePoste dao.
 *
 */
public class FilePosteDAOImpl extends RiscaBeSrvGenericDAO<FilePosteDTO> implements FilePosteDAO {

	public static final String QUERY_INSERT = "insert into risca_t_file_poste (id_file_poste, tipo_file_poste, nome, data_file, data_scarico, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "values(:idFilePoste, :tipoFilePoste, :nome, :dataFile, :dataScarico, :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid) ";

	@Override
	public FilePosteDTO saveFilePoste(FilePosteDTO dto) {
		LOGGER.debug("[FilePosteDAOImpl::saveFilePoste] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			String gestUID = null;
			Long genId = findNextSequenceValue("seq_risca_t_file_poste");
			map.put("idFilePoste", genId);
			map.put("tipoFilePoste", dto.getTipoFilePoste());
			map.put("nome", dto.getNome());
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
			dto.setIdFilePoste(genId);
			return dto;

		} catch (SQLException e) {
			LOGGER.error("[FilePosteDAOImpl::saveFilePoste] Errore nell'esecuzione della query", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[FilePosteDAOImpl::saveFilePoste] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} finally {
			LOGGER.debug("[FilePosteDAOImpl::saveFilePoste] END");
		}

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
	public RowMapper<FilePosteDTO> getRowMapper() throws SQLException {
		return new FilePosteRowMapper();
	}

	@Override
	public RowMapper<FilePosteDTO> getExtendedRowMapper() throws SQLException {

		return new FilePosteRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class FilePosteRowMapper implements RowMapper<FilePosteDTO> {

		/**
		 * Instantiates a new Tipo invio extended row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public FilePosteRowMapper() throws SQLException {
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
		public FilePosteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			FilePosteDTO bean = new FilePosteDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, FilePosteDTO bean) throws SQLException {
			bean.setIdFilePoste(rs.getLong("id_file_poste"));
			bean.setTipoFilePoste(rs.getString("tipo_file_poste"));
			bean.setNome(rs.getString("nome"));
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
