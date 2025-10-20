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

import it.csi.risca.riscabesrv.business.be.impl.dao.ProvinceDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.NazioniDTO;
import it.csi.risca.riscabesrv.dto.ProvinceExtendedDTO;
import it.csi.risca.riscabesrv.dto.ProvinciaDTO;
import it.csi.risca.riscabesrv.dto.RegioneExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Province dao.
 *
 * @author CSI PIEMONTE
 */
public class ProvinceDAOImpl extends RiscaBeSrvGenericDAO<ProvinciaDTO> implements ProvinceDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String COD_ISTAT_COMUNE = "codIstatComune";
	
	private static final String COD_REGIONE = "codRegione";
	
	private static final String ORDER_BY = "ORDER BY sigla_provincia ";

    private static final String WHERE_QUERY_PROVINCE = "WHERE rdp.data_fine_validita is null ";
	
    private static final String QUERY_PROVINCE = "SELECT distinct rdp.*, rdr.*, rdn.* FROM risca_d_provincia rdp  "
    		+ "INNER JOIN risca_d_regione rdr ON rdp.id_regione = rdr.id_regione "
    		+ "INNER JOIN risca_d_nazione rdn ON rdr.id_nazione = rdn.id_nazione "
    		+ "INNER JOIN risca_d_comune rdc ON rdp.id_provincia = rdc.id_provincia ";
   
    
    private static final String QUERY_PROVINCE_BY_COD_ISTAT_COMUNE = QUERY_PROVINCE
            + "where rdc.cod_istat_comune = :codIstatComune "
            + ORDER_BY;
    
    private static final String QUERY_PROVINCE_BY_ID_REGIONE = QUERY_PROVINCE
            + "where rdr.id_regione = :idRegione "
            + ORDER_BY;
    
    private static final String QUERY_PROVINCE_BY_COD_REGIONE = QUERY_PROVINCE
            + "where rdr.cod_regione = :codRegione "
            + ORDER_BY;
   
    private static final String QUERY_PROVINCE_BY_ID_COD_REGIONE_AND_ID_COD_PROVINCIA = QUERY_PROVINCE + WHERE_QUERY_PROVINCE
            + " AND (rdp.id_provincia = :idProvincia OR rdp.cod_provincia = :codProvincia)"
            + "AND (rdr.id_regione = :idRegione OR rdr.cod_regione = :codRegione) "
            + ORDER_BY;
    
	@Override
	public List<ProvinceExtendedDTO> loadProvince(boolean attivo) throws Exception {
		LOGGER.debug("[ProvinceDAOImpl::loadProvince] BEGIN ");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
			if(attivo) {
				LOGGER.debug("[ProvinceDAOImpl::loadProvince] END (attivo true) ");
				  return findListByQuery(CLASSNAME, methodName, QUERY_PROVINCE + WHERE_QUERY_PROVINCE + ORDER_BY, null);
			}else {
				  LOGGER.debug("[ProvinceDAOImpl::loadProvince] END (attivo false) ");
				  return findListByQuery(CLASSNAME, methodName, QUERY_PROVINCE + ORDER_BY, null);
			}
		} catch (Exception e) {
            LOGGER.error("[ProvinceDAOImpl::loadProvince] Errore nell'esecuzione della query", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		}
      
	}

	@Override
	public List<ProvinceExtendedDTO> loadProvinceByCodIstatComune(String codIstatComune) throws Exception {
		LOGGER.debug("[ProvinceDAOImpl::loadProvinceByCodIstatComune] BEGIN ");
        try {
			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			Map<String, Object> map = new HashMap<>();
			map.put(COD_ISTAT_COMUNE, codIstatComune);
			LOGGER.debug("[ProvinceDAOImpl::loadProvinceByCodIstatComune] END ");
			return findListByQuery(CLASSNAME, methodName, QUERY_PROVINCE_BY_COD_ISTAT_COMUNE, map);
		} catch (Exception e) {
            LOGGER.error("[ProvinceDAOImpl::loadProvinceByCodIstatComune] Errore nell'esecuzione della query", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		}
	}

	@Override
	public List<ProvinceExtendedDTO> loadProvinceByCodRegione(String codRegione) throws Exception {
		LOGGER.debug("[ProvinceDAOImpl::loadProvinceByCodRegione] BEGIN ");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
			Map<String, Object> map = new HashMap<>();
			int idReg = 0;
			boolean bool = false;
			try {
				idReg = Integer.parseInt(codRegione);
				bool = true;
			} catch (NumberFormatException nfe) {
			    bool = false;
			}
   
			if(bool) {
			    map.put("idRegione", idReg);
			    LOGGER.debug("[ProvinceDAOImpl::loadProvinceByCodRegione] END (query con id regione) ");
			    return findListByQuery(CLASSNAME, methodName, QUERY_PROVINCE_BY_ID_REGIONE, map);
			}else {
			    map.put(COD_REGIONE, codRegione);
			    LOGGER.debug("[ProvinceDAOImpl::loadProvinceByCodRegione] END (query con cod regione) ");
			    return findListByQuery(CLASSNAME, methodName, QUERY_PROVINCE_BY_COD_REGIONE, map);
			}
		} catch (Exception e) {
            LOGGER.error("[ProvinceDAOImpl::loadProvinceByCodRegione] Errore nell'esecuzione della query", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		}

	}
	
	@Override
	public ProvinceExtendedDTO loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia(String codOrIdRegione,
			String codOrIdProvincia) throws Exception {
			LOGGER.debug("[ProvinceDAOImpl::loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia] BEGIN ");
        	try {
				final Long idRegione =  Long.parseLong(codOrIdRegione);;
				final String codRegione = codOrIdRegione; 
				final Long idProvincia = Long.parseLong(codOrIdProvincia);
				final String codProvincia = codOrIdProvincia; 
				try {
					LOGGER.debug("[ProvinceDAOImpl::loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia] END ");
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("idProvincia", idProvincia);
					parameters.put("codProvincia", codProvincia);
					parameters.put("idRegione", idRegione);
					parameters.put("codRegione", codRegione);

					return (ProvinceExtendedDTO) template.queryForObject(QUERY_PROVINCE_BY_ID_COD_REGIONE_AND_ID_COD_PROVINCIA, parameters, 
							getExtendedRowMapper());
				} catch (DataAccessException e) {
				    LOGGER.error("[ProvinceDAOImpl::loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia] Errore nell'accesso ai dati", e);
					throw new Exception(Constants.ERRORE_GENERICO);
				} catch (SQLException e) {
				    LOGGER.error("[ProvinceDAOImpl::loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia] Errore nell'esecuzione della query", e);
					throw new Exception(Constants.ERRORE_GENERICO);
				}
			} catch (NumberFormatException e) {
	            LOGGER.error("[ProvinceDAOImpl::loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia] Errore nell'esecuzione della query", e);
				throw new Exception(Constants.ERRORE_GENERICO);
			}
	}


	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<ProvinciaDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new ProvinceRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new ProvinceExtendedRowMapper();
	}

    /**
     * The type Provincia row mapper.
     */
    public static class ProvinceRowMapper implements RowMapper<ProvinciaDTO> {

        /**
         * Instantiates a new Provincia row mapper.
         *
         * @throws SQLException the sql exception
         */
        public ProvinceRowMapper() throws SQLException {
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
        public ProvinciaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProvinciaDTO bean = new ProvinciaDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, ProvinciaDTO bean) throws SQLException {
            bean.setIdProvincia(rs.getLong("id_provincia"));
            bean.setCodProvincia(rs.getString("cod_provincia"));
            bean.setDenomProvincia(rs.getString("denom_provincia"));
            bean.setSiglaProvincia(rs.getString("sigla_provincia"));
            bean.setIdRegione(rs.getLong("id_regione"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
        }
    }
    
    /**
     * The type Provincia extended row mapper.
     */
    public static class ProvinceExtendedRowMapper implements RowMapper<ProvinceExtendedDTO> {

        /**
         * Instantiates a new Provincia extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public ProvinceExtendedRowMapper() throws SQLException {
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
        public ProvinceExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ProvinceExtendedDTO bean = new ProvinceExtendedDTO();
            populateBeanExtended(rs, bean);
            return bean;
        }

        private void populateBeanExtended(ResultSet rs, ProvinceExtendedDTO bean) throws SQLException {
            bean.setIdProvincia(rs.getLong("id_provincia"));
            bean.setCodProvincia(rs.getString("cod_provincia"));
            bean.setDenomProvincia(rs.getString("denom_provincia"));
            bean.setSiglaProvincia(rs.getString("sigla_provincia"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
      
            RegioneExtendedDTO regione = new RegioneExtendedDTO();
            populateBeanRegione(rs, regione);
            bean.setRegione(regione);

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
