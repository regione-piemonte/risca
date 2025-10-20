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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RuoloDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente.RuoloAmbienteDAOImpl;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.RuoloDTO;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type StatoDebitorio dao.
 *
 * @author CSI PIEMONTE
 */
public class RuoloDAOImpl extends RiscaBeSrvGenericDAO<RuoloDTO> implements RuoloDAO {

	
	@Autowired
	private AmbitiDAO ambitiDAO;
	
	@Autowired
	private RuoloAmbienteDAOImpl ruoloAmbienteDAOImpl;
	
	private static final String AMBIENTE = "AMBIENTE";
	private static final String OPERE_PUBBLICHE = "Opere pubbliche";
	private static final String ATTIVITA_ESTRATTIVE = "Attivita estrattive";
	private static final String TRIBUTI = "TRIBUTI";
	
	@Override
	public RuoloDTO saveRuolo(RuoloDTO ruoloDTO, Long idAmbito) throws DAOException, Exception {
		LOGGER.debug("[RuoloDAOImpl::saveRuolo] BEGIN");
		String ambito = "";
		Utils utils = new Utils();
	    if(utils.isLocalMod()){
	    	ambito = AMBIENTE;
	    	ruoloDTO = ruoloAmbienteDAOImpl.saveRuolo(ruoloDTO);
		}else {
				LOGGER.debug("[RuoloDAOImpl::saveRuolo] verifica ambito");
				AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
				ambito = ambitoDTO.getCodAmbito();
				LOGGER.debug("[RuoloDAOImpl::saveRuolo] ambito: " + ambito);
				switch (ambito) {
				  case AMBIENTE:
					LOGGER.debug("[RuoloDAOImpl::saveRuolo] ambito: AMBIENTE");
					ruoloDTO = ruoloAmbienteDAOImpl.saveRuolo(ruoloDTO);
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
			}	   
			LOGGER.debug("[RuoloDAOImpl::saveRuolo] END");
	        return ruoloDTO;
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
	public RowMapper<RuoloDTO> getRowMapper() throws SQLException {		
		return new RuoloRowMapper();
	}



	/**
	 * The type Ruolo row mapper.
	 */
	public static class RuoloRowMapper implements RowMapper<RuoloDTO> {

		/**
		 * Instantiates a new Ruolo row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RuoloRowMapper() throws SQLException {		
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
		public RuoloDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RuoloDTO bean = new RuoloDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RuoloDTO bean) throws SQLException {
			bean.setIdRuolo(rs.getLong("id_ruolo"));
			bean.setIdAccertamento(rs.getLong("id_accertamento"));
			bean.setDataCreazioneRuolo(rs.getDate("data_creazione_ruolo"));
			bean.setNumBilAccCanone(rs.getString("num_bil_acc_canone"));
			bean.setNumBilAccInteressi(rs.getString("num_bil_acc_interessi"));
			bean.setNumBilAccSpese(rs.getString("num_bil_acc_spese"));
		    bean.setImportoCanoneMancante(rs.getBigDecimal("importo_canone_mancante"));
			bean.setImportoInteressiMancanti(rs.getBigDecimal("importo_interessi_mancanti"));
			bean.setImportoSpeseMancanti(rs.getBigDecimal("importo_spese_mancanti"));
			bean.setCodiceEnteCreditore(rs.getString("codice_ente_creditore"));
			bean.setTipoUfficio(rs.getString("tipo_ufficio"));
			bean.setCodiceUfficio(rs.getString("codice_ufficio"));
			bean.setAnnoScadenza(rs.getInt("anno_scadenza"));
			bean.setIdentifTipologiaAtto(rs.getInt("identif_tipologia_atto"));
			bean.setNumeroPartita(rs.getInt("numero_partita"));
			bean.setProgressivoPartita(rs.getInt("progressivo_partita"));
			bean.setCodiceTipoAtto(rs.getString("codice_tipo_atto"));
			bean.setMotivazioneIscrizione(rs.getString("motivazione_iscrizione"));
			bean.setpIdentPrenotRuolo(rs.getString("p_ident_prenot_ruolo"));
			bean.setNote(rs.getString("note"));					
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			
		}
	}
	
		

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	

}