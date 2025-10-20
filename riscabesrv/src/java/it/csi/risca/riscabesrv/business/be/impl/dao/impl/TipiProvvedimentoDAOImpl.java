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
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiProvvedimentoDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.TipiProvvedimentoDTO;
import it.csi.risca.riscabesrv.dto.TipiProvvedimentoExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipo Provvedimento dao.
 *
 * @author CSI PIEMONTE
 */
public class TipiProvvedimentoDAOImpl extends RiscaBeSrvGenericDAO<TipiProvvedimentoDTO> implements TipiProvvedimentoDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String ID_AMBITO = "idAmbito";
	
	private static final String FLG_ISTANZA = "flgIstanza";
    
    private static final String QUERY_TIPI_PROVVEDIMENTO = "SELECT rdtp.*, rdtp.id_ambito AS ambito_id, rda.* "
            + "FROM risca_d_tipo_provvedimento rdtp "
            + "INNER JOIN risca_d_ambito rda ON rdtp.id_ambito = rda.id_ambito ";
    
    private static final String QUERY_TIPI_PROVVEDIMENTO_BY_ID_AMBITO_AND_FLG_ISTANZA = QUERY_TIPI_PROVVEDIMENTO
            + "where rdtp.id_ambito = :idAmbito and rdtp.flg_istanza = :flgIstanza ";
    
    private static final String QUERY_TIPI_PROVVEDIMENTO_BY_ID_TIPO_PROVVEDIMENTO_AND_BY_ID_AMBITO = QUERY_TIPI_PROVVEDIMENTO
            + "where rdtp.id_tipo_provvedimento = :idTipoProvvedimento and rdtp.id_ambito = :idAmbito";
    
    private static final String QUERY_TIPI_PROVVEDIMENTO_BY_CODE_TIPO_PROVVEDIMENTO_AND_BY_ID_AMBITO = QUERY_TIPI_PROVVEDIMENTO
            + "where UPPER(rdtp.cod_tipo_provvedimento) = UPPER(:codTipoProvvedimento) and rdtp.id_ambito = :idAmbito";
	
    
    private static final String QUERY_DATA_INIZIO_VALIDITA =" rdtp.data_inizio_validita >= :dataInizioValidita";
    
    private static final String QUERY_DATA_FINE_VALIDITA =" rdtp.data_fine_validita <= :dataFineValidita";
    
    private static final String QUERY_ORDER_BY =" ORDER BY rdtp.ordina_tipo_provv asc ";
    
	@Override
	public List<TipiProvvedimentoExtendedDTO> loadTipiProvvedimento() throws Exception {
		LOGGER.debug("[TipoProvvedimentoDAOImpl::loadTipiProvvedimento] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug("[TipoProvvedimentoDAOImpl::loadTipiProvvedimento] END");
        try {
			return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_PROVVEDIMENTO+QUERY_ORDER_BY, null);
		} catch (Exception e) {
            LOGGER.error("[TipoProvvedimentoDAOImpl::loadTipiProvvedimento] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		}
	}

	@Override
	public List<TipiProvvedimentoExtendedDTO> loadTipiProvvedimentoByIdAmbitoAndFlgIstanza(Long idAmbito, int flgIstanza, String dataIniVal,  String dataFineVal) throws ParseException {
		LOGGER.debug("[TipoProvvedimentoDAOImpl::loadTipiProvvedimentoByIdAmbitoAndFlgIstanza] BEGIN");
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        Map<String, Object> map = new HashMap<>();
        map.put(ID_AMBITO, idAmbito);
        map.put(FLG_ISTANZA, flgIstanza);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    	     if (dataIniVal  !=  null && dataFineVal !=  null) {
	 				Date dataIniValidita = formatter.parse(dataIniVal);
	 				Date dataFineValidita = formatter.parse(dataFineVal);
	 				map.put("dataInizioValidita", dataIniValidita);
	 				map.put("dataFineValidita", dataFineValidita);
	 				LOGGER.debug("[TipoProvvedimentoDAOImpl::loadTipiProvvedimentoByIdAmbitoAndFlgIstanza] BEGIN");
	 				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_PROVVEDIMENTO_BY_ID_AMBITO_AND_FLG_ISTANZA+ " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA+QUERY_ORDER_BY, map);
	    		 
    		 }
    	     else if (dataIniVal  !=  null && dataFineVal ==  null) {
	 				Date dataIniValidita = formatter.parse(dataIniVal);
	 				map.put("dataInizioValidita", dataIniValidita);
	 				LOGGER.debug("[TipoProvvedimentoDAOImpl::loadTipiProvvedimentoByIdAmbitoAndFlgIstanza] BEGIN");
	 				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_PROVVEDIMENTO_BY_ID_AMBITO_AND_FLG_ISTANZA+ " and "+ QUERY_DATA_INIZIO_VALIDITA +" and rdtp.data_fine_validita is null "+QUERY_ORDER_BY , map);
 	        	 
 	        	 }  
    	     else if (dataIniVal  ==  null && dataFineVal !=  null) {
 		 			Date dataFineValidita = formatter.parse(dataFineVal);
	 				map.put("dataFineValidita", dataFineValidita);
	 				LOGGER.debug("[TipoProvvedimentoDAOImpl::loadTipiProvvedimentoByIdAmbitoAndFlgIstanza] BEGIN");
	 				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_PROVVEDIMENTO_BY_ID_AMBITO_AND_FLG_ISTANZA+ " and "+ QUERY_DATA_FINE_VALIDITA+QUERY_ORDER_BY, map);
 	        	 
 	        	 }
 	        else {
 	           LOGGER.debug("[TipoProvvedimentoDAOImpl::loadTipiProvvedimentoByIdAmbitoAndFlgIstanza] BEGIN");
 	           return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_PROVVEDIMENTO_BY_ID_AMBITO_AND_FLG_ISTANZA+QUERY_ORDER_BY, map);
 	        }
    		 
    	}catch (ParseException e) {
   		 LOGGER.error("[TipoRiscossioneDAOImpl::loadTipiRiscossioneByIdAmbitoAndDateValidita] Errore nel parse ", e);
         throw e;
	    }
	}

	@Override
	public TipiProvvedimentoExtendedDTO loadTipoProvvedimentoByIdTipiProvvedimentoAndIdAmbito(Long idTipoProvvedimento, Long idAmbito) throws SQLException {		
        LOGGER.debug("[TipoProvvedimentoDAOImpl::loadTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("idTipoProvvedimento", idTipoProvvedimento);
            map.put(ID_AMBITO, idAmbito);
            MapSqlParameterSource params = getParameterValue(map);
            return template.queryForObject(QUERY_TIPI_PROVVEDIMENTO_BY_ID_TIPO_PROVVEDIMENTO_AND_BY_ID_AMBITO+QUERY_ORDER_BY, params, getExtendedRowMapper());
        } catch (SQLException e) {
            LOGGER.error("[TipoProvvedimentoDAOImpl::loadTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito] Errore nell'esecuzione della query", e);
            throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[TipoProvvedimentoDAOImpl::loadTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito] Errore nell'accesso ai dati", e);
          throw e;
        } finally {
            LOGGER.debug("[TipoProvvedimentoDAOImpl::loadTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito] END");
        }
	}

	@Override
	public TipiProvvedimentoExtendedDTO loadTipoProvvedimentoByCodeAndIdAmbito(String codOrIdTipoProvvedimento, Long idAmbito) throws SQLException {
        LOGGER.debug("[TipoProvvedimentoDAOImpl::loadTipoProvvedimentoByCodeAndIdAmbito] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put(ID_AMBITO, idAmbito);
            Long idTipoProvvedimento = 0L;
			boolean bool = false;
			try {
				idTipoProvvedimento = Long.parseLong(codOrIdTipoProvvedimento);
				bool = true;
			} catch (NumberFormatException nfe) {
				bool = false;
			}
			if (bool) {
				  map.put("idTipoProvvedimento", idTipoProvvedimento);
				MapSqlParameterSource params = getParameterValue(map);
				return template.queryForObject(QUERY_TIPI_PROVVEDIMENTO_BY_ID_TIPO_PROVVEDIMENTO_AND_BY_ID_AMBITO+QUERY_ORDER_BY, params,
						getExtendedRowMapper());

			} else {
		         map.put("codTipoProvvedimento",codOrIdTipoProvvedimento);
				MapSqlParameterSource params = getParameterValue(map);
				return template.queryForObject(QUERY_TIPI_PROVVEDIMENTO_BY_CODE_TIPO_PROVVEDIMENTO_AND_BY_ID_AMBITO+QUERY_ORDER_BY, params,
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
	public RowMapper<TipiProvvedimentoDTO> getRowMapper() throws SQLException {
		 return new TipiProvvedimentoRowMapper();
	}

	@Override
	public RowMapper<TipiProvvedimentoExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new TipiProvvedimentoExtendedRowMapper();
	}

    /**
     * The type Tipo Provvedimento row mapper.
     */
    public static class TipiProvvedimentoRowMapper implements RowMapper<TipiProvvedimentoDTO> {

        /**
         * Instantiates a new Tipo Provvedimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiProvvedimentoRowMapper() throws SQLException {
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
        public TipiProvvedimentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiProvvedimentoDTO bean = new TipiProvvedimentoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiProvvedimentoDTO bean) throws SQLException {
            bean.setIdTipoProvvedimento(rs.getLong("id_tipo_provvedimento"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setCodTipoProvvedimento(rs.getString("cod_tipo_provvedimento"));
            bean.setDesTipoProvvedimento(rs.getString("des_tipo_provvedimento"));
            bean.setFlgIstanza(rs.getString("flg_istanza"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setOrdinaTipoProvv(rs.getLong("ordina_tipo_provv"));
            bean.setFlgDefault(rs.getInt("flg_default"));
        }
    }
    
    /**
     * The type Tipo Provvedimento extended row mapper.
     */
    public static class TipiProvvedimentoExtendedRowMapper implements RowMapper<TipiProvvedimentoExtendedDTO> {

        /**
         * Instantiates a new Tipo provvedimentozione extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiProvvedimentoExtendedRowMapper() throws SQLException {
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
        public TipiProvvedimentoExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiProvvedimentoExtendedDTO bean = new TipiProvvedimentoExtendedDTO();
            populateBeanExtended(rs, bean);
            return bean;
        }

        private void populateBeanExtended(ResultSet rs, TipiProvvedimentoExtendedDTO bean) throws SQLException {
            bean.setIdTipoProvvedimento(rs.getLong("id_tipo_provvedimento"));
            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
            bean.setCodTipoProvvedimento(rs.getString("cod_tipo_provvedimento"));
            bean.setDesTipoProvvedimento(rs.getString("des_tipo_provvedimento"));
            bean.setFlgIstanza(rs.getString("flg_istanza"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setOrdinaTipoProvv(rs.getLong("ordina_tipo_provv"));
            bean.setFlgDefault(rs.getInt("flg_default"));

        }

        private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("ambito_id"));
            bean.setCodAmbito(rs.getString("cod_ambito"));
            bean.setDesAmbito(rs.getString("des_ambito"));
        }
    }


}
