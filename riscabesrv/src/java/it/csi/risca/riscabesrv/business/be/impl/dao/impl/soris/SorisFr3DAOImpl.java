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
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr3DAO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr3DTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type SorisFr3 dao.
 *
 * @author CSI PIEMONTE
 */
public class SorisFr3DAOImpl  extends RiscaBeSrvGenericDAO<SorisFr3DTO> implements SorisFr3DAO {

	private static final String QUERY_INSERT = 

			" insert into risca_w_soris_fr3 "+
					" ( tipo_record,progr_record, cod_ambito, cod_ente_creditore, anno_ruolo, numero_ruolo, "+
					" p_tipo_ufficio, p_codice_ufficio, p_anno_riferimento,p_tipo_modello, p_ident_prenot_ruolo,    "+
					" p_ident_atto, progr_articolo_ruolo, identif_cartella, progr_articolo_cartella, codice_entrata,         "+      
					" tipo_entrata,codice_fiscale, data_evento, importo_carico_risco, importo_interessi, "+                 
					" importo_aggio_ente,importo_aggio_contrib,t_spese_proc_esec,t_spese_proc_esec_p_lista, "+
					" codice_divisa,modalita_pagam,filler1,data_registr,num_operaz_contabile,progr_inter_op_contab, "+
					" filler2,data_decorrenza, numero_riversam,restituzione_rimb_spese,importo_carico_orig, "+
					" identif_provv_magg_rate,importo_carico_resid_cond, filler3 ) "+
					" VALUES (:tipoRecord, :progrRecord, :codAmbito, :codEnteCreditore, :annoRuolo, :numeroRuolo, "+
					" :pTipoUfficio, :pCodiceUfficio, :pAnnoRiferimento, :pTipoModello, :pIdentPrenotRuolo, "+ 
					" :pIdentAtto, :progrArticoloRuolo, :identifCartella, :progrArticoloCartella, :codiceEntrata, "+
					" :tipoEntrata, :codiceFiscale, :dataEvento, :importoCaricoRisco, :importoInteressi, "+
					" :importoAggioEnte, :importoAggioContrib, :tSpeseProcEsec, :tSpeseProcEsecPLista, "+
					" :codiceDivisa, :modalitaPagam, :filler1, :dataRegistr, :numOperazContabile, :progrInterOpContab, "+
					" :filler2, :dataDecorrenza, :numeroRiversam, :restituzioneRimbSpese, :importoCaricoOrig, "+
					" :identifProvvMaggRate, :importoCaricoResidCond, :filler3 ) ";


	private static final String QUERY_LOAD = 
			" select  substr(P_IDENT_ATTO,9,7) utenza, " 
					+" progr_record, "
					+" cod_ambito, "
					+" cod_ente_creditore, "
					+" anno_ruolo, "
					+" numero_ruolo, "
					+" p_tipo_ufficio, "
					+" p_codice_ufficio, "
					+" p_anno_riferimento, "
					+" p_tipo_modello, "
					+" p_ident_prenot_ruolo, "
					+" p_ident_atto, "
					+" progr_articolo_ruolo, "
					+" identif_cartella, "
					+" progr_articolo_cartella, "
					+" codice_entrata, "
					+" tipo_entrata, "
					+" codice_fiscale, "
					+" data_evento, "
					+" importo_carico_risco, "
					+" importo_interessi, "
					+" importo_aggio_ente, "
					+" importo_aggio_contrib, "
					+" t_spese_proc_esec, "
					+" t_spese_proc_esec_p_lista,  "
					+" codice_divisa, "
					+" modalita_pagam,  "
					+" data_registr, "
					+" num_operaz_contabile, "
					+" progr_inter_op_contab, "
					+" (select r.id_ruolo from risca_r_ruolo r where r.p_ident_prenot_ruolo= fr3.p_ident_prenot_ruolo ) as id_ruolo "
					+" from risca_w_soris_fr3 fr3 "
					+"   where codice_entrata in "
					+"    ('1R96', '1R97', '1R98') "
					+"   and importo_carico_risco <> 0 "
					+" order by substr(P_IDENT_ATTO,9,7),  p_anno_riferimento, codice_entrata ";
	
	private static final String QUERY_DELETE = " delete from risca_w_soris_fr3 ";

