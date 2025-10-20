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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.soris.SorisFr3DAOImpl.SorisFr3RowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr7DAO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr3DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr7DTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type SorisFr7 dao.
 *
 * @author CSI PIEMONTE
 */
public class SorisFr7DAOImpl  extends RiscaBeSrvGenericDAO<SorisFr7DTO> implements SorisFr7DAO {
	
	private static final String QUERY_INSERT = 
			

			" 	insert into risca_w_soris_FR7 "+
				" (tipo_record, progr_record, cod_ambito, cod_ente_creditore, chiave_inf_annullare, "+
				" chiave_inf_correttiva,tipo_evento, motivo_rich_annul, ente_richiedente, "+
				" 	data_richiesta, filler ) "+
				" VALUES ( :tipoRecord, :progrRecord, :codAmbito, :codEnteCreditore, :chiaveInfAnnullare, "+
				   " :chiaveInfCorrettiva, :tipoEvento, :motivoRichAnnul, :enteRichiedente,  "+
				" :dataRichiesta, :filler ) ";
	
	
	
	private static final String QUERY_LOAD = 
			
			" select "+
			" progr_record, "+
			" cod_ambito, "+
			" chiave_inf_annullare, "+
			" chiave_inf_correttiva, "+
			" tipo_evento, "+
			" motivo_rich_annul, "+
			" ente_richiedente, "+
			" data_richiesta, "+
			" cod_ente_creditore, "+
			" substr(chiave_inf_annullare ,9,4) as anno_ruolo, "+
			" substr(chiave_inf_annullare ,13,6) as numero_ruolo, "+
			" substr(chiave_inf_annullare ,19,1) as p_tipo_ufficio, "+
			" substr(chiave_inf_annullare ,20,6) as p_codice_ufficio, "+
			" substr(chiave_inf_annullare ,26,4) as p_anno_riferimento, "+
			" substr(chiave_inf_annullare ,33,34) as p_ident_prenot_ruolo, "+
			" substr(chiave_inf_annullare ,118,	8) as data_evento, "+
			" substr(substr(chiave_inf_annullare ,67,48),9,7) utenza, "+
			" (select distinct	id_ruolo from risca_r_ruolo_soris_fr3	where p_ident_prenot_ruolo = substr(chiave_inf_annullare ,33,34) and progr_articolo_ruolo = to_number(substr(chiave_inf_annullare ,115,3)  )) as id_ruolo , "+
			" (select distinct	id_pagamento from risca_r_ruolo_soris_fr3 where	p_ident_prenot_ruolo = substr(chiave_inf_annullare ,33,34) and progr_articolo_ruolo = to_number(substr(chiave_inf_annullare ,115,3)) ) as id_pagamento  "+
			"     from "+
			"     risca_w_soris_fr7 fr7 "+
			"     where "+
			"     chiave_inf_annullare not in ( "+
			"     select "+
			"    	to_char(data_registr, 'YYYYMMDD')|| anno_ruolo || numero_ruolo || p_tipo_ufficio || p_codice_ufficio || p_anno_riferimento || p_tipo_modello || p_ident_prenot_ruolo || p_ident_atto || '00' || progr_articolo_ruolo || to_char(data_evento, 'YYYYMMDD')|| num_operaz_contabile || progr_inter_op_contab "+
			"     from "+
			"    	risca_w_soris_fr3) ";
			
	private static final String QUERY_DELETE = " delete from risca_w_soris_FR7 ";
	
	@Override
	public SorisFr7DTO saveSorisFr7(SorisFr7DTO dto) throws DAOException, DataAccessException, SQLException {
		LOGGER.debug("[SorisFr7DAO::saveSorisFr7] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("tipoRecord", dto.getTipoRecord());
			map.put("progrRecord", dto.getProgrRecord());
			map.put("codAmbito", dto.getCodAmbito());
			map.put("codEnteCreditore", dto.getCodEnteCreditore());
			map.put("chiaveInfAnnullare", dto.getChiaveInfAnnullare());
			map.put("chiaveInfCorrettiva", dto.getChiaveInfCorrettiva());
			map.put("tipoEvento", dto.getTipoEvento());
			map.put("motivoRichAnnul", dto.getMotivoRichAnnul());
			map.put("enteRichiedente", dto.getEnteRichiedente());
			map.put("dataRichiesta", dto.getDataRichiesta());
			map.put("filler", dto.getFiller());
			
			MapSqlParameterSource params = getParameterValue(map);
			
            template.update(getQuery(QUERY_INSERT, null, null), params);
		} catch (Exception e) {
			LOGGER.error("[SorisFr7DAO::saveSorisFr7] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SorisFr7DAO::saveSorisFr7] END");
		}
		return dto;
	}

