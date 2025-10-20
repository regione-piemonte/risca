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

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;


public class StatoDebitorioDaoRowMapper implements RowMapper<StatoDebitorioExtendedDTO> {

	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME + ".dao");

	public StatoDebitorioExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOGGER.debug("StatoDebitorioDaoRowMapper START " );
		StatoDebitorioExtendedDTO dto = new StatoDebitorioExtendedDTO();
		
		dto.setImpSpeseNotifica(rs.getBigDecimal("imp_spese_notifica"));
		return dto;
	}
}

