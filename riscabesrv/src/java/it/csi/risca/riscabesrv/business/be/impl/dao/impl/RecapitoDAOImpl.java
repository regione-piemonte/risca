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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.NazioneDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ProvinciaExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.RegioneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IndirizziSpedizioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RecapitoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.FonteDTO;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscabesrv.dto.NazioniDTO;
import it.csi.risca.riscabesrv.dto.RecapitiDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipiInvioDTO;
import it.csi.risca.riscabesrv.dto.TipiRecapitoDTO;
import it.csi.risca.riscabesrv.dto.TipiSedeDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.ErrorMessages;

public class RecapitoDAOImpl extends RiscaBeSrvGenericDAO<RecapitiDTO> implements RecapitoDAO {

    @Autowired
    private IndirizziSpedizioneDAO indirizziSpedizioneDAO;
    
	private static final String QUERY_LOAD_RECAPITO_BY_ID_RECAPITO = "SELECT rrr.* FROM risca_r_recapito rrr " 
			+ "WHERE rrr.id_recapito = :idRecapito ";
	
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
	
    private static final String QUERY_LOAD_TIPI_SEDE = "SELECT rdts.* "
            + "FROM risca_d_tipo_sede rdts "
            + "where rdts.id_tipo_sede = :idTipoSede";
    

	private static final String QUERY_LOAD_RECAPITI_BY_ID_SOGGETTO = "SELECT rrr.* FROM risca_r_recapito rrr " 
			+ "WHERE rrr.id_soggetto = :idSoggetto "
			+ "AND rrr.data_cancellazione is null";
	
	private static final String QUERY_DELETE_RECAPITO_ALTERNATIVO_ID = "DELETE FROM risca_r_recapito rrr WHERE rrr.id_recapito = :idRecapito and rrr.id_tipo_recapito = 2";
	
	private static final String QUERY_DELETE_RECAPITO_SOGGETTO = "DELETE FROM risca_r_recapito rrr WHERE rrr.id_soggetto = :idSoggetto";
	
	@Override
	public RecapitiExtendedDTO getRecapitiExtendedByIdRecapito(long idRecapito) {
        RecapitiExtendedDTO recapitoPrincSoggetto = new RecapitiExtendedDTO();
     	 Map<String, Object> map = new HashMap<>();
     	LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdRecapito] BEGIN");
		try {
			map.put("idRecapito", idRecapito);
            MapSqlParameterSource params = getParameterValue(map);
            
			recapitoPrincSoggetto = template.queryForObject(QUERY_LOAD_RECAPITO_BY_ID_RECAPITO, params, getExtendedRowMapper());
		

    		TipiInvioDTO tipoInvio = new TipiInvioDTO();
    		FonteDTO fonte = new FonteDTO();
    		TipiRecapitoDTO tipiRecapito = new TipiRecapitoDTO();
    		ComuneExtendedDTO comune = new ComuneExtendedDTO();
    		NazioniDTO nazione = new NazioniDTO();
    		TipiSedeDTO tipiSede = new TipiSedeDTO();
    		if(recapitoPrincSoggetto != null) {
        		LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdRecapito] load indirizzi Spedizione");
        		List<IndirizzoSpedizioneDTO> listIndirizzoSpedizione = indirizziSpedizioneDAO.getIndirizzoSpedizioneByIdRecapitoAndIdGruppo(idRecapito, null);
    		    if(!listIndirizzoSpedizione.isEmpty())
    		    	recapitoPrincSoggetto.setIndirizziSpedizione(listIndirizzoSpedizione);
    		    
        		map.put("idTipoInvio", recapitoPrincSoggetto.getIdTipoInvio());
        		map.put("idTipoRecapito",recapitoPrincSoggetto.getIdTipoRecapito());
        		map.put("idComune", recapitoPrincSoggetto.getIdComuneRecapito());
        		map.put("idNazione",recapitoPrincSoggetto.getIdNazioneRecapito());
        		map.put("idFonte",recapitoPrincSoggetto.getIdFonte());
        		map.put("idTipoSede", recapitoPrincSoggetto.getIdTipoSede());
        		params = getParameterValue(map);
        		try {
					tipoInvio = template.queryForObject(QUERY_LOAD_TIPI_INVIO, params, getTipiInvioRowMapper());
        		} catch (Exception e) {
					LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdRecapito] Nessun tipo invio presente o errore nell'accesso al db");
				}
        		