	@Override
	public List<SorisFr7DTO> loadSorisFr7() throws DAOException, DataAccessException, SQLException {
		LOGGER.debug("[SorisFr7DAO::loadSorisFr7] BEGIN");
		List<SorisFr7DTO> listFr7 = new ArrayList<SorisFr7DTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			listFr7 = template.query(getQuery(QUERY_LOAD, null, null), params, new SorisFr7RowMapper());
		} catch (Exception e) {
			LOGGER.error("[SorisFr7DAO::loadSorisFr7] Errore generale ", e);
			LOGGER.debug("[SorisFr7DAO::loadSorisFr7] END");
			return listFr7;
		}
		LOGGER.debug("[SorisFr7DAO::loadSorisFr7] END");
		return listFr7;
	}
	
	public Integer delete() throws DAOException{
		LOGGER.debug("[SorisFr7DAO::delete] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE, null, null), params, keyHolder);

		} finally {
			LOGGER.debug("[SorisFr7DAO::delete] END");
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowMapper<SorisFr7DTO> getRowMapper() throws SQLException {
		return new SorisFr7RowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SorisFr7DTO> getExtendedRowMapper() throws SQLException {
		return new SorisFr7RowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class SorisFr7RowMapper implements RowMapper<SorisFr7DTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SorisFr7RowMapper() throws SQLException {
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
		public SorisFr7DTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SorisFr7DTO bean = new SorisFr7DTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SorisFr7DTO bean) throws SQLException {
			if(rsHasColumn(rs, "tipo_record"))bean.setTipoRecord(rs.getString("tipo_record"));
			if(rsHasColumn(rs, "progr_record"))bean.setProgrRecord(rs.getInt("progr_record"));
			if(rsHasColumn(rs, "cod_ambito"))bean.setCodAmbito(rs.getString("cod_ambito"));
			if(rsHasColumn(rs, "cod_ente_creditore"))bean.setCodEnteCreditore(rs.getString("cod_ente_creditore"));
			if(rsHasColumn(rs, "chiave_inf_annullare"))bean.setChiaveInfAnnullare(rs.getString("chiave_inf_annullare"));
			if(rsHasColumn(rs, "chiave_inf_correttiva"))bean.setChiaveInfCorrettiva(rs.getString("chiave_inf_correttiva"));
			if(rsHasColumn(rs, "tipo_evento"))bean.setTipoEvento(rs.getString("tipo_evento"));
			if(rsHasColumn(rs, "motivo_rich_annul"))bean.setMotivoRichAnnul(rs.getString("motivo_rich_annul"));
			if(rsHasColumn(rs, "ente_richiedente"))bean.setEnteRichiedente(rs.getString("ente_richiedente"));
			if(rsHasColumn(rs, "data_richiesta"))bean.setDataRichiesta(rs.getDate("data_richiesta"));
			if(rsHasColumn(rs, "filler"))bean.setFiller(rs.getString("filler"));
			if(rsHasColumn(rs, "anno_ruolo"))bean.setAnnoRuolo(rs.getString("anno_ruolo"));
			if(rsHasColumn(rs, "numero_ruolo"))bean.setNumeroRuolo(rs.getString("numero_ruolo"));
			if(rsHasColumn(rs, "p_tipo_ufficio"))bean.setpTipoUfficio(rs.getString("p_tipo_ufficio"));
			if(rsHasColumn(rs, "p_codice_ufficio"))bean.setpCodiceUfficio(rs.getString("p_codice_ufficio"));
			if(rsHasColumn(rs, "p_anno_riferimento"))bean.setpAnnoRiferimento(rs.getString("p_anno_riferimento"));
			if(rsHasColumn(rs, "p_ident_prenot_ruolo"))bean.setpIdentPrenotRuolo(rs.getString("p_ident_prenot_ruolo"));
			if(rsHasColumn(rs, "id_ruolo"))bean.setIdRuolo(rs.getLong("id_ruolo")== 0 ? null : rs.getLong("id_ruolo"));
			if(rsHasColumn(rs, "id_pagamento"))bean.setIdPagamento(rs.getLong("id_pagamento")== 0 ? null : rs.getLong("id_pagamento"));
			if(rsHasColumn(rs, "data_evento"))bean.setDataEvento(rs.getString("data_evento"));
			if(rsHasColumn(rs, "utenza"))bean.setUtenza(rs.getString("utenza"));
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Long decodeId(String fromTableName, String searchFieldCriteris) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Long findNextSequenceValue(String sequenceName) throws DataAccessException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	private static boolean rsHasColumn(ResultSet rs, String column){
	    try{
	        rs.findColumn(column);
	        return true;
	    } catch (SQLException sqlex){
	        //Column not present in resultset
	    }
	    return false;
	}




}