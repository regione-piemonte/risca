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
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiRiscossioneDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.TipoRiscossioneDTO;
import it.csi.risca.riscabesrv.dto.TipoRiscossioneExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipo riscossione dao.
 *
 * @author CSI PIEMONTE
 */
public class TipoRiscossioneDAOImpl extends RiscaBeSrvGenericDAO<TipoRiscossioneDTO> implements TipiRiscossioneDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String ID_AMBITO = "idAmbito";
    
    private static final String QUERY_TIPI_RISCOSSIONE = "SELECT rdtr.*, rdtr.id_ambito AS ambito_id, rda.* "
            + "FROM risca_d_tipo_riscossione rdtr "
            + "INNER JOIN risca_d_ambito rda ON rdtr.id_ambito = rda.id_ambito ";
    
    private static final String QUERY_TIPI_RISCOSSIONE_BY_ID_AMBITO = QUERY_TIPI_RISCOSSIONE
            + "where rdtr.id_ambito = :idAmbito ";
    
    private static final String QUERY_TIPI_RISCOSSIONE_BY_ID_TIPO_RISCOSSIONE_AND_BY_ID_AMBITO = QUERY_TIPI_RISCOSSIONE_BY_ID_AMBITO
            + "and rdtr.id_tipo_riscossione = :idTipoRiscossione ";
    
    private static final String QUERY_TIPI_RISCOSSIONE_BY_CODE_TIPO_RISCOSSIONE_AND_BY_ID_AMBITO = QUERY_TIPI_RISCOSSIONE_BY_ID_AMBITO
            + "and UPPER(rdtr.cod_tipo_riscossione) = UPPER(:codTipoRiscossione) ";
	
    private static final String QUERY_DATA_INIZIO_VALIDITA =" rdtr.data_inizio_validita >= :dataInizioValidita";
    
    private static final String QUERY_DATA_FINE_VALIDITA =" rdtr.data_fine_validita <= :dataFineValidita";
    
    private static final String QUERY_ORDER_BY =" ORDER BY rdtr.ordina_tipo_risco asc ";
    
	@Override
	public List<TipoRiscossioneExtendedDTO> loadTipiRiscossione() throws Exception {
		LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipiRiscossione] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipiRiscossione] END");
        try {
			return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_RISCOSSIONE+QUERY_ORDER_BY, null);
		} catch (Exception e) {
            LOGGER.error("[TipoRiscossioneDAOImpl::loadTipiRiscossione] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		}
	}

	@Override
	public List<TipoRiscossioneExtendedDTO> loadTipiRiscossioneByIdAmbitoAndDateValidita(Long idAmbito, String dataIniVal, String dataFineVal) throws ParseException {
		LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipiRiscossioneByIdAmbitoAndDateValidita] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        Map<String, Object> map = new HashMap<>();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        map.put(ID_AMBITO, idAmbito);
    	try {
    		 if (dataIniVal != null && dataFineVal != null ) {
				Date dataIniValidita = formatter.parse(dataIniVal);
				Date dataFineValidita = formatter.parse(dataFineVal);
				map.put("dataInizioValidita", dataIniValidita);
				map.put("dataFineValidita", dataFineValidita);
				LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipiRiscossioneByIdAmbitoAndDateValidita] END");
				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_RISCOSSIONE_BY_ID_AMBITO+ " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA +QUERY_ORDER_BY, map);
	        }
	        else  if (dataIniVal != null && dataFineVal == null ) {
				Date dataIniValidita = formatter.parse(dataIniVal);
				map.put("dataInizioValidita", dataIniValidita);
				LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipiRiscossioneByIdAmbitoAndDateValidita] END");
				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_RISCOSSIONE_BY_ID_AMBITO+ " and "+ QUERY_DATA_INIZIO_VALIDITA +" and rdtr.data_fine_validita is null " +QUERY_ORDER_BY, map);
			 }  
	        else if (dataIniVal == null && dataFineVal != null ) {
				Date dataFineValidita = formatter.parse(dataFineVal);
				map.put("dataFineValidita", dataFineValidita);
				LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipiRiscossioneByIdAmbitoAndDateValidita] END");
				return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_RISCOSSIONE_BY_ID_AMBITO+ " and "+ QUERY_DATA_FINE_VALIDITA+QUERY_ORDER_BY, map);
			  }
	        else {
	        	LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipiRiscossioneByIdAmbitoAndDateValidita] END");
	            return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_RISCOSSIONE_BY_ID_AMBITO+QUERY_ORDER_BY, map);
	        }
    	} catch (ParseException e) {
    		 LOGGER.error("[TipoRiscossioneDAOImpl::loadTipiRiscossioneByIdAmbitoAndDateValidita] Errore nel parse ", e);
            throw e;
		}

	}

	@Override
	public TipoRiscossioneExtendedDTO loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(String idOrCodTipoRiscossione, Long idAmbito) throws SQLException {		
		LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put(ID_AMBITO, idAmbito);
			Long id = 0L;
			boolean bool = false;
			try {
				id = Long.parseLong(idOrCodTipoRiscossione);
				bool = true;
			} catch (NumberFormatException nfe) {
				bool = false;
			}
			if (bool) {
				map.put("idTipoRiscossione", id);
				MapSqlParameterSource params = getParameterValue(map);
				LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] END");
				return template.queryForObject(QUERY_TIPI_RISCOSSIONE_BY_ID_TIPO_RISCOSSIONE_AND_BY_ID_AMBITO+QUERY_ORDER_BY, params,
						getExtendedRowMapper());

			} else {
				map.put("codTipoRiscossione", idOrCodTipoRiscossione);
				MapSqlParameterSource params = getParameterValue(map);
				LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] END");
				return template.queryForObject(QUERY_TIPI_RISCOSSIONE_BY_CODE_TIPO_RISCOSSIONE_AND_BY_ID_AMBITO+QUERY_ORDER_BY, params,
						getExtendedRowMapper());

			}

		} catch (SQLException e) {
			LOGGER.error(
					"[TipoRiscossioneDAOImpl::loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] Errore nell'esecuzione della query",
					e);
              throw e;
		} catch (DataAccessException e) {
			LOGGER.error(
					"[TipoRiscossioneDAOImpl::loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] Errore nell'accesso ai dati",
					e);
			throw e;
		} finally {
			LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] END");
		}
	}

	@Override
	public TipoRiscossioneExtendedDTO loadTipoRiscossioneByCodeAndIdAmbito(String codTipoRiscossione, Long idAmbito) throws SQLException {
        LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipoRiscossioneByCodeAndIdAmbito] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("codTipoRiscossione",codTipoRiscossione);
            map.put(ID_AMBITO, idAmbito);
            MapSqlParameterSource params = getParameterValue(map);
            return template.queryForObject(QUERY_TIPI_RISCOSSIONE_BY_CODE_TIPO_RISCOSSIONE_AND_BY_ID_AMBITO+QUERY_ORDER_BY, params, getExtendedRowMapper());
        } catch (SQLException e) {
            LOGGER.error("[TipoRiscossioneDAOImpl::loadTipoRiscossioneByCodeAndIdAmbito] Errore nell'esecuzione della query", e);
            throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[TipoRiscossioneDAOImpl::loadTipoRiscossioneByCodeAndIdAmbito] Errore nell'accesso ai dati", e);
            throw e;
        } finally {
            LOGGER.debug("[TipoRiscossioneDAOImpl::loadTipoRiscossioneByCodeAndIdAmbito] END");
        }
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<TipoRiscossioneDTO> getRowMapper() throws SQLException {
		 return new TipoRiscossioneRowMapper();
	}

	@Override
	public RowMapper<TipoRiscossioneExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new TipoRiscossioneExtendedRowMapper();
	}

    /**
     * The type Tipo riscossione row mapper.
     */
    public static class TipoRiscossioneRowMapper implements RowMapper<TipoRiscossioneDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoRiscossioneRowMapper() throws SQLException {
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
        public TipoRiscossioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipoRiscossioneDTO bean = new TipoRiscossioneDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipoRiscossioneDTO bean) throws SQLException {
            bean.setIdTipoRiscossione(rs.getLong("id_tipo_riscossione"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setCodTipoRiscossione(rs.getString("cod_tipo_riscossione"));
            bean.setDesTipoRiscossione(rs.getString("des_tipo_riscossione"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setOrdinaTipoRisco(rs.getLong("ordina_tipo_risco"));
            bean.setFlgDefault(rs.getInt("flg_default"));
        }
    }
    
    /**
     * The type Tipo riscossione extended row mapper.
     */
    public static class TipoRiscossioneExtendedRowMapper implements RowMapper<TipoRiscossioneExtendedDTO> {

        /**
         * Instantiates a new Tipo adempimento extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoRiscossioneExtendedRowMapper() throws SQLException {
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
        public TipoRiscossioneExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipoRiscossioneExtendedDTO bean = new TipoRiscossioneExtendedDTO();
            populateBeanExtended(rs, bean);
            return bean;
        }

        private void populateBeanExtended(ResultSet rs, TipoRiscossioneExtendedDTO bean) throws SQLException {
            bean.setIdTipoRiscossione(rs.getLong("id_tipo_riscossione"));
            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
            bean.setCodTipoRiscossione(rs.getString("cod_tipo_riscossione"));
            bean.setDesTipoRiscossione(rs.getString("des_tipo_riscossione"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setOrdinaTipoRisco(rs.getLong("ordina_tipo_risco"));
            bean.setFlgDefault(rs.getInt("flg_default"));
        }

        private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setCodAmbito(rs.getString("cod_ambito"));
            bean.setDesAmbito(rs.getString("des_ambito"));
        }
    }
}
