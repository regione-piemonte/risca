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

import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscabesrv.util.Constants;


public class RecapitoPostelDaoRowMapper implements RowMapper<IndirizzoSpedizioneDTO> {

	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME + ".dao");

	public IndirizzoSpedizioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOGGER.debug("RecapitoPostelDaoRowMapper START " );
		IndirizzoSpedizioneDTO dto = new IndirizzoSpedizioneDTO();
		dto.setIdRecapitoPostel(rs.getLong("id_recapito_postel"));
		dto.setIdRecapito(rs.getLong("id_recapito"));
		dto.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
		dto.setDestinatarioPostel(rs.getString("destinatario_postel"));
		dto.setPressoPostel(rs.getString("presso_postel"));
		dto.setIndirizzoPostel(rs.getString("indirizzo_postel"));
		dto.setCittaPostel(rs.getString("citta_postel"));
		dto.setProvinciaPostel(rs.getString("provincia_postel"));
		dto.setCapPostel(rs.getString("cap_postel"));
		dto.setFrazionePostel(rs.getString("frazione_postel"));
		dto.setNazionePostel(rs.getString("nazione_postel"));
		dto.setIndValidoPostel(rs.getLong("ind_valido_postel"));
		dto.setGestDataIns(rs.getDate("gest_data_ins"));
		dto.setGestAttoreUpd(rs.getString("gest_attore_ins"));
		dto.setGestDataUpd(rs.getDate("gest_data_upd"));
		dto.setGestAttoreIns(rs.getString("gest_attore_upd"));
		dto.setGestUid(rs.getString("gest_uid"));
		dto.setIdTipoSoggetto(rs.getLong("id_tipo_soggetto"));
		dto.setCognome(rs.getString("cognome"));
		dto.setNome(rs.getString("nome"));
		dto.setDenSoggetto(rs.getString("den_soggetto"));
		dto.setGestAttoreIns(rs.getString("gest_attore_ins"));
		dto.setGestAttoreUpd(rs.getString("gest_attore_upd"));
		return dto;
	}
}

