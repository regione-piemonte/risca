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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.RuoloSorisFr7DAO;
import it.csi.risca.riscabesrv.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr7DTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type RuoloSorisFr7 dao.
 *
 * @author CSI PIEMONTE
 */
public class RuoloSorisFr7DAOImpl  extends RiscaBeSrvGenericDAO<RuoloSorisFr7DTO> implements RuoloSorisFr7DAO {
	
	private static final String QUERY_INSERT = 		
			" insert into risca_r_ruolo_soris_fr7 "+
			" (id_ruolo_soris_fr7, id_ruolo, id_file_soris, id_pagamento, tipo_record, utenza, anno_ruolo,  numero_ruolo, p_tipo_ufficio, "+
			" p_codice_ufficio, p_anno_riferimento, p_ident_prenot_ruolo, progr_record,  cod_ambito, cod_ente_creditore,    " + 
					" chiave_inf_annullare,  chiave_inf_correttiva, tipo_evento, motivo_rich_annul, ente_richiedente, data_richiesta,   " +
					" note, gest_data_ins, gest_attore_ins, gest_attore_upd, gest_data_upd, gest_uid ) " +
					" VALUES ( " +
				    " :idRuoloSorisFr7, :idRuolo, :idFileSoris, :idPagamento, :tipoRecord, :utenza, :annoRuolo, :numeroRuolo, :pTipoUfficio,  " +
					" :pCodiceUfficio, :pAnnoRiferimento, :pIdentPrenotRuolo, :progrRecord, :codAmbito, :codEnteCreditore , "+
					" :chiaveInfAnnullare, :chiaveInfCorrettiva, :tipoEvento, :motivoRichAnnul, :enteRichiedente, :dataRichiesta, "+
					" :note , :gestDataIns, :gestAttoreIns, :gestAttoreUpd, :gestDataUpd, :gestUid) ";
	
		
	private static final String QUERY_LOAD = 
	" select distinct id_pagamento, p_ident_prenot_ruolo, to_date(substr(chiave_inf_annullare ,118,	8),'yyyyMMdd') as data_evento "+
	"   from risca_r_ruolo_soris_fr7  "+
	" where "+
	"  id_file_soris = :idFileSoris "+
	" and id_pagamento is not NULL   "+
	" and chiave_inf_annullare NOT IN  "+
	" (select to_char(data_registr,'YYYYMMDD')||anno_ruolo||numero_ruolo||p_tipo_ufficio||p_codice_ufficio||p_anno_riferimento||p_tipo_modello||p_ident_prenot_ruolo||p_ident_atto "+
	" ||'00'||progr_articolo_ruolo||to_char(data_evento,'YYYYMMDD')||num_operaz_contabile||progr_inter_op_contab "+  
	" FROM risca_w_soris_fr3) ";


