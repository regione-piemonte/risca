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

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiNaturaGiuridicaDAO;
import it.csi.risca.riscabesrv.dto.TipiNaturaGiuridicaDTO;
import it.csi.risca.riscabesrv.util.Constants;



/**
 * The type Tipo invio dao.
 *
 * @author CSI PIEMONTE
 */
public class TipiNaturaGiuridicaDAOImpl extends RiscaBeSrvGenericDAO<TipiNaturaGiuridicaDTO> implements TipiNaturaGiuridicaDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
    
    private static final String QUERY_TIPI_NATURA_GIURIDICA = "SELECT rdtng.* "
            + "FROM risca_d_tipo_natura_giuridica rdtng ";
    
    private static final String QUERY_TIPO_NATURA_GIURIDICA_BY_ID = QUERY_TIPI_NATURA_GIURIDICA
            + "where rdtng.id_tipo_natura_giuridica = :idTipoNaturaGiuridica ";
    
    private static final String QUERY_TIPO_NATURA_GIURIDICA_BY_COD = QUERY_TIPI_NATURA_GIURIDICA
            + "where rdtng.cod_tipo_natura_giuridica = :codTipoNaturaGiuridica ";

    private static final String ORDER_BY = "ORDER BY rdtng.ordina__natura_giuridica";

	@Override
	public List<TipiNaturaGiuridicaDTO> loadTipiNaturaGiuridica() throws Exception {
		LOGGER.debug("[TipiNaturaGiuridicaDAOImpl::loadTipiNaturaGiuridica] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<TipiNaturaGiuridicaDTO> listTipiNaturaGiuridica = new ArrayList<TipiNaturaGiuridicaDTO>();
        try {
			listTipiNaturaGiuridica = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_NATURA_GIURIDICA + ORDER_BY, null);
		}catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[TipiNaturaGiuridicaDAOImpl::loadTipiNaturaGiuridica] No record found in databas ", e);
		  return Collections.emptyList();
		}  catch (Exception e) {
			LOGGER.error("[TipiNaturaGiuridicaDAOImpl::loadTipiNaturaGiuridica] ERROR : " +e);
			throw new Exception(Constants.ERRORE_GENERICO);
		}
        LOGGER.debug("[TipiNaturaGiuridicaDAOImpl::loadTipiNaturaGiuridica] END");
        return listTipiNaturaGiuridica;
	}

	
	@Override
	public TipiNaturaGiuridicaDTO loadTipoNaturaGiuridicaByIdOrCod(String idTipoNaturaGiuridica) throws SQLException {
        LOGGER.debug("[TipiNaturaGiuridicaDAOImpl::loadTipoNaturaGiuridicaByIdOrCod] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            int id = 0;
            boolean bool = false;
            try {
            	id = Integer.parseInt(idTipoNaturaGiuridica);
            	bool = true;
            } catch (NumberFormatException nfe) {
                bool = false;
            }
       
        	if(bool) {
	            map.put("idTipoNaturaGiuridica", id);
	            MapSqlParameterSource params = getParameterValue(map);
	            return template.queryForObject(QUERY_TIPO_NATURA_GIURIDICA_BY_ID + ORDER_BY, params, getRowMapper());
	        	}
            else {
                map.put("codTipoNaturaGiuridica",idTipoNaturaGiuridica);
                MapSqlParameterSource params = getParameterValue(map);
                return template.queryForObject(QUERY_TIPO_NATURA_GIURIDICA_BY_COD + ORDER_BY, params, getRowMapper());
            }
        } catch (SQLException e) {
            LOGGER.error("[TipiNaturaGiuridicaDAOImpl::loadTipoNaturaGiuridicaByIdOrCod] Errore nell'esecuzione della query", e);
            throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[TipiNaturaGiuridicaDAOImpl::loadTipoNaturaGiuridicaByIdOrCod] Errore nell'accesso ai dati", e);
            throw e;
        } finally {
            LOGGER.debug("[TipiNaturaGiuridicaDAOImpl::loadTipoNaturaGiuridicaByIdOrCod] END");
        }
	}

	@Override
	public String getPrimaryKeySelect() {

		return null;
	}

	@Override
	public RowMapper<TipiNaturaGiuridicaDTO> getRowMapper() throws SQLException {
		 return new TipoNaturaGiuridicaRowMapper();
	}

    /**
     * The type Tipo invio row mapper.
     */
    public static class TipoNaturaGiuridicaRowMapper implements RowMapper<TipiNaturaGiuridicaDTO> {

        /**
         * Instantiates a new Tipo Natura Giuridica row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoNaturaGiuridicaRowMapper() throws SQLException {
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
        public TipiNaturaGiuridicaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiNaturaGiuridicaDTO bean = new TipiNaturaGiuridicaDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiNaturaGiuridicaDTO bean) throws SQLException {
            bean.setIdTipoNaturaGiuridica(rs.getLong("id_tipo_natura_giuridica"));
            bean.setCodTipoNaturaGiuridica(rs.getString("cod_tipo_natura_giuridica"));
            bean.setDesTipoNaturaGiuridica(rs.getString("des_tipo_natura_giuridica"));
            bean.setSiglaTipoNaturaGiuridica(rs.getString("sigla_tipo_natura_giuridica"));
            bean.setOrdinaNaturaGiuridica(rs.getLong("ordina__natura_giuridica"));
            bean.setFlgPubblico(rs.getString("flg_pubblico"));
        }
    }

	@Override
	public RowMapper<TipiNaturaGiuridicaDTO> getExtendedRowMapper() throws SQLException {

		return new TipiNaturaGiuridicaExtendedRowMapper();
	}
	
	
    /**
     * The type Tipo invio extended row mapper.
     */
    public static class TipiNaturaGiuridicaExtendedRowMapper implements RowMapper<TipiNaturaGiuridicaDTO> {

        /**
         * Instantiates a new Tipo invio extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiNaturaGiuridicaExtendedRowMapper() throws SQLException {
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
        public TipiNaturaGiuridicaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiNaturaGiuridicaDTO bean = new TipiNaturaGiuridicaDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiNaturaGiuridicaDTO bean) throws SQLException {
            bean.setIdTipoNaturaGiuridica(rs.getLong("id_tipo_natura_giuridica"));
            bean.setCodTipoNaturaGiuridica(rs.getString("cod_tipo_natura_giuridica"));
            bean.setDesTipoNaturaGiuridica(rs.getString("des_tipo_natura_giuridica"));
            bean.setSiglaTipoNaturaGiuridica(rs.getString("sigla_tipo_natura_giuridica"));
            bean.setOrdinaNaturaGiuridica(rs.getLong("ordina__natura_giuridica"));
            bean.setFlgPubblico(rs.getString("flg_pubblico"));
        }
    }


}
