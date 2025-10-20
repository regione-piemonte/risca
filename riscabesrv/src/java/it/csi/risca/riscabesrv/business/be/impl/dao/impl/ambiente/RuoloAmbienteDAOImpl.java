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
import it.csi.risca.riscabesrv.dto.RuoloDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class RuoloAmbienteDAOImpl extends RiscaBeSrvGenericDAO<RuoloDTO> {

	
	public static final String QUERY_INSERT_RUOLO="INSERT INTO risca_r_ruolo (id_ruolo, id_accertamento, data_creazione_ruolo,"
			+ " num_bil_acc_canone, num_bil_acc_interessi, num_bil_acc_spese, importo_canone_mancante, importo_interessi_mancanti, importo_spese_mancanti, "
			+ " codice_ente_creditore, tipo_ufficio, codice_ufficio, anno_scadenza, identif_tipologia_atto, numero_partita, progressivo_partita, codice_tipo_atto, "
			+ " motivazione_iscrizione, p_ident_prenot_ruolo, note, "
			+ " gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) VALUES( :idRuolo, :idAccertamento, :dataCreazioneRuolo, "
			+ " :numBilAccCanone, :numBilAccInteressi, :numBilAccSpese, :importoCanoneMancante, :importoInteressiMancanti, :importoSpeseMancanti, "
			+ " :codiceEnteCreditore, :tipoUfficio, :codiceUfficio, :annoScadenza, :identifTipologiaAtto, :numeroPartita, :progressivoPartita, :codiceTipoAtto, "
			+ " :motivazioneIscrizione, :pIdentPrenotRuolo, :note, "
			+ " :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";
	

	public RuoloDTO saveRuolo(RuoloDTO dto) throws Exception {
		LOGGER.debug("[RuoloAmbienteDAOImpl::saveRuolo] BEGIN");
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		
		Long genId;
		try {
			genId = findNextSequenceValue("seq_risca_r_ruolo");
		
		map.put("idRuolo", genId);
		map.put("idAccertamento", dto.getIdAccertamento());
		map.put("dataCreazioneRuolo", dto.getDataCreazioneRuolo());
		map.put("numBilAccCanone", dto.getNumBilAccCanone());
		map.put("numBilAccInteressi", dto.getNumBilAccInteressi());
		map.put("numBilAccSpese", dto.getNumBilAccSpese());
		map.put("importoCanoneMancante", dto.getImportoCanoneMancante());
		map.put("importoInteressiMancanti", dto.getImportoInteressiMancanti()); 
		map.put("importoSpeseMancanti", dto.getImportoSpeseMancanti());
		map.put("codiceEnteCreditore", dto.getCodiceEnteCreditore());
		map.put("tipoUfficio", dto.getTipoUfficio());
		map.put("codiceUfficio", dto.getCodiceUfficio());
		map.put("annoScadenza", dto.getAnnoScadenza());
		map.put("identifTipologiaAtto", dto.getIdentifTipologiaAtto());
		map.put("numeroPartita", dto.getNumeroPartita());
		map.put("progressivoPartita", dto.getProgressivoPartita());
		map.put("codiceTipoAtto", dto.getCodiceTipoAtto());
		map.put("motivazioneIscrizione", dto.getMotivazioneIscrizione());
		map.put("pIdentPrenotRuolo", dto.getpIdentPrenotRuolo());
		map.put("note", dto.getNote());

		map.put("gestAttoreIns", dto.getGestAttoreIns());
		map.put("gestDataIns", now);
		map.put("gestAttoreUpd", dto.getGestAttoreUpd());
		map.put("gestDataUpd", now);
		map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
		
		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT_RUOLO;
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setIdRuolo(genId);
		dto.setGestDataIns(now);
		dto.setGestDataUpd(now);

		} catch (DataAccessException e) {
			LOGGER.error("[RuoloAmbienteDAOImpl::saveRuolo] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error("[RuoloAmbienteDAOImpl::saveRuolo] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (Exception e) {
			LOGGER.error("[RuoloAmbienteDAOImpl::saveRuolo] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}
		LOGGER.debug("[RuoloAmbienteDAOImpl::saveRuolo] END");
		return dto;
	}
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<RuoloDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
