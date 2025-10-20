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
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiAutorizzazioneDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.TipiAutorizzazioneDTO;
import it.csi.risca.riscabesrv.dto.TipiAutorizzazioneExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipo Autorizzazione dao.
 *
 * @author CSI PIEMONTE
 */
public class TipiAutorizzazioneDAOImpl extends RiscaBeSrvGenericDAO<TipiAutorizzazioneDTO> implements TipiAutorizzazioneDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String ID_AMBITO = "idAmbito";
    
    private static final String QUERY_TIPI_AUTORIZZAZIONE = "SELECT rdta.*, rdta.id_ambito AS ambito_id, rda.* "
            + "FROM risca_d_tipo_autorizza rdta "
            + "INNER JOIN risca_d_ambito rda ON rdta.id_ambito = rda.id_ambito ";
    
    private static final String QUERY_TIPI_AUTORIZZAZIONE_BY_ID_AMBITO = QUERY_TIPI_AUTORIZZAZIONE
            + "where rdta.id_ambito = :idAmbito ";
    
    private static final String QUERY_TIPI_AUTORIZZAZIONE_BY_ID_TIPO_AUTORIZZAZIONE_AND_BY_ID_AMBITO = QUERY_TIPI_AUTORIZZAZIONE_BY_ID_AMBITO
            + "and rdta.id_tipo_autorizza = :idTipoAutorizzazione ";
    
    private static final String QUERY_TIPI_AUTORIZZAZIONE_BY_CODE_TIPO_AUTORIZZAZIONE_AND_BY_ID_AMBITO = QUERY_TIPI_AUTORIZZAZIONE_BY_ID_AMBITO
            + "and UPPER(rdta.cod_tipo_autorizza) = UPPER(:codTipoAutorizzazione) ";
    
    private static final String QUERY_DATA_INIZIO_VALIDITA =" rdta.data_inizio_validita >= :dataInizioValidita";
    
    private static final String QUERY_DATA_FINE_VALIDITA =" rdta.data_fine_validita <= :dataFineValidita";
    
    private static final String QUERY_ORDER_BY =" ORDER BY rdta.ordina_tipo_autorizza asc ";
    
	@Override
	public List<TipiAutorizzazioneExtendedDTO> loadTipiAutorizzazione() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
			return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_AUTORIZZAZIONE+QUERY_ORDER_BY, null);
		} catch (Exception e) {
            LOGGER.error("[TipiAutorizzazioneDAOImpl::loadTipiAutorizzazione] Errore nell'esecuzione della query", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		}
	}

	@Override
	public List<TipiAutorizzazioneExtendedDTO> loadTipiAutorizzazioneByIdAmbito(Long idAmbito,String dataIniVal,  String dataFineVal) throws ParseException {
		LOGGER.debug("[TipoAutorizzazioneDAOImpl::loadTipiAutorizzazioneByIdAmbito] BEGIN");
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
	 				LOGGER.debug("[TipoAutorizzazioneDAOImpl::loadTipiAutorizzazioneByIdAmbito] END");
	 				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_AUTORIZZAZIONE_BY_ID_AMBITO+ " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA+QUERY_ORDER_BY, map);
	    		 
    		 }
 	        else if(dataIniVal != null  && dataFineVal == null) {
	 				Date dataIniValidita = formatter.parse(dataIniVal);
	 				map.put("dataInizioValidita", dataIniValidita);
	 				LOGGER.debug("[TipoAutorizzazioneDAOImpl::loadTipiAutorizzazioneByIdAmbito] END");
	 				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_AUTORIZZAZIONE_BY_ID_AMBITO+ " and "+ QUERY_DATA_INIZIO_VALIDITA +" and rdta.data_fine_validita is null " +QUERY_ORDER_BY, map);
 	        	 
 	        	 }  
 	        else if (dataIniVal == null  && dataFineVal != null){
 	        	  
 		 			Date dataFineValidita = formatter.parse(dataFineVal);
	 				map.put("dataFineValidita", dataFineValidita);
	 				LOGGER.debug("[TipoAutorizzazioneDAOImpl::loadTipiAutorizzazioneByIdAmbito] END");
	 				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_AUTORIZZAZIONE_BY_ID_AMBITO+ " and "+ QUERY_DATA_FINE_VALIDITA+QUERY_ORDER_BY, map);
 	        	 
 	        	 }
 	        else {
 	           LOGGER.debug("[TipoAutorizzazioneDAOImpl::loadTipiAutorizzazioneByIdAmbito] END");
 	           return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_AUTORIZZAZIONE_BY_ID_AMBITO+QUERY_ORDER_BY, map);
 	        }
    		 
    	}catch (ParseException e) {
   		 LOGGER.error("[TipoAutorizzazioneDAOImpl::loadTipiAutorizzazioneByIdAmbito] Errore nel parse ", e);
   	     throw e;
	    }
    
	}

	@Override
	public TipiAutorizzazioneExtendedDTO loadTipoAutorizzazioneByIdTipiAutorizzazioneAndIdAmbito(Long idTipoAutorizzazione, Long idAmbito) throws SQLException {		
        LOGGER.debug("[TipoAutorizzazioneDAOImpl::loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("idTipoAutorizzazione", idTipoAutorizzazione);
            map.put(ID_AMBITO, idAmbito);
            MapSqlParameterSource params = getParameterValue(map);
            return template.queryForObject(QUERY_TIPI_AUTORIZZAZIONE_BY_ID_TIPO_AUTORIZZAZIONE_AND_BY_ID_AMBITO+QUERY_ORDER_BY, params, getExtendedRowMapper());
        } catch (SQLException e) {
            LOGGER.error("[TipoAutorizzazioneDAOImpl::loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] Errore nell'esecuzione della query", e);
            throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[TipoAutorizzazioneDAOImpl::loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] Errore nell'accesso ai dati", e);
            throw e;
        } finally {
            LOGGER.debug("[TipoAutorizzazioneDAOImpl::loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] END");
        }
	}

	@Override
	public TipiAutorizzazioneExtendedDTO loadTipoAutorizzazioneByCodeAndIdAmbito(String codOrIdTipoAutorizzazione, Long idAmbito) throws SQLException {
        LOGGER.debug("[TipoAutorizzazioneDAOImpl::loadTipoAutorizzazioneByCodeAndIdAmbito] BEGIN");
        try {
			Map<String, Object> map = new HashMap<>();
			map.put(ID_AMBITO, idAmbito);
			Long id = 0L;
			boolean bool = false;
			try {
				id = Long.parseLong(codOrIdTipoAutorizzazione);
				bool = true;
			} catch (NumberFormatException nfe) {
				bool = false;
			}
			if (bool) {
			    map.put("idTipoAutorizzazione", id);
				MapSqlParameterSource params = getParameterValue(map);
				return template.queryForObject(QUERY_TIPI_AUTORIZZAZIONE_BY_ID_TIPO_AUTORIZZAZIONE_AND_BY_ID_AMBITO+QUERY_ORDER_BY, params,
						getExtendedRowMapper());

			} else {
				map.put("codTipoAutorizzazione", codOrIdTipoAutorizzazione);
				MapSqlParameterSource params = getParameterValue(map);
				return template.queryForObject(QUERY_TIPI_AUTORIZZAZIONE_BY_CODE_TIPO_AUTORIZZAZIONE_AND_BY_ID_AMBITO+QUERY_ORDER_BY, params,
						getExtendedRowMapper());

			}  
		} catch (SQLException e) {
            LOGGER.error("[TipoAutorizzazioneDAOImpl::loadTipoAutorizzazioneByCodeAndIdAmbito] Errore nell'esecuzione della query", e);
           throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[TipoAutorizzazioneDAOImpl::loadTipoAutorizzazioneByCodeAndIdAmbito] Errore nell'accesso ai dati", e);
            throw e;
        } finally {
            LOGGER.debug("[TipoAutorizzazioneDAOImpl::loadTipoAutorizzazioneByCodeAndIdAmbito] END");
        }
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<TipiAutorizzazioneDTO> getRowMapper() throws SQLException {
		 return new TipiAutorizzazioneRowMapper();
	}

	@Override
	public RowMapper<TipiAutorizzazioneExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new TipiAutorizzazioneExtendedRowMapper();
	}

    /**
     * The type Tipo Autorizzazione row mapper.
     */
    public static class TipiAutorizzazioneRowMapper implements RowMapper<TipiAutorizzazioneDTO> {

        /**
         * Instantiates a new Tipo Autorizzazione row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiAutorizzazioneRowMapper() throws SQLException {
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
        public TipiAutorizzazioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiAutorizzazioneDTO bean = new TipiAutorizzazioneDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiAutorizzazioneDTO bean) throws SQLException {
            bean.setIdTipoAutorizza(rs.getLong("id_tipo_autorizza"));
            bean.setCodTipoAutorizza(rs.getString("cod_tipo_autorizza"));
            bean.setDesTipoAutorizza(rs.getString("des_tipo_autorizza"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setOrdinaTipoAutorizza(rs.getLong("ordina_tipo_autorizza"));
            bean.setFlgDefault(rs.getInt("flg_default"));
        }
    }
    
    /**
     * The type Tipo Autorizzazione extended row mapper.
     */
    public static class TipiAutorizzazioneExtendedRowMapper implements RowMapper<TipiAutorizzazioneExtendedDTO> {

        /**
         * Instantiates a new Tipo autorizzazione extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiAutorizzazioneExtendedRowMapper() throws SQLException {
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
        public TipiAutorizzazioneExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiAutorizzazioneExtendedDTO bean = new TipiAutorizzazioneExtendedDTO();
            populateBeanExtended(rs, bean);
            return bean;
        }

        private void populateBeanExtended(ResultSet rs, TipiAutorizzazioneExtendedDTO bean) throws SQLException {
            bean.setIdTipoAutorizza(rs.getLong("id_tipo_autorizza"));
            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
            bean.setCodTipoAutorizza(rs.getString("cod_tipo_autorizza"));
            bean.setDesTipoAutorizza(rs.getString("des_tipo_autorizza"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setOrdinaTipoAutorizza(rs.getLong("ordina_tipo_autorizza"));
            bean.setFlgDefault(rs.getInt("flg_default"));

        }

        private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("ambito_id"));
            bean.setCodAmbito(rs.getString("cod_ambito"));
            bean.setDesAmbito(rs.getString("des_ambito"));
        }
    }


}
