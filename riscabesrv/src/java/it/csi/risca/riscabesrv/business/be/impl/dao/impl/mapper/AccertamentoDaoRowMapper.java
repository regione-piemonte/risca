/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.dto.AccertamentoDTO;
import it.csi.risca.riscabesrv.util.Constants;


public class AccertamentoDaoRowMapper implements RowMapper<AccertamentoDTO> {

	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME + ".dao");

	public AccertamentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOGGER.debug("AccertamentoDaoRowMapper START " );
		AccertamentoDTO dto = new AccertamentoDTO();
		
		dto.setDataProtocollo(rs.getDate("data_protocollo"));
		dto.setNumProtocollo(rs.getString("num_protocollo"));
		dto.setDataNotifica(rs.getDate("data_notifica"));
		dto.setIdAccertamento(rs.getLong("id_accertamento"));
		return dto;
	}
}

