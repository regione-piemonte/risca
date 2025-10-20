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
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.Soris00CDAO;
import it.csi.risca.riscabesrv.dto.soris.Soris00CDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Soris00C dao.
 *
 * @author CSI PIEMONTE
 */
public class Soris00CDAOImpl extends RiscaBeSrvGenericDAO<Soris00CDTO>  implements Soris00CDAO {
	
	private static final String QUERY_INSERT = 
	
	 " INSERT INTO risca_w_soris_00C  "
	+" (tipo_record,  filler1, cod_dest_mitt, filler2, data_creazione_flusso, cod_tracciato, filler3) "
	+" VALUES (:tipoRecord, :filler1, :codDestMitt, :filler2, :dataCreazioneFlusso, :codTracciato, :filler3) ";
	
	private static final String QUERY_DELETE = " delete from risca_w_soris_00C ";

	
	@Override
	public Soris00CDTO saveSoris00C(Soris00CDTO dto) throws DAOException, DataAccessException, SQLException {
		LOGGER.debug("[Soris00CDAO::saveSoris00C] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("tipoRecord", dto.getTipoRecord());
			map.put("filler1", dto.getFiller1());
			map.put("codDestMitt", dto.getCodDestMitt());
			map.put("filler2", dto.getFiller2());
			map.put("dataCreazioneFlusso", dto.getDataCreazioneFlusso());
			map.put("codTracciato", dto.getCodTracciato());
			map.put("filler3", dto.getFiller3());
			
			MapSqlParameterSource params = getParameterValue(map);
			
            template.update(getQuery(QUERY_INSERT, null, null), params);
			

		} 
		catch (Exception e) {
			LOGGER.error("[Soris00CDAO::saveSoris00C] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} 
		finally {
			LOGGER.debug("[Soris00CDAO::saveSoris00C] END");
		}
		return dto;
	}

	
	public Integer delete() throws DAOException{
		LOGGER.debug("[Soris00CDAOImpl::delete] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE, null, null), params, keyHolder);

		} finally {
			LOGGER.debug("[Soris00CDAOImpl::delete] END");
		}

		return res;
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
	public RowMapper<Soris00CDTO> getRowMapper() throws SQLException {
		return new Soris00CRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<Soris00CDTO> getExtendedRowMapper() throws SQLException {
		return new Soris00CRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class Soris00CRowMapper implements RowMapper<Soris00CDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public Soris00CRowMapper() throws SQLException {
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
		public Soris00CDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			Soris00CDTO bean = new Soris00CDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, Soris00CDTO bean) throws SQLException {
			bean.setTipoRecord(rs.getString("tipo_record"));
			bean.setFiller1(rs.getString("filler1"));
			bean.setCodDestMitt(rs.getString("cod_dest_mitt"));
			bean.setFiller2(rs.getString("filler2"));
			bean.setDataCreazioneFlusso(rs.getDate("data_creazione"));
			bean.setCodTracciato(rs.getString("cod_tracciato"));
			bean.setFiller3(rs.getString("filler3"));
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Long decodeId(String fromTableName, String searchFieldCriteris) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Long findNextSequenceValue(String sequenceName) throws DataAccessException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}





}