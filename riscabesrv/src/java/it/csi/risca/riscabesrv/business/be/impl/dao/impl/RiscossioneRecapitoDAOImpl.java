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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneRecapitoDAO;
import it.csi.risca.riscabesrv.dto.RiscossioneRecapitoDTO;

public class RiscossioneRecapitoDAOImpl extends RiscaBeSrvGenericDAO<RiscossioneRecapitoDTO> implements RiscossioneRecapitoDAO {

	private final String className = this.getClass().getSimpleName();

	
	private static final String QUERY_DELETE_RISCOSSIONE_RECAPITO = "DELETE FROM risca_r_riscossione_recapito rrrr WHERE rrrr.id_recapito = :idRecapito";

	@Override
	public void deleteRiscossioneRecapitoByIdRecapito(Long idRecapito) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        try {
        	Map<String, Object> map = new HashMap<>();
        	map.put("idRecapito", idRecapito);
        	MapSqlParameterSource params = getParameterValue(map);

        	template.update(getQuery(QUERY_DELETE_RISCOSSIONE_RECAPITO, null, null), params);
        } catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<RiscossioneRecapitoDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
