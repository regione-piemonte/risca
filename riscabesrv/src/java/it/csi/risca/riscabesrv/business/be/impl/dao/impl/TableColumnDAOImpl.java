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

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TableColumnDAO;
import it.csi.risca.riscabesrv.dto.TableColumnDTO;

public class TableColumnDAOImpl extends RiscaBeSrvGenericDAO<TableColumnDTO> implements TableColumnDAO {

	private final String className = this.getClass().getSimpleName();
	
	public static final String QUERY_GET_FEILDS_TABLE_BY_NAME_TABLE = "SELECT \r\n"
			+ " column_name,"
			+ " data_type,"
			+ " character_maximum_length,"
			+ " numeric_precision,"
			+ " numeric_scale,"
			+ " CASE "
			+ "    WHEN data_type = 'numeric' AND CAST(numeric_precision AS INTEGER) > 1 THEN '9999999.99'"
			+ "    WHEN data_type = 'integer' THEN '2147483647'"
			+ "    WHEN data_type = 'numeric' AND CAST(numeric_precision AS INTEGER) = 1 THEN '1'"
			+ " END AS max_value"
			+ " FROM information_schema.columns"
			+ " WHERE table_name = :tableName";


	@Override
	public List<TableColumnDTO> findAllNameFieldForTable(String tableName) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			MapSqlParameterSource params = getParameterValue(map);
	
			return template.query(getQuery(QUERY_GET_FEILDS_TABLE_BY_NAME_TABLE, null, null), params,
					getRowMapper());
	
		} catch(EmptyResultDataAccessException e) {
			LOGGER.debug(getClassFunctionDebugString(className, methodName,"No record found in database for table Name "+ tableName ));
		    return Collections.emptyList();
		} catch (SQLException e) {
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
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<TableColumnDTO> getRowMapper() throws SQLException {
		return new TableColumnDTORowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}
    public static class  TableColumnDTORowMapper implements RowMapper<TableColumnDTO> {

		@Override
		public TableColumnDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			    String columnName = rs.getString("column_name");
	            String dataType = rs.getString("data_type");
	            Integer characterMaximumLength = rs.getInt("character_maximum_length");
	            Integer numericPrecision = rs.getInt("numeric_precision");
	            Integer numericScale = rs.getInt("numeric_scale");
	            String maxValue = rs.getString("max_value");
	            return new TableColumnDTO(columnName, dataType, characterMaximumLength,
	                    numericPrecision, numericScale, maxValue);
		}
    }
}
