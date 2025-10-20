/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.RimborsoExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class RimborsoAmbienteDAOImpl extends RiscaBeSrvGenericDAO<RimborsoExtendedDTO> {

	
	public static final String QUERY_INSERT_RIMBORSO="INSERT INTO risca_r_rimborso (id_rimborso, id_stato_debitorio, id_tipo_rimborso,"
			+ " id_soggetto, imp_rimborso, causale, num_determina, data_determina, imp_restituito,"
			+ " gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid, id_gruppo_soggetto) VALUES( :idRimborso,"
			+ " :idStatoDebitorio, :idTipoRimborso, :idSoggetto, :impRimborso, :causale, :numDetermina, :dataDetermina,"
			+ " :impRestituito, :gestDataIns, :gestAttoreIns, :gestDataUp, :gestAttoreUpd, :gestUid"
			+ ", :idGruppoSoggetto)";
	

	public RimborsoExtendedDTO saveRimborso(RimborsoExtendedDTO dto) throws Exception {
		LOGGER.debug("[RimborsoAmbienteDAOImpl::saveRimborso] BEGIN");
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		
		Long genId;
		try {
			genId = findNextSequenceValue("seq_risca_r_rimborso");
		
		map.put("idRimborso", genId);
		map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
		map.put("idTipoRimborso", dto.getTipoRimborso().getIdTipoRimborso());
		map.put("idSoggetto", dto.getSoggettoRimborso().getIdSoggetto());
		map.put("impRimborso", dto.getImpRimborso());
		map.put("causale", dto.getCausale());
		map.put("numDetermina", dto.getNumDetermina());
		map.put("idGruppoSoggetto", dto.getIdGruppoSoggetto()); 
		map.put("dataDetermina", dto.getDataDetermina());
		map.put("impRestituito", dto.getImpRestituito());

		map.put("gestAttoreIns", dto.getGestAttoreIns());
		map.put("gestDataIns", now);
		map.put("gestAttoreUpd", dto.getGestAttoreUpd());
		map.put("gestDataUp", now);
		map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
		
		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT_RIMBORSO;
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setIdRimborso(genId);
		dto.setGestDataIns(now);
		dto.setGestDataUpd(now);

		} catch (DataAccessException e) {
			LOGGER.error("[RimborsoAmbienteDAOImpl::saveRimborso] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error("[RimborsoAmbienteDAOImpl::saveRimborso] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (Exception e) {
			LOGGER.error("[RimborsoAmbienteDAOImpl::saveRimborso] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}
		LOGGER.debug("[RimborsoTributiDAOImpl::saveRimborso] END");
		return dto;
	}
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<RimborsoExtendedDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
