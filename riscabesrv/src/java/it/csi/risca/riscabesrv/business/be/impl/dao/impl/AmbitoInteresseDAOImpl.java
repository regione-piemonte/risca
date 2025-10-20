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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitoInteresseDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.AmbitoInteresseDTO;
import it.csi.risca.riscabesrv.dto.AmbitoInteresseExtendedDTO;
import it.csi.risca.riscabesrv.dto.RataSdExtendedDTO;

/**
 * Ambito Interesse DAO Impl
 *
 * @author CSI PIEMONTE
 */
public class AmbitoInteresseDAOImpl extends RiscaBeSrvGenericDAO<AmbitoInteresseDTO> implements AmbitoInteresseDAO {


    
    private static final String QUERY_LOAD_AMBITO_INTERESSE_BY_TIPO_AND_DATE =  " SELECT * FROM risca_r_ambito_interesse "
			+ " WHERE tipo_interesse = :tipoInteresse "
			+"  AND  data_fine >= :dataIni "
			+"  AND data_inizio  <=  :dataFin "
	  		+ " AND id_ambito = :idAmbito"
	  		+ " ORDER BY data_inizio ";
  
    private static final String QUERY_LOAD_AMBITO_INTERESSE_BY_TIPO_AND_DATE_INIZIO =  " SELECT * FROM risca_r_ambito_interesse "
			+ " WHERE tipo_interesse = :tipoInteresse "
			+"  AND data_inizio  <=  :dataIni "
	  		+ " AND id_ambito = :idAmbito"
	  		+ " ORDER BY data_inizio ";
	  
	    private static final String QUERY_LOAD_AMBITO_INTERESSE_BY_TIPO_AND_DATE_FINE =  " SELECT * FROM risca_r_ambito_interesse "
				+ " WHERE tipo_interesse = :tipoInteresse "
				+"  AND  data_fine >= :dataFin "
		  		+ " AND id_ambito = :idAmbito"
		  		+ " ORDER BY data_inizio ";
	  
	@Override
	public List<AmbitoInteresseDTO> getAmbitoInteresseByDateAndTipo(Long idAmbito, String tipoInteresse,
			Date dataInizio, Date dataFine) throws  DataAccessException, SQLException {
		   LOGGER.debug("[AmbitoInteresseDAOImpl::getAmbitoInteresseByDateAndTipo] BEGIN");
	        Map<String, Object> map = new HashMap<>();
		  
	        map.put("dataIni", dataInizio);
	        map.put("dataFin", dataFine);
	        map.put("idAmbito", idAmbito);
	        map.put("tipoInteresse", tipoInteresse);
	        try {
	            MapSqlParameterSource params = new MapSqlParameterSource(map);
	            return	template.query(QUERY_LOAD_AMBITO_INTERESSE_BY_TIPO_AND_DATE, params, getRowMapper());
				
	        } catch(EmptyResultDataAccessException e) {
	            LOGGER.error(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateAndTipo] No record found in database for Tipo Elabora ", e);
			  return Collections.emptyList();
			}  catch (DataAccessException e) {
	            LOGGER.error(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateAndTipo] Errore nell'accesso ai dati", e);
	            throw e;
	        } catch (SQLException e) {
        	    LOGGER.error(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateAndTipo] Errore nella query ", e);
	             throw e;
			} finally {
	            LOGGER.debug(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateAndTipo] END");
	        }
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<AmbitoInteresseDTO> getRowMapper() throws SQLException {
       return new AmbitoInteresseRowMapper();
	}

	@Override
	public RowMapper<AmbitoInteresseExtendedDTO> getExtendedRowMapper() throws SQLException {
	     return new AmbitoInteresseExtendedRowMapper();
	}

    public static class AmbitoInteresseExtendedRowMapper implements RowMapper<AmbitoInteresseExtendedDTO> {

        /**
         * Instantiates a new Ambito Interesse Extended Row Mapper.
         *
         * @throws SQLException the sql exception
         */
        public AmbitoInteresseExtendedRowMapper() throws SQLException {
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
        public AmbitoInteresseExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AmbitoInteresseExtendedDTO bean = new AmbitoInteresseExtendedDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, AmbitoInteresseExtendedDTO bean) throws SQLException {

            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setIdAmbitoInteresse(rs.getLong("id_ambito_interesse"));
            bean.setTipoInteresse(rs.getString("tipo_interesse"));
            bean.setDataInizio(rs.getDate("data_inizio"));
            bean.setDataFine(rs.getDate("data_fine"));
            bean.setPercentuale(rs.getDouble("percentuale"));
            bean.setGiorniLegali(rs.getInt("giorni_legali"));
        }

        private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setCodAmbito(rs.getString("cod_ambito"));
            bean.setDesAmbito(rs.getString("des_ambito"));
        }
    }
    public static class AmbitoInteresseRowMapper implements RowMapper<AmbitoInteresseDTO> {

