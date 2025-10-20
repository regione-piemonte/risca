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
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.business.be.impl.dao.NazioniDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.NazioniDTO;

/**
 * The type Nazioni dao.
 *
 * @author CSI PIEMONTE
 */
public class NazioniDAOImpl extends RiscaBeSrvGenericDAO<NazioniDTO> implements NazioniDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
    private static final String QUERY_NAZIONI = "SELECT rdn.* FROM risca_d_nazione rdn  ";
    
    private static final String WHERE_QUERY_NAZIONI = "WHERE rdn.data_fine_validita is null ";
    
    private static final String ORDER_BY = "ORDER BY rdn.denom_nazione ";

	
	@Override
	public List<NazioniDTO> loadNazioni(boolean attivo) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<NazioniDTO> listaNazioni = new ArrayList<NazioniDTO>();
        if(attivo)
        	listaNazioni = findListByQuery(CLASSNAME, methodName, QUERY_NAZIONI + WHERE_QUERY_NAZIONI + ORDER_BY, null);
        else
        	listaNazioni = findListByQuery(CLASSNAME, methodName, QUERY_NAZIONI + ORDER_BY, null);
        return listaNazioni;
	}


	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<NazioniDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new NazioniRowMapper();
	}



    
    /**
     * The type Nazioni row mapper.
     */
    public static class NazioniRowMapper implements RowMapper<NazioniDTO> {

        /**
         * Instantiates a new Nazioni extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public NazioniRowMapper() throws SQLException {
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
        public NazioniDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	NazioniDTO bean = new NazioniDTO();
            populateBean(rs, bean);
            return bean;
        }

        
        private void populateBean(ResultSet rs, NazioniDTO bean) throws SQLException {
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


	@Override
	public RowMapper<NazioniDTO> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new NazioniExtendedRowMapper();
	}

	
    /**
     * The type Nazioni extended row mapper.
     */
    public static class NazioniExtendedRowMapper implements RowMapper<NazioniDTO> {

        /**
         * Instantiates a new Nazioni extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public NazioniExtendedRowMapper() throws SQLException {
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
        public NazioniDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	NazioniDTO bean = new NazioniDTO();
            populateBean(rs, bean);
            return bean;
        }

        
        private void populateBean(ResultSet rs, NazioniDTO bean) throws SQLException {
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
