/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.soris;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.soris.RuoloSorisFr3DAOImpl.RuoloSorisFr3RowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.RuoloSorisFr1DAO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr1DTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr3DTO;

/**
 * The type RuoloSorisFr1 dao.
 *
 * @author CSI PIEMONTE
 */
public class RuoloSorisFr1DAOImpl  extends RiscaBeSrvGenericDAO<RuoloSorisFr1DTO> implements RuoloSorisFr1DAO {
	
	private static final String QUERY_INSERT = 
			" insert into risca_r_ruolo_soris_fr1 "+
			" (id_ruolo_soris_fr1, id_ruolo, id_file_soris, tipo_record, utenza, progr_record,  " +
			" cod_ambito, cod_ente_creditore, anno_ruolo, numero_ruolo, p_tipo_ufficio,   " + 
			" p_codice_ufficio, p_anno_riferimento,p_tipo_modello, p_ident_prenot_ruolo,  " +
			" p_ident_atto,progr_articolo_ruolo, identif_cartella,flg_rateizzazione, progr_articolo_cartella, codice_entrata,   " +
			" tipo_entrata, codice_fiscale,tipo_evento,data_evento, importo_carico,  " +
			" codice_divisa, data_scad_reg,note, gest_data_ins, gest_attore_ins, gest_attore_upd, gest_data_upd, gest_uid ) " +
			" VALUES ( :idRuoloSorisFr1, :idRuolo, :idFileSoris, :tipoRecord, :utenza, :progrRecord, " +
			" :codAmbito, :codEnteCreditore, :annoRuolo, :numeroRuolo, :pTipoUfficio, "+
			" :pCodiceUfficio, :pAnnoRiferimento, :pTipoModello, :pIdentPrenotRuolo, "+
			" :pIdentAtto, :progrArticoloRuolo, :identifCartella, :flgRateizzazione, :progrArticoloCartella, :codiceEntrata, "+
			" :tipoEntrata, :codiceFiscale, :tipoEvento, :dataEvento, :importoCarico, "+
			" :codiceDivisa, :dataScadReg, :note , :gestDataIns, :gestAttoreIns, :gestAttoreUpd, :gestDataUpd, :gestUid) ";
	
	public static final String QUERY_IS_SUM_IMPORT_EQUALS = "select case when sum(importo_carico) = (coalesce(rr.importo_canone_mancante, 0) + coalesce(rr.importo_interessi_mancanti, 0) + coalesce(rr.importo_spese_mancanti, 0))   "
			+ " then 'TRUE' else 'FALSE' end confronto "
			+ " from risca_r_ruolo_soris_fr1 rs, risca_r_ruolo rr "
			+ " where id_file_soris = :idFileSoris "
			+ " and rs.id_ruolo = :idRuolo "
			+ "	and rs.id_ruolo =rr.id_ruolo  "
			+ "	group by rr.importo_canone_mancante,rr.importo_interessi_mancanti , rr.importo_spese_mancanti ";			
	
	
	public static final String QUERY_CONFRONTO_IMPORTI = "select rs.id_ruolo ,rs.p_ident_prenot_ruolo,id_file_soris ,  "
			+ " case when sum(importo_carico) = (coalesce(rr.importo_canone_mancante, 0)  "
			+ " + coalesce(rr.importo_interessi_mancanti, 0) + coalesce(rr.importo_spese_mancanti, 0))   "
			+ " then 1 else 0 end flg_confronto  "
			+ " from risca_r_ruolo_soris_fr1 rs, risca_r_ruolo rr "
			+ "	where id_file_soris = :idFileSoris   "
			+ "	and rs.id_ruolo =rr.id_ruolo   "	
			+ "	group by rs.id_ruolo ,rs.p_ident_prenot_ruolo,id_file_soris, rr.importo_canone_mancante,rr.importo_interessi_mancanti , rr.importo_spese_mancanti  ";		