	@Override
	public RuoloSorisFr7DTO saveRuoloSorisFr7(RuoloSorisFr7DTO dto) {
		LOGGER.debug("[RuoloSorisFr7DAOImpl::saveRuoloSorisFr7] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			String gestUID = null;         
			Long genId = findNextSequenceValue("seq_risca_r_ruolo_soris_fr7");
			map.put("idRuoloSorisFr7", genId);
			map.put("idRuolo", dto.getIdRuolo());
			map.put("idFileSoris", dto.getIdFileSoris());
			map.put("idPagamento", dto.getIdPagamento());
			map.put("tipoRecord", dto.getTipoRecord());
			map.put("utenza", dto.getUtenza());
			map.put("annoRuolo", dto.getAnnoRuolo());
			map.put("numeroRuolo", dto.getNumeroRuolo());
			map.put("pTipoUfficio", dto.getpTipoUfficio());
			map.put("pCodiceUfficio", dto.getpCodiceUfficio());
			map.put("pAnnoRiferimento", dto.getpAnnoRiferimento());
			map.put("pIdentPrenotRuolo", dto.getpIdentPrenotRuolo());			
			map.put("progrRecord", dto.getProgrRecord());
			map.put("codAmbito", dto.getCodAmbito());
			map.put("codEnteCreditore", dto.getCodEnteCreditore());
			map.put("chiaveInfAnnullare", dto.getChiaveInfAnnullare());
			map.put("chiaveInfCorrettiva", dto.getChiaveInfCorrettiva());
			map.put("tipoEvento", dto.getTipoEvento());
			map.put("motivoRichAnnul", dto.getMotivoRichAnnul());
			map.put("enteRichiedente", dto.getEnteRichiedente());
			map.put("dataRichiesta", dto.getDataRichiesta());			
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
			dto.setIdRuoloSorisFr7(genId);
			return dto;

		} catch (SQLException e) {
			LOGGER.error("[RuoloSorisFr7DAOImpl::saveRuoloSorisFr7] Errore nell'esecuzione della query", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RuoloSorisFr7DAOImpl::saveRuoloSorisFr7] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} finally {
			LOGGER.debug("[RuoloSorisFr7DAOImpl::saveRuoloSorisFr7] END");
		}

	}

	
	@Override
	public List<RuoloSorisFr7DTO> loadEstrazioneDatiSorisPerInsertAnnullamenti(Long idElabora, Long idFileSoris) {
		LOGGER.debug("[RuoloSorisFr7DAOImpl::loadEstrazioneDatiSorisPerInsertAnnullamenti] BEGIN");
		LOGGER.debug("[RuoloSorisFr7DAOImpl::loadEstrazioneDatiSorisPerInsertAnnullamenti] idElabora: "+idElabora);
		LOGGER.debug("[RuoloSorisFr7DAOImpl::loadEstrazioneDatiSorisPerInsertAnnullamenti] idFileSoris: "+idFileSoris);
		List<RuoloSorisFr7DTO> listRuoloSorisFr7 = new ArrayList<RuoloSorisFr7DTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idFileSoris", idFileSoris);
			MapSqlParameterSource params = getParameterValue(map);
			listRuoloSorisFr7 = template.query(getQuery(QUERY_LOAD, null, null), params,
					new RuoloSorisFr7RowMapper());
		} catch (Exception e) {
			LOGGER.error("[RuoloSorisFr7DAOImpl::loadEstrazioneDatiSorisPerInsertAnnullamenti] Errore generale ", e);
			LOGGER.debug("[RuoloSorisFr7DAOImpl::loadEstrazioneDatiSorisPerInsertAnnullamenti] END");
			return listRuoloSorisFr7;
		}
		LOGGER.debug("[RuoloSorisFr7DAOImpl::loadEstrazioneDatiSorisPerInsertAnnullamenti] END");
		return listRuoloSorisFr7;
	}
	
	@Override
	public Integer countByPIdentPrenotRuolo(String pIdentPrenotRuolo)  throws DAOException, SystemException {
		LOGGER.debug("[RuoloSorisFr7DAOImpl::countByPIdentPrenotRuolo] START");
		LOGGER.debug("[RuoloSorisFr7DAOImpl::countByPIdentPrenotRuolo] pIdentPrenotRuolo: "+pIdentPrenotRuolo);
		StringBuilder sql = new StringBuilder();
		 Map<String, Object> map = new HashMap<>();
		
		sql.append("select count(*) from risca_r_ruolo_soris_fr7  where substr(chiave_inf_annullare ,33,34) = :pIdentPrenotRuolo ");

		Integer conteggio = 0;
		
		try {
			map.put("pIdentPrenotRuolo", pIdentPrenotRuolo);
            MapSqlParameterSource params = getParameterValue(map);
			conteggio = template.queryForObject(sql.toString(), params, Integer.class);

		} catch (RuntimeException ex) {
			LOGGER.error("[RuoloSorisFr7DAOImpl::countByPIdentPrenotRuolo] esecuzione query", ex);
			throw new DAOException("Query failed non trovato");
		} finally {
			LOGGER.debug("[RuoloSorisFr7DAOImpl::countByPIdentPrenotRuolo] END");
		}
		return conteggio;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowMapper<RuoloSorisFr7DTO> getRowMapper() throws SQLException {
		return new RuoloSorisFr7RowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<RuoloSorisFr7DTO> getExtendedRowMapper() throws SQLException {
		return new RuoloSorisFr7RowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class RuoloSorisFr7RowMapper implements RowMapper<RuoloSorisFr7DTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RuoloSorisFr7RowMapper() throws SQLException {
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
		public RuoloSorisFr7DTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RuoloSorisFr7DTO bean = new RuoloSorisFr7DTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RuoloSorisFr7DTO bean) throws SQLException {		
			
			if(rsHasColumn(rs, "p_ident_prenot_ruolo"))bean.setpIdentPrenotRuolo(rs.getString("p_ident_prenot_ruolo"));
			if(rsHasColumn(rs, "id_pagamento"))bean.setIdPagamento(rs.getLong("id_pagamento"));
			if(rsHasColumn(rs, "data_evento"))bean.setDataEvento(rs.getString("data_evento"));

		}
		
		
		private boolean rsHasColumn(ResultSet rs, String column){
		    try{
		        rs.findColumn(column);
		        return true;
		    } catch (SQLException sqlex){
		        //Column not present in resultset
		    }
		    return false;
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