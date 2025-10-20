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
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitoFonteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.AmbitiDAOImpl.AmbitiRowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.FonteDAOImpl.FonteRowMapper;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.AmbitoFonteDTO;
import it.csi.risca.riscabesrv.dto.AmbitoFonteExtendedDTO;
import it.csi.risca.riscabesrv.dto.FonteDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class AmbitoFonteDAOImpl extends RiscaBeSrvGenericDAO<AmbitoFonteDTO> implements AmbitoFonteDAO {

	    private final String className = this.getClass().getSimpleName();
	    
		public static final String QUERY_SELECT_AMBITO_FONTE_BY_COD_FONTE ="SELECT"
				+ "	rdf.*, rda.*"
				+ " FROM risca_r_ambito_fonte rraf "
				+ " INNER JOIN risca_d_fonte rdf on rraf.id_fonte = rdf.id_fonte "
				+ " INNER JOIN risca_d_ambito rda on rraf.id_ambito = rda.id_ambito "
				+ " WHERE rdf.cod_fonte = :codFonte";
	
	@Override
	public Long findIdAmbitoByIdFonte(Long idFonte) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		sql.append("select id_ambito from risca_r_ambito_fonte WHERE id_fonte = :ID_FONTE ");		

		Long idAmbito = null;

		paramMap.addValue("ID_FONTE", idFonte);
		
		try {
			
			idAmbito = template.queryForObject(sql.toString(), paramMap, Long.class);

		} catch (EmptyResultDataAccessException ex)
		{
			LOGGER.debug("[AmbitoFonteDAOImpl - findIdAmbitoByIdFonte] NESSUN RISULTATO");
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (RuntimeException ex) {
			LOGGER.error("[AmbitoFonteDAOImpl::findIdAmbitoByIdFonte] esecuzione query", ex);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return idAmbito;
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<AmbitoFonteDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<AmbitoFonteExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new AmbitoFonteExtendedRowMapper();
	}

	public static class AmbitoFonteExtendedRowMapper implements RowMapper<AmbitoFonteExtendedDTO> {

		public AmbitoFonteExtendedRowMapper() throws SQLException {}

		@Override
		public AmbitoFonteExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			AmbitoFonteExtendedDTO bean = new AmbitoFonteExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AmbitoFonteExtendedDTO bean) throws SQLException {
			AmbitiRowMapper ambitoRowMapper = new AmbitiRowMapper();
			AmbitoDTO ambitoDTO =  ambitoRowMapper.mapRow(rs, 0);
			bean.setAmbito(ambitoDTO);
			FonteRowMapper fonteRowMapper = new FonteRowMapper();
			FonteDTO fonteDTO = fonteRowMapper.mapRow(rs, 0);
			bean.setFonte(fonteDTO);;
		}
			
	}
	
	@Override
	public List<AmbitoFonteExtendedDTO> loadAmbitiFonteByCodFonte(String codFonte) throws Exception {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("codFonte", codFonte);
				MapSqlParameterSource params = getParameterValue(map);
				return template.query(getQuery(QUERY_SELECT_AMBITO_FONTE_BY_COD_FONTE, null, null), params,
						getExtendedRowMapper());
				 
			}catch (EmptyResultDataAccessException e) {
				LOGGER.debug(getClassFunctionDebugString(className, methodName, ":Data not found for codFonte: "+ codFonte));
          return Collections.emptyList();
			}catch (SQLException e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
				throw new Exception(e);
			} catch (DataAccessException e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
				throw new Exception(e);
			} finally {
				LOGGER.debug(getClassFunctionEndInfo(className, methodName));	
			}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}



}