				try {
					tipiRecapito = template.queryForObject(QUERY_LOAD_TIPI_RECAPITO, params, getTipoRecapitoRowMapper());
				} catch (Exception e) {
					LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdRecapito] Nessun tipo recapito presente o errore nell'accesso al db");
				}
				try {
					comune = template.queryForObject(QUERY_LOAD_COMUNE, params, getComuneRowMapper());
				} catch (Exception e) {
					LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdRecapito] Nessun comune presente o errore nell'accesso al db");
				}
				try {
					nazione = template.queryForObject(QUERY_LOAD_NAZIONE, params, getNazioneRowMapper());
				} catch (Exception e) {
					LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdRecapito] Nessuna nazione presente o errore nell'accesso al db");
				}
				try {
					fonte = template.queryForObject(QUERY_LOAD_FONTE, params, getFonteRowMapper());
					recapitoPrincSoggetto.setFonte(fonte);
				} catch (Exception e) {
					LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdRecapito] Nessuna fonte presente o errore nell'accesso al db");
				}
				try {
					tipiSede = template.queryForObject(QUERY_LOAD_TIPI_SEDE, params, getTipiSedeRowMapper());
					recapitoPrincSoggetto.setTipoSede(tipiSede);
				} catch (Exception e) {
					LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdRecapito] Nessun tipo sede presente o errore nell'accesso al db");
				}
    		
    			
				recapitoPrincSoggetto.setComuneRecapito(comune);
				recapitoPrincSoggetto.setNazioneRecapito(nazione);
				recapitoPrincSoggetto.setTipoRecapito(tipiRecapito);
				recapitoPrincSoggetto.setTipoInvio(tipoInvio);
    		}
		} catch (Exception e) {
			LOGGER.error("[RecapitoDAOImpl::getRecapitiExtendedByIdRecapito]: Errore " +e);
		}
     	LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdRecapito] END");
		return recapitoPrincSoggetto;
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<RecapitiDTO> getRowMapper() throws SQLException {
		return new RecapitiRowMapper();
	}

	@Override
	public RowMapper<RecapitiExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new RecapitiExtendedRowMapper();
	}


    /**
     * The type recapiti Extended row mapper.
     */
    public static class RecapitiRowMapper implements RowMapper<RecapitiDTO> {

        /**
         * Instantiates a new recapiti Extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public RecapitiRowMapper() throws SQLException {
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
        public RecapitiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
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

    /**
     * The type recapiti Extended row mapper.
     */
    public static class RecapitiExtendedRowMapper implements RowMapper<RecapitiExtendedDTO> {

        /**
         * Instantiates a new recapiti Extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public RecapitiExtendedRowMapper() throws SQLException {
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
        public RecapitiExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	RecapitiExtendedDTO bean = new RecapitiExtendedDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, RecapitiExtendedDTO bean) throws SQLException {
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
	
	public RowMapper<TipiSedeDTO> getTipiSedeRowMapper() throws SQLException {
		 return new TipoSedeRowMapper();
	}

    /**
     * The type Tipo sede row mapper.
     */
    public static class TipoSedeRowMapper implements RowMapper<TipiSedeDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoSedeRowMapper() throws SQLException {
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
        public TipiSedeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiSedeDTO bean = new TipiSedeDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiSedeDTO bean) throws SQLException {
            bean.setIdTipoSede(rs.getLong("id_tipo_sede"));
            bean.setCodTipoSede(rs.getString("cod_tipo_sede"));
            bean.setDesTipoSede(rs.getString("des_tipo_sede"));
            bean.setOrdinaTipoSede(rs.getLong("ordina_tipo_sede"));
            bean.setIndDefault(rs.getString("ind_default"));
        }
    }


	@Override
	public RecapitiExtendedDTO createRecapito(RecapitiExtendedDTO recapito) throws DAOException, SystemException {
		try {
			StringBuilder sql = new StringBuilder();	
			BigDecimal seqRiscaRRecapito = null;
			Map<String, Object> paramMapPk = new HashMap<String, Object>();
			sql.append(" SELECT nextval('seq_risca_r_recapito')");
			seqRiscaRRecapito = template.queryForObject(sql.toString(), paramMapPk, BigDecimal.class);
			
			LOGGER.debug("[RecapitoDAOImpl::INSERT] seqRiscaRRecapito = " + seqRiscaRRecapito);
			
			String queryInsert = "INSERT INTO risca_r_recapito (id_recapito, id_soggetto, id_tipo_recapito, id_tipo_invio, id_comune_recapito, id_nazione_recapito, id_fonte, id_fonte_origine, cod_recapito, " + 
		            " cod_recapito_fonte, indirizzo, num_civico, email, pec, telefono, presso, citta_estera_recapito, cap_recapito, des_localita, gest_data_ins, gest_attore_ins, " + 
		    		" gest_data_upd, gest_attore_upd, gest_uid, cellulare, id_tipo_sede, data_aggiornamento, data_cancellazione) " + 
		    		" VALUES(:idRecapito, :idSoggetto, :idTipoRecapito, :idTipoInvio, :idComuneRecapito, :idNazioneRecapito, :idFonte, :idFonteOrigine, :codRecapito, :codRecapitoFonte, " + 
		    		" :indirizzo, :numCivico, :email, :pec, :telefono, :presso, :cittaEsteraRecapito, :capRecapito, :desLocalita, current_date, :gestAttoreIns,  " + 
		    		" current_date , :gestAttoreUpd, :gestUid, :cellulare, :idTipoSede, :dataAggiornamento, :dataCancellazione); ";
			

			LOGGER.debug("[RecapitoDaoImpl - createRecapito] query =" + queryInsert.toString());
			LOGGER.debug("[RecapitoDaoImpl - createRecapito] param  recapito = " + recapito);

			Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("idRecapito",seqRiscaRRecapito);
			paramMap.put("idSoggetto", recapito.getIdSoggetto());
			paramMap.put("idTipoRecapito" , recapito.getTipoRecapito().getIdTipoRecapito());
			 if(recapito.getTipoInvio() != null) {
			    paramMap.put("idTipoInvio", recapito.getTipoInvio().getIdTipoInvio());
			 }else {
				 paramMap.put("idTipoInvio", null);
			 }
			 if(recapito.getComuneRecapito() != null) {
			paramMap.put("idComuneRecapito", recapito.getComuneRecapito().getIdComune());
		     }else {
		    	 paramMap.put("idComuneRecapito", null);
		     }
			 if(recapito.getNazioneRecapito() != null) {
			paramMap.put("idNazioneRecapito", recapito.getNazioneRecapito().getIdNazione());
			 }
			paramMap.put("idFonte",  recapito.getIdFonte() != null ? recapito.getIdFonte() :  1L);
			paramMap.put("idFonteOrigine", recapito.getIdFonteOrigine() != null ? recapito.getIdFonteOrigine() : 1L);
			paramMap.put("codRecapito", "RRI" + seqRiscaRRecapito);
			paramMap.put("codRecapitoFonte", recapito.getCodRecapitoFonte());
			paramMap.put("indirizzo", recapito.getIndirizzo());
			paramMap.put("numCivico", recapito.getNumCivico());
			paramMap.put("email", recapito.getEmail());
			paramMap.put("pec", recapito.getPec());
			paramMap.put("telefono", recapito.getTelefono());
			paramMap.put("presso", recapito.getPresso());
			paramMap.put("cittaEsteraRecapito", recapito.getCittaEsteraRecapito());
			paramMap.put("capRecapito", recapito.getCapRecapito());
			paramMap.put("desLocalita", recapito.getDesLocalita());

			paramMap.put("gestAttoreIns", recapito.getGestAttoreIns());
			paramMap.put("gestAttoreUpd", recapito.getGestAttoreUpd());
			 Calendar cal = Calendar.getInstance();
	         Date now = cal.getTime();
			
			paramMap.put("gestUid", generateGestUID(recapito.getGestAttoreIns() + recapito.getGestAttoreUpd() + now));
			paramMap.put("cellulare", recapito.getCellulare());
			 if(recapito.getTipoSede() != null) {
			    paramMap.put("idTipoSede", recapito.getTipoSede().getIdTipoSede());
			 }
			 
			 if(recapito.getDataCancellazione() != null) {
					if(!recapito.getDataCancellazione().equals("")) {
						SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
						Date dataCancellazione = formatter.parse(recapito.getDataCancellazione());
						paramMap.put("dataCancellazione", dataCancellazione);
					}
					else {
						paramMap.put("dataCancellazione", null);
					}
				} 
				else {
					paramMap.put("dataCancellazione", null);
				}
			 
			
			if(recapito.getDataAggiornamento()!=null && !recapito.getDataAggiornamento().equals("")) {
				paramMap.put("dataAggiornamento", now);
			}else {
				paramMap.put("dataAggiornamento", null);
			}

			 template.update(queryInsert, paramMap);

			recapito.setIdRecapito(seqRiscaRRecapito.longValue());			

			LOGGER.debug("[RecapitoDaoImpl::createRecapito]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOGGER.debug("[RecapitoDaoImpl::createRecapito]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DAOException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA);
			}
			ex.printStackTrace();
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex) {
			LOGGER.error(
					"[RecapitoDaoImpl::createRecapito] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOGGER.debug("[RecapitoDaoImpl::createRecapito] END ");
		}
		return recapito;
		
		
	}

	@Override
	public long countRecapitoPrincipaleByIdSoggetto(Long idSoggetto) throws DAOException, SystemException {
		LOGGER.debug("[RecapitoDAOImpl::countRecapitoPrincipale] START");

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		sql.append("select count(*) from risca_r_recapito WHERE id_soggetto = :ID_SOGGETTO and id_tipo_recapito = 1 ");		

		long conteggio = 0;

		paramMap.addValue("ID_SOGGETTO", idSoggetto);
		
		try {
			
			conteggio = template.queryForObject(sql.toString(), paramMap, Long.class);

		} catch (RuntimeException ex) {
			LOGGER.error("[RecapitoDAOImpl::countRecapitoPrincipale] esecuzione query", ex);
			throw new DAOException("Query failed");
		} finally {
			LOGGER.debug("[RecapitoDAOImpl::countRecapitoPrincipale] END");
		}
		return conteggio;
	}

	@Override
	public RecapitiExtendedDTO updateRecapito(RecapitiExtendedDTO recapito) throws DAOException, SystemException {
		try {
			StringBuilder sql = new StringBuilder();	
			BigDecimal seqRiscaRRecapito = null;
			Map<String, Object> paramMapPk = new HashMap<String, Object>();
			sql.append(" SELECT nextval('seq_risca_r_recapito')");
			seqRiscaRRecapito = template.queryForObject(sql.toString(), paramMapPk, BigDecimal.class);
			
			LOGGER.debug("[RecapitoDAOImpl::UPDATE] seqRiscaRRecapito = " + seqRiscaRRecapito);
			
			String queryUpdate = "UPDATE risca_r_recapito SET id_soggetto = :idSoggetto, id_tipo_recapito = :idTipoRecapito, id_tipo_invio = :idTipoInvio, id_comune_recapito = :idComuneRecapito, id_nazione_recapito = :idNazioneRecapito, id_fonte = :idFonte, id_fonte_origine = :idFonteOrigine, " + 
		    		"cod_recapito = :codRecapito, cod_recapito_fonte = :codRecapitoFonte, indirizzo = :indirizzo, num_civico = :numCivico, " + 
		    		"email = :email, pec = :pec, telefono = :telefono, presso = :presso, " + 
		    		"citta_estera_recapito = :cittaEsteraRecapito, cap_recapito = :capRecapito, des_localita = :desLocalita,  " + 
		    		"gest_data_upd = :gestDataUpd, gest_attore_upd = :gestAttoreUpd, gest_uid = :gestUid, cellulare = :cellulare, id_tipo_sede = :idTipoSede, data_aggiornamento = :dataAggiornamento, data_cancellazione = :dataCancellazione "+
		    	    "WHERE id_recapito = :idRecapito;";
			

			LOGGER.debug("[RecapitoDaoImpl - updateRecapito] query =" + queryUpdate.toString());
			LOGGER.debug("[RecapitoDaoImpl - updateRecapito] param  recapito = " + recapito);

			Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("idRecapito",recapito.getIdRecapito() != null ? recapito.getIdRecapito() : seqRiscaRRecapito);
			paramMap.put("idSoggetto", recapito.getIdSoggetto());
			paramMap.put("idTipoRecapito" , recapito.getTipoRecapito().getIdTipoRecapito());
			 if(recapito.getTipoInvio() != null) {
			    paramMap.put("idTipoInvio", recapito.getTipoInvio().getIdTipoInvio());
			 }else {
				 paramMap.put("idTipoInvio", null);
			 }
			 if(recapito.getComuneRecapito() != null) {
			paramMap.put("idComuneRecapito", recapito.getComuneRecapito().getIdComune());
		     }else {
		    	 paramMap.put("idComuneRecapito", null);
		     }
			 if(recapito.getNazioneRecapito() != null) {
			paramMap.put("idNazioneRecapito", recapito.getNazioneRecapito().getIdNazione());
			 }
			paramMap.put("idFonte",  recapito.getIdFonte() != null ? recapito.getIdFonte() :  1L);
			paramMap.put("idFonteOrigine", recapito.getIdFonteOrigine() != null ? recapito.getIdFonteOrigine() : 1L);
			paramMap.put("codRecapito", "RRI" + seqRiscaRRecapito);
			paramMap.put("codRecapitoFonte", recapito.getCodRecapitoFonte());
			paramMap.put("indirizzo", recapito.getIndirizzo());
			paramMap.put("numCivico", recapito.getNumCivico());
			paramMap.put("email", recapito.getEmail());
			paramMap.put("pec", recapito.getPec());
			paramMap.put("telefono", recapito.getTelefono());
			paramMap.put("presso", recapito.getPresso());
			paramMap.put("cittaEsteraRecapito", recapito.getCittaEsteraRecapito());
			paramMap.put("capRecapito", recapito.getCapRecapito());
			paramMap.put("desLocalita", recapito.getDesLocalita());
			
			Calendar cal = Calendar.getInstance();
	        Date now = cal.getTime();
			paramMap.put("gestDataUpd", now);
			paramMap.put("gestAttoreUpd", recapito.getGestAttoreUpd());
			
			
			paramMap.put("gestUid", generateGestUID(recapito.getGestAttoreIns() + recapito.getGestAttoreUpd() + now));
			paramMap.put("cellulare", recapito.getCellulare());
			 if(recapito.getTipoSede() != null) {
			    paramMap.put("idTipoSede", recapito.getTipoSede().getIdTipoSede());
			 }
			 
			paramMap.put("dataAggiornamento", now);
			 
			 if(recapito.getDataCancellazione() != null) {
					if(!recapito.getDataCancellazione().equals("")) {
						SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
						Date dataCancellazione = formatter.parse(recapito.getDataCancellazione());
						paramMap.put("dataCancellazione", dataCancellazione);
					}
					else {
						paramMap.put("dataCancellazione", null);
					}
				} 
				else {
					paramMap.put("dataCancellazione", null);
				}

			 template.update(queryUpdate, paramMap);

			LOGGER.debug("[RecapitoDaoImpl::updateRecapito]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOGGER.debug("[RecapitoDaoImpl::updateRecapito]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DAOException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA);
			}
			ex.printStackTrace();
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex) {
			LOGGER.error(
					"[RecapitoDaoImpl::updateRecapito] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOGGER.debug("[RecapitoDaoImpl::updateRecapito] END ");
		}
		return recapito;
	}
	
	@Override
	public long countRecapitoByIdSoggettoAndIdRecapito(Long idSoggetto, Long idRecapito) throws DAOException, SystemException {
		LOGGER.debug("[RecapitoDAOImpl::countRecapitoByIdSoggettoAndIdRecapito] START");

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		sql.append("select count(*) from risca_r_recapito WHERE id_soggetto = :ID_SOGGETTO and id_recapito = :ID_RECAPITO ");		

		long conteggio = 0;

		paramMap.addValue("ID_SOGGETTO", idSoggetto);
		paramMap.addValue("ID_RECAPITO", idRecapito);
		
		try {
			
			conteggio = template.queryForObject(sql.toString(), paramMap, Long.class);

		} catch (RuntimeException ex) {
			LOGGER.error("[RecapitoDAOImpl::countRecapitoByIdSoggettoAndIdRecapito] esecuzione query", ex);
			throw new DAOException("Query failed");
		} finally {
			LOGGER.debug("[RecapitoDAOImpl::countRecapitoByIdSoggettoAndIdRecapito] END");
		}
		return conteggio;
	}

	@Override
	public List<RecapitiExtendedDTO> getRecapitiExtendedByIdSoggetto(long idSoggetto)
			throws DAOException {
    	LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdSoggetto] BEGIN");
   	    Map<String, Object> map = new HashMap<>();
    	List<RecapitiExtendedDTO> listRecapitiByIdSoggetto = new ArrayList<>();
		try {
			map.put("idSoggetto", idSoggetto);
            MapSqlParameterSource params = getParameterValue(map);
            List<RecapitiExtendedDTO> listRecapiti = template.query(QUERY_LOAD_RECAPITI_BY_ID_SOGGETTO, params, getExtendedRowMapper());
            if (listRecapiti != null) {
    			for (RecapitiExtendedDTO recapitiExtendedDTO : listRecapiti) {
    				listRecapitiByIdSoggetto.add(getRecapitiExtendedByIdRecapito(recapitiExtendedDTO.getIdRecapito())) ;
				}
    		}
           
		} catch (SQLException e) {
			LOGGER.error("[RecapitoDAOImpl::getRecapitiExtendedByIdSoggetto] esecuzione query", e);

				throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RecapitoDAOImpl::getRecapitiExtendedByIdSoggetto] END");
		}
		return listRecapitiByIdSoggetto;
	 
	}

	@Override
	public void deleteRecapitiByIdSoggetto(long idSoggetto) throws DAOException {
	 	LOGGER.debug("[RecapitoDAOImpl::deleteRecapitiByIdSoggetto] BEGIN");
        Map<String, Object> map = new HashMap<>();
		try {
			map.put("idSoggetto", idSoggetto);
            MapSqlParameterSource params = getParameterValue(map);
        	template.update(getQuery(QUERY_DELETE_RECAPITO_SOGGETTO, null, null), params);
           
		} finally {
			LOGGER.debug("[RecapitoDAOImpl::deleteRecapitiByIdSoggetto] END");
		}
		
	}

	@Override
	public void deleteRecapitoAlternativoByIdRecapito(long idRecapito) throws DAOException {
	 	LOGGER.debug("[RecapitoDAOImpl::deleteRecapitoAlternativoByIdRecapito] BEGIN");
        Map<String, Object> map = new HashMap<>();
		try {
			map.put("idRecapito", idRecapito);
            MapSqlParameterSource params = getParameterValue(map);
			template.update(getQuery(QUERY_DELETE_RECAPITO_ALTERNATIVO_ID, null, null), params);
           
		} finally {
			LOGGER.debug("[RecapitoDAOImpl::deleteRecapitoAlternativoByIdRecapito] END");
		}
		
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}
 
	
	
}
