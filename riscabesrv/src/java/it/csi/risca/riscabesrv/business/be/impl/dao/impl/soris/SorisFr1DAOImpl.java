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
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr1DAO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr1DTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type SorisFr1 dao.
 *
 * @author CSI PIEMONTE
 */
public class SorisFr1DAOImpl  extends RiscaBeSrvGenericDAO<SorisFr1DTO> implements SorisFr1DAO {
	
	private static final String QUERY_INSERT = 
			" insert into risca_w_soris_fr1 "+
			" (tipo_record, progr_record, cod_ambito, cod_ente_creditore, anno_ruolo, numero_ruolo,  " +
			" p_tipo_ufficio, p_codice_ufficio, p_anno_riferimento, p_tipo_modello, p_ident_prenot_ruolo,   " + 
			" p_ident_atto, progr_articolo_ruolo,identif_cartella, progr_articolo_cartella,  " +
			" codice_entrata,tipo_entrata, codice_fiscale,tipo_evento, data_evento, importo_carico,   " +
			" codice_divisa, data_scad_reg,iden_prec_avv_bon,esito_notifica, cod_ambito_delegato,  " +
			" ident_proc_esecutiva, tipo_spesa_proc_esec,tab_deposito, tab_spese,importo_conf_proc_esec,  " +
			" spese_proc_esec, spese_proc_esec_p_lista,t_spese_proc_esec, t_spese_proc_esec_p_lista,  " +
			" importo_tot_proc_esec, attivazione_proc_esec,integrazione_proc_esec, info_evento_notifica,  " +
			" filler ) " +
			" VALUES ( :tipoRecord, :progrRecord, :codAmbito, :codEnteCreditore, :annoRuolo, :numeroRuolo, " +
			" :pTipoUfficio, :pCodiceUfficio, :pAnnoRiferimento, :pTipoModello, :pIdentPrenotRuolo, "+
			" :pIdentAtto, :progrArticoloRuolo, :identifCartella, :progrArticoloCartella, "+
			" :codiceEntrata, :tipoEntrata, :codiceFiscale, :tipoEvento, :dataEvento, :importoCarico, "+
			" :codiceDivisa, :dataScadReg, :idenPrecAvvBon, :esitoNotifica, :codAmbitoDelegato, "+
			" :identProcEsecutiva, :tipoSpesaProcEsec, :tabDeposito, :tabSpese, :importoConfProcEsec, "+
			" :speseProcEsec, :speseProcEsecPLista, :tSpeseProcEsec, :tSpeseProcEsecPLista, "+
			" :importoTotProcEsec, :attivazioneProcEsec, :integrazioneProcEsec, :infoEventoNotifica, :filler ) ";
	

	
	private static final String QUERY_LOAD = 
			"select  substr(P_IDENT_ATTO,9,7) utenza, "
			+"progr_record, "
			+"cod_ambito, "
			+"cod_ente_creditore, "
			+"anno_ruolo, "
			+"numero_ruolo, "
			+"p_tipo_ufficio, "
			+"p_codice_ufficio, "
			+"p_anno_riferimento, "
			+"p_tipo_modello, "
			+"p_ident_prenot_ruolo, "
			+"p_ident_atto, "
			+"progr_articolo_ruolo, "
			+"identif_cartella, "
			+"case "
			+"when substr(identif_cartella,14,1) = '0' then '0' "
			+"when substr(identif_cartella,14,1) = '9' then '1' "
			+"end as flg_rateizzazione,"
			+"progr_articolo_cartella, "
			+"codice_entrata, "
			+"tipo_entrata,  "
			+"codice_fiscale, "
			+"tipo_evento, "
			+"data_evento, "
			+"importo_carico, "
			+"codice_divisa, "
			+"data_scad_reg, "
			+"(select r.id_ruolo from risca_r_ruolo r where r.p_ident_prenot_ruolo= fr1.p_ident_prenot_ruolo) as id_ruolo "
			+"from risca_w_soris_fr1 as fr1 "
			+"where "
			+"tipo_evento='F' "
			+"and codice_entrata in  ('1R96',   '1R97' ,   '1R98')  ";
	
	private static final String QUERY_DELETE = " delete from risca_w_soris_fr1 ";
	