	@Override
	public RuoloSorisFr1DTO saveRuoloSorisFr1(RuoloSorisFr1DTO dto) {
		LOGGER.debug("[RuoloSorisFr1DAOImpl::saveRuoloSorisFr1] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			String gestUID = null;         
			Long genId = findNextSequenceValue("seq_risca_r_ruolo_soris_fr1");
			map.put("idRuoloSorisFr1", genId);
			map.put("idRuolo", dto.getIdRuolo());
			map.put("idFileSoris", dto.getIdFileSoris());
			map.put("tipoRecord", dto.getTipoRecord());
			map.put("utenza", dto.getUtenza());
			map.put("progrRecord", dto.getProgrRecord());
			map.put("codAmbito", dto.getCodAmbito());
			map.put("codEnteCreditore", dto.getCodEnteCreditore());
			map.put("annoRuolo", dto.getAnnoRuolo());
			map.put("numeroRuolo", dto.getNumeroRuolo());
			map.put("pTipoUfficio", dto.getpTipoUfficio());
			
			map.put("pCodiceUfficio", dto.getpCodiceUfficio());
			map.put("pAnnoRiferimento", dto.getpAnnoRiferimento());
			
			map.put("pTipoModello", dto.getpTipoModello());
			map.put("pIdentPrenotRuolo", dto.getpIdentPrenotRuolo());
			map.put("pIdentAtto", dto.getpIdentAtto());
			map.put("progrArticoloRuolo", dto.getProgrArticoloRuolo());
			map.put("identifCartella", dto.getIdentifCartella());
			map.put("flgRateizzazione", dto.getFlgRateizzazione()==Boolean.TRUE?1:0);
			map.put("progrArticoloCartella", dto.getProgrArticoloCartella());
			map.put("codiceEntrata", dto.getCodiceEntrata());
			map.put("tipoEntrata", dto.getTipoEntrata());
			map.put("codiceFiscale", dto.getCodiceFiscale());
			
			map.put("tipoEvento", dto.getTipoEvento());
			map.put("dataEvento", dto.getDataEvento());
			map.put("importoCarico", dto.getImportoCarico());
			map.put("codiceDivisa", dto.getCodiceDivisa());
			map.put("dataScadReg", dto.getDataScadReg());
			map.put("note", dto.getNote());
			
			
			gestUID = generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now);
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", gestUID);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			dto.setGestUid(gestUID);
			dto.setIdRuoloSorisFr1(genId);
			return dto;

		} catch (SQLException e) {
			LOGGER.error("[RuoloSorisFr1DAOImpl::saveRuoloSorisFr1] Errore nell'esecuzione della query", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RuoloSorisFr1DAOImpl::saveRuoloSorisFr1] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} finally {
			LOGGER.debug("[RuoloSorisFr1DAOImpl::saveRuoloSorisFr1] END");
		}

	}

	
	@Override
	public Boolean isSumImportEquals(Long idFileSoris, Long idRuolo) throws Exception, BusinessException {
		LOGGER.debug("[RuoloSorisFr1DAOImpl::isSumImportEquals] BEGIN");
		LOGGER.debug("[RuoloSorisFr1DAOImpl::isSumImportEquals] idFileSoris: "+idFileSoris);
		LOGGER.debug("[RuoloSorisFr1DAOImpl::isSumImportEquals] idRuolo: "+idRuolo);
		boolean isEquals = false;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idFileSoris", idFileSoris);
			map.put("idRuolo", idRuolo);
			MapSqlParameterSource params = getParameterValue(map);
			isEquals = template.queryForObject(QUERY_IS_SUM_IMPORT_EQUALS, params,
					Boolean.class);
			
		} catch (DataAccessException e) {
			LOGGER.error("[RuoloSorisFr1DAOImpl::isSumImportEquals] Errore nell'accesso ai dati", e);

			e.printStackTrace();
			return null;
		} finally {
			LOGGER.debug("[RuoloSorisFr1DAOImpl::isSumImportEquals] END");
		}
		return isEquals;
	}
	
	@Override
	public List<RuoloSorisFr1DTO> loadConfrontoImporti(Long idFileSoris) throws Exception, BusinessException {
		LOGGER.debug("[RuoloSorisFr1DAOImpl::loadConfrontoImporti] BEGIN");
		LOGGER.debug("[RuoloSorisFr1DAOImpl::loadConfrontoImporti] idFileSoris: "+idFileSoris);
		List<RuoloSorisFr1DTO> result = new ArrayList<RuoloSorisFr1DTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idFileSoris", idFileSoris);
			MapSqlParameterSource params = getParameterValue(map);
			result = template.query(QUERY_CONFRONTO_IMPORTI, params,new RuoloSorisFr1RowMapper());
			
		} catch (DataAccessException e) {
			LOGGER.error("[RuoloSorisFr1DAOImpl::loadConfrontoImporti] Errore nell'accesso ai dati", e);

			e.printStackTrace();
			return null;
		} finally {
			LOGGER.debug("[RuoloSorisFr1DAOImpl::loadConfrontoImporti] END");
		}
		return result;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowMapper<RuoloSorisFr1DTO> getRowMapper() throws SQLException {
		return new RuoloSorisFr1RowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<RuoloSorisFr1DTO> getExtendedRowMapper() throws SQLException {
		return new RuoloSorisFr1RowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class RuoloSorisFr1RowMapper implements RowMapper<RuoloSorisFr1DTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RuoloSorisFr1RowMapper() throws SQLException {
			// Instantiate class
		}

		/**
		 * Implementations must implement this method to map each row of data in the
		 * ResultSet. This method should not call {@code next()} on the ResultSet; it is
		 * only supposed to map values of the current row.
		 *
		 * @param rs     the ResultSet to map (pre-initialized for the current row)
		 * @param rowNum the number of the current row
		 * @return the result object for the current row (may be {@code null})
		 * @throws SQLException if a SQLException is encountered getting column values
		 *                      (that is, there's no need to catch SQLException)
		 */
		@Override
		public RuoloSorisFr1DTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RuoloSorisFr1DTO bean = new RuoloSorisFr1DTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RuoloSorisFr1DTO bean) throws SQLException {		
			
			bean.setIdRuolo(rs.getLong("id_ruolo"));
			bean.setpIdentPrenotRuolo(rs.getString("p_ident_prenot_ruolo"));
			bean.setIdFileSoris(rs.getLong("id_file_soris"));
		    bean.setFlgConfronto(rs.getInt("flg_confronto") == 1 ? Boolean.TRUE : Boolean.FALSE);
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}


}