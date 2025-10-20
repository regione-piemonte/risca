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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.impl.dao.ImmagineDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.ImmagineDTO;

/**
 * The type FilePoste dao.
 *
 */
public class ImmagineDAOImpl extends RiscaBeSrvGenericDAO<ImmagineDTO> implements ImmagineDAO {

	public static final String QUERY_INSERT = "INSERT INTO risca_t_immagine (id_immagine, immagine, flg_validita, path_immagine, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "values(:idImmagine, :immagine, :flgValidita, :pathImmagine, :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid) ";
	
	public static final String QUERY_SELECT_BY_ID = "SELECT * FROM risca_t_immagine WHERE id_immagine = :idImmagine ";

	@Override
	public ImmagineDTO saveImmagine(ImmagineDTO dto) {
		LOGGER.debug("[ImmagineDAOImpl::saveImmagine] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			String gestUID = null;
			Long genId = findNextSequenceValue("seq_risca_t_immagine");
			map.put("idImmagine", genId);
			map.put("immagine", dto.getImmagine());
			map.put("flgValidita", dto.getFlgValidita());
			map.put("pathImmagine", dto.getPathImmagine());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			dto.setGestUid(gestUID);
			dto.setIdImmagine(genId);
			return dto;

		} catch (SQLException e) {
			LOGGER.error("[ImmagineDAOImpl::saveImmagine] Errore nell'esecuzione della query", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[ImmagineDAOImpl::saveImmagine] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} finally {
			LOGGER.debug("[ImmagineDAOImpl::saveImmagine] END");
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
	public RowMapper<ImmagineDTO> getRowMapper() throws SQLException {
		return new ImmagineRowMapper();
	}

	@Override
	public RowMapper<ImmagineDTO> getExtendedRowMapper() throws SQLException {

		return new ImmagineRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class ImmagineRowMapper implements RowMapper<ImmagineDTO> {

		/**
		 * Instantiates a new extended row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public ImmagineRowMapper() throws SQLException {
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
		public ImmagineDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ImmagineDTO bean = new ImmagineDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, ImmagineDTO bean) throws SQLException {
			bean.setIdImmagine(rs.getLong("id_immagine"));
			bean.setImmagine(rs.getString("immagine"));
			bean.setFlgValidita(rs.getInt("flg_validita"));
			bean.setPathImmagine(rs.getString("path_immagine"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}
	}

	@Override
	public ImmagineDTO loadImmagineById(Integer idImmagine) throws SQLException {
		LOGGER.debug("[LockRiscossioneDAOImpl::loadImmagineById] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idImmagine", idImmagine);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(getQuery(QUERY_SELECT_BY_ID, null, null), params,
					getRowMapper());

		} catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[LockRiscossioneDAOImpl::loadImmagineById] No record found in database for idImmagine "+ idImmagine, e);
		    return null;
		} catch (SQLException e) {
			LOGGER.error("[LockRiscossioneDAOImpl::loadImmagineById] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[LockRiscossioneDAOImpl::loadImmagineById] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[LockRiscossioneDAOImpl::loadImmagineById] END");
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}
}
