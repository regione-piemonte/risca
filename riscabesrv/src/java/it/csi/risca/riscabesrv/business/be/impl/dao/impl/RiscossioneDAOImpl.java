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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.NazioneDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ProvinciaExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.RegioneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettoGruppoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente.RiscossioneAmbienteDAOImpl;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.tributi.RiscossioneTributiDAOImpl;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.FonteDTO;
import it.csi.risca.riscabesrv.dto.GruppiDTO;
import it.csi.risca.riscabesrv.dto.GruppiSoggettoDTO;
import it.csi.risca.riscabesrv.dto.NazioniDTO;
import it.csi.risca.riscabesrv.dto.ProvvedimentoDTO;
import it.csi.risca.riscabesrv.dto.RecapitiDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneRecapitoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchResultDTO;
import it.csi.risca.riscabesrv.dto.SoggettiDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatiRiscossioneDTO;
import it.csi.risca.riscabesrv.dto.TipiAutorizzazioneExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipiInvioDTO;
import it.csi.risca.riscabesrv.dto.TipiProvvedimentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipiRecapitoDTO;
import it.csi.risca.riscabesrv.dto.TipiTitoloExtendedDTO;
import it.csi.risca.riscabesrv.dto.UtRimbDTO;
import it.csi.risca.riscabesrv.dto.VerifyRiscossioneStatoDebDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatiTecniciAmbienteDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type Riscossione dao.
 *
 * @author CSI PIEMONTE
 */
@Service
public class RiscossioneDAOImpl extends RiscaBeSrvGenericDAO<RiscossioneDTO> implements RiscossioneDAO { 

	private final String className = this.getClass().getSimpleName();
	
    @Autowired	
    private MessaggiDAO messaggiDAO;
	
	@Autowired
	private SoggettiDAO soggettiDAO;
	
	@Autowired
	private AmbitiDAO ambitiDAO;
	
	@Autowired
	private RiscossioneAmbienteDAOImpl riscAmbienteDaoImpl;
	
	@Autowired
	private RiscossioneTributiDAOImpl riscTributiDaoImpl;
	
    @Autowired
    private SoggettoGruppoDAO soggettoGruppoDAO;
	
	private static final String AMBIENTE = "AMBIENTE";
	private static final String OPERE_PUBBLICHE = "Opere pubbliche";
	private static final String ATTIVITA_ESTRATTIVE = "Attivita estrattive";
	private static final String TRIBUTI = "TRIBUTI";

	private static final String QUERY_RISCOSSIONE_BY_ID_RISCOSSIONE = "SELECT rtr.* FROM risca_t_riscossione rtr WHERE rtr.id_riscossione = :idRiscossione";

	private static final String QUERY_SOGG_RISC_BY_ID_RISCOSSIONE = "SELECT rtr.id_soggetto FROM risca_t_riscossione rtr WHERE rtr.id_riscossione = :idRiscossione";

	private static final String QUERY_RISCOSSIONE_BY_COD_RISCOSSIONE = "SELECT rtr.* FROM risca_t_riscossione rtr WHERE rtr.cod_riscossione = :codRiscossione";

	private static final String QUERY_CHECK_RISCOSSIONE_RECAPITI = "SELECT rrrr.* from risca_r_riscossione_recapito rrrr "
			+ "INNER JOIN risca_r_recapito rrr ON rrrr.id_recapito = rrr.id_recapito "
			+ "WHERE rrrr.id_riscossione = :idRiscossione ";
	// + "AND rrr.id_tipo_recapito = 1";
	
	private static final String QUERY_RECAPITI_BY_ID_RISCOSSIONE = "SELECT rrr.* FROM risca_r_recapito rrr "
			+ "INNER JOIN risca_r_riscossione_recapito rrrr on rrr.id_recapito = rrrr.id_recapito "
			+ "WHERE rrrr.id_riscossione = :idRiscossione "
			+ "AND rrr.data_cancellazione is null";

	private static final String QUERY_LOAD_TIPI_RECAPITO = "SELECT rdtr.* FROM risca_d_tipo_recapito rdtr "
			+ "WHERE rdtr.id_tipo_recapito = :idTipoRecapito ";
	
	private static final String QUERY_LOAD_TIPI_INVIO = "SELECT rdti.* FROM risca_d_tipo_invio rdti "
			+ "WHERE rdti.id_tipo_invio = :idTipoInvio ";
	
	private static final String QUERY_LOAD_NAZIONE = "SELECT rdn.* FROM risca_d_nazione rdn "
			+ "WHERE rdn.id_nazione = :idNazione ";
	
	private static final String QUERY_LOAD_COMUNE = "SELECT distinct rdc.*, rdp.*, rdr.* , rdn.* FROM risca_d_comune rdc "
    		+ "INNER JOIN risca_d_provincia rdp ON rdc.id_provincia = rdp.id_provincia "
    		+ "INNER JOIN risca_d_regione rdr ON rdp.id_regione = rdr.id_regione  "
    		+ "INNER JOIN risca_d_nazione rdn   ON rdr.id_nazione = rdn.id_nazione "
			+ "WHERE rdc.id_comune = :idComune ";
	
	private static final String QUERY_LOAD_FONTE = "SELECT rdf.* FROM risca_d_fonte rdf "
			+ "WHERE rdf.id_fonte = :idFonte ";
	
	private static final String QUERY_RISCOSSIONI_BY_ID_GRUPPO = "SELECT rtr.* " + "FROM risca_t_riscossione rtr "
			+ "INNER JOIN risca_t_gruppo_soggetto rtgs ON rtgs.id_gruppo_soggetto = rtr.id_gruppo_soggetto "
			+ "WHERE rtr.id_gruppo_soggetto = :idGruppo";

	private static final String QUERY_RISCOSSIONI_BY_ID_SOGGETTO = "SELECT rtr.* " + "FROM risca_t_riscossione rtr "
			+ "INNER JOIN risca_t_soggetto rts ON rts.id_soggetto = rtr.id_soggetto "
			+ "WHERE rtr.id_soggetto = :idSoggetto";

	private static final String QUERY_GRUPPO_BY_ID = "SELECT rtgs.* FROM risca_t_gruppo_soggetto rtgs "
			+ "WHERE rtgs.id_gruppo_soggetto = :idGruppoSoggetto ";

	private static final String QUERY_TIPO_AUTORIZZA_BY_ID = "SELECT rdta.*, rda.* FROM risca_d_tipo_autorizza rdta "
			+ "inner  join risca_d_ambito rda on rdta.id_ambito = rda.id_ambito "
			+ "WHERE rdta.id_tipo_autorizza = :idTipoAutorizza ";

	private static final String QUERY_STATO_RISCOSSIONE_BY_ID_STATO = "SELECT rdsr.* FROM risca_d_stato_riscossione rdsr "
			+ "WHERE rdsr.id_stato_riscossione = :idStatoRiscossione ";


	private static final String QUERY_DATO_TECNICO_BY_ID_RISCOSSIONE = "SELECT rtr.* from risca_t_riscossione rtr "
			+ "WHERE rtr.id_riscossione = :idRiscossione";

	private static final String QUERY_PROVVVEDIMENTO_BY_ID_RISCOSSIONE = "SELECT rrp.*, rdtt.*, rdtp.* ,rda.* FROM risca_r_provvedimento rrp  "
			+ "left join risca_d_tipo_titolo rdtt on rrp.id_tipo_titolo=rdtt.id_tipo_titolo " 
			+ "left join risca_d_tipo_provvedimento rdtp on rrp.id_tipo_provvedimento=rdtp.id_tipo_provvedimento "
			+ "left join risca_d_ambito rda on rdtt.id_ambito = rda.id_ambito "
			+ "WHERE rrp.id_riscossione = :idRiscossione ";

	private static final String QUERY_LOAD_SOGGETTI_GRUPPO = 
			  "SELECT * "
			+ "	FROM risca_r_soggetto_gruppo "
			+ "	WHERE id_gruppo_soggetto = :id_gruppo_soggetto";
	
	private static final String ESCLUDE_ID_RISCOSSIONE =" and rtr.id_riscossione NOT IN (:idRiscossione) ";
			
	private static final String QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_SOGGETTO =" select count(*) as num_riscossioni from risca_t_riscossione rtr, risca_d_tipo_riscossione d "
			+ " where rtr.id_soggetto = :idOggetto "
			+ " and rtr.id_tipo_riscossione = d.id_tipo_riscossione "
			+ " and d.id_ambito = :idAmbito";
	
	private static final String QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_SOGGETTO =" select count(*) as num_statdeb from risca_t_riscossione rtr, risca_d_tipo_riscossione d, risca_t_stato_debitorio rtsd "
			+ " where rtsd.id_soggetto = :idOggetto "
			+ " and rtsd.id_riscossione = rtr.id_riscossione "
			+ " and rtr.id_tipo_riscossione = d.id_tipo_riscossione "
			+ " and d.id_ambito = :idAmbito ";

	private static final String QUERY_CONTROLLO_ASSOCIAZIONE_A_GRUPPO_A_RISCOSSIONE_SOGGETTO ="select count(*) as num_riscossioni  from risca_t_riscossione rtr, risca_d_tipo_riscossione d, risca_r_soggetto_gruppo grp "
			+ " where grp.id_soggetto  = :idOggetto "
			+ " and rtr.id_tipo_riscossione = d.id_tipo_riscossione "
			+ " and d.id_ambito = :idAmbito "
			+ " and rtr.id_gruppo_soggetto = grp.id_gruppo_soggetto ";
	
