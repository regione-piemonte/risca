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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.ProfilazioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.OggettoAppDTO;
import it.csi.risca.riscabesrv.dto.ProfilazioneDTO;
import it.csi.risca.riscabesrv.dto.ProfiloOggAppDTO;
import it.csi.risca.riscabesrv.dto.ProfiloPaDTO;
import it.csi.risca.riscabesrv.dto.TipoOggettoAppDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class ProfilazioneDAOImpl  extends RiscaBeSrvGenericDAO<ProfilazioneDTO> implements ProfilazioneDAO {

	private static final String QUERY_LOAD_PROFILAZIONE = "SELECT  rrpoa.*, rdpp.* , rdoa.* , rdtoa.* FROM Risca_r_profilo_ogg_app rrpoa "
			+ "	inner join risca_d_profilo_pa rdpp on rrpoa.id_profilo_pa = rdpp.id_profilo_pa  "
			+ "	inner join Risca_d_oggetto_app rdoa on rrpoa.id_oggetto_app  = rdoa.id_oggetto_app  "
			+ "	inner join Risca_d_tipo_oggetto_app rdtoa on rdoa.id_tipo_oggetto_app = rdtoa.id_tipo_oggetto_app"  ;
	
	private static final String QUERY_LOAD_PROFILO_OGG_APP_BY_COD_PROFILO_PA = QUERY_LOAD_PROFILAZIONE +" where rdpp.cod_profilo_pa = :ruolo ";
	
	private static final String QUERY_LOAD_PROFILO_PA_BY_COD_PROFILO_PA ="	select distinct rdpp.* FROM Risca_r_profilo_ogg_app rrpoa "
			+ "	inner join risca_d_profilo_pa rdpp on rrpoa.id_profilo_pa = rdpp.id_profilo_pa "
			+ " where rdpp.cod_profilo_pa = :ruolo " ;

	
	@Override
	public ProfilazioneDTO loadProfilazione(String ruolo) throws Exception {
        LOGGER.debug("[ProfilazioneDAOImpl::loadProfilazione] BEGIN");
        List<ProfiloOggAppDTO> listProfiloOggAppDTO = new ArrayList<ProfiloOggAppDTO>();
        Map<String, Object> map = new HashMap<>();
        map.put("ruolo", ruolo);
        MapSqlParameterSource params = getParameterValue(map);
        ProfilazioneDTO ProfilazioneDTO = new ProfilazioneDTO();
        try {
        	ProfilazioneDTO = template.queryForObject(QUERY_LOAD_PROFILO_PA_BY_COD_PROFILO_PA, params, getRowMapper());
        	listProfiloOggAppDTO =  template.query(QUERY_LOAD_PROFILO_OGG_APP_BY_COD_PROFILO_PA, params, getRowMapperProfiloOggApp());
        	
        	ProfilazioneDTO.setProfiloOggettoApp(listProfiloOggAppDTO);
        } catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[ProfilazioneDAOImpl::loadProfilazione] No record found in database for role "+ruolo, e);
		  return ProfilazioneDTO;
		} catch (DataAccessException e) {
			LOGGER.error("[ProfilazioneDAOImpl::loadProfilazione] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
            LOGGER.error("[ProfilazioneDAOImpl::loadProfilazione] Errore nell'esecuzione della query", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		}
       LOGGER.debug("[ProfilazioneDAOImpl::loadProfilazione] END");
       return ProfilazioneDTO;
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<ProfilazioneDTO> getRowMapper() throws SQLException {
		return new ProfilazioneRowMapper();
	}
	
	
	public RowMapper<ProfiloOggAppDTO> getRowMapperProfiloOggApp() throws SQLException {
		return new ProfiloOggAppRowMapper();
	}
	
	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
    /**
     * The type ProfiloOggAppDTORowMapper row mapper.
     */
    public static class ProfiloOggAppRowMapper implements RowMapper<ProfiloOggAppDTO> {

        /**
         * Instantiates a new profilazione row mapper.
         *
         * @throws SQLException the sql exception
         */
        public ProfiloOggAppRowMapper() throws SQLException {
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
        public ProfiloOggAppDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

        	ProfiloOggAppDTO bean = new ProfiloOggAppDTO();
        	populateBeanProfiloOggAppDTO(rs, bean);
            return bean;
        }

        private void populateBeanProfiloOggAppDTO(ResultSet rs, ProfiloOggAppDTO profiloOggAppDTO) throws SQLException {
    		profiloOggAppDTO.setIdProfiloPa(rs.getLong("id_profilo_pa"));
    		OggettoAppDTO oggettoAppDTO = new OggettoAppDTO();
    		populateBeanOggettoAppDTO(rs, oggettoAppDTO);
    		profiloOggAppDTO.setOggettoApp(oggettoAppDTO);
    		profiloOggAppDTO.setFlgAttivo(rs.getBoolean("flg_attivo"));
    		
    	}
        private void populateBeanOggettoAppDTO(ResultSet rs, OggettoAppDTO oggettoAppDTO ) throws SQLException{
    		TipoOggettoAppDTO tipoOggettoAppDTO = new TipoOggettoAppDTO();
    		populateBeanTipoOggettoAppDTO(rs, tipoOggettoAppDTO);
    		oggettoAppDTO.setIdOggettoApp(rs.getLong("id_oggetto_app"));
    		oggettoAppDTO.setTipoOggettoAppDTO(tipoOggettoAppDTO);
    		oggettoAppDTO.setCodOggettoApp(rs.getString("cod_oggetto_app"));
    		oggettoAppDTO.setDesOggettoApp(rs.getString("des_oggetto_app"));
    		
    	}
        private void populateBeanTipoOggettoAppDTO(ResultSet rs, TipoOggettoAppDTO tipoOggettoAppDTO ) throws SQLException {
        	tipoOggettoAppDTO.setIdTipoOggettoApp(rs.getLong("id_tipo_oggetto_app"));
        	tipoOggettoAppDTO.setCodTipoOggettoApp(rs.getString("cod_tipo_oggetto_app"));
        	tipoOggettoAppDTO.setDesTipoOggettoApp(rs.getString("des_tipo_oggetto_app"));
    		
    	}
        
    }
    /**
     * The type profilazione row mapper.
     */
    public static class ProfilazioneRowMapper implements RowMapper<ProfilazioneDTO> {

        /**
         * Instantiates a new profilazione row mapper.
         *
         * @throws SQLException the sql exception
         */
        public ProfilazioneRowMapper() throws SQLException {
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
        public ProfilazioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ProfilazioneDTO bean = new ProfilazioneDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, ProfilazioneDTO bean) throws SQLException {
        	ProfiloPaDTO profiloPaDTO = new ProfiloPaDTO();
        	populateBeanProfiloPaDTO(rs ,profiloPaDTO);
            bean.setProfiloPa(profiloPaDTO);
            
        	
        }
        
        private void populateBeanProfiloPaDTO(ResultSet rs, ProfiloPaDTO profiloPaDTO) throws SQLException {
    		profiloPaDTO.setIdProfiloPa(rs.getLong("id_profilo_pa"));
    		profiloPaDTO.setCodProfiloPa(rs.getString("cod_profilo_pa"));
    		profiloPaDTO.setDesProfiloPa(rs.getString("des_profilo_pa"));
    	}
        
    }
}