        /**
         * Instantiates a new Ambito Interesse  Row Mapper.
         *
         * @throws SQLException the sql exception
         */
        public AmbitoInteresseRowMapper() throws SQLException {
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
        public AmbitoInteresseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AmbitoInteresseDTO bean = new AmbitoInteresseDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, AmbitoInteresseDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setIdAmbitoInteresse(rs.getLong("id_ambito_interesse"));
            bean.setTipoInteresse(rs.getString("tipo_interesse"));
            bean.setDataInizio(rs.getDate("data_inizio"));
            bean.setDataFine(rs.getDate("data_fine"));
            bean.setPercentuale(rs.getDouble("percentuale"));
            bean.setGiorniLegali(rs.getInt("giorni_legali"));
        }
    }
	@Override
	public List<AmbitoInteresseDTO> getAmbitoInteresseByDateInizioAndTipo(Long idAmbito, String tipoInteresse,
			Date dataInizio) throws DataAccessException, SQLException {
		 LOGGER.debug("[AmbitoInteresseDAOImpl::getAmbitoInteresseByDateInizioAndTipo] BEGIN");
	        Map<String, Object> map = new HashMap<>();
		  
	        map.put("dataIni", dataInizio);
	        map.put("idAmbito", idAmbito);
	        map.put("tipoInteresse", tipoInteresse);
	        try {
	            MapSqlParameterSource params = new MapSqlParameterSource(map);
	            return	template.query(QUERY_LOAD_AMBITO_INTERESSE_BY_TIPO_AND_DATE_INIZIO, params, getRowMapper());
				
	        } catch(EmptyResultDataAccessException e) {
	            LOGGER.error(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateInizioAndTipo] No record found in database for Tipo Elabora ", e);
			  return Collections.emptyList();
			}  catch (DataAccessException e) {
	            LOGGER.error(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateInizioAndTipo] Errore nell'accesso ai dati", e);
	            throw e;
	        } catch (SQLException e) {
     	    LOGGER.error(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateInizioAndTipo] Errore nella query ", e);
	             throw e;
			} finally {
	            LOGGER.debug(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateInizioAndTipo] END");
	        }
	}

	@Override
	public List<AmbitoInteresseDTO> getAmbitoInteresseByDateFineAndTipo(Long idAmbito, String tipoInteresse,
			Date dataFine) throws DataAccessException, SQLException {
		 LOGGER.debug("[AmbitoInteresseDAOImpl::getAmbitoInteresseByDateInizioAndTipo] BEGIN");
	        Map<String, Object> map = new HashMap<>();
		  
	        map.put("dataFin", dataFine);
	        map.put("idAmbito", idAmbito);
	        map.put("tipoInteresse", tipoInteresse);
	        try {
	            MapSqlParameterSource params = new MapSqlParameterSource(map);
	            return	template.query(QUERY_LOAD_AMBITO_INTERESSE_BY_TIPO_AND_DATE_FINE, params, getRowMapper());
				
	        } catch(EmptyResultDataAccessException e) {
	            LOGGER.error(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateInizioAndTipo] No record found in database for Tipo Elabora ", e);
			  return Collections.emptyList();
			}  catch (DataAccessException e) {
	            LOGGER.error(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateInizioAndTipo] Errore nell'accesso ai dati", e);
	            throw e;
	        } catch (SQLException e) {
  	    LOGGER.error(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateInizioAndTipo] Errore nella query ", e);
	             throw e;
			} finally {
	            LOGGER.debug(" [AmbitoInteresseDAOImpl::getAmbitoInteresseByDateInizioAndTipo] END");
	        }
	}


	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}