	private static final String QUERY_CONTROLLO_ASSOCIAZIONE_A_GRUPPO_A_STATO_DEBITORIO_SOGGETTO =" select count(*) as num_statdeb from risca_t_stato_debitorio rtsd, risca_t_riscossione rtr, risca_d_tipo_riscossione d, risca_r_soggetto_gruppo grp "
			+ " where grp.id_soggetto  = :idOggetto"
			+ " and rtr.id_tipo_riscossione = d.id_tipo_riscossione "
			+ " and d.id_ambito = :idAmbito"
			+ " and rtsd.id_gruppo_soggetto = grp.id_gruppo_soggetto" ;
	
	private static final String QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_RECAPITI=" select count(*) as num_riscossioni from risca_t_riscossione rtr, risca_d_tipo_riscossione d, risca_r_riscossione_recapito rec "
			+ " where rtr.id_tipo_riscossione = d.id_tipo_riscossione "
			+ " and d.id_ambito = :idAmbito "
			+ " and rec.id_recapito = :idOggetto "
			+ " and rec.id_riscossione = rtr.id_riscossione ";
	
	private static final String QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_RECAPITI="select count(*) as num_statdeb from risca_t_riscossione rtr, risca_d_tipo_riscossione d, risca_t_stato_debitorio rtsd "
			+ " where rtr.id_tipo_riscossione = d.id_tipo_riscossione "
			+ " and d.id_ambito = :idAmbito " 
			+ " and rtsd.id_recapito = :idOggetto "
			+ " and rtsd.id_riscossione = rtr.id_riscossione ";
	
	private static final String QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_GRUPPO="	select  count(*) as num_riscossioni  from risca_t_riscossione rtr, risca_d_tipo_riscossione d "
			+ " where rtr.id_gruppo_soggetto  =  :idOggetto "
			+ " and rtr.id_tipo_riscossione = d.id_tipo_riscossione "
			+ " and d.id_ambito = :idAmbito ";

	private static final String QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_GRUPPO_UPDATE_REFERENTE_SI="	select count(*) as num_statdeb from risca_t_riscossione rtr, risca_d_tipo_riscossione d, risca_t_stato_debitorio rtsd "
			+ " where rtsd.id_gruppo_soggetto  =  :idOggetto "
			+ " and rtsd.id_riscossione = rtr.id_riscossione "
			+ " and rtr.id_tipo_riscossione = d.id_tipo_riscossione "
			+ " and d.id_ambito =  :idAmbito "
			+ " and rtsd.id_attivita_stato_deb is null ";
	
	private static final String QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_GRUPPO ="select count(*) as num_statdeb from risca_t_riscossione rtr, risca_d_tipo_riscossione d, risca_t_stato_debitorio rtsd "
			+ " where rtsd.id_gruppo_soggetto  =  :idOggetto "
			+ " and rtsd.id_riscossione = rtr.id_riscossione "
			+ " and rtr.id_tipo_riscossione = d.id_tipo_riscossione "
			+ " and d.id_ambito = :idAmbito ";
			
//	private static final String QUERY_RIMBORSI_BY_ID_STATO_DEBITORIO_PADRE = "select rrr.id_rimborso from risca_r_rimborso rrr " + 
//			"where rrr.id_stato_debitorio = :idStatoDebitorioPadre";
//	private static final String QUERY_ID_STATO_DEBITORIO_BY_RIMBORSI = "select rrrsdu.id_stato_debitorio from risca_r_rimborso_sd_utilizzato rrrsdu " + 
//			"where rrrsdu.id_rimborso in (:listaRimborsi)";
//	private static final String QUERY_COD_UTENZA_BY_ID_STATO_DEBITORIO = "";
	private static final String QUERY_RISCOSSIONI_BY_ID_STATO_DEBITORIO = " select rtr.cod_riscossione ,rtsd.desc_periodo_pagamento  from risca_t_riscossione rtr \r\n"
			+ " left join risca_t_stato_debitorio rtsd on rtr.id_riscossione = rtsd.id_riscossione \r\n"
			+ "		where rtr.id_riscossione in (select rtsd.id_riscossione\r\n"
			+ "									 from risca_t_stato_debitorio rtsd  \r\n"
			+ "										where rtsd.id_stato_debitorio in (select rrr.id_stato_debitorio from risca_r_rimborso rrr \r\n"
			+ "										where rrr.id_rimborso in (select rrrsdu.id_rimborso from risca_r_rimborso_sd_utilizzato rrrsdu  \r\n"
			+ "										where rrrsdu.id_stato_debitorio = :idStatoDebitorio))\r\n"
			+ "										)\r\n"
			+ "		and rtsd.id_stato_debitorio in ( select rtsd.id_stato_debitorio \r\n"
			+ "									 from risca_t_stato_debitorio rtsd  \r\n"
			+ "										where rtsd.id_stato_debitorio in (select rrr.id_stato_debitorio from risca_r_rimborso rrr \r\n"
			+ "										where rrr.id_rimborso in (select rrrsdu.id_rimborso from risca_r_rimborso_sd_utilizzato rrrsdu  \r\n"
			+ "										where rrrsdu.id_stato_debitorio = :idStatoDebitorio))\r\n"
			+ "										)";
	
	
	@Override
	public Long saveRiscossione(RiscossioneDTO dto,String fruitore, Long idAmbito) throws BusinessException, Exception{
		LOGGER.debug("[RiscossioneDAOImpl::saveRiscossione] BEGIN");
	String ambito = "";
	Long idRiscossione = null;
	Utils utils = new Utils();
    if(utils.isLocalMod()){
    	ambito = AMBIENTE;
	    idRiscossione = riscAmbienteDaoImpl.saveRiscossione(dto, fruitore);
	}else {
			//TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
			LOGGER.debug("[RiscossioneDAOImpl::saveRiscossione] verifica ambito");
			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			ambito = ambitoDTO.getCodAmbito();
			LOGGER.debug("[RiscossioneDAOImpl::saveRiscossione] ambito: " + ambito);
			switch (ambito) {
			  case AMBIENTE:
				LOGGER.debug("[RiscossioneDAOImpl::saveRiscossione] ambito: AMBIENTE");
		    	idRiscossione = riscAmbienteDaoImpl.saveRiscossione(dto, fruitore);
			    break;
			  case OPERE_PUBBLICHE:
				//TO DO
			    break;
			  case ATTIVITA_ESTRATTIVE:
				//TO DO
			    break;
			  case TRIBUTI:
				  LOGGER.debug("[RiscossioneDAOImpl::saveRiscossione] ambito: TRIBUTI");
				  idRiscossione = riscTributiDaoImpl.saveRiscossione(dto, fruitore);
				  break;
			}
		}	   
		LOGGER.debug("[RiscossioneDAOImpl::saveRiscossione] END");
        return idRiscossione;

	}




	@Override
	public Long updateRiscossione(RiscossioneDTO dto, Long idAmbito) throws BusinessException, Exception {
		LOGGER.debug("[RiscossioneDAOImpl::updateRiscossione] BEGIN");
		String ambito = "";
		Long idRiscossione = null;
		Utils utils = new Utils();
    	if(utils.isLocalMod()){
    		ambito = AMBIENTE;
		    idRiscossione = riscAmbienteDaoImpl.updateRiscossione(dto);
		}else {
				AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
				ambito = ambitoDTO.getCodAmbito();
				switch (ambito) {
				  case AMBIENTE:
			    		idRiscossione = riscAmbienteDaoImpl.updateRiscossione(dto);
				    break;
				  case OPERE_PUBBLICHE:
					//TO DO
				    break;
				  case ATTIVITA_ESTRATTIVE:
					//TO DO
				    break;
				  case TRIBUTI:
					  LOGGER.debug("[RiscossioneDAOImpl::updateRiscossione] ambito: TRIBUTI");
					  idRiscossione = riscTributiDaoImpl.updateRiscossione(dto);
					  break;
				}
			}	    
			LOGGER.debug("[RiscossioneDAOImpl::updateRiscossione] END");
	        return idRiscossione;
		

	}



	public List<RiscossioneRecapitoDTO> checkRiscossioneRecapiti(Long idRiscossione) {
		LOGGER.debug("[RiscossioneDAOImpl::checkRiscossioneRecapiti] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();

			List<RiscossioneRecapitoDTO> risc = new ArrayList<RiscossioneRecapitoDTO>();
			map.put("idRiscossione", idRiscossione);

			MapSqlParameterSource params = getParameterValue(map);

			risc = template.query(QUERY_CHECK_RISCOSSIONE_RECAPITI, params, getRiscossioneRecapitoRowMapper());

			return risc;

		} catch (SQLException e) {
			LOGGER.error("[RiscossioneDAOImpl::checkRiscossioneRecapiti] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneDAOImpl::checkRiscossioneRecapiti] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[RiscossioneDAOImpl::checkRiscossioneRecapiti] END");
		}
	}

