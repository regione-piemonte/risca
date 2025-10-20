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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RecapitoPostelDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper.RecapitoPostelDaoRowMapper;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscabesrv.dto.RegioneDTO;

public class RecapitoPostelDAOImpl  extends RiscaBeSrvGenericDAO<RegioneDTO> implements RecapitoPostelDAO {
	
	private final String className = this.getClass().getSimpleName();
	
	private static final String QUERY_DELETE_INDIRIZZI_RECAPITI_ALTERNATIVI = "DELETE FROM risca_r_recapito_postel rrrp USING risca_r_recapito rrr WHERE rrrp.id_recapito = rrr.id_recapito AND rrrp.id_recapito = :idRecapito AND rrr.id_tipo_recapito = 2";

	private static final String QUERY_DELETE_INDIRIZZI_RECAPITI_SOGGETTO = "DELETE FROM risca_r_recapito_postel rrrp WHERE rrrp.id_recapito in (SELECT rrr.id_recapito FROM "
			+ "risca_r_recapito rrr WHERE rrr.id_soggetto = :idSoggetto)";
	
	@Override
	public List<IndirizzoSpedizioneDTO> getRecapitoPostelByIdRecapitoAndIdGruppo(Long idRecapito,
			List<Long> listIdGruppoSoggetto) throws DAOException {
		LOGGER.debug("[RecapitoPostelDAOImpl::getRecapitoPostelByIdRecapitoAndIdGruppo] BEGIN");
		
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		
		sql.append("SELECT id_recapito_postel,  ");			
		sql.append("rp.id_recapito, ");	
		sql.append("rp.id_gruppo_soggetto, ");			
		sql.append("rp.destinatario_postel, ");
		sql.append("rp.presso_postel, ");
		sql.append("rp.indirizzo_postel, ");
		sql.append("rp.citta_postel, ");
		sql.append("rp.provincia_postel, ");
		sql.append("rp.cap_postel,  ");
		sql.append("rp.frazione_postel, ");
		sql.append("rp.nazione_postel, ");
		sql.append("rp.ind_valido_postel, ");
		sql.append("rp.gest_data_ins, ");
		sql.append("rp.gest_attore_ins, ");
		sql.append("rp.gest_data_upd, ");
		sql.append("rp.gest_attore_upd, ");
		sql.append("rp.gest_uid, ");
		sql.append("rts.id_tipo_soggetto, ");
		sql.append("rts.cognome, ");
		sql.append("rts.nome, ");
		sql.append("rts.den_soggetto, ");
		sql.append("rts.gest_attore_ins, ");
		sql.append("rts.gest_attore_upd  ");
		sql.append("FROM risca_r_recapito_postel rp ");
		sql.append("inner join risca_r_recapito r on r.id_recapito = rp.id_recapito  ");
		sql.append("inner join risca_t_soggetto rts on rts.id_soggetto = r.id_soggetto  ");
		sql.append("WHERE rp.id_recapito = :ID_RECAPITO ");
		if(listIdGruppoSoggetto != null) {
			sql.append(" AND id_gruppo_soggetto IN (:listIdGruppoSoggetto) ");
		}

		List<IndirizzoSpedizioneDTO> list = new ArrayList<IndirizzoSpedizioneDTO>();
		
		paramMap.addValue("ID_RECAPITO", idRecapito);
		
		if(listIdGruppoSoggetto != null) {
			paramMap.addValue("listIdGruppoSoggetto", listIdGruppoSoggetto);
		}
		
		try {
			list = template.query(sql.toString(), paramMap, new RecapitoPostelDaoRowMapper());

		} catch (Exception ex) {
			LOGGER.error("[RecapitoPostelDAOImpl::getRecapitoPostelByIdRecapitoAndIdGruppo] esecuzione query", ex);
			return list;
		} finally {
			LOGGER.debug("[RecapitoPostelDAOImpl::getRecapitoPostelByIdRecapitoAndIdGruppo] END");
		}
		return list;
	}

	@Override
	public void deleteRecapitoAlternativoPostelByIdRecapito(Long idRecapito) throws DAOException {
	    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        try {
        	Map<String, Object> map = new HashMap<>();
        	map.put("idRecapito", idRecapito);
        	MapSqlParameterSource params = getParameterValue(map);

        	template.update(getQuery(QUERY_DELETE_INDIRIZZI_RECAPITI_ALTERNATIVI, null, null), params);
        } catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
		
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<RegioneDTO> getRowMapper() throws SQLException {
		return null;
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}

	@Override
	public void deleteRecapitoAlternativoPostelByIdSoggetto(Long idSoggetto) throws DAOException {
	    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        try {
        	Map<String, Object> map = new HashMap<>();
        	map.put("idSoggetto", idSoggetto);
        	MapSqlParameterSource params = getParameterValue(map);

        	template.update(getQuery(QUERY_DELETE_INDIRIZZI_RECAPITI_SOGGETTO, null, null), params);
        } catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
		
	}

    
}