	@Override
	public SorisFr1DTO saveSorisFr1(SorisFr1DTO dto) throws DAOException, DataAccessException, SQLException {
		LOGGER.debug("[SorisFr1DAO::saveSorisFr1] BEGIN");
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
			map.put("tipoEvento", dto.getTipoEvento());
			map.put("dataEvento", dto.getDataEvento());
			map.put("importoCarico", dto.getImportoCarico());
			map.put("codiceDivisa", dto.getCodiceDivisa());
			map.put("dataScadReg", dto.getDataScadReg());
			map.put("idenPrecAvvBon", dto.getIdenPrecAvvBon());
			map.put("esitoNotifica", dto.getEsitoNotifica());
			map.put("codAmbitoDelegato", dto.getCodAmbitoDelegato());
			map.put("identProcEsecutiva", dto.getIdentProcEsecutiva());
			map.put("tipoSpesaProcEsec", dto.getTipoSpesaProcEsec());
			map.put("tabDeposito", dto.getTabDeposito());
			map.put("tabSpese", dto.getTabSpese());
			map.put("importoConfProcEsec", dto.getImportoConfProcEsec());
			map.put("speseProcEsec", dto.getSpeseProcEsec());
			map.put("speseProcEsecPLista", dto.getSpeseProcEsecPLista());
			map.put("tSpeseProcEsec", dto.gettSpeseProcEsec());
			map.put("tSpeseProcEsecPLista", dto.gettSpeseProcEsecPLista());
			map.put("importoTotProcEsec", dto.getImportoTotProcEsec());
			map.put("attivazioneProcEsec", dto.getAttivazioneProcEsec());
			map.put("integrazioneProcEsec", dto.getIntegrazioneProcEsec());
			map.put("infoEventoNotifica", dto.getInfoEventoNotifica());
			map.put("filler", dto.getFiller());
			
            MapSqlParameterSource params = getParameterValue(map);
			
            template.update(getQuery(QUERY_INSERT, null, null), params);
		} catch (Exception e) {
			LOGGER.error("[SorisFr1DAO::saveSorisFr1] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SorisFr1DAO::saveSorisFr1] END");
		}
		return dto;
	}


	@Override
	public List<SorisFr1DTO> loadSorisFr1() throws DAOException, DataAccessException, SQLException {
		LOGGER.debug("[SorisFr1DAO::loadSorisFr1] BEGIN");
		List<SorisFr1DTO> listFr1 = new ArrayList<SorisFr1DTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			listFr1 = template.query(getQuery(QUERY_LOAD, null, null), params, new SorisFr1RowMapper());
		} catch (Exception e) {
			LOGGER.error("[SorisFr1DAO::loadSorisFr1] Errore generale ", e);
			LOGGER.debug("[SorisFr1DAO::loadSorisFr1] END");
			return listFr1;
		}
		LOGGER.debug("[SorisFr1DAO::loadSorisFr1] END");
		return listFr1;
	}

	public Integer delete() throws DAOException{
		LOGGER.debug("[SorisFr1DAO::delete] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE, null, null), params, keyHolder);

		} finally {
			LOGGER.debug("[SorisFr1DAO::delete] END");
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
	public RowMapper<SorisFr1DTO> getRowMapper() throws SQLException {
		return new SorisFr1RowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SorisFr1DTO> getExtendedRowMapper() throws SQLException {
		return new SorisFr1RowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class SorisFr1RowMapper implements RowMapper<SorisFr1DTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SorisFr1RowMapper() throws SQLException {
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
		public SorisFr1DTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SorisFr1DTO bean = new SorisFr1DTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SorisFr1DTO bean) throws SQLException {		
			
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
			if(rsHasColumn(rs, "flg_rateizzazione"))bean.setFlgRateizzazione(rs.getBoolean("flg_rateizzazione"));
			bean.setProgrArticoloCartella(rs.getInt("progr_articolo_cartella"));
			bean.setCodiceEntrata(rs.getString("codice_entrata"));
			bean.setTipoEntrata(rs.getString("tipo_entrata"));			
			bean.setCodiceFiscale(rs.getString("codice_fiscale"));
            bean.setTipoEvento(rs.getString("tipo_evento"));
            bean.setDataEvento(rs.getDate("data_evento"));
            bean.setImportoCarico(rs.getBigDecimal("importo_carico"));
            bean.setCodiceDivisa(rs.getString("codice_divisa"));
            bean.setDataScadReg(rs.getDate("data_scad_reg"));
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