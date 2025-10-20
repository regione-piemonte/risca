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
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.FonteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.FonteDTO;

/**
 * The type Fonte dao Impl.
 *
 * @author CSI PIEMONTE
 */
public class FonteDAOImpl extends RiscaBeSrvGenericDAO<FonteDTO> implements FonteDAO {
	

	private static final String QUERY_LOAD_FONTE ="SELECT rdf.* FROM risca_d_fonte rdf ";
	
	private static final String QUERY_LOAD_FONTE_BY_ID_FONTE = QUERY_LOAD_FONTE 
			+ "WHERE rdf.id_fonte = :idFonte ";
	
	private static final String QUERY_LOAD_FONTE_BY_COD_FONTE = QUERY_LOAD_FONTE 
			+ "WHERE rdf.cod_fonte = :codFonte ";
	
	private static final String QUERY_LOAD_FONTE_BY_CHIAVE_AND_COD = QUERY_LOAD_FONTE
			+ "WHERE rdf.chiave_sottoscrizione = :chiave  and rdf.cod_fonte = :codFonte";
	
	@Override
	public FonteDTO loadFonteByCodFonte(String codFonte) {
		LOGGER.debug("[FonteDaoImpl::loadFonteByCodFonte] BEGIN");
		FonteDTO fonteDTO = null;
		try {
			Map<String, Object> map = new HashMap<>();
			int id = 0;
			boolean bool = false;
			try {
				id = Integer.parseInt(codFonte);
				bool = true;
			} catch (NumberFormatException nfe) {
				bool = false;
			}

			if (bool) {
				map.put("idFonte", id);
				MapSqlParameterSource params = getParameterValue(map);
				return template.queryForObject(QUERY_LOAD_FONTE_BY_ID_FONTE, params, getRowMapper());
			} else {
				map.put("codFonte", codFonte);
				MapSqlParameterSource params = getParameterValue(map);
				return template.queryForObject(QUERY_LOAD_FONTE_BY_COD_FONTE, params, getRowMapper());
			}
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[FonteDaoImpl::loadFonteByCodFonte]No record found in database for codFonte: " + codFonte);
			return fonteDTO;
		} catch (SQLException e) {
			LOGGER.error("[FonteDaoImpl::loadFonteByCodFonte] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[FonteDaoImpl::loadFonteByCodFonte] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[FonteDaoImpl::loadFonteByCodFonte] END");
		}
	}
	@Override
	public FonteDTO loadFonteByChiaveFonte(String fruitore, String chiave ) {
		LOGGER.debug("[FonteDaoImpl::loadFonteByChiaveFonte] BEGIN");
		FonteDTO fonteDTO = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("chiave", chiave);
			map.put("codFonte", fruitore);
			MapSqlParameterSource params = getParameterValue(map);
			fonteDTO = template.queryForObject(QUERY_LOAD_FONTE_BY_CHIAVE_AND_COD, params, getRowMapper());
		}
		catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[FonteDaoImpl::loadFonteByChiaveFonte]No record found in database" , e);
			return fonteDTO;
		} catch (SQLException e) {
			LOGGER.error("[FonteDaoImpl::loadFonteByChiaveFonte] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[FonteDaoImpl::loadFonteByChiaveFonte] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[FonteDaoImpl::loadFonteByCodFonte] END");
		}
		return fonteDTO;
	}
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<FonteDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new FonteRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	 /**
     * The type fonte row mapper.
     */
    public static class FonteRowMapper implements RowMapper<FonteDTO> {

        /**
         * Instantiates a new Fonte row mapper.
         *
         * @throws SQLException the sql exception
         */
        public FonteRowMapper() throws SQLException {
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
        public FonteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	FonteDTO bean = new FonteDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, FonteDTO bean) throws SQLException {
            bean.setIdFonte(rs.getLong("id_fonte"));
            bean.setCodFonte(rs.getString("cod_fonte"));
            bean.setDesFonte(rs.getString("des_fonte"));
            bean.setChiaveSottoscrizione(rs.getString("chiave_sottoscrizione"));
        }
    }

}