	@Override
	public SorisFr3DTO saveSorisFr3(SorisFr3DTO dto) throws DAOException, DataAccessException, SQLException {
		LOGGER.debug("[SorisFr3DAO::saveSorisFr3] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("tipoRecord", dto.getTipoRecord());
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
			map.put("progrArticoloCartella", dto.getProgrArticoloCartella());
			map.put("codiceEntrata", dto.getCodiceEntrata());
			map.put("tipoEntrata", dto.getTipoEntrata());
			map.put("codiceFiscale", dto.getCodiceFiscale());
			map.put("dataEvento", dto.getDataEvento());
			map.put("importoCaricoRisco", dto.getImportoCaricoRisco());
			map.put("importoInteressi", dto.getImportoInteressi());
			map.put("importoAggioEnte", dto.getImportoAggioEnte());
			map.put("importoAggioContrib", dto.getImportoAggioContrib());
			map.put("tSpeseProcEsec", dto.gettSpeseProcEsec());
			map.put("tSpeseProcEsecPLista", dto.gettSpeseProcEsecPLista());
			map.put("codiceDivisa", dto.getCodiceDivisa());
			map.put("modalitaPagam", dto.getModalitaPagam());
			map.put("filler1", dto.getFiller1());
			map.put("dataRegistr", dto.getDataRegist());
			map.put("numOperazContabile", dto.getNumOperazContabile());
			map.put("progrInterOpContab", dto.getProgrInterOpContab());
			map.put("filler2", dto.getFiller2());
			map.put("dataDecorrenza", dto.getDataDecorrenza());
			map.put("numeroRiversam", dto.getNumeroRiversam());
			map.put("restituzioneRimbSpese", dto.getRestituzioneRimbSpese());
			map.put("importoCaricoOrig", dto.getImportoCaricoOrig());
			map.put("identifProvvMaggRate", dto.getIdentifProvvMaggRate());
			map.put("importoCaricoResidCond", dto.getImportoCaricoResidCond());
			map.put("filler3", dto.getFiller3());

			MapSqlParameterSource params = getParameterValue(map);

			template.update(getQuery(QUERY_INSERT, null, null), params);
		} catch (Exception e) {
			LOGGER.error("[SorisFr3DAO::saveSorisFr3] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SorisFr3DAO::saveSorisFr3] END");
		}
		return dto;
	}

	@Override
	public List<SorisFr3DTO> loadSorisFr3() throws DAOException, DataAccessException, SQLException {
		LOGGER.debug("[SorisFr3DAO::loadSorisFr3] BEGIN");
		List<SorisFr3DTO> listFr3 = new ArrayList<SorisFr3DTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			listFr3 = template.query(getQuery(QUERY_LOAD, null, null), params, new SorisFr3RowMapper());
		} catch (Exception e) {
			LOGGER.error("[SorisFr3DAO::loadSorisFr3] Errore generale ", e);
			LOGGER.debug("[SorisFr3DAO::loadSorisFr3] END");
			return listFr3;
		}
		LOGGER.debug("[SorisFr3DAO::loadSorisFr3] END");
		return listFr3;
	}

	public Integer delete() throws DAOException{
		LOGGER.debug("[SorisFr3DAO::delete] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE, null, null), params, keyHolder);

		} finally {
			LOGGER.debug("[SorisFr3DAO::delete] END");
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
	public RowMapper<SorisFr3DTO> getRowMapper() throws SQLException {
		return new SorisFr3RowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SorisFr3DTO> getExtendedRowMapper() throws SQLException {
		return new SorisFr3RowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class SorisFr3RowMapper implements RowMapper<SorisFr3DTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SorisFr3RowMapper() throws SQLException {
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
		public SorisFr3DTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SorisFr3DTO bean = new SorisFr3DTO();
			populateBean(rs, bean);
			return bean;
		}
		

		private void populateBean(ResultSet rs, SorisFr3DTO bean) throws SQLException {
			if(rsHasColumn(rs, "utenza"))bean.setUtenza(rs.getString("utenza"));
			bean.setProgrRecord(rs.getInt("progr_record"));
			bean.setCodAmbito(rs.getString("cod_ambito"));
			bean.setCodEnteCreditore(rs.getString("cod_ente_creditore"));
			bean.setAnnoRuolo(rs.getString("anno_ruolo"));
			bean.setNumeroRuolo(rs.getString("numero_ruolo"));
			bean.setpTipoUfficio(rs.getString("p_tipo_ufficio"));
			bean.setpCodiceUfficio(rs.getString("p_codice_ufficio"));			
			bean.setpAnnoRiferimento(rs.getString("p_anno_riferimento"));
			bean.setpTipoModello(rs.getString("p_tipo_modello"));
			bean.setpIdentPrenotRuolo(rs.getString("p_ident_prenot_ruolo")); 
			bean.setpIdentAtto(rs.getString("p_ident_atto"));
			bean.setProgrArticoloRuolo(rs.getInt("progr_articolo_ruolo"));
			bean.setIdentifCartella(rs.getString("identif_cartella"));
			bean.setProgrArticoloCartella(rs.getInt("progr_articolo_cartella"));
			bean.setCodiceEntrata(rs.getString("codice_entrata"));
			bean.setTipoEntrata(rs.getString("tipo_entrata"));			
			bean.setCodiceFiscale(rs.getString("codice_fiscale"));
            bean.setDataEvento(rs.getDate("data_evento"));
            bean.setImportoCaricoRisco(rs.getBigDecimal("importo_carico_risco"));
            bean.setImportoInteressi(rs.getInt("importo_interessi"));
            bean.setImportoAggioEnte(rs.getInt("importo_aggio_ente"));
            bean.setImportoAggioContrib(rs.getInt("importo_aggio_contrib"));
            bean.settSpeseProcEsec(rs.getInt("t_spese_proc_esec"));
            bean.settSpeseProcEsecPLista(rs.getInt("t_spese_proc_esec_p_lista"));
            bean.setCodiceDivisa(rs.getString("codice_divisa"));
            bean.setModalitaPagam(rs.getString("modalita_pagam"));
            bean.setDataRegist(rs.getDate("data_registr"));
            bean.setNumOperazContabile(rs.getString("num_operaz_contabile"));
            bean.setProgrInterOpContab(rs.getString("progr_inter_op_contab"));
            if(rsHasColumn(rs, "id_ruolo"))bean.setIdRuolo(rs.getLong("id_ruolo")== 0 ? null : rs.getLong("id_ruolo"));


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