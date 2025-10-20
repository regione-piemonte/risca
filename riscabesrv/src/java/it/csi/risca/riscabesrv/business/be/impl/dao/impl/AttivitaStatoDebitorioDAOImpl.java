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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.AttivitaStatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AttivitaStatoDebitorioDTO;

public class AttivitaStatoDebitorioDAOImpl  extends RiscaBeSrvGenericDAO<AttivitaStatoDebitorioDTO> implements AttivitaStatoDebitorioDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	public static final String QUERY_SELECT_ATTIVITA_SD = "SELECT rdasd.* FROM risca_d_attivita_stato_deb rdasd";
	public static final String QUERY_SELECT_ATTIVITA_SD_FILTERED_RIMBORSI = "SELECT rdasd.* FROM risca_d_attivita_stato_deb rdasd "
			+ "INNER JOIN risca_d_tipo_attivita_stato_deb rdtasd ON rdasd.id_tipo_attivita_stato_deb = rdtasd.id_tipo_attivita_stato_deb "
			+ "WHERE rdtasd.cod_tipo_attivita_stato_deb = 'R' or rdtasd.cod_tipo_attivita_stato_deb = 'M' ";
	
	public static final String QUERY_SELECT_ATTIVITA_SD_FILTERED_ACCERTAMENTI = "SELECT rdasd.* FROM risca_d_attivita_stato_deb rdasd "
			+ "INNER JOIN risca_d_tipo_attivita_stato_deb rdtasd ON rdasd.id_tipo_attivita_stato_deb = rdtasd.id_tipo_attivita_stato_deb "
			+ "WHERE rdtasd.cod_tipo_attivita_stato_deb = 'S' or rdtasd.cod_tipo_attivita_stato_deb = 'M' ";
	
	public static final String QUERY_SELECT_ATTIVITA_SD_BY_ID = "SELECT * FROM risca_d_attivita_stato_deb "
			+ "WHERE id_attivita_stato_deb = :idAttivitaStatoDebitorio  ";
	
	@Override
	public AttivitaStatoDebitorioDTO getAttivitaStatoDebitorioById(Long idAttivitaStatoDebitorio) {

		LOGGER.debug("[AttivitaStatoDebitorioDAOImpl::getAttivitaStatoDebitorioById] BEGIN");
        Map<String, Object> map = new HashMap<>();
        AttivitaStatoDebitorioDTO attivitaStatoDebitorioDTO = null;
        try {
            map.put("idAttivitaStatoDebitorio", idAttivitaStatoDebitorio);
            MapSqlParameterSource params = getParameterValue(map);
            attivitaStatoDebitorioDTO=  template.queryForObject(QUERY_SELECT_ATTIVITA_SD_BY_ID, params, getRowMapper());
		}catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[StatoContribuzioneDAOImpl::loadStatoContribuzioneById] Data not found for idAttivitaStatoDebitorio: "+ idAttivitaStatoDebitorio);
			return null;
		}  catch (Exception e) {
            LOGGER.error("[AttivitaStatoDebitorioDAOImpl::getAttivitaStatoDebitorioById] Errore generale ", e);
            LOGGER.debug("[AttivitaStatoDebitorioDAOImpl::getAttivitaStatoDebitorioById] END");
            return null;
		}
        LOGGER.debug("[AttivitaStatoDebitorioDAOImpl::getAttivitaStatoDebitorioById] END");
        return attivitaStatoDebitorioDTO;
        
	}
	
	@Override
	public List<AttivitaStatoDebitorioDTO> getAttivitaStatoDeb(String tipoAttivita) {

		LOGGER.debug("[AttivitaStatoDebitorioDAOImpl::getAttivitaStatoDeb] BEGIN");
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<AttivitaStatoDebitorioDTO> listAttivita = new ArrayList<AttivitaStatoDebitorioDTO>();
        try {
        	if(tipoAttivita != null) {
        		if(tipoAttivita.equals("rimborsi"))
        			listAttivita = findListByQuery(CLASSNAME, methodName, QUERY_SELECT_ATTIVITA_SD_FILTERED_RIMBORSI, null);
        		else if (tipoAttivita.equals("accertamenti"))
        			listAttivita = findListByQuery(CLASSNAME, methodName, QUERY_SELECT_ATTIVITA_SD_FILTERED_ACCERTAMENTI, null);
        	}
        	else
        		listAttivita = findListByQuery(CLASSNAME, methodName, QUERY_SELECT_ATTIVITA_SD, null); 
		}catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[AttivitaStatoDebitorioDAOImpl::getAttivitaStatoDeb] Lista attivita stato debitorio vuota");
			return Collections.emptyList();
		}  catch (Exception e) {
            LOGGER.error("[AttivitaStatoDebitorioDAOImpl::getAttivitaStatoDeb] Errore generale: ", e);
            LOGGER.debug("[AttivitaStatoDebitorioDAOImpl::getAttivitaStatoDeb] END");
           throw e;
		}
        LOGGER.debug("[AttivitaStatoDebitorioDAOImpl::getAttivitaStatoDeb] END");
        return listAttivita;
        
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<AttivitaStatoDebitorioDTO> getRowMapper() throws SQLException {
		 return new AttivitaStatoDebitorioRowMapper();
	}

    /**
     * The type AttivitaStatoDebitorioRowMapper .
     */
    public static class AttivitaStatoDebitorioRowMapper implements RowMapper<AttivitaStatoDebitorioDTO> {

        /**
         * Instantiates a new AttivitaStatoDebitorioRowMapper .
         *
         * @throws SQLException the sql exception
         */
        public AttivitaStatoDebitorioRowMapper() throws SQLException {
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
        public AttivitaStatoDebitorioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AttivitaStatoDebitorioDTO bean = new AttivitaStatoDebitorioDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, AttivitaStatoDebitorioDTO bean) throws SQLException {
        	bean.setIdAttivitaStatoDeb(rs.getLong("id_attivita_stato_deb"));
            bean.setCodAttivitaStatoDeb(rs.getString("cod_attivita_stato_deb"));
            bean.setDesAttivitaStatoDeb(rs.getString("des_attivita_stato_deb"));
        	bean.setIdTipoAttivitaStatoDeb(rs.getLong("id_tipo_attivita_stato_deb"));
        }
    }

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new AttivitaStatoDebitorioExtendedRowMapper();
	}
    
    /**
     * The type Attivita Stato Debitorio extended row mapper.
     */
    public static class AttivitaStatoDebitorioExtendedRowMapper implements RowMapper<AttivitaStatoDebitorioDTO> {

        /**
         * Instantiates a new Attivita Stato Debitorio row mapper.
         *
         * @throws SQLException the sql exception
         */
        public AttivitaStatoDebitorioExtendedRowMapper() throws SQLException {
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
        public AttivitaStatoDebitorioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AttivitaStatoDebitorioDTO bean = new AttivitaStatoDebitorioDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, AttivitaStatoDebitorioDTO bean) throws SQLException {
        	bean.setIdAttivitaStatoDeb(rs.getLong("id_attivita_stato_deb"));
            bean.setCodAttivitaStatoDeb(rs.getString("cod_attivita_stato_deb"));
            bean.setDesAttivitaStatoDeb(rs.getString("des_attivita_stato_deb"));
        	bean.setIdTipoAttivitaStatoDeb(rs.getLong("id_tipo_attivita_stato_deb"));
        }
    }

}
