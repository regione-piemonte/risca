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

import it.csi.risca.riscabesrv.dto.SollDatiTitolareDTO;
import it.csi.risca.riscabesrv.util.Constants;


public class SollDatiTitolareDaoRowMapper implements RowMapper<SollDatiTitolareDTO> {

	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME + ".dao");

	public SollDatiTitolareDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOGGER.debug("SollDatiTitolareDaoRowMapper START " );
		SollDatiTitolareDTO dto = new SollDatiTitolareDTO();
		
		dto.setIdAccertamento(rs.getLong("id_accertamento_soll"));
		dto.setIdSoggetto(rs.getLong("id_soggetto"));
		dto.setCodiceFiscaleCalc(rs.getString("codice_fiscale"));
		dto.setIndirizzoIndPost(rs.getString("indirizzo"));
		dto.setComuneIndPost(rs.getString("comune"));
		dto.setProvIndPost(rs.getString("provincia"));
		dto.setCodBelfioreComune(rs.getString("cod_belfiore"));
		dto.setCapComune(rs.getString("cap_comune"));
		dto.setDenSoggetto(rs.getString("den_soggetto"));
		dto.setNome(rs.getString("nome"));
		dto.setCognome(rs.getString("cognome"));
		dto.setDataNascita(rs.getString("data_nascita"));
		dto.setPartitaIva(rs.getString("partita_iva"));
		dto.setIdTipoSoggetto(rs.getLong("id_tipo_soggetto"));
		return dto;
	}
}

