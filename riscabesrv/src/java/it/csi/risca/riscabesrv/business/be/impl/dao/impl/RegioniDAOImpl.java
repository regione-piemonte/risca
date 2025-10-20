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
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.RegioniDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.NazioniDTO;
import it.csi.risca.riscabesrv.dto.RegioneDTO;
import it.csi.risca.riscabesrv.dto.RegioneExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class RegioniDAOImpl  extends RiscaBeSrvGenericDAO<RegioneDTO> implements  RegioniDAO{

	
	private final String CLASSNAME = this.getClass().getSimpleName();
	
	
    private static final String QUERY_REGIONI = "SELECT distinct rdr.*, rdn.* FROM risca_d_regione rdr  "
    		+ "INNER JOIN risca_d_nazione rdn ON rdr.id_nazione = rdn.id_nazione ";
    
    private static final String WHERE_COND_REGIONI = "WHERE ";
    
    private static final String COD_REGIONI = "rdr.cod_regione =:codRegione";

	@Override
	public List<RegioneExtendedDTO> loadRegioni() throws Exception {
		LOGGER.debug("[RegioniDAOImpl::loadRegioni] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug("[RegioniDAOImpl::loadRegioni] END");
		  try {
			return findListByQuery(CLASSNAME, methodName, QUERY_REGIONI , null);
		} catch (Exception e) {
            LOGGER.error("[RegioniDAOImpl::loadRegioni] Errore nell'esecuzione della query", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		}
	}
	
	@Override
	public RegioneExtendedDTO loadRegioniByCodRegione(String codRegione) throws Exception {
        LOGGER.debug("[RegioniDAOImpl::loadRegioniByCodRegione] BEGIN");
        Map<String, Object> map = new HashMap<>();
        try {
        	if(codRegione != null) {
            map.put("codRegione", codRegione);
            MapSqlParameterSource params = getParameterValue(map);
            return template.queryForObject( QUERY_REGIONI + WHERE_COND_REGIONI + COD_REGIONI, params, getExtendedRowMapper());
         }else {
        	 return template.queryForObject( QUERY_REGIONI , map, getExtendedRowMapper());
        }
	  } catch (SQLException e) {
          LOGGER.error("[RegioniDAOImpl::loadRegioniByCodRegione] Errore nell'esecuzione della query", e);
      	throw new Exception(Constants.ERRORE_GENERICO);
      } catch (DataAccessException e) {
          LOGGER.error("[RegioniDAOImpl::loadRegioniByCodRegione] Errore nell'accesso ai dati", e);
      	throw new Exception(Constants.ERRORE_GENERICO);
      } finally {
          LOGGER.debug("[RegioniDAOImpl::loadRegioniByCodRegione] END");
      }
	}
	
	@Override
	public String getPrimaryKeySelect() {
		return null;
	}
	
	@Override
	public RowMapper<RegioneDTO> getRowMapper() throws SQLException {
		return new RegioneRowMapper();
	}
	
	@Override
	public RowMapper<RegioneExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new RegioneExtendedRowMapper();
	}
	
	 /**
     * The type Provincia row mapper.
     */
    public static class RegioneRowMapper implements RowMapper<RegioneDTO> {

        /**
         * Instantiates a new Regione row mapper.
         *
         * @throws SQLException the sql exception
         */
        public RegioneRowMapper() throws SQLException {
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
        public RegioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	RegioneDTO bean = new RegioneDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, RegioneDTO bean) throws SQLException {
            bean.setIdRegione(rs.getLong("id_regione"));
            bean.setCodRegione(rs.getString("cod_regione"));
            bean.setDenomRegione(rs.getString("denom_regione"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
        }
    }
    
    /**
     * The type Provincia extended row mapper.
     */
    public static class RegioneExtendedRowMapper implements RowMapper<RegioneExtendedDTO> {

        /**
         * Instantiates a new Regioni extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public RegioneExtendedRowMapper() throws SQLException {
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
        public RegioneExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            RegioneExtendedDTO bean = new RegioneExtendedDTO();
            populateBeanRegione(rs, bean);
            return bean;
        }


        private void populateBeanRegione(ResultSet rs, RegioneExtendedDTO bean) throws SQLException {
            bean.setIdRegione(rs.getLong("id_regione"));
            bean.setCodRegione(rs.getString("cod_regione"));
            bean.setDenomRegione(rs.getString("denom_regione"));
            //bean.setIdNazione(rs.getLong("id_nazione"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            
            NazioniDTO nazione = new NazioniDTO();
            populateBeanNazione(rs, nazione);
            bean.setNazione(nazione);
        }
        
        private void populateBeanNazione(ResultSet rs, NazioniDTO bean) throws SQLException {
            bean.setIdNazione(rs.getLong("id_nazione"));
            bean.setCodIstatNazione(rs.getString("cod_istat_nazione"));
            bean.setCodBelfioreNazione(rs.getString("cod_belfiore_nazione"));
            bean.setDenomNazione(rs.getString("denom_nazione"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setDtIdStato(rs.getLong("dt_id_stato"));
            bean.setDtIdStatoPrev(rs.getLong("dt_id_stato_prev"));
            bean.setDtIdStatoNext(rs.getLong("dt_id_stato_next"));
            bean.setCodIso2(rs.getString("cod_iso2"));
            
        }
        
    }

	
}
