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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr0DAO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr0DTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type SorisFr0 dao.
 *
 * @author CSI PIEMONTE
 */
public class SorisFr0DAOImpl  extends RiscaBeSrvGenericDAO<SorisFr0DTO> implements SorisFr0DAO {
	
	private static final String QUERY_INSERT = 
	
	" insert into risca_w_soris_fr0 " + 
	" (tipo_record, progr_record, cod_ambito, mittente, data_riferimento, identif_file, " + 
	" tipo_file, identif_file_orig, data_creazione_file,  release, filler3 ) "+
	" VALUES (:tipoRecord, :progrRecord, :codAmbito, :mittente, :dataRiferimento, :identifFile, "+
    " :tipoFile, :identifFileOrig, :dataCreazioneFile, :release, :filler3 )";
	
	private static final String QUERY_DELETE = " delete from risca_w_soris_fr0 ";
	
	@Override
	public SorisFr0DTO saveSorisFr0(SorisFr0DTO dto) throws DAOException, DataAccessException, SQLException {
		LOGGER.debug("[SorisFr0DAO::saveSorisFr0] BEGIN");		
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("tipoRecord", dto.getTipoRecord());
			map.put("progrRecord", dto.getProgrRecord());
			map.put("codAmbito", dto.getCodAmbito());
			map.put("mittente", dto.getMittente());
			map.put("dataRiferimento", dto.getDataRiferimento());
			map.put("identifFile", dto.getIdentifFile());
			map.put("tipoFile", dto.getTipoFile());
			map.put("identifFileOrig", dto.getIdentifFileOrig());
			map.put("dataCreazioneFile", dto.getDataCreazioneFile());
			map.put("release", dto.getRelease());
			map.put("filler3", dto.getFiller3());
			
            MapSqlParameterSource params = getParameterValue(map);
			
            template.update(getQuery(QUERY_INSERT, null, null), params);
		} catch (Exception e) {
			LOGGER.error("[SorisFr0DAO::saveSorisFr0] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SorisFr0DAO::saveSorisFr0] END");
		}
		return dto;
	}

	public Integer delete() throws DAOException{
		LOGGER.debug("[SorisFr0DAO::delete] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE, null, null), params, keyHolder);

		} finally {
			LOGGER.debug("[SorisFr0DAO::delete] END");
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
	public RowMapper<SorisFr0DTO> getRowMapper() throws SQLException {
		return new SorisFr0RowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SorisFr0DTO> getExtendedRowMapper() throws SQLException {
		return new SorisFr0RowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class SorisFr0RowMapper implements RowMapper<SorisFr0DTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SorisFr0RowMapper() throws SQLException {
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
		public SorisFr0DTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SorisFr0DTO bean = new SorisFr0DTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SorisFr0DTO bean) throws SQLException {
			bean.setTipoRecord(rs.getString("tipo_record"));
			bean.setProgrRecord(rs.getInt("progr_record"));
			bean.setCodAmbito(rs.getString("cod_ambito"));
			bean.setMittente(rs.getString("mittente"));
			bean.setDataRiferimento(rs.getDate("data_riferimento"));
			bean.setIdentifFile(rs.getString("identif_file"));
			bean.setTipoFile(rs.getString("tipo_file"));
			bean.setIdentifFileOrig(rs.getString("identif_file_orig"));
			bean.setDataCreazioneFile(rs.getDate("data_creazione_file"));
			bean.setRelease(rs.getString("release"));
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