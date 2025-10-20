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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiTitoloDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.TipiTitoloDTO;
import it.csi.risca.riscabesrv.dto.TipiTitoloExtendedDTO;

/**
 * The type Tipo Titolo dao.
 *
 * @author CSI PIEMONTE
 */
public class TipiTitoloDAOImpl extends RiscaBeSrvGenericDAO<TipiTitoloDTO> implements TipiTitoloDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String ID_AMBITO = "idAmbito";
    
    private static final String QUERY_TIPI_TITOLO = "SELECT rdtt.*, rdtt.id_ambito AS ambito_id, rda.* "
            + "FROM risca_d_tipo_titolo rdtt "
            + "INNER JOIN risca_d_ambito rda ON rdtt.id_ambito = rda.id_ambito ";
    
    private static final String QUERY_TIPI_TITOLO_BY_ID_AMBITO = QUERY_TIPI_TITOLO
            + "where rdtt.id_ambito = :idAmbito ";
    
    private static final String QUERY_TIPI_TITOLO_BY_ID_TIPO_TITOLO_AND_BY_ID_AMBITO = QUERY_TIPI_TITOLO_BY_ID_AMBITO
            + "and rdtt.id_tipo_titolo = :idTipoTitolo ";
    
    private static final String QUERY_TIPI_TITOLO_BY_CODE_TIPO_TITOLO_AND_BY_ID_AMBITO = QUERY_TIPI_TITOLO_BY_ID_AMBITO
            + "and UPPER(rdtt.cod_tipo_titolo) = UPPER(:codTipoTitolo) ";
	

    private static final String QUERY_DATA_INIZIO_VALIDITA =" rdtt.data_inizio_validita >= :dataInizioValidita";
    
    private static final String QUERY_DATA_FINE_VALIDITA =" rdtt.data_fine_validita <= :dataFineValidita";
    
    private static final String QUERY_ORDER_BY =" ORDER BY rdtt.ordina_tipo_titolo asc ";
    
	@Override
	public List<TipiTitoloExtendedDTO> loadTipiTitolo() {
		LOGGER.debug("[TipiTitoloDAOImpl::loadTipiTitolo] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug("[TipiTitoloDAOImpl::loadTipiTitolo] BEGIN");
        return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_TITOLO + QUERY_ORDER_BY, null);
	}

	@Override
	public List<TipiTitoloExtendedDTO> loadTipiTitoloByIdAmbito(Long idAmbito , String dataIniVal,  String dataFineVal) throws ParseException {
		LOGGER.debug("[TipiTitoloDAOImpl::loadTipiTitoloByIdAmbito] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        Map<String, Object> map = new HashMap<>();
        map.put(ID_AMBITO, idAmbito);
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		
	        if (dataIniVal != null && dataFineVal != null ) {
	 				Date dataIniValidita = formatter.parse(dataIniVal);
	 				Date dataFineValidita = formatter.parse(dataFineVal);
	 				map.put("dataInizioValidita", dataIniValidita);
	 				map.put("dataFineValidita", dataFineValidita);
	 				LOGGER.debug("[TipiTitoloDAOImpl::loadTipiTitoloByIdAmbito] END");
	 				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_TITOLO_BY_ID_AMBITO+ " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + QUERY_ORDER_BY, map);
	    		 }
    		 
 	        else  if (dataIniVal != null && dataFineVal == null ) {
	 				Date dataIniValidita = formatter.parse(dataIniVal);
	 				map.put("dataInizioValidita", dataIniValidita);
	 				LOGGER.debug("[TipiTitoloDAOImpl::loadTipiTitoloByIdAmbito] END");
	 				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_TITOLO_BY_ID_AMBITO+ " and "+ QUERY_DATA_INIZIO_VALIDITA +" and rdtt.data_fine_validita is null " + QUERY_ORDER_BY , map);
 	        	 
 	        	 }  
 	        else  if (dataIniVal == null && dataFineVal != null ) {
 		 			Date dataFineValidita = formatter.parse(dataFineVal);
	 				map.put("dataFineValidita", dataFineValidita);
	 				LOGGER.debug("[TipiTitoloDAOImpl::loadTipiTitoloByIdAmbito] END");
	 				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_TITOLO_BY_ID_AMBITO+ " and "+ QUERY_DATA_FINE_VALIDITA + QUERY_ORDER_BY, map);
 	        	 
 	        	 }
 	        else {
 	           LOGGER.debug("[TipiTitoloDAOImpl::loadTipiTitoloByIdAmbito] END");
 	           return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_TITOLO_BY_ID_AMBITO + QUERY_ORDER_BY, map);
 	        }
    		 
    	}catch (ParseException e) {
   		 LOGGER.error("[TipiTitoloDAOImpl::loadTipiTitoloByIdAmbito] Errore nel parse ", e);
   		 throw e;
	    }
    
        
        
	}

	@Override
	public TipiTitoloExtendedDTO loadTipoTitoloByIdTipiTitoloAndIdAmbito(Long idTipoTitolo, Long idAmbito) throws SQLException {		
        LOGGER.debug("[TipiTitoloDAOImpl::loadTipoTitoloByIdTipoTitoloAndIdAmbito] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("idTipoTitolo", idTipoTitolo);
            map.put(ID_AMBITO, idAmbito);
            MapSqlParameterSource params = getParameterValue(map);
            return template.queryForObject(QUERY_TIPI_TITOLO_BY_ID_TIPO_TITOLO_AND_BY_ID_AMBITO + QUERY_ORDER_BY, params, getExtendedRowMapper());
        } catch (SQLException e) {
            LOGGER.error("[TipiTitoloDAOImpl::loadTipoTitoloByIdTipoTitoloAndIdAmbito] Errore nell'esecuzione della query", e);
            throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[TipiTitoloDAOImpl::loadTipoTitoloByIdTipoTitoloAndIdAmbito] Errore nell'accesso ai dati", e);
            throw e;
        } finally {
            LOGGER.debug("[TipiTitoloDAOImpl::loadTipoTitoloByIdTipoTitoloAndIdAmbito] END");
        }
	}

	@Override
	public TipiTitoloExtendedDTO loadTipoTitoloByCodeAndIdAmbito(String codOrIdTipoTitolo, Long idAmbito) throws SQLException {
        LOGGER.debug("[TipiTitoloDAOImpl::loadTipoTitoloByCodeAndIdAmbito] BEGIN");
        try { 
            Map<String, Object> map = new HashMap<>();
            map.put(ID_AMBITO, idAmbito);
            Long idTipoTitolo = 0L;
			boolean bool = false;
			try {
				idTipoTitolo = Long.parseLong(codOrIdTipoTitolo);
				bool = true;
			} catch (NumberFormatException nfe) {
				bool = false;
			}
			if (bool) {
	            map.put("idTipoTitolo", idTipoTitolo);
				MapSqlParameterSource params = getParameterValue(map);
				LOGGER.debug("[TipiTitoloDAOImpl::loadTipoTitoloByCodeAndIdAmbito] END");
				return template.queryForObject(QUERY_TIPI_TITOLO_BY_ID_TIPO_TITOLO_AND_BY_ID_AMBITO + QUERY_ORDER_BY, params,
						getExtendedRowMapper());

			} else {
		        map.put("codTipoTitolo",codOrIdTipoTitolo);
				MapSqlParameterSource params = getParameterValue(map);
				LOGGER.debug("[TipiTitoloDAOImpl::loadTipoTitoloByCodeAndIdAmbito] END");
				return template.queryForObject(QUERY_TIPI_TITOLO_BY_CODE_TIPO_TITOLO_AND_BY_ID_AMBITO + QUERY_ORDER_BY, params,
						getExtendedRowMapper());

			}  
        } catch (SQLException e) {
            LOGGER.error("[TipiTitoloDAOImpl::loadTipoTitoloByCodeAndIdAmbito] Errore nell'esecuzione della query", e);
            throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[TipiTitoloDAOImpl::loadTipoTitoloByCodeAndIdAmbito] Errore nell'accesso ai dati", e);
           throw e;
        } finally {
            LOGGER.debug("[TipiTitoloDAOImpl::loadTipoTitoloByCodeAndIdAmbito] END");
        }
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<TipiTitoloDTO> getRowMapper() throws SQLException {
		 return new TipiTitoloRowMapper();
	}

	@Override
	public RowMapper<TipiTitoloExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new TipiTitoloExtendedRowMapper();
	}

    /**
     * The type Tipo Titolo row mapper.
     */
    public static class TipiTitoloRowMapper implements RowMapper<TipiTitoloDTO> {

        /**
         * Instantiates a new Tipo Titolo row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiTitoloRowMapper() throws SQLException {
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
        public TipiTitoloDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiTitoloDTO bean = new TipiTitoloDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiTitoloDTO bean) throws SQLException {
            bean.setIdTipoTitolo(rs.getLong("id_tipo_titolo"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setCodTipoTitolo(rs.getString("cod_tipo_titolo"));
            bean.setDesTipoTitolo(rs.getString("des_tipo_titolo"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setOrdinaTipoTitolo(rs.getLong("ordina_tipo_titolo"));
            bean.setFlgDefault(rs.getInt("flg_default"));

        }
    }
    
    /**
     * The type Tipo Titolo extended row mapper.
     */
    public static class TipiTitoloExtendedRowMapper implements RowMapper<TipiTitoloExtendedDTO> {

        /**
         * Instantiates a new Tipo titolo extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiTitoloExtendedRowMapper() throws SQLException {
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
        public TipiTitoloExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiTitoloExtendedDTO bean = new TipiTitoloExtendedDTO();
            populateBeanExtended(rs, bean);
            return bean;
        }

        private void populateBeanExtended(ResultSet rs, TipiTitoloExtendedDTO bean) throws SQLException {
            bean.setIdTipoTitolo(rs.getLong("id_tipo_titolo"));
            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
            bean.setCodTipoTitolo(rs.getString("cod_tipo_titolo"));
            bean.setDesTipoTitolo(rs.getString("des_tipo_titolo"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setOrdinaTipoTitolo(rs.getLong("ordina_tipo_titolo"));
            bean.setFlgDefault(rs.getInt("flg_default"));

        }

        private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("ambito_id"));
            bean.setCodAmbito(rs.getString("cod_ambito"));
            bean.setDesAmbito(rs.getString("des_ambito"));
        }
    }


}
