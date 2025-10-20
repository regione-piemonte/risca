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
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.AreaCompetenzaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.AreaCompetenzaDTO;
import it.csi.risca.riscabesrv.dto.AreaCompetenzaExtendedDTO;
import it.csi.risca.riscabesrv.dto.GestAttoreDTO;
import it.csi.risca.riscabesrv.dto.TipoAreaCompetenzaDTO;

public class AreaCompetenzaDAOImpl extends RiscaBeSrvGenericDAO<AreaCompetenzaDTO> implements AreaCompetenzaDAO {
	
	
	private final String className = this.getClass().getSimpleName();
	
	private static final String QUERY_AREA_COMPETENZA_BY_COD_TIPO = "select raac.*,dac.*,da.*  from risca_r_ambito_area_competenza  raac "
			+ "	inner join risca_d_tipo_area_competenza  dac on raac.id_tipo_area_competenza = dac.id_tipo_area_competenza "
			+ "	inner join risca_d_ambito da  on raac.id_ambito = da.id_ambito"
			+ "	where dac.cod_tipo_area_competenza = :codTipoAreaCompetenza "
			+ "	and raac.id_ambito = :idAmbito";
	@Override
	public String getTableNameAString() {
		return null;
	}

	@Override
	public AreaCompetenzaExtendedDTO getAreaCompetenzaByCod(Long idAmbito, String codTipoAreaCompetenza) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName)); 
		AreaCompetenzaExtendedDTO areaCompetenza = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			map.put("codTipoAreaCompetenza", codTipoAreaCompetenza);
            MapSqlParameterSource params = getParameterValue(map);
            areaCompetenza=  template.queryForObject(QUERY_AREA_COMPETENZA_BY_COD_TIPO, params, getExtendedRowMapper());
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			return null;
				
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		}finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return areaCompetenza;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<AreaCompetenzaDTO> getRowMapper() throws SQLException {
		return null;
	}

	@Override
	public RowMapper<AreaCompetenzaExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new AreaCompetenzaExtendedRowMapper();
	}
	/**
	 * The type AreaCompetenzaExtendedRowMapper row mapper.
	 */
	public static class AreaCompetenzaExtendedRowMapper implements RowMapper<AreaCompetenzaExtendedDTO> {

		/**
		 * Instantiates a new AreaCompetenzaExtendedRowMapper row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public AreaCompetenzaExtendedRowMapper() throws SQLException {
			// Instantiate class
		}

		/**
		 * Implementations must implement this method to map each row of data in the
		 * ResultSet. This method should not call {@code next()} on the ResultSet; it is
		 * only supposed to map values of the current row.
		 *
		 * @param rs     the ResultSet to map (pre-initialized for the current row)
		 * @param rowNum the number of the current row
		 * @return the result object for the current row (may be {@code null})
		 * @throws SQLException if a SQLException is encountered getting column values
		 *                      (that is, there's no need to catch SQLException)
		 */
		@Override
		public AreaCompetenzaExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AreaCompetenzaExtendedDTO bean = new AreaCompetenzaExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AreaCompetenzaExtendedDTO bean) throws SQLException{
        	AmbitoDTO ambito = new AmbitoDTO();
        	popolaAmbito(rs, ambito);
        	bean.setAmbito(ambito);
        	
        	TipoAreaCompetenzaDTO tipoAreaCompetenzaDTO = new TipoAreaCompetenzaDTO();
        	popolaTipoAreaCompetenzaDTO(rs, tipoAreaCompetenzaDTO);
        	bean.setTipoAreaCompetenza(tipoAreaCompetenzaDTO);
        	
        	bean.setIdAmbito(rs.getInt("id_ambito"));
        	bean.setIdAmbitoAreaCompetenza(rs.getInt("id_ambito_area_competenza"));
        	bean.setChiaveAreaCompetenza(rs.getString("chiave_area_competenza"));
        	bean.setIdTipoAreaCompetenza(rs.getInt("id_tipo_area_competenza"));
        	popolaGuest(rs, bean);
        	

		}
		
		private void popolaTipoAreaCompetenzaDTO(ResultSet rs, TipoAreaCompetenzaDTO bean) throws SQLException {
			bean.setIdTipoAreaCompetenza(rs.getInt("id_tipo_area_competenza"));
			bean.setCodTipoAreaCompetenza(rs.getString("cod_tipo_area_competenza"));
			bean.setDesTipoAreaCompetenza(rs.getString("cod_tipo_area_competenza"));
        	popolaGuest(rs, bean);
			
		}
		private void popolaAmbito(ResultSet rs, AmbitoDTO ambito) throws SQLException {
			ambito.setIdAmbito(rs.getLong("id_ambito"));
			ambito.setCodAmbito(rs.getString("cod_ambito"));
			ambito.setDesAmbito(rs.getString("des_ambito"));
			
		}
		
		private void popolaGuest(ResultSet rs, GestAttoreDTO bean) throws SQLException {
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
		    bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
		    bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			
		}
	}
}