	@Override
	public RiscossioneDTO getRiscossione(String codRiscossione) throws SQLException {
		LOGGER.debug("[RiscossioneDAOImpl::getRiscossione] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();

			int id = 0;
			boolean bool = false;
			try {
				id = Integer.parseInt(codRiscossione);
				bool = true;
			} catch (NumberFormatException nfe) {
				bool = false;
			}

			RiscossioneDTO riscossione = new RiscossioneDTO();
			SoggettiExtendedDTO soggetto = new SoggettiExtendedDTO();
			GruppiDTO gruppo = new GruppiDTO();
			TipiAutorizzazioneExtendedDTO tipoAutorizza = new TipiAutorizzazioneExtendedDTO();
			StatiRiscossioneDTO statiRiscossione = new StatiRiscossioneDTO();
			MapSqlParameterSource params;
			if (bool) {
				map.put("idRiscossione", id);
				params = getParameterValue(map);
				riscossione = (RiscossioneDTO) template.queryForObject(QUERY_RISCOSSIONE_BY_ID_RISCOSSIONE, params,
						getRowMapper());

			} else {
				map.put("codRiscossione", codRiscossione);
				params = getParameterValue(map);
				riscossione = template.queryForObject(QUERY_RISCOSSIONE_BY_COD_RISCOSSIONE, params, getRowMapper());

			}
			map.put("idRiscossione", riscossione.getIdRiscossione());
			params = getParameterValue(map);
			List<RiscossioneRecapitoDTO> riscRecap = template.query(QUERY_CHECK_RISCOSSIONE_RECAPITI, params,
					getRiscossioneRecapitoRowMapper());
			List<RecapitiExtendedDTO> listRecapiti = new ArrayList<RecapitiExtendedDTO>();
//			for (int i = 0; i < riscRecap.size(); i++) {
//				map.put("idRecapito", riscRecap.get(i).getIdRecapito());
//				params = getParameterValue(map);
//				recap = (RecapitiExtendedDTO) template.queryForObject(QUERY_RECAPITI_BY_ID_RECAPITO, params,
//						getRecapitiRowMapper());
//				recapiti.add(recap);
			if(riscRecap != null && riscRecap.size()>0) {
				listRecapiti = template.query(QUERY_RECAPITI_BY_ID_RISCOSSIONE, params,
						getRecapitiRowMapper());

            	if(listRecapiti!=null) {
            		
            		TipiInvioDTO tipoInvio = new TipiInvioDTO();
	        		FonteDTO fonte = new FonteDTO();
	        		TipiRecapitoDTO tipiRecapito = new TipiRecapitoDTO();
	        		ComuneExtendedDTO comune = new ComuneExtendedDTO();
	        		NazioniDTO nazione = new NazioniDTO();
	        		for(int j = 0; j<listRecapiti.size(); j++) {
		        		map.put("idTipoInvio", listRecapiti.get(j).getIdTipoInvio());
		        		map.put("idTipoRecapito", listRecapiti.get(j).getIdTipoRecapito());
		        		map.put("idComune", listRecapiti.get(j).getIdComuneRecapito());
		        		map.put("idNazione", listRecapiti.get(j).getIdNazioneRecapito());
		        		map.put("idFonte", listRecapiti.get(j).getIdFonte());
		        		params = getParameterValue(map);
		        		try {
							tipoInvio = template.queryForObject(QUERY_LOAD_TIPI_INVIO, params, getTipiInvioRowMapper());
							listRecapiti.get(j).setTipoInvio(tipoInvio);
		        		} catch (Exception e) {
							LOGGER.debug("[RiscossioneDAOImpl::getRiscossione] Nessun tipo invio presente o errore nell'accesso al db");
						}
		        		
						try {
							tipiRecapito = template.queryForObject(QUERY_LOAD_TIPI_RECAPITO, params, getTipoRecapitoRowMapper());
							listRecapiti.get(j).setTipoRecapito(tipiRecapito);
						} catch (Exception e) {
							LOGGER.debug("[RiscossioneDAOImpl::getRiscossione] Nessun tipo recapito presente o errore nell'accesso al db");
						}
						try {
							comune = template.queryForObject(QUERY_LOAD_COMUNE, params, getComuneRowMapper());
							listRecapiti.get(j).setComuneRecapito(comune);
						} catch (Exception e) {
							LOGGER.debug("[RiscossioneDAOImpl::getRiscossione] Nessun comune presente o errore nell'accesso al db");
						}
						try {
							nazione = template.queryForObject(QUERY_LOAD_NAZIONE, params, getNazioneRowMapper());
							listRecapiti.get(j).setNazioneRecapito(nazione);
						} catch (Exception e) {
							LOGGER.debug("[RiscossioneDAOImpl::getRiscossione] Nessuna nazione presente o errore nell'accesso al db");
						}
						try {
							fonte = template.queryForObject(QUERY_LOAD_FONTE, params, getFonteRowMapper());
							listRecapiti.get(j).setFonte(fonte);
						} catch (Exception e) {
							LOGGER.debug("[RiscossioneDAOImpl::getRiscossione] Nessuna fonte presente o errore nell'accesso al db");
						}
    			
	        		}

        		}
    			riscossione.setRecapiti(listRecapiti);			
			}



			if (riscossione.getIdSoggetto() != null) {
				map.put("idSoggetto", riscossione.getIdSoggetto());
				params = getParameterValue(map);
				try {
					soggetto = soggettiDAO.loadSoggettoById(riscossione.getIdSoggetto());
				} catch (Exception e) {
					LOGGER.error("[RiscossioneDAOImpl::getRiscossione] Errore nell'esecuzione della query", e);
				
				}
//				soggetto = template.queryForObject(QUERY_SOGGETTO_BY_ID, params, getSoggettoRowMapper());
			}
			riscossione.setSoggetto(soggetto);

			if (riscossione.getIdGruppoSoggetto() != null && riscossione.getIdGruppoSoggetto() != 0) {
				map.put("idGruppoSoggetto", riscossione.getIdGruppoSoggetto());
				params = getParameterValue(map);
				gruppo = template.queryForObject(QUERY_GRUPPO_BY_ID, params, getGruppoRowMapper());
            	final Long idGruppoSoggetto = gruppo.getIdGruppoSoggetto();
            	
            	List<GruppiSoggettoDTO> listGruppiSoggetto = null;
				try {
					listGruppiSoggetto = soggettoGruppoDAO.loadSoggettoGruppoByIdGruppoSoggetto(idGruppoSoggetto);
				} catch (Exception e) {
					LOGGER.error("[RiscossioneDAOImpl::getRiscossione] Errore nell'esecuzione della query listGruppiSoggetto", e);
				}
            	gruppo.setComponentiGruppo(listGruppiSoggetto);
				riscossione.setGruppoSoggetto(gruppo);
			}

			

			if (riscossione.getIdTipoAutorizza() != null) {
				map.put("idTipoAutorizza", riscossione.getIdTipoAutorizza());
				params = getParameterValue(map);
				tipoAutorizza = template.queryForObject(QUERY_TIPO_AUTORIZZA_BY_ID, params,
						getTipoAutorizzaRowMapper());
			}
			riscossione.setTipoAutorizza(tipoAutorizza);

			if (riscossione.getIdStatoRiscossione() != null) {
				map.put("idStatoRiscossione", riscossione.getIdStatoRiscossione());
				params = getParameterValue(map);
				statiRiscossione = template.queryForObject(QUERY_STATO_RISCOSSIONE_BY_ID_STATO, params,
						getStatiRiscossioneRowMapper());
			}
			
			map.put("idRiscossione", riscossione.getIdRiscossione());
			params = getParameterValue(map);
			List<ProvvedimentoDTO> riscProvvedi = template.query(QUERY_PROVVVEDIMENTO_BY_ID_RISCOSSIONE, params,
					getProvvedimentoRowMapper());
			riscossione.setProvvedimento(riscProvvedi);
			riscossione.setStatiRiscossione(statiRiscossione);

			return riscossione;

		} catch (SQLException e) {
			LOGGER.error("[RiscossioneDAOImpl::getRiscossione] Errore nell'esecuzione della query", e);
			throw new SQLException(Constants.ERRORE_GENERICO);

		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneDAOImpl::getRiscossione] Errore nell'accesso ai dati", e);
			throw new SQLException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RiscossioneDAOImpl::getRiscossione] END");
		}
	}
	
	public Long getIdSoggettoRiscossione(Long idRiscossione) throws SQLException {
		LOGGER.debug("[RiscossioneDAOImpl::getIdSoggettoRiscossione] BEGIN");
		Long idSoggetto = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRiscossione", idRiscossione);
			MapSqlParameterSource params = getParameterValue(map);
			idSoggetto = template.queryForObject(QUERY_SOGG_RISC_BY_ID_RISCOSSIONE, params,
					getLongRowMapper("id_soggetto"));
		} catch (SQLException e) {
			LOGGER.error("[RiscossioneDAOImpl::getIdSoggettoRiscossione] Errore nell'esecuzione della query", e);
			throw new SQLException(Constants.ERRORE_GENERICO);
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneDAOImpl::getIdSoggettoRiscossione] Errore nell'accesso ai dati", e);
			throw new SQLException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RiscossioneDAOImpl::getIdSoggettoRiscossione] END");
		}
		return idSoggetto;
	}

	@Override
	public List<RiscossioneDTO> getRiscossioniGruppo(String codGruppo) throws SQLException {
		LOGGER.debug("[RiscossioneDAOImpl::getRiscossioniGruppo] BEGIN");
		List<RiscossioneDTO> listRiscossioniGruppo = new ArrayList<RiscossioneDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			int id = 0;
			boolean bool = false;
			try {
				id = Integer.parseInt(codGruppo);
				bool = true;
			} catch (NumberFormatException nfe) {
				bool = false;
			}

			if (bool) {
				map.put("idGruppo", id);
				MapSqlParameterSource params = getParameterValue(map);
				listRiscossioniGruppo = template.query(QUERY_RISCOSSIONI_BY_ID_GRUPPO, params, getRowMapper());
				return listRiscossioniGruppo;
			} else {
				map.put("codGruppo", codGruppo);
				MapSqlParameterSource params = getParameterValue(map);
				listRiscossioniGruppo = template.query(QUERY_RISCOSSIONI_BY_ID_GRUPPO, params, getRowMapper()); // CHIARIRE
																												// CON
																												// BARBARA
																												// ALGORITMO
																												// 20
				return listRiscossioniGruppo;
			}
		} catch (SQLException e) {
			LOGGER.error("[RiscossioneDAOImpl::getRiscossioniGruppo] Errore nell'esecuzione della query", e);
			throw new SQLException(Constants.ERRORE_GENERICO);
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneDAOImpl::getRiscossioniGruppo] Errore nell'accesso ai dati", e);
			throw new SQLException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RiscossioneDAOImpl::getRiscossioniGruppo] END");
		}
	}


	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<RiscossioneDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new RiscossioneRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * The type riscossione row mapper.
	 */
	public static class RiscossioneRowMapper implements RowMapper<RiscossioneDTO> {

		/**
		 * Instantiates a new Tipo riscossione row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RiscossioneRowMapper() throws SQLException {
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
		public RiscossioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RiscossioneDTO bean = new RiscossioneDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RiscossioneDTO bean) throws SQLException {
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setIdTipoRiscossione(rs.getLong("id_tipo_riscossione"));
			bean.setIdComponenteDt(rs.getLong("id_componente_dt"));
			bean.setIdStatoRiscossione(rs.getLong("id_stato_riscossione"));
			bean.setCodRiscossione(rs.getString("cod_riscossione"));
			bean.setCodRiscossioneProv(rs.getString("cod_riscossione_prov"));
			bean.setCodRiscossioneProg(rs.getString("cod_riscossione_prog"));
			bean.setIdSiglaRiscossione(rs.getLong("id_sigla_riscossione"));
			bean.setCodRiscossioneLetteraProv(rs.getString("cod_riscossione_lettera_prov"));
			bean.setNumPratica(rs.getString("num_pratica"));
			bean.setFlgPrenotata(rs.getInt("flg_prenotata"));
			bean.setMotivoPrenotazione(rs.getString("motivo_prenotazione"));
			bean.setNoteRiscossione(rs.getString("note_riscossione"));
			bean.setDataIniConcessione(rs.getString("data_ini_concessione"));
			bean.setDataScadConcessione(rs.getString("data_scad_concessione"));
			bean.setDataIniSospCanone(rs.getString("data_ini_sosp_canone"));
			bean.setDataFineSospCanone(rs.getString("data_fine_sosp_canone"));
			bean.setJsonDt(rs.getString("json_dt"));
			bean.setGestDataIns(rs.getTimestamp("gest_data_ins"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataUpd(rs.getTimestamp("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
			bean.setIdTipoAutorizza(rs.getLong("id_tipo_autorizza"));
			bean.setDataInizioTitolarita(rs.getString("data_inizio_titolarita"));
			bean.setDataRinunciaRevoca(rs.getString("data_rinuncia_revoca"));

		}
	}

	public RowMapper<RiscossioneRecapitoDTO> getRiscossioneRecapitoRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new RiscossioneRecapitoRowMapper();
	}

	/**
	 * The type riscossione recapito row mapper.
	 */
	public static class RiscossioneRecapitoRowMapper implements RowMapper<RiscossioneRecapitoDTO> {

		/**
		 * Instantiates a new riscossione recapito row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RiscossioneRecapitoRowMapper() throws SQLException {
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
		public RiscossioneRecapitoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RiscossioneRecapitoDTO bean = new RiscossioneRecapitoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RiscossioneRecapitoDTO bean) throws SQLException {
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setIdRecapito(rs.getLong("id_recapito"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}
	}

	@Override
	public Integer checkRecapitoRiscossione(String idRiscossione) {
		// TODO Auto-generated method stub
		return null;
	}

	public RowMapper<RecapitiExtendedDTO> getRecapitiRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new RecapitiRowMapper();
	}

	/**
	 * The type recapiti row mapper.
	 */
	public static class RecapitiRowMapper implements RowMapper<RecapitiExtendedDTO> {

		/**
		 * Instantiates a new recapiti row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RecapitiRowMapper() throws SQLException {
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
		public RecapitiExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RecapitiExtendedDTO bean = new RecapitiExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RecapitiDTO bean) throws SQLException {
			bean.setIdRecapito(rs.getLong("id_recapito"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			bean.setIdTipoRecapito(rs.getLong("id_tipo_recapito"));
			bean.setIdTipoInvio(rs.getLong("id_tipo_invio"));
			bean.setIdComuneRecapito(rs.getLong("id_comune_recapito"));
			bean.setIdNazioneRecapito(rs.getLong("id_nazione_recapito"));
			bean.setIdFonte(rs.getLong("id_fonte"));
			bean.setIdFonteOrigine(rs.getLong("id_fonte_origine"));
			bean.setCodRecapito(rs.getString("cod_recapito"));
			bean.setCodRecapitoFonte(rs.getString("cod_recapito_fonte"));
			bean.setIndirizzo(rs.getString("indirizzo"));
			bean.setNumCivico(rs.getString("num_civico"));
			bean.setEmail(rs.getString("email"));
			bean.setPec(rs.getString("pec"));
			bean.setTelefono(rs.getString("telefono"));
			bean.setPresso(rs.getString("presso"));
			bean.setCittaEsteraRecapito(rs.getString("citta_estera_recapito"));
			bean.setCapRecapito(rs.getString("cap_recapito"));
			bean.setDesLocalita(rs.getString("des_localita"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			bean.setCellulare(rs.getString("cellulare"));
			bean.setIdTipoSede(rs.getLong("id_tipo_sede"));
			bean.setDataAggiornamento(rs.getString("data_aggiornamento"));
			bean.setDataCancellazione(rs.getString("data_cancellazione"));
		}

	}

	public RowMapper<GruppiDTO> getGruppoRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new GruppoRowMapper();
	}

	/**
	 * The type gruppo row mapper.
	 */
	public static class GruppoRowMapper implements RowMapper<GruppiDTO> {

		/**
		 * Instantiates a new gruppo row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public GruppoRowMapper() throws SQLException {
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
		public GruppiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			GruppiDTO bean = new GruppiDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, GruppiDTO bean) throws SQLException {
			bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
			bean.setCodGruppoSoggetto(rs.getString("cod_gruppo_soggetto"));
			bean.setCodGruppoFonte(rs.getString("cod_gruppo_fonte"));
			bean.setDesGruppoSoggetto(rs.getString("des_gruppo_soggetto"));
			bean.setIdFonte(rs.getLong("id_fonte"));
			bean.setIdFonteOrigine(rs.getLong("id_fonte_origine"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			bean.setDataAggiornamento(rs.getString("data_aggiornamento"));
			bean.setDataCancellazione(rs.getString("data_cancellazione"));
		}
	}

	public RowMapper<SoggettiExtendedDTO> getSoggettoRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new SoggettiRowMapper();
	}

	/**
	 * The type soggetti row mapper.
	 */
	public static class SoggettiRowMapper implements RowMapper<SoggettiExtendedDTO> {

		/**
		 * Instantiates a new soggetti row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SoggettiRowMapper() throws SQLException {
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
		public SoggettiExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SoggettiExtendedDTO bean = new SoggettiExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SoggettiDTO bean) throws SQLException {
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			bean.setIdAmbito(rs.getLong("id_ambito"));
			bean.setIdTipoSoggetto(rs.getLong("id_tipo_soggetto"));
			bean.setCfSoggetto(rs.getString("cf_soggetto"));
			bean.setIdTipoNaturaGiuridica(rs.getLong("id_tipo_natura_giuridica"));
			bean.setIdFonteOrigine(rs.getLong("id_fonte_origine"));
			bean.setIdFonte(rs.getLong("id_fonte"));
			bean.setNome(rs.getString("nome"));
			bean.setCognome(rs.getString("cognome"));
			bean.setDenSoggetto(rs.getString("den_soggetto"));
			bean.setPartitaIvaSoggetto(rs.getString("partita_iva_soggetto"));
			bean.setDataNascitaSoggetto(rs.getString("data_nascita_soggetto"));
			bean.setIdComuneNascita(rs.getInt("id_comune_nascita"));
			bean.setIdStatoNascita(rs.getInt("id_stato_nascita"));
			bean.setCittaEsteraNascita(rs.getString("citta_estera_nascita"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			bean.setDataAggiornamento(rs.getString("data_aggiornamento"));
			bean.setDataCancellazione(rs.getString("data_cancellazione"));
		}
	}

	public RowMapper<TipiAutorizzazioneExtendedDTO> getTipoAutorizzaRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new TipiAutorizzazioneRowMapper();
	}

	/**
	 * The type Tipo Autorizzazione row mapper.
	 */
	public static class TipiAutorizzazioneRowMapper implements RowMapper<TipiAutorizzazioneExtendedDTO> {

		/**
		 * Instantiates a new Tipo Autorizzazione row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public TipiAutorizzazioneRowMapper() throws SQLException {
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
		public TipiAutorizzazioneExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TipiAutorizzazioneExtendedDTO bean = new TipiAutorizzazioneExtendedDTO();
            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
			populateBean(rs, bean);
			return bean;
		}

		private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setCodAmbito(rs.getString("cod_ambito"));
            bean.setDesAmbito(rs.getString("des_ambito"));
			
		}

		private void populateBean(ResultSet rs, TipiAutorizzazioneExtendedDTO bean) throws SQLException {
			bean.setIdTipoAutorizza(rs.getLong("id_tipo_autorizza"));
			bean.setCodTipoAutorizza(rs.getString("cod_tipo_autorizza"));
			bean.setDesTipoAutorizza(rs.getString("des_tipo_autorizza"));
		}
	}
	
	
	public RowMapper<ProvvedimentoDTO> getProvvedimentoRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new ProvvedimentoRowMapper();
	}

	/**
	 * The type Provvedimento Row Mapper.
	 */
	public class ProvvedimentoRowMapper implements RowMapper<ProvvedimentoDTO> {

		@Override
		public ProvvedimentoDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			ProvvedimentoDTO bean = new ProvvedimentoDTO();
            populateBeanExtended(rs, bean);
            return bean;

		}

		private void populateBeanExtended(ResultSet rs, ProvvedimentoDTO bean) throws SQLException {
	
			//bean.setIdTipoProvvedimento(rs.getLong("id_tipo_provvedimento"));
			//bean.setIdTipoTitolo(rs.getLong("id_tipo_titolo"));
			bean.setIdProvvedimento(rs.getLong("id_provvedimento"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setNumTitolo(rs.getString("num_titolo"));
			bean.setDataProvvedimento(rs.getString("data_provvedimento"));
			bean.setNote(rs.getString("note"));
			
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
            TipiTitoloExtendedDTO tipoTitolo = new TipiTitoloExtendedDTO();
            populateBeanTipoTitolo(rs, tipoTitolo);
            bean.setTipoTitoloExtendedDTO(tipoTitolo);
            
            TipiProvvedimentoExtendedDTO tipoProvvedimento = new TipiProvvedimentoExtendedDTO();
            populateBeanTipoProvvedimento(rs, tipoProvvedimento);
            bean.setTipiProvvedimentoExtendedDTO(tipoProvvedimento);
		}
		
        private void populateBeanTipoTitolo(ResultSet rs, TipiTitoloExtendedDTO bean) throws SQLException {
        	bean.setIdTipoTitolo(rs.getLong("id_tipo_titolo"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
            bean.setCodTipoTitolo(rs.getString("cod_tipo_titolo"));
            bean.setDesTipoTitolo(rs.getString("des_tipo_titolo"));
            
        }
        
        private void populateBeanTipoProvvedimento(ResultSet rs, TipiProvvedimentoExtendedDTO bean) throws SQLException {
        	bean.setIdTipoProvvedimento(rs.getLong("id_tipo_provvedimento"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
            bean.setCodTipoProvvedimento(rs.getString("cod_tipo_provvedimento"));
            bean.setDesTipoProvvedimento(rs.getString("des_tipo_provvedimento"));
            bean.setFlgIstanza(rs.getString("flg_istanza"));
        }
        
        private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setCodAmbito(rs.getString("cod_ambito"));
            bean.setDesAmbito(rs.getString("des_ambito"));
        }

	}
	
	
	public RowMapper<StatiRiscossioneDTO> getStatiRiscossioneRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new StatiRiscossioneRowMapper();
	}

	/**
	 * The type stati riscossione row mapper.
	 */
	public static class StatiRiscossioneRowMapper implements RowMapper<StatiRiscossioneDTO> {

		/**
		 * Instantiates a new Tipo adempimento row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public StatiRiscossioneRowMapper() throws SQLException {
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
		public StatiRiscossioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			StatiRiscossioneDTO bean = new StatiRiscossioneDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, StatiRiscossioneDTO bean) throws SQLException {
			bean.setIdStatoRiscossione(rs.getLong("id_stato_riscossione"));
			bean.setIdAmbito(rs.getLong("id_ambito"));
			bean.setCodStatoRiscossione(rs.getString("cod_stato_riscossione"));
			bean.setDesStatoRiscossione(rs.getString("des_stato_riscossione"));
			bean.setFlgDefault(rs.getInt("flg_default"));
		}
	}

	@Override
	public List<RiscossioneSearchResultDTO> searchRiscossioni(RiscossioneSearchDTO dto, Long idAmbito,String modalitaRicerca , Integer offset,Integer limit, String sort) throws Exception {
		LOGGER.debug("[RiscossioneDAOImpl::searchRiscossione] BEGIN");
		List<RiscossioneSearchResultDTO> result = new ArrayList<RiscossioneSearchResultDTO>();
		String ambito = "";
		Utils utils = new Utils();
    	if(utils.isLocalMod()){
    		if(dto.getIdAmbito() != null && dto.getIdAmbito().equals(1L)) {
    	    	result = riscAmbienteDaoImpl.searchRiscossioni(dto, dto.getIdAmbito(), modalitaRicerca,  offset, limit, sort);

    		}else if(dto.getIdAmbito() != null && dto.getIdAmbito().equals(4L)) {
				result = riscTributiDaoImpl.searchRiscossioni(dto, dto.getIdAmbito(), modalitaRicerca, offset, limit, sort);

    		}else {
    	    	result = riscAmbienteDaoImpl.searchRiscossioni(dto, 1L,modalitaRicerca,  offset, limit, sort);
    		}
	    	
		}
		else {
			//TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			ambito = ambitoDTO.getCodAmbito();
			switch (ambito) {
			  case AMBIENTE:
			    result = riscAmbienteDaoImpl.searchRiscossioni(dto, ambitoDTO.getIdAmbito(),modalitaRicerca, offset, limit, sort);
			    break;
			  case OPERE_PUBBLICHE:
				//TO DO
			    break;
			  case ATTIVITA_ESTRATTIVE:
				//TO DO
			    break;
			  case TRIBUTI:
				result = riscTributiDaoImpl.searchRiscossioni(dto,ambitoDTO.getIdAmbito(), modalitaRicerca, offset, limit, sort);
			    break;
			}
		}	    
		LOGGER.debug("[RiscossioneDAOImpl::searchRiscossione] END");
		return result;

	}



	@Override
	public String loadDatoTecnico(Long idRiscossione) throws Exception {
		LOGGER.debug("[DatoTecnicoDAOImpl::loadDatoTecnico] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params;
			map.put("idRiscossione", idRiscossione);
			params = getParameterValue(map);
			RiscossioneDTO risc = new RiscossioneDTO();
			risc = template.queryForObject(QUERY_DATO_TECNICO_BY_ID_RISCOSSIONE, params, getRowMapper());
			String json = risc.getJsonDt();
			// String result = "\"" + json + "\"";
			return json;

		} catch (SQLException e) {
			LOGGER.error("[DatoTecnicoDAOImpl::loadDatoTecnico] Errore nell'esecuzione della query", e);
			throw new Exception(e);
		} catch (DataAccessException e) {
			LOGGER.error("[DatoTecnicoDAOImpl::loadDatoTecnico] Errore nell'accesso ai dati", e);
			throw new Exception(e);
		} finally {
			LOGGER.debug("[DatoTecnicoDAOImpl::loadDatoTecnico] END");
		}
	}

	public RowMapper<RiscossioneSearchResultDTO> getRowMapperRiscossioneSearch() throws SQLException {
		return new RiscossioneSearchRowMapper();
	}

	/**
	 * The type RiscossioneSearch row mapper.
	 */
	public static class RiscossioneSearchRowMapper implements RowMapper<RiscossioneSearchResultDTO> {

		/**
		 * Instantiates a new RiscossioneSearchResultDTO row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RiscossioneSearchRowMapper() throws SQLException {
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
		public RiscossioneSearchResultDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RiscossioneSearchResultDTO bean = new RiscossioneSearchResultDTO();
			try {
				populateBean(rs, bean);
			} catch (SQLException | IOException e) {
				LOGGER.error(
						"[RiscossioneDAOImpl::searchRiscossione::mapRow::populateBean] Errore nella popolazione bean",
						e);
			}
			return bean;
		}

		private void populateBean(ResultSet rs, RiscossioneSearchResultDTO bean)
				throws SQLException, JsonParseException, JsonMappingException, IOException {
			bean.setCodiceUtenza(rs.getString("codiceutenza"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setNumeroPratica(rs.getString("num_pratica"));
			bean.setProcedimento(rs.getString("procedimento"));
			bean.setTitolare(rs.getString("titolare"));
			bean.setStato(rs.getString("stato"));
			DatiTecniciAmbienteDTO datiTecniciAmbiente = null;
			if (rs.getString("json_dt") != null) {
				datiTecniciAmbiente = Utils.creaDatiTecniciFromJsonDt(rs.getString("json_dt"), "dati_tecnici");
				bean.setComuneOperaDiPresa(datiTecniciAmbiente.getDatiGenerali().getComune());
				bean.setCorpoIdrico(datiTecniciAmbiente.getDatiGenerali().getCorpoIdricoCaptazione());
				bean.setNomeImpianto(datiTecniciAmbiente.getDatiGenerali().getNomeImpiantoIdrico());

			}

		}
	
	}


	
	
	public RowMapper<TipiInvioDTO> getTipiInvioRowMapper() throws SQLException {
		 return new TipoInvioRowMapper();
	}

   /**
    * The type Tipo invio row mapper.
    */
   public static class TipoInvioRowMapper implements RowMapper<TipiInvioDTO> {

       /**
        * Instantiates a new Tipo invio row mapper.
        *
        * @throws SQLException the sql exception
        */
       public TipoInvioRowMapper() throws SQLException {
           // Instantiate class
       }

       /**
        * Implementations must implement this method to map each row of data
        * in the ResultSet. This method should not call {@code next()} on
        * the ResultSet; it is only supposed to map values of the current row.
        *
        * @param rs     the ResultSet to map (pre-initialized for the current row)
        * @param rowNum the number of the current row
        * @return the result object for the current row (may be {@code null})
        * @throws SQLException if a SQLException is encountered getting
        *                      column values (that is, there's no need to catch SQLException)
        */
       @Override
       public TipiInvioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
           TipiInvioDTO bean = new TipiInvioDTO();
           populateBean(rs, bean);
           return bean;
       }

       private void populateBean(ResultSet rs, TipiInvioDTO bean) throws SQLException {
           bean.setIdTipoInvio(rs.getLong("id_tipo_invio"));
           bean.setCodTipoInvio(rs.getString("cod_tipo_invio"));
           bean.setDesTipoInvio(rs.getString("des_tipo_invio"));
           bean.setOrdinaTipoInvio(rs.getLong("ordina_tipo_invio"));
       }
   }

   
	public RowMapper<NazioniDTO> getNazioneRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new NazioniRowMapper();
	}
   
   /**
    * The type Nazioni row mapper.
    */
   public static class NazioniRowMapper implements RowMapper<NazioniDTO> {

       /**
        * Instantiates a new Nazioni extended row mapper.
        *
        * @throws SQLException the sql exception
        */
       public NazioniRowMapper() throws SQLException {
           // Instantiate class
       }

       /**
        * Implementations must implement this method to map each row of data
        * in the ResultSet. This method should not call {@code next()} on
        * the ResultSet; it is only supposed to map values of the current row.
        *
        * @param rs     the ResultSet to map (pre-initialized for the current row)
        * @param rowNum the number of the current row
        * @return the result object for the current row (may be {@code null})
        * @throws SQLException if a SQLException is encountered getting
        *                      column values (that is, there's no need to catch SQLException)
        */
       @Override
       public NazioniDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
       	NazioniDTO bean = new NazioniDTO();
           populateBean(rs, bean);
           return bean;
       }

       
       private void populateBean(ResultSet rs, NazioniDTO bean) throws SQLException {
           bean.setIdNazione(rs.getLong("id_nazione"));
           bean.setCodIstatNazione(rs.getString("cod_istat_nazione"));
           bean.setCodBelfioreNazione(rs.getString("cod_belfiore_nazione"));
           bean.setDenomNazione(rs.getString("denom_nazione"));
           bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
           bean.setDataFineValidita(rs.getDate("data_fine_validita"));
           bean.setDtIdStato(rs.getLong("dt_id_stato"));
           bean.setDtIdStatoPrev(rs.getLong("dt_id_stato_prev"));
           bean.setDtIdStatoNext(rs.getLong("dt_id_stato_next"));
           bean.setCodIso2(rs.getString("cod_iso2"));
           
       }
       
   }
   
   
	public RowMapper<ComuneExtendedDTO> getComuneRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new ComuniRowMapper();
	}
   /**
    * The type Comune row mapper.
    */
   public static class ComuniRowMapper implements RowMapper<ComuneExtendedDTO> {

       /**
        * Instantiates a new Provincia row mapper.
        *
        * @throws SQLException the sql exception
        */
       public ComuniRowMapper() throws SQLException {
           // Instantiate class
       }

       /**
        * Implementations must implement this method to map each row of data
        * in the ResultSet. This method should not call {@code next()} on
        * the ResultSet; it is only supposed to map values of the current row.
        *
        * @param rs     the ResultSet to map (pre-initialized for the current row)
        * @param rowNum the number of the current row
        * @return the result object for the current row (may be {@code null})
        * @throws SQLException if a SQLException is encountered getting
        *                      column values (that is, there's no need to catch SQLException)
        */
       @Override
       public ComuneExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
         	 ComuneExtendedDTO bean = new ComuneExtendedDTO();
             populateBean(rs, bean);
             return bean;
       }

       private void populateBean(ResultSet rs, ComuneExtendedDTO bean) throws SQLException {
           bean.setIdComune(rs.getLong("id_comune"));
           bean.setCodIstatComune(rs.getString("cod_istat_comune"));
           bean.setCodBelfioreComune(rs.getString("cod_belfiore_comune"));
           bean.setDenomComune(rs.getString("denom_comune"));
           bean.setIdProvincia(rs.getLong("id_provincia"));
           bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
           bean.setDataFineValidita(rs.getDate("data_fine_validita"));
           bean.setDtIdComune(rs.getLong("dt_id_comune"));
           bean.setDtIdComunePrev(rs.getLong("dt_id_comune_prev"));
           bean.setDtIdComuneNext(rs.getLong("dt_id_comune_next"));
           bean.setCapComune(rs.getString("cap_comune"));
           ProvinciaExtendedDTO ProvinciaExtendedDTO = new ProvinciaExtendedDTO();
           populateBeanProvincia(rs, ProvinciaExtendedDTO);
           bean.setProvincia(ProvinciaExtendedDTO);
           
       }

  	private void populateBeanProvincia(ResultSet rs, ProvinciaExtendedDTO bean) throws SQLException{
          bean.setIdProvincia(rs.getLong("id_provincia"));
          bean.setCodProvincia(rs.getString("cod_provincia"));
          bean.setDenomProvincia(rs.getString("denom_provincia"));
          bean.setSiglaProvincia(rs.getString("sigla_provincia"));
          bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
          bean.setDataFineValidita(rs.getDate("data_fine_validita"));
    
          RegioneExtendedDTO regione = new RegioneExtendedDTO();
          populateBeanRegione(rs, regione);
          bean.setRegione(regione);
  	}
      private void populateBeanRegione(ResultSet rs, RegioneExtendedDTO bean) throws SQLException {
          bean.setIdRegione(rs.getLong("id_regione"));
          bean.setCodRegione(rs.getString("cod_regione"));
          bean.setDenomRegione(rs.getString("denom_regione"));
          //bean.setIdNazione(rs.getLong("id_nazione"));
          bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
          bean.setDataFineValidita(rs.getDate("data_fine_validita"));
          
          NazioneDTO nazione = new NazioneDTO();
          populateBeanNazione(rs, nazione);
          bean.setNazione(nazione);
      }
      
      private void populateBeanNazione(ResultSet rs, NazioneDTO bean) throws SQLException {
          bean.setIdNazione(rs.getLong("id_nazione"));
          bean.setCodIstatNazione(rs.getString("cod_istat_nazione"));
          bean.setCodBelfioreNazione(rs.getString("cod_belfiore_nazione"));
          bean.setDenomNazione(rs.getString("denom_nazione"));
          bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
          bean.setDataFineValidita(rs.getDate("data_fine_validita"));
          bean.setDtIdStato(rs.getLong("dt_id_stato"));
          bean.setDtIdStatoPrev(rs.getLong("dt_id_stato_prev"));
          bean.setDtIdStatoNext(rs.getLong("dt_id_stato_next"));
          bean.setCodIso2(rs.getString("cod_iso2"));
          
      }
   }

	public RowMapper<TipiRecapitoDTO> getTipoRecapitoRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new TipoRecapitoRowMapper();
	}
   /**
    * The type tipo recapito row mapper.
    */
   public static class TipoRecapitoRowMapper implements RowMapper<TipiRecapitoDTO> {

       /**
        * Instantiates a new Provincia row mapper.
        *
        * @throws SQLException the sql exception
        */
       public TipoRecapitoRowMapper() throws SQLException {
           // Instantiate class
       }

       /**
        * Implementations must implement this method to map each row of data
        * in the ResultSet. This method should not call {@code next()} on
        * the ResultSet; it is only supposed to map values of the current row.
        *
        * @param rs     the ResultSet to map (pre-initialized for the current row)
        * @param rowNum the number of the current row
        * @return the result object for the current row (may be {@code null})
        * @throws SQLException if a SQLException is encountered getting
        *                      column values (that is, there's no need to catch SQLException)
        */
       @Override
       public TipiRecapitoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
       	TipiRecapitoDTO bean = new TipiRecapitoDTO();
           populateBean(rs, bean);
           return bean;
       }

       private void populateBean(ResultSet rs, TipiRecapitoDTO bean) throws SQLException {
           bean.setIdTipoRecapito(rs.getLong("id_tipo_recapito"));
           bean.setCodTipoRecapito(rs.getString("cod_tipo_recapito"));
           bean.setDesTipoRecapito(rs.getString("des_tipo_recapito"));
       }
   }
   
	public RowMapper<FonteDTO> getFonteRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new FonteRowMapper();
	}
   /**
    * The type fonte row mapper.
    */
   public static class FonteRowMapper implements RowMapper<FonteDTO> {

       /**
        * Instantiates a new Fonte row mapper.
        *
        * @throws SQLException the sql exception
        */
       public FonteRowMapper() throws SQLException {
           // Instantiate class
       }

       /**
        * Implementations must implement this method to map each row of data
        * in the ResultSet. This method should not call {@code next()} on
        * the ResultSet; it is only supposed to map values of the current row.
        *
        * @param rs     the ResultSet to map (pre-initialized for the current row)
        * @param rowNum the number of the current row
        * @return the result object for the current row (may be {@code null})
        * @throws SQLException if a SQLException is encountered getting
        *                      column values (that is, there's no need to catch SQLException)
        */
       @Override
       public FonteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
       	FonteDTO bean = new FonteDTO();
           populateBean(rs, bean);
           return bean;
       }

       private void populateBean(ResultSet rs, FonteDTO bean) throws SQLException {
           bean.setIdFonte(rs.getLong("id_fonte"));
           bean.setCodFonte(rs.getString("cod_fonte"));
           bean.setDesFonte(rs.getString("des_fonte"));
           bean.setChiaveSottoscrizione(rs.getString("chiave_sottoscrizione"));
       }
   }
   

//	public List<Map<String, Object>> findSoggettoGruppoByIdGruppoSoggetto(Long IdGruppoSoggetto) {
//		Map<String, Object> map = new HashMap<>();	
//		map.put("id_gruppo_soggetto", IdGruppoSoggetto);
//		MapSqlParameterSource params = getParameterValue(map);
//		return template.queryForList(QUERY_LOAD_SOGGETTI_GRUPPO,params );
//	}
	
    

	@Override  
	public VerifyRiscossioneStatoDebDTO verifyRiscossioniSTDebitori(Long idAmbito, Long idOggetto, String indTipoOggetto,
			String idTipoOper, Long idRiscossione) throws SQLException {
		LOGGER.debug("[RiscossioneDAOImpl::verifyRiscossioniSTDebitori] BEGIN");
		VerifyRiscossioneStatoDebDTO verifyRiscSTDeb = null;
		try {
		    Utils utils = new Utils();
	    	if(utils.isLocalMod()){
	    		idAmbito = 1l;
	    	}
			Map<String, Object> map = new HashMap<>();	
			MapSqlParameterSource params;
			map.put("idAmbito", idAmbito);
			map.put("idOggetto", idOggetto);
			if (indTipoOggetto.equals("S")) {
				if (idTipoOper.equals("U")) {
					if (idRiscossione != null) {
						map.put("idRiscossione", idRiscossione);
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(
								QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_SOGGETTO + ESCLUDE_ID_RISCOSSIONE, params,
								getRowMapperVerifyRiscossione());
					} else {
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_SOGGETTO,
								params, getRowMapperVerifyRiscossione());
					}
				}
				if (idTipoOper.equals("D")) {
					if (idRiscossione != null) {
						map.put("idRiscossione", idRiscossione);
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_SOGGETTO + ESCLUDE_ID_RISCOSSIONE, params,
								getRowMapperVerifyRiscossione());
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) {
							verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_SOGGETTO + ESCLUDE_ID_RISCOSSIONE,
									params, getRowMapperVerifyStatoDeb());
						}
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) {
							verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_GRUPPO_A_RISCOSSIONE_SOGGETTO
											+ ESCLUDE_ID_RISCOSSIONE, params, getRowMapperVerifyRiscossione());
						}
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) {
							verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_GRUPPO_A_STATO_DEBITORIO_SOGGETTO
											+ ESCLUDE_ID_RISCOSSIONE, params, getRowMapperVerifyStatoDeb());
						}
					} else {
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_SOGGETTO,params, getRowMapperVerifyRiscossione());
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) 
							verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_SOGGETTO, params,
									getRowMapperVerifyStatoDeb());
						
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) 
							verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_GRUPPO_A_RISCOSSIONE_SOGGETTO, params,
									getRowMapperVerifyRiscossione());
				
							if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) 
						verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_GRUPPO_A_STATO_DEBITORIO_SOGGETTO, params,
								getRowMapperVerifyStatoDeb());

					    }
					}
				

			}
			// parte recapito alternativo
			if(indTipoOggetto.equals("A")) {
				if(idTipoOper.equals("U")) {
					if (idRiscossione != null) {
						map.put("idRiscossione", idRiscossione);
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(
								QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_RECAPITI + ESCLUDE_ID_RISCOSSIONE, params,
								getRowMapperVerifyRiscossione());
					
					} else {
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_RECAPITI,
								params, getRowMapperVerifyRiscossione());
						
					}
					
				}
				if(idTipoOper.equals("D")) {
					if (idRiscossione != null) {
						map.put("idRiscossione", idRiscossione);
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(
								QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_RECAPITI + ESCLUDE_ID_RISCOSSIONE, params,
								getRowMapperVerifyRiscossione());
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) {
							verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_RECAPITI + ESCLUDE_ID_RISCOSSIONE,
									params, getRowMapperVerifyStatoDeb());
						}
					} else {
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_RECAPITI,
								params, getRowMapperVerifyRiscossione());
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) {
							verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_RECAPITI ,
									params, getRowMapperVerifyStatoDeb());
						}
					}
				}
			}
			//parte gruppi 
			if(indTipoOggetto.equals("G")) {
				if(idTipoOper.equals("U")) {
					if (idRiscossione != null) {
						map.put("idRiscossione", idRiscossione);
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(
								QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_GRUPPO + ESCLUDE_ID_RISCOSSIONE, params,
								getRowMapperVerifyRiscossione());
						
					} else {
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_GRUPPO,
								params, getRowMapperVerifyRiscossione());
					}
					
					
					
				}
				if(idTipoOper.equals("D")) {
					if (idRiscossione != null) {
						map.put("idRiscossione", idRiscossione);
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(
								QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_GRUPPO + ESCLUDE_ID_RISCOSSIONE, params,
								getRowMapperVerifyRiscossione());
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) {
							verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_GRUPPO + ESCLUDE_ID_RISCOSSIONE,
									params, getRowMapperVerifyStatoDeb());
						}
					} else {
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_GRUPPO,
								params, getRowMapperVerifyRiscossione());
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) {
							verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_GRUPPO ,
									params, getRowMapperVerifyStatoDeb());
						}
					}
				}
			}
			// parte gruppi referente
			if (indTipoOggetto.equals("R")) {
				if (idTipoOper.equals("U")) {
					if (idRiscossione != null) {
						map.put("idRiscossione", idRiscossione);
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(
								QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_GRUPPO + ESCLUDE_ID_RISCOSSIONE, params,
								getRowMapperVerifyRiscossione());
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) {
							verifyRiscSTDeb = template.queryForObject(
									QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_GRUPPO_UPDATE_REFERENTE_SI
											+ ESCLUDE_ID_RISCOSSIONE,
									params, getRowMapperVerifyStatoDeb());
						}

					} else {
						params = getParameterValue(map);
						verifyRiscSTDeb = template.queryForObject(QUERY_CONTROLLO_ASSOCIAZIONE_A_RISCOSSIONE_GRUPPO,
								params, getRowMapperVerifyRiscossione());
						if (verifyRiscSTDeb.getNumRiscossioni() == 0 && verifyRiscSTDeb.getNumStatDeb() == 0) {
							verifyRiscSTDeb = template.queryForObject(
									QUERY_CONTROLLO_ASSOCIAZIONE_A_STATO_DEBITORIO_GRUPPO_UPDATE_REFERENTE_SI, params,
									getRowMapperVerifyStatoDeb());
						}
					}
				}
			}
			
			

		} catch (SQLException e) {
			LOGGER.error("[RiscossioneDAOImpl::verifyRiscossioniSTDebitori] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneDAOImpl::verifyRiscossioniSTDebitori] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("RiscossioneDAOImpl::verifyRiscossioniSTDebitori] END");
		}
		
    return verifyRiscSTDeb;
	}

	
	@Override
	public List<String> getCodiciUtenzaByIdStatoDebitorio(Long idStatoDebitorio) throws SQLException {
		LOGGER.debug("[RiscossioneDAOImpl::getCodiciUtenzaByIdStatoDebitorio] BEGIN");
		List<String> listCodUtenza = new ArrayList<String>();
		List<UtRimbDTO> utenzComp = new ArrayList<>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);
			utenzComp = template.query(QUERY_RISCOSSIONI_BY_ID_STATO_DEBITORIO, params, getUtRimbRowMapper()); 
			if(utenzComp != null) {
				for(int i=0; i<utenzComp.size(); i++) {
					listCodUtenza.add(utenzComp.get(i).getCodUtenza()+ "/"+utenzComp.get(i).getDescPeriodoPagamento());
				}
			}
			return listCodUtenza;
			
		} catch (SQLException e) {
			LOGGER.error("[RiscossioneDAOImpl::getCodiciUtenzaByIdStatoDebitorio] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneDAOImpl::getCodiciUtenzaByIdStatoDebitorio] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[RiscossioneDAOImpl::getCodiciUtenzaByIdStatoDebitorio] END");
		}
	}
	private RowMapper<UtRimbDTO> getUtRimbRowMapper() throws SQLException {

		return new UtRimbRowMapper();
	}
	/**
	 * The type UtRimb RowMapper
	 */
	public static class UtRimbRowMapper implements RowMapper<UtRimbDTO> {
		
		/**
		 * Instantiates a new UtRimb RowMapper
		 *
		 * @throws SQLException the sql exception
		 */
		public UtRimbRowMapper() throws SQLException {
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
		public UtRimbDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			UtRimbDTO bean = new UtRimbDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, UtRimbDTO bean) throws SQLException {
	
			bean.setCodUtenza(rs.getString("cod_riscossione"));
			bean.setDescPeriodoPagamento(rs.getString("desc_periodo_pagamento"));

		}
		
	
	}
	
	public  RowMapper<VerifyRiscossioneStatoDebDTO> getRowMapperVerifyRiscossione() throws SQLException {
		return new RowMapperVerifyRiscossione();
	}
	public  RowMapper<VerifyRiscossioneStatoDebDTO> getRowMapperVerifyStatoDeb() throws SQLException {
		return new RowMapperVerifyStatoDeb();
	}
	  /**
	    * The type Row Mapper Verify Riscossione .
	    */
	   public static class RowMapperVerifyRiscossione implements RowMapper<VerifyRiscossioneStatoDebDTO> {

	       /**
	        * Instantiates a new Row Mapper Verify Riscossione .
	        *
	        * @throws SQLException the sql exception
	        */
	       public RowMapperVerifyRiscossione() throws SQLException {
	           // Instantiate class
	       }

	       /**
	        * Implementations must implement this method to map each row of data
	        * in the ResultSet. This method should not call {@code next()} on
	        * the ResultSet; it is only supposed to map values of the current row.
	        *
	        * @param rs     the ResultSet to map (pre-initialized for the current row)
	        * @param rowNum the number of the current row
	        * @return the result object for the current row (may be {@code null})
	        * @throws SQLException if a SQLException is encountered getting
	        *                      column values (that is, there's no need to catch SQLException)
	        */
	       @Override
	       public VerifyRiscossioneStatoDebDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	   VerifyRiscossioneStatoDebDTO bean = new VerifyRiscossioneStatoDebDTO();
	           populateBean(rs, bean);
	           return bean;
	       }

	       private void populateBean(ResultSet rs, VerifyRiscossioneStatoDebDTO bean) throws SQLException {
	           bean.setNumRiscossioni(rs.getLong("num_riscossioni"));
	           bean.setNumStatDeb(0l);
	       }
	   }
	   /**
	    * The type Row Mapper Verify stato Deb .
	    */
	   public static class RowMapperVerifyStatoDeb implements RowMapper<VerifyRiscossioneStatoDebDTO> {

	       /**
	        * Instantiates a new Row Mapper Verify stato Deb .
	        *
	        * @throws SQLException the sql exception
	        */
	       public RowMapperVerifyStatoDeb() throws SQLException {
	           // Instantiate class
	       }

	       /**
	        * Implementations must implement this method to map each row of data
	        * in the ResultSet. This method should not call {@code next()} on
	        * the ResultSet; it is only supposed to map values of the current row.
	        *
	        * @param rs     the ResultSet to map (pre-initialized for the current row)
	        * @param rowNum the number of the current row
	        * @return the result object for the current row (may be {@code null})
	        * @throws SQLException if a SQLException is encountered getting
	        *                      column values (that is, there's no need to catch SQLException)
	        */
	       @Override
	       public VerifyRiscossioneStatoDebDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	   VerifyRiscossioneStatoDebDTO bean = new VerifyRiscossioneStatoDebDTO();
	           populateBean(rs, bean);
	           return bean;
	       }

	       private void populateBean(ResultSet rs, VerifyRiscossioneStatoDebDTO bean) throws SQLException {
	    	   bean.setNumRiscossioni(0l);
	           bean.setNumStatDeb(rs.getLong("num_statdeb"));
	       }
	   }

	@Override
	public Integer countRiscossioni(RiscossioneSearchDTO dto, Long idAmbito, String modalitaRicerca ) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Integer result = null;
		String ambito = "";
		Utils utils = new Utils();
    	if(utils.isLocalMod()){
    		ambito = AMBIENTE;
	    	result = riscAmbienteDaoImpl.countRiscossioni(dto, 1L,modalitaRicerca);
		}
		else {
			//TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			ambito = ambitoDTO.getCodAmbito();
			switch (ambito) {
			  case AMBIENTE:
			    result = riscAmbienteDaoImpl.countRiscossioni(dto, ambitoDTO.getIdAmbito(), modalitaRicerca);
			    break;
			  case OPERE_PUBBLICHE:
				//TO DO
			    break;
			  case ATTIVITA_ESTRATTIVE:
				//TO DO
			    break;
			  case TRIBUTI:
				result = riscTributiDaoImpl.countRiscossioni(dto, ambitoDTO.getIdAmbito(), modalitaRicerca);
			    break;
			}
		}	    
		LOGGER.debug(getClassFunctionEndInfo(className, methodName));	
		return result;
	}




	@Override
	public List<RiscossioneSearchResultDTO> searchRiscossioniDelegati(RiscossioneSearchDTO dto, Long idAmbito,
			String modalitaRicerca, Integer offset, Integer limit, String sort) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<RiscossioneSearchResultDTO> result = new ArrayList<RiscossioneSearchResultDTO>();
		String ambito = "";
		Utils utils = new Utils();
    	if(utils.isLocalMod()){
    		if(dto.getIdAmbito() != null && dto.getIdAmbito().equals(1L)) {
    	    	result = riscAmbienteDaoImpl.searchRiscossioniDelegati(dto, dto.getIdAmbito(), modalitaRicerca,  offset, limit, sort);

    		}else if(dto.getIdAmbito() != null && dto.getIdAmbito().equals(4L)) {
				result = riscTributiDaoImpl.searchRiscossioniDelegati(dto, dto.getIdAmbito(), modalitaRicerca, offset, limit, sort);

    		}else {
    	    	result = riscAmbienteDaoImpl.searchRiscossioniDelegati(dto, 1L,modalitaRicerca,  offset, limit, sort);
    		}
	    	
		}
		else {
			//TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			ambito = ambitoDTO.getCodAmbito();
			switch (ambito) {
			  case AMBIENTE:
			    result = riscAmbienteDaoImpl.searchRiscossioniDelegati(dto,idAmbito,modalitaRicerca, offset, limit, sort);
			    break;
			  case OPERE_PUBBLICHE:
				//TO DO
			    break;
			  case ATTIVITA_ESTRATTIVE:
				//TO DO
			    break;
			  case TRIBUTI:
				result = riscTributiDaoImpl.searchRiscossioniDelegati(dto,idAmbito, modalitaRicerca, offset, limit, sort);
			    break;
			}
		}	    
		LOGGER.debug(getClassFunctionEndInfo(className, methodName));	
		return result;
	}




	@Override
	public Integer countRiscossioniDelegati(RiscossioneSearchDTO dto, Long idAmbito,
			String modalitaRicerca) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Integer result = null;
		String ambito = "";
		Utils utils = new Utils();
    	if(utils.isLocalMod()){
    		if(dto.getIdAmbito() != null && dto.getIdAmbito().equals(1L)) {
    	    	result = riscAmbienteDaoImpl.countRiscossioniDelegati(dto, dto.getIdAmbito(), modalitaRicerca);

    		}else if(dto.getIdAmbito() != null && dto.getIdAmbito().equals(4L)) {
				result = riscTributiDaoImpl.countRiscossioniDelegati(dto, dto.getIdAmbito(), modalitaRicerca);

    		}else {
    	    	result = riscAmbienteDaoImpl.countRiscossioniDelegati(dto, 1L,modalitaRicerca);
    		}
		}
		else {
			//TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			ambito = ambitoDTO.getCodAmbito();
			switch (ambito) {
			  case AMBIENTE:
			    result = riscAmbienteDaoImpl.countRiscossioniDelegati(dto, ambitoDTO.getIdAmbito(), modalitaRicerca);
			    break;
			  case OPERE_PUBBLICHE:
				//TO DO
			    break;
			  case ATTIVITA_ESTRATTIVE:
				//TO DO
			    break;
			  case TRIBUTI:
				result = riscTributiDaoImpl.countRiscossioniDelegati(dto, ambitoDTO.getIdAmbito(), modalitaRicerca);
			    break;
			}
		}	    
		LOGGER.debug(getClassFunctionEndInfo(className, methodName));	
		return result;
	}




	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return getTableName();
	}




	@Override
	public Long updateRiscossioneStatoPratica(RiscossioneDTO riscossione, Long idAmbito)
			throws BusinessException, Exception {
		LOGGER.debug("[RiscossioneDAOImpl::updateRiscossioneStatoPratica] BEGIN");
		String ambito = "";
		Long idRiscossione = null;		
				AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
				ambito = ambitoDTO.getCodAmbito();
				switch (ambito) {
				  case AMBIENTE:
			    		idRiscossione = riscAmbienteDaoImpl.updateRiscossioneStatoPratica(riscossione);
				    break;
				  case OPERE_PUBBLICHE:
					//TO DO
				    break;
				  case ATTIVITA_ESTRATTIVE:
					//TO DO
				    break;
				  case TRIBUTI:
					 //TO DO
					  break;
				}
				    
			LOGGER.debug("[RiscossioneDAOImpl::updateRiscossioneStatoPratica] END");
	        return idRiscossione;
	}


}
