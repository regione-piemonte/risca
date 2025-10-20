/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper.LongRowMapper;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.hashing.HashingUtil;

/**
 * The type Risca be srv generic dao.
 *
 * @param <T> the type parameter
 * @author CSI PIEMONTE
 */
@Component
public abstract class RiscaBeSrvGenericDAO<T> {

    /**
     * The constant LOGGER.
     */
    protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME + ".dao");

    /**
     * The Template.
     */
    protected NamedParameterJdbcTemplate template;
    /**
     * The Table name.
     */
    protected String tableName;
    /**
     * The constant ISNOTNULL.
     */
    public static final String ISNOTNULL = "ROW_ISNOT_NULL";
    /**
     * The constant ISNULL.
     */
    public static final String ISNULL = "ROW_IS_NULL";
    /**
     * The constant TABLE_NAME.
     */
    public static final String TABLE_NAME = "";


    /**
     * Constructor for a standard table (without auto-incremented column)
     */
    protected RiscaBeSrvGenericDAO() {
        super();
    }

    /**
     * Sets template.
     *
     * @param template the template
     */
    public void setTemplate(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Sets table name.
     *
     * @param tableName the table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets table name.
     *
     * @return the table name
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Gets parameter value.
     *
     * @param filter the filter
     * @return the parameter value
     */
    protected MapSqlParameterSource getParameterValue(Map<String, Object> filter) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        for (String key : filter.keySet()) {
            Object value = filter.get(key);

            if (value != null) {
                if ((value instanceof Long) || (value instanceof Integer) || (value instanceof Float) || (value instanceof Double) || (value instanceof java.math.BigDecimal)) {
                    params.addValue(key, value, Types.NUMERIC);
                } else if (value instanceof java.util.Date) {
                    params.addValue(key, value, Types.TIMESTAMP);
                } else if (value instanceof Boolean) {
                    params.addValue(key, value, Types.BOOLEAN);
                } else if (value instanceof String) {
                    params.addValue(key, value, Types.VARCHAR);
                } else {
                    params.addValue(key, value);
                }
            } else {
                params.addValue(key, value, Types.NULL);
            }
        }
        return params;
    }

    /**
     * Find all list.
     *
     * @return the list
     * @throws DataAccessException the data access exception
     * @throws SQLException        the sql exception
     */
    public List<T> findAll() throws DataAccessException, SQLException {
        try {
            String query = "select * from " + getTableName();
            MapSqlParameterSource params = new MapSqlParameterSource();
            return template.query(query, params, getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("[RiscaBeSrvGenericDAO<T>::findAll] ", e);
            return null;
        }
    }

    
    


    /**
     * Find list by query e.
     *
     * @param <E>        the type parameter
     * @param className  the class name
     * @param methodName the method name
     * @param query      the query
     * @param filter     the filter
     * @return the e
     */
    public <E> E findListByQuery(String className, String methodName, String query, Map<String, Object> filter) {
        return this.findListByQuery(className, methodName, query, filter, null, null, Boolean.FALSE);
    }

    public <E> E findSimpleDTOListByQuery(String className, String methodName, String query, Map<String, Object> filter) {
        return this.findListByQuery(className, methodName, query, filter, null, null, Boolean.TRUE);
    }


    public <E> E findListLongByQuery(String className, String methodName, String query, Map<String, Object> filter, String fieldName) {
        return this.findListLongByQuery(className, methodName, query, filter, null, null, fieldName);
    }

    /**
     * Find list by query e.
     *
     * @param <E>        the type parameter
     * @param className  the class name
     * @param methodName the method name
     * @param query      the query
     * @param filter     the filter
     * @param offset     the offset
     * @param limit      the limit
     * @return the e
     */
    @SuppressWarnings("unchecked")
    public <E> E findListByQuery(String className, String methodName, String query, Map<String, Object> filter, String offset, String limit, Boolean isSimpleDTO) {
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        E result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (filter != null && filter.size() > 0) {
            params = getParameterValue(filter);
        }
        try {
            result = isSimpleDTO ? (E) template.query(getQuery(query, offset, limit), params, getRowMapper()) : (E) template.query(getQuery(query, offset, limit), params, getExtendedRowMapper());
        } catch (Exception e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <E> E findListLongByQuery(String className, String methodName, String query, Map<String, Object> filter, String offset, String limit, String fieldName) {
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        E result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (filter != null && filter.size() > 0) {
            params = getParameterValue(filter);
        }
        try {
            result = (E) template.query(getQuery(query, offset, limit), params, getLongRowMapper(fieldName));
        } catch (Exception e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return result;
    }


    /**
     * Find by pk t.
     *
     * @param filter the filter
     * @return the t
     * @throws DataAccessException the data access exception
     * @throws SQLException        the sql exception
     */
    public T findByPK(Map<String, Object> filter) throws DataAccessException, SQLException {
        try {
            String query = getPrimaryKeySelect();
            MapSqlParameterSource params = getParameterValue(filter);
            return template.queryForObject(query, params, getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("[RiscaBeSrvGenericDAO<T>::findByPK] ", e);
            return null;
        }
    }

    /**
     * Find by sequence name t.
     *
     * @param sequenceName the name of the sequence
     * @return the Long
     * @throws DataAccessException the data access exception
     * @throws SQLException        the sql exception
     */
    public Long findNextSequenceValue(String sequenceName) throws DataAccessException, SQLException {
        try {
            String query = "SELECT nextval('" + sequenceName + "')";
            return template.queryForObject(query, new HashMap<>(), Long.class);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("[findNextSequenceValue<T>::findByPK] ", e);
            return null;
        }
    }

    /**
     * Gets query.
     *
     * @param query  the query
     * @param offset the offset
     * @param limit  the limit
     * @return the query
     */
    public String getQuery(String query, String offset, String limit) {
        String q = query.replaceAll("_replaceTableName_", getTableName());
        StringBuilder sb = new StringBuilder(q);
        LOGGER.debug("[RiscaBeSrvGenericDAO::getQuery] INPUT PARAMS : [query] : " + query + " - [offset] : " + offset + " - [limit] : " + limit);

        if (null != offset && null != limit) {
            Integer calcOffset = Integer.parseInt(offset) <= 1 ? 0 : ((Integer.parseInt(offset) - 1) * Integer.parseInt(limit));
            LOGGER.debug("[RiscaBeSrvGenericDAO::getQuery] [calcOffset] : " + calcOffset );

            sb.append(" offset ").append(calcOffset);
        }
        if (null != limit) {
            sb.append(" limit ").append(limit);
        }
        return sb.toString();
    }


    /**
     * Returns the SQL SELECT REQUEST to be used to retrieve the bean data from
     * the database
     *
     * @return String primary key select
     */
    public abstract String getPrimaryKeySelect();

    /**
     * Returns a RowMapper for a new bean instance
     *
     * @return RowMapper<T>  row mapper
     * @throws SQLException the sql exception
     */
    public abstract RowMapper<T> getRowMapper() throws SQLException;

    /**
     * Returns a RowMapper for a new bean instance
     *
     * @return RowMapper<T>  extended row mapper
     * @throws SQLException the sql exception
     */
    public abstract RowMapper<?> getExtendedRowMapper() throws SQLException;

    /**
     * Gets long row mapper.
     *
     * @return the long row mapper
     * @throws SQLException the sql exception
     */
    public RowMapper<Long> getLongRowMapper(String fieldName) throws SQLException {
        return new LongRowMapper(fieldName);
    }

    /**
     * Generate gest uid string.
     *
     * @param stringToHash the string to hash
     * @return the string
     */
    protected String generateGestUID(String stringToHash) {
        try {
            return HashingUtil.encodeSH3(stringToHash);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Errore nella creazione del UID: Algoritmo non valido", e);
            return String.valueOf(this.hashCode());
        } catch (Exception ex) {
            LOGGER.error("Errore nella creazione del UID", ex);
            return String.valueOf(this.hashCode());
        }
    }

    /**
     * Gets query sorting segment.
     *
     * @param sort the sort
     * @return the query sorting segment
     */
    protected String getQuerySortingSegment(String sort) {
        String ASCENDING = " ASC ";
        String DESCENDING = " DESC ";
        char ORDER = '-';
        String sortField = null;
        String sortOrder = null;

        StringBuilder sql = new StringBuilder();
        if (sort != null && !sort.isEmpty()) {
            sortOrder = sort.charAt(0) == ORDER ? DESCENDING : ASCENDING;
            sortField = sortOrder.equals(DESCENDING) ? sort.substring(1) : sort;
            if (!sortField.isEmpty()) {
                sql.append(" ORDER BY ");
                sql.append(sortField).append(" ").append(sortOrder);
            }
        }
        return sql.toString();
    }

    /**
     * Gets class function begin info.
     *
     * @param className  the class name
     * @param methodName the method name
     * @return the class function begin info
     */
    protected String getClassFunctionBeginInfo(String className, String methodName) {
        return getClassFunctionDebugString(className, methodName, "BEGIN");
    }

    /**
     * Gets class function end info.
     *
     * @param className  the class name
     * @param methodName the method name
     * @return the class function end info
     */
    protected String getClassFunctionEndInfo(String className, String methodName) {
        return getClassFunctionDebugString(className, methodName, "END");
    }

    /**
     * Gets class function error info.
     *
     * @param className  the class name
     * @param methodName the method name
     * @param error      the error
     * @return the class function error info
     */
    protected String getClassFunctionErrorInfo(String className, String methodName, Object error) {
        return getClassFunctionDebugString(className, methodName, "ERROR : " + error);
    }

    /**
     * Gets class function debug string.
     *
     * @param className  the class name
     * @param methodName the method name
     * @param info       the info
     * @return the class function debug string
     */
    protected String getClassFunctionDebugString(String className, String methodName, String info) {
        String functionIdentity = "[" + className + "::" + methodName + "] ";
        return functionIdentity + info;
    }

    protected HashMap<String, String> getInvalidMandatoryFields(String containerObjectName, Object ... fields) {
    	HashMap<String, String> fieldsMap = new HashMap<String, String>();
    	
    	for(int j=0; j<fields.length; j+=2) {
    		Object field = fields[j+1];
    		String fieldId = (String)fields[j];
    		
    		if(field == null 
    				|| (field instanceof String && StringUtils.isBlank((String)field))
    				|| (field instanceof Boolean && !(Boolean)field) )
    			fieldsMap.put(containerObjectName + fieldId, "campo obbligatorio"); 

    	}

    	return fieldsMap;
    }
    
    protected void validateMandatoryFields(Object ... fields) {
    	HashMap<String, String> fieldsMap = getInvalidMandatoryFields("", fields);
    	
    	if(!fieldsMap.isEmpty())
    		throw new BusinessException(400, "E001", "errore validazione dati di ingresso", fieldsMap);
    }

    public Long decodeId(String fromTableName, String searchFieldCriteris) {
		try {
			@SuppressWarnings("unchecked")
			Long id = template.queryForObject("SELECT * FROM " + fromTableName + " WHERE " + searchFieldCriteris, 
					new HashMap(), 
					(RowMapper<Long>)((rs, rowNum) -> Long.valueOf(rs.getLong(1))));			
	        return id;
		} catch (Exception ignore) {
			LOGGER.error("[PingApiServiceImpl::login] ERROR : " + ignore.getMessage());
			}
		
		return null;
    }        
}
