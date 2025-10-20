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

import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ProvinciaExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettiDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;


public class SoggettoDaoRowMapper implements RowMapper<SoggettiExtendedDTO> {

	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME + ".dao");

	public SoggettiExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOGGER.debug("SoggettoDaoRowMapper START " );
		SoggettiExtendedDTO dto = new SoggettiExtendedDTO();
		dto.setIdTipoSoggetto(rs.getLong("id_tipo_soggetto"));
		dto.setCfSoggetto(rs.getString("cf_soggetto"));
		dto.setPartitaIvaSoggetto(rs.getString("partita_iva_soggetto"));
		dto.setCognome(rs.getString("cognome"));
		dto.setNome(rs.getString("nome"));
		dto.setDataNascitaSoggetto(rs.getString("data_nascita_soggetto"));
		ComuneExtendedDTO comune = new ComuneExtendedDTO();
		comune.setCodBelfioreComune(rs.getString("cod_belfiore_comune"));
		ProvinciaExtendedDTO provincia = new ProvinciaExtendedDTO();
		provincia.setSiglaProvincia(rs.getString("sigla_provincia"));
		comune.setProvincia(provincia);
		dto.setComuneNascita(comune);
		dto.setDenSoggetto(rs.getString("den_soggetto"));
	
		return dto;
	}
}

