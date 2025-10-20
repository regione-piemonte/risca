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

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.TipoMessaggioDTO;

/**
 * The type Messaggi dao.
 *
 * @author CSI PIEMONTE
 */
public class MessaggiDAOImpl extends RiscaBeSrvGenericDAO<MessaggiDTO> implements MessaggiDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String COD_MESSAGGIO = "codMessaggio";
	
	private static final String ID_TIPO_MESSAGGIO = "idTipoMessaggio";
	
	private static final String ID_MESSAGGIO = "idMessaggio";
	
	private static final String ORDER_BY = "ORDER BY id_messaggio ";
	
    private static final String QUERY_MESSAGGI = "SELECT rdm.*, rdtm.* FROM risca_d_messaggio rdm  "
    		+ "INNER JOIN risca_d_tipo_messaggio rdtm ON rdm.id_tipo_messaggio = rdtm.id_tipo_messaggio ";
   
    
    private static final String QUERY_MESSAGGI_BY_COD_MESSAGGIO = QUERY_MESSAGGI
            + "where rdm.cod_messaggio = :codMessaggio ";
    
    private static final String QUERY_MESSAGGI_BY_ID_TIPO_MESSAGGIO = QUERY_MESSAGGI
            + "where rdtm.cod_tipo_messaggio = :codTipoMessaggio "
            + ORDER_BY;
    
    private static final String QUERY_MESSAGGI_BY_ID_MESSAGGIO = QUERY_MESSAGGI
            + "where rdm.id_messaggio = :idMessaggio ";
   
	@Override
	public List<MessaggiDTO> loadMessaggi() {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug("[MessaggiDAOImpl::loadMessaggi] BEGIN");
		List<MessaggiDTO> listMessaggi = new ArrayList<MessaggiDTO>();
		try {
			listMessaggi = findListByQuery(CLASSNAME, methodName, QUERY_MESSAGGI, null);
        return listMessaggi;
        
	    } catch (DataAccessException e) {
	         LOGGER.error("[MessaggiDAOImpl::loadMessaggi] Errore nell'accesso ai dati", e);
	         return listMessaggi;
	    } finally {
	         LOGGER.debug("[MessaggiDAOImpl::loadMessaggi] END");
	    }
	}
    
	
	@Override
	public MessaggiDTO loadMessaggiByCodMessaggio(String codMessaggio) throws SQLException {

		LOGGER.debug("[MessaggiDAOImpl::loadMessaggiByCodMessaggio] BEGIN");
		try {
        Map<String, Object> map = new HashMap<>();
        map.put(COD_MESSAGGIO, codMessaggio);
        MapSqlParameterSource params = getParameterValue(map);     
        return template.queryForObject(QUERY_MESSAGGI_BY_COD_MESSAGGIO, params, getRowMapper());
        
	    } catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[MessaggiDAOImpl::loadMessaggiByCodMessaggio] No record found in database for codMessaggio "+ codMessaggio, e);
		   return null;
		}catch (SQLException e) {
	         LOGGER.error("[MessaggiDAOImpl::loadMessaggiByCodMessaggio] Errore nell'esecuzione della query", e);
	         throw e;
	    } catch (DataAccessException e) {
	         LOGGER.error("[MessaggiDAOImpl::loadMessaggiByCodMessaggio] Errore nell'accesso ai dati", e);
	         throw e;
	    } finally {
	         LOGGER.debug("[MessaggiDAOImpl::loadMessaggiByCodMessaggio] END");
	    }
	}


	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<MessaggiDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new MessaggiRowMapper();
	}


    /**
     * The type Messaggio row mapper.
     */
    public static class MessaggiRowMapper implements RowMapper<MessaggiDTO> {

        /**
         * Instantiates a new Messaggio row mapper.
         *
         * @throws SQLException the sql exception
         */
        public MessaggiRowMapper() throws SQLException {
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
        public MessaggiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            MessaggiDTO bean = new MessaggiDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, MessaggiDTO bean) throws SQLException {
            bean.setIdMessaggio(rs.getLong("id_messaggio"));
            TipoMessaggioDTO TipoMessaggioDTO = new TipoMessaggioDTO();
            populateBeanTipoMessaggioDTO(rs, TipoMessaggioDTO);
            bean.setTipoMessaggio(TipoMessaggioDTO);
            bean.setCodMessaggio(rs.getString("cod_messaggio"));
            bean.setDesTestoMessaggio(rs.getString("des_testo_messaggio"));
        }
        private void populateBeanTipoMessaggioDTO(ResultSet rs, TipoMessaggioDTO bean) throws SQLException {
            bean.setIdTipoMessaggio(rs.getLong("id_tipo_messaggio"));
            bean.setCodTipoMessaggio(rs.getString("cod_tipo_messaggio"));
            bean.setDesTipoMessaggio(rs.getString("des_tipo_messaggio"));
        }
    }


	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new MessaggiExtendedRowMapper();
	}
	
    /**
     * The type Messaggio extended row mapper.
     */
    public static class MessaggiExtendedRowMapper implements RowMapper<MessaggiDTO> {

        /**
         * Instantiates a new Messaggio row mapper.
         *
         * @throws SQLException the sql exception
         */
        public MessaggiExtendedRowMapper() throws SQLException {
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
        public MessaggiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            MessaggiDTO bean = new MessaggiDTO();
            populateBean(rs, bean);
            return bean;
        }


        private void populateBean(ResultSet rs, MessaggiDTO bean) throws SQLException {
            bean.setIdMessaggio(rs.getLong("id_messaggio"));
            TipoMessaggioDTO TipoMessaggioDTO = new TipoMessaggioDTO();
            populateBeanTipoMessaggioDTO(rs, TipoMessaggioDTO);
            bean.setTipoMessaggio(TipoMessaggioDTO);
            bean.setCodMessaggio(rs.getString("cod_messaggio"));
            bean.setDesTestoMessaggio(rs.getString("des_testo_messaggio"));
        }
        private void populateBeanTipoMessaggioDTO(ResultSet rs, TipoMessaggioDTO bean) throws SQLException {
            bean.setIdTipoMessaggio(rs.getLong("id_tipo_messaggio"));
            bean.setCodTipoMessaggio(rs.getString("cod_tipo_messaggio"));
            bean.setDesTipoMessaggio(rs.getString("des_tipo_messaggio"));
        }
    }


}
